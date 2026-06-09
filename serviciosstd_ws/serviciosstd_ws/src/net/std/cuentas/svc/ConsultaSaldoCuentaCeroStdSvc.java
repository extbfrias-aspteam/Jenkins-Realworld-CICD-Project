package net.std.cuentas.svc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroProcreaStdDAO;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.request.TransaccionCuentasReq;
import net.std.soap.servicios.ProcesoDepositarPlastico;


@Controller
public class ConsultaSaldoCuentaCeroStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ConsultaSaldoCuentaCeroStdSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	private static final String _FECHA_AUT_ = "yyyyMMddHHmmssSSS";
	private static final String _MOVTO_ = "DEPOSITO";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;
	private static AhorroProcreaStdDAO daoPro = null;
	private static SolicitanteStdDAO daoSol = null;
	private static CuentasReferenciadasStdDAO daoRef = null; 


	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoPro = (AhorroProcreaStdDAO)s.getApplicationContext().getBean("AhorroProcreaStdDAO");
			daoSol = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/consultaSaldoCuentaCeroExtStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Map<String, String> map = new HashMap<>();
		String autorizacion = null;
		HeaderWS header;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try{
			if(dao == null || daoAho == null || daoSol == null || daoRef == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoAho == null || daoSol == null || daoRef == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}

			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			String valida = validaParams(map);
			if(valida != null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida), HttpStatus.FORBIDDEN);
				return response;
			}
		}catch(Exception ex){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			
			/* VERIFICA LA CUENTA Y LA VALIDES DE LA CUENTA ORDENANTE (DEPOSITOS), BENEFICIARIA (RETIROS) */
			
			CuentaOBJ ctaDeposito = null;
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(map.get("cuentaID")));
			if(respCtaDep.getErrores().getCodigoError() == 0){
				/* ENCONTRO LA CUENTA COMO CUENTA CONCENTRADORA */
				ctaDeposito = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
				ctaDeposito.setCuenta_referencia(map.get("cuentaID"));
			}else{
				/* BUSCA COMO REFERENCIA */
				RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(map.get("cuentaID")), Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA));
				if(respRef.getErrores().getCodigoError() != 0){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA, 
							       					String.format("%s - %s", Comun._TX(map.get("cuentaID")) ,Comun._TX(map.get("cuentaID")))), HttpStatus.FORBIDDEN);
					return response;	
				}
				
				CuentaReferenciadaOBJ obj = (CuentaReferenciadaOBJ)respRef.getBody().getValor("CUENTA");
				RespuestaSVC respRefer = daoAho.leerCuentaAhorroClabeDao(Comun._TX(obj.getClabe_interbancaria()));
				if(respRefer.getErrores().getCodigoError() != 0){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA, 
	       						String.format("%s - %s", Comun._TX(map.get("cuentaID")) ,Comun._TX(map.get("cuentaID")))), HttpStatus.FORBIDDEN);
					return response;	
				}
				ctaDeposito = (CuentaOBJ) respRefer.getBody().getValor("CUENTA");
				if(ctaDeposito != null){
					ctaDeposito.setCuenta_referencia(obj.getCuenta_referencia());
				}
			}	
				
			/* VERIFICA QUE LA CUENTA NO ESTE VIGENTE */
			if(!"VIG".equals(Comun._T(ctaDeposito.getEstatusClave()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", map.get("cuentaID"), Comun._T(ctaDeposito.getEstatus()))), HttpStatus.FORBIDDEN);
				return response;
			}

			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
			if("BLOQUEADO".equals(Comun._T(ctaDeposito.getBloqueado()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, map.get("cuentaID")), HttpStatus.FORBIDDEN);
				return response;
			}
			
			String saldo = "";
			if(map.get("fecha").equals("")){
				saldo = dao.leerSaldoCuentaStdDao(ctaDeposito.getId()).getBody().getValor("SALDO").toString();
			}else{
				saldo = dao.leerSaldoByFechaStdDao(ctaDeposito.getId(), map.get("fecha")).toString();
			}
			mapResultado.put("SALDO", saldo);
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}


	private String validaParams(Map<String, String> obj){
		String valida = null;
		if(obj == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(obj.get("cuentaID").equals("")) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATOS DE LA CUENTA");
		return valida;
	}

	private String getAutorizacion(String identificador){
		SimpleDateFormat sdf = new SimpleDateFormat(_FECHA_AUT_);
		int random = (int )(Math.random() * 50 + 1);
		String autorizacion = String.format("%s-%s-%s", identificador, Comun._T(random), sdf.format(Calendar.getInstance().getTime()));
		return autorizacion;
	}
	
	@SuppressWarnings("unused")
	private static RespuestaSVC enviaDepositoLocal(CuentaOBJ cta, TransaccionCuentasReq trx, HeaderWS header, String autorizacion, RespuestaSVC respSol, Map<String, Object> respBanco){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		try{
			//RespuestaSVC respDeposito = dao.depositarStdDao(cta, "DEP_TRANS", new Date(), trx.getMonto(), 
			//		String.format("DEPOSITO %s -%s", Comun._T(cta.getTipoCliente()), Comun._T(trx.getConcepto())), autorizacion, header, trx.getClaveRastreo());
			RespuestaSVC respDeposito = dao.depositarStdDao(cta, "DEP_TRANS", new Date(), trx.getMonto(), 
					String.format("PAGO RECIBIDO DE %s POR ORDEN DE %s CTA. ORDENANTE %s REF. %s CLAVE DE RASTREO: %s", 
							Comun._T(respBanco.get("NOMBRE")),
							Comun._T(respSol.getBody().getValor("NOMBRE")), 
							Comun._T(trx.getCuentaClabe()), 
							Comun._T(cta.getReferencia()),
							Comun._T(trx.getClaveRastreo())), 
					autorizacion, header, trx.getClaveRastreo());
			
			if(respDeposito.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO, (Errores.desc(Errores.ERROR_DEPOSITO,
						String.format("%s - ", cta.getClabeInterbancaria(), respDeposito.getErrores().getDescError()))));
				return respuestaSvc;
			}
			
			RespuestaSVC resActSaldo = dao.actualizaSaldoStdDao(cta, trx.getActualizaSaldo(), _MOVTO_,	Comun._I(Comun._T(header.getIdUsuario())));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		return respuestaSvc;
	}
}

