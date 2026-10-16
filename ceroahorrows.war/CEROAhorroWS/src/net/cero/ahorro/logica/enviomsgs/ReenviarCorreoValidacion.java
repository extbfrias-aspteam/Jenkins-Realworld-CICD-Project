package net.cero.ahorro.logica.enviomsgs;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.PlvalidacionCorreo;
import net.cero.data.Respuesta;
import net.cero.req.reenviarws.EnviaValidaCorreoOBJ;
import net.cero.req.reenviarws.ReenviarMensajeReq;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.dao.ValCorreoDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * Clase utilizada para el reenvio del correo de validacion dentro del proceso de onboarding en caso de que este no hubiese llegado al principio
 */
@Log4j2
@Service
@AllArgsConstructor
public class ReenviarCorreoValidacion extends AReenviarMensaje {
    private final ValCorreoDAO valCorreoDAO;

    @Override
    public Respuesta ejecutarReenvioMensaje(ReenviarMensajeReq request) {
        Respuesta resultado = new Respuesta();
        try{
            String data = String.format("{\"correoElectronico\":\"%s\"}",request.getCorreoElectronico());
            resultado = validaEntrada(data,EnviaValidaCorreoOBJ.class);
            if(resultado.getCodigo() != 0)
                return resultado;

            PlvalidacionCorreo registro = valCorreoDAO.obtenerUltimoRegistroValCorreo(request.getCorreoElectronico(),"0");
            if(registro==null)
            {
                log.error("No se encontraron validaciones de correo pendientes para el dato proporcionado");
                resultado.setCodigo(6);
                resultado.setMensaje("No se encontraron validaciones de correo pendientes para el dato proporcionado.");
                return resultado;
            }
            if(StringUtils.isBlank(registro.getRfc()) || StringUtils.isBlank(registro.getCurp()) || StringUtils.isBlank(registro.getToken()))
            {
                log.error("El registron o trae uno de los datos importantes como el rfc {}, la curp: {} o el token: {}",registro.getRfc()
                        ,registro.getCurp(),registro.getToken());
                resultado.setCodigo(7);
                resultado.setMensaje("No se registró una curp y/o rfc valido en el ultimo intento de validación.");
                return resultado;
            }

            JsonObject json = new JsonObject();
            json.addProperty("correoElectronico",request.getCorreoElectronico());
            json.addProperty("token",registro.getToken());
            json.addProperty("accion","1");
            json.addProperty("nombre"," ");
            json.addProperty("rfc",registro.getRfc());
            json.addProperty("curp",registro.getCurp());

            String url= ConstantesUtil.CERO_CODI_WS+"validacionDeCorreo";
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
            resultado.setMensaje("Ocurrio un error al ejecutar la rutina para el envio del correo de validacion de la cuenta");
        }
        return resultado;
    }
}
