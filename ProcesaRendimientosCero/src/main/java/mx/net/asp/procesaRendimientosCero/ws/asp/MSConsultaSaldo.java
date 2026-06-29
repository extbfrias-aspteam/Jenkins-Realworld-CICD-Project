package mx.net.asp.procesaRendimientosCero.ws.asp;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.utilerias.InvokeRestServiceUtil;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MSConsultaSaldo {

    @Value("${ms.management.host}")
    private String msManagementHost;
    @Value("${ms.management.port}")
    private String msManagementPort;
    @Value("${ms.management.consulta.saldo.service}")
    private String consultaSaldoService;
    @Value("${asp.pago.management.user}")
    private String msManagementUser;
    @Value("${asp.pago.management.pass}")
    private String msManagementPass;
    private final InvokeRestServiceUtil invokeRestServiceUtil;
    private final Gson gson;

    private String getAuthorizationHeader() {
        return "Basic " + Base64.getEncoder().encodeToString(
                (msManagementUser + ":" + msManagementPass).getBytes()
        );
    }

    public RespuestaDTO consultarSaldo(String cuentaah) {
        // Construir el cuerpo de la solicitud
        new RespuestaDTO();
        RespuestaDTO respuesta;
        //ConsultaSaldoReq consultaSaldoReq = new ConsultaSaldoReq();
        Map<String, String> params = new HashMap<>();
        params.put("cuenta", cuentaah);
        //consultaSaldoReq.setCuenta(cuentaah);
        String url = InvokeRestServiceUtil.getURLRest(msManagementHost, msManagementPort, consultaSaldoService);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationHeader());
        // Configurar la solicitud con los encabezados
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        respuesta = invokeRestServiceUtil.enviarSolicitud(url, requestEntity, HttpMethod.GET, params);
        if (respuesta.getCodigo() <= -501 && respuesta.getCodigo() >= -503) {
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_WS,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_WS));
        }
        return respuesta;
    }
}

