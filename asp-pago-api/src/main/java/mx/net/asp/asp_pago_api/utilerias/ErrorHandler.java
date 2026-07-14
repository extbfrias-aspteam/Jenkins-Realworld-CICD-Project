package mx.net.asp.asp_pago_api.utilerias;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ErrorHandler {
    public void handleException(Exception e) {
        StackTraceElement ste = getStackTraceElement(e);

        String className = ste.getClassName();
        String fileName = ste.getFileName();
        String methodName = ste.getMethodName();
        int lineNumber = ste.getLineNumber();
        // Imprimir la clase, archivo, metodo y línea donde ocurrió el error
        log.error("Error en clase: {} en archivo: {} en el metodo: {} en la linea: {}",
                className, fileName, methodName, lineNumber);
        // Imprimir el mensaje de error
        log.error("Mensaje de error:  {}", e.getMessage());
        // Imprimir la causa solo si está presente
        if (e.getCause() != null) {
            log.error("Causa: {}", e.getCause().getMessage());
        }
    }

    private static StackTraceElement getStackTraceElement(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        // Buscar el primer elemento que pertenezca a tu paquete
        StackTraceElement ste = null;
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("mx.net.asp")) {
                ste = element;
                break; // Detener en el primer match
            }
        }

        // Si no encontramos un match, usamos el primero del stack trace
        if (ste == null) {
            ste = stackTrace[0];
        }
        return ste;
    }
}
