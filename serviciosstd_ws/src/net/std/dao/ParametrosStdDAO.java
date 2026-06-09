package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;

public class ParametrosStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbcTemplateSti;
	private String ParametroPStd;
	private String insertarParametroPStd;
	private String actualizarParametroPStd;
	
	public RespuestaSVC insertarParametroPStdDao(String clave, String valor, String estatus, Long usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplateSti.update(insertarParametroPStd, clave, valor, estatus, usuarioID);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS INSERTADOS");

		} catch (Exception ex) {
			ex.getMessage();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC actualizarParametroPStdDao(String clave, String estatus, Long usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplateSti.update(actualizarParametroPStd, estatus, usuarioID, clave);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");

		} catch (Exception ex) {
			ex.getMessage();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC parametroPStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, Object>> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(ParametroPStd, clave);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, Object> map = new HashMap<>();
					map.put("CLAVE", _T(row.get("clave")));
					map.put("VALOR", _T(row.get("valor")));
					map.put("FECHA", _T(row.get("fecha")));
					map.put("ESTATUS", _T(row.get("estado")));

					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
				respuesta.getBody().addValor("PARAMETRO", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}

	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public String getParametroPStd() {
		return ParametroPStd;
	}

	public void setParametroPStd(String parametroPStd) {
		ParametroPStd = parametroPStd;
	}

	public String getInsertarParametroPStd() {
		return insertarParametroPStd;
	}

	public void setInsertarParametroPStd(String insertarParametroPStd) {
		this.insertarParametroPStd = insertarParametroPStd;
	}

	public String getActualizarParametroPStd() {
		return actualizarParametroPStd;
	}

	public void setActualizarParametroPStd(String actualizarParametroPStd) {
		this.actualizarParametroPStd = actualizarParametroPStd;
	}
	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}
	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}
}

