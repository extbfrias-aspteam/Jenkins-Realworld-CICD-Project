package net.spei.svc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spei.data.OutgoingJdbcDevOBJ;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.CCifra;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.implementacion.BuscaClabeParticipanteImp;
import net.std.implementacion.DevolucionCuentaCeroImp;
import net.std.implementacion.DevolucionCuentaProcreaImp;
import net.std.implementacion.InsertarControlDevolucionesImp;
import net.std.request.TransaccionCuentasProcreaReq;
import net.std.request.TransaccionCuentasReq;
import net.std.soap.servicios.ProcesoDepositarPlastico;

//@Controller
@RestController
public class DevolucionSpeiStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DevolucionSpeiStdSvc.class);
	private static String _FORMATO_FECHA_ = "yyyy-MM-dd";
	private static String _FORMATO_FECHA_OPERACION_ = "dd/MM/yyyy";
	private static String[] _OBSERVACIONES = {
			"INICIO PROCESO DEVOLUCION CUENTA %s - %s : %s", 
			"OK, SE DEVOLVIO EL SALDO CUENTA PLASTICO, NO LOCAL A LA CUENTA ORDENANTE %s - %s : %s", 
			"OK, DEVOLUCION CORRECTA A LA CUENTA ORDENANTE %s - %s : %s", 
			"ERROR EN DEVOLUCION CUENTA ORDENANTE %s - %s : %s",
			"NO PROCEDE DEVOLUCION POR ESTAR FUERA DE PERIODO %s - %s : %s",
			"OK, DEVOLUCION YA EXISTENTE EN LA CUENTA ORDENANDE %s - %s : %s"
	};

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@SuppressWarnings("unused")
	@RequestMapping(value="/devolucionSaldoSpeiStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		String strDecodificado = null;
		String fechaOperacion = null;
		OutgoingJdbcDevOBJ dev = null;
		String dato = "";
		String concepto = "";

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));


		/* VERIFICA QUE SE TENGA PERMISOS PARA ACCEDER AL METODO Y VALIDACION DE PARAMETROS */
		try{
			Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
			if(!authenticate.isAuthenticated()){
				log.error("ERROR: DE AUTENTICACION");
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, "ERROR: DE AUTENTICACION");
				
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}

			/* VERIFICA PARAMETROS DE ENTRADA ANTES DE HACER CUALQUIER PROCESO */
			if(json == null || "".equals(Comun._T(json))) {
				log.error("ERROR: PARAMETROS VACIOS");
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, "ERROR: PARAMETROS VACIOS");
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
			return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}
		
		synchronized (this) {
			/* OBTIENE EL BEAN DE LA CONSULTA PARA SU ANALISIS Y APLICACION */
			
			try {	
				HashMap<String, String> map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
				strDecodificado = CCifra.decodeBCDtoStr(map.get("DEVOLUCION_SPEI"));
				fechaOperacion = CCifra.decodeBCDtoStr(map.get("FECHA_OPERACION"));

				ObjectMapper mapper = new ObjectMapper();
				dev = mapper.readValue(strDecodificado, OutgoingJdbcDevOBJ.class);
				//dev = new Gson().fromJson(strDecodificado, OutgoingJdbcOBJ.class);
				if(dev == null) {
					log.error("ERROR: NO SE OBTUVO LA CADENA DE PROCESO");
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, "ERROR: NO SE OBTUVO LA CADENA DE PROCESO");
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}

				/* VALIDACION EXTRAORDINARIA POR SI LAS FLIES 
				 * 30 OCT 2020 
				 */
				log.info(String.format("** VALIDANDO PARAMETROS ------------------------------->> **"));
				despliegaParametros(dev);
				
				RespuestaSVC resVal = new RespuestaSVC();
				
				if("".equals(Comun._TX(dev.getClave_rastreo()))) {
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s] CLAVE RASTREO NULO", dev.getClave_rastreo()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}
				if(dev.getId_spei_outgoing() == null || dev.getId_spei_outgoing() <= 0L){
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s - %d] SPEI OUTGOING ID INCORRECTO", dev.getClave_rastreo(),dev.getId_spei_outgoing()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}
				if(dev.getStatus_operacion() == null || dev.getStatus_operacion() <= 0){
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s - %d] STATUS OPERACION ID INCORRECTO", dev.getClave_rastreo(),dev.getStatus_operacion()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}
				if("".equals(Comun._TX(dev.getMonto())) || Comun._D(dev.getMonto()) <= 0.00d){
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s - %s] MONTO INCORRECTO", dev.getClave_rastreo(),dev.getMonto()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}

				if("".equals(Comun._TX(dev.getCuenta_ordenante()))){
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s - %s] STATUS OPERACION ID INCORRECTO", dev.getClave_rastreo(),dev.getCuenta_ordenante()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);

				}
				
				if(!(dev.getStatus_operacion() == 5 || dev.getStatus_operacion() == 6 || dev.getStatus_operacion() == 9 || dev.getStatus_operacion() == 10 ||
						dev.getStatus_operacion() == 102 || dev.getStatus_operacion() == 103 || dev.getStatus_operacion() == 104)) {
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s - %d] STATUS OPERACION ID INCORRECTO", dev.getClave_rastreo(),dev.getStatus_operacion()));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}
				
				/*
				if(dev.getStatus_operacion() != 15){
					if(!(dev.getStatus_operacion() == 5 || dev.getStatus_operacion() == 6 || dev.getStatus_operacion() == 9 || dev.getStatus_operacion() == 10 ||
							dev.getStatus_operacion() == 102)) return;
				}else{
					if(!(dev.getStatus_operacion() == 15 && "17".equals(dev.getId_tipo_pago()))) return;  // sobra la comparacion de status operacion 
				}
				*/

				dato = String.format("....[ CR : %s ] STATUS OPERACION : %d , ID TIPO PAGO : %s",
						   dev.getClave_rastreo(),
			               dev.getStatus_operacion(), 
			               dev.getId_tipo_pago());
				
				log.info(dato);
				respuestaSvc.getBody().addValor("RESULTADO", dato);
			}catch(Exception ex){
				ex.printStackTrace();
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}
			
			/* OBTIENE EL CONCEPTO STANDARD
			 * 05 FEB 2021
			 * JBM
			 */
			concepto = String.format("PAGO RECIBIDO POR DEVOLUCION DE %s ORDEN DE %s POR UN MONTO DE %s CTA ORDENANTE %s REFERENCIA %s FOLIO %s CLAVE RASTREO %s FECHA %s",
					dev.getNombre_institucion_ben(),
					dev.getNombre_beneficiario(),
					dev.getMonto(),
					dev.getCuenta_beneficiario(),
					Comun._T(dev.getReferencia_numerica()),
					Comun._T(dev.getId_spei_outgoing()),
					dev.getClave_rastreo(),
					Comun._Fecha(new Date(), "dd / MMM / yyyy HH:mm:ss"));

			log.info(".... Valida la informacion de duplicidad en Spei IzelSti ....");
			RespuestaSVC respValSpei = InsertarControlDevolucionesImp.procesarValidaSpei(dev.getClave_rastreo());
			if(respValSpei.getErrores().getCodigoError() != 0L){
				log.info(String.format("[CR : %s] ERROR EN LA BUSQUEDA DE LA CLAVE DE RASTREO", dev.getClave_rastreo()));
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s] ERROR EN LA BUSQUEDA DE LA CLAVE DE RASTREO", dev.getClave_rastreo()));
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}
			
			String valStr = (String)respValSpei.getBody().getValor("DEVOLUCION");
			if(!"NO_EXISTE".equals(Comun._T(valStr))){
				log.info(String.format("[CR : %s] CLAVE DE RASTREO YA EXISTE EN SPEI STI", dev.getClave_rastreo()));
				respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_DEVOLUCION_EXISTENTE, String.format("[CR : %s] CLAVE DE RASTREO YA EXISTE EN SPEI STI", dev.getClave_rastreo()));
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				
			}
			
			log.info(String.format("** <<---------------------------------- PASA VALIDACION **"));

			insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), dev.getObservaciones(), 0, strDecodificado);

			/* APLICA LA DEVOLUCION */
			try {

				/* VERIFICA QUE LA CUENTA SEA O NO SEA REFERENCIADA */
				CuentaReferenciadaOBJ objRef = null;
				String clabeDeposito = null;
				RespuestaSVC respRef = DevolucionCuentaCeroImp.getCuentaReferenciada(Comun._TX(dev.getCuenta_ordenante()));
				if(respRef.getErrores().getCodigoError() == 0L){
					objRef = (CuentaReferenciadaOBJ)respRef.getBody().getValor("CUENTA");
					clabeDeposito = Comun._TX(objRef.getClabe_interbancaria());	// VALOR DE LA CUENTA PADRE EN CASO DE QUE SEA TIPO BLU
				}

				if(objRef == null || "".equals(Comun._TX(clabeDeposito))){
					clabeDeposito = Comun._TX(dev.getCuenta_ordenante());		// SI NO ES TIPO REFERENCIADO ASIGNA EL VALOR ORIGINAL
				}

				RespuestaSVC respCta = BuscaClabeParticipanteImp.procesar(clabeDeposito);
				if(respCta.getErrores().getCodigoError() != 0L){
					insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), respCta.getErrores().getDescError(), 3, strDecodificado);
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, respCta.getErrores().getDescError());
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}

				CuentaOBJ cta = (CuentaOBJ)respCta.getBody().getValor("CUENTA");
				if(cta == null){
					insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), "NO SE ENCONTRO LA CUENTA ORDENANTE", 3, strDecodificado);
					respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("CLAVE NO ENCONTRADA", clabeDeposito));
					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}

				if("AHOPRO".equals(cta.getBase())){	/* CUENTAS DE AHORRO CORE PROCREA */
					/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
					if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEPOSITO_AHORRO), "TRX_DEPOSITO_AHORRO: " + Comun._T(dev.getCuenta_ordenante()))){
						respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("VALIDA INCORRECTA TRX_DEPOSITO_AHORRO", (dev.getCuenta_ordenante())));
						return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
					}


					//String concepto = String.format("DEVOLUCION PROCREA : CLAVE RASTREO [%s], %s", dev.getClave_rastreo(), dev.getConcepto_pago());
					SimpleDateFormat sdf = new SimpleDateFormat(_FORMATO_FECHA_);
					String fechaTransaccion = sdf.format(Calendar.getInstance().getTime());

					TransaccionCuentasProcreaReq trans = new TransaccionCuentasProcreaReq();
					trans.setCuentaOri(cta.getCuenta());
					trans.setCuentaDes(cta.getCuenta());
					trans.setFecha(fechaTransaccion);
					trans.setMonto(dev.getMonto());
					trans.setUsuarioId(Comun._T(Constantes.USUARIO_ID));
					trans.setMovimientoId("0");
					trans.setObservaciones(concepto);
					trans.setTipoMovto(Comun._T(Constantes.ID_SPEI_DEVOLUCION_PR));

					/*  REESTABLECER POSTERIORMENTE */
					respuestaSvc = DevolucionCuentaProcreaImp.procesar(trans);
					if(respuestaSvc.getErrores().getCodigoError() != 0L){
						insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), respuestaSvc.getErrores().getDescError(), 3, strDecodificado);
					}else{
						insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), null, 2, strDecodificado);
					}

					return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}else{	
					/* VALIDA QUE NO ESTE DUPLICADO DE LADO DE CERO */
					log.info(".... Valida la informacion de duplicidad en Cero Transacciones ....");
					RespuestaSVC respValCero = InsertarControlDevolucionesImp.procesarValidaSpeiCero(Comun._T(dev.getClave_rastreo()), "DEV_RET_TRASPASO");
					if(respValCero.getErrores().getCodigoError() != 0L){
						log.info(String.format("[CR : %s] ERROR EN LA BUSQUEDA DE LA CLAVE DE RASTREO EN CERO", dev.getClave_rastreo()));
						respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s] ERROR EN LA BUSQUEDA DE LA CLAVE DE RASTREO EN CERO", dev.getClave_rastreo()));
						return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
					}
					
					String valCeroStr = (String)respValCero.getBody().getValor("DEVOLUCION");
					System.out.println("VALIDACION DEV :: " + valCeroStr);
					if("OK".equals(valCeroStr)){
						log.info(String.format("[CR : %s] CLAVE DE RASTREO YA EXISTE EN CERO TRANSACCIONES", dev.getClave_rastreo()));
						insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), null, 5, strDecodificado);
						respuestaSvc.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, String.format("[CR : %s] CLAVE DE RASTREO YA EXISTE EN CERO TRANSACCIONES", dev.getClave_rastreo()));
						return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
					}
					System.out.println("SE HARCE DEVOLUCION");
					
					/* VERIFICA QUE LA CUENTA ESTE VIGENTE */
					if("VIG".equals(Comun._T(cta.getEstatusClave())) && !"BLOQUEADO".equals(Comun._T(cta.getBloqueado()))){

						/* VERIFICA SI LA CUENTA ES CON PLASTICO, SI = ENVIO DEPOSITO A PROVEEDOR SISCOOP */
						if("SI".equals(Comun._T(cta.getCon_plastico())) && !"".equals(Comun._T(cta.getPan()))){

							/* LLENA LOS PARAMETROS PARA SER INVOCADOS EN EL DEPOSITO DE MULE DEL PROVEEDOR PARA PLASTICOS*/
							PlaHeaderWS plaHeader = new PlaHeaderWS();

							plaHeader.setIdUsuario(Comun._L(Constantes.USUARIO_ID));
							plaHeader.setIdCanalAtencion(Comun._L(Constantes.CANAL_ID));
							plaHeader.setIdSucursal(Comun._L(Constantes.SUCURSAL_ID));
							plaHeader.setIdCliente(Comun._L(cta.getPersonaId()));
							plaHeader.setIdCuenta(Comun._L(cta.getId()));
							plaHeader.setIpHost(Comun._T(Constantes.HOST_ID));
							plaHeader.setIdPan(Comun._L(cta.getPan_id()));

							DatosPlasticoREQ req = new DatosPlasticoREQ();
							req.setPlastico(Comun._T(cta.getPan()));
							req.setMonto(Comun._D(dev.getMonto()));

							/* REESTABLECER POSTERIORMENTE */
							RespuestaSVC respPla = ProcesoDepositarPlastico.Depositar(plaHeader, req);
							if(respPla.getErrores().getCodigoError() != 0L){
								String error = String.format("[ CUENTA CON PLASTICO] %s", respPla.getErrores().getDescError());
								insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), error, 3, strDecodificado);
							}else{
								DatosPlasticoOBJ obj = (DatosPlasticoOBJ)respPla.getBody().getValor("DATOS_PLASTICO_OBJ");
								log.info("CODIGO         : " + obj.getCodigo());
								log.info("AUTORIZACION   : " + obj.getAutorizacion());
								log.info("DESCRIPCION    : " + obj.getDescripcion());
								log.info("BALANCE        : " + obj.getBalance());
								log.info("BALANCE ACTUAL : " + obj.getBalanceActual());
								insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), null, 1, strDecodificado);

								respuestaSvc = enviaDepositoLocal(dev, clabeDeposito, concepto);
								if(respuestaSvc.getErrores().getCodigoError() == 0L){
									insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), null, 2, strDecodificado);
								}
							}

						}else{
							/* REESTABLECER POSTERIORMENTE */
							respuestaSvc = enviaDepositoLocal(dev, clabeDeposito, concepto);
							if(respuestaSvc.getErrores().getCodigoError() != 0L){
								insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), respuestaSvc.getErrores().getDescError(), 3, strDecodificado);
							}else{
								insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), null, 2, strDecodificado);
							}

							return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
						}
					}else{
						insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), "CUENTA BLOQUEADA/CANCELADA", 3, strDecodificado);
					}
				}
				return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}catch(Exception ex){
				ex.printStackTrace();
				insertarControlDevoluciones(dev.getStatus_operacion(), dev.getClave_rastreo(), dev.getCuenta_ordenante(), dev.getId_spei_outgoing(), dev.getMonto(), ex.getMessage(), 3, strDecodificado);
			}

		} /* fin de synchronized (this) */
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
	}

	private static RespuestaSVC enviaDepositoLocal(OutgoingJdbcDevOBJ devLocal, String clabeDeposito, String concepto){
		RespuestaSVC respuestaSvc = new RespuestaSVC();

		try{
			/* ENVIA A LA BD DE CERO YA QUE NO CONTIENE PLASTICOS */
			TransaccionCuentasReq trans = new TransaccionCuentasReq();
			trans.setCuentaClabe(devLocal.getCuenta_ordenante());
			trans.setMonto(new BigDecimal(devLocal.getMonto()));
			trans.setIdentificador("CERO");
			trans.setClaveRastreo(devLocal.getClave_rastreo());
			trans.setConcepto(concepto);
			trans.setActualizaSaldo(1);

			respuestaSvc = DevolucionCuentaCeroImp.procesar(trans, clabeDeposito);
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		return respuestaSvc;
	}

	/* 0 INICIO DE PROCESO DE DEVOLUCION, 1 = DEVOLUCION PROVEEDOR, 2 = DEVOLUCION INSERTADA Y SATISFACTORIA,  3 = ERROR, NO SE DEVOLVIO */
	private static void insertarControlDevoluciones(Integer estatusOperacion, String claveRastreo, String cuentaOrdenante, Long speiOutgoingId, 
			String monto, String descripcion, Integer control, String cadena){
		try{
			String observaciones = String.format(_OBSERVACIONES[control], Comun._T(cuentaOrdenante), Comun._T(monto), Comun._T(descripcion));

			log.info(observaciones);
			RespuestaSVC respInsertarDev = InsertarControlDevolucionesImp.procesar(estatusOperacion, claveRastreo, speiOutgoingId, monto, observaciones, control, cadena, cuentaOrdenante);
			if(respInsertarDev.getErrores().getCodigoError() != 0L){
				log.info(respInsertarDev.getErrores().getDescError());
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}

	@SuppressWarnings("unused")
	private static Boolean validaFechaOperacion(String fechaOperacion, Date fechaTransaccion){
		Boolean valida = false;

		if("".equals(Comun._T(fechaOperacion))) return valida;
		if(fechaTransaccion == null) return valida;

		Calendar calFechaOperacionActual = Calendar.getInstance();
		Calendar calFechaOperacion = Calendar.getInstance();
		Calendar calFechaTransaccion = Calendar.getInstance();
		try{
			Integer diasOperacion = Comun._I(Constantes.DIAS_OPERACION);
			diasOperacion = diasOperacion * -1;

			calFechaOperacionActual.setTime(new SimpleDateFormat(_FORMATO_FECHA_OPERACION_).parse(fechaOperacion));
			calFechaOperacion.setTime(new SimpleDateFormat(_FORMATO_FECHA_OPERACION_).parse(fechaOperacion));
			calFechaTransaccion.setTime(fechaTransaccion);
			calFechaOperacion.add(Calendar.DATE, diasOperacion);

			if(calFechaTransaccion.equals(calFechaOperacion) || calFechaTransaccion.after(calFechaOperacion)){
				valida = true;
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		log.info(String.format("\nFECHA OPERACION : %s\nFECHA VALIDAD DESDE : %s\nFECHA TRANSACCION : %s\nVALIDA : %s", 
				new SimpleDateFormat(_FORMATO_FECHA_OPERACION_).format(calFechaOperacionActual.getTime()),
				new SimpleDateFormat(_FORMATO_FECHA_OPERACION_).format(calFechaOperacion.getTime()), 
				new SimpleDateFormat(_FORMATO_FECHA_OPERACION_).format(calFechaTransaccion.getTime()),
				valida ? "PASA" : "NO PASA"));
		return valida;
	}

	private void despliegaParametros(OutgoingJdbcDevOBJ obj){
		log.info("ID SPEI OUTGOING		: " + obj.getId_spei_outgoing());
		log.info("CLAVE RASTREO			: " + obj.getClave_rastreo());
		log.info("NOMBRE ORDENANTE		: " + obj.getNombre_ordenante());
		log.info("CUENTA ORDENANTE		: " + obj.getCuenta_ordenante());
		log.info("NOMBRE BENEFICIARIO	: " + obj.getNombre_beneficiario());
		log.info("CUENTA BENEFICIARIO	: " + obj.getCuenta_beneficiario());
		log.info("CONCEPTO PAGO			: " + obj.getConcepto_pago());
		log.info("MONTO					: " + obj.getMonto());
		log.info("ID TIPO PAGO			: " + obj.getId_tipo_pago());
		log.info("ID INSTITUCION BEN	: " + obj.getId_institucion_ben());
		log.info("STATUS				: " + obj.getStatus());
		log.info("ENVIO AUTOMATICO		: " + obj.getEnvio_automatico());
		log.info("TIPO OPERACION		: " + obj.getTipo_operacion());
		log.info("STATUS OPERACION		: " + obj.getStatus_operacion());
		log.info("PROCESADO				: " + obj.getProcesado());
		log.info("FECHA CAPTUTURA		: " + obj.getFecha_captura());
		log.info("FH OPERACION			: " + obj.getFh_operacion());
		log.info("INF ADICIONAL			: " + obj.getInf_adicional());
		log.info("OBSERVACIONES			: " + obj.getObservaciones());
		log.info("CORREO ELECTRONICO	: " + obj.getCorreo_electronico());
		log.info("CORE ID				: " + obj.getCore_id());
	}
}

