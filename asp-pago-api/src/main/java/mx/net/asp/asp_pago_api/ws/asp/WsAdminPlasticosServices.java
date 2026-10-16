package mx.net.asp.asp_pago_api.ws.asp;

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

import java.util.Base64;
import java.util.Map;

@Service
@Log4j2
public class WsAdminPlasticosServices {
    @Value("${ms.admin.plasticos.services.host}")
    private String msAdminPlasticosServicesHost;
    @Value("${ms.admin.plasticos.services.port}")
    private String msAdminPlasticosServicesPort;
    @Value("${admin.plasticos.services.user}")
    private String msAdminPlasticosServicesUser;
    @Value("${admin.plasticos.services.pass}")
    private String msAdminPlasticosServicesPass;
    private final InvokeRestServiceUtil invokeRestServiceUtil;

    // Constructor con inyección de dependencias
    public WsAdminPlasticosServices(
            InvokeRestServiceUtil invokeRestServiceUtil) {
        this.invokeRestServiceUtil = invokeRestServiceUtil;
    }

    public RespuestaDTO enviarPeticion(String pathEndPoint, Object object, HttpMethod method, Map<String, String> queryParams) {
        RespuestaDTO respuesta = new RespuestaDTO();
        String url = InvokeRestServiceUtil.getURLRest(msAdminPlasticosServicesHost, msAdminPlasticosServicesPort, pathEndPoint);
        HttpHeaders headers = createHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
        respuesta = invokeRestServiceUtil.enviarSolicitud(url, requestEntity, method, queryParams);
        if (respuesta.getCodigo() <= -501 && respuesta.getCodigo() >= -503) {
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_WS,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_WS));
        }
        return respuesta;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationHeader());
        return headers;
    }

    private String getAuthorizationHeader() {
        String auth = msAdminPlasticosServicesUser + ":" + msAdminPlasticosServicesPass;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}
