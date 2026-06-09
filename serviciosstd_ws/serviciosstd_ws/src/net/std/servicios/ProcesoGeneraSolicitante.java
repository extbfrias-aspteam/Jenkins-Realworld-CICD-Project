package net.std.servicios;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.Respuesta;
import net.std.data.CuentaOBJ;
import net.std.data.DomicilioOBJ;
import net.std.data.SolicitanteOBJ;
import net.std.request.AltaSolicitanteReq;

@SuppressWarnings("unused")
public class ProcesoGeneraSolicitante implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraSolicitante.class);
	
	
	public static RespuestaSVC procesar(SolicitanteOBJ sol, DomicilioOBJ dom){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		
		//String servicio = "M".equals(Comun._T(sol.getTipoPersona())) ? "altaSolicitanteMoral" : "altaSolicitante";
		String servicio = "M".equals(Comun._T(sol.getTipoPersona())) ? "altaSolicitanteMoral" : "altaSolicitanteMinimo";
		String uri = new StrBuilder(Constantes.NUCLEO_CARTERA_WS).append("/").append(servicio).toString(); 
		
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();
		
		log.info(uri);

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes. PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);
			
			HeaderWS header = new HeaderWS();
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			
			AltaSolicitanteReq altaSol = new AltaSolicitanteReq();
			altaSol.setHeader(header);
			altaSol.setSolicitante(sol);
			altaSol.setDomicilio(dom);

			if("F".equals(sol.getTipoPersona())){
				altaSol.setIgnorarRfcCurp(false);
				altaSol.setProveedor(false);
			}else{
				altaSol.setIgnorarRfcCurp(false);
				altaSol.setProveedor(false);
			}
			
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(altaSol));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			
			if(resp.getCodigo() == 0){
				String clienteID = Comun._T(resp.getData()).replace("\"", "");
				respuestaSvc.getBody().addValor("CLIENTE_ID", clienteID);
			}else{
				respuestaSvc.getErrores().addCodigo(null, resp.getCodigo(), resp.getMensaje());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuestaSvc;
	}
}

