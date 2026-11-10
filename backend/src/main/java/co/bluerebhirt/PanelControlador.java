package co.bluerebhirt;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelControlador {
    private final JdbcTemplate jdbc;
    public PanelControlador(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @GetMapping("/inicio")
    public String inicio(Principal usuario, Model modelo) {
        String nombre = jdbc.queryForObject("SELECT nombre FROM usuarios WHERE correo = ?", String.class, usuario.getName());
        Integer cultivos = jdbc.queryForObject("SELECT COUNT(*) FROM cultivos c JOIN usuarios u ON c.usuario_id = u.id WHERE u.correo = ?", Integer.class, usuario.getName());
        var ultimas = jdbc.queryForList("SELECT m.id, m.ph, m.estado, m.medido_en, c.nombre AS cultivo FROM mediciones m JOIN cultivos c ON c.id=m.cultivo_id JOIN usuarios u ON u.id=c.usuario_id WHERE u.correo=? ORDER BY m.medido_en DESC, m.id DESC LIMIT 1", usuario.getName());
        if (!ultimas.isEmpty()) modelo.addAttribute("tiempoUltima", Fechas.transcurrido(((java.sql.Timestamp) ultimas.get(0).get("medido_en")).toLocalDateTime().toLocalDate()));
        modelo.addAttribute("ultima", ultimas.isEmpty() ? null : ultimas.get(0));
        modelo.addAttribute("nombre", nombre);
        modelo.addAttribute("cultivos", cultivos);
        modelo.addAttribute("fecha", LocalDate.now(java.time.ZoneId.of("America/Bogota")).format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", new Locale("es", "CO"))));
        return "panel";
    }
}
