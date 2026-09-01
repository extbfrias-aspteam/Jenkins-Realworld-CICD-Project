package net.cero.ahorro.logica.enviomsgs;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.req.reenviarws.ReenviarMensajeReq;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.UtileriasAES;
import net.cero.ws.data.ToolsR;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Clase abstracta empleada para definir el comportamiento para los reenvios de mensajes ya sea de sms o correos
 */
@Log4j2
public abstract class AReenviarMensaje {
    @Autowired
    protected UtileriasAES utileriasAES;
    protected final Gson gson = ToolsR.GBuilder();
    protected final String AES= ConstantesUtil.CERO_CODI_AES;

    /**
     * El fin de este metodo es realizar el comportamiento principal de la clase, que es definir el envio del mensaje
     * correspondiente
     * @param request Es un objeto con los datos necesarios para el envio
     * @return Contiene el resultado de la operacion
     */
    public abstract Respuesta ejecutarReenvioMensaje(ReenviarMensajeReq request);

    /**
     * Metodo definido para validar la info que venga en el campo data que será proporcionado al metodo ejecutarReenvioMensaje
     * @param data Es un String con los datos necesarios para el envio
     * @return Contiene el resultado de la operacion
     */
    public Respuesta validaEntrada(String data,Class clase) {
        Respuesta respuesta = new Respuesta();
        try{
            final Object objeto = clase.newInstance();
            Object entrada = gson.fromJson(data, objeto.getClass());
            if(entrada == null)
            {
                respuesta.setCodigo(2);
                respuesta.setData("Entrada de datos incorrecta. Favor de verificar.");
                return respuesta;
            }

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Object>> violations = validator.validate(entrada);
            if(!violations.isEmpty())
            {
                ConstraintViolation<Object> rule = violations.stream().findFirst().orElse(null);
                respuesta.setCodigo(2);
                if(!StringUtils.isBlank(rule.getMessageTemplate()))
                    if(rule.getMessageTemplate().contains("%s"))
                        respuesta.setMensaje(String.format(rule.getMessageTemplate(),rule.getPropertyPath()));
                    else
                        respuesta.setMensaje(rule.getMessageTemplate());
                else
                    respuesta.setMensaje("Campos incorrectos");
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al tratar de validar la entrada de datos",e);
            respuesta.setCodigo(1);
            respuesta.setData("Ocurrió un error al tratar de validar la entrada de datos");
        }
        return respuesta;
    }

    /**
     * Metodo que se utiliza dentro de la clase para realizar peticiones http
     * @param url String, contiene la ruta completa
     * @param body String, contiene el json string
     * @return String, el body de la respuesta
     */
    protected String http(String url, String body,String user,String pass) {
        log.info("url: {}", url);
        log.info("body: {}", body);
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient cliente = new OkHttpClient();
        String auth = Credentials.basic(user, pass);
        try {
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            return  cliente.newCall(request).execute().body().string();

        }catch(Exception e) {
            log.error("Error en http, se intento realiza una peticion a [" + url + "] El error fue: " + e.getMessage(),e);
            return "ERROR";
        }
    }
}
