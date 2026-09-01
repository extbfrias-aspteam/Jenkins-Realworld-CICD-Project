package net.cero.ahorro.logica;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

import net.cero.data.IneOcrReqOBJ;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Log4j2
public class ObtenerDatosIneOcrLogic {

	public String obtenerDatosIne(IneOcrReqOBJ ine) {
		String jsonResp = "";
		String HOST = "https://ine.nubarium.com:443/ocr/obtener_datos";
		log.info("## Host OCR NUBARIUM: " + HOST);
		final Logger log = LogManager.getLogger(ObtenerDatosIneOcrLogic.class);

		String auth = Credentials.basic("aspintegraopciones", "_4rg3tn1Xps4");
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
				log.error(e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResp = "";
			log.error(e.getMessage());
		}
		
		return jsonResp;
	}
}
