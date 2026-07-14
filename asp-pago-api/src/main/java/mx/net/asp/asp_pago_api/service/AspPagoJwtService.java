package mx.net.asp.asp_pago_api.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoJWT;
import mx.net.asp.asp_pago_api.ws.asp.request.LoginAuthReq;
import mx.net.asp.asp_pago_api.ws.asp.request.RefreshTokenReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AspPagoJwtService {

    @Value("${aes.key.general}")
    private String cipherKey;
    private String encoding;
    @Value("${ms.jwt.login.auth.service}")
    private String pathLoginAuth;
    @Value("${ms.jwt.refresh.token.service}")
    private String pathRefreshToken;
    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoJWT wsAspPagoJWT;
    private final Gson gson;

    public AspPagoJwtService(CifradoUtil cifradoUtil,
                                ErrorHandler errorHandler,
                                WsAspPagoJWT wsAspPagoJWT,
                                Gson gson) {
        this.cifradoUtil = cifradoUtil;
        this.errorHandler = errorHandler;
        this.wsAspPagoJWT = wsAspPagoJWT;
        this.gson = gson;
    }

    public String loginAuth(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoJWT.enviarPeticion(pathLoginAuth,
                        gson.fromJson(respuestaDTO.getData(), LoginAuthReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String refreshToken(String request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request);
            if (respuestaDTO.getCodigo() == 0) {
                respuestaDTO = wsAspPagoJWT.enviarPeticion(pathRefreshToken,
                        gson.fromJson(respuestaDTO.getData(), RefreshTokenReq.class), HttpMethod.POST, null);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }
}
