package net.std.implementacion;

import java.io.Serializable;
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
import net.std.constantes.Errores;
import net.std.dao.DevolucionesSpeiStdDAO;


public class InsertarControlDevolucionesImp implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(InsertarControlDevolucionesImp.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static DevolucionesSpeiStdDAO dao = null;

	private static Boolean initialized() {
		Boolean valida = true;
		if(dao != null) return valida;
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (DevolucionesSpeiStdDAO)s.getApplicationContext().getBean("DevolucionesSpeiStdDAO");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(dao == null) valida = false;
		return valida;
	}
	
	@SuppressWarnings("unused")
	public static RespuestaSVC procesar(Integer estatusOperacion, String claveRastreo, Long speiOutgoingId, String monto, String descripcion, Integer control, String cadena, String cuentaOrdenante){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Map<String, String> mapResultado = new HashMap<>();
		String autorizacion = null;
		HeaderWS header;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			return Comun.RespError(Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
		}
		
		try{
			respuestaSvc = dao.insertarDevolucionSpeiStdDao(estatusOperacion, claveRastreo, speiOutgoingId, monto, descripcion, control, cadena, cuentaOrdenante);
			if(respuestaSvc.getErrores().getCodigoError() != 0L){
				log.info(respuestaSvc.getErrores().getDescError());
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return Comun.RespError(Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
	
	public static RespuestaSVC procesarValidaSpei(String claveRastreo){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, "ERROR INICIAR CONEXION BD"));
			return respuestaSvc;
		}
		
		try{
			respuestaSvc = dao.validarDevolucionStdDao(Comun._T(claveRastreo));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, ex.getMessage()));
			return respuestaSvc;
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
	
	public static RespuestaSVC procesarValidaSpeiCero(String claveRastreo, String tipoTransaccion){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, "ERROR INICIAR CONEXION BD"));
			return respuestaSvc;
		}
		
		try{
			respuestaSvc = dao.validarDevolucionCeroStdDao(Comun._T(claveRastreo), Comun._T(tipoTransaccion));
		}catch(Exception ex){
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, ex.getMessage()));
			return respuestaSvc;
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
}

