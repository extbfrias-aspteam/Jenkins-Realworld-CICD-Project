package net.std.servicios;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import net.std.constantes.Constantes;

public class ServicioValidarMontoTransaccional implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ServicioValidarMontoTransaccional.class);

	public static Boolean process(String cuenta, BigDecimal importe) {
		String uri = Constantes.WS_ADMIN_PLA + "/validarMontoTransaccional";
        log.info(uri);
        WebResource webResource;
		final HTTPBasicAuthFilter authFilter;
        Client client;
        String responseJson;
        
        try{
			authFilter = new HTTPBasicAuthFilter("ASP", "a5p2017$");
            client = Client.create();
			client.addFilter(authFilter);
            webResource = client.resource(uri);
            
            JsonObject header = new JsonObject();
            header.addProperty("idUsuario", 9);
            
            JsonObject json = new JsonObject();
            json.addProperty("cuenta", cuenta);
            json.addProperty("importe", importe);
            json.add("header", header);
            
            log.info(json.toString());
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, json.toString());
			log.info(String.format("Codigo de Respuesta : %d, %s", response.getStatus(), response.getStatusInfo()));

			if(response.getStatus() != 200){
				responseJson = response.getEntity(String.class);
	            log.info(new Gson().toJson(responseJson));
				return false;
			}
			
            responseJson = response.getEntity(String.class);
			Map<String, String> mapping = new Gson().fromJson(responseJson, new TypeToken<HashMap<String, String>>() {}.getType());
            log.info(new Gson().toJson(mapping));
            return Integer.valueOf(mapping.get("codigo").toString()) == 0;
        }catch(Exception ex){
        	log.error(ex.getMessage());
            return false;
        }
    }

}