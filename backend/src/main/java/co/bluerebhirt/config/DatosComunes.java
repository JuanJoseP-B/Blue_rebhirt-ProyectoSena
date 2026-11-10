package co.bluerebhirt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;

@ControllerAdvice
public class DatosComunes {
    private final JdbcTemplate jdbc;
    public DatosComunes(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @Value("${app.nombre}") private String nombre;
    @ModelAttribute("nombreApp") public String nombreApp() { return nombre; }
    @ModelAttribute("menuTieneCultivos")
    public boolean menuTieneCultivos(Authentication usuario) {
        if (usuario == null || usuario.getAuthorities().stream().noneMatch(rol -> rol.getAuthority().equals("ROLE_AGRICULTOR"))) return false;
        Integer cantidad = jdbc.queryForObject("SELECT COUNT(*) FROM cultivos c JOIN usuarios u ON u.id=c.usuario_id WHERE u.correo=?", Integer.class, usuario.getName());
        return cantidad != null && cantidad > 0;
    }
}
