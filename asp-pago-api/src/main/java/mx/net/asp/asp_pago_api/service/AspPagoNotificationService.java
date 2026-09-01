package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.dto.ServiceResponse;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoNotification;
import mx.net.asp.asp_pago_api.ws.asp.request.EnviaEmailReq;
import mx.net.asp.asp_pago_api.ws.asp.request.EnviaSMSReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Log4j2
public class AspPagoNotificationService {

    @Value("${ms.notification.enviar.mail.service}")
    private String pathEnviarEmail;
    @Value("${ms.notification.enviar.mail.attachment.service}")
    private String pathEnviarEmailAttachment;
    @Value("${ms.notification.enviar.sms}")
    private String pathEnviarSMS;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoNotification wsAspPagoNotification;
    private final Gson gson;

    public AspPagoNotificationService(CifradoUtil cifradoUtil,
                                      ErrorHandler errorHandler,
                                      WsAspPagoNotification wsAspPagoNotification,
                                      Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoNotification = wsAspPagoNotification;
        this.gson = gson;
    }

    public String enviarEmail(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoNotification.enviarPeticion(pathEnviarEmail,
                        gson.fromJson(respuestaDTO.getData(), EnviaEmailReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public ServiceResponse enviarSMS(EnviaSMSReq req) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            respuesta = wsAspPagoNotification.enviarPeticion(pathEnviarSMS,
                    req,
                    HttpMethod.POST, null);

            return RespuestaUtils.evaluaRespuesta(respuesta, respuesta.getData(), false);
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));

            return new ServiceResponse(gson.toJson(respuesta), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String enviarEmailAttachment(String destinatario, String asunto, String contenido, List<MultipartFile> archivos) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            if (!archivos.isEmpty())
                body.add("archivos", archivos);
            body.add("destinatario", destinatario);
            body.add("asunto", asunto);
            body.add("contenido", contenido);
            respuestaDTO = wsAspPagoNotification.enviarPeticion(pathEnviarEmailAttachment,
                    body, HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

}
