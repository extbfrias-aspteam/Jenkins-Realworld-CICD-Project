package net.std.servicios;

//import java.io.File;
import java.io.Serializable;
//import java.text.SimpleDateFormat;

import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import net.std.data.ExpedienteOBJ;
import net.std.response.ExpedienteRes;

import java.util.Base64;
//import java.util.Calendar;



public class ProcesoGeneraExpediente implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoGeneraExpediente.class);

	public static RespuestaSVC procesar(ExpedienteOBJ exp){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		/* CREA EL CONTENIDO */
		ImagenAlfrescoReq requestParameters = new ImagenAlfrescoReq();
		try {
			String jsondecoded = new Gson().fromJson(exp.getImagen(), String.class);
			log.info(jsondecoded);
			byte[] bytedec = Base64.getDecoder().decode(jsondecoded.getBytes());
			
			/* VERIFICA QUE EL ARCHIVO NO SOBREPASE LOS 10 MB */
			if(bytedec.length > (10*(1024*1024))){
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_LONGITUD_ARCHIVO, ErrProd.desc(ErrProd.ERROR_LONGITUD_ARCHIVO, "10 MB"));
				return respuestaSvc;
			}
			
			requestParameters.setFile(bytedec);
			requestParameters.setCarpeta(exp.getRutaAlfresco());
			requestParameters.setNombreImagen(exp.getNombre());

		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}


		/* CREA LA CARPETA DESTINO */
		try{
			respuestaSvc = procesarImagen(requestParameters, "CrearCarpeta");
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INSERTAR_ALFRESCO, 
						ErrProd.desc(ErrProd.ERROR_INSERTAR_ALFRESCO, respuestaSvc.getErrores().getDescError()));
				return respuestaSvc;
			}

			respuestaSvc = procesarImagen(requestParameters, "SubirImagenes");
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INSERTAR_ALFRESCO,
						ErrProd.desc(ErrProd.ERROR_INSERTAR_ALFRESCO, respuestaSvc.getErrores().getDescError()));
				return respuestaSvc;
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
				ExpedienteRes expImagen = new Gson().fromJson(response.getEntity(String.class), ExpedienteRes.class);
				respuestaSvc.getBody().addValor("RESULTADO", expImagen);

			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO",Errores.ERROR_INESPERADO, response.getEntity(String.class));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ErrProd.desc(ErrProd.ERROR_INESPERADO, ex.getMessage()));
		}
		return respuestaSvc;
	}
	
}

