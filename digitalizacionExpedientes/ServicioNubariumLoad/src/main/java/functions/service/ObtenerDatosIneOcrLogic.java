package functions.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import functions.dto.IneOcrReqOBJ;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ObtenerDatosIneOcrLogic {

	private static final Logger log = LogManager.getLogger(ObtenerDatosIneOcrLogic.class);
	
	public String obtenerDatosIne(IneOcrReqOBJ ine, String usuario, String pass) {
		String jsonResp = "";
		String HOST = "https://ine.nubarium.com:443/ocr/obtener_datos";
		log.info("## Host OCR NUBARIUM: " + HOST);

		String auth = Credentials.basic(usuario, pass);
		Gson gson = new Gson();
		MediaType media = MediaType.parse("application/json; charset=utf-8");

		try {
			String body = gson.toJson(ine);
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(HOST).post(RequestBody.create(media, body))
					.header("Authorization", auth).build();
			try {
				Response response = client.newCall(request).execute();
				jsonResp = response.body().string();
				//log.info("## SYNC CALL OCR NUBARIUM: " + jsonResp);
				//log.info("## SYNC CALL OCR NUBARIUM: " + jsonResp);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("Ocurrio un error en la llamada" + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResp = "";
			log.error(e.getMessage());
		}
		
		return jsonResp;
	}
}
