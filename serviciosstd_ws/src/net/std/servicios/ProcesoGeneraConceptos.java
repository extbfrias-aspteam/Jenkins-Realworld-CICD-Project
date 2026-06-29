package net.std.servicios;

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

import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.Respuesta;

public class ProcesoGeneraConceptos implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraConceptos.class);

	public static RespuestaSVC procesar(Integer productoID, Integer cuentaID){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_WS).append("/").append("copiarByProductoID2CuentaID").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();
		
		try{
			authFilter = new HTTPBasicAuthFilter(Constantes. PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);
			
			Map<String, Object> map = new HashMap<>();
			map.put("cuentaID", Comun._T(cuentaID));
			map.put("productoID", Comun._T(productoID));
			map.put("usuarioID", Comun._T(Constantes.USUARIO_ID));
			map.put("estatus", "ALTA");

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_CREAR_CUENTA_CONCEPTOS, Errores.desc(Errores.ERROR_CREAR_CUENTA_CONCEPTOS));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuestaSvc;
	}
}

