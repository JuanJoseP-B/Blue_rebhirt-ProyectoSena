package co.bluerebhirt.admin;

import java.nio.charset.StandardCharsets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
public class AdminListasControlador {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    public AdminListasControlador(JdbcTemplate jdbc, PasswordEncoder encoder) { this.jdbc=jdbc; this.encoder=encoder; }
    @GetMapping("/admin/usuarios")
    public String usuarios(Model modelo) {
        modelo.addAttribute("usuarios",jdbc.queryForList("SELECT id,nombre,correo,rol,activo FROM usuarios ORDER BY nombre"));
        return "admin-usuarios";
    }
    @PostMapping("/admin/usuarios/{id}/clave")
    public String cambiarClave(@PathVariable long id, @RequestParam String clave, Model modelo) {
        if (clave.length()<6 || clave.getBytes(StandardCharsets.UTF_8).length>72) { modelo.addAttribute("error","La contraseña debe tener al menos 6 caracteres y máximo 72 bytes."); return usuarios(modelo); }
        int cambiados=jdbc.update("UPDATE usuarios SET contrasena_hash=? WHERE id=? AND rol=?",encoder.encode(clave),id,"agricultor");
        if (cambiados==0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "redirect:/admin/usuarios?guardado";
    }
    @GetMapping("/admin/cultivos")
    public String cultivos(Model modelo) {
        modelo.addAttribute("titulo","Cultivos");
        modelo.addAttribute("filas",jdbc.queryForList("SELECT c.nombre AS principal, u.nombre AS detalle, m.estado FROM cultivos c JOIN usuarios u ON u.id=c.usuario_id LEFT JOIN mediciones m ON m.id=(SELECT ultima.id FROM mediciones ultima WHERE ultima.cultivo_id=c.id ORDER BY ultima.medido_en DESC, ultima.id DESC LIMIT 1) ORDER BY c.nombre"));
        return "admin-lista";
    }
    @GetMapping("/admin/mediciones")
    public String mediciones(Model modelo) {
        modelo.addAttribute("titulo","Mediciones recientes");
        var filas = jdbc.queryForList("SELECT c.nombre AS principal, m.medido_en, m.ph, m.estado FROM mediciones m JOIN cultivos c ON c.id=m.cultivo_id ORDER BY m.medido_en DESC,m.id DESC LIMIT 30");
        var formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (var fila : filas) {
            var fecha = ((java.sql.Timestamp) fila.get("medido_en")).toLocalDateTime();
            fila.put("detalle", fecha.format(formato) + " · pH " + fila.get("ph"));
        }
        modelo.addAttribute("filas", filas);
        return "admin-lista";
    }
}
