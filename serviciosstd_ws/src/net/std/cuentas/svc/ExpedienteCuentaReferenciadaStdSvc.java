package net.std.cuentas.svc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
//import net.std.constantes.Respuesta;
import net.std.constantes.ValidaPermisos;
import net.std.cuentas.logic.ValidaCuentaReferenciadas;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.data.CuentaReferenciadaVolumenOBJ;
import net.std.data.DomicilioOBJ;
import net.std.data.ExpedienteBluOBJ;
import net.std.data.ExpedienteCuentaRefOBJ;
import net.std.data.PersonaOBJ;
import net.std.data.SolicitanteOBJ;
import net.std.expediente.dao.ExpedienteStdDAO;
import net.std.request.AltaDocumentoReq;
import net.std.request.DomicilioReq;
import net.std.servicios.ProcesoAltaSolicitante;
import net.std.servicios.ProcesoGeneraSolicitante;
import net.std.sftp.SFTPLogic;
import net.std.valida.nubarium.ValidaNubariumClient;

@SuppressWarnings("unused")
@Controller
public class ExpedienteCuentaReferenciadaStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ExpedienteCuentaReferenciadaStdSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static SolicitanteStdDAO daoSol = null;
	private static CuentasReferenciadasStdDAO daoRef = null;
	private static ExpedienteStdDAO daoExp = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}	
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");
			daoSol = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoExp = (ExpedienteStdDAO)s.getApplicationContext().getBean("ExpedienteStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="expedienteCuentaReferenciadaStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<ExpedienteCuentaRefOBJ>lstRef = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try{
			if(daoRef == null || daoSol == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(daoRef == null || daoSol == null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			ObjectMapper mapper = new ObjectMapper();
			lstRef = mapper.readValue(json, new TypeReference<List<ExpedienteCuentaRefOBJ>>(){});
			if(lstRef == null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_CAMPOS_REQUERIDOS, "LISTA DE CUENTAS REFERENCIADAS VACIAS"), HttpStatus.FORBIDDEN);
				return response;
			}
		}catch(Exception ex){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}

		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CREAR_CUENTAS_REFERENCIADAS), "TRX_CREAR_CUENTAS_REFERENCIADAS: " + Comun._T(json))){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}

		try{
			
			List<Map<String, String>> listMapResultados = new ArrayList<>(); 
			for(ExpedienteCuentaRefOBJ obj : lstRef){
				String cuentaReferencia =  Comun._TX(obj.getCuenta_referencia());	
				String obs = null;

			
				RespuestaSVC respValida = validacionesCuenta(obj);
				String obsValida = Comun._T(respValida.getBody().getValor("ESTATUS"));
	
				if(obsValida.equals("OK")){
				
					String solId = Comun._T(respValida.getBody().getValor("SOLICITANTE_ID"));
					String repLegalId = Comun._T(respValida.getBody().getValor("REPLEGAL_ID"));
					String tipoCuentaNivel = Comun._T(respValida.getBody().getValor("TIPO_CUENTA_NIVEL"));
					String tipoPersonaSol = "";
					
					RespuestaSVC respSol = daoSol.solicitanteStdDao(solId);
					String obsSolicitante = "OK";
					if(respSol.getErrores().getCodigoError() != 0){
						obsSolicitante = Errores.desc(Errores.ERROR_SOLICITANTE, "SOLICITANTE");
					}else{
						tipoPersonaSol = Comun._T(respSol.getBody().getValor("T_PERSONA"));
					}
					if(tipoPersonaSol.equals("M") && repLegalId.equals("")){
						obsSolicitante = Errores.desc(Errores.ERROR_SOLICITANTE, "REPRESENTANTE LEGAL");
					}
					if(obsSolicitante.equals("OK")){
						
						RespuestaSVC respDoctos = validaDocumentos(obj.getArchivos(),tipoCuentaNivel,tipoPersonaSol);
						String obsDoctos = Comun._T(respDoctos.getBody().getValor("ESTATUS"));
						if(obsDoctos.equals("OK")){

							List<Map<String,String>> listaDoctos = (List<Map<String, String>>) respDoctos.getBody().getValor("DOCTOS");
							
							/*******	SE SUBEN DOCUMENTOS A SERVIDOR SFTP *******************/
							RespuestaSVC respuestaDoctos = new RespuestaSVC();
							respuestaDoctos = subirDocumentosSolicitante(obj,solId, repLegalId, cuentaReferencia,tipoCuentaNivel, listaDoctos);				
							
							String estatusDoctos = Comun._T(respuestaDoctos.getBody().getValor("ESTATUS"));
							if(estatusDoctos.equals("OK")){															
								List<Map<String, String>> lista = (List<Map<String, String>>) respuestaDoctos.getBody().getValor("DATOS_DOCTOS");
								List<Map<String, String>> listaCliente = (List<Map<String, String>>) respuestaDoctos.getBody().getValor("DATOS_DOCTOS_CLIENTE");
								
								String insertDoctos = insertarDocumentos(lista, listaCliente, cuentaReferencia);					
								if(!insertDoctos.equals("OK")){
									obs = insertDoctos;									
								}
								
							}else{
								obs = estatusDoctos;
							}		
						}else{
							obs = obsDoctos;
						}
					}else{
						obs = obsSolicitante;
					}
				}else{
					obs = obsValida;
				}

				Map<String, String> map = new HashMap<>();
				map.put("CUENTA_REFERENCIA", cuentaReferencia);
				map.put("RESULTADO", "".equals(Comun._T(obs)) ? "OK" : "ERROR");
				map.put("OBSERVACIONES", Comun._T(obs));
				listMapResultados.add(map);
			}

			respuestaSvc.getBody().addValor("RESULTADO", listMapResultados);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);

		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	@SuppressWarnings("unchecked")
	private RespuestaSVC validaDocumentos(List<ExpedienteBluOBJ> archivos, String tipoCuentaNivel, String tipoPersonaSol) {
		RespuestaSVC resp = new RespuestaSVC();
		RespuestaSVC doctos = daoExp.listDocumentosNivel(tipoCuentaNivel, tipoPersonaSol);
		String obs = "OK";
		if(doctos.getErrores().getCodigoError() != 0L){
			resp.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO, " AL OBTENER DOCUMENTOS"));
			return resp;
		}
		List<Map<String,String>> lista = (List<Map<String, String>>) doctos.getBody().getValor("DOCTOS");
		for (Map<String, String> map : lista) {
			int bandera = 0;
			for (ExpedienteBluOBJ obj : archivos) {
				if(Comun._T(map.get("CLAVE")).equals(obj.getNombre()) && Comun._T(map.get("REP_LEGAL")).equals(obj.getRepLegal())){
					++bandera;
				}
			}
			if(bandera == 0){
				obs = "ERROR :: FALTA DOCUMENTO - " + map.get("CLAVE");
			}
			if(bandera > 1){
				obs = "ERROR :: DOCUMENTO DUPLICADO - " + map.get("CLAVE");
			}
		}
		resp.getBody().addValor("ESTATUS", obs);
		resp.getBody().addValor("DOCTOS", lista);
		return resp;
	}

	@SuppressWarnings("unchecked")
	private RespuestaSVC validacionesCuenta(ExpedienteCuentaRefOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		String obs = "OK";
		String persona_id = "";		
		String repLegal_id = "";
		String tipoCuentaNivel = "";
		
		/* VERIFICA QUE LA CUENTA REFERENCIADA EXISTA */
		RespuestaSVC respRef = daoRef.leerCuentaReferenciadaSolicitanteStdDao(obj.getCuenta_referencia());
		if(respRef.getErrores().getCodigoError() != 0L){
			obs = Errores.desc(Errores.ERROR_CUENTA_REFERENCIADA_EN_PROCESO, obj.getCuenta_referencia());
		}else{
			Map<String, String> mapa = (Map<String, String>) respRef.getBody().getValor("CUENTA");
			persona_id = Comun._T(mapa.get("SOLICITANTE_ID"));
			repLegal_id = Comun._T(mapa.get("REPLEGAL_ID"));
			tipoCuentaNivel = Comun._T(mapa.get("TIPO_CUENTA_AHORRO_CLAVE"));
		}

		respuesta.getBody().addValor("SOLICITANTE_ID", persona_id);
		respuesta.getBody().addValor("REPLEGAL_ID", repLegal_id);
		respuesta.getBody().addValor("TIPO_CUENTA_NIVEL", tipoCuentaNivel);
		respuesta.getBody().addValor("ESTATUS", obs);
		
		return respuesta;
	}
	public String insertarDocumentos(List<Map<String, String>> lista, List<Map<String, String>> listaClientes, String cuentaReferencia) {
		String obs = "OK";
		for (Map<String, String> map : lista) {
			String nombreArchivo = map.get("NOMBRE_ARCHIVO");
			String claveArchivo = map.get("CLAVE_ARCHIVO");
			String rutaArchivo = map.get("RUTA_ARCHIVO");
			String solID = map.get("SOLICITANTE_ID");
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		    Calendar cal = Calendar.getInstance();
			String fechaExpedicion = format.format(cal.getTime());
			
			cal.add(Calendar.YEAR, 1);
			String fechaVigencia = format.format(cal.getTime());
			
			RespuestaSVC res = daoExp.insertarCuentaRefExpedienteStdDao(solID, cuentaReferencia, claveArchivo, rutaArchivo, nombreArchivo, fechaExpedicion, fechaVigencia, Comun._I(Constantes.USUARIO_ID));
			if(res.getErrores().getCodigoError()!=0){
				obs = res.getErrores().getDescError();
				log.error(obs);
				return obs;
			}
		}
		for (Map<String, String> map : listaClientes) {
			String claveArchivo = map.get("CLAVE_ARCHIVO");
			String rutaArchivo = map.get("RUTA_ARCHIVO");
			String tipoArchivo = map.get("TIPO_ARCHIVO");
			String solID = map.get("SOLICITANTE_ID");
			
			RespuestaSVC res = daoExp.insertarClienteExpedienteStdDao(solID, rutaArchivo, tipoArchivo, claveArchivo, Comun._I(Constantes.USUARIO_ID));
			if(res.getErrores().getCodigoError()!=0){
				obs = res.getErrores().getDescError();
				log.error(obs);
				return obs;
			}
		}
		
		return obs;
	}
	public RespuestaSVC subirDocumentosSolicitante(ExpedienteCuentaRefOBJ sol, String solID, String repLegalId, String cuentaReferencia, String tipoCuentaNivel, List<Map<String,String>> listaDoctos) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		Map<String,String> datos = new HashMap<String, String>();
		String obs = "OK";
		int i = 0;
		String ruta = ""; 
		String datosDoctos = "DATOS_DOCTOS";
		String carpeta = "CUENTA_REFERENCIA/" + sol.getCuenta_referencia();
		
			
		do{
			lista = new ArrayList<Map<String,String>>();
			for (ExpedienteBluOBJ archivos : sol.getArchivos()) {
				String numeroSol = "";
				Boolean repL = false;

				if(archivos.getRepLegal().equals("S")){
					numeroSol = repLegalId;
				}else{
					numeroSol = solID;
				}
				
				ruta = String.format(numeroSol + "/%s" ,carpeta); 
				
				obs = validaDoctos(archivos);
				if(!obs.equals("OK")){
					respuesta.getBody().addValor("ESTATUS", obs);
					return respuesta;
				}
				
				datos = subirDocto(archivos.getDocumento(), archivos.getExtension(), archivos.getNombre(), ruta);
				obs = Comun._T(datos.get("ESTATUS"));
				if(!obs.equals("OK")){
					respuesta.getBody().addValor("ESTATUS", obs);
					return respuesta;
				}
				
				datos.put("SOLICITANTE_ID", numeroSol);
				lista.add(datos);
			}
			respuesta.getBody().addValor(datosDoctos, lista);
			i++;
			carpeta = "CLIENTE"; 
			datosDoctos = "DATOS_DOCTOS_CLIENTE";
		}while(i < 2);
		
		respuesta.getBody().addValor("ESTATUS", obs);
		return respuesta;
	}
	
	private String validaDoctos(ExpedienteBluOBJ obj) {
		if(obj.getExtension().equals(""))
			return String.format("ERROR :: DOCUMENTO INVALIDO (EXTENSION)- %s",obj.getNombre());
		if(obj.getDocumento().equals(""))
			return String.format("ERROR :: DOCUMENTO INVALIDO (DOCUMENTO)- %s",obj.getNombre());
		
		return "OK";
	}

	private Map<String,String> subirDocto(String docto, String tipo, String nameFile, String nameFolder){
		RespuestaSVC respuesta = new RespuestaSVC();
		String obs = "OK";
		AltaDocumentoReq doc = new AltaDocumentoReq();
		doc.setDocumento(docto);
		doc.setNombreArchivo(nameFile);
		doc.setRutaArchivo(nameFolder);
		doc.setTipoArchivo(tipo);
		
		respuesta = SFTPLogic.procesar(doc);
		if(respuesta.getErrores().getCodigoError() != 0){
			obs = String.format("ERROR: AL SUBIR - %s",nameFile);
		}
		Map<String,String> datos = new HashMap<>();
		datos.put("NOMBRE_ARCHIVO", nameFile + "." +tipo);
		datos.put("TIPO_ARCHIVO", tipo);
		datos.put("CLAVE_ARCHIVO", nameFile);
		datos.put("RUTA_ARCHIVO", Comun._T(respuesta.getBody().getValor("RUTA_ARCHIVO")));
		datos.put("ESTATUS", obs);
		return datos;
	}
	
}
