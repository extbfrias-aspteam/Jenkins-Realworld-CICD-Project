package mx.net.asp.procesaRendimientosCero.utilerias.errores;

import java.util.HashMap;
import java.util.Map;

public class ErroresInversiones {

    public static final int RENDIMIENTOS_PEND_NOT_FOUND = -100;
    public static final int REINVERSIONES_PEND_NOT_FOUND = -101;
    public static final int ERROR_DEPOSITO_RENDIMIENTI = -102;
    public static final int MODALIDAD_NOT_FOUND = -103;
    public static final int RANGO_INVALIDO = -104;
    public static final int DETALLE_INVERSION_NOT_FOUND = -105;
    public static final int CUENTA_NOT_FOUND = -106;
    public static final int ERROR_CONSULTA_SALDO = -107;
    public static final int SALDO_INSUFICIENTE = -108;
    public static final int REINVERSION_NO_ACTIVA = -109;
    public static final int DEVOLUCION_CUENTA_ORIGEN = -110;
    public static final int ERROR_DEVOLUCION_CUENTA_ORIGEN = -111;
    public static final int ERROR_CUENTA_CON_SALDO = -112;
    public static final int RENDIMIENTO_NOT_FOUND = -113;
    public static final int INVERSIONES_ACTIVAS_NOT_FOUND = -114;
    public static final int NOT_FECHA_FIN = -115;
    public static final int REINVERSION_ACTIVA = -116;
    public static final int SALDO_NOT_FOUND = -117;
    public static final int CUENTA_CON_SALDO = -118;

    public static final Map<Integer, String> descError = new HashMap<>();

    static {
        descError.put(RENDIMIENTOS_PEND_NOT_FOUND, "No se encontraron rendimientos pendientes en la cola.");
        descError.put(REINVERSIONES_PEND_NOT_FOUND, "No se encontraron reinversiones pendientes en la cola.");
        descError.put(ERROR_DEPOSITO_RENDIMIENTI, "No fue posible realizar el deposito a la cuenta padre.");
        descError.put(MODALIDAD_NOT_FOUND, "Modalidad no encontrada.");
        descError.put(RANGO_INVALIDO, "El monto esta fuera del rango permitido.");
        descError.put(DETALLE_INVERSION_NOT_FOUND, "Lo sentimos no fue posible obtener el detalle de la cuenta de inversión.");
        descError.put(CUENTA_NOT_FOUND, "No se pudo obtener la informacion de la cuenta.");
        descError.put(SALDO_INSUFICIENTE, "No se pudo realizar la inversión, saldo insuficiente.");
        descError.put(ERROR_CONSULTA_SALDO, "No se pudo realizar la inversión, ocurrio un error al consultar el saldo de la cuenta.");
        descError.put(REINVERSION_NO_ACTIVA, "La cuenta de inversión ya esta configurada para no ser reinvertida.");
        descError.put(DEVOLUCION_CUENTA_ORIGEN, "No se pudo realizar la transferencia a su cuenta de inversion, por lo que el monto fue reembolsado a su cuenta de ahorro.");
        descError.put(ERROR_DEVOLUCION_CUENTA_ORIGEN, "No se pudo realizar la transferencia a su cuenta de inversion, ocurrió un error al intentar hacer el reembolso a su cuenta de ahorro, comunicate a contact center.");
        descError.put(ERROR_CUENTA_CON_SALDO, "La cuenta de inversion tiene saldo.");
        descError.put(RENDIMIENTO_NOT_FOUND, "No fue posible obtener el rendimiento de la modalidad seleccionada.");
        descError.put(INVERSIONES_ACTIVAS_NOT_FOUND, "No se encontraron inversiones con fecha final %s para ser canceladas.");
        descError.put(NOT_FECHA_FIN, "Hoy no es la fecha final de la cuenta de inversion %s.");
        descError.put(REINVERSION_ACTIVA, "La cuenta de inversión esta configurada para ser reinvertida.");
        descError.put(SALDO_NOT_FOUND, "No fue posible consultar el saldo de la cuenta de inversion %s.");
        descError.put(CUENTA_CON_SALDO, "La cuenta de inversion %s aun tiene saldo disponible.");
    }

    public static String getMensajeError(int codigo) {
        return descError.getOrDefault(codigo, "Error desconocido en inversiones.");
    }

    public static String getMensajeError(int codigo, String param) {
        String mensaje = descError.getOrDefault(codigo, "Error desconocido en inversiones.");
        return String.format(mensaje, param);
    }
}
