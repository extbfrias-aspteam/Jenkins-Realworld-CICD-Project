package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.model.PasswordResetCambioResp;
import mx.net.asp.asp_pago_api.model.TokenPassword;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoAccess;
import mx.net.asp.asp_pago_api.ws.asp.request.*;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class AspPagoAccessService {
    @Value("${aes.key.general}")
    private String cipherKey;
    @Value("${aes.key.password}")
    private String passCipherKey;
    @Value("${aes.key.cambio.pass}")
    private String cambioPassCipherKey;
    @Value("${aes.key.initial}")
    private String initialCipherKey;
    @Value("${encoding.format}")
    private String encoding;
    @Value("${ms.access.validar.codigo.autorizacion}")
    private String pathValidarCodigoAutorizacion;
    @Value("${ms.access.cambiar.codigo.autorizacion}")
    private String pathCambiarCodigoAutorizacion;
    @Value("${ms.access.peticion.cambio.pass.v2}")
    private String pathPeticionCambioPass;
    @Value("${ms.access.cambio.password.v2}")
    private String pathCambioPass;
    @Value("${ms.access.logIn}")
    private String pathLogIn;
    @Value("${ms.access.valida.version.app}")
    private String pathValidaVersionApp;
    @Value("${ms.access.cierra.sesion}")
    private String pathCierraSesion;
    @Value("${ms.access.obtener.datos.iniciales}")
    private String pathObtenerDatosIniciales;
    @Value("${ms.access.validar.token.cambio.pass}")
    private String pathValidarTokenCambioPass;

    @Value("${ms.access.password.reset.request}")
    private String pathRequestPasswordReset;
    @Value("${ms.access.password.reset.valida.otp}")
    private String pathValidaOTPPasswordReset;
    @Value("${ms.access.password.reset.change}")
    private String pathChangePwdPasswordReset;

    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoAccess wsAspPagoAccess;
    private final Gson gson;

    public AspPagoAccessService(CifradoUtil cifradoUtil,
            ErrorHandler errorHandler,
                                WsAspPagoAccess wsAspPagoAccess,
                                Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoAccess = wsAspPagoAccess;
        this.gson = gson;
    }

    public String peticionCambioPassV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request, cambioPassCipherKey);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathPeticionCambioPass,
                        gson.fromJson(respuestaDTO.getData(), PeticionCambioPassReq.class), HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(gson.toJson(respuestaDTO), cambioPassCipherKey);
    }

    public RespuestaDTO cambioPasswordV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptPassword(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathCambioPass,
                        gson.fromJson(respuestaDTO.getData(), CambioPasswordV2Req.class), HttpMethod.PATCH, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return respuestaDTO;
    }

    public String logInV3(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathLogIn,
                        gson.fromJson(respuestaDTO.getData(), LogInReq.class), HttpMethod.POST, null);
                if (respuestaDTO.getCodigo() == 0)
                    respuestaDTO = cifradoUtil.obtenerRespuestaDto(respuestaDTO);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validarCodigoAutorizacion(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathValidarCodigoAutorizacion,
                        gson.fromJson(respuestaDTO.getData(), ValidarCodigoAutorizacionReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String cambiarCodigoAutorizacion(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathCambiarCodigoAutorizacion,
                        gson.fromJson(respuestaDTO.getData(), CambiarCodigoAutorizacionReq.class),
                        HttpMethod.PATCH, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validaVersionApp(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathValidaVersionApp,
                        gson.fromJson(respuestaDTO.getData(), ValidaVersionAppReq.class),
                        HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String cierraSesion(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathCierraSesion,
                        gson.fromJson(respuestaDTO.getData(), CierraSesionReq.class), HttpMethod.PATCH, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerDatosIniciales(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request, initialCipherKey);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathObtenerDatosIniciales,
                        gson.fromJson(respuestaDTO.getData(), DatosInicialesReq.class), HttpMethod.GET, null);
                respuestaDTO.setData(cifradoUtil.encryptResponse(respuestaDTO.getData(), initialCipherKey));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(new Gson().toJson(respuestaDTO), initialCipherKey);
    }

    public RespuestaDTO validarTokenCambioPass(TokenPassword request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = wsAspPagoAccess.enviarPeticion(pathValidarTokenCambioPass, request, HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return respuestaDTO;
    }

    public String solicitudPasswordReset(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request, cambioPassCipherKey);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathRequestPasswordReset,
                        gson.fromJson(respuestaDTO.getData(), SolicitudPasswordResetReq.class), HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(gson.toJson(respuestaDTO), cambioPassCipherKey);
    }

    public String validaOtpPasswordReset(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request, cambioPassCipherKey);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathValidaOTPPasswordReset,
                        gson.fromJson(respuestaDTO.getData(), ValidaOTPPasswordResetReq.class), HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(gson.toJson(respuestaDTO), cambioPassCipherKey);
    }

    public String cambioPassword(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request, cambioPassCipherKey);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoAccess.enviarPeticion(pathChangePwdPasswordReset,
                        gson.fromJson(respuestaDTO.getData(), CambioPasswordResetReq.class), HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(gson.toJson(respuestaDTO), cambioPassCipherKey);
    }
}
