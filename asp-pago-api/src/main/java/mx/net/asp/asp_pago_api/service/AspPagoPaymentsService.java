package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.CodigoRespuestaDTO;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.dto.ServiceResponse;
import mx.net.asp.asp_pago_api.model.DataCif;
import mx.net.asp.asp_pago_api.model.HeaderWS;
import mx.net.asp.asp_pago_api.model.RespuestaTraspasoEiyu;
import mx.net.asp.asp_pago_api.request.ProcesamientoSpeiSimpleReq;
import mx.net.asp.asp_pago_api.ws.asp.request.EntradaEnviaServiciosReq;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoPayments;
import mx.net.asp.asp_pago_api.ws.asp.request.GenerarAbonoReq;
import mx.net.asp.asp_pago_api.ws.asp.request.SpeiSimpleReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AspPagoPaymentsService {

    @Value("${ms.payments.procesamiento.spei.simple.V3}")
    private String pathProcesamientoSpeiSimpleV3;
    @Value("${ms.payments.procesa}")
    private String pathProcesa;
    @Value("${ms.payments.generar.abono.eiyu}")
    private String pathGenerarAbono;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final Gson gson;
    private final WsAspPagoPayments wsAspPagoPayments;

    public AspPagoPaymentsService(CifradoUtil cifradoUtil,
                                  ErrorHandler errorHandler,
                                  Gson gson,
                                  WsAspPagoPayments wsAspPagoPayments) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.gson = gson;
        this.wsAspPagoPayments = wsAspPagoPayments;
    }

    public String procesamientoSpeiSimpleV3(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                SpeiSimpleReq speiSimpleReq = gson.fromJson(respuestaDTO.getData(), SpeiSimpleReq.class);
                ProcesamientoSpeiSimpleReq pSpeiSimpleReq = gson.fromJson(speiSimpleReq.getDataCif(), ProcesamientoSpeiSimpleReq.class);
                pSpeiSimpleReq.setNombreBeneficiario(validarNombreBeneficiario(pSpeiSimpleReq.getNombreBeneficiario()));
                pSpeiSimpleReq.setCuentaAhorro(speiSimpleReq.getCuentaAhorro());
                pSpeiSimpleReq.setHeader(speiSimpleReq.getHeader());
                pSpeiSimpleReq.setCuentaAhorro(speiSimpleReq.getCuentaAhorro());
                respuestaDTO = wsAspPagoPayments.enviarPeticion(pathProcesamientoSpeiSimpleV3,
                        pSpeiSimpleReq, HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String procesa(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0)
                respuestaDTO = wsAspPagoPayments.enviarPeticion(pathProcesa,
                        gson.fromJson(respuestaDTO.getData(), EntradaEnviaServiciosReq.class),
                        HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public ServiceResponse generarAbonoEiyu(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                Object respuesta = sendAndProcessRequest(respuestaDTO);
                if (respuesta instanceof RespuestaDTO) {
                    RespuestaDTO resDto = (RespuestaDTO) respuesta;
                    String body = cifradoUtil.encryptResponse(resDto);
                    return new ServiceResponse(body, HttpStatus.BAD_REQUEST);
                } else if (respuesta instanceof RespuestaTraspasoEiyu) {
                    RespuestaTraspasoEiyu resEiyu = (RespuestaTraspasoEiyu) respuesta;
                    Map<String, String> mapJsonResp = createResponseMap(respuestaDTO, resEiyu);
                    String body = gson.toJson(mapJsonResp);
                    return new ServiceResponse(body, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        String body = cifradoUtil.encryptResponse(respuestaDTO);
        return new ServiceResponse(body, HttpStatus.BAD_REQUEST);
    }

    private Object sendAndProcessRequest(RespuestaDTO respuestaDTO) {
        // Ejecutar petición
        RespuestaDTO respuestaServicio = wsAspPagoPayments.enviarPeticion(
                pathGenerarAbono,
                gson.fromJson(respuestaDTO.getData(), GenerarAbonoReq.class),
                HttpMethod.POST,
                null
        );

        //Si el código es 0, intentamos convertir data a RespuestaTraspasoEiyu
        if (respuestaServicio.getCodigo() == 0) {
            RespuestaTraspasoEiyu traspaso = gson.fromJson(respuestaServicio.getData(), RespuestaTraspasoEiyu.class);
            return traspaso;
        } else {
            //Si el código es distinto de 0, devolvemos el error tal cual vino
            return respuestaServicio;
        }
    }

    private Map<String, String> createResponseMap(RespuestaDTO respuestaDTO, RespuestaTraspasoEiyu respuestaTraspasoEiyu) {
        Map<String, String> mapJsonResp = new HashMap<>();

        if (respuestaTraspasoEiyu.getClave().equals("DEV")) {
            mapJsonResp.put("retornoRespuesta", cifradoUtil.encryptResponse(gson.toJson(respuestaTraspasoEiyu)));
        } else {
            mapJsonResp.put("traspasosRespuesta", cifradoUtil.encryptResponse(gson.toJson(respuestaTraspasoEiyu)));
        }
        return mapJsonResp;
    }

    private String validarNombreBeneficiario(String nombreBeneficiario) {
        return (nombreBeneficiario != null && nombreBeneficiario.length() > 40)
                ? nombreBeneficiario.substring(0, 40)
                : nombreBeneficiario;
    }
}
