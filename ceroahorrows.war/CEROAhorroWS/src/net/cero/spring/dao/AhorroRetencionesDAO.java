package net.cero.spring.dao;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class AhorroRetencionesDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroRetencionesDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String montoRetenciones;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getMontoRetenciones() {
		return montoRetenciones;
	}

	public void setMontoRetenciones(String montoRetenciones) {
		this.montoRetenciones = montoRetenciones;
	}

	public Double montoRetenciones(String cuenta){
		return jdbcTemplatePr.queryForObject(montoRetenciones, Double.class,cuenta);
	}

	
	
}
