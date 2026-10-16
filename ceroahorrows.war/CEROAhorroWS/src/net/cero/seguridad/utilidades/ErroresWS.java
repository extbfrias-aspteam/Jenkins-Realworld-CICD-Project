package net.cero.seguridad.utilidades;


import java.util.HashMap;
import java.util.Map;

public class ErroresWS {
    public static final int ERROR_INTERNO = -1;
    public static final int CAMPOS_INCORRECTOS = 1;
    public static final int NO_REGISTROS_CODI = 2;
    public static final int NO_DATOS_REFERENCIA = 3;
    public static final int NO_RESULTADOS = 4;
    public static final int FALTA_INFORMACION = 5;

    public static final int WS_NO_DISPONIBVLE = 6;


    public static Map<Integer, String> descError = new HashMap<>();

    private ErroresWS() {
        throw new IllegalStateException("Clase de solo constantes");
    }

    static {
        descError.put(ERROR_INTERNO,"Ocurrió un error inesperado.");
        descError.put(CAMPOS_INCORRECTOS,"Campos Incorrectos.");
        descError.put(NO_REGISTROS_CODI,"No existe información en el periodo seleccionado.");
        descError.put(NO_DATOS_REFERENCIA,"No existe la referencia en la fecha proporcionada.");
        descError.put(NO_RESULTADOS,"No se encontraron datos.");
        descError.put(FALTA_INFORMACION,"Falta información para poder evaluar los datos.");
        descError.put(WS_NO_DISPONIBVLE,"El servicio de %s no se encuentra disponible temporalmente. Favor de intentar mas tarde.");
    }
}

