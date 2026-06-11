package net.std.cuentas.svc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
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

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.BitLogger;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.cuentas.logic.TransaccionSaldo;
import net.std.dao.AcumuladoSaldoMensualDAO;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.dao.SpeiStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.data.HeaderWS;
import net.std.request.TransaccionCuentasReq;
import net.std.servicios.ServicioValidarMontoTransaccional;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Controller
public class DepositoCuentaCeroStdSvc implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DepositoCuentaCeroStdSvc.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	private static final String _FECHA_AUT_ = "yyyyMMddHHmmssSSS";
	private static final String _MOVTO_ = "DEPOSITO";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;
	private static SolicitanteStdDAO daoSol = null;
	private static CuentasReferenciadasStdDAO daoRef = null;

	private static AcumuladoSaldoMensualDAO daoAcum = null;
	private static SpeiStdDAO daoSSTD = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null)
					apps = s;
			}
			dao = (TransaccionesStdDAO) s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO) s.getApplicationContext().getBean("AhorroStdDAO");
			daoSol = (SolicitanteStdDAO) s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoRef = (CuentasReferenciadasStdDAO) s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");
			daoAcum = (AcumuladoSaldoMensualDAO) s.getApplicationContext().getBean("AcumuladoSaldoMensualDAO");
			daoSSTD = (SpeiStdDAO) s.getApplicationContext().getBean("SpeiStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/depositoCuentaCeroExtStd", method = RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json) {
    	Thread.currentThread().setName("deposito_"+System.currentTimeMillis());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		TransaccionCuentasReq trans = null;
		String autorizacion = null;
		net.cero.ws.data.HeaderWS header;
		Boolean tipoPagoEnProperties = false;
		
		log.info(String.format("IN -> %s :: %s", new Object() {
		}.getClass().getName(), new Object() {
		}.getClass().getEnclosingMethod().getName()));
		log.info(json);

		try {
			if (dao == null || daoAho == null || daoSol == null || daoRef == null || daoSSTD == null)
				initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if (dao == null || daoAho == null || daoSol == null || daoRef == null || daoSSTD == null) {
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}

			trans = new Gson().fromJson(json, TransaccionCuentasReq.class);
			String valida = validaParams(trans);
			if (valida != null) {
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida),
						HttpStatus.FORBIDDEN);
				return response;
			}

			/************************************************************
			 * SPEI_BITACORA
			 **********************************************************/
			BitLogger.bitacora("DEPOSITO :: " + DepositoCuentaCeroStdSvc.class.getName() + " :: LINE "
					+ new Throwable().getStackTrace()[0].getLineNumber(), trans.getClaveRastreo());

		} catch (Exception ex) {
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()),
					HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}

		try {

			/* LLENA EL HEADER */
			header = new net.cero.ws.data.HeaderWS();
			header.setIdCanalAtencion(Comun._L(Constantes.CANAL_ID));
			header.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
			header.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
			header.setIpHost(Comun._T(Constantes.HOST_ID));

			/*
			 * VERIFICA LA CUENTA Y LA VALIDES DE LA CUENTA ORDENANTE
			 * (DEPOSITOS), BENEFICIARIA (RETIROS)
			 */

			CuentaOBJ ctaDeposito = null;
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(trans.getCuentaClabe()));
			if (respCtaDep.getErrores().getCodigoError() == 0) {
				/* ENCONTRO LA CUENTA COMO CUENTA CONCENTRADORA */
				ctaDeposito = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
				if (Comun._TX(Constantes.TIPO_CUENTA_BLU).equals(ctaDeposito.getTipoCliente())) {
					/* VALIDA LA CUENTA ORDENANTE */
					RespuestaSVC respValida = daoRef.leerCuentaConcentradoraReferenciadaStdDao(
							Comun._TX(trans.getCuentaClabe()), Comun._TX(trans.getCuentaClabeEmiRec()),
							Comun._TX(Constantes.TIPO_CUENTA_ENTRADA));
					if (respValida.getErrores().getCodigoError() != 0) {
						log.info(Errores.desc(Errores.ERROR_TIPO_CUENTA_ENTRADA, String.format("%s - %s",
								Comun._TX(trans.getCuentaClabe()), Comun._TX(trans.getCuentaClabeEmiRec()))));
						/*
						 * response = new ResponseEntity<>(Errores.desc(Errores.
						 * ERROR_TIPO_CUENTA_ENTRADA, String.format("%s - %s",
						 * Comun._TX(trans.getCuentaClabe())
						 * ,Comun._TX(trans.getCuentaClabeEmiRec()))),
						 * HttpStatus.FORBIDDEN); return response;
						 */
					}
				}
				ctaDeposito.setCuenta_referencia(trans.getCuentaClabe());
			} else {
				/* BUSCA COMO REFERENCIA */
				RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(trans.getCuentaClabe()),
						Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA));
				if (respRef.getErrores().getCodigoError() != 0) {
					response = new ResponseEntity<>(
							Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA, String.format("%s - %s",
									Comun._TX(trans.getCuentaClabe()), Comun._TX(trans.getCuentaClabeEmiRec()))),
							HttpStatus.FORBIDDEN);
					return response;
				}

				CuentaReferenciadaOBJ obj = (CuentaReferenciadaOBJ) respRef.getBody().getValor("CUENTA");
				RespuestaSVC respRefer = daoAho.leerCuentaAhorroClabeDao(Comun._TX(obj.getClabe_interbancaria()));
				if (respRefer.getErrores().getCodigoError() != 0) {
					response = new ResponseEntity<>(
							Errores.desc(Errores.ERROR_CUENTA, String.format("%s - %s",
									Comun._TX(trans.getCuentaClabe()), Comun._TX(trans.getCuentaClabeEmiRec()))),
							HttpStatus.FORBIDDEN);
					return response;
				}
				ctaDeposito = (CuentaOBJ) respRefer.getBody().getValor("CUENTA");
				if (ctaDeposito != null) {
					ctaDeposito.setCuenta_referencia(obj.getCuenta_referencia());
				}
			}

			/*VALIDAMOS CANALES*/
			/*
			String respuestaCanal = dao.validaCanales("SPEI","DEP_TRANS"
					,String.valueOf(ctaDeposito.getProductoAhorroId()));
			log.info("VALIDACION CANAL: {}, {}, {}",respuestaCanal,ctaDeposito.getCuenta(),trans.getClaveRastreo());
			if(!StringUtils.isBlank(respuestaCanal) && !respuestaCanal.equals("ok") && !respuestaCanal.equals("No"))
			{
				String error = String.format("[ PRODUCTO VALIDACION CANAL BLOQUEADO %s] %s", ctaDeposito.getClabeInterbancaria(),
						respuestaCanal);
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, error), HttpStatus.FORBIDDEN);
				return response;
			}
			*/
			/*VALIDAMOS CANALES CUENTAS*/
			/*
			Boolean respuestaCuentaCanal = dao.validaCanalCuenta("SPEI","DEP_TRANS"
					,String.valueOf(ctaDeposito.getProductoAhorroId()));
			log.info("VALIDACION CANAL CUENTA: {}, {}, {}",respuestaCuentaCanal,ctaDeposito.getCuenta(),trans.getClaveRastreo());
			if(!respuestaCuentaCanal)
			{
				String error = String.format("[ CUENTA VALIDACION CANAL BLOQUEADO %s] %s", ctaDeposito.getClabeInterbancaria(),
						respuestaCuentaCanal);
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, error), HttpStatus.FORBIDDEN);
				return response;
			}
			*/

			/* VERIFICA QUE LA CUENTA NO ESTE VIGENTE */
			if (!"VIG".equals(Comun._T(ctaDeposito.getEstatusClave()))) {
				response = new ResponseEntity<>(
						Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA,
								String.format("%s - %s", trans.getCuentaClabe(), Comun._T(ctaDeposito.getEstatus()))),
						HttpStatus.FORBIDDEN);
				return response;
			}

			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
			if ("BLOQUEADO".equals(Comun._T(ctaDeposito.getBloqueado()))) {
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, trans.getCuentaClabe()),
						HttpStatus.FORBIDDEN);
				return response;
			}

			/* OBTIENE UN NUMERO DE AUTORIZACION */
			autorizacion = getAutorizacion(Comun._TX(trans.getIdentificador()));
			if ("".equals(Comun._T(autorizacion))) {
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_NUMERO_AUTORIZACION, trans.getCuentaClabe()),
						HttpStatus.FORBIDDEN);
				return response;
			}

			RespuestaSVC respDeposito = new RespuestaSVC();
			/*
			 * Consulta si tipo de pago de spei in se encuentra en listado de std.properties
			 */
			//OBTENER TIPO DE PAGO DE SPEI_INCOMING
			Integer tipoPago = trans.getId_spei() != null ? daoSSTD.ObtenerTipoPago(trans.getId_spei()) : null;
			log.info("tipoPago: " + new Gson().toJson(tipoPago));
			boolean esMovimientoAcumulable = false;
			int [] tipoPagosNoAcumulables = Stream.of(Constantes.ID_TIPO_TRANSACCIONES.split(",")).mapToInt(Integer::parseInt).toArray();
			List<Integer> ListaTipoPagos = Arrays.stream(tipoPagosNoAcumulables).boxed().collect(Collectors.toList());
			log.info("ListaTipoPagos: " + new Gson().toJson(ListaTipoPagos));
			log.info("tipoPagosNoAcumulables: " + new Gson().toJson(tipoPagosNoAcumulables));
			
			if(ListaTipoPagos.contains(tipoPago)){
				tipoPagoEnProperties = true;
			} else {
				tipoPagoEnProperties = false;
			}
			
			log.info("Valor de ListaTipoPagos: " + tipoPagoEnProperties);
			if(!tipoPagoEnProperties) {
				log.info("Inicia validacion de monto");
				Boolean validamonto = ServicioValidarMontoTransaccional.process(ctaDeposito.getCuenta(), trans.getMonto());
				if(!validamonto)
				{
					String error = String.format("[ CUENTA %s] %s", ctaDeposito.getClabeInterbancaria(),
							"La cuenta supera el limite mensual permitido de depositos mensuales para el tipo de cuenta.");
					log.info("Cuenta arrebasa o ha arrebasado limite de depositos maximos mensuales: {},{}",ctaDeposito.getCuenta(),trans.getClaveRastreo());
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, error), HttpStatus.FORBIDDEN);
					return response;
				}
			}
			
			
			/*
			 * VERIFICA SI LA CUENTA ES CON PLASTICO, SI = ENVIO DEPOSITO A
			 * PROVEEDOR SISCOOP
			 */
			log.info("Datos cuenta: " + new Gson().toJson(ctaDeposito));
			if ("SI".equals(Comun._T(ctaDeposito.getCon_plastico()))) {
				log.info("CON_PLASTICO : SI");
				log.info("PAN : " + Comun._T(ctaDeposito.getTarjeta_principal()));
				HeaderWS headerws = new HeaderWS();
				net.std.data.TransaccionTarjetaOrquestadorOBJ reqTra = new net.std.data.TransaccionTarjetaOrquestadorOBJ();
				net.std.data.Respuesta respuesta = new net.std.data.Respuesta();

				///////////////Se cambia el llamado del ws para fondear a siscoop por el orquestador
				log.info("concepto de pago: "+trans.getConcepto());
				headerws.setIpHost(header.getIpHost());
				headerws.setIdBanco("659");
				headerws.setNameHost("Servidor SPEI-BLU");
				headerws.setIdCanalAtencion(headerws.getIdCanalAtencion());
				headerws.setIdSucursal(headerws.getIdSucursal());
				headerws.setIdUsuario(headerws.getIdUsuario() == 0 ? 9 : headerws.getIdUsuario());
				headerws.setUsuarioClave(headerws.getUsuarioClave());
				reqTra.setHeader(headerws);
				reqTra.setCuenta(ctaDeposito.getCuenta());
				reqTra.setImporte(String.valueOf(trans.getMonto()));
				reqTra.setMedioPago(Constantes.MEDIO_PAGO_SPEI);
				reqTra.setConcepto("DEPOSITO TRANSFERENCIA SPEI");
				reqTra.setObservaciones(StringUtils.truncate(trans.getConcepto(),255));
				reqTra.setReferenciaNumerica(generaReferencia());
				reqTra.setClave_rastreo(trans.getClaveRastreo());
				reqTra.setNumero_tarjeta(ctaDeposito.getTarjeta_principal());
				reqTra.setIdSpei(trans.getId_spei());

				if(tipoPagoEnProperties){
					reqTra.setClaveMovimiento(Constantes.CVE_MOV_DEV_SPEI);
					
				} else {
					reqTra.setClaveMovimiento(Constantes.CVE_MOV_DEP_SPEI);
				}
				
				MediaType media = MediaType.parse("application/json; charset=utf-8");
				OkHttpClient cliente = new OkHttpClient.Builder()
						.readTimeout(Constantes.TIMEOUT_TRANSACCION_PLASTICOS_SEGUNDOS, TimeUnit.SECONDS)
						.build();
				String auth = Credentials.basic("ASP", "a5p2017$");
				String url = Constantes.WS_ADMIN_PLA + "/fondearTarjetaOrquestadorSpei";
				String body = new Gson().toJson(reqTra);
				log.info("url ORQUESTADOR: {}",url);
				log.info("Request body para el orquestador: {}",body);
				Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
				Response responseOr = cliente.newCall(request).execute();
				String respuestaString = responseOr.body().string() ;
				log.info("Respuesta de transaccionTarjetaOrquestador: " + respuestaString);
				respuesta = new Gson().fromJson(respuestaString, net.std.data.Respuesta.class);
				///////////////

				if (respuesta.getCodigo() != 0L) {
					String error = String.format("[ CUENTA CON PLASTICO %s] %s", ctaDeposito.getClabeInterbancaria(),
							respuesta.getMensaje());
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, error), HttpStatus.FORBIDDEN);
					return response;
				}
			} else {
				log.info("CAMINO DE CUENTAS DE CERO SIN PLASTICO PARA LA CUENTA {} CON CLAVE {}",ctaDeposito.getCuenta(),trans.getClaveRastreo());
				log.info("CON_PLASTICO : NO");
				log.info("PAN : " + Comun._T(ctaDeposito.getPan()));

				log.info("INICIO DE DEPOSITO LOCAL EN CERO: {},{}",ctaDeposito.getCuenta(),trans.getClaveRastreo());
				
				log.info("Constantes.BANDERA_DEPOSITOS_NUEVOS: " + Constantes.BANDERA_DEPOSITOS_NUEVOS);
				if(!Constantes.BANDERA_DEPOSITOS_NUEVOS) respDeposito = enviaDepositoLocal(ctaDeposito, trans, header, autorizacion, tipoPagoEnProperties);
				else 
				{
					log.info("tipoPagoEnProperties: " + tipoPagoEnProperties);
					if(tipoPagoEnProperties){
						log.info("Ingresa If CVE_TIPO_TRANS_SPEI");
						respDeposito = TransaccionSaldo.transaccionLocal(ctaDeposito, trans, Constantes.CVE_TIPO_TRANS_SPEI, header, autorizacion, trans.getId_spei(), 1);   /* 0 = RETIRO, 1 = DEPOSITO */
					}else {
						log.info("Ingresa If DEP_TRANS");
						respDeposito = TransaccionSaldo.transaccionLocal(ctaDeposito, trans, "DEP_TRANS", header, autorizacion, trans.getId_spei(), 1);   /* 0 = RETIRO, 1 = DEPOSITO */
					}
				}	
				log.info("respDeposito: " + respDeposito);
				
				if (respDeposito.getErrores().getCodigoError() != 0L) {
					String error = String.format("[ CUENTA SIN PLASTICO %s] %s", ctaDeposito.getClabeInterbancaria(),
							respDeposito.getErrores().getDescError());
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_DEPOSITO, error), HttpStatus.FORBIDDEN);
					log.info("FIN DE DEPOSITO LOCAL EN CERO CON ERROR: {},{}",ctaDeposito.getCuenta(),trans.getClaveRastreo());
					return response;
				}
				log.info("FIN DE DEPOSITO LOCAL EN CERO: {},{}",ctaDeposito.getCuenta(),trans.getClaveRastreo());

			}

			/*
			 * VERIFICAR SI LA CUENTA REFERENCIADA ES CUENTA_BLU, COLOCAR LOS
			 * VALORES DE CORREO Y TELEFONO
			 */

			mapResultado.put("TRANSACCION", _MOVTO_);
			mapResultado.put("REFERENCIA", Comun._T(ctaDeposito.getReferencia()));
			mapResultado.put("PRODUCTO_ID", Comun._T(ctaDeposito.getProductoAhorroId()));

			mapResultado.put("AUTORIZACION", autorizacion);
			mapResultado.put("TIPO_CLIENTE", Comun._T(ctaDeposito.getTipoCliente()));

			mapResultado.put("CUENTA_DEPOSITO", Comun._T(ctaDeposito.getCuenta()));
			mapResultado.put("MONTO_TRANSACCION", Comun._T(trans.getMonto()));
			mapResultado.put("FECHA_TRANSACCION",
					new SimpleDateFormat(_FECHA_FORMATO_).format(Calendar.getInstance().getTime()));

			mapResultado.put("CUENTA_CONCENTRADORA", Comun._TX(ctaDeposito.getClabeInterbancaria()));
			// mapResultado.put("CUENTA_REFERENCIA", Comun._TX(clabeDeposito));
			mapResultado.put("CUENTA_REFERENCIA", Comun._TX(ctaDeposito.getCuenta_referencia()));

			mapResultado.put("ID", Comun._T(respDeposito.getBody().getValor("ID")));

			mapResultado.put("ESTATUS", "OK");

			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

		log.info(String.format("OUT -> %s :: %s", new Object() {
		}.getClass().getName(), new Object() {
		}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	private String validaParams(TransaccionCuentasReq obj) {
		String valida = null;
		if (obj == null)
			return Errores.desc(Errores.ERROR_PARAMETROS, "SIN PARAMETROS");
		if (obj.getCuentaClabe() == null)
			return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATOS DE LA CLABE INTERBANCARIA");
		if (obj.getMonto() == null)
			return Errores.desc(Errores.ERROR_PARAMETROS, "SIN MONTO PROPORCIONADO");
		if (obj.getMonto().doubleValue() <= 0.00d)
			return Errores.desc(Errores.ERROR_PARAMETROS, "MONTO ES MENOR A CERO");
		// if(obj.getIdentificador() == null) return
		// Errores.desc(Errores.ERROR_PARAMETROS, "SIN TIPO DE IDENTIFICADOR");
		return valida;
	}

	private String getAutorizacion(String identificador) {
		SimpleDateFormat sdf = new SimpleDateFormat(_FECHA_AUT_);
		int random = (int) (Math.random() * 50 + 1);
		String autorizacion = String.format("%s-%s-%s", identificador, Comun._T(random),
				sdf.format(Calendar.getInstance().getTime()));
		return autorizacion;
	}

	@SuppressWarnings("unused")
	private static RespuestaSVC enviaDepositoLocal(CuentaOBJ cta, TransaccionCuentasReq trx, net.cero.ws.data.HeaderWS header,
												   String autorizacion, Boolean tipoPagoEnProperties) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		try {
			RespuestaSVC respDeposito = new RespuestaSVC();
			
			log.info("VALOR DE trx.getId_spei() :: " + trx.getId_spei());
			
			if (trx.getId_spei() == null) {
				respDeposito = dao.depositarStdDao(cta, "DEP_TRANS", new Date(), trx.getMonto(),
						Comun._T(trx.getConcepto()), autorizacion, header, trx.getClaveRastreo());
			} else {
				
				log.info("VALOR DE tipoPagoEnProperties :: " + tipoPagoEnProperties);
				//cambiar por clave devolucion_spei clave de transaccion DEVOLUCION_SPEI ahtipos_transacciones para cuentas {}
				if(!tipoPagoEnProperties){
					log.info("If DEP_TRANS ");
					respDeposito = dao.depositarSpeiStdDao(cta, "DEP_TRANS", new Date(), trx.getMonto(),
						Comun._T(trx.getConcepto()), autorizacion, header, trx.getClaveRastreo(), trx.getId_spei());
				} else {
					log.info("If CVE_TIPO_TRANS_SPEI ");
					respDeposito = dao.depositarSpeiStdDao(cta, Constantes.CVE_TIPO_TRANS_SPEI, new Date(), trx.getMonto(),
						Comun._T(trx.getConcepto()), autorizacion, header, trx.getClaveRastreo(), trx.getId_spei());
				}
				
			}

			if (respDeposito.getErrores().getCodigoError() != 0) {
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO,
						(Errores.desc(Errores.ERROR_DEPOSITO, String.format("%s - ", cta.getClabeInterbancaria(),
								respDeposito.getErrores().getDescError()))));
				return respuestaSvc;
			}

			respuestaSvc.getBody().addValor("ID", respDeposito.getBody().getValor("ID"));

			/* ACTUALIZA EL SALDO */
			RespuestaSVC resActSaldo = dao.actualizaSaldoStdDao(cta, trx.getActualizaSaldo(), _MOVTO_,
					Comun._I(Comun._T(header.getIdUsuario())));
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		return respuestaSvc;
	}

	private String generaReferencia() {
		Integer numeroReferencia = daoAho.referenciaQR();
		String numeroReferenciaTMP = numeroReferencia.toString();
		log.info("numeroReferenciaTMP :: " + numeroReferenciaTMP);
		if(numeroReferenciaTMP.length() <= 6) {
			for(int x=0;numeroReferenciaTMP.length()<6;x++) {
				numeroReferenciaTMP = "0" + numeroReferenciaTMP;
			}
			numeroReferenciaTMP = "1" + numeroReferenciaTMP;
		}
		return numeroReferenciaTMP;
	}

}
