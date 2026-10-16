package net.cero.ahorro.serviciosPin;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

public class ServiciosNipWS implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ServiciosNipWS.class);

	//@RequestMapping(value="/grabarAhorroNip", method=RequestMethod.POST)
	public static RespuestaSVC grabarNip(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("grabarAhorroNip").toString();
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
			log.error(String.format("Error: grabarNip::%d\n%s", map.get("cuentaID"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "grabarNip");
		}
		return respuestaSvc;
	}

	//@RequestMapping(value="/actualizarAhorroNip", method=RequestMethod.POST)
	public static RespuestaSVC actualizarNip(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("actualizarAhorroNip").toString();
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
			log.error(String.format("Error: actualizarNip::%d\n%s", map.get("cuentaID"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "actualizarNip");
		}
		return respuestaSvc;
	}

	//@RequestMapping(value="/leerAhorroUltimoNIP", method=RequestMethod.POST)
	public static RespuestaSVC consultarNip(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerAhorroUltimoNIP").toString();
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
			log.error(String.format("Error: consultarNip::%d\n%s", map.get("cuentaID"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "consultarNip");
		}
		return respuestaSvc;
	}

	//@RequestMapping(value="/LeerAhorroNIPs", method=RequestMethod.POST)
	public static RespuestaSVC consultar100Nips(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("LeerAhorroNIPs").toString();
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
			log.error(String.format("Error: listarNips::%d\n%s", map.get("cuentaID"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "listarNips");
		}
		return respuestaSvc;
	}

	@RequestMapping(value="/generarPinBlockAhorro", method=RequestMethod.POST)
	public static RespuestaSVC obtenerPinBlock(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("generarPinBlockAhorro").toString();
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
			log.error(String.format("Error: obtenerPinBlock::%s\n%s", map.get("tarjeta"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "obtenerPinBlock");
		}
		return respuestaSvc;
	}

	@RequestMapping(value="/generarPinAhorro", method=RequestMethod.POST)
	public static RespuestaSVC obtenerPin(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("generarPinAhorro").toString();
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
			log.error(String.format("Error: obtenerPin::%s\n%s", map.get("tarjeta"), ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "obtenerPin");
		}
		return respuestaSvc;
	}

}
