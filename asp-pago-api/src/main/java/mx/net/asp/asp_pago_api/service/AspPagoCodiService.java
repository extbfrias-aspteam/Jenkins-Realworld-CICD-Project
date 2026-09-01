package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.model.RegistroCodi;
import mx.net.asp.asp_pago_api.request.CodiReq;
import mx.net.asp.asp_pago_api.ws.asp.request.*;
import mx.net.asp.asp_pago_api.request.RegistroSubsecuenteReq;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoCodi;
import mx.net.asp.asp_pago_api.request.RegistroInicialReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AspPagoCodiService {
    @Value("${ms.codi.registro.inicial.V2}")
    private String pathRegistroInicialV2;
    @Value("${ms.codi.registro.subsecuente.V2}")
    private String pathRegistroSubsecuenteV2;
    @Value("${ms.codi.registra.app.por.omision.V2}")
    private String pathRegistraAppPorOmisionV2;
    @Value("${ms.codi.validacion.cuentas.V2}")
    private String pathValidacionDeCuentasV2;
    @Value("${ms.codi.procesamiento.pago.V2}")
    private String pathProcesamientoPagoV2;
    @Value("${ms.codi.registra.pago.generado.V2}")
    private String pathRegistraPagoGeneradoV2;
    @Value("${ms.codi.consultar.estatus.codi}")
    private String pathConsultarEstatusCoDi;
    @Value("${ms.codi.consultar.operaciones.codi}")
    private String pathConsultarOperacionesCoDi;
    @Value("${ms.codi.obtener.referencia.serial.cobro.V2}")
    private String pathObtenerReferenciaSerialCobroV2;
    @Value("${ms.codi.registra.cobro.generado.V2}")
    private String pathRegistraCobroGeneradoV2;
    @Value("${ms.codi.registrar.bitacora.codi}")
    private String pathRegistrarBitacoraCodi;
    @Value("${ms.codi.procesamiento.devolucion.V2}")
    private String pathProcesamientoDevolucionV2;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoCodi wsAspPagoCodi;
    private final Gson gson;

    public AspPagoCodiService(CifradoUtil cifradoUtil,
                             ErrorHandler errorHandler,
                              WsAspPagoCodi wsAspPagoCodi,
                              Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoCodi = wsAspPagoCodi;
        this.gson = gson;
    }

    public String registroInicialV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                CodiReq codiReq = gson.fromJson(respuestaDTO.getData(), CodiReq.class);
                respuestaDTO = enviarPeticionObtenerRegistroCodi(codiReq.getNumeroCuentaAhorro());
                if (respuestaDTO.getCodigo() == 0) {
                    String dataDecripted = desencriptarPeticionCodi(respuestaDTO, codiReq.getIdCanal(), codiReq.getDataCif());
                    respuestaDTO = enviarPeticionCodi(pathRegistroInicialV2, gson.fromJson(dataDecripted, RegistroInicialReq.class));
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registroSubsecuenteV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                RegistroSubsecuenteReq registroSubsecuenteReq = gson.fromJson(respuestaDTO.getData(), RegistroSubsecuenteReq.class);
                respuestaDTO = enviarPeticionObtenerRegistroCodi(registroSubsecuenteReq.getNumeroCuentaAhorro());
                if (respuestaDTO.getCodigo() == 0) {
                    String dataDecripted = desencriptarPeticionCodi(respuestaDTO, registroSubsecuenteReq.getIdCanal(), registroSubsecuenteReq.getDataCif());
                    respuestaDTO = enviarPeticionCodi(pathRegistroSubsecuenteV2, gson.fromJson(dataDecripted, CodiDataReq.class));
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registraAppPorOmisionV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = enviarPeticionCodi(pathRegistraAppPorOmisionV2, gson.fromJson(respuestaDTO.getData(), CodiReq.class));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String validacionDeCuentasV2(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                CodiReq codiReq = gson.fromJson(respuestaDTO.getData(), CodiReq.class);
                respuestaDTO = enviarPeticionObtenerRegistroCodi(codiReq.getNumeroCuentaAhorro());
                if (respuestaDTO.getCodigo() == 0) {
                    String dataDecripted = desencriptarPeticionCodiSubstring(respuestaDTO, codiReq.getIdCanal(), codiReq.getDataCif());
                    respuestaDTO = enviarPeticionCodi(pathValidacionDeCuentasV2, gson.fromJson(dataDecripted, CodiDataReq.class));
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String consultarEstatusCoDi(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = enviarPeticionCodi(pathConsultarEstatusCoDi, gson.fromJson(respuestaDTO.getData(), ConsultarEstatusCoDiReq.class));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String consultarOperacionesCoDi(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = enviarPeticionCodi(pathConsultarOperacionesCoDi, gson.fromJson(respuestaDTO.getData(), ConsultarOperacionesCoDiReq.class));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerReferenciaSerialCobro(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = enviarPeticionCodi(pathObtenerReferenciaSerialCobroV2, gson.fromJson(respuestaDTO.getData(), ObtenerReferenciaSerialCobroReq.class));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String registrarBitacoraCodi(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = enviarPeticionCodi(pathRegistrarBitacoraCodi, gson.fromJson(respuestaDTO.getData(), RegistrarBitacoraCodiReq.class));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private RespuestaDTO enviarPeticionObtenerRegistroCodi(String numeroCuentaAhorro) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("numeroCuentaAhorro", numeroCuentaAhorro);
        return wsAspPagoCodi.enviarPeticion("Nuevo end point para obtener el registro Codi por cuenta", null, HttpMethod.POST, queryParams);
    }

    private String desencriptarPeticionCodi(RespuestaDTO respuestaDTO, Integer idCanal, String dataCif) {
        RegistroCodi registroCodi = gson.fromJson(respuestaDTO.getData(), RegistroCodi.class);
        String keySourceASP = generarKeySource(registroCodi, idCanal);
        return cifradoUtil.desencriptaInformacionB64(keySourceASP, dataCif);
    }

    private String desencriptarPeticionCodiSubstring(RespuestaDTO respuestaDTO, Integer idCanal, String dataCif) {
        RegistroCodi registroCodi = gson.fromJson(respuestaDTO.getData(), RegistroCodi.class);
        String keySourceASP = generarKeySource(registroCodi, idCanal);
        String key = keySourceASP.substring(0, 32);
        String iv = keySourceASP.substring(32, 64);
        return cifradoUtil.decryptB64(key, iv, dataCif);
    }

    private String generarKeySource(RegistroCodi registroCodi, Integer idCanal) {
        if (idCanal != null && idCanal == 3) {
            return cifradoUtil.generaKeySource(registroCodi.getUsuario(), registroCodi.getContraseña());
        } else {
            return cifradoUtil.generaKeySource(registroCodi.getCuenta(), registroCodi.getContraseña());
        }
    }

    private RespuestaDTO enviarPeticionCodi(String path, Object object) {
        return wsAspPagoCodi.enviarPeticion(path, object, HttpMethod.POST, null);
    }
}
