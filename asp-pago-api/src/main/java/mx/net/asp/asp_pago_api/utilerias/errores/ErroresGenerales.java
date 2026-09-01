package mx.net.asp.asp_pago_api.utilerias.errores;

import java.util.HashMap;
import java.util.Map;

public class ErroresGenerales {
    public static final int ERROR_INTERNO = -200;
    public static final int ERROR_WS = -199;

    public static final Map<Integer, String> descError = new HashMap<>();

    static {
        descError.put(ERROR_INTERNO, "Error interno.");
        descError.put(ERROR_WS, "Lo sentimos, el servicio no se encuentra disponible, inténtelo más tarde.");
    }

    public static String getMensajeError(int codigo) {
        return descError.getOrDefault(codigo, "Error desconocido en transferencia");
    }
}
