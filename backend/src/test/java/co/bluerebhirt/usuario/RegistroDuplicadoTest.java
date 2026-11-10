package co.bluerebhirt.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;

class RegistroDuplicadoTest {
    @Test
    void correoRepetidoMuestraErrorYNoIniciaSesion() throws Exception {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(jdbc.update(anyString(), any(Object[].class))).thenThrow(new DuplicateKeyException("duplicado"));
        var form = new RegistroForm();
        form.setNombre("Ana"); form.setCorreo("ana@example.com"); form.setClave("clave123");
        var errores = new BeanPropertyBindingResult(form, "registroForm");
        var peticion = mock(HttpServletRequest.class);
        assertEquals("registro", new AuthControlador(jdbc, encoder).registrar(form, errores, peticion));
        assertTrue(errores.hasFieldErrors("correo"));
        verify(peticion, never()).login(anyString(), anyString());
    }
}
