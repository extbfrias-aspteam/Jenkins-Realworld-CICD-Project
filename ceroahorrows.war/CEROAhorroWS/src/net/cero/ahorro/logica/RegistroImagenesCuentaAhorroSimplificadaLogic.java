package net.cero.ahorro.logica;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import com.mx.Req.ImagenAlfrescoReq;
import com.mx.Res.RespuestaCommons;

import net.cero.data.AgenteOBJ;
import net.cero.data.AhorroAlfrescoOBJ;
import net.cero.data.AhorroContrato;
import net.cero.data.AhorroContratoDatos;
import net.cero.data.AhorroCuentaOBJ;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaReq;
import net.cero.data.AhorroTransferenciaReqOBJ;
import net.cero.data.CampaniaOBJ;
import net.cero.data.ColoniaOBJ;
import net.cero.data.DirectorioTelefonicoOBJ;
import net.cero.data.GeneraReporteContratoReq;
import net.cero.data.GeneraReporteTarjetaReq;
import net.cero.data.GuardarServiciosDigitalesReq;
import net.cero.data.IneOcrRespOBJ;
import net.cero.data.RegionesOBJ;
import net.cero.data.RegistroCodiOBJ;
import net.cero.data.RegistroCuentaAhorroSimplificadaReq;
import net.cero.data.RegistroImagenesCuentaAhorroSimplificadaReq;
import net.cero.data.RegistroServiciosDigitalesOBJ;
import net.cero.data.ResponseService;
import net.cero.data.Respuesta;
import net.cero.data.SMS;
import net.cero.data.Solicitante;
import net.cero.data.ValidacionOcrReq;
import net.cero.promesi.AuthHeadersRequest;
import net.cero.promesi.RestCall2;
import net.cero.ahorro.logica.GeneracionFolioLogic;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.Encrypted;
import net.cero.seguridad.utilidades.iso9564;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AgenteDAO;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroContratoDatosDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.AuditoriaDAO;
import net.cero.spring.dao.CampaniaDAO;
import net.cero.spring.dao.CanalesDAO;
import net.cero.spring.dao.ColoniaDAO;
import net.cero.spring.dao.DirectorioTelefonicoDAO;
import net.cero.spring.dao.PINDAO;
import net.cero.spring.dao.ParametrosGeneralesDAO;
import net.cero.spring.dao.RegionesDAO;
import net.cero.spring.dao.RegistroCodiDAO;
import net.cero.spring.dao.RegistroServiciosDigitalesDAO;
import net.cero.spring.dao.SolicitanteDAO;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Logica de negocio para registrar una cuenta de ahorro simplificada.
 * 
 * @author Israel
 * @version 1.0 01/06/19
 */
@Log4j2
public class RegistroImagenesCuentaAhorroSimplificadaLogic {
	private static Apps apps = null;

