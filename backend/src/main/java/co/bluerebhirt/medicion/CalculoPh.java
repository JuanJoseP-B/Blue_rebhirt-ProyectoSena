package co.bluerebhirt.medicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CalculoPh {
    private final JdbcTemplate jdbc;
    public CalculoPh(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public RangoPh calcular(BigDecimal valor) {
        var rangos = jdbc.query("SELECT id, ph_desde, ph_hasta, estado, recomendacion FROM rangos_ph ORDER BY orden",
                (rs, fila) -> new RangoPh(rs.getInt("id"), rs.getBigDecimal("ph_desde"), rs.getBigDecimal("ph_hasta"), rs.getString("estado"), rs.getString("recomendacion")));
        return encontrar(valor, rangos);
    }
    public static RangoPh encontrar(BigDecimal valor, List<RangoPh> rangos) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0 || valor.compareTo(BigDecimal.valueOf(14)) > 0) throw new IllegalArgumentException("El pH debe estar entre 0 y 14.");
        BigDecimal redondeado = valor.setScale(1, RoundingMode.HALF_UP);
        var encontrados = rangos.stream().filter(r -> redondeado.compareTo(r.desde()) >= 0 && redondeado.compareTo(r.hasta()) <= 0).toList();
        if (encontrados.size() != 1) throw new IllegalStateException("Los rangos de pH no cubren el valor de forma única.");
        return encontrados.get(0);
    }
}
