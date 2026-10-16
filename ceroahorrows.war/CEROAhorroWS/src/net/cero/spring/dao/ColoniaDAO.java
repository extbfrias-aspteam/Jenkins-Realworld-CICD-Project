package net.cero.spring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.cero.data.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class ColoniaDAO{
	public static final Logger LOG = LogManager.getLogger(ColoniaDAO.class);

	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplate;
	private String obtenerColoniaByCpNombre;
	private String obtenerColoniaCentroByLocalidad;
	private String obtenerColoniaByCpNombreCERO;
	private String obtenerDatosCpbyCodigoCp;
	private String obtenerDatosColoniasByCp;



	public ColoniaOBJ obtenerColoniaByCpNombre(String cp, String nombre) {

		ColoniaOBJ result = new ColoniaOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerColoniaByCpNombre,nombre,cp);

			if(!rows.isEmpty()) {
				result.setClave((Integer) rows.get(0).get("clave"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setCp((String) rows.get(0).get("cp"));
				result.setAgenteId((String) rows.get(0).get("agente_id"));
				result.setLocalidadId((Integer) rows.get(0).get("localidad_id"));
				result.setRegionId((Integer) rows.get(0).get("region_id"));
				result.setExclusiva((String) rows.get(0).get("exclusiva"));
				result.setComisionGestor((Double) rows.get(0).get("comision_gestor"));
				result.setBloqueada((Integer) rows.get(0).get("bloqueada"));
				result.setIdSubsidio((Integer) rows.get(0).get("id_subsidio"));
				result.setClaveCnbv((String) rows.get(0).get("clave_cnbv"));

			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}

	public ColoniaOBJ obtenerColoniaCentroByLocalidad(Integer localidad) {

		ColoniaOBJ result = new ColoniaOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerColoniaCentroByLocalidad, localidad);

			if(!rows.isEmpty()) {
				result.setClave((Integer) rows.get(0).get("clave"));
				result.setNombre((String) rows.get(0).get("nombre"));
				result.setCp((String) rows.get(0).get("cp"));
				result.setAgenteId((String) rows.get(0).get("agente_id"));
				result.setLocalidadId((Integer) rows.get(0).get("localidad_id"));
				result.setRegionId((Integer) rows.get(0).get("region_id"));
				result.setExclusiva((String) rows.get(0).get("exclusiva"));
				result.setComisionGestor((Double) rows.get(0).get("comision_gestor"));
				result.setBloqueada((Integer) rows.get(0).get("bloqueada"));
				result.setIdSubsidio((Integer) rows.get(0).get("id_subsidio"));
				result.setClaveCnbv((String) rows.get(0).get("clave_cnbv"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}
	public ColoniasDTO  obtenerDatosCpbyCodigoCp(String claveColonia) {
		LOG.info("Obteniendo datos de cp by codigocp");
		List<Map<String, Object>> rows = new ArrayList<>();
		ColoniasDTO colonia = new ColoniasDTO();
		try {
			Map<String, Object> row = jdbcTemplatePr.queryForMap(obtenerDatosCpbyCodigoCp, claveColonia);

			if (!row.isEmpty()) {
				colonia.setCp((String) row.get("cp"));
				colonia.setColonia((String) row.get("colonia"));
				colonia.setEstado((String) row.get("estado"));
				colonia.setMunicipio((String) row.get("municipio"));
				colonia.setLocalidad((String) row.get("localidad"));
			}
			} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return colonia;
	}
	public List<Map<String, Object>>  obtenerDatosColoniaByCp(String localidad) {

		List<Map<String, Object>> rows = new ArrayList<>();

		try {
			rows = jdbcTemplatePr.queryForList(obtenerDatosColoniasByCp, localidad);
			LOG.info("Resultado directo del query: " + rows.toString());


		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return rows;
	}

	public Long obtenerColoniaByCpNombreV2(String cp, String nombre) {

//		ColoniaOBJ result = new ColoniaOBJ();
		List<Map<String, Object>> rows;
		Long result = 0L;
		try {
			rows = jdbcTemplate.queryForList(obtenerColoniaByCpNombreCERO, cp, nombre);

			if (!rows.isEmpty()) {
				result = (Long) rows.get(0).get("colonia_id");
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
	 * @return the obtenerColoniaByCpNombre
	 */
	public String getObtenerColoniaByCpNombre() {
		return obtenerColoniaByCpNombre;
	}

	/**
	 * @param obtenerColoniaByCpNombre the obtenerColoniaByCpNombre to set
	 */
	public void setObtenerColoniaByCpNombre(String obtenerColoniaByCpNombre) {
		this.obtenerColoniaByCpNombre = obtenerColoniaByCpNombre;
	}

	/**
	 * @return the obtenerColoniaCentroByLocalidad
	 */
	public String getObtenerColoniaCentroByLocalidad() {
		return obtenerColoniaCentroByLocalidad;
	}

	/**
	 * @param obtenerColoniaCentroByLocalidad the obtenerColoniaCentroByLocalidad to set
	 */
	public void setObtenerColoniaCentroByLocalidad(String obtenerColoniaCentroByLocalidad) {
		this.obtenerColoniaCentroByLocalidad = obtenerColoniaCentroByLocalidad;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getObtenerColoniaByCpNombreCERO() {
		return obtenerColoniaByCpNombreCERO;
	}

	public void setObtenerColoniaByCpNombreCERO(String obtenerColoniaByCpNombreCERO) {
		this.obtenerColoniaByCpNombreCERO = obtenerColoniaByCpNombreCERO;
	}
	public String getObtenerDatosCpbyCodigoCp() {
		return obtenerDatosCpbyCodigoCp;
	}

	public void setObtenerDatosCpbyCodigoCp(String obtenerDatosCpbyCodigoCp) {
		this.obtenerDatosCpbyCodigoCp = obtenerDatosCpbyCodigoCp;
	}

	public String getObtenerDatosColoniasByCp() {
		return obtenerDatosColoniasByCp;
	}

	public void setObtenerDatosColoniasByCp(String obtenerDatosColoniasByCp) {
		this.obtenerDatosColoniasByCp = obtenerDatosColoniasByCp;
	}
}
