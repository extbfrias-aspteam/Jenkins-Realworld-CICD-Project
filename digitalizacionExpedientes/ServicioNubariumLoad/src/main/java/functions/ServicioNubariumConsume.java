package functions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;

import functions.cloudsql.CloudSqlConnectionPool;
import functions.dto.CustomerSummary;
import functions.dto.ObjetoOCR;
import functions.dto.PubSubStorageSendDto;
import functions.dto.RFC;
import functions.dto.ResponseCodeRFC;
import functions.dto.ResponseRFC;
import functions.dto.Respuesta;
import functions.dto.SavingAccount;
import functions.dto.ValidaCurpResponseOBJ;
import functions.dto.ValidaINEResponseOBJ;
import functions.dto.ValidaRfcResponseOBJ;
import functions.dto.ValidacionIneDto;
import functions.entity.ActualizaEstatusFlujo;
import functions.entity.DocumentoRegistroEntity;
import functions.entity.SCbitacora;
import functions.entity.SCcorePersona;
import functions.entity.SCrefeiyu;
import functions.repository.DocumentoRegistroRepository;
import functions.service.SubscribeToTopic;
import io.grpc.netty.shaded.io.netty.handler.timeout.TimeoutException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServicioNubariumConsume implements HttpFunction {

	private static final Logger log = LogManager.getLogger(ServicioNubariumConsume.class);

	private ObjectMapper objetMapper = new ObjectMapper();
	private static final Gson gson = new Gson();

	private static final String HOST_RFC_GET =System.getenv("HOST_RFC_GET");//"http://172.17.7.125:8092";
	private static final String RFC_GET = "/persona/readDetails?cuentaReferencia=";
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db
	private static final String projectId = System.getenv("PROJECT_ID");
	private static final String topicId = System.getenv("TOPICID");
	private static final String PATH = System.getenv("HOST_GOOGLE"); //https://us-central1-prj-digitalizacion-expedientes.cloudfunctions.net
	private static final String MENSAJE = "EL Documento [:doc] :valido";
	private static final String HOST_NEW_CORE = System.getenv("HOST_NEW_CORE");
	private static final String TOKEN_NEW_CORE = System.getenv("TOKEN_NEW_CORE");
	private static final String RFC_GET_NEW_CORE = "/customer/customerSummary?customerNumber=";
	
	/**
	 * documentoRegistroRepository - variable de tipo DocumentoRegistroRepository
	 * para usarlo como
	 */
	DocumentoRegistroRepository documentoRegistroRepository = new DocumentoRegistroRepository();

	private static HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build();

	// PARAMETRIZAR
	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws IOException, GeneralSecurityException {
		PubSubStorageSendDto body = gson.fromJson(request.getReader(), PubSubStorageSendDto.class);
		Storage storage = PoolStorageHolder.getInstance();
		log.info("parametros recibidos son: " + body);
		DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
		// verificar documento en caso de ser ine validar rCurp && rRfc;
		validarDatos(body, pool, storage);
		final BufferedWriter writer = response.getWriter();
		writer.write("Ok");
	}

	public void validarDatos(PubSubStorageSendDto body, DataSource pool, Storage storage) {
		log.info("body:::::: " + body);
		try {
			if (body.esINE() && this.documentoRegistroRepository.banderaFlujo(pool)) {
				try {
					this.consultaDatosOCR(body, pool, storage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				log.info("##################### Actualiza el estatus del documento cargado #####################");
				obtenerInfoDocumento(body, pool);
			}
		} catch (SQLException e) {
			log.info("#... Ocurrio un error al consultar la parametria");

			e.printStackTrace();
		}
	}

	private void obtenerInfoDocumento(PubSubStorageSendDto body, DataSource pool) {
		SCbitacora objeto = null;
		Respuesta respValDocumentosOtros = null;
		try {
			List<DocumentoRegistroEntity> listDocumentoByClient = documentoRegistroRepository.consultarDoscumentosINE(body.selfLink(), pool);
			log.info("listDocumentoByClient:: ..." + listDocumentoByClient);
			String codigoCliente = "";
			String tipoDocumento = "";
			String codigoSistema = "";
			int idDocumento = 0;
		
			if (listDocumentoByClient != null && listDocumentoByClient.size() > 0) {
				codigoCliente = listDocumentoByClient.get(0).codigo();
				idDocumento = listDocumentoByClient.get(0).id();
				tipoDocumento = listDocumentoByClient.get(0).tipodocumento();
				codigoSistema = listDocumentoByClient.get(0).codsistema();
				respValDocumentosOtros = new Respuesta(idDocumento, false, codigoCliente, codigoCliente, tipoDocumento, "", codigoSistema, true, "", false, "");
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Comienza la validacion del documento.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}else {
				listDocumentoByClient = documentoRegistroRepository.consultarDoscumentosNotINE(body.selfLink(), pool);
				codigoCliente = listDocumentoByClient.get(0).codigo();
				idDocumento = listDocumentoByClient.get(0).id();
				tipoDocumento = listDocumentoByClient.get(0).tipodocumento();
				codigoSistema = listDocumentoByClient.get(0).codsistema();
				respValDocumentosOtros = new Respuesta(idDocumento, false, codigoCliente, codigoCliente, tipoDocumento, "", codigoSistema, true, "", false, "");
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Comienza la validacion del documento.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}
			
			objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Consultando datos en el core.");
			this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			String rfcRespuesta = "";
			RFC rfcObjet = null;
			String[] clienteFsysOPld = splitCliente(codigoCliente);
			if(clienteFsysOPld.length==2) {
				rfcRespuesta = this.validacionNewBank(clienteFsysOPld[0], HOST_NEW_CORE + RFC_GET_NEW_CORE);
				rfcObjet = convertirRfcObjeto(rfcRespuesta, clienteFsysOPld[0], clienteFsysOPld[1]);
			}else {
				rfcRespuesta = this.obtieneDatosRFC(codigoCliente, HOST_RFC_GET + RFC_GET);
				rfcObjet = gson.fromJson(rfcRespuesta, RFC.class);
			}

			log.info("rfcRespuesta::::::: [" + rfcRespuesta + "]");
			
			if (rfcObjet != null && rfcObjet.responseCode() != null && rfcObjet.response() != null) {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta del Core fue completa.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
				respValDocumentosOtros = respValDocumentosOtros.withParticipante_id(rfcObjet.response().clientId());
				respValDocumentosOtros = respValDocumentosOtros.withTPersona(normalizarTipoPersona(rfcObjet.response().personalidadCuentaReferencia()));
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La validacion fue completa.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			} else {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La consulta de datos en el Core no fue completa.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}
			
			objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Se notifica al componente validacion.");
			this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			this.enviarMensajePUBSUB(respValDocumentosOtros);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void consultaDatosOCR(PubSubStorageSendDto body, DataSource pool, Storage storage) throws SQLException {
		ObjetoOCR bodyResponseOCR = null;
		Respuesta respuesta = null;
		String codigoCliente = "";
		String tipoDocumento = "";
		String codigoSistema = "";
		int idDocumento = 0;
		boolean curpCore = false;
		SCbitacora objeto = null;
		List<DocumentoRegistroEntity> listDocumentoByClient = documentoRegistroRepository.consultarDoscumentosINE(body.selfLink(), pool);
		log.info("listDocumentoByClient:: ..." + listDocumentoByClient);
		if (listDocumentoByClient != null & listDocumentoByClient.size() > 0) {
			codigoCliente = listDocumentoByClient.get(0).codigo();
			idDocumento = listDocumentoByClient.get(0).id();
			tipoDocumento = listDocumentoByClient.get(0).tipodocumento();
			codigoSistema = listDocumentoByClient.get(0).codsistema();
			respuesta = new Respuesta(idDocumento, true, codigoCliente, codigoCliente, tipoDocumento, "", codigoSistema, false, "", false, "");
			objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Comienza validacion OCR");
			this.documentoRegistroRepository.guardarBitacora(objeto, pool);
		}
		System.out.println("codigoCliente [" + codigoCliente + "]");
		System.out.println("idDocumento [" + idDocumento + "]");

		try {
			String jsonOCR = objetMapper.writeValueAsString(body);
			java.net.http.HttpRequest httpRequestOCR = java.net.http.HttpRequest.newBuilder()
					.uri(new URI(PATH + "/FunctionObtieneOCR")).headers("Content-Type", "text/plain;charset=UTF-8")
					.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
					.POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonOCR)).build();
			ActualizaEstatusFlujo ocrInfo = new ActualizaEstatusFlujo(0, null, "", idDocumento, jsonOCR, "", 1, null, null, "", "", "", "", "");
			documentoRegistroRepository.insertarScnubocrine(ocrInfo, pool, storage);

			objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Envia solicitud a nubarium OCR");
			this.documentoRegistroRepository.guardarBitacora(objeto, pool);

			java.net.http.HttpResponse<String> httpResponseOCR = client.send(httpRequestOCR, BodyHandlers.ofString());
			log.info("httpResponseOCR.body()::::::: [" + httpResponseOCR.body() + "]");
			bodyResponseOCR = gson.fromJson(httpResponseOCR.body(), ObjetoOCR.class);
			log.info("httpResponseOCR.body()::::::: [" + bodyResponseOCR + "]");
			if (bodyResponseOCR != null && bodyResponseOCR.ineOcrRespOBJ() != null && bodyResponseOCR.ineOcrRespOBJ().getValidacionMRZ() != null) {
				ActualizaEstatusFlujo ocrInfoRespuesta = new ActualizaEstatusFlujo(0, null, bodyResponseOCR.ineOcrRespOBJ().getValidacionMRZ().vigencia(), idDocumento, jsonOCR, httpResponseOCR.body(), 2, null, null, "", "", "", "", "");
				log.info(ocrInfoRespuesta);
				documentoRegistroRepository.actualizarScnubocrine(ocrInfoRespuesta, pool, storage);
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Nubarium OCR responde completa.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			} else {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta de Nubarium OCR no fue completa.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}

			try {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Envia solicitud de RFC Core.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);

				ActualizaEstatusFlujo rfcInfo = new ActualizaEstatusFlujo(0, null, "", idDocumento, (HOST_RFC_GET + RFC_GET + codigoCliente), "", 1, null, null, "", "", "", "", "");
				documentoRegistroRepository.insertarScnubvalidarfcPersona(rfcInfo, pool, storage);

				String rfcRespuesta = "";
				RFC rfcObjet = null;

				String[] clienteFsysOPld = splitCliente(codigoCliente);
				if(clienteFsysOPld.length==2) {
					rfcRespuesta = this.validacionNewBank(codigoCliente, HOST_NEW_CORE + RFC_GET_NEW_CORE);
					rfcObjet = convertirRfcObjeto(rfcRespuesta, clienteFsysOPld[0], clienteFsysOPld[1]);
				}else {
					rfcRespuesta = this.obtieneDatosRFC(codigoCliente, HOST_RFC_GET + RFC_GET);
					rfcObjet = gson.fromJson(rfcRespuesta, RFC.class);
				}
				
				log.info("rfcRespuesta::::::: [" + rfcRespuesta + "]");

					if (rfcObjet != null && rfcObjet.responseCode() != null && rfcObjet.response() != null) {
						objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta de RFC del Core fue completa.");
						this.documentoRegistroRepository.guardarBitacora(objeto, pool);
						respuesta = respuesta.withEsValidoCURP(this.validacionCurp(rfcObjet, bodyResponseOCR));
						respuesta = respuesta.withTPersona(normalizarTipoPersona(rfcObjet.response().personalidadCuentaReferencia()));
					} else {
						log.info("No hay CURP para validar informacion del core no obtenida");
						objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta de RFC del Core no fue completa.");
					this.documentoRegistroRepository.guardarBitacora(objeto, pool);
				}

				if(rfcObjet!=null && rfcObjet.responseCode()!=null) {
					ActualizaEstatusFlujo rfcInfoRespuesta = new ActualizaEstatusFlujo(0, null, rfcObjet.responseCode().message(), idDocumento, (HOST_RFC_GET + RFC_GET + codigoCliente), rfcRespuesta, 2, null, null, "", "", "", "", "");
					documentoRegistroRepository.actualizarScnubvalidarfcPersona(rfcInfoRespuesta, pool, storage);
					documentoRegistroRepository.actualizarScarchivodigital(rfcObjet.response().clientId(), idDocumento, pool, storage);
					respuesta = respuesta.withParticipante_id(rfcObjet.response().clientId());
					try {
						SCcorePersona scorePersona = new SCcorePersona(rfcObjet.response().cuentaReferencia(), "", rfcObjet.response().curpCuentaReferencia(), rfcObjet.response().rfcCuentaReferencia(),
								rfcObjet.response().nombreCuentaReferencia(), rfcObjet.response().apatCuentaReferencia(), rfcObjet.response().amatCuentaReferencia(), rfcObjet.response().fechaNacCuentaReferencia(),
								rfcObjet.response().entidadNacimiento(), rfcObjet.response().personalidadCuentaReferencia(), new Date(1200), new Date(1200), "", "");
						documentoRegistroRepository.insertarScnubvalidarfcPersona(scorePersona, pool, storage);
						SCrefeiyu screfieyu = new SCrefeiyu(rfcObjet.response().clientId(), rfcObjet.response().umbralCuentaReferencia(), new Date(1200), codigoCliente, codigoCliente, null, null, "", "");
						documentoRegistroRepository.insertarSCrefeiyuPersona(screfieyu, pool, storage);
					} catch (Exception ex) {
						log.info("Ocurrio un error al momento de insertar los datos detalle:::: " + ex.getMessage());
					}
				}else {
					log.info("No hay informacion para insertar en las tablas datos del core no obtenidos");
				}
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Se envia a validar el RFC a Nubarium.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);

				if(rfcObjet!=null && rfcObjet.response()!=null) {
					java.net.http.HttpRequest requestRfc = java.net.http.HttpRequest.newBuilder()
							.uri(new URI(PATH + "/FunctionValidaRFC")).headers("Content-Type", "text/plain;charset=UTF-8")
							.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
							.POST(java.net.http.HttpRequest.BodyPublishers
							.ofString(rfcObjet.response().rfcCuentaReferencia())).build();
					ActualizaEstatusFlujo rfcInfoNub = new ActualizaEstatusFlujo(0, null, "", idDocumento,
							rfcObjet.response().rfcCuentaReferencia(), "", 1, null, null, "", "", "", "", "");
					documentoRegistroRepository.updateScnubvalidarfc(rfcInfoNub, pool, storage);
	
					java.net.http.HttpResponse<String> responseRfc = client.send(requestRfc, BodyHandlers.ofString());
					log.info("responseRfc.body()::::::: [" + responseRfc.body() + "]");
					ValidaRfcResponseOBJ bodyResponseRFCNub = gson.fromJson(responseRfc.body(), ValidaRfcResponseOBJ.class);
					log.info("bodyResponseRFCNub.body()::::::: [" + bodyResponseRFCNub + "]");
					if (bodyResponseRFCNub != null) {
						ActualizaEstatusFlujo rfcInfoRespuestaNub = new ActualizaEstatusFlujo(0, null,
								bodyResponseRFCNub.estatus(), idDocumento, rfcObjet.response().rfcCuentaReferencia(),
								responseRfc.body(), 2, null, null, "", "", "", "", "");
						documentoRegistroRepository.updateScnubvalidarfc(rfcInfoRespuestaNub, pool, storage);
						objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta de RFC de Nubarium fue completa.");
						this.documentoRegistroRepository.guardarBitacora(objeto, pool);
	
					} else {
						objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La respuesta de RFC de Nubarium no fue completa.");
						this.documentoRegistroRepository.guardarBitacora(objeto, pool);
					}
				}else {
					log.info("No hay RFC para validar informacion del core no obtenida");
				}
			} catch (TimeoutException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			log.info("bodyResponseOCR validacionINE>>>>>" + bodyResponseOCR);
			if(bodyResponseOCR!=null && bodyResponseOCR.ineOcrRespOBJ()!= null && bodyResponseOCR.ineOcrRespOBJ().getCurp()!=null) {
				log.info("Validacion Curp::::::: " + bodyResponseOCR.ineOcrRespOBJ().getCurp());
				java.net.http.HttpRequest requestCurp = java.net.http.HttpRequest.newBuilder()
						.uri(new URI(PATH + "/FunctionValidaCURP")).headers("Content-Type", "text/plain;charset=UTF-8")
						.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
						.POST(java.net.http.HttpRequest.BodyPublishers
						.ofString(bodyResponseOCR.ineOcrRespOBJ().getCurp())).build();
	
				ActualizaEstatusFlujo curpInfo = new ActualizaEstatusFlujo(0, null, "", idDocumento,
						bodyResponseOCR.ineOcrRespOBJ().getCurp(), "", 1, null, null, "", "", "", "", "");
				documentoRegistroRepository.insertarScnubvalidacurp(curpInfo, pool, storage);
	
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Comienza la validacion CURP Nubarium");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
	
				java.net.http.HttpResponse<String> responseCurp = client.send(requestCurp, BodyHandlers.ofString());
				log.info("responseCurp.body()::::::: [" + responseCurp.body() + "]");
				ValidaCurpResponseOBJ resp = gson.fromJson(responseCurp.body(), ValidaCurpResponseOBJ.class);
				ActualizaEstatusFlujo curpInfoRespuesta = new ActualizaEstatusFlujo(0, null, resp.estatus(), idDocumento,
						bodyResponseOCR.ineOcrRespOBJ().getCurp(), responseCurp.body(), 2, null, null, "", "", "", "", "");
				documentoRegistroRepository.actualizarScnubvalidacurp(curpInfoRespuesta, pool, storage);
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La validacion CURP Nubarium termino.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}else {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La validacion CURP No es posible datos NULL.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}
			log.info("bodyResponseOCR validacionINE>>>>>" + bodyResponseOCR);
			if((bodyResponseOCR!=null && bodyResponseOCR.ineOcrRespOBJ()!= null && bodyResponseOCR.ineOcrRespOBJ().getCic()!=null) &&
						bodyResponseOCR!=null && bodyResponseOCR.ineOcrRespOBJ()!= null && bodyResponseOCR.ineOcrRespOBJ().getIdentificadorCiudadano()!=null) {
				ValidacionIneDto validacionINE = new ValidacionIneDto(bodyResponseOCR.ineOcrRespOBJ().getCic(), bodyResponseOCR.ineOcrRespOBJ().getIdentificadorCiudadano());
				String jsonStrValidaINE = objetMapper.writeValueAsString(validacionINE);
				java.net.http.HttpRequest httpRequestINE = java.net.http.HttpRequest.newBuilder()
						.uri(new URI(PATH + "/FunctionValidaINE")).headers("Content-Type", "text/plain;charset=UTF-8")
						.version(HttpClient.Version.HTTP_2).timeout(Duration.ofSeconds(60))
						.POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonStrValidaINE)).build();
				ActualizaEstatusFlujo ineInfo = new ActualizaEstatusFlujo(0, null, "", idDocumento, jsonStrValidaINE, "", 1, null, null, "", "", "", "", "");
				documentoRegistroRepository.insertarScnubvalidaine(ineInfo, pool, storage);
	
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento,	"Comienza la validacion INE Nubarium");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
	
				java.net.http.HttpResponse<String> httpResponseIne = client.send(httpRequestINE, BodyHandlers.ofString());
				log.info("httpResponseIne.body()::::::: [" + httpResponseIne.body() + "]");
				ValidaINEResponseOBJ respIne = gson.fromJson(httpResponseIne.body(), ValidaINEResponseOBJ.class);
				ActualizaEstatusFlujo ineInfoRespuesta = new ActualizaEstatusFlujo(0, null, respIne.estatus(),
						idDocumento, jsonStrValidaINE, httpResponseIne.body(), 2, null, null, "", "", respIne.claveMensaje(), respIne.vigencia(), respIne.claveElector());
				documentoRegistroRepository.actualizarScnubvalidaine(ineInfoRespuesta, pool, storage);
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La validacion INE Nubarium termino.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}else {
				objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "La validacion INE es posible datos NULL.");
				this.documentoRegistroRepository.guardarBitacora(objeto, pool);
			}
		} catch (InterruptedException e) {
			log.info("InterruptedException...");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			log.info("URISyntaxException::::::::: ...");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.info("JsonProcessingException::::::::: ...");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("IOException...");
			e.printStackTrace();
		}

		objeto = new SCbitacora(idDocumento, codigoSistema, codigoCliente, idDocumento, "Se notifica al componente validacion.");
		this.documentoRegistroRepository.guardarBitacora(objeto, pool);
		this.enviarMensajePUBSUB(respuesta);
	}
	
	private RFC convertirRfcObjeto(String rfcRespuesta, String clienteAntesDiagonal, String clienteDespuesDiagonal) {
		CustomerSummary rfcObjet = gson.fromJson(rfcRespuesta, CustomerSummary.class);
		ResponseCodeRFC responseCode = new ResponseCodeRFC(rfcObjet.responseCode(), rfcObjet.responseMessage());
		String cuentaReferencia = "";
		if(rfcObjet.savingAccountsList()!=null && !rfcObjet.savingAccountsList().isEmpty()) {
			for(SavingAccount account : rfcObjet.savingAccountsList()) {
				if(account.keyAccoun().trim().equalsIgnoreCase(clienteDespuesDiagonal)) {
					cuentaReferencia = account.savingAccountNumber();
				}
			}
		}
		ResponseRFC response = new ResponseRFC(
		clienteAntesDiagonal, 
		rfcObjet.rfc(), 
		clienteDespuesDiagonal, 
		rfcObjet.registrationDate(),
		rfcObjet.personTypeDesc(), 
		rfcObjet.fullName(), 
		"",// ocupacion,
		"",// numeroIdentificacionOficial,
		"",// entidadNacimiento, 
		"",// paisNacimiento, 
		rfcObjet.genre(),//genero
		"",// numSerie, 
		"",// unidadDeNegocio, 
		"",// noInterior, 
		"",// noExterior, 
		"",// geolocalizacion,
		rfcObjet.email(), 
		rfcObjet.personTypeDesc(),// personalidadCuentaReferencia, 
		"",// domicilioCuentaReferencia,
		"",// umbralCuentaReferencia, 
		"",// personalidadRepresentante, 
		rfcObjet.curp(),
		rfcObjet.phoneNumber(), 
		"",// celularCuentaReferencia, 
		"",// estatusCuentaReferencia,
		rfcObjet.nationality(),// coloniaCuentaReferencia, 
		rfcObjet.nationality(),// ciudadCuentaReferencia, 
		rfcObjet.nationality(),// municipioCuentaReferencia,
		rfcObjet.nationality(),// localidadCuentaReferencia, 
		rfcObjet.dateBirth(),// fechaNacCuentaReferencia, 
		"",// entidadCuentaReferencia,
		"",// nacionalidadCuentaReferencia, 
		"",// montoMaximoCuentaReferencia, 
		"",// nombreRepresentante,
		rfcObjet.rfc(), 
		rfcObjet.curp(), 
		"",// coloniaRepresentante,
		"",// entidadNacimientoRepresentante, 
		"",// estatusRepresentante, 
		"",// domicilioRepresentante,
		"",// telefonoRepresentante, 
		"",// fechaNacRepresentante, 
		"",// umbralRepresentante,
		"",// centroTrabajo, 
		"",// municipioRepresentante, 
		"",// localidadRepresentante,
		"",// nacionalidadRepresentante, 
		"",// numSerieRepresentante, 
		"",// paisNacimientoRepresentante,
		"",// entidadRepresentante, 
		"",// ocupacionRepresentante, 
		"",// numeroIdentificacionOficialRepresentante,
		"",// noExteriorRepresentante, 
		"",// nombreCompletoRepresentante, 
		"",// celularRepresentante,
		"",// apatCuentaReferencia, 
		"",// amatCuentaReferencia, 
		"",// apatRepresentante,
		""// amatRepresentante
		);
		
		RFC objetoComvertido = new RFC(responseCode, response);
		
		return objetoComvertido;
	}

	private String normalizarTipoPersona(String tipoPersona) {
		if (tipoPersona == null || tipoPersona.isBlank()) {
			return "";
		}
		String tipoPersonaNormalizado = tipoPersona.trim().toUpperCase();
		if ("F".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("FIS")) {
			return "FISICA";
		}
		if ("M".equals(tipoPersonaNormalizado) || tipoPersonaNormalizado.contains("MOR")) {
			return "MORAL";
		}
		return tipoPersonaNormalizado;
	}

	private void enviarMensajePUBSUB(Respuesta respuestaValidacionDocumento) {
		SubscribeToTopic enviar = new SubscribeToTopic();
		try {
			log.info("Enviando resultados a la QUEUE:: " + respuestaValidacionDocumento);
			String mensajeVerificaExpediente = objetMapper.writeValueAsString(respuestaValidacionDocumento);
			enviar.publisherExample(projectId, topicId, mensajeVerificaExpediente);
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

	private String obtieneDatosRFC(String solicitanteId, String host) {
		log.info("host :: " + host);
		log.info("body :: " + solicitanteId);
		String respuesta = "";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(host + solicitanteId).get().build();
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

	private String validacionNewBank(String solicitanteId, String host) {
		log.info("host :: " + host);
		log.info("body :: " + solicitanteId);
		String respuesta = "";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().addHeader("Autorizacion", TOKEN_NEW_CORE).url(host + solicitanteId).get().build();
		try {
			Response response = client.newCall(request).execute();
			respuesta = response.body().string();
			log.info("SYNC CALL : " + respuesta);
//			rfcObjet = gson.fromJson(respuesta, RFC.class);
			return respuesta;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return respuesta;
	}
	
	private String splitClienteUno(String idCliente) {
		String[] id = {};
		if(!idCliente.isEmpty()) {
			id = idCliente.split("/");
		}
		String idSplit =id[0];
		log.info("idSplit : " + idSplit);

		return idSplit;
	}
	
	private String[] splitCliente(String idCliente) {
		String[] id = {};
		if(!idCliente.isEmpty()) {
			id = idCliente.split("/");
		}
		return id;
	}
	
	public boolean validacionCurp(RFC rfcObjetCore, ObjetoOCR bodyResponseOCR) {
		boolean validacion = false;
		log.info("rfcObjetCore>> " + rfcObjetCore);
		log.info("bodyResponseOCR>> " +bodyResponseOCR);
		if(rfcObjetCore!=null && bodyResponseOCR!=null && rfcObjetCore.response()!=null 
				&& bodyResponseOCR.ineOcrRespOBJ()!=null && rfcObjetCore.response().rfcCuentaReferencia()!=null 
				&& bodyResponseOCR.ineOcrRespOBJ().getCurp()!=null) {
			log.info(rfcObjetCore.response().rfcCuentaReferencia().trim());
			log.info(bodyResponseOCR.ineOcrRespOBJ().getCurp().trim());
			if(rfcObjetCore.response().rfcCuentaReferencia().trim().equalsIgnoreCase(bodyResponseOCR.ineOcrRespOBJ().getCurp().trim())) {
				validacion = true;
			}
		}
		
		return validacion;
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
