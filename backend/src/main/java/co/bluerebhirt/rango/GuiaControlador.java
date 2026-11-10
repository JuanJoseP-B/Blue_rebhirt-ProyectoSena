package co.bluerebhirt.rango;

import java.math.BigDecimal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuiaControlador {
    private final JdbcTemplate jdbc;
    public GuiaControlador(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @GetMapping("/guia")
    public String guia(Model modelo) {
        var rangos = jdbc.queryForList("SELECT zona, recomendacion, ph_desde, ph_hasta FROM rangos_ph WHERE zona IN (?, ?, ?) ORDER BY orden", "acido", "ideal", "alcalino");
        var ideal = rangos.stream().filter(r -> r.get("zona").equals("ideal")).findFirst().orElseThrow();
        modelo.addAttribute("rangos", rangos); modelo.addAttribute("ideal", ideal);
        modelo.addAttribute("inicioIdeal", ((BigDecimal) ideal.get("ph_desde")).doubleValue() * 100 / 14);
        modelo.addAttribute("finIdeal", ((BigDecimal) ideal.get("ph_hasta")).doubleValue() * 100 / 14);
        return "guia";
    }
}
