package net.std.cuentas.svc.v2;

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
public class CrearCuentaReferenciadaStdSvcV2 implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CrearCuentaReferenciadaStdSvcV2.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static SolicitanteStdDAO daoSol = null;
	private static CuentasReferenciadasStdDAO daoRef = null;
	private static ExpedienteStdDAO daoExp = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");
			daoSol = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoExp = (ExpedienteStdDAO)s.getApplicationContext().getBean("ExpedienteStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/v2/crearCuentaReferenciadaStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<CuentaReferenciadaVolumenOBJ>lstRef = null;
		ValidaCuentaReferenciadas valCuentas = new ValidaCuentaReferenciadas();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try{
			if(dao == null || daoRef == null || daoSol == null || daoExp == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoRef == null || daoSol == null || daoExp == null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			ObjectMapper mapper = new ObjectMapper();
			lstRef = mapper.readValue(json, new TypeReference<List<CuentaReferenciadaVolumenOBJ>>(){});
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

		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */

		try{
			/* GUARDA EN TABLA DE VOLUMEN PARA SU POSTERIOR APLICACION */
			/**********	SE COMENTA PARA MEJORA DE TIEMPO		*************/
			/*RespuestaSVC respGuardarMasiva = valCuentas.guardarCuentasReferenciadasMasivas(lstRef);
			if(respGuardarMasiva.getErrores().getCodigoError() != 0L){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA_MASIVA, respGuardarMasiva.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response; 
			}

			//RespuestaSVC respLog = ProcesoBitLogger.procesar(Constantes.PROCESO, "ALTA CUENTAS REFERENCIADAS", json);

			String control = (String)respGuardarMasiva.getBody().getValor("CONTROL");
			RespuestaSVC respMasivas = daoRef.listarCuentaReferenciadaMasivaStdDao(control, null);
			if(respMasivas.getErrores().getCodigoError() != 0L){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_PROCESAR_CUENTA_REFERENCIADA_MASIVA, respMasivas.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response; 
			}

			List<CuentaReferenciadaVolumenOBJ> lst = (List<CuentaReferenciadaVolumenOBJ>)respMasivas.getBody().getValor("LISTA_CUENTA");
			if(lst == null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_PROCESAR_CUENTA_REFERENCIADA_MASIVA, respMasivas.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response; 
			}*/	
			/*****************************************************/
			
			List<CuentaReferenciadaVolumenOBJ> lst = lstRef;

			/* COMIENZA A DAR DE ALTA LA CUENTA REFERENCIADA */
			List<Map<String, String>> listMapResultados = new ArrayList<>(); 
			UUID control = UUID.randomUUID();
			Integer consecutivo = 0;
			for(CuentaReferenciadaVolumenOBJ obj : lst){
				obj.setControl(control.toString());
				obj.setConsecutivo(++consecutivo);
				
				CuentaOBJ cta = new CuentaOBJ();
				String relacion = String.format("%s-%s", obj.getCuenta_concentradora(), obj.getCuenta_referencia());
				String cuentaConcentradora = Comun._TX(obj.getCuenta_concentradora());
				String cuentaReferencia =  Comun._TX(obj.getCuenta_referencia());	
				String obs = null;
				
				obj.setTipoCuenta(obj.getTipoCuenta() == null ? "REFERENCIADA" : obj.getTipoCuenta());
				obj.setAccion(obj.getAccion() == null ? "AGREGAR" : obj.getAccion());
				obj.setValor(obj.getValor() == null ? null : obj.getValor());
			
				RespuestaSVC respValida = valCuentas.validacionesCuenta(obj);
				String obsValida = Comun._T(respValida.getBody().getValor("ESTATUS"));
				if(obsValida.equals("OK")){
					cta = (CuentaOBJ)respValida.getBody().getValor("CUENTA");
			
					Map<String,String> mapPersonas = valCuentas.validaDatosPersona(obj);
					String status = Comun._T(mapPersonas.get("ESTATUS"));
					if(status.equals("OK")){
										
						String solId = Comun._T(mapPersonas.get("SOLICITANTE_ID"));
						String repLegalId = Comun._T(mapPersonas.get("REPLEGAL_ID"));
						
						/* REALIZA EL ALTA DE LA CUENTA REFERENCIADA */
						CuentaReferenciadaOBJ objRef = new CuentaReferenciadaOBJ();
						objRef.setCuenta_id(cta.getId());
						objRef.setCuenta_referencia(obj.getCuenta_referencia());
						objRef.setNombre_referencia(obj.getNombre_referencia());
						objRef.setRfc_referencia(obj.getRfc_referencia());
						objRef.setCurp_referencia(obj.getCurp_referencia());
						objRef.setCorreo_referencia(obj.getCorreo_referencia());
						objRef.setTelefono_referencia(obj.getTelefono_referencia());
						objRef.setObservaciones(obj.getObservaciones());
						objRef.setControl(obj.getControl());
						objRef.setTipo_cuenta(obj.getTipoCuenta());
						objRef.setValor(obj.getValor());
						
						objRef.setPersona_id("");
						objRef.setTipo_cuenta_nivel(Comun._T(mapPersonas.get("TIPO_CUENTA_NIVEL")));
						objRef.setUnidad_negocio(Comun._T(mapPersonas.get("UNIDAD_NEGOCIO")));
						
						RespuestaSVC altaReferenciada = daoRef.insertarCuentaReferenciadaStdDao(objRef);
						if(altaReferenciada.getErrores().getCodigoError() == 0L){
							RespuestaSVC respIzelInsertar = daoRef.insertarIzelCuentaClabeStdDao(obj.getCuenta_referencia(), Comun._T(Constantes.APLICACION_IZEL_ID));
							if(respIzelInsertar.getErrores().getCodigoError() == 0L){
								
								if(solId.equals("")	|| (obj.getSolicitante().getTipo_persona_cuenta().equals("M") && repLegalId.equals(""))){
									RespuestaSVC respDatosCuenta = daoRef.insertarDatosSolcitanteCuentaReferenciadaStdDao(obj);
									if(respDatosCuenta.getErrores().getCodigoError() == 0L){
										
									}else{
										obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, obj.getCuenta_referencia());
										valCuentas.rollBackCuenta(obj.getCuenta_referencia());
									}
								}else{
									RespuestaSVC updateSol = daoRef.actualizarCuentaReferenciadaSolicitanteStdDao(cuentaReferencia, solId);
									if(updateSol.getErrores().getCodigoError()!=0L){
										obs = updateSol.getErrores().getDescError();	
									}
									
	
									if(obj.getSolicitante().getTipo_persona_cuenta().equals("M")){
										RespuestaSVC insertRep = daoRef.insertarCuentaReferenciadaRepresentanteStdDao(cuentaReferencia, solId, repLegalId, 1);
										if(insertRep.getErrores().getCodigoError()!=0L){
											obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, obj.getCuenta_referencia());	
										}
									}
								}
							}else{
								obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, obj.getCuenta_referencia());
								valCuentas.rollBackCuenta(obj.getCuenta_referencia());
							}
						}else{
							obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, obj.getCuenta_referencia());
						}
					}else{
						obs = status;
					}
					
				}else{
					obs = obsValida;
				}
				

				Map<String, String> map = new HashMap<>();

				map.put("CUENTA_CONCENTRADORA", cuentaConcentradora);
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



}
