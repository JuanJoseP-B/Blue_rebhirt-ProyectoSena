package co.bluerebhirt.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ExtendedModelMap;

class AdminControladorTest {
    @Test
    void rechazaHuecosSolapesYLimitesSinGuardar() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForList(contains("FOR UPDATE"))).thenReturn(List.of(
                fila(1, "acido", "alerta"), fila(2, "bajo", "aviso"),
                fila(3, "ideal", "ok"), fila(4, "alto", "aviso"), fila(5, "alcalino", "alerta")));
        var controlador = new AdminControlador(jdbc);
        var textos = List.of("a", "b", "c", "d", "e");
        var hasta = List.of("4.9", "5.4", "7.0", "7.5", "14.0");
        for (var desde : List.of(
                List.of("0", "5.1", "5.5", "7.1", "7.6"),
                List.of("0", "4.9", "5.5", "7.1", "7.6"),
                List.of("0.1", "5.0", "5.5", "7.1", "7.6"))) {
            assertEquals("admin", controlador.guardar(desde, hasta, textos, new ExtendedModelMap()));
        }
        verify(jdbc, never()).update(anyString(), any(Object[].class));
    }

    private Map<String, Object> fila(int id, String zona, String estado) {
        return Map.of("id", id, "zona", zona, "estado", estado);
    }
}
