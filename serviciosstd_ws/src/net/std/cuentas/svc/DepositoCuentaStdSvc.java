package net.std.cuentas.svc;

import java.io.Serializable;
import java.math.BigDecimal;
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

import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.request.CanalesReq;
import net.std.request.TransaccionCuentasReq;


@Controller
public class DepositoCuentaStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DepositoCuentaStdSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	private static final String _FECHA_AUT_ = "yyyyMMddHHmmssSSS";
	private static final String _MOVTO_ = "DEPOSITO";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;
	

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value="/depositoCuentaExtStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		TransaccionCuentasReq trans = null;
		String autorizacion = null;
		HeaderWS header;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null || daoAho == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoAho == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			trans = new Gson().fromJson(json, TransaccionCuentasReq.class);
			String valida = validaParams(trans);
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
		
		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEPOSITO_AHORRO), "TRX_DEPOSITO_AHORRO: " + Comun._T(trans.getCuentaClabe()))){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			
			/* LLENA EL HEADER */
			header = new HeaderWS();
			header.setIdCanalAtencion(Comun._L(Constantes.CANAL_ID));
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			header.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
			header.setIpHost(Comun._T(Constantes.HOST_ID));
			
			/* OBTIENE LOS DATOS COMPLETOS DE LA CUENTA */
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(trans.getCuentaClabe()));
			if(respCtaDep.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}
			
			CuentaOBJ ctaEje = null;
			CuentaOBJ ctaDeposito = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
			
			/* VERIFICA QUE LA CUENTA NO ESTE VIGENTE */
			if(!"VIG".equals(Comun._T(ctaDeposito.getEstatusClave()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", trans.getCuentaClabe(), Comun._T(ctaDeposito.getEstatus()))), HttpStatus.FORBIDDEN);
				return response;
			}
			
			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
			if("BLOQUEADO".equals(Comun._T(ctaDeposito.getBloqueado()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}
			
			/* VERIFICA QUE LA CUENTA NO TENGA BLOQUEO POR PARTE DEL PARTICIPANTE */
			RespuestaSVC respPermiso = dao.leerCanalStdDao(ctaDeposito.getCuenta(), 
					                                       "CUENTA_FINAL".equals(ctaDeposito.getTipoCliente()) ? Comun._T(Constantes.APLICATIVO_BLU_FINAL) :  Comun._T(Constantes.APLICATIVO_BLU), 
					                                       Comun._T(Constantes.TRX_CUENTA_BLU)); 
			if(respPermiso.getErrores().getCodigoError() == 0){
				CanalesReq req = (CanalesReq)respPermiso.getBody().getValor("CANAL");
				if(req.getStatus()){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA_POR_CANAL, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
					return response;
				}
			}
			
			/* OBTIENE UN NUMERO DE AUTORIZACION */
			autorizacion = getAutorizacion(trans.getIdentificador());
			if("".equals(Comun._T(autorizacion))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_NUMERO_AUTORIZACION, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}
			
			if("CUENTA_FINAL".equals(ctaDeposito.getTipoCliente())){
				
				/* VERIFICA QUE LA CUENTA CLABE EJE EXISTA EN LA CUENTA FINAL */
				if("".equals(Comun._T(ctaDeposito.getClabeEje()))){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_SIN_CUENTA_PADRE, ctaDeposito.getClabeInterbancaria()), HttpStatus.FORBIDDEN);
					return response;
				}
				
				/* OBTIENE LOS DATOS DE LA CUENTA EJE */
				RespuestaSVC respCtaEje = daoAho.leerCuentaAhorroClabeDao(Comun._TX(ctaDeposito.getClabeEje()));
				if(respCtaEje.getErrores().getCodigoError() != 0){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_PARTICIPANTE_ASOCIADO, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
				ctaEje = (CuentaOBJ) respCtaEje.getBody().getValor("CUENTA");
				
				/* VERIFICA QUE LA CUENTA ESTE VIGENTE */
				if(!"VIG".equals(Comun._T(ctaEje.getEstatusClave()))){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", ctaEje.getClabeInterbancaria(), Comun._T(ctaEje.getEstatus()))), HttpStatus.FORBIDDEN);
					return response;
				}
				
				/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
				if("BLOQUEADO".equals(Comun._T(ctaEje.getBloqueado()))){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_PADRE_BLOQUEADA, String.format("CUENTA INTERBANCARIA : %s | CUENTA DEPENDIENTE : %s",
                            trans.getCuentaClabe(), ctaEje.getClabeInterbancaria())), HttpStatus.FORBIDDEN);
					return response;
				}
				
				/* VERIFICA QUE LA CUENTA NO TENGA BLOQUEO POR PARTE DEL PARTICIPANTE */
				RespuestaSVC respPermisoEje = dao.leerCanalStdDao(ctaEje.getCuenta(), 
						                                       Comun._T(Constantes.APLICATIVO_BLU), 
						                                       Comun._T(Constantes.TRX_CUENTA_BLU)); 
				if(respPermisoEje.getErrores().getCodigoError() == 0){
					CanalesReq req = (CanalesReq)respPermisoEje.getBody().getValor("CANAL");
					if(req.getStatus()){
						response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA_POR_CANAL_DEPENDIENTE, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
						return response;
					}
				}

				
				/* DEPOSITA EL MONTO PRIMERO EN EL CLIENTE FINAL */
				RespuestaSVC respDeposito = dao.depositarStdDao(ctaDeposito, "DEP_TRANS", new Date(), trans.getMonto(), 
						                                        String.format("DEPOSITO %s - %s", Comun._T(ctaDeposito.getTipoCliente()), Comun._T(trans.getConcepto())), autorizacion, header, trans.getClaveRastreo());
				if(respDeposito.getErrores().getCodigoError() != 0){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
				
				/* COMPRUEBA EL SALDO DE LA CUENTA DE DEPOSITO */
				RespuestaSVC respSaldo = dao.leerSaldoCuentaStdDao(ctaDeposito.getId());
				if(respSaldo.getErrores().getCodigoError() != 0){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SALDO_NO_ENCONTRADO, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
				
				BigDecimal saldo = (BigDecimal)(respSaldo.getBody().getValor("SALDO"));
				saldo = saldo.add(trans.getMonto());
				
				if(saldo.doubleValue() < trans.getMonto().doubleValue()){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SALDO_INSUFICIENTE, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
						
				/* RETIRA EL MONTO EN LA CUENTA FINAL */
				RespuestaSVC respRetiro = dao.retirarStdDao(ctaDeposito, "RET_TRANS", new Date(), trans.getMonto(),
						                                    String.format("RETIRO %s - %s", Comun._T(ctaDeposito.getTipoCliente()), Comun._T(trans.getConcepto())), autorizacion, header, trans.getClaveRastreo());
				if(respRetiro.getErrores().getCodigoError() != 0){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_RETIRO, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
				
				/* DEPOSITA EL MONTO EN LA CUENTA PARTICIPANTE (eje) */
				RespuestaSVC respDepositoEje = dao.depositarStdDao(ctaEje, "DEP_TRANS", new Date(), trans.getMonto(), 
						                                           String.format("DEPOSITO %s - %s", Comun._T(ctaEje.getTipoCliente()), Comun._T(trans.getConcepto())), autorizacion, header, trans.getClaveRastreo());
				if(respDepositoEje.getErrores().getCodigoError() != 0){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, ctaEje.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}

			/* }else if("CUENTA_BLU".equals(Comun._T(ctaDeposito.getTipoCliente()))){ */
			}else{
			
				/* DEPOSITA EL MONTO PRIMERO EN EL CLIENTE FINAL */
				RespuestaSVC respDeposito = dao.depositarStdDao(ctaDeposito, "DEP_TRANS", new Date(), trans.getMonto(), 
						                                        String.format("DEPOSITO %s -%s", Comun._T(ctaDeposito.getTipoCliente()), Comun._T(trans.getConcepto())), autorizacion, header, trans.getClaveRastreo());
				if(respDeposito.getErrores().getCodigoError() != 0){
					RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, ctaDeposito.getClabeEje()), HttpStatus.FORBIDDEN);
					return response;
				}
			}
			/*
			}else{
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}
			*/
			
			/* ACTUALIZA EL SALDO */
			if("CUENTA_FINAL".equals(ctaDeposito.getTipoCliente())){
				RespuestaSVC resActSaldoEje = dao.actualizaSaldoStdDao(ctaEje, trans.getActualizaSaldo(), _MOVTO_, 
							Comun._I(Comun._T(header.getIdUsuario())));
			}
			
			RespuestaSVC resActSaldo = dao.actualizaSaldoStdDao(ctaDeposito, trans.getActualizaSaldo(), _MOVTO_, 
					                                            Comun._I(Comun._T(header.getIdUsuario())));
			
			

			mapResultado.put("REFERENCIA", Comun._T(ctaDeposito.getReferencia()));
			mapResultado.put("PRODUCTO_ID",  Comun._T(ctaDeposito.getProductoAhorroId()));
			
			mapResultado.put("AUTORIZACION", autorizacion);
			mapResultado.put("TIPO_CLIENTE", Comun._T(ctaDeposito.getTipoCliente()));
			mapResultado.put("CUENTA_DEPOSITO", Comun._T(ctaDeposito.getCuenta()));
			mapResultado.put("CUENTA_EJE", ctaEje != null ? Comun._T(ctaEje.getCuenta()) : "");
			mapResultado.put("MONTO_TRANSACCION", Comun._T(trans.getMonto()));
			mapResultado.put("FECHA_TRANSACCION", new SimpleDateFormat(_FECHA_FORMATO_).format(Calendar.getInstance().getTime()));
			mapResultado.put("ESTATUS", "OK");
			
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	
	private String validaParams(TransaccionCuentasReq obj){
		String valida = null;
		if(obj == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(obj.getCuentaClabe() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATOS DE LA CLABE INTERBANCARIA");
		if(obj.getMonto() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN MONTO PROPORCIONADO");
		if(obj.getMonto().doubleValue() <= 0.00d) return Errores.desc(Errores.ERROR_PARAMETROS, "MONTO ES MENOR A CERO");
		if(obj.getIdentificador() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN TIPO DE IDENTIFICADOR");
		return valida;
	}
	
	private String getAutorizacion(String identificador){
		SimpleDateFormat sdf = new SimpleDateFormat(_FECHA_AUT_);
		int random = (int )(Math.random() * 50 + 1);
		String autorizacion = String.format("%s-%s-%s", identificador, Comun._T(random), sdf.format(Calendar.getInstance().getTime()));
		return autorizacion;
	}
}

