package mx.net.asp.asp_pago_api.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.request.*;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class BaseService {

    private final Environment env;
    private final ErrorHandler errorHandler;

    public RespuestaDTO baseTest(BaseReq req) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            respuesta.setCodigo(0);
            respuesta.setMensaje("OK");
            respuesta.setData(req.getRequest());
        } catch (Exception e) {
            errorHandler.handleException(e);
            respuesta.setCodigo(-200);
            respuesta.setMensaje("Error interno");
            respuesta.setData(null);
        }
        return respuesta;
    }
}
