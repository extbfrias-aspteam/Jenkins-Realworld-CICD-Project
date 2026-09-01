package net.cero.spring.dao;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class AhorroGarantiasDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroGarantiasDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String montoGarantia;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getMontoGarantia() {
		return montoGarantia;
	}

	public void setMontoGarantia(String montoGarantia) {
		this.montoGarantia = montoGarantia;
	}

	public Double montoGarantia(String cuenta){
		return jdbcTemplatePr.queryForObject(montoGarantia, Double.class,cuenta);
	}
	
}
