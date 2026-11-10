package co.bluerebhirt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {
    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService usuarios(JdbcTemplate jdbc) {
        return correo -> jdbc.query("SELECT correo, contrasena_hash, rol, activo FROM usuarios WHERE correo = ?",
                (rs, numero) -> User.withUsername(rs.getString("correo"))
                        .password(rs.getString("contrasena_hash"))
                        .roles(rs.getString("rol").toUpperCase(java.util.Locale.ROOT))
                        .disabled(!rs.getBoolean("activo")).build(), correo)
                .stream().findFirst().orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    SecurityFilterChain seguridad(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(reglas -> reglas
                .requestMatchers("/", "/entrar", "/registro", "/guia", "/css/**", "/img/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/inicio", "/cultivos/**", "/mediciones/**", "/historial").hasRole("AGRICULTOR")
                .anyRequest().authenticated())
                .formLogin(formulario -> formulario.loginPage("/entrar").successHandler((peticion, respuesta, autenticacion) -> {
                    boolean admin = autenticacion.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_ADMINISTRADOR"));
                    respuesta.sendRedirect(admin ? "/admin" : "/inicio");
                }).permitAll())
                .logout(salida -> salida.logoutUrl("/salir").logoutSuccessUrl("/"))
                .build();
    }
}
