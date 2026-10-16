package net.cero.spring.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroContratoDatos;

public class AhorroContratoDatosDAO{	
	public static final Logger LOG = LogManager.getLogger(AhorroContratoDatosDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSecAhorroContratoDatos;
	private String nuevoAhorroContratoDatos;
	
	public Integer sigSecAhorroContratoDatos(){
		try {
			return jdbcTemplatePr.queryForObject(sigSecAhorroContratoDatos, Integer.class);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer nuevoAhorroContratoDatos(AhorroContratoDatos a) {
		try {
			Integer id = sigSecAhorroContratoDatos();
			a.setId(id);
			jdbcTemplatePr.update(nuevoAhorroContratoDatos,a.getId(),a.getCuenta(),a.getComoEnteroDesc(),a.getUsuarioCreacion(),a.getRespReferencia(),a.getRespSucursal(),a.getIdProspectadoPor());
			return a.getId();
		}catch(Exception e) {
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
	 * @return the sigSecAhorroContratoDatos
	 */
	public String getSigSecAhorroContratoDatos() {
		return sigSecAhorroContratoDatos;
	}

	/**
	 * @param sigSecAhorroContratoDatos the sigSecAhorroContratoDatos to set
	 */
	public void setSigSecAhorroContratoDatos(String sigSecAhorroContratoDatos) {
		this.sigSecAhorroContratoDatos = sigSecAhorroContratoDatos;
	}

	/**
	 * @return the nuevoAhorroContratoDatos
	 */
	public String getNuevoAhorroContratoDatos() {
		return nuevoAhorroContratoDatos;
	}

	/**
	 * @param nuevoAhorroContratoDatos the nuevoAhorroContratoDatos to set
	 */
	public void setNuevoAhorroContratoDatos(String nuevoAhorroContratoDatos) {
		this.nuevoAhorroContratoDatos = nuevoAhorroContratoDatos;
	}

	
}
