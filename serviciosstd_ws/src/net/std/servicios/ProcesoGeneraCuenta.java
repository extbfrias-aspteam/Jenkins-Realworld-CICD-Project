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
import net.std.data.CuentaOBJ;
import net.std.request.GuardarCuentaReq;

public class ProcesoGeneraCuenta implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraCuenta.class);

	public static RespuestaSVC procesar(CuentaOBJ cuenta){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_WS).append("/").append("guardarCuenta").toString();
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
			
			HeaderWS header = new HeaderWS();
			header.setIdCanalAtencion(Comun._L(Constantes.CANAL_ID));
			header.setIdEmpresa(Comun._L(Constantes.EMPRESA_ID));
			header.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			header.setIpHost(Comun._T(Constantes.HOST_ID));
			
			GuardarCuentaReq req = new GuardarCuentaReq();
			req.setHeader(header);
			req.setCuenta(cuenta);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(req));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			
			if(resp.getCodigo() == 0){
				CuentaOBJ cuentaTmp = gson.fromJson(resp.getData(), CuentaOBJ.class);
				respuestaSvc.getBody().addValor("CUENTA_CREADA", cuentaTmp);
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

