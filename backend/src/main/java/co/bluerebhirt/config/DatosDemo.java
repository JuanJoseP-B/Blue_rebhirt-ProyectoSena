package co.bluerebhirt.config;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import co.bluerebhirt.medicion.CalculoPh;

@Configuration
public class DatosDemo {
    @Bean
    @ConditionalOnProperty(name = "app.datos-demo", havingValue = "true")
    CommandLineRunner crearUsuarios(JdbcTemplate jdbc, PasswordEncoder encoder, CalculoPh calculo, TransactionTemplate transaccion) {
        return args -> transaccion.executeWithoutResult(estado -> {
            Integer cantidad = jdbc.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
            if (cantidad == null || cantidad != 0) return;
            jdbc.update("INSERT INTO usuarios (nombre, correo, contrasena_hash, rol) VALUES (?, ?, ?, ?)", "Administrador", "admin@bluerebhirt.test", encoder.encode("admin123"), "administrador");
            jdbc.update("INSERT INTO usuarios (nombre, correo, contrasena_hash, rol) VALUES (?, ?, ?, ?)", "Ana", "ana@bluerebhirt.test", encoder.encode("ana123"), "agricultor");
            Long usuario=jdbc.queryForObject("SELECT id FROM usuarios WHERE correo=?", Long.class, "ana@bluerebhirt.test");
            jdbc.update("INSERT INTO cultivos (usuario_id,nombre,lugar,fecha_siembra) VALUES (?,?,?,?)",usuario,"Lote La Cumbre 1","Bella Vista",LocalDate.of(2026,3,12));
            jdbc.update("INSERT INTO cultivos (usuario_id,nombre,lugar,fecha_siembra) VALUES (?,?,?,?)",usuario,"Lote del río","Bella Vista",LocalDate.of(2026,4,3));
            Long primero=jdbc.queryForObject("SELECT id FROM cultivos WHERE usuario_id=? AND nombre=?",Long.class,usuario,"Lote La Cumbre 1");
            Long segundo=jdbc.queryForObject("SELECT id FROM cultivos WHERE usuario_id=? AND nombre=?",Long.class,usuario,"Lote del río");
            String[] valores={"6.1","6.3","5.9","5.4","4.9","5.6","6.4","6.0"};
            LocalDate[] fechas={LocalDate.of(2026,10,12),LocalDate.of(2026,10,15),LocalDate.of(2026,10,19),LocalDate.of(2026,10,22),LocalDate.of(2026,10,26),LocalDate.of(2026,10,30),LocalDate.of(2026,11,3),LocalDate.of(2026,11,7)};
            for (int i=0;i<valores.length;i++) guardar(jdbc,calculo,primero,valores[i],fechas[i]);
            guardar(jdbc,calculo,segundo,"4.8",LocalDate.of(2026,11,9));
        });
    }
    private void guardar(JdbcTemplate jdbc, CalculoPh calculo, long cultivo, String valor, LocalDate fecha) {
        BigDecimal ph=new BigDecimal(valor); var rango=calculo.calcular(ph);
        jdbc.update("INSERT INTO mediciones (cultivo_id,ph,nivel_agua_cm,humedad_pct,temperatura_c,hojas,ultimo_abono,llovio_3_dias,rango_id,estado,recomendacion,medido_en) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",cultivo,ph,new BigDecimal("18.0"),new BigDecimal("62.0"),new BigDecimal("24.0"),"verdes","2_a_4_semanas",false,rango.id(),rango.estado(),rango.recomendacion(),Timestamp.valueOf(fecha.atTime(9,0)));
    }
}
