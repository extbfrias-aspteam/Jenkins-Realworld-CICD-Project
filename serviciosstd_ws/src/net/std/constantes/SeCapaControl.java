package net.std.constantes;

import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/*
import net.cero.seguridad.utilidades.CapaControlReqt;
import net.cero.seguridad.utilidades.CapaControlResp;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.RespuestaEnum;
*/

public class SeCapaControl{
	private static final Logger LOG = LogManager.getLogger(SeCapaControl.class);
	private static final String ERROR_HTTP = "Codigo de error HTTP: ";
	private Client client;
	
	private static final String SERVICIO = "capacontrol";
	
	public SeCapaControl(){
		iniClient();
	}
	/**
	 * Crear nuevo Canal
	 * @param seCanal Objeto Canal
	 * @return respuesta
	 */
	public CapaControlResp valida(CapaControlReqt request) {
		CapaControlResp response = null;
		ClientResponse clientResponse = null;
        
		response = new CapaControlResp();
        
		WebResource webResource = client.resource(Constantes.ADMIN_SEG_WS).path(SERVICIO).path("valida");
        LOG.info("Llama al servicio " + webResource.getURI());

        try {
        	clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
        	
        	if (clientResponse.getStatus() != 200) {
    			response.setCodeStatus(RespuestaEnum.FAIL.name());
    			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + ERROR_HTTP	+ clientResponse.getStatus());

    		} else {
    			response = clientResponse.getEntity(CapaControlResp.class);
    		}
		} catch (Exception e) {
			response.setCodeStatus(RespuestaEnum.FAIL.name());
			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + e.getMessage());
		}

		
		LOG.info(response.getMessageStatus());
		
		if("1".equals(Constantes.BYPASS)){
        	response.setCodeStatus(RespuestaEnum.SUCCESS.name());
		}
		
		return response;
	}
	
	public CapaControlResp escucha(CapaControlReqt request) {
		CapaControlResp response = null;
		ClientResponse clientResponse = null;
        
		response = new CapaControlResp();
        
		WebResource webResource = client.resource(Constantes.ADMIN_SEG_WS).path(SERVICIO).path("escucha");
        LOG.info("Llama al servicio " + webResource.getURI());

        try {
        	clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
        	
        	if (clientResponse.getStatus() != 200) {
    			response.setCodeStatus(RespuestaEnum.FAIL.name());
    			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + ERROR_HTTP	+ clientResponse.getStatus());

    		} else {
    			response = clientResponse.getEntity(CapaControlResp.class);
    		}
		} catch (Exception e) {
			response.setCodeStatus(RespuestaEnum.FAIL.name());
			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + e.getMessage());
		}

		
		LOG.info(response.getMessageStatus());
		
		if("1".equals(Constantes.BYPASS)){
        	response.setCodeStatus(RespuestaEnum.SUCCESS.name());
		}
		
		return response;
	}
	public CapaControlResp login(CapaControlReqt request) {
		CapaControlResp response = null;
		ClientResponse clientResponse = null;
        
		response = new CapaControlResp();
        
		WebResource webResource = client.resource(Constantes.ADMIN_SEG_WS).path(SERVICIO).path("login");
        LOG.info("Llama al servicio " + webResource.getURI());

        try {
        	clientResponse = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
        	
        	if (clientResponse.getStatus() != 200) {
    			response.setCodeStatus(RespuestaEnum.FAIL.name());
    			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + ERROR_HTTP	+ clientResponse.getStatus());

    		} else {
    			response = clientResponse.getEntity(CapaControlResp.class);
    		}
		} catch (Exception e) {
			response.setCodeStatus(RespuestaEnum.FAIL.name());
			response.setMessageStatus(RespuestaEnum.FAIL.getResultado() + e.getMessage());
		}

		
		LOG.info(response.getMessageStatus());
		
		if("1".equals(Constantes.BYPASS)){
        	response.setCodeStatus(RespuestaEnum.SUCCESS.name());
		}
		
		return response;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	/**
	 * Inicializa cliente REST
	 */
	public void iniClient(){
    	ClientConfig clientConfig = new DefaultClientConfig();
    	clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    	client = Client.create(clientConfig);
	}
}
