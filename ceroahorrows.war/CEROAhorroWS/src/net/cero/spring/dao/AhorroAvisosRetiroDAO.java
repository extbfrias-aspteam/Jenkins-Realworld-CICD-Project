package net.cero.spring.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.SecurityFilterChain;

import net.cero.data.AhorroAvisoRetiroOBJ;


public class AhorroAvisosRetiroDAO {	

	public static final Logger LOG = LogManager.getLogger(AhorroAvisosRetiroDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String obtenerAvisoRetiroById;
	private String actualizaAvisoRetiro;
	
	
	public AhorroAvisoRetiroOBJ obtenerAvisoRetiroById(Integer avisoId) {
		
		AhorroAvisoRetiroOBJ obj = new AhorroAvisoRetiroOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerAvisoRetiroById, avisoId);

			if (!rows.isEmpty()) {
				obj.setAvisoId((Integer) rows.get(0).get("aviso_id"));
				obj.setCuenta((String) rows.get(0).get("cuenta"));
				obj.setMonto((Double) rows.get(0).get("monto"));
				obj.setEstatus((String) rows.get(0).get("estatus"));
				obj.setAutorizadoPor((Integer) rows.get(0).get(""));
				obj.setSucursal((String) rows.get(0).get("autorizado_por"));
				obj.setRegion((Integer) rows.get(0).get("region"));
				obj.setMovimientoId((Integer) rows.get(0).get("movimiento_id"));
				obj.setTipoTran((String) rows.get(0).get("tipo_tran"));
			}

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}

		return obj;
	}
	
	public void actualizaAvisoRetiro(AhorroAvisoRetiroOBJ req){
		try{
			jdbcTemplatePr.update(actualizaAvisoRetiro, req.getCuenta(), req.getMonto(), req.getEstatus(),req.getSucursal(),req.getRegion(),req.getModificadoPor(),req.getMovimientoId(),req.getTipoTran());
		}catch(Exception e){
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getObtenerAvisoRetiroById() {
		return obtenerAvisoRetiroById;
	}
	public void setObtenerAvisoRetiroById(String obtenerAvisoRetiroById) {
		this.obtenerAvisoRetiroById = obtenerAvisoRetiroById;
	}
	
	public String getActualizaAvisoRetiro() {
		return actualizaAvisoRetiro;
	}
	public void setActualizaAvisoRetiro(String actualizaAvisoRetiro) {
		this.actualizaAvisoRetiro = actualizaAvisoRetiro;
	}

}
