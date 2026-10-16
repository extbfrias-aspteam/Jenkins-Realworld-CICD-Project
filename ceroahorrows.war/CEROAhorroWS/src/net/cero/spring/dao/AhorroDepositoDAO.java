package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroDeposito;


public class AhorroDepositoDAO {	

	public static final Logger LOG = LogManager.getLogger(AhorroDepositoDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String nuevoAhorroDeposito;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getNuevoAhorroDeposito() {
		return nuevoAhorroDeposito;
	}
	public void setNuevoAhorroDeposito(String nuevoAhorroDeposito) {
		this.nuevoAhorroDeposito = nuevoAhorroDeposito;
	}

	public Boolean nuevo(AhorroDeposito ad) {
		// TODO Auto-generated method stub
		try{
			jdbcTemplatePr.update(nuevoAhorroDeposito, ad.getDepositoId(),ad.getCuenta(),ad.getMonto(),ad.getFecha(),ad.getFormaPago(),ad.getBanco(),ad.getNoCheque(),ad.getObservaciones(),ad.getCreadoPor(),ad.getFechaCreacion(),ad.getModificadoPor(),ad.getFechaModificacion(),ad.getTransaccionId(),ad.getTarjetaOperativaId(),ad.getApp(),ad.getTransaccionVersionId());
		}catch(Exception e){
			return null;
		}
		return true;
	}


}
