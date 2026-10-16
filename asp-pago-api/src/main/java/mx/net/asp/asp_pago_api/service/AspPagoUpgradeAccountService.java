package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.model.CatalogoPLD;
import mx.net.asp.asp_pago_api.model.SesionIncode;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoUpgradeAccount;
import mx.net.asp.asp_pago_api.ws.asp.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;

@Service
@Log4j2
public class AspPagoUpgradeAccountService {

    @Value("${ms.services.consultar.giros.by.actividad.id}")
    private String pathConsultarGirosByActividadId;
    @Value("${ms.services.consultar.sesion.incode}")
    private String pathConsultarSesionIncode;
    @Value("${ms.services.finalizar.sesion.incode}")
    private String pathFinalizarSesionIncode;
    @Value("${ms.services.finalizar.sesion.incode.manual}")
    private String pathFinalizarSesionIncodeManual;
    @Value("${ms.services.actualizar.sesion.incode}")
    private String pathActualizarSesionIncode;
    @Value("${ms.services.guardar.sesion.incode}")
    private String pathGuardarSesionIncode;
    @Value("${ms.services.subir.nivel.cuenta.remota}")
    private String pathSubirNivelCuentaRemota;
    @Value("${ms.services.guardar.formulario}")
    private String pathGuardarFormulario;
    @Value("${ms.services.valida.curp.cuenta}")
    private String pathValidaCurpCuenta;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoUpgradeAccount wsAspPagoUpgradeAccount;
    private final Gson gson;

    public AspPagoUpgradeAccountService(CifradoUtil cifradoUtil,
                                ErrorHandler errorHandler,
                                WsAspPagoUpgradeAccount wsAspPagoUpgradeAccount,
                                        Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoUpgradeAccount = wsAspPagoUpgradeAccount;
        this.gson = gson;
    }

    public String consultarCatalogos(String path) {
        return processRequestWithParams(path, null, new TypeToken<List<CatalogoPLD>>() {}.getType());
    }

    public String consultarCatalogos(Map<String, String> queryParams) {
        return processRequestWithParams(pathConsultarGirosByActividadId, queryParams, new TypeToken<List<CatalogoPLD>>() {}.getType());
    }

    public String consultarSesionIncode(Map<String, String> queryParams) {
        return processRequestWithParams(pathConsultarSesionIncode, queryParams, SesionIncode.class);
    }

    public String actualizarSesionIncode(String request) {
        return processRequest(request, pathActualizarSesionIncode, ActualizarSesionIncodeReq.class, HttpMethod.PATCH);
    }

    public String finalizarSesionIncode(String request) {
        return processRequest(request, pathFinalizarSesionIncode, FinalizarSesionIncodeReq.class, HttpMethod.PATCH);
    }

    public String finalizarSesionIncodeManual(String request) {
        return processRequestIncode(request, pathFinalizarSesionIncodeManual, FinalizarSesionIncodeManualReq.class, HttpMethod.PATCH);
    }

    public String guardarSesionIncode(String request) {
        return processRequest(request, pathGuardarSesionIncode, GuardarSesionIncodeReq.class, HttpMethod.POST);
    }

    public String subirNivelCuentaRemota(String request) {
        return processRequest(request, pathSubirNivelCuentaRemota, SubirNivelCuentaRemotaReq.class, HttpMethod.POST);
    }

    public String guardarFormulario(String request) {
        return processRequest(request, pathGuardarFormulario, SubirNivelCuentaRemotaReq.class, HttpMethod.POST);
    }

    public String validaCurpCuenta(String request) {
        return processRequest(request, pathValidaCurpCuenta, ValidarCurpCuentaReq.class, HttpMethod.POST);
    }

    private String processRequestWithParams(String path, Map<String, String> queryParams, Type responseType) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = wsAspPagoUpgradeAccount.enviarPeticion(path, String.class, HttpMethod.GET, queryParams);
            if (respuestaDTO.getCodigo() == 0) {
                Object responseData = gson.fromJson(respuestaDTO.getData(), responseType);
                respuestaDTO.setData(cifradoUtil.encryptResponse(gson.toJson(responseData)));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private String processRequest(String request, String path, Class<?> requestType, HttpMethod method) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Object requestData = gson.fromJson(respuestaDTO.getData(), requestType);
                respuestaDTO = wsAspPagoUpgradeAccount.enviarPeticion(path, requestData, method, null);
                if (respuestaDTO.getCodigo() == 0) {
                    Object responseData = gson.fromJson(respuestaDTO.getData(), (Class<?>) SesionIncode.class);
                    respuestaDTO.setData(cifradoUtil.encryptResponse(gson.toJson(responseData)));
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private String processRequestIncode(String request, String path, Class<?> requestType, HttpMethod method) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            Object requestData = gson.fromJson(request, requestType);
            Field field = requestType.getDeclaredField("onboardingStatus");
            field.setAccessible(true);
            if (field.get(requestData).toString().contains("MANUAL")) {
                respuestaDTO = wsAspPagoUpgradeAccount.enviarPeticion(path, requestData, method, null);
                if (respuestaDTO.getCodigo() == 0) {
                   Object responseData = gson.fromJson(respuestaDTO.getData(), (Class<?>) SesionIncode.class);
                   respuestaDTO.setData(cifradoUtil.encryptResponse(gson.toJson(responseData)));
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }
}