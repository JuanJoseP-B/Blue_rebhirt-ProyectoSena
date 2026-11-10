package co.bluerebhirt.admin;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminControlador {
    private final JdbcTemplate jdbc;
    public AdminControlador(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @GetMapping("/admin")
    public String panel(Model modelo) {
        modelo.addAttribute("rangos", jdbc.queryForList("SELECT id,zona,estado,ph_desde,ph_hasta,recomendacion FROM rangos_ph ORDER BY orden"));
        modelo.addAttribute("usuarios", jdbc.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class));
        modelo.addAttribute("cultivos", jdbc.queryForObject("SELECT COUNT(*) FROM cultivos", Integer.class));
        return "admin";
    }
    @PostMapping("/admin/rangos")
    @Transactional
    public String guardar(@RequestParam(name="desde", required=false) List<String> desde, @RequestParam(name="hasta", required=false) List<String> hasta, @RequestParam(name="recomendacion", required=false) List<String> recomendaciones, Model modelo) {
        try {
            if (desde == null || hasta == null || recomendaciones == null || desde.size()!=5 || hasta.size()!=5 || recomendaciones.size()!=5) throw new IllegalArgumentException("Deben existir cinco rangos completos.");
            var filas=jdbc.queryForList("SELECT id,zona,estado FROM rangos_ph ORDER BY orden FOR UPDATE");
            if (filas.size()!=5) throw new IllegalArgumentException("La configuración debe tener cinco rangos.");
            List<String> zonas=List.of("acido","bajo","ideal","alto","alcalino"); List<String> estados=List.of("alerta","aviso","ok","aviso","alerta");
            BigDecimal anterior=null;
            for (int i=0;i<5;i++) {
                BigDecimal inicio=new BigDecimal(desde.get(i)).setScale(1, java.math.RoundingMode.UNNECESSARY), fin=new BigDecimal(hasta.get(i)).setScale(1, java.math.RoundingMode.UNNECESSARY);
                String texto=recomendaciones.get(i).trim();
                if (!zonas.get(i).equals(filas.get(i).get("zona")) || !estados.get(i).equals(filas.get(i).get("estado")) || inicio.compareTo(BigDecimal.ZERO)<0 || fin.compareTo(BigDecimal.valueOf(14))>0 || inicio.compareTo(fin)>0 || (i==0 && inicio.compareTo(BigDecimal.ZERO)!=0) || (anterior!=null && inicio.compareTo(anterior.add(new BigDecimal("0.1")))!=0) || (i==4 && fin.compareTo(BigDecimal.valueOf(14))!=0) || texto.isEmpty() || texto.length()>255) throw new IllegalArgumentException("Revisa los cinco rangos: deben cubrir de 0 a 14 sin huecos ni solapes y tener recomendaciones.");
                anterior=fin;
            }
            for (int i=0;i<5;i++) jdbc.update("UPDATE rangos_ph SET ph_desde=?, ph_hasta=?, recomendacion=? WHERE id=?", new BigDecimal(desde.get(i)),new BigDecimal(hasta.get(i)),recomendaciones.get(i).trim(),filas.get(i).get("id"));
            return "redirect:/admin?guardado";
        } catch (IllegalArgumentException | ArithmeticException invalido) {
            modelo.addAttribute("error", "Revisa los cinco rangos: deben cubrir de 0 a 14 sin huecos ni solapes, con un decimal y recomendaciones completas.");
            return panel(modelo);
        }
    }
}
