package co.bluerebhirt.config;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdministradorInicial {
    @Bean
    @ConditionalOnProperty(name = "app.admin-inicial-correo")
    CommandLineRunner crearAdministradorInicial(JdbcTemplate jdbc, PasswordEncoder encoder,
            org.springframework.core.env.Environment entorno) {
        return args -> {
            Integer cantidad = jdbc.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
            if (cantidad == null || cantidad != 0) return;
            String correo = entorno.getProperty("app.admin-inicial-correo", "").trim().toLowerCase(Locale.ROOT);
            String clave = entorno.getProperty("app.admin-inicial-clave", "");
            if (correo.length() > 120 || !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                    || clave.length() < 12 || clave.getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new IllegalStateException("Configura un correo válido y una contraseña inicial de 12 caracteres como mínimo y hasta 72 bytes.");
            }
            jdbc.update("INSERT INTO usuarios (nombre, correo, contrasena_hash, rol) VALUES (?, ?, ?, ?)",
                    "Administrador", correo, encoder.encode(clave), "administrador");
        };
    }
}
