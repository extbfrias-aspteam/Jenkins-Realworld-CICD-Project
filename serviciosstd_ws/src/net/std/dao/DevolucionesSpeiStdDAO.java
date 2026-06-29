package net.std.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Errores;
import net.std.request.CanalesReq;

@SuppressWarnings("unused")
public class DevolucionesSpeiStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DevolucionesSpeiStdDAO.class);

	private JdbcTemplate jdbcTemplateSti;
	private JdbcTemplate jdbcTemplate;
	private String READ_speiOutgoingStd;
	private String INSERT_speiOutgoingDevueltoStd;
	private String READ_speiOutgoingDevueltoStd;
	private String READ_speiOutgoingDevueltoCeroStd;
	
	public RespuestaSVC insertarDevolucionSpeiStdDao(Integer estatusOperacion, String claveRastreo, Long speiOutgoingId, String monto, String descripcion, 
			                                         Integer control, String cadena, String cuentaOrdenante) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplateSti.update(INSERT_speiOutgoingDevueltoStd, 
					                                                       estatusOperacion,
													                       claveRastreo,
													                       speiOutgoingId,
													                       monto == null ? null : new BigDecimal(Comun._T(monto)), 
													                       descripcion,
													                       control,
													                       cadena,
													                       cuentaOrdenante);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS INSERTADOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		return respuesta;
	}
	
	public RespuestaSVC validarDevolucionStdDao(String claveRastreo) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(READ_speiOutgoingDevueltoStd, claveRastreo);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
			}
			
			if(dato != null){
				respuestaSvc.getBody().addValor("DEVOLUCION", "OK");
			}else{
				respuestaSvc.getBody().addValor("DEVOLUCION", "NO_EXISTE");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, ex.getMessage()));
		}
		
		return respuestaSvc;
	}
	
	
	public RespuestaSVC validarDevolucionCeroStdDao(String claveRastreo, String tipoTransaccion) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplate.queryForList(READ_speiOutgoingDevueltoCeroStd, claveRastreo, tipoTransaccion);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
			}
			
			if(dato != null){
				respuestaSvc.getBody().addValor("DEVOLUCION", "OK");
			}else{
				respuestaSvc.getBody().addValor("DEVOLUCION", "NO_EXISTE");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, ex.getMessage()));
		}
		
		return respuestaSvc;
	}
	
	public RespuestaSVC validarSpeiOutgoingStdDao(String claveRastreo) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplateSti.queryForList(READ_speiOutgoingStd, claveRastreo);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
			}
			
			if(dato != null){
				respuestaSvc.getBody().addValor("SPEI", "OK");
			}else{
				respuestaSvc.getBody().addValor("SPEI", "NO_EXISTE");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", claveRastreo, ex.getMessage()));
		}
		
		return respuestaSvc;
	}
	
	
	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}
	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getREAD_speiOutgoingStd() {
		return READ_speiOutgoingStd;
	}
	
	public void setREAD_speiOutgoingStd(String rEAD_speiOutgoingStd) {
		READ_speiOutgoingStd = rEAD_speiOutgoingStd;
	}
	
	public String getINSERT_speiOutgoingDevueltoStd() {
		return INSERT_speiOutgoingDevueltoStd;
	}
	
	public void setINSERT_speiOutgoingDevueltoStd(String iNSERT_speiOutgoingDevueltoStd) {
		INSERT_speiOutgoingDevueltoStd = iNSERT_speiOutgoingDevueltoStd;
	}

	public String getREAD_speiOutgoingDevueltoStd() {
		return READ_speiOutgoingDevueltoStd;
	}

	public void setREAD_speiOutgoingDevueltoStd(String rEAD_speiOutgoingDevueltoStd) {
		READ_speiOutgoingDevueltoStd = rEAD_speiOutgoingDevueltoStd;
	}

	public String getREAD_speiOutgoingDevueltoCeroStd() {
		return READ_speiOutgoingDevueltoCeroStd;
	}

	public void setREAD_speiOutgoingDevueltoCeroStd(String rEAD_speiOutgoingDevueltoCeroStd) {
		READ_speiOutgoingDevueltoCeroStd = rEAD_speiOutgoingDevueltoCeroStd;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}

