package net.cero.multiple.servicios;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import net.cero.spring.config.Respuesta;
import net.cero.ws.data.BuscarComisionistaReferenciaReq;
import net.cero.ws.data.Constantes;

public class ServiciosBusquedaComisionistaWS implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ServiciosBusquedaComisionistaWS.class);
	
	public static Respuesta procesa(String referencia){
		
		Respuesta resp = new Respuesta();
		Gson gson = new Gson();
		
		String url=Constantes.VENTANILLA_WS+"/buscarComisionistaReferencia";
		log.info(url);
		
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		
		authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
		BuscarComisionistaReferenciaReq req = new BuscarComisionistaReferenciaReq();
		req.setReferencia(referencia);
		
		client = Client.create();
		client.addFilter(authFilter);
		webResource = client.resource(url);
		
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(req));
		jsonResponse = response.getEntity(String.class);
		resp = gson.fromJson(jsonResponse, Respuesta.class);
		
		return resp;
	}
}
