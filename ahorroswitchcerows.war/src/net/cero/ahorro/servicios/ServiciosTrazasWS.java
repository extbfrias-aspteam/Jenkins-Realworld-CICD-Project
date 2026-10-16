package net.cero.ahorro.servicios;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.Constantes;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.cero.spring.config.Respuesta;

public class ServiciosTrazasWS implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ServiciosTrazasWS.class);

	//@RequestMapping(value="/envioMensajeID", method=RequestMethod.POST)
	public static RespuestaSVC envioMensajeID(String trazas) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerSaldoTransaccion").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			//map.put("cuenta", cuentaAhorro);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			//log.error(String.format("Error: consultarSaldoAhorro::%s\n%s", cuentaAhorro, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "consultarSaldoAhorro");
		}
		return respuestaSvc;
	}
}
