package net.std.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.data.CuentasVolumenOBJ;

public class AltaCuentasVolumenStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AltaCuentasVolumenStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private String READ_AltaCuentaStd;
	private String UPDATE_AltaCuentaStd;
	private String INSERT_AltaCuentaStd;
	
	public RespuestaSVC insertarCuentaMasivaStdDao(CuentasVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_AltaCuentaStd, new String[]{"id"});
			ps.setString(1, obj.getIdentificador());
			ps.setString(2, obj.getProductoAhorro());
			ps.setString(3, obj.getDatosjason());
			ps.setInt(4, Comun._I(Constantes.USUARIO_ID));
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INSERTAR_CUENTA_MASIVA, Errores.desc(Errores.ERROR_INSERTAR_CUENTA_MASIVA));
			}else{
				respuesta.getBody().addValor("CUENTA_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC actualizarCuentaMasivaStdDao(CuentasVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_AltaCuentaStd, obj.getProcesado(), obj.getObservaciones(), Comun._I(Constantes.USUARIO_ID), obj.getId());
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_CUENTA_MASIVA,  Errores.desc(Errores.ERROR_ACTUALIZAR_CUENTA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCuentaMasivaStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentasVolumenOBJ obj = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_AltaCuentaStd);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					obj = new CuentasVolumenOBJ();
					obj.setId(Comun._L(row.get("ID")));
					obj.setIdentificador(_T(row.get("IDENTIFICADOR")));
					obj.setProductoAhorro(_T(row.get("PRODUCTO_AHORRO")));
					obj.setDatosjason(_T(row.get("DATOS_JSON")));
					obj.setFechaRegistro(_DF(row.get("FECHA_REGISTRO")));
					obj.setProcesado(_T(row.get("PROCESADO")));
					obj.setObservaciones(_T(row.get("OBSERVACIONES")));
					break;
				}
			}

			if(obj != null){
				respuesta.getBody().addValor("CUENTA", obj);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_MASIVA,  Errores.desc(Errores.ERROR_LEER_CUENTA_MASIVA));
			}

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
	
	private java.sql.Date _DF(Object obj){
		return obj == null ? null : (obj instanceof java.sql.Date ? (java.sql.Date)obj : null);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getREAD_AltaCuentaStd() {
		return READ_AltaCuentaStd;
	}

	public void setREAD_AltaCuentaStd(String rEAD_AltaCuentaStd) {
		READ_AltaCuentaStd = rEAD_AltaCuentaStd;
	}

	public String getUPDATE_AltaCuentaStd() {
		return UPDATE_AltaCuentaStd;
	}

	public void setUPDATE_AltaCuentaStd(String uPDATE_AltaCuentaStd) {
		UPDATE_AltaCuentaStd = uPDATE_AltaCuentaStd;
	}

	public String getINSERT_AltaCuentaStd() {
		return INSERT_AltaCuentaStd;
	}

	public void setINSERT_AltaCuentaStd(String iNSERT_AltaCuentaStd) {
		INSERT_AltaCuentaStd = iNSERT_AltaCuentaStd;
	}
}

