package net.cero.utilidades;

import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.servicios.ServiciosTransaccionesWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class ValidacionesAhorroUtils {

    public boolean validaReglaMontoMaximo(Map<String, Object> map)
    {
        boolean validReglaMontoMaximo=true;
        if(map.containsKey("tipoClave") && map.get("tipoClave") != null)
        {
            log.info("Clave movimiento a validar si es para devolucion: {}",map.get("tipoClave"));
            RespuestaSVC respMovimiento = ServiciosTransaccionesWS.getTipoTransaccion(ToolsR._T(map.get("tipoClave")));
            if(respMovimiento.getErrores().getCodigoError() == 0)
            {
                String paraDevolucionStr = ToolsR._T(respMovimiento.getBody().getValor("PARA_DEVOLUCION"));
                log.info("PARA_DEVOLUCION valor: {}",paraDevolucionStr);
                if(!StringUtils.isBlank(paraDevolucionStr))
                {
                    boolean paraDevolucion = Boolean.parseBoolean(paraDevolucionStr);
                    if(paraDevolucion)
                        validReglaMontoMaximo = false;
                }
            }
        }
        return validReglaMontoMaximo;
    }
}
