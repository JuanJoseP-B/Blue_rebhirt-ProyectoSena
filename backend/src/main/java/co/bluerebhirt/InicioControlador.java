package co.bluerebhirt;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControlador {
    @GetMapping("/")
    public String inicio(Authentication usuario) {
        if (usuario == null || !usuario.isAuthenticated() || usuario.getPrincipal().equals("anonymousUser")) return "index";
        return usuario.getAuthorities().stream().anyMatch(rol -> rol.getAuthority().equals("ROLE_ADMINISTRADOR")) ? "redirect:/admin" : "redirect:/inicio";
    }
}
