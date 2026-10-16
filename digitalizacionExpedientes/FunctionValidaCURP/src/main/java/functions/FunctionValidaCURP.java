package functions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FunctionValidaCURP implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionValidaCURP.class);

	private static final Gson gson = new Gson();
	//PARAMETRIZAR
	private static final String USER_NUBARIUM = System.getenv("USER_NUBARIUM");
	private static final String PASS_NUBARIUM = System.getenv("PASS_NUBARIUM");
	private static final String CURP_ENDPOINT = System.getenv("CURP_ENDPOINT");//"CURP_ENDPOINT=https://curp.nubarium.com/renapo/v2/valida_curp";

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		String body = gson.fromJson(request.getReader(), String.class);
		log.info("parametros recibidos son: " + request.getReader());
		String vCurp = validacionCurpNubarium(body, "");
		final BufferedWriter writer = response.getWriter();
		log.info("Deja el hilo corriendo....");
		writer.write(vCurp+"");
	}

	private String validacionCurpNubarium(String curp, String solicitanteId) {
		String respuesta = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("curp", curp);
		try {
			respuesta = ejecutaServicioNubarium(gson.toJson(map));
			log.info("respuesta: " + respuesta);
			
//			if (respuesta != null && !respuesta.isEmpty()) {
//				ValidaCurpResponseOBJ resp = gson.fromJson(respuesta, ValidaCurpResponseOBJ.class);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	private String ejecutaServicioNubarium(String jsonReq) {
		String respuesta = "";
		log.info("host :: " + CURP_ENDPOINT);
		log.info("body :: " + jsonReq);

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		String auth = Credentials.basic(USER_NUBARIUM, PASS_NUBARIUM);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(CURP_ENDPOINT).post(RequestBody.create(media, jsonReq))
				.header("Authorization", auth).build();
		try {
			Response response = client.newCall(request).execute();
			respuesta = response.body().string();
			log.info("SYNC CALL : " + respuesta);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return respuesta;
	}

}