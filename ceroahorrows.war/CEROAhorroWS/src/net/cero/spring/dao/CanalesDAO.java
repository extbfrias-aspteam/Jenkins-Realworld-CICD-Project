package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class CanalesDAO {
	public static final Logger LOG = LogManager.getLogger(CanalesDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String consultarCanal;
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getConsultarCanal() {
		return consultarCanal;
	}
	public void setConsultarCanal(String consultarCanal) {
		this.consultarCanal = consultarCanal;
	}
	
	public String consultarCanal(long idCanal) {
		List<Map<String, Object>> rows;
		String claveCanal = "";

		try {
			rows = jdbcTemplatePr.queryForList(consultarCanal, idCanal);

			for (Map<String, Object> row : rows) {
				claveCanal = (String) row.get("clave");
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return claveCanal;
	}
}
