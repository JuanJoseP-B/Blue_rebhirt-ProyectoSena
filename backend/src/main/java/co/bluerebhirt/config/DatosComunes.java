package co.bluerebhirt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class DatosComunes {
    @Value("${app.nombre}") private String nombre;
    @ModelAttribute("nombreApp") public String nombreApp() { return nombre; }
}
