package net.std.cuentas.svc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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

import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
//import net.std.constantes.Respuesta;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.servicios.ProcesoBitLogger;
import net.std.data.ClabeIzelOBJ;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.data.CuentaReferenciadaVolumenOBJ;
import net.std.productos.dao.CatalogosProdAhorroCeroStdDAO;
import net.std.request.SolicitanteReq;

@SuppressWarnings("unused")
@Controller
public class CrearCuentaReferenciadaStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CrearCuentaReferenciadaStdSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static CuentasReferenciadasStdDAO daoRef = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/crearCuentaReferenciadaStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<CuentaReferenciadaVolumenOBJ>lstRef = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try{
			if(dao == null || daoRef == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoRef == null){
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
			/*RespuestaSVC respGuardarMasiva = guardarCuentasReferenciadasMasivas(lstRef);
			if(respGuardarMasiva.getErrores().getCodigoError() != 0L){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA_MASIVA, respGuardarMasiva.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response; 
			}

			RespuestaSVC respLog = ProcesoBitLogger.procesar(Constantes.PROCESO, "ALTA CUENTAS REFERENCIADAS", json);

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
			for(CuentaReferenciadaVolumenOBJ obj : lst){
				obj.setControl(control.toString());
				String relacion = String.format("%s-%s", obj.getCuenta_concentradora(), obj.getCuenta_referencia());
				String cuentaConcentradora = Comun._TX(obj.getCuenta_concentradora());
				String cuentaReferencia =  Comun._TX(obj.getCuenta_referencia());		
				String obs = null;

				obj.setTipoCuenta(obj.getTipoCuenta() == null ? "REFERENCIADA" : obj.getTipoCuenta());
				obj.setAccion(obj.getAccion() == null ? "AGREGAR" : obj.getAccion());
				obj.setValor(obj.getValor() == null ? null : obj.getValor());
				
				
				/*VERIFICA QUE E	L TIPO DE CUENTA EXISTA EN EL CATALOGO DE TIPOCUENTAS DE AHORRO*/
				/*if(!("ELIMINAR".equals(obj.getAccion()) || "AGREGAR".equals(obj.getAccion()))){
					obs = String.format("[ELIMINAR | AGREGAR ] %s]", Errores.desc(Errores.ERROR_ACCION_NO_DEFINIDA, obj.getAccion()));
				}else{*/

					/* PRIMERO VERIFICA QUE LA CUENTA CONCENTRADORA EXISTA */
					RespuestaSVC respConc = dao.leerCuentaAhorroClabeDao(obj.getCuenta_concentradora());
					if(respConc.getErrores().getCodigoError() != 0L){
						obs = Comun._T(respConc.getErrores().getDescError());
					}else{
						CuentaOBJ cta = (CuentaOBJ)respConc.getBody().getValor("CUENTA");
						if(cta == null){
							obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA);
						}else{
							/* VERIFICA QUE LA CUENTA ESTE VIGENTE Y NO CANCELADA */
							if(Comun._T(Constantes.ESTATUS_CUENTA_BLOQUEDA).equals(cta.getBloqueado())){
								obs = Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVA);
							}else{
								/* VERIFICA QUE LA CUENTA REFERENCIADA NO EXISTA */
								RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(obj.getCuenta_referencia(), obj.getTipoCuenta());
								if(respRef.getErrores().getCodigoError() == 0L){
									obs = Errores.desc(Errores.ERROR_CUENTA_REFERENCIADA_EXISTE, obj.getCuenta_referencia());
								}else{
									/* VERIFICA QUE LA CUENTA EN IZEL NO EXISTA */
									RespuestaSVC respIzel = daoRef.leerIzelCuentaClabeStdDao(obj.getCuenta_referencia());
									if(respIzel.getErrores().getCodigoError() == 0L){
										obs = Errores.desc(Errores.ERROR_CUENTA_IZEL_REFERENCIADA_EXISTE, obj.getCuenta_referencia());
									}else{
										/* VERIFCA DATOS AUSENTES DE RFC / CURP, INSERTA GENERICOS */
										/* SI EL RFC ES BLANCO , COLOCA EL RFC GENERICO */
										if("".equals(Comun._T(obj.getRfc_referencia()))) obj.setRfc_referencia("AAAA010203BB1");

										//if(validarRFC(obj.getRfc_referencia()) || validarCURP(obj.getRfc_referencia())){     // VALIDA EL RFC YA SEA INCLUYENTE DE CURP / RFC
										if(validarRFCMinimo(obj.getRfc_referencia())){     // VALIDA EL RFC YA SEA INCLUYENTE DE CURP / RFC

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

											RespuestaSVC altaReferenciada = daoRef.insertarCuentaReferenciadaStdDao(objRef);
											if(altaReferenciada.getErrores().getCodigoError() != 0L){
												obs = Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, obj.getCuenta_referencia());
											}else{
												RespuestaSVC respIzelInsertar = daoRef.insertarIzelCuentaClabeStdDao(obj.getCuenta_referencia(), Comun._T(Constantes.APLICACION_IZEL_ID));
												if(respIzelInsertar.getErrores().getCodigoError() != 0L){
													obs = Errores.desc(Errores.ERROR_ALTA_CUENTA_REFERENCIADA_INCOMPLETA, obj.getCuenta_referencia());
												}
											}
										}else{
											obs = String.format("FORMATO RFC/CURP INVALIDO , %s | %s | %s", Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA), obj.getCuenta_referencia(), obj.getRfc_referencia());
										}
									}
								}
							}
						}
					}
				//}

				Map<String, String> map = new HashMap<>();

				//map.put("RELACION_CUENTA", relacion);
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

	private RespuestaSVC guardarCuentasReferenciadasMasivas(List<CuentaReferenciadaVolumenOBJ> lst){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		UUID control = UUID.randomUUID();
		if(lst != null){
			respuestaSvc = daoRef.eliminarCuentaReferenciadaMasivaStdDao();
			if(respuestaSvc.getErrores().getCodigoError() != 0L){
				return respuestaSvc;
			}

			Integer consecutivo = 0;
			for(CuentaReferenciadaVolumenOBJ obj : lst){
				obj.setControl(control.toString());
				obj.setConsecutivo(++consecutivo);
				
				obj.setTipoCuenta(obj.getTipoCuenta() == null ? "REFERENCIADA" : obj.getTipoCuenta());
				obj.setAccion(obj.getAccion() == null ? "AGREGAR" : obj.getAccion());
				obj.setValor(obj.getValor() == null ? null : obj.getValor());

				respuestaSvc = daoRef.insertarCuentaReferenciadaMasivaStdDao(obj); 
				if(respuestaSvc.getErrores().getCodigoError() != 0L){
					return respuestaSvc;
				}
			}
			respuestaSvc.getBody().addValor("CONTROL", control.toString());
		}

		return respuestaSvc;
	}

	private String validaParams(CuentaReferenciadaVolumenOBJ obj){
		String valida = null;
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if("".equals(Comun._T(obj.getCuenta_concentradora())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CUENTA CONCENTRADORA");
		if("".equals(Comun._T(obj.getCuenta_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CUENTA REFERENCIADA");
		if("".equals(Comun._T(obj.getNombre_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NOMBRE REFERENCIADO");
		if("".equals(Comun._T(obj.getTelefono_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "TELEFONO");
		if("".equals(Comun._T(obj.getCorreo_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CORREO");
		if("".equals(Comun._T(obj.getRfc_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "RFC");
		if("".equals(Comun._T(obj.getCurp_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CURP");

		if(!"".equals(Comun._T(obj.getCurp_referencia()))){
			if(!validarCURP(Comun._T(obj.getCurp_referencia()))){
				return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CURP");
			}
		}

		if(!"".equals(Comun._T(obj.getRfc_referencia()))){
			if(!validarRFC(Comun._T(obj.getRfc_referencia()))){
				return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "RFC");
			}
		}

		return valida;
	}

	private boolean validarCURP(String curp){
		String regex = 
				"[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
						"(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
						"[HM]{1}" +
						"(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
						"[B-DF-HJ-NP-TV-Z]{3}" +
						"[0-9A-Z]{1}[0-9]{1}$";

		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(curp).matches()){
			return false;
		}else{
			return true;
		}
	}

	private boolean validarRFC(String rfc){
		String regex = "^([A-Z&Ññ]{3}|[A-Z][AEIOU][A-Z]{2})\\d{2}((01|03|05|07|08|10|12)(0[1-9]|[12]\\d|3[01])|02(0[1-9]|[12]\\d)|(04|06|09|11)(0[1-9]|[12]\\d|30))([A-Z0-9]{2}[0-9A])?$";

		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(rfc).matches()){
			return false;
		}else{
			return true;
		}
	}


	private  static boolean validarRFCMinimo(String rfc){
		if(rfc == null || rfc.length() == 0) return false;
		if(!(rfc.length() == 18 || rfc.length() == 13 || rfc.length() == 12|| rfc.length() == 10 || rfc.length() == 9)){
			return false;
		}
		
		//String cadena = rfc.length() == 18 || rfc.length() == 13 || rfc.length() == 12|| rfc.length() == 10 ? rfc.substring(0,10) : rfc.substring(0,9);
		String cadena = "";
		
		if( rfc.length() == 18 || rfc.length() == 13 || rfc.length() == 10){
			cadena = rfc.substring(0,10);
		}else if(rfc.length() == 12 || rfc.length() == 9){
			cadena = rfc.substring(0,9);
		}else{
			return false;
		}
			
		//String cadena = rfc.length() == 18 || rfc.length() == 13 || rfc.length() == 10 ? rfc.substring(0,10) : rfc.substring(0,9);
		String regex = "^([A-ZÑ\\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))$";

		Pattern patron = Pattern.compile(regex);
		if(!patron.matcher(cadena).matches()){
			return false;
		}else{
			return true;
		}
	}


}
