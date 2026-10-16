package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroDisposicionesOBJ;


public class AhorroDisposicionesDAO {	

	public static final Logger LOG = LogManager.getLogger(AhorroDisposicionesDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSecAhorroDisp;
	private String nuevoAhorroDisposicion;

	private Integer sigSecAhorroDisp(){
		return jdbcTemplatePr.queryForObject(sigSecAhorroDisp, Integer.class);
	}

	public Integer nuevo(AhorroDisposicionesOBJ ad) {
		Integer id = sigSecAhorroDisp();
		try{
			jdbcTemplatePr.update(nuevoAhorroDisposicion,id, ad.getCuenta(),ad.getFecha(),ad.getMonto(),ad.getFormaPagoId(),ad.getBancoId(),ad.getCheque(),ad.getMovtoId(),ad.getCreadoPor(),ad.getTransaccionId(),ad.getTarjetaOperativaId(),ad.getApp(),ad.getTransaccionVersionId(),ad.getAvisoId());
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		return id;
	}


	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}


	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}


	public String getNuevoAhorroDisposicion() {
		return nuevoAhorroDisposicion;
	}


	public void setNuevoAhorroDisposicion(String nuevoAhorroDisposicion) {
		this.nuevoAhorroDisposicion = nuevoAhorroDisposicion;
	}


	public String getSigSecAhorroDisp() {
		return sigSecAhorroDisp;
	}


	public void setSigSecAhorroDisp(String sigSecAhorroDisp) {
		this.sigSecAhorroDisp = sigSecAhorroDisp;
	}
	
}
