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

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.BitLogger;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.cuentas.logic.TransaccionSaldo;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.request.TransaccionCuentasReq;
import net.std.soap.servicios.ProcesoRetirarPlastico;


@Controller
public class RetiraCuentaCeroTraspasoSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(RetiraCuentaCeroTraspasoSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	private static final String _FECHA_AUT_ = "yyyyMMddHHmmssSSS";
	private static final String _MOVTO_ = "RETIRO";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;
	private static CuentasReferenciadasStdDAO daoRef = null; 

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/retiroCuentaCeroTraspasoStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		Thread.currentThread().setName("retiro_Traspaso_"+System.currentTimeMillis());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		TransaccionCuentasReq trans = null;
		String autorizacion = null;
		HeaderWS header;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		log.info(json);

		try{
			if(dao == null || daoAho == null || daoRef == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoAho == null || daoRef == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}

			trans = new Gson().fromJson(json, TransaccionCuentasReq.class);
			String valida = validaParams(trans);
			if(valida != null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida), HttpStatus.FORBIDDEN);
				return response;
			}
			
			/************************************************************
			 * 	SPEI_BITACORA
			 * **********************************************************/
			BitLogger.bitacora("RETIRO :: " + RetiraCuentaCeroTraspasoSvc.class.getName() + " :: LINE " + new Throwable().getStackTrace()[0].getLineNumber(), trans.getClaveRastreo());

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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_RETIRO_AHORRO), "TRX_RETIRO_AHORRO: " + Comun._T(trans.getCuentaClabe()))){
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
			
			
			/* VERIFICA LA CUENTA Y LA VALIDES DE LA CUENTA ORDENANTE (DEPOSITOS), BENEFICIARIA (RETIROS) */
			
			CuentaOBJ ctaRetiro = null;
			RespuestaSVC respCtaRet = daoAho.leerCuentaAhorroClabeDao(Comun._TX(trans.getCuentaClabe()));
			if(respCtaRet.getErrores().getCodigoError() == 0){
				/* ENCONTRO LA CUENTA COMO CUENTA CONCENTRADORA */
				ctaRetiro = (CuentaOBJ) respCtaRet.getBody().getValor("CUENTA");
				if(Comun._TX(Constantes.TIPO_CUENTA_BLU).equals(ctaRetiro.getTipoCliente())){
                    /* VALIDA LA CUENTA ORDENANTE */
                    RespuestaSVC respValida = daoRef.leerCuentaConcentradoraReferenciadaStdDao(Comun._TX(trans.getCuentaClabe()),
                                                        Comun._TX(trans.getCuentaClabeEmiRec()), Comun._TX(Constantes.TIPO_CUENTA_SALIDA));
                    if(respValida.getErrores().getCodigoError() != 0){
                        log.info(Errores.desc(Errores.ERROR_TIPO_CUENTA_SALIDA, String.format("%s - %s", Comun._TX(trans.getCuentaClabe()) ,Comun._TX(trans.getCuentaClabeEmiRec()))));
                        /*
                        response = new ResponseEntity<>(Errores.desc(Errores.ERROR_TIPO_CUENTA_SALIDA,
                                                           String.format("%s - %s", Comun._TX(trans.getCuentaClabe()) ,Comun._TX(trans.getCuentaClabeEmiRec()))), HttpStatus.FORBIDDEN);
                        return response;
                        */    
                    }
                }
                
                ctaRetiro.setCuenta_referencia(trans.getCuentaClabe());
			}else{
				/* BUSCA COMO REFERENCIA */
				RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(trans.getCuentaClabe()), Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA));
				if(respRef.getErrores().getCodigoError() != 0){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA, 
							       					String.format("%s - %s", Comun._TX(trans.getCuentaClabe()) ,Comun._TX(trans.getCuentaClabeEmiRec()))), HttpStatus.FORBIDDEN);
					return response;	
				}
				CuentaReferenciadaOBJ obj = (CuentaReferenciadaOBJ)respRef.getBody().getValor("CUENTA");
				RespuestaSVC respRefer = daoAho.leerCuentaAhorroClabeDao(Comun._TX(obj.getClabe_interbancaria()));
				if(respRefer.getErrores().getCodigoError() != 0){
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA, 
	       						String.format("%s - %s", Comun._TX(trans.getCuentaClabe()) ,Comun._TX(trans.getCuentaClabeEmiRec()))), HttpStatus.FORBIDDEN);
					return response;	
				}
				ctaRetiro = (CuentaOBJ) respRefer.getBody().getValor("CUENTA");
				if(ctaRetiro != null){
					ctaRetiro.setCuenta_referencia(obj.getCuenta_referencia());
				}
			}	
			

			/* VERIFICA QUE LA CUENTA NO ESTE VIGENTE */
			if(!"VIG".equals(Comun._T(ctaRetiro.getEstatusClave()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", trans.getCuentaClabe(), Comun._T(ctaRetiro.getEstatus()))), HttpStatus.FORBIDDEN);
				return response;
			}

			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
			if("BLOQUEADO".equals(Comun._T(ctaRetiro.getBloqueado()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}

			/* OBTIENE UN NUMERO DE AUTORIZACION */
			autorizacion = getAutorizacion(Comun._TX(trans.getIdentificador()));
			if("".equals(Comun._T(autorizacion))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_NUMERO_AUTORIZACION, trans.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}

			RespuestaSVC respRetiro = new RespuestaSVC();
			/* VERIFICA SI LA CUENTA ES CON PLASTICO, SI = ENVIO DEPOSITO A PROVEEDOR SISCOOP */
			if("SI".equals(Comun._T(ctaRetiro.getCon_plastico())) && !"".equals(Comun._T(ctaRetiro.getPan()))){
				
				/* LLENA LOS PARAMETROS PARA SER INVOCADOS EN EL RETIRO DE MULE DEL PROVEEDOR PARA PLASTICOS*/
				PlaHeaderWS plaHeader = new PlaHeaderWS();

				plaHeader.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
				plaHeader.setIdCanalAtencion(Comun._L(Constantes.CANAL_ID));
				plaHeader.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
				plaHeader.setIdCliente(Comun._L(ctaRetiro.getPersonaId()));
				plaHeader.setIdCuenta(Comun._L(ctaRetiro.getId()));
				plaHeader.setIpHost(Comun._T(Constantes.HOST_ID));
				plaHeader.setIdPan(Comun._L(ctaRetiro.getPan_id()));

				DatosPlasticoREQ req = new DatosPlasticoREQ();
				req.setPlastico(Comun._T(ctaRetiro.getPan()));
				req.setMonto(Comun._D(trans.getMonto()));

				RespuestaSVC respPla = ProcesoRetirarPlastico.Retirar(plaHeader, req);
				if(respPla.getErrores().getCodigoError() != 0L){
					String error = String.format("[ CUENTA CON PLASTICO %s] %s", ctaRetiro.getClabeInterbancaria(), respPla.getErrores().getDescError());
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_RETIRO, error), HttpStatus.FORBIDDEN);
					return response;
				}else{
					DatosPlasticoOBJ ret = (DatosPlasticoOBJ)respPla.getBody().getValor("DATOS_PLASTICO_OBJ");
					log.info("CODIGO         : " + ret.getCodigo());
					log.info("AUTORIZACION   : " + ret.getAutorizacion());
					log.info("DESCRIPCION    : " + ret.getDescripcion());
					log.info("BALANCE        : " + ret.getBalance());
					log.info("BALANCE ACTUAL : " + ret.getBalanceActual());
					
					if(!Constantes.BANDERA_RETIROS_TRASPASOS_NUEVOS) respRetiro = enviaRetiroLocal(ctaRetiro, trans, header, autorizacion, false);   /* TRUE = CONSULTA SALDO, FALSE = NO CONSULTA SALDO */
					else respRetiro = TransaccionSaldo.transaccionLocal(ctaRetiro, trans, "RET_TRASPASO", header, autorizacion, trans.getId_spei(), 0);   /* 0 = RETIRO, 1 = DEPOSITO */
				}
			}else{
				log.info("Retiro Local");
				if(!Constantes.BANDERA_RETIROS_TRASPASOS_NUEVOS) respRetiro = enviaRetiroLocal(ctaRetiro, trans, header, autorizacion, true);   /* TRUE = CONSULTA SALDO, FALSE = NO CONSULTA SALDO */
				else respRetiro = TransaccionSaldo.transaccionLocal(ctaRetiro, trans, "RET_TRASPASO", header, autorizacion, trans.getId_spei(), 0);   /* 0 = RETIRO, 1 = DEPOSITO */
				if(respRetiro.getErrores().getCodigoError() != 0L){
					String error = String.format("[ CUENTA CON PLASTICO %s] %s", ctaRetiro.getClabeInterbancaria(), respRetiro.getErrores().getDescError());
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_RETIRO, error), HttpStatus.FORBIDDEN);
					return response;		
				}
			}
		
			/* VERIFICAR SI LA CUENTA REFERENCIADA ES CUENTA_BLU, COLOCAR LOS VALORES DE CORREO Y TELEFONO */
			mapResultado.put("TRANSACCION", _MOVTO_);
			mapResultado.put("REFERENCIA", Comun._T(ctaRetiro.getReferencia()));
			mapResultado.put("PRODUCTO_ID",  Comun._T(ctaRetiro.getProductoAhorroId()));

			mapResultado.put("AUTORIZACION", autorizacion);
			mapResultado.put("TIPO_CLIENTE", Comun._T(ctaRetiro.getTipoCliente()));

			mapResultado.put("CUENTA_RETIRO", Comun._T(ctaRetiro.getCuenta()));
			mapResultado.put("MONTO_TRANSACCION", Comun._T(trans.getMonto()));
			mapResultado.put("FECHA_TRANSACCION", new SimpleDateFormat(_FECHA_FORMATO_).format(Calendar.getInstance().getTime()));

			mapResultado.put("CUENTA_CONCENTRADORA", Comun._TX(ctaRetiro.getClabeInterbancaria()));
			mapResultado.put("CUENTA_REFERENCIA", Comun._TX(ctaRetiro.getCuenta_referencia()));
			
			mapResultado.put("ID", Comun._T(respRetiro.getBody().getValor("ID")));
			
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
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(obj.getCuentaClabe() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS DE LA CLABE INTERBANCARIA");
		if(obj.getMonto() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN MONTO PROPORCIONADO");
		if(obj.getMonto().doubleValue() <= 0.00d) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MONTO ES MENOR A CERO");
		//if(obj.getIdentificador() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN TIPO DE IDENTIFICADOR");
		return valida;
	}

	private String getAutorizacion(String identificador){
		SimpleDateFormat sdf = new SimpleDateFormat(_FECHA_AUT_);
		int random = (int )(Math.random() * 50 + 1);
		String autorizacion = String.format("%s-%s-%s", identificador, Comun._T(random), sdf.format(Calendar.getInstance().getTime()));
		return autorizacion;
	}

	@SuppressWarnings("unused")
	private static RespuestaSVC enviaRetiroLocal(CuentaOBJ cta, TransaccionCuentasReq trx, HeaderWS header, String autorizacion, Boolean consultaSaldo){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		try{

			if(consultaSaldo){
				/* COMPRUEBA EL SALDO DE LA CUENTA DE RETIRO */
				RespuestaSVC respSaldo = dao.leerSaldoCuentaStdDao(cta.getId());
				if(respSaldo.getErrores().getCodigoError() != 0){
					return respSaldo;
				}

				BigDecimal saldo = (BigDecimal)(respSaldo.getBody().getValor("SALDO"));
				if(saldo.doubleValue() < trx.getMonto().doubleValue()){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_SALDO_INSUFICIENTE, (Errores.desc(Errores.ERROR_SALDO_INSUFICIENTE, cta.getClabeInterbancaria())));
					return respuestaSvc;
				}
			}

			RespuestaSVC respRetiro = dao.retirarStdDao(cta, "RET_TRASPASO", new Date(), trx.getMonto(), 
														trx.getConcepto(), autorizacion, header, trx.getClaveRastreo());
			
			if(respRetiro.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_RETIRO, (Errores.desc(Errores.ERROR_RETIRO,
						String.format("%s - ", cta.getClabeInterbancaria(), respRetiro.getErrores().getDescError()))));
				return respuestaSvc;
			}
			
			respuestaSvc.getBody().addValor("ID", respRetiro.getBody().getValor("ID"));
			
			RespuestaSVC resActSaldo = dao.actualizaSaldoStdDao(cta, trx.getActualizaSaldo(), _MOVTO_,	Comun._I(Comun._T(header.getIdUsuario())));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		return respuestaSvc;
	}
}

