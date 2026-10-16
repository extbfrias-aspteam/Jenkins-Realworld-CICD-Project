package functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.Collections;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.blu.seguridadKaiosama.Algoritmo;
//import com.blu.seguridadKaiosama.Criptography;
//import com.blu.seguridadKaiosama.PKITools;
//import com.blu.seguridadKaiosama.simetricKey.PrivateKeyComponent;
//import com.blu.seguridadKaiosama.simetricKey.PublicKeyComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FunctionActCuenta implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionActCuenta.class);
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String login_token = obtenerVariableEntorno("MS_DIGITALIZACION_AUTH_URL", "login_token");
	private static final String api_cue_act = obtenerVariableEntorno("MS_DIGITALIZACION_ACTIVAR_URL", "api_cue_act");
	private static final String user = obtenerVariableEntorno("MS_DIGITALIZACION_AUTH_USER", "user");
	private static final String pass = obtenerVariableEntorno("MS_DIGITALIZACION_AUTH_PASS", "pass");
	private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build();

	@Override
	public void service(final HttpRequest request, final HttpResponse response)
			throws IOException, GeneralSecurityException, URISyntaxException {
		String requestBody = request.getReader().lines().collect(Collectors.joining());
		log.info("Parametros recibidos son:->>>>>>>>>>>>>>>>>>>>>> " + requestBody);
		Map<String, Object> responseMessage = this.procesarSolicitud(requestBody);
		final BufferedWriter writer = response.getWriter();
		String jsonResponse = gson.toJson(responseMessage);
		writer.write(jsonResponse);
	}

	public Map<String, Object> procesarSolicitud(String requestBody) throws URISyntaxException {
		Map<String, Object> payload = this.normalizarPayload(requestBody);
		log.info("Payload normalizado:->>>>>>>>>>>>>>>>>>>>>> " + payload);

		String clabeDirecta = this.obtenerCadena(payload, "Clabe");
		if (!clabeDirecta.isBlank()) {
			return this.api_activar_cuenta_token(clabeDirecta);
		}

		String claveResultado = this.obtenerCadena(payload, "clave");
		List<Map<String, Object>> documentosPendientes = this.obtenerDocumentosPendientes(payload);

		if (!claveResultado.isBlank()) {
			if (documentosPendientes.isEmpty()) {
				log.info("No se encontraron documentos pendientes para la clave " + claveResultado + ". Se activa la cuenta.");
				return this.api_activar_cuenta_token(claveResultado);
			}
			log.info("La cuenta " + claveResultado + " aun tiene documentos pendientes o con error. No se activa.");
			return this.respuestaSinActivacion(claveResultado, documentosPendientes);
		}

		if (!documentosPendientes.isEmpty()) {
			return this.respuestaError("El mensaje contiene documentos pendientes, pero no incluye la clave para activar la cuenta.",
					documentosPendientes);
		}

		return this.respuestaError("No se encontro un payload valido con Clabe o clave para activar la cuenta.",
				Collections.emptyList());
	}

	private Map<String, Object> normalizarPayload(String requestBody) {
		if (requestBody == null || requestBody.isBlank()) {
			return new HashMap<>();
		}

		Object payload = gson.fromJson(requestBody, Object.class);
		return this.normalizarPayload(payload);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> normalizarPayload(Object payload) {
		if (payload instanceof Map<?, ?> payloadMap) {
			Object message = payloadMap.get("message");
			if (message instanceof Map<?, ?> messageMap) {
				Object data = messageMap.get("data");
				if (data instanceof String dataCodificada && !dataCodificada.isBlank()) {
					String mensajeDecodificado = new String(Base64.getDecoder().decode(dataCodificada), StandardCharsets.UTF_8);
					log.info("Mensaje Pub/Sub decodificado:->>>>>>>>>>>>>>>>>>>>>> " + mensajeDecodificado);
					return this.normalizarPayload(mensajeDecodificado);
				}
			}
			return new HashMap<>((Map<String, Object>) payloadMap);
		}

		if (payload instanceof List<?> payloadList) {
			Map<String, Object> payloadNormalizado = new HashMap<>();
			payloadNormalizado.put("documenot", payloadList);
			return payloadNormalizado;
		}

		return new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> obtenerDocumentosPendientes(Map<String, Object> payload) {
		Object documentos = payload.get("documenot");
		if (documentos instanceof List<?> listaDocumentos) {
			return (List<Map<String, Object>>) listaDocumentos;
		}
		return Collections.emptyList();
	}

	private String obtenerCadena(Map<String, Object> payload, String llave) {
		Object valor = payload.get(llave);
		return valor instanceof String ? ((String) valor).trim() : "";
	}

	private Map<String, Object> respuestaSinActivacion(String clave, List<Map<String, Object>> documentosPendientes) {
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("estatus", "PENDIENTE_VALIDACION");
		respuesta.put("activarCuenta", false);
		respuesta.put("Clabe", clave);
		respuesta.put("mensaje", "La cuenta no se activo porque aun existen documentos pendientes o con error.");
		respuesta.put("documentosPendientes", documentosPendientes);
		return respuesta;
	}

	private Map<String, Object> respuestaError(String mensaje, List<Map<String, Object>> documentosPendientes) {
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("estatus", "ERROR_VALIDACION");
		respuesta.put("activarCuenta", false);
		respuesta.put("mensaje", mensaje);
		respuesta.put("documentosPendientes", documentosPendientes);
		return respuesta;
	}

	public Map<String, Object> api_activar_cuenta_token(String clabe) throws URISyntaxException {
		log.info(">>>PETICIÓN ms-asp-digitalizacion-bloqueo-cuenta /activar>>>>>>");
		Map<String, String> requestBodyCifrado = new HashMap<>();
		requestBodyCifrado.put("clabe", clabe);
		String jsonBodys = gson.toJson(requestBodyCifrado);
		log.info(">>>URL>>>>>>" + api_cue_act);
		log.info(">>>BODY>>>>>>" + jsonBodys);
		String authorizationHeader = this.ConsultaToken();
		log.info(">>>Authorization>>>>>>" + authorizationHeader);
		if (authorizationHeader.isBlank() || authorizationHeader.startsWith("Error")
				|| authorizationHeader.equalsIgnoreCase("Token no encontrado")) {
			throw new IllegalArgumentException("Token no encontrado");
		}
		try {
			java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder().uri(new URI(api_cue_act))
					.headers("Content-Type","application/json")
					.headers("Authorization", "Bearer " +authorizationHeader)
					.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
					.POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBodys)).build();

			java.net.http.HttpResponse<String> httpResponse = client.send(httpRequestOCR, BodyHandlers.ofString());

			if (httpResponse.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> responseMessage = objectMapper.readValue(httpResponse.body(), Map.class);
				log.info(responseMessage);
				return responseMessage;
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> responseMessage = objectMapper.readValue(httpResponse.body(), Map.class);
				log.warn("Respuesta no exitosa del servicio de activacion: status={} body={}",
						httpResponse.statusCode(), httpResponse.body());
				return responseMessage;
			}
		} catch (IOException | InterruptedException e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			log.error("Error al activar la cuenta", e);
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String ConsultaToken() throws URISyntaxException {
		log.info("Auth token URL ms-asp-digitalizacion-bloqueo-cuenta: " + login_token);
		String username = user;
		String password = pass;
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", username);
		requestBody.put("password", password);
		String jsonBody = gson.toJson(requestBody);
		log.info(">>>URL TOKEN>>>>>>" + login_token);
		log.info(">>>Body>>>>>>" + jsonBody);
		try {
			java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder().uri(new URI(login_token))
					.headers("Content-Type", "application/json").version(HttpClient.Version.HTTP_2)
					.timeout(Duration.ofSeconds(60)).POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
					.build();

			java.net.http.HttpResponse<String> httpResponse = client.send(httpRequestOCR, BodyHandlers.ofString());

			if (httpResponse.statusCode() == 200) {
				String token = this.extraerToken(httpResponse.body());
				if (!token.isBlank()) {
					log.info("Token recibido del microservicio de bloqueo de cuenta.");
					return token;
				}
				log.warn("Token no encontrado en la respuesta del microservicio de bloqueo de cuenta");
				return "Token no encontrado";
			} else {
				log.warn("Error al obtener el token. Código de estado: " + httpResponse.statusCode());
				return "Error al obtener el token";
			}

		} catch (IOException | InterruptedException e) {
			log.error("Error en la autenticación", e);
			Thread.currentThread().interrupt();
			return "Error en la autenticación";
		}
	}

	private String extraerToken(String body) {
		if (body == null || body.isBlank()) {
			return "";
		}

		String bodyLimpio = body.trim().replace("\"", "");
		if (!bodyLimpio.startsWith("{")) {
			return bodyLimpio;
		}

		try {
			Map<?, ?> respuesta = gson.fromJson(bodyLimpio, Map.class);
			Object token = respuesta.get("token");
			if (token instanceof String tokenString && !tokenString.isBlank()) {
				return tokenString.trim();
			}
			Object accessToken = respuesta.get("accessToken");
			if (accessToken instanceof String accessTokenString && !accessTokenString.isBlank()) {
				return accessTokenString.trim();
			}
		} catch (Exception e) {
			log.warn("No fue posible parsear el token de la respuesta del microservicio", e);
		}

		return "";
	}

	private static String obtenerVariableEntorno(String preferida, String legado) {
		String valorPreferido = System.getenv(preferida);
		if (valorPreferido != null && !valorPreferido.isBlank()) {
			return valorPreferido;
		}

		String valorLegado = System.getenv(legado);
		return valorLegado == null ? "" : valorLegado;
	}

}
