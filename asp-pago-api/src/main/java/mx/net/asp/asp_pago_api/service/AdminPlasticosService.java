package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAdminPlasticosServices;
import mx.net.asp.asp_pago_api.ws.asp.request.EntradaConsultarMovimientosReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;

@Service
@Log4j2
public class AdminPlasticosService {

    @Value("${aes.key.general}")
    private String cipherKey;
    @Value("${encoding.format}")
    private String encoding;
    @Value("${timeout.extended}")
    private int timeoutExtended;
    @Value("${ms.admin.plasticos.services.consultar.movimientos.tarjeta}")
    private String pathConsultarMovimientosTarjeta;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAdminPlasticosServices wsAdminPlasticosServices;
    private final TimeoutConfigService timeoutConfigService;
    private final Gson gson;
    private static final String ERROR_NO_MOVEMENTS = "No se encontraron movimientos en la cuenta";

    public AdminPlasticosService(
            CifradoUtil cifradoUtil,
            ErrorHandler errorHandler,
            WsAdminPlasticosServices wsAdminPlasticosServices,
            TimeoutConfigService timeoutConfigService,
            Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAdminPlasticosServices = wsAdminPlasticosServices;
        this.timeoutConfigService = timeoutConfigService;
        this.gson = gson;
    }

    public String consultaMovimientos(String request, boolean aumentoTimeout) {
        try {
            EntradaConsultarMovimientosReq entradaConsultarMovimientos = parseRequest(request);
            if (aumentoTimeout) {
                increaseTimeout();
            }

            String jsonDecoded = serializeToJson(entradaConsultarMovimientos);
            RespuestaDTO respuestaDTO = enviarPeticion(pathConsultarMovimientosTarjeta, jsonDecoded);

            return cifrarRespuesta(respuestaDTO);
        } catch (Exception e) {
            errorHandler.handleException(e);
            return cifrarErrorResponse();
        }
    }

    private EntradaConsultarMovimientosReq parseRequest(String request) {
        try {
            String jsonDecoded = decodeRequest(request);
            return transformToRequest(jsonDecoded);
        } catch (Exception e) {
            errorHandler.handleException(e);
            return new EntradaConsultarMovimientosReq();
        }
    }

    private String decodeRequest(String request) {
        String decodedString = "";
        try {
            String decodedRequest = URLDecoder.decode(request, encoding);
            decodedString = decodedRequest.replace(" ", "+");
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return decodedString;
    }

    private EntradaConsultarMovimientosReq transformToRequest(String jsonDecoded) {
        String decryptedJson = cifradoUtil.desencriptaInformacionB64(cifradoUtil.generaKeySource(cipherKey),
                jsonDecoded);
        EntradaConsultarMovimientosReq entrada = gson.fromJson(decryptedJson,
                EntradaConsultarMovimientosReq.class);

        if (entrada.getNumeroTarjeta().length() == 10) {
            entrada.setMedioAcceso(entrada.getNumeroTarjeta());
            entrada.setTipoMedioAcceso("ASP");
            entrada.setNumeroTarjeta("");
        }
        return entrada;
    }

    public String procesarPeticion(String path, String request, boolean aumentoTimeout) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            if (aumentoTimeout) {
                increaseTimeout();
            }
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                String jsonDecoded = respuestaDTO.getData();
                respuestaDTO = enviarPeticion(path, jsonDecoded);
                if (respuestaDTO.getCodigo() == 0)
                    respuestaDTO = cifradoUtil.obtenerRespuestaDto(respuestaDTO);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return cifrarErrorResponse();
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private void increaseTimeout() {
        timeoutConfigService.setConnectTimeout(timeoutExtended);
        timeoutConfigService.setReadTimeout(timeoutExtended);
    }

    private RespuestaDTO enviarPeticion(String path, String jsonDecoded) {
        return wsAdminPlasticosServices.enviarPeticion(path, jsonDecoded, HttpMethod.POST, null);
    }

    private String cifrarRespuesta(RespuestaDTO respuestaDTO) {
        return cifradoUtil.encriptaInformacionB64(cifradoUtil.generaKeySource(cipherKey),
                serializeToJson(cifrarData(respuestaDTO)));
    }

    private String cifrarErrorResponse() {
        RespuestaDTO errorResponse = new RespuestaDTO();
        setErrorResponse(errorResponse, "Error interno");
        return cifrarRespuesta(errorResponse);
    }

    private RespuestaDTO cifrarData(RespuestaDTO respuestaDTO) {
        if (respuestaDTO.getData() != null && !respuestaDTO.getData().equals("[]")) {
            respuestaDTO.setData(
                    cifradoUtil.encriptaInformacionB64(cifradoUtil.generaKeySource(cipherKey), respuestaDTO.getData()));
        } else {
            setErrorResponse(respuestaDTO, ERROR_NO_MOVEMENTS);
        }
        return respuestaDTO;
    }

    private void setErrorResponse(RespuestaDTO respuestaDTO, String mensaje) {
        respuestaDTO.setCodigo(-20);
        respuestaDTO.setMensaje(mensaje);
    }

    private String serializeToJson(Object object) {
        return gson.toJson(object);
    }
}
