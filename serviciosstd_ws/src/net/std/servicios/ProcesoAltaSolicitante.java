package net.std.servicios;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.Errores;
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Respuesta;
import net.std.data.DomicilioOBJ;
import net.std.data.PersonaOBJ;
import net.std.data.SolicitanteOBJ;
import net.std.request.AltaSolicitanteReq;
import net.std.request.DomicilioReq;
import net.std.request.ReferenciaReq;
import net.std.response.ReferenciaRes;

public class ProcesoAltaSolicitante implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoAltaSolicitante.class);

	public static RespuestaSVC procesar(SolicitanteOBJ sol, DomicilioOBJ dom){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.NUCLEO_CARTERA_WS).append("/").append("altaSolicitante").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();
		String numSol = null;

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes. PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);
			
			AltaSolicitanteReq solReq = new AltaSolicitanteReq();
			
			HeaderWS header = new HeaderWS();
			header.setIdUsuario(9);
			
			solReq.setDomicilio(dom);
			solReq.setSolicitante(sol);
			solReq.setHeader(header);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(solReq));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				numSol = gson.fromJson(resp.getData(), String.class);
				respuestaSvc.getBody().addValor("CLIENTE_ID", Comun._T(numSol));
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, resp.getMensaje());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuestaSvc;
	}
}

