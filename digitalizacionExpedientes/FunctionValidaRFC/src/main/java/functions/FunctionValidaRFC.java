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

public class FunctionValidaRFC implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionValidaRFC.class);

	private static final Gson gson = new Gson();
	// PARAMETRIZAR
	private static final String USER_NUBARIUM = System.getenv("USER_NUBARIUM");
	private static final String PASS_NUBARIUM = System.getenv("PASS_NUBARIUM");
	private static final String RFC_ENDPOINT = System.getenv("RFC_ENDPOINT");//"RFC_ENDPOINT=https://sat.nubarium.com/sat/valida_rfc";

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		String body = gson.fromJson(request.getReader(), String.class);
		log.info("parametros recibidos son: " + body);
		String respuesta = validacionCurpNubarium(body);
		final BufferedWriter writer = response.getWriter();
		writer.write(respuesta + "");
	}

	private String validacionCurpNubarium(String rfc) {
		String respuesta = "";
		Map<String, String> map = new HashMap<String, String>();
		log.info("Dato rfc obtenido :: " + rfc);
		map.put("rfc", rfc);
		try {
			respuesta = ejecutaServicioNubarium(gson.toJson(map), RFC_ENDPOINT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	private String ejecutaServicioNubarium(String jsonReq, String host) {
		String respuesta = "";
		log.info("host :: " + host);
		log.info("body :: " + jsonReq);

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		String auth = Credentials.basic(USER_NUBARIUM, PASS_NUBARIUM);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(host).post(RequestBody.create(media, jsonReq))
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