package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoDocuments;
import mx.net.asp.asp_pago_api.ws.asp.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AspPagoDocumentsService {
    @Value("${timeout.extended}") private
    Integer timeoutExtended;
    @Value("${ms.documents.genera.doc.cta.simplificada}")
    private String pathGeneraDocumentacionCtaSimplificada;
    @Value("${ms.documents.genera.anexo.detalle.beneficiario}")
    private String pathGeneraAnexoDetalleBeneficiario;
    @Value("${ms.documents.registra.imagenes.cuenta.simplificada}")
    private String pathRegistraImagenesCuentaSimplificada;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoDocuments wsAspPagoDocuments;
    private final TimeoutConfigService timeoutConfigService;
    private final Gson gson;

    public AspPagoDocumentsService(CifradoUtil cifradoUtil,
                              ErrorHandler errorHandler,
                                   TimeoutConfigService timeoutConfigService,
                                   WsAspPagoDocuments wsAspPagoDocuments,
                                   Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.timeoutConfigService = timeoutConfigService;
        this.wsAspPagoDocuments = wsAspPagoDocuments;
        this.gson = gson;
    }

    public String generaDocumentacionCtaSimplificada(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoDocuments.enviarPeticion(pathGeneraDocumentacionCtaSimplificada,
                        gson.fromJson(respuestaDTO.getData(), GeneraDocCtaSimplificadaReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registraImagenesCuentaSimplificada(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            increaseTimeout();
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoDocuments.enviarPeticion(pathRegistraImagenesCuentaSimplificada,
                        gson.fromJson(respuestaDTO.getData(), RegistroImagenesCuentaSimplificadaReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String generaAnexoDetalleBeneficiario(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            increaseTimeout();
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoDocuments.enviarPeticion(pathGeneraAnexoDetalleBeneficiario,
                        gson.fromJson(respuestaDTO.getData(), GeneraAnexoDetalleBeneficiarioReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private void increaseTimeout() {
        timeoutConfigService.setConnectTimeout(timeoutExtended);
        timeoutConfigService.setReadTimeout(timeoutExtended);
    }
}
