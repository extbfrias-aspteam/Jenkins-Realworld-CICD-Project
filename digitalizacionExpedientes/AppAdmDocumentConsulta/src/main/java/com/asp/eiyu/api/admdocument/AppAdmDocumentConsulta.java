package com.asp.eiyu.api.admdocument;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.Duration;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asp.eiyu.api.admdocument.configuration.cloudsql.CloudSqlConnectionPool;
import com.asp.eiyu.api.admdocument.dto.ConsultaEstatusCuentaDocs;
import com.asp.eiyu.api.admdocument.dto.SolicitudDocumento;
import com.asp.eiyu.api.admdocument.service.DocumentosInfoServicesImpl;
import com.asp.eiyu.api.admdocument.service.IDocumentosInfoServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppAdmDocumentConsulta implements HttpFunction {
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger log = LogManager.getLogger(AppAdmDocumentConsulta.class);
	private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build();
	private static final IDocumentosInfoServices iDocumentosInfoServices = new DocumentosInfoServicesImpl();
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");
	
	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException, SQLException {
		ConsultaEstatusCuentaDocs body = gson.fromJson(request.getReader(), ConsultaEstatusCuentaDocs.class);

		log.info("parametros recibidos son: " + request.getReader());
		try {
			DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
			
			SolicitudDocumento docResponse = iDocumentosInfoServices.obtenerListaDocumentos(body.clabe(), pool);
			
			ObjectMapper objMapper = new ObjectMapper();
			String documentoJson = "";
			try {
				documentoJson = objMapper.writeValueAsString(docResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			log.info("respuesta enviada: " + documentoJson);
			final BufferedWriter writer = response.getWriter();
			response.appendHeader("Content-Type", "application/json; charset=utf-8");
			writer.write(documentoJson);
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
