package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.ValoresPropertiesOBJ;

public class ValoresPropertiesInicialesDAO {
public static final Logger LOG = LogManager.getLogger(ValoresPropertiesInicialesDAO.class);
	
	private JdbcTemplate jdbcTemplate;
	private String obtenerEstatusSwitchOCR;
	
	
	public ValoresPropertiesOBJ obtenerEstatusSwitchOCR(){
		List<Map<String, Object>> rows;
		ValoresPropertiesOBJ object = new ValoresPropertiesOBJ();
		try{
			rows = jdbcTemplate.queryForList(obtenerEstatusSwitchOCR);			
		if (!rows.isEmpty()) {
			object = new ValoresPropertiesOBJ();
				object.setId((Integer) rows.get(0).get("id"));
				object.setClave((String) rows.get(0).get("clave"));
				object.setValor((String) rows.get(0).get("valor"));
				System.out.print("###ID: "+ object.getId());
				System.out.print("###CLAVE: "+ object.getClave());
				System.out.print("###VALOR: "+ object.getValor());
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return object;
	}
	
	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * @return the obtenerEstatusSwitchOCR
	 */
	public String getObtenerEstatusSwitchOCR() {
		return obtenerEstatusSwitchOCR;
	}
	/**
	 * @param obtenerEstatusSwitchOCR the obtenerEstatusSwitchOCR to set
	 */
	public void setObtenerEstatusSwitchOCR(String obtenerEstatusSwitchOCR) {
		this.obtenerEstatusSwitchOCR = obtenerEstatusSwitchOCR;
	}
	
	
}
