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

import functions.dto.IneOcrRequestOBJ;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FunctionValidaINE implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionValidaINE.class);

	private static final Gson gson = new Gson();

	private static final String USER_NUBARIUM = System.getenv("USER_NUBARIUM");
	private static final String PASS_NUBARIUM = System.getenv("PASS_NUBARIUM");
	private static final String INE_ENDPOINT = System.getenv("INE_ENDPOINT");//INE_ENDPOINT=https://ine.nubarium.com/ine/v2/valida_ine;

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		IneOcrRequestOBJ body = gson.fromJson(request.getReader(), IneOcrRequestOBJ.class);
		log.info("parametros recibidos son: " + body);
		// verificar documento en caso de ser ine validar

		Map<String, String> map = new HashMap<String, String>();
		map.put("cic", body.cic());
		map.put("identificadorCiudadano", body.identificadorCiudadano());

		String infoINE = validacionINE(gson.toJson(map));
		final BufferedWriter writer = response.getWriter();
		log.info("Deja el hilo corriendo....");
		writer.write(infoINE+"");
	}

	
	private String validacionINE(String jsonReq) {
		String respuesta = validacionINENubarium(jsonReq);
		
//		ValidaINEResponseOBJ body = gson.fromJson(respuesta, ValidaINEResponseOBJ.class);
		
		return respuesta;
	}
	
	
	private String validacionINENubarium(String jsonReq) {
		String respuesta = "";
		log.info("host :: " + INE_ENDPOINT);
		log.info("body :: " + jsonReq);

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		String auth = Credentials.basic(USER_NUBARIUM, PASS_NUBARIUM);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(INE_ENDPOINT).post(RequestBody.create(media, jsonReq))
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