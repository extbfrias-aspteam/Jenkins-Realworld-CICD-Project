package net.std.cuentas.svc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.servicios.ClaveValorWS;
import net.std.servicios.ProcesoGeneraReferencia;
import net.std.servicios.ProcesoGeneraSolicitante;
import net.std.soap.servicios.ProcesoGeneraClabe;
import net.std.data.CuentaOBJ;
import net.std.data.DatosMatrizRiesgoOBJ;
import net.std.data.DatosPldOBJ;
import net.std.data.DomicilioOBJ;
import net.std.data.SolicitanteOBJ;
import net.std.productos.dao.CatalogosProdAhorroCeroStdDAO;
import net.std.productos.dao.ProductosAhorroCeroStdDAO;
import net.std.request.SolicitanteReq;

public class ProcesoAltaCuentasStd implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoAltaCuentasStd.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static ProductosAhorroCeroStdDAO daoProd = null;
	private static CatalogosProdAhorroCeroStdDAO daoCat = null;
	private static SolicitanteStdDAO daoSol = null;
	
	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoSol = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");
			daoProd = (ProductosAhorroCeroStdDAO)s.getApplicationContext().getBean("ProductosAhorroCeroStdDAO");
			daoCat = (CatalogosProdAhorroCeroStdDAO)s.getApplicationContext().getBean("CatalogosProdAhorroCeroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}
	
	public static RespuestaSVC crearCuentaCeroStd(SolicitanteReq altaSol){
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		if(dao == null || daoProd == null || daoCat == null || daoSol == null) initialized();
		/* PERSISTENCIA NULA DEL DAO */
		if(dao == null || daoProd == null || daoCat == null || daoSol == null){
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
			return respuestaSvc;
		}
		
		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CREAR_CUENTAS_AHORRO), "TRX_CREAR_CUENTAS_AHORRO: " + Comun._T(altaSol.getSolicitante().getNombreCompleto()))){
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_PERMISO));
			return respuestaSvc;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		try{
			String valida = validaParams(altaSol);
			if(valida != null){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida));
				return respuestaSvc;
			}
			
			SolicitanteOBJ solicitante = altaSol.getSolicitante();
			DomicilioOBJ domicilio = altaSol.getDomicilio();
			CuentaOBJ cuenta = altaSol.getCuenta();
			DatosPldOBJ pld = altaSol.getPld();
			List<DatosMatrizRiesgoOBJ> lstMatrizRiesgo = altaSol.getLstMatriz();
			
			RespuestaSVC respEst = ClaveValorWS.getEstatus(Comun._T(Constantes.ALTA_ID));
			Integer estatusID = respEst.getErrores().getCodigoError() == 0 ? Comun._I(respEst.getBody().getValor("ID")) : 0;
			
			RespuestaSVC respEstAh = dao.estatusAhorroDao(Comun._T(Constantes.ESTATUS_CUENTA_ACTIVA));
			Integer estatusAhID = respEstAh.getErrores().getCodigoError() == 0 ? Comun._I(respEstAh.getBody().getValor("ID")) : 0;
			
			RespuestaSVC respEstSegAh = dao.estatusAhorroDao(Comun._T(Constantes.ESTATUS_CUENTA_SEGUIMIENTO));
			Integer estatusAhSegID = respEstSegAh.getErrores().getCodigoError() == 0 ? Comun._I(respEstAh.getBody().getValor("ID")) : 0;
			
			RespuestaSVC respEntero = daoCat.getComoSeEnteroStdDao(Comun._T(Constantes.COMO_ENTERO_OBS));
			Integer enteroID = respEntero.getErrores().getCodigoError() == 0 ? Comun._I(respEntero.getBody().getValor("ID")) : 0;
			
			RespuestaSVC respMoneda = daoCat.claveValorStdDao("MONEDA_ID", Comun._T(Constantes.MONEDA_ID));
			Integer monedaID = respEntero.getErrores().getCodigoError() == 0 ? Comun._I(respMoneda.getBody().getValor("ID")) : 0;
			
			/* paso X0. VERIFICA QUE EL PRODUCTO EXISTA */
			if("CUENTA_FINAL".equals(Comun._T(altaSol.getIdentificador()))){
				if(!"LINEA_BLU_FINAL".equals(Comun._T(cuenta.getProductoAhorro()))){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO,  Errores.desc(Errores.ERROR_PRODUCTO_NO_EXISTE, cuenta.getProductoAhorro()));
					return respuestaSvc;
				}
			}else{
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO,  Errores.desc(Errores.ERROR_PRODUCTO_NO_EXISTE, cuenta.getProductoAhorro()));
				return respuestaSvc;
			}
			
			RespuestaSVC respProd = daoProd.leerProductosCeroStdDao(cuenta.getProductoAhorro(), Comun._T(Constantes.ALTA_ID));
			if(respProd.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO,  Errores.desc(Errores.ERROR_PRODUCTO_NO_EXISTE, respProd.getErrores().getDescError()));
				return respuestaSvc;
			}
			
			Integer productoAhorroId = Comun._I(respProd.getBody().getValor("ID"));
			cuenta.setProductoAhorroId(productoAhorroId);
			
			/* paso X1. VERIFICA QUE LA CUENTA NO EXISTA TANTO EJE COMO TITULAR*/
			if("CUENTA_FINAL".equals(Comun._T(altaSol.getIdentificador()))){
				if("".equals(Comun._T(altaSol.getCuenta_clabe_eje()))){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CUENTA_FINAL, Comun._T(altaSol.getCuenta_clabe_eje())));
					return respuestaSvc;
				}
				
				RespuestaSVC respVerEje = dao.verificaEjeAhorroStdDao(altaSol.getCuenta_clabe_eje());
				if(respVerEje.getErrores().getCodigoError() == 0){
					if(!Comun._T(Constantes.ESTATUS_CUENTA_ACTIVA).equals(Comun._T(respVerEje.getBody().getValor("CVE_ESTATUS")))){
						respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, (Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVA, 
		                        String.format("%s - %s", Comun._T(respVerEje.getBody().getValor("ESTATUS")), Comun._T(altaSol.getCuenta_clabe_eje())))));
						return respuestaSvc;
					}
				}else{
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CUENTA_EJE_NO_EXISTE, Comun._T(altaSol.getCuenta_clabe_eje())));
					return respuestaSvc;
					
				}
			}else{
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CUENTA_DESCONOCIDA, Comun._T(altaSol.getCuenta_clabe_eje())));
				return respuestaSvc;
			}
			
			/* PASO 0. GENERA ALTA DE CLIENTE EN PERSONAL */
			String clienteID = null;
			RespuestaSVC respBuscaSol = daoSol.BuscarSolicitanteDao(altaSol.getSolicitante());
			if(respBuscaSol.getErrores().getCodigoError() == 0){
				clienteID = Comun._T(respBuscaSol.getBody().getValor("CLIENTE_ID"));
			}else{
				RespuestaSVC respSolicitante = ProcesoGeneraSolicitante.procesar(solicitante, domicilio);
				if(respSolicitante.getErrores().getCodigoError() != 0){
					respuestaSvc.getErrores().addCodigo(null, respSolicitante.getErrores().getCodigoError(), respSolicitante.getErrores().getDescError());
					return respuestaSvc;
				}
				clienteID = Comun._T(respSolicitante.getBody().getValor("CLIENTE_ID"));
			}
	
			if("".equals(clienteID)){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_SOLICITANTE, altaSol.getSolicitante().getNombreCompleto()));
				return respuestaSvc;
			}
			
			
			/* PASO 1. GENERA CUENTA / REFERENCIA  */
			RespuestaSVC respReferencia = ProcesoGeneraReferencia.procesar(cuenta.getProductoAhorroId(), Comun._I(Constantes.TIPO_REFERENCIA_BLU));
			if(respReferencia.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CREAR_REFERENCIA, respReferencia.getErrores().getDescError()));
				return respuestaSvc;
			}
			String referencia =  Comun._T(respReferencia.getBody().getValor("REFERENCIA"));
			
			/* PASO 2. GENERAR CUENTA INTERBANCARIA /  CLABE */
			RespuestaSVC respClabe = ProcesoGeneraClabe.procesar(cuenta.getProductoAhorroId(), referencia, Comun._T(Constantes.APLICACION_CERO_ID));
			if(respClabe.getErrores().getCodigoError() != 0){
				log.info(respClabe.getErrores().getDescError());
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CREAR_CLABE, respClabe.getErrores().getCodigoError()));
				return respuestaSvc;
			}
			String clabeInterbancaria = Comun._T(respClabe.getBody().getValor("CLABE"));
			
			
			cuenta.setCuenta(referencia);
			cuenta.setEstatusId("CUENTA_FINAL".equals(Comun._T(altaSol.getIdentificador())) ? estatusAhID : estatusAhSegID);
			cuenta.setPersonaId(clienteID);
			cuenta.setProductoAhorroId(productoAhorroId);
			cuenta.setMontoApertura(Comun._D(Constantes.MONTO_APERTURA));
			cuenta.setSucursalId(Comun._I(Constantes.SUCURSAL_ID));
			cuenta.setRendimiento(Comun._D(Constantes.RENDIMIENTO));
			cuenta.setMonedaId(monedaID);

			cuenta.setGatNominal(Comun._D(Constantes.GAT_NOMINAL));
			cuenta.setGatReal(Comun._D(Constantes.GAT_REAL));
			cuenta.setAsesorId(Comun._I(Constantes.ASESOR_ID));
			
			cuenta.setComoEnteroId(enteroID);
			cuenta.setComoEnteroObs(Comun._T(Constantes.COMO_ENTERO_OBS));
			cuenta.setClabeInterbancaria(clabeInterbancaria);
			cuenta.setReferencia(referencia); /* MISMA QUE CLAVE */
			cuenta.setFechaApertura(Calendar.getInstance().getTime());
			cuenta.setTipoCliente(Comun._T(altaSol.getIdentificador()));
			
			/* PASO 3. GENERA LA CUENTA */
			//RespuestaSVC respCuenta = ProcesoGeneraCuenta.procesar(cuenta);
			
			RespuestaSVC respCuenta = dao.crearCuentaStdDao(cuenta, Comun._I(Constantes.USUARIO_ID));
			if(respCuenta.getErrores().getCodigoError() != 0){
				respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CREAR_CUENTA, respCuenta.getErrores().getCodigoError()));
				return respuestaSvc;
			}
			Integer cuentaID = Comun._I(respCuenta.getBody().getValor("CUENTA_ID"));
			cuenta.setId(cuentaID);
			
			/* paso 4. ACTUALIZA CUENTA EJE SI ES CUENTA FINAL*/
			if("CUENTA_FINAL".equals(Comun._T(altaSol.getIdentificador()))){
				RespuestaSVC respActEje = dao.actualizarEjeAhorroStdDao(Comun._T(cuenta.getCuenta()),
						                                                Comun._T(altaSol.getCuenta_clabe_eje()));
				mapResultado.put("CUENTA_EJE", respActEje.getErrores().getCodigoError() == 0 ? "ACTUALIZADA CUENTA FINAL" : respActEje.getErrores().getDescError());
			}else{
				mapResultado.put("CUENTA_EJE", "CUENTA EJE OK");
			}

			/* PASO 5. GENERA LOS CONCEPTOS */
			RespuestaSVC respConceptos = dao.copiarByProductoID2CuentaIDDao(
																			cuenta.getId(), 
																			Comun._I(Constantes.USUARIO_ID), 
																			cuenta.getProductoAhorroId(), 
																			Comun._T(Constantes.ALTA_ID)); 
		
			/* PASO 6. POSICION GLOBAL */
			RespuestaSVC resPospGlobal = dao.crearPosicionGlobalStdDao(cuenta);
			
			/* PASO 7. GENERA CONCEPTOS PLD */ 
			if(pld != null){
				pld.setCuenta_id(cuenta.getId());
				pld.setEstatus_id(estatusID);
			}
			RespuestaSVC respPld = dao.crearCuentasPldStdDao(pld);
			
			/* PASO 8 GENERA MATRIZ DE RIESGO */
			RespuestaSVC respMR = dao.crearMatrizRiesgoPldStdDao(lstMatrizRiesgo, cuenta, Comun._I(Constantes.USUARIO_ID));
			
			mapResultado.put("CUENTA_ID", Comun._T(cuenta.getId()));
			mapResultado.put("NOMBRE", Comun._T(solicitante.getNombreCompleto()));
			mapResultado.put("RFC", Comun._T(solicitante.getRfc()));
			mapResultado.put("CURP", Comun._T(solicitante.getCurp()));
			mapResultado.put("CUENTA", Comun._T(cuenta.getCuenta()));
			mapResultado.put("CUENTA_CLABE", Comun._T(cuenta.getClabeInterbancaria()));
			mapResultado.put("ESTATUS", Comun._T(Constantes.ESTATUS_CUENTA_ACTIVA));
			mapResultado.put("FECHA_APERTURA", new SimpleDateFormat(_FECHA_FORMATO_).format(Calendar.getInstance().getTime()));
			mapResultado.put("CONCEPTOS", respConceptos.getErrores().getCodigoError() == 0 ? "ACTUALIZADOS" : respConceptos.getErrores().getDescError());
			mapResultado.put("CONCEPTOS PLD", respPld.getErrores().getCodigoError() == 0 ? "ACTUALIZADOS" : respPld.getErrores().getDescError());
			mapResultado.put("POSICION GLOBAL", resPospGlobal.getErrores().getCodigoError() == 0 ? "ACTUALIZADOS" : resPospGlobal.getErrores().getDescError());
			mapResultado.put("MATRIZ RIESGO PLD", respMR.getErrores().getCodigoError() == 0 ? "ACTUALIZADOS" : respMR.getErrores().getDescError());
			
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}

	private static String validaParams(SolicitanteReq sol){
		String valida = null;
		if(sol == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(sol.getSolicitante() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS SOLICITANTE");
		if(sol.getCuenta() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS CUENTA");
		if(sol.getDomicilio() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DOMICILIO");
		if(sol.getPld() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS PLD");
		if(sol.getLstMatriz() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS MATRIZ DE RIESGO");
		
		if("".equals(Comun._T(sol.getSolicitante().getNombreCompleto())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NOMBRE COMPLETO");
		if("".equals(Comun._T(sol.getSolicitante().getNombre())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NOMBRE");
		if("".equals(Comun._T(sol.getSolicitante().getCelular())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CELULAR");

		if("F".equals(Comun._T(sol.getSolicitante().getTipoPersona()))){
			if("".equals(Comun._T(sol.getSolicitante().getFechaNacimiento())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "FECHA NACIMIENTO");
			if("".equals(Comun._T(sol.getSolicitante().getCurp())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CURP");
		}
		//if("".equals(Comun._T(sol.getSolicitante().getRfc())))  return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "RFC");
		
		/* VALORES NULOS, LOS COLOCA EN VACIO */
		sol.getSolicitante().setApellidoPaterno(Comun._T(sol.getSolicitante().getApellidoPaterno()));
		sol.getSolicitante().setApellidoMaterno(Comun._T(sol.getSolicitante().getApellidoMaterno()));
		
		return valida;
	}
}

