package functions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import functions.cloudsql.CloudSqlConnectionPool;
import functions.cloudsql.TcpConnectionPoolFactory;
import functions.dto.DatoDocumento;
import functions.dto.IneOcrReqOBJ;
import functions.dto.IneOcrRespOBJ;
import functions.dto.ObjetoOCR;
import functions.dto.PubSubStorageSendDto;
import functions.repository.DocumentoRegistroRepository;
import functions.service.FileServiceImpl;
import functions.service.IFileService;
import functions.service.ObtenerDatosIneOcrLogic;
import funtions.entity.DocumentoRegistroEntity;

public class FunctionObtieneOCR implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionObtieneOCR.class);

	private ObjectMapper objetMapper = new ObjectMapper();
	/**
	 * fileService - variable de tipo IFileService para usarlo como
	 */
	private IFileService fileService = new FileServiceImpl();

	private static final Gson gson = new Gson();

	private static final String USER_NUBARIUM = System.getenv("USER_NUBARIUM");
	private static final String PASS_NUBARIUM = System.getenv("PASS_NUBARIUM");
	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");
	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db
	/**
	 * documentoRegistroRepository - variable de tipo DocumentoRegistroRepository
	 * para usarlo como
	 */
	DocumentoRegistroRepository documentoRegistroRepository = new DocumentoRegistroRepository();

	@Override
	public void service(final HttpRequest request, final HttpResponse response) throws GeneralSecurityException, IOException {
		PubSubStorageSendDto body = null;
		try {
			System.out.println("request.getReader():: " + request.getReader());
			body = gson.fromJson(request.getReader(), PubSubStorageSendDto.class);
			Storage storage = PoolStorageHolder.getInstance();
			DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);
			// verificar documento en caso de ser ine validar rCurp && rRfc;
			String infoOCR = consultaDatosOCR(body, pool, storage);
			final BufferedWriter writer = response.getWriter();
			writer.write(infoOCR);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String consultaDatosOCR(PubSubStorageSendDto body, DataSource pool, Storage storage) {
		IneOcrReqOBJ req = null;
		ObtenerDatosIneOcrLogic obtener = new ObtenerDatosIneOcrLogic();
		List<DocumentoRegistroEntity> listDocumentoByClient;
		String codigoCliente = "";
		int idDocumento = 0;
		try {
			log.info("Entro a consultar...");
			listDocumentoByClient = this.documentoRegistroRepository.consultarDoscumentosINE(body.selfLink(), pool);
			log.info("Termino la consulta...");
			if(listDocumentoByClient != null & listDocumentoByClient.size()>0 ) {
				codigoCliente = listDocumentoByClient.get(0).getCodigo();
				idDocumento = listDocumentoByClient.get(0).getId();
			}
			req = this.obtenerObjeto(listDocumentoByClient, storage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ObjetoOCR obnjeto = new ObjetoOCR(new DatoDocumento(idDocumento, codigoCliente), null);
		if (req != null && (req.id() != null && !req.id().isEmpty())
				&& (req.idReverso() != null && !req.idReverso().isEmpty())) {
			String info = obtener.obtenerDatosIne(req, USER_NUBARIUM, PASS_NUBARIUM);
			log.info("Consulto los datos del OCR...[" + info + "]");
			obnjeto = obnjeto.withineObjet(gson.fromJson(info, IneOcrRespOBJ.class));
			log.info("Consulto los datos del OCR ineObjet...[" + obnjeto.ineOcrRespOBJ() + "]");
//			jsonIne = jsonIne.replace("{", "{\"\":\""+codigoCliente+"\",\"clave\":\""+codigoCliente+"\",");
//		}else {
//			jsonIne = "{\"clave\":\""+codigoCliente+"\",\"calle\":\"\",\"cic\":\"\",\"ciudad\":\"\",\"claveElector\":\"\",\"codigoValidacion\":\"\",\"colonia\":\"\",\"curp\":\"\",\"emision\":\"\",\"estado\":\"\",\"fechaNacimiento\":\"\",\"identificadorCiudadano\":\"\",\"localidad\":\"\",\"mrz\":\"\",\"municipio\":\"\",\"nombres\":\"\",\"ocr\":\"\",\"primerApellido\":\"\",\"registro\":\"\",\"seccion\":\"\",\"segundoApellido\":\"\",\"sexo\":\"\",\"subTipo\":\"\",\"tipo\":\"\",\"validacionesMRZ\":{\"emision\":\"KO\",\"fechaNacimiento\":\"KO\",\"nombre\":\"KO\",\"sexo\":\"KO\",\"vigencia\":\"KO\"},\"vigencia\":\"\"}";
		}
//		obnjeto.setIneOcrRespOBJ(ineObjet);
		try {
			return objetMapper.writeValueAsString(obnjeto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		};
		return "";
	}
	
//PARAMETRIZAR
	private IneOcrReqOBJ obtenerObjeto(List<DocumentoRegistroEntity> listDocumentoByClient, Storage storage) {
		String bucketName = System.getenv("BUCKET_NAME");
		ByteArrayResource frontal = null;
		ByteArrayResource reverso = null;
		IneOcrReqOBJ req = null;
		log.info("Entro en metodo obtener objeto>>>> " + listDocumentoByClient);
		if (listDocumentoByClient != null && !listDocumentoByClient.isEmpty() && listDocumentoByClient.size() == 2) {
			req = new IneOcrReqOBJ("", "");
			for (DocumentoRegistroEntity documentoRegistroEntity : listDocumentoByClient) {
				if ("INE_FRONTAL".equals(documentoRegistroEntity.getTipodocumento())) {
					log.info("Descargando el frontal:: " + documentoRegistroEntity.getRuta_storage());
					frontal = fileService.downloadFile(documentoRegistroEntity.getCodigo().trim()+"/"+documentoRegistroEntity.getTipodocumento().trim()+".jpeg",
							storage, bucketName);
					log.info("Archivo frontal:: " + reverso);
					req = req.withIneOcrReqOBJId(convertirImgToBase64(frontal));
				} else if ("INE_REVERSO".equals(documentoRegistroEntity.getTipodocumento())) {
					log.info("Descargando el reverso:: " + documentoRegistroEntity.getRuta_storage());
					reverso = fileService.downloadFile(documentoRegistroEntity.getCodigo().trim()+"/"+documentoRegistroEntity.getTipodocumento().trim()+".jpeg",
							storage, bucketName);
					log.info("Archivo el reverso:: " + reverso);
					req = req.withIneOcrReqOBJIdRev(convertirImgToBase64(reverso));
				}
			}
		}
		System.out.println("###########################  Objeto>>> " + req);
		return req;
	}

	private String convertirImgToBase64(ByteArrayResource imagen) {
		String encodedString = "";
		if (imagen != null) {
			encodedString = Base64.getEncoder().encodeToString(imagen.getByteArray());
			log.info("Archivo el codificado:: " + encodedString);
		}
		return encodedString;
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