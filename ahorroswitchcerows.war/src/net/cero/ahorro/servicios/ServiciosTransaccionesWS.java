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

public class ServiciosTransaccionesWS implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ServiciosTransaccionesWS.class);

	//@RequestMapping(value="/grabarTransaccion", method=RequestMethod.POST)
	public static RespuestaSVC depositarAhorro(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("grabarDepositarTransaccion").toString();
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

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error al hacer el dep\u00F3sito, consulte con el responsable del \u00E1rea");
		}
		return respuestaSvc;
	}


	//@RequestMapping(value="/grabarTransaccion", method=RequestMethod.POST)
	public static RespuestaSVC retirarAhorro(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("grabarRetirarTransaccion").toString();
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

			log.info("grabarRetirarTransaccion: request {}",gson.toJson(map));
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			log.info("grabarRetirarTransaccion: response {}",jsonResponse);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error al hacer el retiro, consulte con el responsable del \u00E1rea");
		}
		return respuestaSvc;
	}


	//@RequestMapping(value="/grabarBitacora", method=RequestMethod.POST)
	public static RespuestaSVC bitacoraAhorro(Map<String, Object> map) {

		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("grabarBitacora").toString();
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

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "bitacoraAhorro");
		}
		return respuestaSvc;
	}

	//@RequestMapping(value="/grabarTransaccion", method=RequestMethod.POST)
	public static RespuestaSVC TransaccionAhorro(Map<String, Object> map) {

		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("grabarTransaccion").toString();
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

			log.info("grabarTransaccion: request: {}",gson.toJson(map));
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			log.info("grabarTransaccion: response: {}",jsonResponse);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "TransaccionAhorro");
		}
		return respuestaSvc;
	}

	public static RespuestaSVC leerSaldoCuentaTabla(Map<String, Object> map) {

		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerSaldoCuenta").toString();
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

			log.info("leerSaldoCuentaTabla request: {}",map);
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			log.info("leerSaldoCuentaTabla response: {}",jsonResponse);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "leerSaldoCuentaTabla");
		}
		return respuestaSvc;
	}


	public static RespuestaSVC getTipoTransaccion(String clave) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("claveValorTipoTransaccion").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();
		Map<String, Object> map = new HashMap<>();
		map.put("clave", clave);

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "getTipoTransaccion");
		}
		return respuestaSvc;
	}
	
}
