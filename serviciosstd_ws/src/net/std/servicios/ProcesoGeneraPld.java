package net.std.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import net.std.data.ConceptoPLD;
import net.std.data.CuentaPLDOBJ;
import net.std.request.GuardarPLDReq;

public class ProcesoGeneraPld implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraPld.class);

	public static RespuestaSVC procesar(Integer cuentaId){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_WS).append("/").append("guardarPld").toString();
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

			RespuestaSVC respEst = ClaveValorWS.getEstatus("ALTA");
			
			HeaderWS header = new HeaderWS();
			header.setIdEmpresa(Comun._L(Constantes.EMPRESA_ID));
			header.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			header.setIpHost(Comun._T(Constantes.HOST_ID));
			
			List<ConceptoPLD> conceptosPld = guardaConceptosPLD();

			CuentaPLDOBJ pld = new CuentaPLDOBJ();
			pld.setCuentaId(cuentaId);
			pld.setEstatusId(respEst.getErrores().getCodigoError() == 0 ? Comun._I(respEst.getBody().getValor("ID")) : 1 );
			pld.setIndProvRec(false);

			GuardarPLDReq req = new GuardarPLDReq();
			req.setHeader(header);
			req.setConceptos(conceptosPld);
			req.setDatos(pld);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(req));
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
	
	private static List<ConceptoPLD> guardaConceptosPLD(){
		List<ConceptoPLD> lst = new ArrayList<>();
		return lst;
	}
}

