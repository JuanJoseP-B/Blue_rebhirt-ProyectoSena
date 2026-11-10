package co.bluerebhirt.cultivo;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import co.bluerebhirt.Fechas;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CultivoControlador {
    private final JdbcTemplate jdbc;
    public CultivoControlador(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    private void cargar(Principal usuario, Model modelo) {
        var cultivos = jdbc.queryForList("SELECT c.nombre, c.lugar, c.fecha_siembra, (SELECT m.ph FROM mediciones m WHERE m.cultivo_id=c.id ORDER BY m.medido_en DESC,m.id DESC LIMIT 1) AS ultimo_ph, (SELECT m.estado FROM mediciones m WHERE m.cultivo_id=c.id ORDER BY m.medido_en DESC,m.id DESC LIMIT 1) AS ultimo_estado, (SELECT m.medido_en FROM mediciones m WHERE m.cultivo_id=c.id ORDER BY m.medido_en DESC,m.id DESC LIMIT 1) AS ultima_fecha FROM cultivos c JOIN usuarios u ON u.id = c.usuario_id WHERE u.correo = ? ORDER BY c.creado_en DESC", usuario.getName());
        for (var cultivo : cultivos) {
            if (cultivo.get("fecha_siembra") != null) cultivo.put("siembra_texto", Fechas.fecha(((java.sql.Date) cultivo.get("fecha_siembra")).toLocalDate()));
            if (cultivo.get("ultima_fecha") != null) cultivo.put("tiempo_ultima", Fechas.transcurrido(((java.sql.Timestamp) cultivo.get("ultima_fecha")).toLocalDateTime().toLocalDate()));
        }
        modelo.addAttribute("cultivos", cultivos);
    }
    @GetMapping("/cultivos")
    public String listar(Principal usuario, Model modelo) { cargar(usuario, modelo); return "cultivos"; }
    @PostMapping("/cultivos")
    public String guardar(@RequestParam Map<String, String> datos, Principal usuario, Model modelo) {
        String nombre = datos.getOrDefault("nombre", "").trim();
        String lugar = datos.getOrDefault("lugar", "").trim();
        String fecha = datos.getOrDefault("fecha", "").trim();
        String error = null;
        LocalDate fechaSiembra = null;
        if (nombre.isEmpty() || nombre.length() > 80) error = "Escribe el nombre del cultivo (máximo 80 caracteres).";
        else if (lugar.length() > 120) error = "La referencia debe tener máximo 120 caracteres.";
        else if (!fecha.isEmpty()) {
            try { fechaSiembra = LocalDate.parse(fecha); if (fechaSiembra.isAfter(LocalDate.now(java.time.ZoneId.of("America/Bogota")))) error = "La fecha de siembra no puede ser futura."; }
            catch (java.time.format.DateTimeParseException invalida) { error = "Escribe una fecha válida."; }
        }
        if (error == null) {
            try {
                jdbc.update("INSERT INTO cultivos (usuario_id, nombre, lugar, fecha_siembra) SELECT id, ?, ?, ? FROM usuarios WHERE correo = ?", nombre, lugar.isEmpty() ? null : lugar, fechaSiembra, usuario.getName());
                return "redirect:/cultivos";
            } catch (DuplicateKeyException repetido) { error = "Ya tienes un cultivo con ese nombre."; }
        }
        modelo.addAttribute("error", error); modelo.addAttribute("datos", datos); cargar(usuario, modelo); return "cultivos";
    }
}
