package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class CatinstiDAO {	

	public static final Logger LOG = LogManager.getLogger(CatinstiDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String actualizaSaldoNullo;

	public void actualizaSaldoNullo(Double saldo, Integer catinsti) {
		try{
			jdbcTemplatePr.update(actualizaSaldoNullo, saldo, catinsti);
		}catch(Exception e){
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getActualizaSaldoNullo() {
		return actualizaSaldoNullo;
	}
	public void setActualizaSaldoNullo(String actualizaSaldoNullo) {
		this.actualizaSaldoNullo = actualizaSaldoNullo;
	}
}
