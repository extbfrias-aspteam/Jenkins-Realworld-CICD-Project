package net.cero.spring.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class ValorUdiDAO {

	private JdbcTemplate jdbcTemplatePr;
	private String obtenerValorUdi;
	
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getObtenerValorUdi() {
		return obtenerValorUdi;
	}
	public void setObtenerValorUdi(String obtenerValorUdi) {
		this.obtenerValorUdi = obtenerValorUdi;
	}
	
	
	public Double obtenerValorUdi(){
		Double valorUdi=0.0;
		
		try{
			valorUdi=jdbcTemplatePr.queryForObject(obtenerValorUdi, Double.class);
			
		}catch(IncorrectResultSizeDataAccessException e){
			
		}
		
		return valorUdi;
	}
	
	
}
