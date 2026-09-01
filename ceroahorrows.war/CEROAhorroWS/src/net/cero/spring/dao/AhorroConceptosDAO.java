package net.cero.spring.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class AhorroConceptosDAO {

	private JdbcTemplate jdbcTemplatePr;
	private String registrarConceptoAhorro;
	
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getRegistrarConceptoAhorro() {
		return registrarConceptoAhorro;
	}
	public void setRegistrarConceptoAhorro(String registrarConceptoAhorro) {
		this.registrarConceptoAhorro = registrarConceptoAhorro;
	}
	
	
	public boolean registrarConcepto(Integer ahorroContratoId,Integer conceptoId,String valor,Integer usuarioId){
		int rows=0;
		
		try{
			rows=jdbcTemplatePr.update(registrarConceptoAhorro, ahorroContratoId,conceptoId,valor,usuarioId);
			
			return rows>0;
		}catch(DataAccessException e){
			e.printStackTrace();
			return false;
		}
	}
	
}
