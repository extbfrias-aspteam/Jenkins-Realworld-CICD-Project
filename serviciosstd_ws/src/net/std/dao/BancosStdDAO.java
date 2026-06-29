package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;

public class BancosStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbcTemplateSti;
	private String leerTipoPagoSpei;
	private String leerInstitucionSpei;
	private String leerInstitucionSpeiByID;
	private String leerTipoCuentaSpei;
	private String leerTipoCuentaSpeiByID;
	private String leerAreaEmiteSpei;
	private String leerInstitucionSpeiDescripcion;
	
	public RespuestaSVC leerTipoPagoSpeiDao(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerTipoPagoSpei, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}

	public RespuestaSVC leerInstitucionSpeiDao(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerInstitucionSpei, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC leerInstitucionSpeiDaoByID(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerInstitucionSpeiByID, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC leerInstitucionSpeiDescripcionDao(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerInstitucionSpeiDescripcion, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	
	public RespuestaSVC leerTipoCuentaSpeiDao(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerTipoCuentaSpei, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC leerTipoCuentaSpeiDaoByID(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerTipoCuentaSpeiByID, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC leerAreaEmiteSpeiDao(String valor) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(leerAreaEmiteSpei, valor);
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
				respuesta.getBody().addValor("CUENTA", dato);
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

	public String getLeerTipoPagoSpei() {
		return leerTipoPagoSpei;
	}

	public void setLeerTipoPagoSpei(String leerTipoPagoSpei) {
		this.leerTipoPagoSpei = leerTipoPagoSpei;
	}

	public String getLeerInstitucionSpei() {
		return leerInstitucionSpei;
	}

	public void setLeerInstitucionSpei(String leerInstitucionSpei) {
		this.leerInstitucionSpei = leerInstitucionSpei;
	}

	public String getLeerTipoCuentaSpei() {
		return leerTipoCuentaSpei;
	}

	public void setLeerTipoCuentaSpei(String leerTipoCuentaSpei) {
		this.leerTipoCuentaSpei = leerTipoCuentaSpei;
	}

	public String getLeerAreaEmiteSpei() {
		return leerAreaEmiteSpei;
	}

	public void setLeerAreaEmiteSpei(String leerAreaEmiteSpei) {
		this.leerAreaEmiteSpei = leerAreaEmiteSpei;
	}

	public String getLeerInstitucionSpeiDescripcion() {
		return leerInstitucionSpeiDescripcion;
	}

	public void setLeerInstitucionSpeiDescripcion(String leerInstitucionSpeiDescripcion) {
		this.leerInstitucionSpeiDescripcion = leerInstitucionSpeiDescripcion;
	}

	public String getLeerInstitucionSpeiByID() {
		return leerInstitucionSpeiByID;
	}

	public void setLeerInstitucionSpeiByID(String leerInstitucionSpeiByID) {
		this.leerInstitucionSpeiByID = leerInstitucionSpeiByID;
	}

	public String getLeerTipoCuentaSpeiByID() {
		return leerTipoCuentaSpeiByID;
	}

	public void setLeerTipoCuentaSpeiByID(String leerTipoCuentaSpeiByID) {
		this.leerTipoCuentaSpeiByID = leerTipoCuentaSpeiByID;
	}
}

