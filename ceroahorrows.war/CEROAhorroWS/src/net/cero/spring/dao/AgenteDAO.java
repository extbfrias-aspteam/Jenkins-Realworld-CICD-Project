package net.cero.spring.dao;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AgenteOBJ;
import net.cero.data.ColoniaOBJ;
import net.cero.data.MovimientosCaja;
import net.cero.data.RegionesOBJ;
import net.cero.data.Solicitante;

public class AgenteDAO{	
	public static final Logger LOG = LogManager.getLogger(AgenteDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String obtenerSucursalAperturaByRegion;
	
	public AgenteOBJ obtenerSucursalAperturaByRegion(Integer region) {
		
		AgenteOBJ result = new AgenteOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerSucursalAperturaByRegion, region);

			if(!rows.isEmpty()) {
				result.setClave((String) rows.get(0).get("clave"));
				result.setRfc((String) rows.get(0).get("rfc"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setDomicilio((String) rows.get(0).get("domicilio"));
				result.setColonia((String) rows.get(0).get("colonia"));
				result.setTelefono((String) rows.get(0).get("telefono"));
				result.setResponsa((String) rows.get(0).get("responsa"));
				result.setPuesto((String) rows.get(0).get("puesto"));
				result.setCorreo((String) rows.get(0).get("correo"));
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
	 * @return the obtenerSucursalAperturaByRegion
	 */
	public String getObtenerSucursalAperturaByRegion() {
		return obtenerSucursalAperturaByRegion;
	}

	/**
	 * @param obtenerSucursalAperturaByRegion the obtenerSucursalAperturaByRegion to set
	 */
	public void setObtenerSucursalAperturaByRegion(String obtenerSucursalAperturaByRegion) {
		this.obtenerSucursalAperturaByRegion = obtenerSucursalAperturaByRegion;
	}
}
