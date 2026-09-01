package mx.net.asp.asp_pago_api.ws.asp;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.InvokeRestServiceUtil;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
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
@RequiredArgsConstructor
public class WsAspPagoStorage {
    @Value("${ms.documents.host}")
    private String msDocumentsHost;
    @Value("${ms.documents.port}")
    private String msDocumentsPort;
    @Value("${asp.pago.documents.user}")
    private String msDocumentsUser;
    @Value("${asp.pago.documents.pass}")
    private String msDocumentsPass;

    private final InvokeRestServiceUtil invokeRestServiceUtil;

    public RespuestaDTO enviarPeticion(
            String pathEndPoint,
            MultiValueMap<String, Object> body,
            HttpMethod method,
            Map<String, String> queryParams
    ) {
        RespuestaDTO respuesta;
        String url = InvokeRestServiceUtil.getURLRest(msDocumentsHost, msDocumentsPort, pathEndPoint);
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        respuesta = invokeRestServiceUtil.enviarSolicitud(url, requestEntity, method, queryParams);
        if (respuesta.getCodigo() <= -501 && respuesta.getCodigo() >= -503) {
            RespuestaUtils.asignarError(
                    respuesta,
                    ErroresGenerales.ERROR_WS,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_WS)
            );
        }

        return respuesta;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", getAuthorizationHeader());
        //headers.set("X-Internal-Token", internalToken);
        return headers;
    }

    private String getAuthorizationHeader() {
        String auth = msDocumentsUser + ":" + msDocumentsPass;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}
