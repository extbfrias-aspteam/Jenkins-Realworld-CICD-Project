package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class AhorroRendimientoDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroRendimientoDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String ahorroTipocuenta;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	
	public String getAhorroTipocuenta() {
		return ahorroTipocuenta;
	}

	public void setAhorroTipocuenta(String ahorroTipocuenta) {
		this.ahorroTipocuenta = ahorroTipocuenta;
	}

	public String ahorroTipocuenta(Integer idTipo){
		return jdbcTemplatePr.queryForObject(ahorroTipocuenta, String.class,idTipo);
	}
}
