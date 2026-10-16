package net.cero.spring.dao;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.ColoniaOBJ;
import net.cero.data.MovimientosCaja;
import net.cero.data.RegionesOBJ;
import net.cero.data.Solicitante;

public class RegionesDAO{	
	public static final Logger LOG = LogManager.getLogger(RegionesDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String obtenerSucursalIdByColoniaId;
	
	
	public RegionesOBJ obtenerSucursalIdByColoniaId(Integer coloniaId) {
		
		RegionesOBJ result = new RegionesOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerSucursalIdByColoniaId, coloniaId);

			if(!rows.isEmpty()) {
				result.setClave((Integer) rows.get(0).get("clave"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setDepende_region_id((Integer) rows.get(0).get(""));
				result.setPlazo_max_avio((Integer) rows.get(0).get(""));
				result.setPlazo_max_refa((Integer) rows.get(0).get(""));
				result.setCoordinacion_id((Integer) rows.get(0).get(""));
				result.setCuenta((String) rows.get(0).get(""));
				result.setSubcuenta((String) rows.get(0).get(""));
				result.setIp((String) rows.get(0).get(""));
				result.setZona((String) rows.get(0).get(""));
				result.setDomicilio((String) rows.get(0).get(""));
				result.setTelefono((String) rows.get(0).get(""));
				result.setCiudad((String) rows.get(0).get(""));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}

	/**
	 * @return the jdbcTemplatePr
	 */
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	/**
	 * @param jdbcTemplatePr the jdbcTemplatePr to set
	 */
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	/**
	 * @return the obtenerSucursalIdByColoniaId
	 */
	public String getObtenerSucursalIdByColoniaId() {
		return obtenerSucursalIdByColoniaId;
	}

	/**
	 * @param obtenerSucursalIdByColoniaId the obtenerSucursalIdByColoniaId to set
	 */
	public void setObtenerSucursalIdByColoniaId(String obtenerSucursalIdByColoniaId) {
		this.obtenerSucursalIdByColoniaId = obtenerSucursalIdByColoniaId;
	}

	
}
