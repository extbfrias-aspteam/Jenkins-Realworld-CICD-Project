package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroTransferenciaOBJ;

public class AhorroTransferenciasDAO {	
	public static final Logger LOG = LogManager.getLogger(AhorroTransferenciasDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSeqAT;
	private String nuevoAT;
	
		
	private Integer sigSeqAT(){
		return jdbcTemplatePr.queryForObject(sigSeqAT, Integer.class);
	}
	
	public Integer nuevoAT(AhorroTransferenciaOBJ at) {
		try{
			at.setId(sigSeqAT());
			jdbcTemplatePr.update(nuevoAT,at.getId(),at.getCuentaOrigen(),at.getCuentaDestino(),at.getFecha(),at.getMonto(),at.getMovimientoId(),at.getCreadoPor(),at.getDepositoId(),at.getDisposicionId());
			return at.getId();
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @return the jdbcTemplatePr
	 */
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	/**
	 * @param jdbcTemplatePr the jdbcTemplatePr to set
	 */
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	/**
	 * @return the sigSeqAT
	 */
	public String getSigSeqAT() {
		return sigSeqAT;
	}

	/**
	 * @param sigSeqAT the sigSeqAT to set
	 */
	public void setSigSeqAT(String sigSeqAT) {
		this.sigSeqAT = sigSeqAT;
	}

	/**
	 * @return the nuevoAT
	 */
	public String getNuevoAT() {
		return nuevoAT;
	}

	/**
	 * @param nuevoAT the nuevoAT to set
	 */
	public void setNuevoAT(String nuevoAT) {
		this.nuevoAT = nuevoAT;
	}
	
}
