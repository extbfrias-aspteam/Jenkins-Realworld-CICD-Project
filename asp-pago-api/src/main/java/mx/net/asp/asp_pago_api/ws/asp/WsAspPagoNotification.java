package mx.net.asp.asp_pago_api.ws.asp;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.InvokeRestServiceUtil;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Base64;
import java.util.Map;

@Service
@Log4j2
public class WsAspPagoNotification {
    @Value("${ms.notification.host}")
    private String msNotificationHost;
    @Value("${ms.notification.port}")
    private String msNotificationPort;
    @Value("${asp.pago.notification.user}")
    private String msNotificationUser;
    @Value("${asp.pago.notification.pass}")
    private String msNotificationPass;
    private final InvokeRestServiceUtil invokeRestServiceUtil;
    private final Gson gson;

    @Autowired
    public WsAspPagoNotification(InvokeRestServiceUtil invokeRestServiceUtil, Gson gson) {
        this.invokeRestServiceUtil = invokeRestServiceUtil;
        this.gson = gson;
    }

    public RespuestaDTO enviarPeticion(String pathEndPoint, Object object, HttpMethod method, Map<String, String> queryParams) {
        RespuestaDTO respuesta = new RespuestaDTO();
        String url = InvokeRestServiceUtil.getURLRest(msNotificationHost, msNotificationPort, pathEndPoint);
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
        respuesta = invokeRestServiceUtil.enviarSolicitud(url, requestEntity, method, queryParams);
        if (respuesta.getCodigo() <= -501 && respuesta.getCodigo() >= -503) {
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_WS,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_WS));
        }

        return respuesta;
    }

    public RespuestaDTO enviarPeticion(String pathEndPoint, MultiValueMap<String, Object> body, HttpMethod method, Map<String, String> queryParams) {
        RespuestaDTO respuesta = new RespuestaDTO();
        String url = InvokeRestServiceUtil.getURLRest(msNotificationHost, msNotificationPort, pathEndPoint);
        HttpHeaders headers = createHeaders(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        respuesta = invokeRestServiceUtil.enviarSolicitud(url, requestEntity, method, queryParams);
        if (respuesta.getCodigo() <= -501 && respuesta.getCodigo() >= -503) {
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_WS,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_WS));
        }
        return respuesta;
    }

    private HttpHeaders createHeaders(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.set("Authorization", getAuthorizationHeader());
        return headers;
    }

    private String getAuthorizationHeader() {
        String auth = msNotificationUser + ":" + msNotificationPass;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}
