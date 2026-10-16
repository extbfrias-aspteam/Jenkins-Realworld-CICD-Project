package com.asp.eiyu.api.auth;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.sql.DataSource;

import com.asp.eiyu.api.auth.configuration.cloudsql.CloudSqlConnectionPool;
import com.asp.eiyu.api.auth.entity.ResponseDTO;
import com.asp.eiyu.api.auth.service.UsuarioService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FunctionAuthToken implements HttpFunction {

	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String SECRET_key= System.getenv("SECRET_KEY");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db
	
	private UsuarioService isu = new UsuarioService();
	
	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		ResponseDTO<String> respuesta = new ResponseDTO<>();
		System.out.println("parametros recibidos son: " + request.getReader());
		DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
		Map<String, Object> body = gson.fromJson(request.getReader(), Map.class);
		String user = (body != null && body.get("username") != null) ? (String) body.get("username") : "";
		String pass = (body != null && body.get("password") != null) ? (String) body.get("password") : "";
		respuesta = isu.auth(pool,user,pass,SECRET_key);
		String jsonResponse = gson.toJson(respuesta);
		final BufferedWriter writer = response.getWriter();
		writer.write(jsonResponse);
	}

	
}
