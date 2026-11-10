package co.bluerebhirt;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Fechas {
    private static final ZoneId BOGOTA = ZoneId.of("America/Bogota");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d 'de' MMMM", new Locale("es", "CO"));

    public static String fecha(LocalDate fecha) { return fecha.format(FORMATO); }
    public static String transcurrido(LocalDate fecha) {
        long dias = ChronoUnit.DAYS.between(fecha, LocalDate.now(BOGOTA));
        if (dias <= 0) return "Hoy";
        if (dias == 1) return "Ayer";
        return "Hace " + dias + " días";
    }
}
