package net.cero.ahorro.logica.enviomsgs;


import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.req.reenviarws.ReenviarMensajeReq;
import net.cero.req.reenviarws.ReenvioCodigoOBJ;
import net.cero.seguridad.utilidades.ConstantesUtil;
import org.springframework.stereotype.Service;

/**
 * Clase empleada para realizar el reenvio del codigo del token para el mensaje de dispositivos diferentes y
 * el cambio del codigo de seguridad
 */
@Log4j2
@Service
public class ReenviarCodigo extends AReenviarMensaje {
    @Override
    public Respuesta ejecutarReenvioMensaje(ReenviarMensajeReq request) {
        Respuesta resultado = new Respuesta();
        try {
            String data = String.format("{\"telefono\":\"%s\"}",request.getTelefono());
            resultado = validaEntrada(data, ReenvioCodigoOBJ.class);
            if(resultado.getCodigo() != 0)
                return resultado;

            JsonObject json = new JsonObject();
            json.addProperty("telefono",request.getTelefono());

            String url= ConstantesUtil.CERO_CODI_WS+"enviarCodigo";
            log.info("Json original: {}",gson.toJson(json));
            String encodedJson = utileriasAES.encriptaInformacionB64(utileriasAES.generaKeySource(AES),gson.toJson(json));
            String response = http(url,encodedJson,"ASP","a5p2017$");
            if(response.equals("ERROR"))
            {
                log.error("Ocurrio un error al invocar los servicios");
                resultado.setCodigo(5);
                resultado.setMensaje("Ocurrio un error inesperado al consumir los servicios.");
                return resultado;
            }
            log.info("Encoded response: {}",response);
            String decodedJson = utileriasAES.desencriptaInformacionB64(utileriasAES.generaKeySource(AES),response);
            log.info("Json encoded: {}",decodedJson);
            resultado = gson.fromJson(decodedJson,Respuesta.class);
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al ejecutar la funcion ejecutarReenvioMensaje ",e);
            resultado.setCodigo(1);
            resultado.setMensaje("Ocurrio un error al ejecutar la rutina para el envio del correo de validacion del a cuenta");
        }
        return resultado;
    }
}
