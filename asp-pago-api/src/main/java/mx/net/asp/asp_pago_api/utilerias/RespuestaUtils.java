package mx.net.asp.asp_pago_api.utilerias;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.dto.ServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RespuestaUtils {

    @Value("${clave.app.error.api}")
    private String cveErrorAplicativoValue;

    private static String cveErrorAplicativo;

    @PostConstruct
    public void init() {
        cveErrorAplicativo = cveErrorAplicativoValue;
    }

    private static final Gson gson = new Gson();

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

    public static RespuestaDTO respuestaExito(RespuestaDTO respuesta, String mensaje, String data) {
        respuesta.setCodigo(0);
        respuesta.setMensaje(mensaje);
        respuesta.setData(data);
        return respuesta;
    }

    public static RespuestaDTO asignarError(RespuestaDTO respuesta, Integer codigoError, String mensaje) {
        String mensajeFinal = String.format("%s [%s-%d]", mensaje, cveErrorAplicativo, Math.abs(codigoError));

        respuesta.setCodigo(codigoError);
        respuesta.setMensaje(mensajeFinal);
        respuesta.setData(null);
        return respuesta;
    }

    public static ServiceResponse evaluaRespuesta(RespuestaDTO respuesta, String body, boolean equalResp) {
        if(respuesta.getCodigo() == 0){
            return new ServiceResponse(body, HttpStatus.OK);
        }else{
            if(equalResp) //Evaluamos si el objeto de respuesta debe ser el mismo independientemente del STATUS CODE
                return new ServiceResponse(body, HttpStatus.BAD_REQUEST);

            return new ServiceResponse(gson.toJson(respuesta), HttpStatus.BAD_REQUEST);
        }
    }
}
