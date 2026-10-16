package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.RegistroCodiOBJ;

public class RegistroCodiDAO {
	private JdbcTemplate jdbcTemplate;
	private String buscarRegistroCodi;
	private String actualizarRegistroCodi;
	private String guardarRegistroCodi;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getBuscarRegistroCodi() {
		return buscarRegistroCodi;
	}

	public void setBuscarRegistroCodi(String buscarRegistroCodi) {
		this.buscarRegistroCodi = buscarRegistroCodi;
	}

	public String getActualizarRegistroCodi() {
		return actualizarRegistroCodi;
	}

	public void setActualizarRegistroCodi(String actualizarRegistroCodi) {
		this.actualizarRegistroCodi = actualizarRegistroCodi;
	}

	public String getGuardarRegistroCodi() {
		return guardarRegistroCodi;
	}

	public void setGuardarRegistroCodi(String guardarRegistroCodi) {
		this.guardarRegistroCodi = guardarRegistroCodi;
	}

	public RegistroCodiOBJ buscarRegistroCodi(String cuenta) {
		RegistroCodiOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarRegistroCodi, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new RegistroCodiOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((Integer) row.get("estatus"));
				obj.setFolioContrato((String) row.get("folio_contrato"));
				obj.setId((Integer) row.get("id"));

			}

			return obj;

		} catch (EmptyResultDataAccessException e) {
			return obj;
		}
	}

	public boolean actualizarRegistroCodi(RegistroCodiOBJ obj) {
		int rows = 0;

		try {
			rows = jdbcTemplate.update(actualizarRegistroCodi, obj.getFolioContrato(), obj.getEstatus(), obj.getId());

			return rows > 0;
		} catch (DataAccessException e) {
			return false;
		}
	}

	public boolean guardarRegistroCodi(RegistroCodiOBJ obj) {
		int rows = 0;

		try {
			rows = jdbcTemplate.update(guardarRegistroCodi, obj.getCuenta(), obj.getFolioContrato(), obj.getEstatus(), obj.getIdSolicitante());

			return rows > 0;
		} catch (DataAccessException e) {
			return false;
		}
	}

}
