package com.asp.eiyu.api.admdocument.gtw;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asp.eiyu.api.admdocument.gtw.cloudsql.CloudSqlConnectionPool;
import com.asp.eiyu.api.admdocument.gtw.dto.DocumentoRegistroJSONDto;
import com.asp.eiyu.api.admdocument.gtw.dto.DocumentoRespDTO;
import com.asp.eiyu.api.admdocument.gtw.dto.ResponseDTO;
import com.asp.eiyu.api.admdocument.gtw.dto.ResponseTokenDTO;
import com.asp.eiyu.api.admdocument.gtw.dto.SolicitudDocumento;
import com.asp.eiyu.api.admdocument.gtw.entity.SCTipoDocumento;
import com.asp.eiyu.api.admdocument.gtw.repository.TipoDocumentoRepository;
import com.asp.eiyu.api.admdocument.gtw.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
//import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocumentoGTWHttpFunction implements HttpFunction {
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger log = LogManager.getLogger(DocumentoGTWHttpFunction.class);
	private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build();
	private String CARGA_DOCUMENTOENDPOINT = System.getenv("CARGA_DOCUMENTOENDPOINT");// "CARGA_DOCUMENTOENDPOINT=https://us-central1-prj-digitalizacion-expedientes.cloudfunctions.net/AppAdmDocumentGcpApplication";
	private String DESCARGA_DOCUMENTOENDPOINT = System.getenv("DESCARGA_DOCUMENTOENDPOINT");// "DESCARGA_DOCUMENTOENDPOINT=https://us-central1-prj-digitalizacion-expedientes.cloudfunctions.net/AppAdmObtieneDocumentGCP";
	private String CONSULTA_DOCUMENTOENDPOINT = System.getenv("CONSULTA_DOCUMENTOENDPOINT");// "CONSULTA_DOCUMENTOENDPOINT=https://us-central1-prj-digitalizacion-expedientes.cloudfunctions.net/AppAdmDocumentConsulta";
	private String ENDPOINT_VALIDATION = System.getenv("ENDPOINT_VALIDATION");
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db
	private static final long TAMANO_MAXIMO_BYTES = 10 * 1024 * 1024; // 5MB
    private static final int LONGITUD_MAX_NOMBRE_ARCHIVO = 120;
	private static final String SECRET_key= System.getenv("SECRET_KEY");
	private static final String VALIDAR_PROPIETARIO_ENDPOINT = System.getenv("VALIDAR_PROPIETARIO_ENDPOINT");
	private static final String ACTIVACION_CUENTAS_JWT_ISSUER = System.getenv("ACTIVACION_CUENTAS_JWT_ISSUER");
	private static final String ACTIVACION_CUENTAS_JWT_AUDIENCE = System.getenv("ACTIVACION_CUENTAS_JWT_AUDIENCE");
	private static final String ACTIVACION_CUENTAS_JWT_SECRET_KEY = System.getenv("ACTIVACION_CUENTAS_JWT_SECRET_KEY");
	private static final long ACTIVACION_CUENTAS_JWT_EXPIRATION_MS = 5 * 60 * 1000L;
	
	
	private ObjectMapper objetMapper = new ObjectMapper();
	TipoDocumentoRepository documentoRegistroRepository = new TipoDocumentoRepository();
	UsuarioRepository usuaRepo = new UsuarioRepository();
	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException {
		Map<String, List<String>> headers = request.getHeaders();
		final BufferedWriter writer = response.getWriter();
		DocumentoRespDTO respuestaDto = new DocumentoRespDTO("ERROR_DB", "Error al cargar documento error en DB", null);
		String respuesta = "";
		try {
			DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
			try {
				Optional<String> authHeader = obtenerHeaderAutorizacion(request);
				if(!authHeader.isPresent()) {
					writer.write("Token no encontrado...");
					return;
				}
				String token = extraerBearerToken(authHeader.get());
				if(token == null) {
					respuestaDto = new DocumentoRespDTO("ERROR_TOKEN", "Error al cargar documento, formato de token no valido", null);
					respuesta = objetMapper.writeValueAsString(respuestaDto);
					writer.write(respuesta);
					return;
				}
				
				ResponseDTO rd = this.validarTokenActual(SECRET_key, token);
				 
				if(!rd.getEiyuResponseCode().equals("0")) {
					respuesta=objetMapper.writeValueAsString(rd);
					writer.write(respuesta);
					return;
				}
				
				if (rd.getEiyuResponseCode().equals("0")) {
					
					try {
						SolicitudDocumento body = gson.fromJson(request.getReader(), SolicitudDocumento.class);
						Integer idPblu = extraerIdPbluDesdeRespuestaToken(rd);
						if (idPblu == null || idPblu <= 0) {
							respuestaDto = new DocumentoRespDTO("ERROR_TOKEN", "Error al cargar documento, el token no contiene un idPblu valido", null);
							respuesta = objetMapper.writeValueAsString(respuestaDto);
							writer.write(respuesta);
							return;
						}
						String cuentaOperacion = obtenerCuentaOperacion(body);
						if (cuentaOperacion == null || cuentaOperacion.isBlank()) {
							respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, no se encontro la clabe/codigo a validar", null);
							respuesta = objetMapper.writeValueAsString(respuestaDto);
							writer.write(respuesta);
							return;
						}
						ValidacionPropietarioResultado validacionPropietario = validarPropietarioCuenta(cuentaOperacion, idPblu);
						if (!validacionPropietario.valida()) {
							respuestaDto = new DocumentoRespDTO("ERROR_PROPIETARIO", validacionPropietario.mensaje(), null);
							respuesta = objetMapper.writeValueAsString(respuestaDto);
							writer.write(respuesta);
							return;
						}
						List<SCTipoDocumento> tiposDocumentos = documentoRegistroRepository.consultarTipoDoscumentos((body != null && body.documentoRegistro()!=null)?body.documentoRegistro().tipoDocumento():"", pool);
						log.info("body:::" +body);
						if (body.tipo().equalsIgnoreCase("001")) {
							if(validarTamanioArchivoBase64(body.documentoRegistro().archivo())) {
								if(!validacionTipoDocumento(body.documentoRegistro(), tiposDocumentos)) {
									respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, Documento no valido " + errorValidacion02(body.documentoRegistro(), tiposDocumentos), null);
									respuesta = objetMapper.writeValueAsString(respuestaDto);
									writer.write(respuesta);
									return;
								}
								String errorNombreArchivo = validarNombreArchivoLibre(body.documentoRegistro());
								if (errorNombreArchivo != null) {
								    respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, " + errorNombreArchivo, body.documentoRegistro().nombreArchivo());
								    respuesta = objetMapper.writeValueAsString(respuestaDto);
								    writer.write(respuesta);
								    return;
								}
								respuesta = this.ejecutaServicio(gson.toJson(body.documentoRegistro()),
										CARGA_DOCUMENTOENDPOINT);
							}else {
								respuestaDto = new DocumentoRespDTO("ERROR_ARCHIVO", "Error al cargar documento, El archivo excede el tamaño máximo permitido de " + TAMANO_MAXIMO_BYTES + " bytes.", null);
								respuesta = objetMapper.writeValueAsString(respuestaDto);
								writer.write(respuesta);
								return;
							}
						} else if (body.tipo().equalsIgnoreCase("002")) {
							String nombreArchivo = body._documentoRegistro().getTipoDocumento();
							if (!nombreArchivo.equals(nombreArchivo.toUpperCase())) {
							    respuestaDto = new DocumentoRespDTO("ERROR", "Error al descargar documento, Nombre de archivo no válido (debe estar en MAYÚSCULAS)", nombreArchivo);
							    respuesta = objetMapper.writeValueAsString(respuestaDto);
							    writer.write(respuesta);
							    return;
							}
							respuesta = this.ejecutaServicio(gson.toJson(body._documentoRegistro()),
									DESCARGA_DOCUMENTOENDPOINT);
						} else if (body.tipo().equalsIgnoreCase("003")) {
							respuesta = this.ejecutaServicio(gson.toJson(body.documentoConsulta()),
									CONSULTA_DOCUMENTOENDPOINT);
						}
						log.info("respuesta:::: " + respuesta);
						writer.write(respuesta);
							documentoRegistroRepository.terminarConexionesIdleJDBCDefault(pool);
						return;
					} catch (JsonSyntaxException e) {
						respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, SintaxError JSON", null);
						respuesta = objetMapper.writeValueAsString(respuestaDto);
						writer.write(respuesta);
						e.printStackTrace();
						return;
					} catch (JsonIOException e) {
						respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, Error JSON", null);
						respuesta = objetMapper.writeValueAsString(respuestaDto);
						writer.write(respuesta);
						e.printStackTrace();
						return;
					} catch (IOException e) {
						respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento,  " + e.getMessage(), null);
						respuesta = objetMapper.writeValueAsString(respuestaDto);
						writer.write(respuesta);
						e.printStackTrace();
						return;
					} catch (SQLException e) {
						respuestaDto = new DocumentoRespDTO("ERROR_DB", "Error al cargar documento,  " + e.getMessage(), null);
						respuesta = objetMapper.writeValueAsString(respuestaDto);
						writer.write(respuesta);
						e.printStackTrace();
						return;
					}
				}
				respuestaDto = new DocumentoRespDTO("ERROR_TOKEN", "Error al cargar documento token no valido", null);
				respuesta = objetMapper.writeValueAsString(respuestaDto);
				writer.write(respuesta);
				return;
			} catch (NoSuchElementException ne) {
				log.info(ne+"");
				respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, token no encontrado", null);
				respuesta = objetMapper.writeValueAsString(respuestaDto);
				writer.write(respuesta);
				ne.printStackTrace();
				return;
//			} catch (URISyntaxException e) {
//				respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, SintaxError URI", null);
//				respuesta = objetMapper.writeValueAsString(respuestaDto);
//				writer.write(respuesta);
//				e.printStackTrace();
//				return;
			} catch (IOException e) {
				respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, " + e.getMessage(), null);
				respuesta = objetMapper.writeValueAsString(respuestaDto);
				writer.write(respuesta);
				e.printStackTrace();
				return;
//			} catch (InterruptedException e) {
//				respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento, " + e.getMessage(), null);
//				respuesta = objetMapper.writeValueAsString(respuestaDto);
//				writer.write(respuesta);
//				e.printStackTrace();
//				return;
			}
		} catch (GeneralSecurityException e) {
			respuesta = objetMapper.writeValueAsString(respuestaDto);
			writer.write(respuesta);
			e.printStackTrace();
			return;
		}
	}

	private Optional<String> obtenerHeaderAutorizacion(HttpRequest request) {
		Optional<String> forwardedAuthorization = request.getFirstHeader("x-forwarded-authorization");
		if (forwardedAuthorization.isPresent()) {
			log.info("Se utilizara el header x-forwarded-authorization para validar el token.");
			return forwardedAuthorization;
		}
		Optional<String> authorization = request.getFirstHeader("Authorization");
		if (authorization.isPresent()) {
			log.info("Se utilizara el header Authorization para validar el token.");
			return authorization;
		}
		return Optional.empty();
	}

	private String extraerBearerToken(String authHeader) {
		if (authHeader == null) {
			return null;
		}
		String valor = authHeader.trim();
		if (valor.isEmpty()) {
			return null;
		}
		if (valor.regionMatches(true, 0, "Bearer ", 0, 7)) {
			String token = valor.substring(7).trim();
			return token.isEmpty() ? null : token;
		}
		return null;
	}

	private boolean validacionTipoDocumento(DocumentoRegistroJSONDto documentoRegistro, List<SCTipoDocumento> tiposDocumentos) {
		boolean validadoOk = false;
		log.info("que extension es: " + documentoRegistro.nombreArchivo());
		if(documentoRegistro.nombreArchivo().toUpperCase().contains(documentoRegistro.extensionArchivo().toUpperCase())) {
			for (SCTipoDocumento tipoDocumento : tiposDocumentos) {
				if(documentoRegistro.nombreArchivo().substring(documentoRegistro.nombreArchivo().lastIndexOf(".")).toUpperCase().contains(tipoDocumento.nombreArchivo().substring(tipoDocumento.nombreArchivo().indexOf(".")).toUpperCase())) {
					validadoOk = true;
					break;
				}else {
					validadoOk = false;
				}
			}
		}
		return validadoOk;
	}
	private String errorValidacion02(DocumentoRegistroJSONDto documentoRegistro, List<SCTipoDocumento> tiposDocumentos) {
		StringBuilder validacion = new StringBuilder();

		String extensionNombreArchivo = documentoRegistro.nombreArchivo().substring(documentoRegistro.nombreArchivo().lastIndexOf(".")).toUpperCase();
		String extensionReal = documentoRegistro.extensionArchivo().toUpperCase();

		if (!extensionNombreArchivo.equals(extensionReal)) {
			// Caso: extensión del nombre no coincide con la extensión reportada
			validacion.append("La extensión del nombre del archivo (")
			          .append(extensionNombreArchivo)
			          .append(") y la extensión del archivo ingresado (")
			          .append(extensionReal)
			          .append(") no corresponden.");
		} else {
			// Caso: extensión coincide, pero no es una de las permitidas
			validacion.append("Los tipos de archivos aceptados (");

			for (int i = 0; i < tiposDocumentos.size(); i++) {
				SCTipoDocumento tipoDocumento = tiposDocumentos.get(i);
				String ext = tipoDocumento.nombreArchivo().substring(tipoDocumento.nombreArchivo().lastIndexOf("."));
				validacion.append(ext.toUpperCase());
				if (i < tiposDocumentos.size() - 1) {
					validacion.append(", ");
				}
			}

			validacion.append("), pero la extensión del archivo ingresado (")
			          .append(extensionReal)
			          .append(") no corresponde.");
		}

		return validacion.toString();
	}

	
	public boolean validarNombreArchivo(DocumentoRegistroJSONDto documentoRegistro) {
		boolean validadoOk = true;
		String nombre = documentoRegistro.nombreArchivo()!=null? documentoRegistro.nombreArchivo().toUpperCase().replace(documentoRegistro.extensionArchivo().toUpperCase(), ""):"";
		if(nombre.trim().isEmpty()) {
			validadoOk = false; 
		}
		return validadoOk;
	}
	private String validarNombreArchivoLibre(DocumentoRegistroJSONDto documentoRegistro) {
		String nombreArchivo = documentoRegistro.nombreArchivo() != null ? documentoRegistro.nombreArchivo().trim() : "";
		String extensionArchivo = documentoRegistro.extensionArchivo() != null ? documentoRegistro.extensionArchivo().trim() : "";

		if (nombreArchivo.isEmpty()) {
			return "el nombre del archivo es obligatorio.";
		}

		if (nombreArchivo.length() > LONGITUD_MAX_NOMBRE_ARCHIVO) {
			return "el nombre del archivo excede la longitud máxima permitida de " + LONGITUD_MAX_NOMBRE_ARCHIVO + " caracteres.";
		}

		if (nombreArchivo.contains("/") || nombreArchivo.contains("\\")) {
			return "el nombre del archivo no puede contener diagonales ni rutas.";
		}

		if (!nombreArchivo.matches("^[A-Za-z0-9 _.-]+$")) {
			return "el nombre del archivo contiene caracteres no permitidos. Solo se permiten letras, números, espacios, guion, guion bajo y punto.";
		}

		if (!extensionArchivo.isEmpty() && !nombreArchivo.toUpperCase().endsWith(extensionArchivo.toUpperCase())) {
			return "la extensión del nombre del archivo no coincide con la extensión reportada.";
		}

		String nombreSinExtension = nombreArchivo;
		int lastDotIndex = nombreArchivo.lastIndexOf('.');
		if (lastDotIndex > 0) {
			nombreSinExtension = nombreArchivo.substring(0, lastDotIndex).trim();
		}

		if (nombreSinExtension.isEmpty()) {
			return "el nombre del archivo no puede contener solo la extensión.";
		}

		return null;
	}

	
    public boolean validarTamanioArchivoBase64(String archivoBase64) {
        try {
            byte[] archivoBytes = Base64.getDecoder().decode(archivoBase64);
            // Validar el tamaño del archivo
            long tamanoArchivo = archivoBytes.length;
            
            if (tamanoArchivo > TAMANO_MAXIMO_BYTES) {
                System.out.println("El archivo excede el tamaño máximo permitido de " + TAMANO_MAXIMO_BYTES + " bytes.");
                return false;
            }

            System.out.println("El archivo es válido y tiene un tamaño de " + tamanoArchivo + " bytes.");
            return true;
        } catch (IllegalArgumentException e) {
            // Excepción lanzada si la cadena no es válida en Base64
            System.out.println("La cadena Base64 no es válida.");
            return false;
        }
    }

	
	private String errorValidacion(DocumentoRegistroJSONDto documentoRegistro, List<SCTipoDocumento> tiposDocumentos) {
		String validacion = "";
		if(documentoRegistro.nombreArchivo().toUpperCase().contains(documentoRegistro.extensionArchivo().toUpperCase())) {
			validacion = validacion + "Los tipos de archivos aceptados (";
			for (SCTipoDocumento tipoDocumento : tiposDocumentos) {
				validacion = validacion + tipoDocumento.nombreArchivo().substring(tipoDocumento.nombreArchivo().lastIndexOf(".")) + ", ";
			}
			validacion = validacion + ") y la extension del archivo ingresado ("+ documentoRegistro.extensionArchivo()+") no corresponde.";

		}else {
			validacion = validacion + "la extension del nombre del archivo ("+ documentoRegistro.nombreArchivo().substring(documentoRegistro.nombreArchivo().lastIndexOf("."))+") y la extension del archivo ingresado ("+ documentoRegistro.extensionArchivo()+") no corresponde.";
		}
		return validacion;
	}
	
	private String ejecutaServicio(String jsonReq, String ENDPOINT) {
		String respuesta = "";
		log.info("host :: " + ENDPOINT);
		log.info("body :: " + jsonReq);

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(ENDPOINT).post(RequestBody.create(media, jsonReq)).build();
		try {
			Response response = client.newCall(request).execute();
			respuesta = response.body().string();
			log.info("SYNC CALL : " + respuesta);
			return respuesta;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return respuesta;
	}

	public boolean validarToken(String token) throws URISyntaxException, IOException, InterruptedException {
		java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder()
				.uri(new URI(ENDPOINT_VALIDATION))
				.headers("Content-Type", "text/plain;charset=UTF-8", "Authorization", token)
				.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
				.POST(java.net.http.HttpRequest.BodyPublishers.ofString("Validando....")).build();

		java.net.http.HttpResponse<String> httpResponseOCR = client.send(httpRequestOCR, BodyHandlers.ofString());

		ResponseTokenDTO bodyResponseOCR = gson.fromJson(httpResponseOCR.body(), ResponseTokenDTO.class);
		log.info("la respuesta de la validacion es:: " + httpResponseOCR.body());
		return bodyResponseOCR.valido();
	}

//	public boolean ConsultaToken(String user,String pass) throws URISyntaxException {
//		log.info("Login token URL: " + login_token);
//		// Crear el JSON body con username y password
//		String username = user;
//		String password = pass;
//		Map<String, String> requestBody = new HashMap<>();
//		requestBody.put("username", username);
//		requestBody.put("password", password);
//		// Convertir el cuerpo de la solicitud a JSON
//		String jsonBody = gson.toJson(requestBody);
//		log.info(">>>URL>>>>>>" + login_token);
//		log.info(">>>Body>>>>>>" + jsonBody);
//		try {
//			// Crear la solicitud POST
//			java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder().uri(new URI(login_token))
//					.headers("Content-Type", "application/json").version(HttpClient.Version.HTTP_2)
//					.timeout(Duration.ofSeconds(60)).POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
//					.build();
//
//			// Enviar la solicitud y obtener la respuesta
//			java.net.http.HttpResponse<String> httpResponse = client.send(httpRequestOCR, BodyHandlers.ofString());
//
//			// Verificar si la respuesta es exitosa
//			if (httpResponse.statusCode() == 200) {
//				Optional<String> token = httpResponse.headers().firstValue("Authorization");
//
//				if (token.isPresent()) {
//					log.info("Token recibido en header: " + token.get());
//					return true; // Retornar el token desde el header
//				} else {
//					log.warn("Token no encontrado en el header de la respuesta");
//					return false;
//				}
//			} else {
//				log.warn("Error al obtener el token. Código de estado: " + httpResponse.statusCode());
//				return false;
//			}
//
//		} catch (IOException | InterruptedException e) {
//			log.error("Error en la autenticación", e);
//			Thread.currentThread().interrupt();
//			return false;
//		}
//	}
	
	public ResponseDTO validarTokenActual(String secret, String token) {
		try {
			String secretString = secret;
			String encodedKey = Base64.getEncoder().encodeToString(secretString.getBytes());
			Key secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedKey));
			// Convertir la clave secreta en un Key válido

			// Validar y parsear el token
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);

			Claims claims = claimsJws.getBody();
			return new ResponseDTO<>("0", "Token válido", claims);
		} catch (SignatureException e) {
			return new ResponseDTO<>("1", "Firma del token inválida", null);
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			return new ResponseDTO<>("2", "Token expirado", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseDTO<>("3", "Token inválido", token);

		
		}
	}

	private Integer extraerIdPbluDesdeRespuestaToken(ResponseDTO respuestaToken) {
		if (respuestaToken == null || !(respuestaToken.getContent() instanceof Claims claims)) {
			return null;
		}
		Object idPblu = claims.get("idPblu");
		if (idPblu instanceof Integer integer) {
			return integer;
		}
		if (idPblu instanceof Long longValue) {
			return longValue.intValue();
		}
		if (idPblu instanceof Double doubleValue) {
			return doubleValue.intValue();
		}
		if (idPblu instanceof String stringValue && !stringValue.isBlank()) {
			try {
				return Integer.parseInt(stringValue);
			} catch (NumberFormatException ex) {
				log.warn("No fue posible convertir idPblu del token a entero: {}", stringValue);
			}
		}
		return null;
	}

	private String obtenerCuentaOperacion(SolicitudDocumento body) {
		if (body == null || body.tipo() == null) {
			return null;
		}
		if (body.tipo().equalsIgnoreCase("001") && body.documentoRegistro() != null) {
			return body.documentoRegistro().codigo();
		}
		if (body.tipo().equalsIgnoreCase("002") && body._documentoRegistro() != null) {
			return body._documentoRegistro().getCodigo();
		}
		if (body.tipo().equalsIgnoreCase("003") && body.documentoConsulta() != null) {
			return body.documentoConsulta().clabe();
		}
		return null;
	}

	private ValidacionPropietarioResultado validarPropietarioCuenta(String clabe, Integer idPblu) {
		String errorConfiguracion = validarConfiguracionValidarPropietario();
		if (errorConfiguracion != null) {
			return new ValidacionPropietarioResultado(false, errorConfiguracion);
		}

		MediaType media = MediaType.parse("application/json; charset=utf-8");
		String cuerpo = gson.toJson(new ValidarPropietarioRequest(clabe, idPblu));
		Request request = new Request.Builder().url(VALIDAR_PROPIETARIO_ENDPOINT)
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer " + generarJwtServicioValidarPropietario())
				.post(RequestBody.create(media, cuerpo)).build();

		try (Response response = new OkHttpClient().newCall(request).execute()) {
			String respuestaServicio = response.body() != null ? response.body().string() : "";
			log.info("Validacion propietario status :: {}", response.code());
			log.info("Validacion propietario body :: {}", respuestaServicio);

			ValidarPropietarioResponse payload = gson.fromJson(respuestaServicio, ValidarPropietarioResponse.class);
			if (payload == null) {
				return new ValidacionPropietarioResultado(false,
						"No fue posible validar la propiedad de la cuenta.");
			}

			boolean cuentaValida = payload.success()
					&& payload.data() != null
					&& Boolean.TRUE.equals(payload.data().cuentaValida());

			if (cuentaValida) {
				return new ValidacionPropietarioResultado(true, payload.message());
			}

			String mensaje = payload.message();
			if (mensaje == null || mensaje.isBlank()) {
				mensaje = "La cuenta no existe.";
			}
			if (mensaje.equalsIgnoreCase("La cuenta no pertenece al participante")) {
				mensaje = "La cuenta no existe.";
			}
			return new ValidacionPropietarioResultado(false, mensaje);
		} catch (IOException ex) {
			log.error("Error al validar propietario de la cuenta", ex);
			return new ValidacionPropietarioResultado(false,
					"No fue posible validar la propiedad de la cuenta.");
		}
	}

	private String validarConfiguracionValidarPropietario() {
		if (VALIDAR_PROPIETARIO_ENDPOINT == null || VALIDAR_PROPIETARIO_ENDPOINT.isBlank()) {
			return "No fue posible validar la propiedad de la cuenta: no se encontro la configuracion VALIDAR_PROPIETARIO_ENDPOINT.";
		}
		if (ACTIVACION_CUENTAS_JWT_ISSUER == null || ACTIVACION_CUENTAS_JWT_ISSUER.isBlank()) {
			return "No fue posible validar la propiedad de la cuenta: no se encontro la configuracion ACTIVACION_CUENTAS_JWT_ISSUER.";
		}
		if (ACTIVACION_CUENTAS_JWT_AUDIENCE == null || ACTIVACION_CUENTAS_JWT_AUDIENCE.isBlank()) {
			return "No fue posible validar la propiedad de la cuenta: no se encontro la configuracion ACTIVACION_CUENTAS_JWT_AUDIENCE.";
		}
		if (ACTIVACION_CUENTAS_JWT_SECRET_KEY == null || ACTIVACION_CUENTAS_JWT_SECRET_KEY.isBlank()) {
			return "No fue posible validar la propiedad de la cuenta: no se encontro la configuracion ACTIVACION_CUENTAS_JWT_SECRET_KEY.";
		}
		return null;
	}

	private String generarJwtServicioValidarPropietario() {
		Key secretKey = Keys.hmacShaKeyFor(ACTIVACION_CUENTAS_JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		Date ahora = new Date();
		Date expiracion = new Date(System.currentTimeMillis() + ACTIVACION_CUENTAS_JWT_EXPIRATION_MS);
		@SuppressWarnings("deprecation")
		String jwt = Jwts.builder()
				.setIssuer(ACTIVACION_CUENTAS_JWT_ISSUER)
				.setAudience(ACTIVACION_CUENTAS_JWT_AUDIENCE)
				.setIssuedAt(ahora)
				.setExpiration(expiracion)
				.signWith(secretKey)
				.compact();
		return jwt;
	}

	private record ValidarPropietarioRequest(String clabe, Integer idPblu) {
	}

	private record ValidarPropietarioResponse(boolean success, String message, ValidarPropietarioData data,
			List<ValidarPropietarioError> errors) {
	}

	private record ValidarPropietarioData(Boolean cuentaValida) {
	}

	private record ValidarPropietarioError(String code, String message, String field) {
	}

	private record ValidacionPropietarioResultado(boolean valida, String mensaje) {
	}
	
}
