package topico;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Base64;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.pubsub.v1.Message;
import com.google.events.cloud.pubsub.v1.MessagePublishedData;
import com.google.gson.Gson;

import io.cloudevents.CloudEvent;
import topico.configuration.cloudsql.CloudSqlConnectionPool;
import topico.dao.DocumentoRegistroValidacionImpl;
import topico.dto.Respuesta;
import topico.dto.SCbitacora;
import topico.dto.ValidacionDocumentos;

public class PubSubNubariumValidacionLeerApp implements CloudEventsFunction {
	private static final Logger logger = Logger.getLogger(PubSubNubariumValidacionLeerApp.class.getName());

	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String URL_NOTIFICA_DOCUMENTO = System.getenv("HOST_COMUNICA");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db
	private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build();
	DocumentoRegistroValidacionImpl documentoUpdate = new DocumentoRegistroValidacionImpl();

	private ObjectMapper objetMapper = new ObjectMapper();

	@Override
	public void accept(CloudEvent event) throws GeneralSecurityException {
		DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
		try {
			System.out.println("Exist Connection " + pool.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("event.getData():: "+event.getData());
		System.out.println("event.getId():: "+event.getId());
		System.out.println("event.getSubject():: "+event.getSubject());
		System.out.println("event:: "+event);
		// Get cloud event data as JSON string
		String cloudEventData = new String(event.getData().toBytes());
		System.out.println("cloudEventData:::" + cloudEventData);
		// Decode JSON event data to the Pub/Sub MessagePublishedData type
		Gson gson = new Gson();
		MessagePublishedData data = gson.fromJson(cloudEventData, MessagePublishedData.class);
		// Get the message from the data
		Message message = data.getMessage();
		// Get the base64-encoded data from the message & decode it
		String encodedData = message.getData();
		String decodedData = new String(Base64.getDecoder().decode(encodedData));
		// Log the message
		logger.info("Resultado de la validacion: " + decodedData);
		Respuesta respuesta = gson.fromJson(decodedData, Respuesta.class);
		logger.info("Resultado de la validacion: " + respuesta);
		SCbitacora objeto = null;
		try {
			if(respuesta.esIne()) {
				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se valida y guarda en tabla SCRESVALIDACION.");
				this.documentoUpdate.guardarBitacora(objeto, pool);			
				documentoUpdate.actualizaSCresvalidacion(respuesta, pool);
				
				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se valida y guarda en tabla SCRESULTVALFILE.");
				this.documentoUpdate.guardarBitacora(objeto, pool);	
				documentoUpdate.actualizaSCresultvalfile(respuesta, pool);
				
				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se Termina el flujo del documento.");
				this.documentoUpdate.guardarBitacora(objeto, pool);	
			}else {
				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se valida y guarda en tabla SCRESVALIDACION.");
				this.documentoUpdate.guardarBitacora(objeto, pool);			
				documentoUpdate.actualizaSCresvalidacionIsNotINE(respuesta, pool);

				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se valida y guarda en tabla SCRESULTVALFILE.");
				this.documentoUpdate.guardarBitacora(objeto, pool);	
				documentoUpdate.actualizaSCresultvalfile(respuesta, pool);
				
				objeto = new SCbitacora(0, respuesta.codigoSistema(), respuesta.codigoCliente(), respuesta.scarchivodigital_id(), "Se Termina el flujo del documento.");
				this.documentoUpdate.guardarBitacora(objeto, pool);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			logger.info("respuesta.t_persona()::: " + respuesta.t_persona() );
			String t_persona = normalizarTipoPersona(respuesta.t_persona());
			ValidacionDocumentos documentoValido = new ValidacionDocumentos(respuesta.clave(), t_persona);
			comunicarPendientes(documentoValido);
		}
	}

	private String normalizarTipoPersona(String tipoPersona) {
		if (tipoPersona == null || tipoPersona.isBlank()) {
			return "M";
		}
		String tipoPersonaNormalizado = tipoPersona.trim().toUpperCase();
		if ("F".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("FIS")) {
			return "F";
		}
		if ("M".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("MOR")) {
			return "M";
		}
		logger.info("No se reconocio el tipo de persona recibido del core: " + tipoPersona + ". Se usara M por compatibilidad.");
		return "M";
	}

	private void comunicarPendientes(ValidacionDocumentos documentoValido) {
		try {
			String jsonOCR = objetMapper.writeValueAsString(documentoValido);
			java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder()
					.uri(new URI(URL_NOTIFICA_DOCUMENTO + "/FunctionComunicaPendientes")).headers("Content-Type", "text/plain;charset=UTF-8")
					.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
					.POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonOCR)).build();
			logger.info("Enviando resultado validacion::::::::: ..." + jsonOCR);
			java.net.http.HttpResponse<String> httpResponseOCR = client.send(httpRequestOCR, BodyHandlers.ofString());
			logger.info("Resultado>>>: " + httpResponseOCR);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.info("Ocurrio un error en JsonProcessingException::: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Ocurrio un error en IOException::: " + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.info("Ocurrio un error en InterruptedException::: " + e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.info("Ocurrio un error en URISyntaxException::: " + e.getMessage());
		}
	}
}
