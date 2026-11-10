package co.bluerebhirt.medicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class HistorialControlador {
    private final JdbcTemplate jdbc;
    private final CalculoPh calculo;
    public HistorialControlador(JdbcTemplate jdbc, CalculoPh calculo) { this.jdbc = jdbc; this.calculo = calculo; }
    public record Punto(long id, BigDecimal ph, String estado, String fecha, double x, double y) { }
    @GetMapping("/historial")
    public String historial(@RequestParam(required = false) Long cultivo, @RequestParam(defaultValue = "false") boolean demo, Principal usuario, Model modelo) {
        var cultivos = jdbc.queryForList("SELECT c.id, c.nombre FROM cultivos c JOIN usuarios u ON u.id=c.usuario_id WHERE u.correo=? ORDER BY CASE WHEN (SELECT MAX(m.medido_en) FROM mediciones m WHERE m.cultivo_id=c.id) IS NULL THEN 1 ELSE 0 END, (SELECT MAX(m.medido_en) FROM mediciones m WHERE m.cultivo_id=c.id) DESC, c.nombre", usuario.getName());
        if (cultivo == null && !cultivos.isEmpty()) cultivo = ((Number) cultivos.get(0).get("id")).longValue();
        Long seleccionado = cultivo;
        if (seleccionado != null && cultivos.stream().noneMatch(c -> ((Number) c.get("id")).longValue() == seleccionado)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var filas = seleccionado == null ? List.<Map<String,Object>>of() : jdbc.queryForList("SELECT m.id, m.ph, m.estado, m.medido_en FROM mediciones m JOIN cultivos c ON c.id=m.cultivo_id JOIN usuarios u ON u.id=c.usuario_id WHERE c.id=? AND u.correo=? ORDER BY m.medido_en DESC, m.id DESC LIMIT 8", seleccionado, usuario.getName());
        var cronologico = new ArrayList<>(filas); Collections.reverse(cronologico);
        if (demo) {
            cronologico.clear();
            String[] ejemplos = {"6.1", "6.3", "5.9", "5.4", "4.9", "5.6", "6.4", "6.0"};
            var hoy = java.time.LocalDate.now(java.time.ZoneId.of("America/Bogota"));
            for (int i = 0; i < ejemplos.length; i++) {
                BigDecimal valor = new BigDecimal(ejemplos[i]);
                cronologico.add(Map.of("id", 0L, "ph", valor, "estado", calculo.calcular(valor).estado(),
                        "medido_en", java.sql.Timestamp.valueOf(hoy.minusDays((ejemplos.length - 1L - i) * 4).atTime(9, 0))));
            }
        }
        var puntos = new ArrayList<Punto>(); BigDecimal suma = BigDecimal.ZERO; int fuera = 0;
        for (int i=0; i<cronologico.size(); i++) {
            var fila=cronologico.get(i); var ph=(BigDecimal) fila.get("ph"); String estado=fila.get("estado").toString();
            var fecha=((java.sql.Timestamp) fila.get("medido_en")).toLocalDateTime().toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("d MMM", new java.util.Locale("es", "CO")));
            double x=cronologico.size()==1 ? 64 : 64 + i*536.0/(cronologico.size()-1);
            double y=260-(Math.max(4, Math.min(9, ph.doubleValue()))-4)*48;
            puntos.add(new Punto(((Number)fila.get("id")).longValue(),ph,estado,fecha,x,y)); suma=suma.add(ph); if (!estado.equals("ok")) fuera++;
        }
        var ideal=jdbc.queryForMap("SELECT ph_desde, ph_hasta FROM rangos_ph WHERE zona=?", "ideal");
        double inferior=Math.max(4,Math.min(9,((BigDecimal)ideal.get("ph_desde")).doubleValue()));
        double superior=Math.max(4,Math.min(9,((BigDecimal)ideal.get("ph_hasta")).doubleValue()));
        modelo.addAttribute("cultivos",cultivos); modelo.addAttribute("seleccionado",seleccionado); modelo.addAttribute("puntos",puntos);
        modelo.addAttribute("demo", demo);
        modelo.addAttribute("linea",puntos.stream().map(p -> p.x()+","+p.y()).collect(java.util.stream.Collectors.joining(" ")));
        modelo.addAttribute("promedio",puntos.isEmpty() ? null : suma.divide(BigDecimal.valueOf(puntos.size()),1,RoundingMode.HALF_UP)); modelo.addAttribute("fuera",fuera);
        modelo.addAttribute("ideal",ideal); modelo.addAttribute("bandaY",260-(superior-4)*48); modelo.addAttribute("bandaAlto",(superior-inferior)*48);
        return "historial";
    }
}
