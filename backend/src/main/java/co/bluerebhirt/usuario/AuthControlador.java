package co.bluerebhirt.usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthControlador {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    public AuthControlador(JdbcTemplate jdbc, PasswordEncoder encoder) { this.jdbc = jdbc; this.encoder = encoder; }

    @GetMapping("/entrar")
    public String entrar() { return "entrar"; }

    @GetMapping("/registro")
    public String registro(Model modelo) { modelo.addAttribute("registroForm", new RegistroForm()); return "registro"; }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute RegistroForm form, BindingResult errores, HttpServletRequest peticion) throws Exception {
        if (form.getClave() != null && form.getClave().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) errores.rejectValue("clave", "longitud", "La contraseña es demasiado larga.");
        if (errores.hasErrors()) return "registro";
        try {
            jdbc.update("INSERT INTO usuarios (nombre, correo, contrasena_hash, rol) VALUES (?, ?, ?, ?)",
                    form.getNombre().trim(), form.getCorreo().trim().toLowerCase(java.util.Locale.ROOT), encoder.encode(form.getClave()), "agricultor");
        } catch (DuplicateKeyException repetido) {
            errores.rejectValue("correo", "repetido", "Ese correo ya tiene una cuenta.");
            return "registro";
        }
        peticion.login(form.getCorreo().trim().toLowerCase(java.util.Locale.ROOT), form.getClave());
        return "redirect:/cultivos";
    }
}