	private static AhorroContratoDAO adao;
	private static Gson gson;
	private static AuthHeadersRequest headerAuth = null;

	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			gson = new Gson();
			headerAuth = new AuthHeadersRequest("SISTEMAS");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public ResponseService registroImagenesCuentaAhorroSimplificadaReq(RegistroImagenesCuentaAhorroSimplificadaReq req) {
		initialized();
		ResponseService respuesta = new ResponseService();
		AhorroContrato ahorroContrato = null;

		String numeroSolicitanteNuevo = "";
		try {
			ahorroContrato = adao.buscarByCuenta(req.getNumeroCuenta());
			
			String solicitante = ahorroContrato.getSolicitante();
			String cuenta = ahorroContrato.getCuenta();
			String contrato = ahorroContrato.getContrato();
			String rutaAlfresco = new String ("Alfresco/Personas/" + solicitante + "/Ahorro/" +cuenta);
			String[] idArchivoAlfresco = uploadIneAlfresco(req.getValidacionOcrReq(), solicitante, cuenta);
			AhorroAlfrescoOBJ[] objeto = new AhorroAlfrescoOBJ[2];
			
			objeto[0] = LlenadoCamposBDAlfresco(cuenta, 6, rutaAlfresco, idArchivoAlfresco[0], "INE FRONTAL");
			objeto[1] = LlenadoCamposBDAlfresco(cuenta, 7, rutaAlfresco, idArchivoAlfresco[1], "INE REVERSO");
			
			OkHttpClient client = new OkHttpClient();
			String auth = Credentials.basic("ASP", "a5p2017$");
			MediaType media = MediaType.parse("application/json; charset=utf-8");
			Request request;
			Response resp;
			String porm = "empt";
			String host = ConstantesUtil.WS_CERO_AHORRO+"/CuentaAhorroBDAlfresco";
			
			for(int i=0; i<=objeto.length; i++) {
				String obj = gson.toJson(objeto[i]);
				request = new Request.Builder().url(host).post(RequestBody.create(media, obj)).header("Authorization", auth).build();
				
				try {
					resp = client.newCall(request).execute();
					porm = resp.body().string();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info("###PORM: " + porm);
				Respuesta resp2 = gson.fromJson(porm, Respuesta.class);
			}			

			respuesta.setCode(0);
			respuesta.setMenssage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(-1);
			respuesta.setMenssage("Se presento un problema al subir las imgenes");
		}
		
		return respuesta;
	}

	protected String[] uploadIneAlfresco(String validacionOcrReq, String numeroSolicitante, String cuenta) {
		String[] idArchivo = new String[2];
		try {
			String nombreArchivo = "";
			if (validacionOcrReq == null) {
				log.info("validacionOcrReq es null");
			} else if (validacionOcrReq.isEmpty()) {
				log.info("validacionOcrReq esta vacio");
			} else {
				// log.info("validacionOcrReq :: " +
				// validacionOcrReq);
			}

			ValidacionOcrReq validacionOcrObj = new ValidacionOcrReq();
			validacionOcrObj = gson.fromJson(validacionOcrReq, ValidacionOcrReq.class);
			if (validacionOcrObj != null) {
				if (validacionOcrObj.getId() != null) {
					if (!validacionOcrObj.getId().isEmpty()) {
						log.info("Inicia procesamiento frontal ine");
						nombreArchivo = "IneFrontal.png";
						byte[] imgBytes = Base64.getDecoder()
								.decode(new String(validacionOcrObj.getId()).getBytes("UTF-8"));
						;
						if (imgBytes != null) {
							log.info("Creo el arreglo de bytes de IneFrontal :: " + imgBytes.length);
							idArchivo[0] = invokeServiceUploadImages(imgBytes, nombreArchivo, numeroSolicitante, cuenta);
						}
					} else {
						log.info("validacionOcrObj.id esta vaciol");
					}
				} else {
					log.info("validacionOcrObj.id es null");
				}

				if (validacionOcrObj.getIdReverso() != null) {
					if (!validacionOcrObj.getIdReverso().isEmpty()) {
						log.info("Inicia procesamiento reverso ine");
						nombreArchivo = "IneReverso.png";
						byte[] imgReversoBytes = Base64.getDecoder()
								.decode(new String(validacionOcrObj.getIdReverso()).getBytes("UTF-8"));
						;
						if (imgReversoBytes != null) {
							log.info("Creo el arreglo de bytes de IneReverso :: " + imgReversoBytes.length);
							idArchivo[1] = invokeServiceUploadImages(imgReversoBytes, nombreArchivo, numeroSolicitante, cuenta);
						}
					} else {
						log.info("validacionOcrObj.idReverso esta vacio");
					}
				} else {
					log.info("validacionOcrObj.idReverso es null");
				}
			} else {
				log.info("validacionOcrObj es null");
			}
		} catch (Exception e) {
			log.error("ERROR AL GENERAR REPORTE");
			e.printStackTrace();

		}
		return idArchivo;
	}
	//restScoring
	private String invokeServiceUploadImages(byte[] imgBytes, String nombreArchivo, String numeroSolicitante,
			String cuentaAhorro) {
		
		String idArchivo = "";
		String archivo = "";
		try {

			String nameFolder = callCrearCarpeta(requestParametersCreateFolder(numeroSolicitante, cuentaAhorro));
			log.info("nameFolder :: " + nameFolder);
			ImagenAlfrescoReq req = requestParametersUploadImage(imgBytes, nombreArchivo, nameFolder);

			if (req != null) {
				RespuestaCommons response = callUploadFileImage(req);
				if (response.getImagenAlfresco() != null) {
					if (response.getImagenAlfresco().getIdImagen() != null) {
						idArchivo = response.getImagenAlfresco().getIdImagen();
					} else {
						log.info("response.getImagenAlfresco().getIdImagen() null");
					}

					if (response.getImagenAlfresco().getNombre() != null) {
						archivo = response.getImagenAlfresco().getNombre();
					} else {
						log.info("response.getImagenAlfresco().getNombre() null");
					}
					log.info("Se cargo el archivo exitosamente");

				} else {
					log.info("Ocurrio un error al subir archivo");
				}
			} else {
				log.info("Ocurrio un error al subir archivo ImagenAlfrescoReq null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return idArchivo;
		
	}

	private String callCrearCarpeta(ImagenAlfrescoReq requestParameters) {
		RespuestaCommons response;

		RestCall2<ImagenAlfrescoReq, RespuestaCommons> callServiceAlfresco = new RestCall2<>();

		callServiceAlfresco.setUrl(ConstantesUtil.ALFRESCO_WS + "/WSAlfrescoREST" + "/data" + "/CrearCarpeta");
		if (callServiceAlfresco.getAuth() == null)
			callServiceAlfresco.setAuth(headerAuth);
		callServiceAlfresco.setEntrada(requestParameters);
		callServiceAlfresco.setClase(RespuestaCommons.class);
		response = callServiceAlfresco.call();
		return requestParameters.getCarpeta();
	}

	private final ImagenAlfrescoReq requestParametersCreateFolder(String numeroSolicitante, String cuentaAhorro) {
		ImagenAlfrescoReq request = new ImagenAlfrescoReq();
		request.setCarpeta("Personas/" + numeroSolicitante + "/Ahorro/" + cuentaAhorro);
		return request;
	}

	private RespuestaCommons callUploadFileImage(ImagenAlfrescoReq requestParameters) {
		RespuestaCommons response;
		RestCall2<ImagenAlfrescoReq, RespuestaCommons> peticion = new RestCall2<>();
		peticion.setUrl(ConstantesUtil.ALFRESCO_WS + "/WSAlfrescoREST" + "/data" + "/SubirImagenes");
		log.info("alfresco url :: " + peticion.getUrl());
		if (peticion.getAuth() == null)
			peticion.setAuth(headerAuth);
		peticion.setEntrada(requestParameters);
		peticion.setClase(RespuestaCommons.class);
		response = peticion.call();
		if (response == null) {
			log.info("Response alfresco null");
		} else {
			log.info("Response alfresco no es null");
			// log.info("Response alfresco :: " +
			// gson.toJson(response));
		}
		return response;
	}

	private final ImagenAlfrescoReq requestParametersUploadImage(byte[] bytesArray, String nameImage, String folder) {
		ImagenAlfrescoReq requestParameters = new ImagenAlfrescoReq();
		try {
			requestParameters.setFile(bytesArray);
			requestParameters.setCarpeta(folder);
			requestParameters.setNombreImagen(nameImage);
			// ruta += folder;
			// archivo = nameImage;
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return requestParameters;
	}
	
	private AhorroAlfrescoOBJ LlenadoCamposBDAlfresco(String contrato, int idDocumento, String carpeta, String arregloArchivo, String nombre) {
		AhorroAlfrescoOBJ obj= new AhorroAlfrescoOBJ();
		
		obj.setCuenta(contrato);
		obj.setDocumentos_ahorro_id(idDocumento);
		obj.setRuta_alfresco(carpeta);
		obj.setId_archivo_alfresco(arregloArchivo);
		obj.setObservaciones("");
		obj.setNombre(nombre);
		obj.setFecha_expedicion(null);
		obj.setFecha_vigencia(null);
		return obj;
	}

	// ###############################################################################################################################################################################
	

}
