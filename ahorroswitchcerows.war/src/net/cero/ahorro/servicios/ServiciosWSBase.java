package net.cero.ahorro.servicios;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ahorro.data.Respuesta;
import net.cero.ws.data.ToolsR;

/**
 * clase base para invocar WS
 */

public class ServiciosWSBase {
	private static Logger log = LogManager.getLogger(ServiciosWSBase.class);
	private static final String USR = "ASP";
	private static final String HID = "a5p2017$";
	/**
	 * constructor default
	 */
	public ServiciosWSBase() {
		super();
	}

	/**
	 * ejectua un WS
	 * @param servicio URL del servicio a invocar
	 * @param parametros parametros a enviar en el post
	 * @return
	 */
	public static Respuesta ejecuta(String servicio, String parametros) {
		String jsonResponse = null;
		Respuesta resp = new Respuesta();
		Gson gson = ToolsR.GBuilder();
		try {
			jsonResponse = ejecutaString( servicio,  parametros);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
		} catch (Exception ex) {
			log.error("ERROR DENTRO DEL METODO ejecuta",ex);
			resp.setCodigo(9);
			resp.setMensaje("ERROR AL EJECUTAR EL SERVICIO " + servicio.toUpperCase() + "(" + resp.getMensaje() + ")");
		}
		return resp;
	}


	public  static Respuesta ejecutaSinLogin(String servicio, String parametros) {
		//log.info("servicio: "+servicio+", parametros: "+parametros);
		Respuesta respuesta= null;
		String json=parametros.trim();
		respuesta=ejecuta(servicio, json);
		return respuesta;
	}
	
	private static String ejecutaString(String servicio, String parametros) {
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse = null;
		Respuesta resp = new Respuesta();
		log.info("url servicio: "+servicio+" parametros: "+parametros);
		try {
			authFilter = new HTTPBasicAuthFilter(USR, HID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(servicio);
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, parametros);
			jsonResponse = response.getEntity(String.class);
			log.info("jsonResponse: "+jsonResponse);
			
		} catch (Exception ex) {
			log.error("ERROR DENTRO DEL METODO ejecutaString",ex);
			resp.setCodigo(9);
			resp.setMensaje("ERROR AL EJECUTAR EL SERVICIO " + servicio.toUpperCase() + "(" + resp.getMensaje() + ")");
		}
		return jsonResponse;
	}
}
