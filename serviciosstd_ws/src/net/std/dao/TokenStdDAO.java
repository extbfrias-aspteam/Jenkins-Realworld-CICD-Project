package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;


public class TokenStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;

	private JdbcTemplate jdbcTemplateSti;
	private String TokenPStd;
	private String insertarTokenPStd;
	private String actualizarTokenPStd;
	private String actualizarAllTokenPStd;

	public RespuestaSVC insertarTokenDao(String clave, String valor, String token, String estatus, Long usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();

		try {
			int rows = jdbcTemplateSti.update(insertarTokenPStd, clave, valor, token, estatus, usuarioID);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS INSERTADOS");
			else respuesta.getBody().addValor("RESULTADO", "OK");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}

	public RespuestaSVC actualizarAllTokenDao(Long usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();

		try {
			int rows = jdbcTemplateSti.update(actualizarAllTokenPStd,usuarioID);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");
			else respuesta.getBody().addValor("RESULTADO", "OK");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}

	public RespuestaSVC actualizarTokenDao(String fecha, String estatus, Long usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();

		try {
			Boolean todos = "".equals(Comun._T(fecha)) ? true : false;
			int rows = jdbcTemplateSti.update(actualizarTokenPStd, estatus, usuarioID, todos, fecha);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");
			else respuesta.getBody().addValor("RESULTADO", "OK");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}

	public RespuestaSVC tokenDao(String fecha, String estatus) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String>> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(TokenPStd, estatus);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
			}

			if(dato != null){
				respuesta.getBody().addValor("TOKEN_DIARIO", dato);
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

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getTokenPStd() {
		return TokenPStd;
	}

	public void setTokenPStd(String tokenPStd) {
		TokenPStd = tokenPStd;
	}

	public String getInsertarTokenPStd() {
		return insertarTokenPStd;
	}

	public void setInsertarTokenPStd(String insertarTokenPStd) {
		this.insertarTokenPStd = insertarTokenPStd;
	}

	public String getActualizarTokenPStd() {
		return actualizarTokenPStd;
	}

	public void setActualizarTokenPStd(String actualizarTokenPStd) {
		this.actualizarTokenPStd = actualizarTokenPStd;
	}

	public String getActualizarAllTokenPStd() {
		return actualizarAllTokenPStd;
	}

	public void setActualizarAllTokenPStd(String actualizarAllTokenPStd) {
		this.actualizarAllTokenPStd = actualizarAllTokenPStd;
	}

}

