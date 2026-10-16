package net.cero.spring.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.CampaniaOBJ;

public class CampaniaDAO {

	private JdbcTemplate jdbcTemplatePr;
	private String buscarCampaniaCodigo;
	private String actualizarAcumuladoCampania;

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getBuscarCampaniaCodigo() {
		return buscarCampaniaCodigo;
	}

	public void setBuscarCampaniaCodigo(String buscarCampaniaCodigo) {
		this.buscarCampaniaCodigo = buscarCampaniaCodigo;
	}

	public String getActualizarAcumuladoCampania() {
		return actualizarAcumuladoCampania;
	}

	public void setActualizarAcumuladoCampania(String actualizarAcumuladoCampania) {
		this.actualizarAcumuladoCampania = actualizarAcumuladoCampania;
	}

	public CampaniaOBJ buscarCampaniaPorCodigo(String codigo) {
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCampaniaCodigo, codigo);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				CampaniaOBJ obj = new CampaniaOBJ();

				obj.setAcumulados((Integer) row.get("acumulados"));
				obj.setCampania((String) row.get("campania"));

				BigDecimal costo = (BigDecimal) row.get("costo");
				if (costo != null)
					obj.setCosto(costo.doubleValue());

				BigDecimal incentivo = (BigDecimal) row.get("incentivo");
				if (incentivo != null)
					obj.setIncentivo(incentivo.doubleValue());

				obj.setInfluencerId((Integer) row.get("influencer_id"));
				obj.setMeta((Integer) row.get("meta"));
				obj.setVigencia((Date) row.get("vigencia"));
				obj.setCuentaAhorro((String) row.get("cuenta_ahorro"));

				return obj;

			}

			return null;

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public boolean actualizarAcumulados(Integer acumulados, String campania) {

		int rows = 0;

		try {
			rows = jdbcTemplatePr.update(actualizarAcumuladoCampania, acumulados, campania);

			return rows > 0;

		} catch (DataAccessException e) {
			return false;
		}

	}

}
