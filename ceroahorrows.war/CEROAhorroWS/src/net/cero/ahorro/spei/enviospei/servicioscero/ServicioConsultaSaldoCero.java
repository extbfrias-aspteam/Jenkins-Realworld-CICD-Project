package net.cero.ahorro.spei.enviospei.servicioscero;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.servicioscero.base.BaseServicioWS;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Log4j2
@Service
public class ServicioConsultaSaldoCero extends BaseServicioWS {
    private static final long serialVersionUID = 1L;

    public RespuestaSVC consultaSaldoCero(String cuenta){
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.SWITCHER_WS).append("/").append("consultaSaldo").toString();
        log.info("url consultaSaldoCero: {}",uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();

        try{


            Map<String, Object> map = new HashMap<>();
            map.put("cuenta", cuenta);  // Cuenta de ahorro Procrea
            log.info("body consultaSaldoCero: {}",gson.toJson(map));

            jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            log.info("jsonResponse consultaSaldoCero: {}",jsonResponse);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            if(resp.getCodigo() == 0){
                respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
            }else{
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ToolsR._T(resp.getMensaje()).toUpperCase());
            }
        }catch(Exception ex){
            ex.printStackTrace();
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "consultaClienteProcreaSaldoDisponible");
        }
        return respuestaSvc;
    }
}
