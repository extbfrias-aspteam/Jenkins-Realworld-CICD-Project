package mx.net.asp.procesaRendimientosCero.utilerias;

import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import org.springframework.stereotype.Component;

@Component
public class RespuestaUtils {

    public static RespuestaDTO respuestaExito(RespuestaDTO respuesta) {
        respuesta.setCodigo(0);
        respuesta.setMensaje("OK");
        respuesta.setData(null);
        return respuesta;
    }

    public static RespuestaDTO respuestaExito(RespuestaDTO respuesta, Integer codigo, String mensaje) {
        respuesta.setCodigo(codigo);
        respuesta.setMensaje(mensaje);
        respuesta.setData(null);
        return respuesta;
    }

    public static RespuestaDTO respuestaExito(RespuestaDTO respuesta, String data) {
        respuesta.setCodigo(0);
        respuesta.setMensaje("OK");
        respuesta.setData(data);
        return respuesta;
    }

    public static RespuestaDTO asignarError(RespuestaDTO respuesta, Integer codigoError, String mensaje) {
        respuesta.setCodigo(codigoError);
        respuesta.setMensaje(mensaje);
        respuesta.setData(null);
        return respuesta;
    }
}
