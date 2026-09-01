package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoOnb;
import mx.net.asp.asp_pago_api.ws.asp.request.AltaBeneficiarioReq;
import mx.net.asp.asp_pago_api.ws.asp.request.RegistroDeCuentaSimplificadaReq;
import mx.net.asp.asp_pago_api.ws.asp.request.ValidaFotoIneReq;
import mx.net.asp.asp_pago_api.ws.asp.request.ValidacionDeCorreoReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AspPagoOnbService {
    @Value("${timeout.extended}")
    private int timeoutExtended;
    @Value("${ms.onb.registro.cuenta.simplificada}")
    private String pathRegistroDeCuentaSimplificada;
    @Value("${ms.onb.validacion.correo}")
    private String pathValidacionDeCorreo;
    @Value("${ms.onb.alta.beneficiarios.V2}")
    private String pathAltaBeneficiariosV2;
    @Value("${ms.onb.valida.foto.ine}")
    private String pathValidaFotoIne;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoOnb wsAspPagoOnb;
    private final TimeoutConfigService timeoutConfigService;
    private final Gson gson;

    public AspPagoOnbService(CifradoUtil cifradoUtil,
            ErrorHandler errorHandler,
            WsAspPagoOnb wsAspPagoOnb,
            Gson gson,
                             TimeoutConfigService timeoutConfigService) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoOnb = wsAspPagoOnb;
        this.gson = gson;
        this.timeoutConfigService = timeoutConfigService;
    }

    public String registroDeCuentaSimplificada(String request, boolean aumentoTimeout) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            if (aumentoTimeout) {
                increaseTimeout();
            }
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoOnb.enviarPeticion(pathRegistroDeCuentaSimplificada,
                        gson.fromJson(respuestaDTO.getData(), RegistroDeCuentaSimplificadaReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validacionDeCorreo(String request, boolean aumentoTimeout) {
        RespuestaDTO respuesta = procesarValidacionCorreo(request, aumentoTimeout);
        return cifradoUtil.encryptResponse(respuesta);
    }

    public RespuestaDTO validaCorreoPHP(String request, boolean aumentoTimeout) {
        return procesarValidacionCorreo(request, aumentoTimeout);
    }

    private RespuestaDTO procesarValidacionCorreo(String request, boolean aumentoTimeout) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            if (aumentoTimeout) {
                increaseTimeout();
            }

            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                ValidacionDeCorreoReq datos = gson.fromJson(respuestaDTO.getData(), ValidacionDeCorreoReq.class);
                respuestaDTO = wsAspPagoOnb.enviarPeticion(
                        pathValidacionDeCorreo,
                        datos,
                        HttpMethod.POST,
                        null
                );
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return respuestaDTO;
    }

    public String altaBeneficiariosV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoOnb.enviarPeticion(pathAltaBeneficiariosV2,
                        gson.fromJson(respuestaDTO.getData(), AltaBeneficiarioReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaFotoIne(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoOnb.enviarPeticion(pathValidaFotoIne,
                        gson.fromJson(respuestaDTO.getData(), ValidaFotoIneReq.class),
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
