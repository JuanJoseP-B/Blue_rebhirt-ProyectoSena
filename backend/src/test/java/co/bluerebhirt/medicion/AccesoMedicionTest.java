package co.bluerebhirt.medicion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.server.ResponseStatusException;

class AccesoMedicionTest {
    @Test
    void unaMedicionAjenaResponde404() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForList(contains("m.id=? AND u.correo=?"), eq(42L), eq("ana@example.com")))
                .thenReturn(List.of());
        var controlador = new MedicionControlador(jdbc, mock(CalculoPh.class));
        var error = assertThrows(ResponseStatusException.class,
                () -> controlador.resultado(42L, () -> "ana@example.com", new ExtendedModelMap()));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }
}
