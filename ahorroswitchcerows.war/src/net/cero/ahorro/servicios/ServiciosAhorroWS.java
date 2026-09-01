package net.cero.ahorro.servicios;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.cero.ahorro.data.ValidaOperacionCeroReq;
import okhttp3.*;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.cero.ws.data.Constantes;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.cero.spring.config.Respuesta;

public class ServiciosAhorroWS implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ServiciosAhorroWS.class);

	//@RequestMapping(value="/leerCuentaAhorro", method=RequestMethod.POST)
	public static RespuestaSVC consultarCuentaAhorro(String cuentaAhorro) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerCuentaAhorro").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("cuentaAhorro", cuentaAhorro);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: ConsultarCuentaAhorro::%s\n%s", cuentaAhorro, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ConsultarCuentaAhorro");
		}
		return respuestaSvc;
	}


	//@RequestMapping(value="/leerReferenciaAhorro", method=RequestMethod.POST)
	public static RespuestaSVC consultarReferenciaAhorro(String referencia) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerReferenciaAhorro").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("referencia", referencia);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: ConsultarReferenciaAhorro::%s\n%s", referencia, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ConsultarReferenciaAhorro");
		}
		return respuestaSvc;
	}

	//@RequestMapping(value="/leerClabeAhorro", method=RequestMethod.POST)
		public static RespuestaSVC consultarClabeAhorro(String clabe) {
			RespuestaSVC respuestaSvc = new RespuestaSVC();
			Gson gson = ToolsR.GBuilder();
			String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerClabeAhorro").toString();
			log.info(uri);
			WebResource webResource;
			Client client;
			final HTTPBasicAuthFilter authFilter;
			String jsonResponse;
			Respuesta resp = new Respuesta();

			try{
				authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
				client = Client.create();
				client.addFilter(authFilter);
				webResource = client.resource(uri);

				Map<String, Object> map = new HashMap<>();
				map.put("clabe", clabe);

				ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
				jsonResponse = response.getEntity(String.class);
				resp = gson.fromJson(jsonResponse, Respuesta.class);
				if(resp.getCodigo() == 0){
					respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
				}
			}catch(Exception ex){
				log.error(ex.getMessage(),ex);
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ConsultarClabeAhorro");
			}
			return respuestaSvc;
		}
	
	//@RequestMapping(value="/leerPlasticoAhorro", method=RequestMethod.POST)
	public static RespuestaSVC consultarPlasticoAhorro(String tarjeta) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerPlasticoAhorro").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("tarjeta", tarjeta);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: ConsultarPlasticoAhorro::%s\n%s", tarjeta, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ConsultarPlasticoAhorro");
		}
		return respuestaSvc;
	}	


	//@RequestMapping(value="/leerPlasticoCuenta", method=RequestMethod.POST)
	public static RespuestaSVC consultarPlasticoCuenta(String cuentaID) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerPlasticoCuenta").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("cuentaID", cuentaID);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: ConsultarPlasticoCuenta::%s\n%s", cuentaID, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ConsultarPlasticoCuenta");
		}
		return respuestaSvc;
	}		

	//@RequestMapping(value="/leerSolicitante", method=RequestMethod.POST)
	public static RespuestaSVC consultarSolicitante(String personaID) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerSolicitante").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("personaID", personaID);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: consultarSolicitante::%s\n%s", personaID, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "consultarSolicitante");
		}
		return respuestaSvc;
	}		


	//@RequestMapping(value="/leerByCuentaClaveConceptoClaveCuentaAhorroDatos", method=RequestMethod.POST)
	public static RespuestaSVC consultarCuentaConcepto(String cuenta, String concepto) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerByCuentaClaveConceptoClaveCuentaAhorroDatos").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			Map<String, Object> map = new HashMap<>();
			map.put("cuenta", cuenta);
			map.put("concepto", concepto);
			map.put("estatus", "ALTA");

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}
		}catch(Exception ex){
			log.error(String.format("Error: consultarCuentaConcepto::%s\n%s", cuenta+"/"+concepto, ex.getMessage()));
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "consultarCuentaConcepto");
		}
		return respuestaSvc;
	}		


	public static RespuestaSVC buscarCuenta(String cuenta){
		RespuestaSVC respuesta = new RespuestaSVC();

		respuesta = buscarCuentaAhorro(cuenta);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}

		respuesta = buscarReferenciaAhorro(cuenta);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}	
		
		respuesta = buscarClabeAhorro(cuenta);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}	

		RespuestaSVC respTarjeta = buscarPlasticoAhorro(cuenta);
		if(respTarjeta.getErrores().getCodigoError() == 0){
			String cuentaTarjeta = ToolsR._T(respTarjeta.getBody().getValor("CUENTA_ID"));

			respuesta = buscarCuentaAhorro(cuentaTarjeta);
			if(respuesta.getErrores().getCodigoError() == 0){
				return respuesta;
			}
		}	 
		respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("La cuenta %s No existe en el sistema de ahorro", cuenta));
		return respuesta;
	}
	
	public static RespuestaSVC buscarCuentaSpei(String cuentaClabe){
		RespuestaSVC respuesta = new RespuestaSVC();

		respuesta = buscarClabeAhorro(cuentaClabe);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}	

		respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("La cuenta %s No existe en el sistema de ahorro", cuentaClabe));
		return respuesta;
	}

	public static RespuestaSVC buscarCuentaBase(String cuenta){
		RespuestaSVC respuesta = new RespuestaSVC();

		respuesta = buscarCuentaAhorro(cuenta);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}

		respuesta = buscarReferenciaAhorro(cuenta);
		if(respuesta.getErrores().getCodigoError() == 0){
			return respuesta;
		}	

		RespuestaSVC respTarjeta = buscarPlasticoAhorro(cuenta);
		if(respTarjeta.getErrores().getCodigoError() == 0){
			String cuentaTarjeta = ToolsR._T(respTarjeta.getBody().getValor("CUENTA_ID"));

			respuesta = buscarCuentaAhorro(cuentaTarjeta);
			if(respuesta.getErrores().getCodigoError() == 0){
				return respuesta;
			}
		}	 
		respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("La cuenta %s No existe en el sistema de ahorro", cuenta));
		return respuesta;
	}

	public static RespuestaSVC buscarSolicitante(String personaID){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarSolicitante(personaID);
		}catch(Exception ex){
			log.error(String.format("Error: buscarSolicitante::%s\n%s", personaID, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarSolicitante : " + personaID);
		}
		return respuesta;
	}

	public static RespuestaSVC buscarCuentaAhorro(String cuentaAhorro){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarCuentaAhorro(cuentaAhorro);
		}catch(Exception ex){
			log.error(String.format("Error: buscarCuentaAhorro::%s\n%s", cuentaAhorro, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarCuentaAhorro : " + cuentaAhorro);
		}
		return respuesta;
	}

	public static RespuestaSVC buscarReferenciaAhorro(String referencia){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarReferenciaAhorro(referencia);
		}catch(Exception ex){
			log.error(String.format("Error: buscarReferenciaAhorro::%s\n%s", referencia, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarReferenciaAhorro : " + referencia);
		}
		return respuesta;
	}

	public static RespuestaSVC buscarClabeAhorro(String clabe){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarClabeAhorro(clabe);
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarClabeAhorro : " + clabe);
		}
		return respuesta;
	}

	public static RespuestaSVC buscarPlasticoAhorro(String tarjeta){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarPlasticoAhorro(tarjeta);
		}catch(Exception ex){
			log.error(String.format("Error: buscarPlasticoAhorro::%s\n%s", tarjeta, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarPlasticoAhorro : " + tarjeta);
		}
		return respuesta;
	}


	public static RespuestaSVC buscarPlasticoCuenta(String cuentaID){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarPlasticoCuenta(cuentaID);
		}catch(Exception ex){
			log.error(String.format("ALERTA: buscarPlasticoCuenta::%s\n%s", cuentaID, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarPlasticoCuenta : " + cuentaID);
		}
		return respuesta;
	}

	public static RespuestaSVC buscarCuentaConcepto(String cuenta, String concepto){
		RespuestaSVC respuesta = new RespuestaSVC();
		try{
			respuesta = ServiciosAhorroWS.consultarCuentaConcepto(cuenta, concepto);
		}catch(Exception ex){
			log.error(String.format("ALERTA: buscarCuentaConcepto::%s\n%s", cuenta+"/"+concepto, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "buscarCuentaConcepto : " + cuenta+"/"+concepto);
		}
		return respuesta;
	}

	/* *********************************************************
	 * PROCESOS DE GENERACION DE RENDIMIENTOS
	 * 
	 * ********************************************************/

	//@RequestMapping(value="/leerCtasConcepto", method=RequestMethod.POST)
	public static RespuestaSVC listaCuentaConceptoSW(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerCtasConcepto").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "listaCuentaConceptoSW::leerCtasConcepto");
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "listaCuentaConceptoSW");
		}
		return respuestaSvc;
	}


	//@RequestMapping(value="/leerByCuentaByFechaDRTra", method=RequestMethod.POST)
	public static RespuestaSVC listaCuentaTransaccionesDRSW(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_SIMPLIFICADA_WS).append("/").append("leerByCuentaByFechaDRTra").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "listaCuentaTransaccionesDRSW::leerByCuentaByFechaDRTra");
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "listaCuentaTransaccionesDRSW");
		}
		return respuestaSvc;
	}
	//@RequestMapping(value="/cancelarCuenta", method=RequestMethod.POST)
	public static RespuestaSVC autCancelarCuenta(Map<String, Object> map) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Gson gson = ToolsR.GBuilder();
		String uri = new StrBuilder(Constantes.AHORRO_WS).append("/").append("autCancelarCuenta").toString();
		log.info(uri);
		WebResource webResource;
		Client client;
		final HTTPBasicAuthFilter authFilter;
		String jsonResponse;
		Respuesta resp = new Respuesta();

		try{
			authFilter = new HTTPBasicAuthFilter(Constantes.PALABRAUSU, Constantes.PALABRAHID);
			client = Client.create();
			client.addFilter(authFilter);
			webResource = client.resource(uri);

			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, gson.toJson(map));
			jsonResponse = response.getEntity(String.class);
			resp = gson.fromJson(jsonResponse, Respuesta.class);
			if(resp.getCodigo() == 0){
				respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "cancelarCuenta::cancelarCuenta");
			}
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "cancelarCuenta");
		}
		return respuestaSvc;
	}

	public static Respuesta validarMontoOperacion(ValidaOperacionCeroReq req)
	{
		Gson gson = new Gson();
		MediaType media = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient cliente = new OkHttpClient();
		String auth = Credentials.basic("ASP", "a5p2017$");
		String url = Constantes.AHORRO_WS + "/validarOperacionCuenta";
		String body = gson.toJson(req);
		log.info("URL del WS: {}",url);
		log.info("Request body para servicio : {}",body);
		try{
			Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
			Response response = cliente.newCall(request).execute();
			String obj = response.body().string();
			log.info("Respuesta validarMontoOperacion: {}",obj);
			Respuesta respOrquesta = gson.fromJson(obj,Respuesta.class);
			response.body().close();
			return respOrquesta;
		}
		catch(IOException e)
		{
			log.error("Ocurrió un detalle al consumir el metodo de validarMontoOperacion del orquestador",e);
			Respuesta respOrquesta = new Respuesta();
			respOrquesta.setCodigo(-1);
			respOrquesta.setData(null);
			respOrquesta.setMensaje("Ocurrio un error interno al tratar de validar la operacion.");
			return respOrquesta;
		}
	}



}
