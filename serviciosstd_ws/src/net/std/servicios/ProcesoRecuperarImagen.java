package net.std.servicios;

import java.io.Serializable;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.mx.Req.ImagenAlfrescoReq;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;


public class ProcesoRecuperarImagen implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoRecuperarImagen.class);

	public static RespuestaSVC procesar(String repositorio_id){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		/* CREA EL CONTENIDO */
		ImagenAlfrescoReq requestParameters = new ImagenAlfrescoReq();
		try {
			requestParameters.setId(repositorio_id);;
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}

		/* RECUPERAR IMAGEN */
		try{
			log.info("ENTRA A ALFRESCO A BUSCAR IMAGEN : " + requestParameters.getId()) ;
		
			respuestaSvc = procesarImagen(requestParameters, "BuscaImagen");
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				log.info("PRIMER ERROR Y VUELVE A EJECUTAR : " + respuestaSvc.getErrores().getDescError()) ;
				
				/* REPITE LA BUSQUEDA POR SI NO LO ENCONTRO LA PRIMERA VEZ */
				respuestaSvc = procesarImagen(requestParameters, "BuscaImagen");
				if(respuestaSvc.getErrores().getCodigoError() != 0){
					log.info("SEGUNDO ERROR : " + respuestaSvc.getErrores().getDescError()) ;
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_LEER_ALFRESCO, ErrProd.desc(ErrProd.ERROR_LEER_ALFRESCO, respuestaSvc.getErrores().getDescError()));
					return respuestaSvc;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}

		return respuestaSvc;
	}

	public static RespuestaSVC procesarImagen(ImagenAlfrescoReq alfresco, String servicio){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		String uri = new StrBuilder(Constantes.ALFRESCO_WS).append("/").append(servicio).toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes. PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, new Gson().toJson(alfresco));
			if(response.getStatus() == HttpStatus.SC_OK){ /* 200 */
				respuestaSvc.getBody().addValor("IMAGEN", response.getEntity(String.class));
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO",Errores.ERROR_INESPERADO, response.getEntity(String.class));
			}
			response.close();
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ErrProd.desc(ErrProd.ERROR_INESPERADO, ex.getMessage()));
		}
		return respuestaSvc;
	}
	
}

