package co.bluerebhirt.medicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class MedicionControlador {
    private final JdbcTemplate jdbc;
    private final CalculoPh calculo;
    public MedicionControlador(JdbcTemplate jdbc, CalculoPh calculo) { this.jdbc = jdbc; this.calculo = calculo; }
    private void cargarCultivos(Principal usuario, Model modelo) {
        modelo.addAttribute("cultivos", jdbc.queryForList("SELECT c.id, c.nombre FROM cultivos c JOIN usuarios u ON u.id = c.usuario_id WHERE u.correo = ? ORDER BY c.nombre", usuario.getName()));
    }
    @GetMapping("/mediciones/nueva")
    public String nueva(Principal usuario, Model modelo) {
        cargarCultivos(usuario, modelo); modelo.addAttribute("datos", Map.of()); modelo.addAttribute("errores", Map.of()); return "medicion";
    }
    @PostMapping("/mediciones")
    public String guardar(@RequestParam Map<String, String> datos, Principal usuario, Model modelo) {
        Map<String, String> errores = new LinkedHashMap<>();
        try {
            long cultivo = Long.parseLong(datos.getOrDefault("cultivo", ""));
            Integer propio = jdbc.queryForObject("SELECT COUNT(*) FROM cultivos c JOIN usuarios u ON u.id = c.usuario_id WHERE c.id = ? AND u.correo = ?", Integer.class, cultivo, usuario.getName());
            if (propio == null || propio == 0) errores.put("cultivo", "Selecciona uno de tus cultivos.");
        } catch (NumberFormatException invalido) { errores.put("cultivo", "Selecciona uno de tus cultivos."); }
        validarNumero(datos, errores, "ph", "0", "14", "Escribe un pH entre 0 y 14.");
        validarNumero(datos, errores, "nivel", "0", "500", "Escribe un valor entre 0 y 500.");
        validarNumero(datos, errores, "humedad", "0", "100", "Escribe un valor entre 0 y 100.");
        validarNumero(datos, errores, "temperatura", "-5", "60", "Escribe un valor entre -5 y 60.");
        validarOpcion(datos, errores, "hojas", List.of("verdes", "amarillas", "manchas"));
        validarOpcion(datos, errores, "abono", List.of("menos_2_semanas", "2_a_4_semanas", "mas_1_mes", "nunca"));
        validarOpcion(datos, errores, "lluvia", List.of("0", "1"));
        if (!errores.isEmpty()) {
            modelo.addAttribute("errores", errores); modelo.addAttribute("datos", datos);
            cargarCultivos(usuario, modelo); return "medicion";
        }
        try {
            long cultivo = Long.parseLong(datos.getOrDefault("cultivo", ""));
            Integer propio = jdbc.queryForObject("SELECT COUNT(*) FROM cultivos c JOIN usuarios u ON u.id = c.usuario_id WHERE c.id = ? AND u.correo = ?", Integer.class, cultivo, usuario.getName());
            if (propio == null || propio == 0) throw new IllegalArgumentException("Selecciona uno de tus cultivos.");
            BigDecimal ph = numero(datos, "ph", "0", "14").setScale(1, RoundingMode.HALF_UP);
            BigDecimal nivel = numero(datos, "nivel", "0", "500").setScale(1, RoundingMode.HALF_UP);
            BigDecimal humedad = numero(datos, "humedad", "0", "100").setScale(1, RoundingMode.HALF_UP);
            BigDecimal temperatura = numero(datos, "temperatura", "-5", "60").setScale(1, RoundingMode.HALF_UP);
            String hojas = opcion(datos, "hojas", List.of("verdes", "amarillas", "manchas"));
            String abono = opcion(datos, "abono", List.of("menos_2_semanas", "2_a_4_semanas", "mas_1_mes", "nunca"));
            String lluvia = opcion(datos, "lluvia", List.of("0", "1"));
            RangoPh rango = calculo.calcular(ph);
            var llave = new GeneratedKeyHolder();
            jdbc.update(conexion -> {
                var sentencia = conexion.prepareStatement("INSERT INTO mediciones (cultivo_id, ph, nivel_agua_cm, humedad_pct, temperatura_c, hojas, ultimo_abono, llovio_3_dias, rango_id, estado, recomendacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] {"id"});
                sentencia.setLong(1, cultivo); sentencia.setBigDecimal(2, ph); sentencia.setBigDecimal(3, nivel); sentencia.setBigDecimal(4, humedad); sentencia.setBigDecimal(5, temperatura);
                sentencia.setString(6, hojas); sentencia.setString(7, abono); sentencia.setBoolean(8, lluvia.equals("1")); sentencia.setInt(9, rango.id()); sentencia.setString(10, rango.estado()); sentencia.setString(11, rango.recomendacion());
                return sentencia;
            }, llave);
            return "redirect:/mediciones/" + llave.getKey().longValue();
        } catch (IllegalArgumentException error) {
            modelo.addAttribute("error", error instanceof NumberFormatException ? "Revisa los valores numéricos y selecciona tu cultivo." : error.getMessage());
            modelo.addAttribute("datos", datos); modelo.addAttribute("errores", Map.of()); cargarCultivos(usuario, modelo); return "medicion";
        }
    }
    private void validarNumero(Map<String, String> datos, Map<String, String> errores, String campo, String minimo, String maximo, String mensaje) {
        try { numero(datos, campo, minimo, maximo); }
        catch (IllegalArgumentException invalido) { errores.put(campo, mensaje); }
    }
    private void validarOpcion(Map<String, String> datos, Map<String, String> errores, String campo, List<String> permitidos) {
        if (!permitidos.contains(datos.getOrDefault(campo, ""))) errores.put(campo, "Selecciona una opción válida.");
    }
    private BigDecimal numero(Map<String, String> datos, String campo, String minimo, String maximo) {
        BigDecimal valor = new BigDecimal(datos.getOrDefault(campo, ""));
        if (valor.compareTo(new BigDecimal(minimo)) < 0 || valor.compareTo(new BigDecimal(maximo)) > 0) throw new IllegalArgumentException("Revisa los valores de pH, nivel, humedad y temperatura.");
        return valor;
    }
    private String opcion(Map<String, String> datos, String campo, List<String> permitidos) {
        String valor = datos.getOrDefault(campo, "");
        if (!permitidos.contains(valor)) throw new IllegalArgumentException("Responde las tres preguntas sobre tu cultivo.");
        return valor;
    }
    @GetMapping("/mediciones/{id}")
    public String resultado(@PathVariable long id, Principal usuario, Model modelo) {
        var filas = jdbc.queryForList("SELECT m.*, c.nombre AS cultivo, r.pasos FROM mediciones m JOIN cultivos c ON c.id=m.cultivo_id JOIN usuarios u ON u.id=c.usuario_id JOIN rangos_ph r ON r.id=m.rango_id WHERE m.id=? AND u.correo=?", id, usuario.getName());
        if (filas.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        var medicion = filas.get(0);
        var ideal = jdbc.queryForMap("SELECT ph_desde, ph_hasta FROM rangos_ph WHERE zona = ?", "ideal");
        double desde = ((BigDecimal) ideal.get("ph_desde")).doubleValue(); double hasta = ((BigDecimal) ideal.get("ph_hasta")).doubleValue();
        modelo.addAttribute("medicion", medicion); modelo.addAttribute("ideal", ideal);
        modelo.addAttribute("marca", ((BigDecimal) medicion.get("ph")).doubleValue() * 100 / 14);
        modelo.addAttribute("inicioIdeal", desde * 100 / 14); modelo.addAttribute("finIdeal", hasta * 100 / 14);
        modelo.addAttribute("pasos", medicion.get("pasos").toString().split("\\R"));
        return "resultado";
    }
}
