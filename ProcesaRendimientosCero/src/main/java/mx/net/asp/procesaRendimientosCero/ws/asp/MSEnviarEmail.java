package mx.net.asp.procesaRendimientosCero.ws.asp;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.ws.asp.request.EnviaEmailReq;
import mx.net.asp.procesaRendimientosCero.utilerias.InvokeRestServiceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class MSEnviarEmail {

    @Value("${ms.notification.host}")
    private String msNotificationHost;
    @Value("${ms.notification.port}")
    private String msNotificationPort;
    @Value("${ms.notification.enviar.mail.service}")
    private String mailService;
    @Value("${ms.notification.enviar.mail.attachment.service}")
    private String mailAttachmentService;
    @Value("${asp.pago.notification.user}")
    private String msNotificationUser;
    @Value("${asp.pago.notification.pass}")
    private String msNotificationPass;
    private final InvokeRestServiceUtil invokeRestServiceUtil;

    private String getAuthorizationHeader() {
        return "Basic " + Base64.getEncoder().encodeToString(
                (msNotificationUser + ":" + msNotificationPass).getBytes()
        );
    }

    @Async
    public CompletableFuture<RespuestaDTO> enviarCorreo(String destinatario, String asunto, String contenido) {
        // Construir el cuerpo de la solicitud
        EnviaEmailReq emailRequest = new EnviaEmailReq();
        emailRequest.setDestinatario(destinatario);
        emailRequest.setAsunto(asunto);
        emailRequest.setContenido(contenido);
        String url = InvokeRestServiceUtil.getURLRest(msNotificationHost, msNotificationPort, mailService);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationHeader());
        // Configurar la solicitud con los encabezados
        HttpEntity<EnviaEmailReq> requestEntity = new HttpEntity<>(emailRequest, headers);
        return invokeRestServiceUtil.enviarSolicitudAsync(url, requestEntity, HttpMethod.POST, null);
    }
}

