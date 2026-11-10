package co.bluerebhirt.config;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

class AdministradorInicialTest {
    @Test
    void creaAdministradorSoloEnUnaBaseVacia() throws Exception {
        var jdbc = mock(JdbcTemplate.class);
        var encoder = mock(PasswordEncoder.class);
        when(jdbc.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);
        when(encoder.encode("clave-segura-123")).thenReturn("hash-bcrypt");
        var entorno = new MockEnvironment().withProperty("app.admin-inicial-correo", "ADMIN@example.test")
                .withProperty("app.admin-inicial-clave", "clave-segura-123");
        new AdministradorInicial().crearAdministradorInicial(jdbc, encoder, entorno).run();
        verify(jdbc).update(anyString(), eq("Administrador"), eq("admin@example.test"), eq("hash-bcrypt"), eq("administrador"));
    }

    @Test
    void rechazaClaveDebilSinCrearCuenta() {
        var jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);
        var entorno = new MockEnvironment().withProperty("app.admin-inicial-correo", "admin@example.test")
                .withProperty("app.admin-inicial-clave", "corta");
        var inicio = new AdministradorInicial().crearAdministradorInicial(jdbc, mock(PasswordEncoder.class), entorno);
        assertThrows(IllegalStateException.class, inicio::run);
        verify(jdbc, never()).update(anyString(), any(Object[].class));
    }

    @Test
    void noCambiaUsuariosExistentes() throws Exception {
        var jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);
        new AdministradorInicial().crearAdministradorInicial(jdbc, mock(PasswordEncoder.class), new MockEnvironment()).run();
        verify(jdbc, never()).update(anyString(), any(Object[].class));
    }
}
