package net.std.dao;

import java.io.Serializable;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;


@SuppressWarnings("unused")
public class BitacoraStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbcTemplateSti;
	private String insertarBitacoraPStd;
	private String insertSpeiBitacora;

	public RespuestaSVC insertarbitacoraDao(String userID, String logger, String level, String log) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplateSti.update(insertarBitacoraPStd, userID, logger, level, log);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS INSERTADOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public void insertarSpeiBitacoraDao(String descripcion, String clave_rastreo) {	
		try {
			jdbcTemplateSti.update(insertSpeiBitacora, "SERVICIOS_STD_WS", descripcion, clave_rastreo);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getInsertarBitacoraPStd() {
		return insertarBitacoraPStd;
	}

	public void setInsertarBitacoraPStd(String insertarBitacoraPStd) {
		this.insertarBitacoraPStd = insertarBitacoraPStd;
	}

	public String getInsertSpeiBitacora() {
		return insertSpeiBitacora;
	}

	public void setInsertSpeiBitacora(String insertSpeiBitacora) {
		this.insertSpeiBitacora = insertSpeiBitacora;
	}
}

