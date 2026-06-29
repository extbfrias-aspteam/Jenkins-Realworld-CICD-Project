package net.std.servicios;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProcesoBitLogger implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ProcesoBitLogger.class);

	public static void procesar(String proceso, String dato, String observaciones) {
		//RespuestaSVC respuestaSvc = new RespuestaSVC();
		String uri = new StrBuilder(Constantes.SERVICIO_STD_WS).append("/").append("bitLoggerStd").toString();
		log.info(uri);
		/*WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;*/
		Gson gson = new Gson();

		try{
			/*authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);*/
			
			Map<String, String> map = new HashMap<>();
			map.put("proceso", Comun._T(proceso));
			map.put("dato", Comun._T(dato));
			map.put("observaciones", Comun._T(observaciones));
			
			MediaType media = MediaType.parse("application/json; charset=utf-8");
			String body = gson.toJson(map);
			
			String auth = Credentials.basic(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			
			OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();
			Request request = new Request.Builder().url(uri).post(RequestBody.create(media, body))
					.header("Authorization", auth).build();
			
			client.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					log.info(response.code()+": "+response.message());
					/* 13 DIC 2021 SE AGREGA EL METODO CERRAR, YA QUE SE QUEDA ABIERTO Y EL PROCESO SE CICLA*/
					response.close();
				}	
				
				@Override
				public void onFailure(Call call, IOException e) {
					log.info(e.getMessage());
					
				}
			});
			
			

			/*ClientResponse response = webResource.type("application/json").post(ClientResponse.class, new Gson().toJson(map));
			if(response.getStatus() == HttpStatus.SC_OK){ /* 200 */
			/*	respuestaSvc = new Gson().fromJson(response.getEntity(String.class), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO",Errores.ERROR_INESPERADO, response.getEntity(String.class));
			}*/
		}catch(Exception ex){
			ex.printStackTrace();
			//respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}
		//return respuestaSvc;
	}
}


