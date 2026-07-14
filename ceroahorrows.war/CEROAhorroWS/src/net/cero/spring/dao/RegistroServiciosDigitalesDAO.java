package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.RegistroServiciosDigitalesOBJ;

public class RegistroServiciosDigitalesDAO {
	
	private JdbcTemplate jdbcTemplate;
	private String buscarRegistroServiciosDigitales;
	private String actualizarRegistroServiciosDigitales;
	private String guardarRegistroServiciosDigitales;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getBuscarRegistroServiciosDigitales() {
		return buscarRegistroServiciosDigitales;
	}

	public void setBuscarRegistroServiciosDigitales(String buscarRegistroServiciosDigitales) {
		this.buscarRegistroServiciosDigitales = buscarRegistroServiciosDigitales;
	}

	public String getActualizarRegistroServiciosDigitales() {
		return actualizarRegistroServiciosDigitales;
	}

	public void setActualizarRegistroServiciosDigitales(String actualizarRegistroServiciosDigitales) {
		this.actualizarRegistroServiciosDigitales = actualizarRegistroServiciosDigitales;
	}

	public String getGuardarRegistroServiciosDigitales() {
		return guardarRegistroServiciosDigitales;
	}

	public void setGuardarRegistroServiciosDigitales(String guardarRegistroServiciosDigitales) {
		this.guardarRegistroServiciosDigitales = guardarRegistroServiciosDigitales;
	}

	public RegistroServiciosDigitalesOBJ buscarRegistroServiciosDigitales(String cuenta) {
		RegistroServiciosDigitalesOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarRegistroServiciosDigitales, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new RegistroServiciosDigitalesOBJ();

				obj.setAlfrescoId((String) row.get("alfresco_id"));
				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((Integer) row.get("estatus"));
				obj.setFolioContrato((String) row.get("folio_contrato"));
				obj.setId((Integer) row.get("id"));
				obj.setNombreDocumento((String) row.get("nombre_documento"));
				obj.setPassword((String) row.get("password"));
				obj.setTipoArchivoId((Integer) row.get("tipo_archivo_id"));
				obj.setUsuario((String) row.get("usuario"));

			}

			return obj;

		} catch (EmptyResultDataAccessException e) {
			return obj;
		}
	}

	public boolean actualizarRegistroServiciosDigitales(RegistroServiciosDigitalesOBJ obj) {
		int rows = 0;

		try {
			rows = jdbcTemplate.update(actualizarRegistroServiciosDigitales, obj.getFolioContrato(), obj.getPassword(),
					obj.getEstatus(), obj.getNombreDocumento(), obj.getAlfrescoId(), obj.getTipoArchivoId(),
					obj.getId());

			return rows > 0;
		} catch (DataAccessException e) {
			return false;
		}
	}

	public boolean guardarRegistroServiciosDigitales(RegistroServiciosDigitalesOBJ obj) {
		int rows = 0;

		try {
			rows = jdbcTemplate.update(guardarRegistroServiciosDigitales, obj.getCuenta(), obj.getFolioContrato(),
					obj.getEstatus(), obj.getNombreDocumento(), obj.getAlfrescoId(), obj.getTipoArchivoId());

			return rows > 0;
		} catch (DataAccessException e) {
			return false;
		}
	}

}
