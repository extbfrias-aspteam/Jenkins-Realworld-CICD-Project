package net.std.implementacion;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.dao.SolicitanteStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
//import net.std.request.CanalesReq;
import net.std.request.TransaccionCuentasReq;


public class DevolucionCuentaCeroImp implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DevolucionCuentaCeroImp.class);
	private static final String _FECHA_FORMATO_ = "yyyy-MM-dd";
	private static final String _FECHA_AUT_ = "yyyyMMddHHmmssSSS";
	private static final String _MOVTO_ = "DEPOSITO";

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;
	private static CuentasReferenciadasStdDAO daoRef = null; 

	private static Boolean initialized() {
		Boolean valida = true;
		if(dao != null && daoAho != null && daoRef != null) return valida;
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(dao == null || daoAho == null || daoRef == null) valida = false;
		return valida;
	}
	
	@SuppressWarnings("unused")
	public static RespuestaSVC procesar(TransaccionCuentasReq trans, String clabeDeposito){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Map<String, String> mapResultado = new HashMap<>();
		String autorizacion = null;
		HeaderWS header;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		/* VALIDA PARAMETROS DE ENTRADA */
		String valida = validaParams(trans);
		if(valida != null){
			return Comun.RespError(Errores.ERROR_CAMPOS_REQUERIDOS, Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida));
		}
		
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			return Comun.RespError(Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
		}
		
		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEPOSITO_AHORRO), "TRX_DEPOSITO_AHORRO: " + Comun._T(trans.getCuentaClabe()))){
			return Comun.RespError(Errores.ERROR_PERMISO, Errores.desc(Errores.ERROR_PERMISO));
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
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(clabeDeposito));
			if(respCtaDep.getErrores().getCodigoError() != 0){
				return Comun.RespError(Errores.ERROR_CUENTA, Errores.desc(Errores.ERROR_CUENTA, trans.getCuentaClabe()));
			}
			
			CuentaOBJ ctaDeposito = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
			ctaDeposito.setCuenta_referencia(trans.getCuentaClabe());
			
			
			/* OBTIENE UN NUMERO DE AUTORIZACION */
			autorizacion = getAutorizacion(trans.getIdentificador());
			if("".equals(Comun._T(autorizacion))){
				return Comun.RespError(Errores.ERROR_NUMERO_AUTORIZACION, Errores.desc(Errores.ERROR_NUMERO_AUTORIZACION, trans.getCuentaClabe()));
			}
			
			
			RespuestaSVC respDeposito = dao.depositarStdDao(ctaDeposito, "DEV_RET_TRASPASO", new Date(), trans.getMonto(), 
						                                        Comun._T(trans.getConcepto()), autorizacion, header, trans.getClaveRastreo());
			if(respDeposito.getErrores().getCodigoError() != 0){
				RespuestaSVC respRB = dao.rollBackStdDao(autorizacion);
				return Comun.RespError(Errores.ERROR_DEPOSITO, Errores.desc(Errores.ERROR_DEPOSITO, trans.getCuentaClabe()));
			}
			
			/* ACTUALIZA EL SALDO */
			RespuestaSVC resActSaldo = dao.actualizaSaldoStdDao(ctaDeposito, trans.getActualizaSaldo(), _MOVTO_, 
					                                            Comun._I(Comun._T(header.getIdUsuario())));
			
			mapResultado.put("REFERENCIA", Comun._T(ctaDeposito.getReferencia()));
			mapResultado.put("PRODUCTO_ID",  Comun._T(ctaDeposito.getProductoAhorroId()));
			
			mapResultado.put("AUTORIZACION", autorizacion);
			mapResultado.put("TIPO_CLIENTE", Comun._T(ctaDeposito.getTipoCliente()));
			mapResultado.put("CUENTA_DEVOLUCION", Comun._T(ctaDeposito.getCuenta()));
			mapResultado.put("MONTO_TRANSACCION", Comun._T(trans.getMonto()));
			mapResultado.put("FECHA_TRANSACCION", new SimpleDateFormat(_FECHA_FORMATO_).format(Calendar.getInstance().getTime()));
			mapResultado.put("ESTATUS", "OK");
			
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
		}catch(Exception ex){
			ex.printStackTrace();
			return Comun.RespError(Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}

	
	private static String validaParams(TransaccionCuentasReq obj){
		String valida = null;
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(obj.getCuentaClabe() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS DE LA CLABE INTERBANCARIA");
		if(obj.getMonto() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN MONTO PROPORCIONADO");
		if(obj.getMonto().doubleValue() <= 0.00d) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MONTO ES MENOR A CERO");
		if(obj.getIdentificador() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN TIPO DE IDENTIFICADOR");
		return valida;
	}
	
	private static String getAutorizacion(String identificador){
		SimpleDateFormat sdf = new SimpleDateFormat(_FECHA_AUT_);
		int random = (int )(Math.random() * 50 + 1);
		String autorizacion = String.format("%s-%s-%s", identificador, Comun._T(random), sdf.format(Calendar.getInstance().getTime()));
		return autorizacion;
	}
	
	public static RespuestaSVC getCuentaReferenciada(String cuentaClabe){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		try{
			/* VALIDA LA CONEXION A LOS BEANS DAO */
			if(!initialized()) {
				return Comun.RespError(Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
			}
			
			respuestaSvc = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(cuentaClabe), Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA));
		}
		return respuestaSvc;
	}
}

