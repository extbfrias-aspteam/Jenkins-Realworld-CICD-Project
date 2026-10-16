package net.cero.ahorro.test;

import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import net.cero.data.DepositoAhorroReq;
import net.cero.seguridad.utilidades.HeaderWS;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistraDepositoAhorroTest {
	public static void main(String[] args) {			
		String HOST = "http://192.168.0.122:9080/CEROAhorroWS/rest/registraDepositoAhorro";
		
		final Logger log = LogManager.getLogger(RegistraDepositoAhorroTest.class);


		String auth = Credentials.basic("ASP", "a5p2017$");
		Gson gson = new Gson();
		MediaType media = MediaType.parse("application/json; charset=utf-8");
		HeaderWS header = new HeaderWS();
		DepositoAhorroReq req = new DepositoAhorroReq();
		
		try {
			
			header.setIdUsuario(2996);
			header.setUsuarioClave("ISGOMEZ");
			req.setHeader(header);
			req.setBancoId(21);
			req.setCajaId(1);
			req.setCheque("12345");
			req.setCuenta("0023323300");
			req.setFecha(new Date());
			req.setFormaPago(3);
			req.setMonto((double) 50000);
			req.setMovimientoId(2);
			req.setObservaciones("Prueba de servicio de deposito de ahorro");
			
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
