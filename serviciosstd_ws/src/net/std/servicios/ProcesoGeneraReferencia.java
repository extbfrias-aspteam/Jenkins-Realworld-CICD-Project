package net.std.servicios;

import java.io.Serializable;

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
import net.std.request.ReferenciaReq;
import net.std.response.ReferenciaRes;

public class ProcesoGeneraReferencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraReferencia.class);

	public static RespuestaSVC procesar(Integer productoId, Integer tipoReferenciaId){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.CREDITO_WS).append("/").append("generarReferencia").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();
		ReferenciaRes referencia = null;

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes. PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);
			
			HeaderWS header = new HeaderWS();
			ReferenciaReq req = new ReferenciaReq();
			
			header.setIdEmpresa(Comun._L(Constantes.EMPRESA_ID));
			header.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			header.setIpHost(Comun._T(Constantes.HOST_ID));
			
			req.setHeader(header);
			req.setProducto(productoId);
			req.setTipoReferencia(tipoReferenciaId);   // <<--- de donde viene?

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(req));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				referencia = gson.fromJson(resp.getData(), ReferenciaRes.class);
				respuestaSvc.getBody().addValor("REFERENCIA", Comun._T(referencia.getReferencia()));
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

