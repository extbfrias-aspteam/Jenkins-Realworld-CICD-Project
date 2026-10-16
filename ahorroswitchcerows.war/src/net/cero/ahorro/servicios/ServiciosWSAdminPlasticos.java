/**
 * ServiciosWSAdminPlasticos.java
 * ASP Integra Opciones  2024-12-20
 * https://www.aspintegraopciones.com/fr/home/
 * @autor rodolfo
 */
package net.cero.ahorro.servicios;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.cero.ahorro.data.RequestvalidarMontoTransaccional;
import net.cero.ahorro.data.Respuesta;
import net.cero.ws.data.Constantes;
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.ToolsR;

/**
 * 
 */
public class ServiciosWSAdminPlasticos extends ServiciosWSBase {
    private static final Logger log = LogManager.getLogger(ServiciosWSAdminPlasticos.class);

    public static Respuesta validarMontoTransaccional(String cuenta, Double monto, HeaderWS header) {
	RequestvalidarMontoTransaccional entrada = new RequestvalidarMontoTransaccional();
	entrada.setCuenta(cuenta);
	entrada.setHeader(header);
	entrada.setImporte(monto);
	Gson gson = ToolsR.GBuilder();
	log.debug(Constantes.WS_VALIDAR_MONTO_TRANSACCIONAL);
	Respuesta res = ejecuta(Constantes.WS_VALIDAR_MONTO_TRANSACCIONAL, gson.toJson(entrada));
	if (res == null) {
	    res = new Respuesta();
	    res.setCodigo(15);
	    log.info("No se pudo evaluar");
	    res.setMensaje("No se pudo evaluar el monto de depósitos de la cuenta");
	    
	}
	log.debug(res.getCodigo());
	log.debug(res.getMensaje());
	return res;
    }

}
