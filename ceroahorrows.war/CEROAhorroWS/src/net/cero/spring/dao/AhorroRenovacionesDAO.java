package net.cero.spring.dao;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class AhorroRenovacionesDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroRenovacionesDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String ultimaFechaRenovacion;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getUltimaFechaRenovacion() {
		return ultimaFechaRenovacion;
	}

	public void setUltimaFechaRenovacion(String ultimaFechaRenovacion) {
		this.ultimaFechaRenovacion = ultimaFechaRenovacion;
	}

	public Date ultimaFechaRenovacion(String cuenta){
		return (Date) jdbcTemplatePr.queryForObject(ultimaFechaRenovacion, Date.class,cuenta);
	}

	

	
}
