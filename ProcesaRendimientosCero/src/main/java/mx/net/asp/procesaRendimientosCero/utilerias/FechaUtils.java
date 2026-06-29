package mx.net.asp.procesaRendimientosCero.utilerias;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

@Component
public class FechaUtils {

    public static LocalDate convertirADateLocal(Date fecha) {
        if (fecha == null) return null;

        if (fecha instanceof java.sql.Date) {
            return ((java.sql.Date) fecha).toLocalDate();
        } else {
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    public static String convertirFechaALetras(Date fecha) {
        if (fecha == null) return "";

        LocalDate localDate;

        if (fecha instanceof java.sql.Date) {
            // Convierte directamente si es java.sql.Date
            localDate = ((java.sql.Date) fecha).toLocalDate();
        } else {
            // Convierte si es java.util.Date u otro tipo compatible
            localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        String fechaFormateada = localDate.format(formatter);

        int inicioMes = fechaFormateada.indexOf("de ") + 3;
        String mesCapitalizado = fechaFormateada.substring(inicioMes, inicioMes + 1).toUpperCase()
                + fechaFormateada.substring(inicioMes + 1);
        return fechaFormateada.substring(0, inicioMes) + mesCapitalizado;
    }

    public static long calcularDiasEntre(Date fechaInicio, Date fechaFin) {
        LocalDate inicio = convertirADateLocal(fechaInicio);
        LocalDate fin = convertirADateLocal(fechaFin);
        return ChronoUnit.DAYS.between(inicio, fin);
    }

    public static long calcularDiasPasados(Date fechaInicio) {
        LocalDate inicio = convertirADateLocal(fechaInicio);
        LocalDate hoy = LocalDate.now();
        long diasPasados = ChronoUnit.DAYS.between(inicio, hoy);
        // Si hoy es antes de la fecha inicio, devolver 0 o negativo según el caso
        return Math.max(diasPasados, 0);
    }

    public static long calcularDiasFaltantes(Date fechaFin) {
        LocalDate fin = convertirADateLocal(fechaFin);
        LocalDate hoy = LocalDate.now();
        long diasFaltanes = ChronoUnit.DAYS.between(hoy, fin);
        // Si hoy ya pasó la fecha fin, devolver 0 o negativo según el caso
        return Math.max(diasFaltanes, 0);
    }
}
