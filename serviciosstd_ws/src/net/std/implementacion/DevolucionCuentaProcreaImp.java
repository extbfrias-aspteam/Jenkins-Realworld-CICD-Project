package net.std.implementacion;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroProcreaStdDAO;
import net.std.request.TransaccionCuentasProcreaReq;

public class DevolucionCuentaProcreaImp implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DevolucionCuentaProcreaImp.class);

	@Autowired
	private static Apps apps = null;
	private static AhorroProcreaStdDAO dao = null;
	
	
	private static Boolean initialized() {
		Boolean valida = true;
		if(dao != null) return valida;
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroProcreaStdDAO)s.getApplicationContext().getBean("AhorroProcreaStdDAO");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(dao == null) valida = false;
		return valida;
	}

	public static RespuestaSVC procesar(TransaccionCuentasProcreaReq trans){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEVOLUCION_AHORRO), "TRX_DEVOLUCION_AHORRO: " + Comun._T(trans.getCuentaDes()))){
			return Comun.RespError(Errores.ERROR_PERMISO, Errores.desc(Errores.ERROR_PERMISO));
		}
	
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			respuestaSvc = dao.speiDevolucionAhorroPrStdDao(
											Comun._T(trans.getCuentaOri()),
											Comun._T(trans.getCuentaDes()),
											Comun._T(trans.getFecha()),
											Comun._D(trans.getMonto()),
											Comun._L(trans.getUsuarioId()),
											Comun._L(trans.getMovimientoId()),
											Comun._T(trans.getObservaciones()),
											Comun._I(trans.getTipoMovto()));
			
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				return Comun.RespError(Errores.ERROR_DEVOLUCION, respuestaSvc.getErrores().getDescError());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return Comun.RespError(Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
	
	private static String validaParams(TransaccionCuentasProcreaReq obj){
		String valida = null;
		if(obj == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(obj.getCuentaOri() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO DE LA CUENTA ORIGEN");
		if(obj.getCuentaDes() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO DE LA CUENTA DESTINO");
		if(obj.getFecha() == null)  Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO DE FECHA");
		if(obj.getMonto() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO DE MONTO");
		if(obj.getMovimientoId() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO TIPO MOVIMIENTO ID");
		if(obj.getTipoMovto() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO TIPO MOVIMIENTO");
		if(obj.getUsuarioId() == null) return Errores.desc(Errores.ERROR_PARAMETROS, "SIN DATO USUARIO ID");
		return valida;
	}
}

