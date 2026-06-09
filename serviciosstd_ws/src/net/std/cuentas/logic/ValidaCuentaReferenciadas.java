package net.std.cuentas.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CatalogoProcreaStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaVolumenOBJ;
import net.std.data.PersonaOBJ;
import net.std.expediente.dao.ExpedienteStdDAO;
import net.std.request.AltaDocumentoReq;
import net.std.sftp.SFTPLogic;

public class ValidaCuentaReferenciadas implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ValidaCuentaReferenciadas.class);
	
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static SolicitanteStdDAO daoSol = null;
	private static ExpedienteStdDAO daoExp = null;
	private static CatalogoProcreaStdDAO daoCat = null;
	private static CuentasReferenciadasStdDAO daoRef = null;

	private static Double montoCTA_N1_min = null;
	private static Double montoCTA_N1_max = null;
	private static Double montoCTA_N2_min = null;
	private static Double montoCTA_N2_max = null;
	private static Double montoCTA_N3_min = null;
	private static Double montoCTA_N3_max = null;
	private static Double montoCTA_N4_min = null;
	private static Double montoCTA_N4_max = null;

	public ValidaCuentaReferenciadas() {
		if(dao == null || daoExp == null || daoSol == null || daoCat == null || daoRef == null){
			initialized();
		}
	}
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoSol = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoExp = (ExpedienteStdDAO)s.getApplicationContext().getBean("ExpedienteStdDAO");
			daoCat = (CatalogoProcreaStdDAO)s.getApplicationContext().getBean("CatalogoProcreaStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String inicializaValores(){
		String obs = "OK";
				
		RespuestaSVC res = daoRef.listarTiposCuentasAhorroStdDao();	
		if(res.getErrores().getCodigoError() != 0){
			obs = res.getErrores().getDescError();
			log.error(obs);
			return obs;			
		}
		
		List<Map<String, Object>> lista = (List<Map<String, Object>>) res.getBody().getValor("LISTA");
		for (Map<String, Object> map : lista) {
			switch (Comun._T(map.get("CLAVE"))) {
			case "CTA_N1":
				montoCTA_N1_min = Comun._D(map.get("monto_min"));
				montoCTA_N1_max = Comun._D(map.get("monto_max"));
				break;
			case "CTA_N2":
				montoCTA_N2_min = Comun._D(map.get("monto_min"));
				montoCTA_N2_max = Comun._D(map.get("monto_max"));				
				break;
			case "CTA_N3":
				montoCTA_N3_min = Comun._D(map.get("monto_min"));
				montoCTA_N3_max = Comun._D(map.get("monto_max"));				
				break;
			case "CTA_N4":
				montoCTA_N4_min = Comun._D(map.get("monto_min"));
				montoCTA_N4_max = Comun._D(map.get("monto_max"));				
				break;
			}
		}
		if(montoCTA_N1_min == null || montoCTA_N1_max == null || montoCTA_N2_min == null || montoCTA_N2_max == null || 
				montoCTA_N3_min == null || montoCTA_N3_max == null || montoCTA_N4_min == null || montoCTA_N4_max == null)
			obs = "ERROR: AL OBTENER MONTOS DE TIPOS DE CUENTA DE AHORRO";
		return obs;
	}


	public RespuestaSVC validacionesCuenta(CuentaReferenciadaVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		CuentaOBJ cta = null;
		String obs = "OK";
	
		obs = validaParams(obj);
		if(obs.equals("OK")){
			/* PRIMERO VERIFICA QUE LA CUENTA CONCENTRADORA EXISTA */
			RespuestaSVC respConc = dao.leerCuentaAhorroClabeDao(obj.getCuenta_concentradora());
			if(respConc.getErrores().getCodigoError() != 0L){
				obs = Comun._T(respConc.getErrores().getDescError());
			}else{
				cta = (CuentaOBJ)respConc.getBody().getValor("CUENTA");
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
								if(!validarRFCMinimo(obj.getRfc_referencia())){     // VALIDA EL RFC YA SEA INCLUYENTE DE CURP / RFC
									obs = String.format("FORMATO RFC/CURP INVALIDO , %s | %s | %s", Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA), obj.getCuenta_referencia(), obj.getRfc_referencia());
								}
							}
						}
					}
				}
			}
		}
		respuesta.getBody().addValor("ESTATUS", obs);
		respuesta.getBody().addValor("CUENTA", cta);
		
		return respuesta;
	}


	
	public RespuestaSVC guardarCuentasReferenciadasMasivas(List<CuentaReferenciadaVolumenOBJ> lst){
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
				respuestaSvc = daoRef.insertarDatosSolcitanteCuentaReferenciadaStdDao(obj); 
				if(respuestaSvc.getErrores().getCodigoError() != 0L){
					return respuestaSvc;
				}
			}
			respuestaSvc.getBody().addValor("CONTROL", control.toString());
		}

		return respuestaSvc;
	}
	public Map<String,String> validaDatosPersona(CuentaReferenciadaVolumenOBJ obj) {
		Map<String,String> mapaReturn = new HashMap<>();
		String obs = "";
		String unidad_negocio = obj.getSolicitante().getUnidad_negocio_cuenta();
		String nivel_cuenta = obj.getSolicitante().getNivel_cuenta();
		String solID = "";
		String repLegalId = "";
		
		String obsMontos = inicializaValores();
		if(obsMontos.equals("OK")){
			
			obs = validaNivelCuenta(nivel_cuenta);
			if(obs.equals("OK")){
				Map<String,String> respPersona = validaCreacionPersona(obj);
				obs = respPersona.get("ESTATUS");
				solID = respPersona.get("SOLICITANTE_ID");
				repLegalId = respPersona.get("REPLEGAL_ID");
			}
		}
		
		mapaReturn.put("REPLEGAL_ID", repLegalId);
		mapaReturn.put("SOLICITANTE_ID", solID);
		mapaReturn.put("TIPO_CUENTA_NIVEL", Comun._T(nivel_cuenta));
		mapaReturn.put("UNIDAD_NEGOCIO", Comun._T(unidad_negocio));
		mapaReturn.put("ESTATUS", obs);
		return mapaReturn;
	}

	private String validaNivelCuenta(String nivel_cuenta) {
		if(!(Constantes.TIPO_CUENTA_AHO_N1.equals(nivel_cuenta) || Constantes.TIPO_CUENTA_AHO_N2.equals(nivel_cuenta) 
				|| Constantes.TIPO_CUENTA_AHO_N3.equals(nivel_cuenta) || Constantes.TIPO_CUENTA_AHO_N4.equals(nivel_cuenta)))
			return String.format("ERROR :: TIPO DE CUENTA INVALIDA - %s", nivel_cuenta); 
		return "OK";
	}


	private Map<String, String> validaCreacionPersona(CuentaReferenciadaVolumenOBJ obj) {
		String obs = "OK";
		String solID = "";
		String repLegalId = "";
		Map<String,String> respuesta = new HashMap<String, String>();
		String tipo_persona = Comun._T(obj.getSolicitante().getTipo_persona_cuenta());
		
		if(tipo_persona.equals("F")){
	
			String year = obj.getSolicitante().getCurp_cuenta().substring(4, 6);
			if(Integer.valueOf(year) > 20){
				year = "19"+year;
			}else{
				year = "20"+year;
			}
			String fechaNacimientoStr = year
					+ obj.getSolicitante().getCurp_cuenta().substring(6, 8)
					+ obj.getSolicitante().getCurp_cuenta().substring(8, 10);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate fechaNac = LocalDate.parse(fechaNacimientoStr, fmt);
			LocalDate ahora = LocalDate.now();
			Period periodo = Period.between(fechaNac, ahora);
			if (periodo.getYears() < 18) {
				obs = "ERROR :: SOLICITANTE MENOR DE EDAD";
				respuesta.put("ESTATUS", obs);
				return respuesta;
			}
		}
		
		if(tipo_persona.equals("M")){ 
			
			String year = obj.getRepLegal().getCurp_cuenta().substring(4, 6);
			if(Integer.valueOf(year) > 20){
				year = "19"+year;
			}else{
				year = "20"+year;
			}
			String fechaNacimientoStr = year
					+ obj.getRepLegal().getCurp_cuenta().substring(6, 8)
					+ obj.getRepLegal().getCurp_cuenta().substring(8, 10);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate fechaNac = LocalDate.parse(fechaNacimientoStr, fmt);
			LocalDate ahora = LocalDate.now();
			Period periodo = Period.between(fechaNac, ahora);
			if (periodo.getYears() < 18) {
				obs = "ERROR :: REPRESENTANTE LEGAL MENOR DE EDAD";
				respuesta.put("ESTATUS", obs);
				return respuesta;
			}
			
			/*	SI ES PERSONA MORAL VALIDAR REP LEGAL */
			RespuestaSVC existeRepLegal = daoSol.BuscarSolicitanteByTipoDao(obj.getRepLegal().getCurp_cuenta(), obj.getRepLegal().getRfc_cuenta(), "F");
			if(existeRepLegal.getErrores().getCodigoError() == 0){ 
				/* EXISTE SOLICITANTE */
				if(Comun._T(existeRepLegal.getBody().getValor("bloqueado")).equals("1")){ 
					/* SOLICITANTE BLOQUEADO */
					obs = Errores.desc(Errores.ERROR_SOLICITANTE_BLOQUEADO);
					respuesta.put("ESTATUS", obs);
					return respuesta;
				}else{
					/* SOLICITANTE OK */
					repLegalId = Comun._T(existeRepLegal.getBody().getValor("CLIENTE_ID"));
				}
			}
		}
		
		RespuestaSVC existePersona = daoSol.BuscarSolicitanteByTipoDao(obj.getSolicitante().getCurp_cuenta(), obj.getSolicitante().getRfc_cuenta(), tipo_persona);		
		if(existePersona.getErrores().getCodigoError() == 0){ 
			/* EXISTE SOLICITANTE */
			if(Comun._T(existePersona.getBody().getValor("bloqueado")).equals("1")){ 
				/* SOLICITANTE BLOQUEADO */
				obs = Errores.desc(Errores.ERROR_SOLICITANTE_BLOQUEADO);
				respuesta.put("ESTATUS", obs);
				return respuesta;
			}else{
				/* SOLICITANTE OK */
				solID = Comun._T(existePersona.getBody().getValor("CLIENTE_ID"));
			}
		}

		respuesta.put("SOLICITANTE_ID", solID);
		respuesta.put("REPLEGAL_ID", repLegalId);
		respuesta.put("ESTATUS", obs);
		return respuesta;
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


	private String validaParams(CuentaReferenciadaVolumenOBJ obj){
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if("".equals(Comun._T(obj.getCuenta_concentradora())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CUENTA CONCENTRADORA");
		if("".equals(Comun._T(obj.getCuenta_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CUENTA REFERENCIADA");
		if("".equals(Comun._T(obj.getNombre_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NOMBRE REFERENCIADO");
		/*if("".equals(Comun._T(obj.getRfc_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "RFC");
		if("".equals(Comun._T(obj.getTelefono_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "TELEFONO");
		if("".equals(Comun._T(obj.getCorreo_referencia())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CORREO");
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
		}*/

		return "OK";
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
	
	public void rollBackCuenta(String cuentaReferencia){
		daoRef.eliminarCuentaReferenciadaCeroStdDao(cuentaReferencia);
		daoRef.eliminarCuentaReferenciadaIzelStdDao(cuentaReferencia);
	}
}
