package net.cero.ahorro.test;

import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;

import net.cero.data.ConsultaSaldoAhorroReq;
import net.cero.seguridad.utilidades.HeaderWS;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConsultaSaldoAhorroTest {
	public static void main(String[] args) {			
		String HOST = "http://172.17.8.11:8081/CEROAhorroWS/rest/consultaSaldoAhorro";
		
		final Logger log = LogManager.getLogger(ConsultaSaldoAhorroTest.class);


		String auth = Credentials.basic("ASP", "a5p2017$");
		Gson gson = new Gson();
		MediaType media = MediaType.parse("application/json; charset=utf-8");
		HeaderWS header = new HeaderWS();
		ConsultaSaldoAhorroReq req = new ConsultaSaldoAhorroReq();
		
		try {
			
			header.setIdUsuario(2996);
			header.setUsuarioClave("ISGOMEZ");
			req.setHeader(header);
			req.setCuenta("0383010700");
			req.setFechaConsulta(new Date());
			
			String body = gson.toJson(req);

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(HOST).post(RequestBody.create(media, body))
					.header("Authorization", auth).build();

			try {
				Response response = client.newCall(request).execute();
				log.info("SYNC CALL: " + response.body().string());
			} catch (IOException e) {
				log.error(e.getMessage());
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
