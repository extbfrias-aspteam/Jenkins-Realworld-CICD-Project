package com.asp.eiyu.api.admdocument;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.configuration.cloudsql.CloudSqlConnectionPool;
import com.asp.eiyu.api.admdocument.gtw.dto.DocumentoRegistroJSONDto;
import com.asp.eiyu.api.admdocument.service.DocumentoRegistroServiceImpl;
import com.asp.eiyu.api.admdocument.service.IDocumentoRegistroService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;

public class AppAdmDocumentGcpApplication implements HttpFunction {
	/**
	 * documentoRegistro - variable de tipo IDocumentoRegistroService para usarlo
	 * como
	 */
	private IDocumentoRegistroService documentoRegistroService = new DocumentoRegistroServiceImpl();

	private static final Gson gson = new Gson();

	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");

	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		System.out.println("parametros recibidos son: " + request.getReader());
		DataSource pool = CloudSqlConnectionPool.getDataSource(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);

		Storage storage = PoolStorageHolder.getInstance();
		// Deserialization into the `Employee` class
		System.out.println("request.getReader():: " + request.getReader());
		DocumentoRegistroJSONDto body = gson.fromJson(request.getReader(), DocumentoRegistroJSONDto.class);
		System.out.println("body:: " + body);
		String respuesta = this.documentoRegistroService.cargaDocumentoRegistro(body, pool, storage);
		
		final BufferedWriter writer = response.getWriter();
		writer.write(respuesta);
	}

	private static class PoolStorageHolder {

		private PoolStorageHolder() {
		}

		private static Storage STORAGE = setupPool();

		private static Storage setupPool() {
			String projectId = System.getenv("PROJECT_ID");
			STORAGE = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			return STORAGE;
		}

		private static Storage getInstance() {
			if (STORAGE != null) {
				STORAGE = setupPool();
			}
			return STORAGE;
		}
	}
}
