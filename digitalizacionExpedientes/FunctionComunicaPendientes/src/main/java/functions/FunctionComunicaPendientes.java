package functions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;

import functions.cloudsql.CloudSqlConnectionPool;
import functions.dto.ValidacionDocumentos;
import functions.dto.ValidacionesDocumentosByNivel;
import functions.repository.DocumentoRegistroRepository;
import functions.services.SubscribeToTopic;

public class FunctionComunicaPendientes implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionComunicaPendientes.class);

	private static final Gson gson = new Gson();

	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String projectId = System.getenv("PROJECT_ID");
	private static final String topicId = System.getenv("TOPICSALIDAID");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db

	/**
	 * documentoRegistroRepository - variable de tipo DocumentoRegistroRepository
	 * para usarlo como
	 */
	DocumentoRegistroRepository documentoRegistroRepository = new DocumentoRegistroRepository();

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		ValidacionDocumentos body = gson.fromJson(request.getReader(), ValidacionDocumentos.class);
		DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
		log.info("parametros recibidos son: " + body);
		
		List<ValidacionesDocumentosByNivel> listaFaltantes = new ArrayList<>();
		boolean consultaExitosa = true;
		try {
			listaFaltantes = documentoRegistroRepository.consultarDoscumentosErrores(body.clave(), body.t_persona(), pool);
			listaFaltantes.addAll(documentoRegistroRepository.consultarDoscumentosPendientes(body.clave(), body.t_persona(), pool));
		} catch (SQLException e) {
			consultaExitosa = false;
			e.printStackTrace();
			log.info("Ocurrio un error al consultar los datos: " + e.getMessage());
		}
		
		final BufferedWriter writer = response.getWriter();
		response.appendHeader("Content-Type", "application/json; charset=utf-8");
		if (!consultaExitosa) {
			response.setStatusCode(500);
			writer.write(gson.toJson(new ErrorRespuesta("ERROR_CONSULTA_PENDIENTES",
					"No fue posible consultar los documentos pendientes o con error.")));
			return;
		}

		String respuesta = gson.toJson(listaFaltantes);
		String mensajeQueue = gson.toJson(new ValidacionDocumentos(body.clave(), body.t_persona(), listaFaltantes));
		
		log.info("Documentos Faltantes de carga y con errores: " + listaFaltantes);
		log.info("Documentos respuesta: " + respuesta);
		log.info("Mensaje enviado a queue de resultado validacion: " + mensajeQueue);
		publicaMensajeQueue(mensajeQueue);
		writer.write(respuesta);
	}

	private record ErrorRespuesta(String codigo, String mensaje) {}
	
	private void publicaMensajeQueue(String mensaje) {
		SubscribeToTopic enviar = new SubscribeToTopic();
		try {
			log.info("Enviando resultados a la QUEUE:: " + mensaje);
			enviar.publisherExample(projectId, topicId, mensaje);
		} catch (IOException e) {
			log.info("Ocurrio un error IOException:: ");
			e.printStackTrace();
		} catch (ExecutionException e) {
			log.info("Ocurrio un error ExecutionException:: ");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.info("Ocurrio un error InterruptedException:: ");
			e.printStackTrace();
		}
	}
}
