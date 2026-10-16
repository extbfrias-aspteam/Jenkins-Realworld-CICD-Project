package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class PINDAO {
public static final Logger LOG = LogManager.getLogger(PINDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String generarPIN;
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getGenerarPIN() {
		return generarPIN;
	}
	public void setGenerarPIN(String generarPIN) {
		this.generarPIN = generarPIN;
	}
	
	public String generarPIN(String cuenta) {
		List<Map<String,Object>> rows = null;
		String pin = "";
		try {
			rows = jdbcTemplatePr.queryForList(generarPIN, cuenta);
			for (Map<String, Object> row : rows) {
				pin = (String) row.get("pin");
			}
			
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return "";
		}
		return pin;
	}
}
