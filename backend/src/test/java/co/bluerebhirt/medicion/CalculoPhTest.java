package co.bluerebhirt.medicion;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalculoPhTest {
    private RangoPh rango(int id, String desde, String hasta, String estado) {
        return new RangoPh(id, new BigDecimal(desde), new BigDecimal(hasta), estado, "Recomendación de prueba");
    }
    private final List<RangoPh> rangos = List.of(rango(1, "0.0", "4.9", "alerta"), rango(2, "5.0", "5.4", "aviso"), rango(3, "5.5", "7.0", "ok"), rango(4, "7.1", "7.5", "aviso"), rango(5, "7.6", "14.0", "alerta"));
    @Test void clasificaLimites() {
        String[] valores = {"0.0", "4.9", "5.0", "5.4", "5.5", "6.2", "7.0", "7.1", "7.5", "7.6", "14.0"};
        String[] estados = {"alerta", "alerta", "aviso", "aviso", "ok", "ok", "ok", "aviso", "aviso", "alerta", "alerta"};
        for (int i = 0; i < valores.length; i++) assertEquals(estados[i], CalculoPh.encontrar(new BigDecimal(valores[i]), rangos).estado(), valores[i]);
    }
    @Test void validaEntradaYConfiguracion() {
        assertThrows(IllegalArgumentException.class, () -> CalculoPh.encontrar(new BigDecimal("14.1"), rangos));
        assertThrows(IllegalStateException.class, () -> CalculoPh.encontrar(new BigDecimal("6.0"), List.of()));
        assertThrows(IllegalStateException.class, () -> CalculoPh.encontrar(new BigDecimal("6.0"), List.of(rangos.get(2), rangos.get(2))));
        assertEquals("ok", CalculoPh.encontrar(new BigDecimal("5.46"), rangos).estado());
    }
}
