package net.std.dao;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.RespuestaSVC;
import net.std.catalogos.svc.CatStdActividadSvc;
import net.std.constantes.Comun;
import net.std.constantes.Errores;


@SuppressWarnings("unused")
public class BitacoraLogStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(BitacoraLogStdDAO.class);
	
	private JdbcTemplate jdbcTemplate;
	private String bitacoraLogStd;
	private String bitacoraTraStd;
	
	public RespuestaSVC insertarbitacoraLogDao(String proceso, String dato, String observaciones, Integer usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int rows = jdbcTemplate.update(bitacoraLogStd, proceso, dato, observaciones, usuarioID);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_BITACORA, Errores.desc(Errores.ERROR_BITACORA));

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertarbitacoraTraDao(Map<String, String> map) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int rows = jdbcTemplate.update(bitacoraTraStd, 
					                       Comun._I(map.get("TIPO_TRANSACCION_ID")),
					                       Comun._I(map.get("PROCESO_ID")),
					                       Comun._T(map.get("PROCESO")),
					                       Comun._T(map.get("CUENTA_ORD")),
					                       Comun._T(map.get("CUENTA_DES")),
					                       Comun._T(map.get("CLAVE_RASTREO")),
					                       Comun._I(map.get("ESTATUS_OPERACION_ID")),
					                       Comun._D(map.get("MONTO")),
					                       Comun._T(map.get("OBSERVACIONES")),
					                       Comun._T(map.get("DATOS")),
					                       Comun._I(map.get("USUARIO_CREACION")));
					                       
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_BITACORA_TRANSACCIONES, Errores.desc(Errores.ERROR_BITACORA_TRANSACCIONES));

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getBitacoraLogStd() {
		return bitacoraLogStd;
	}

	public void setBitacoraLogStd(String bitacoraLogStd) {
		this.bitacoraLogStd = bitacoraLogStd;
	}

	public String getBitacoraTraStd() {
		return bitacoraTraStd;
	}

	public void setBitacoraTraStd(String bitacoraTraStd) {
		this.bitacoraTraStd = bitacoraTraStd;
	}
}

