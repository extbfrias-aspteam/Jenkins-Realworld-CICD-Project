package net.cero.spring.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroPagare;


public class AhorroPagareDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroPagareDAO.class);

	private JdbcTemplate jdbcTemplatePr;
	private String buscarPagareByCuenta;
	private String sigSeqAhorroPagare;
	private String nuevoAhorroPagare;
	private String buscarMaxNumPagareByCuenta;
	private String buscarPagareByCuentaNumero;
	
	public AhorroPagare buscarByCuenta(String cuenta) {
		AhorroPagare obj = new AhorroPagare();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarPagareByCuenta, cuenta);

			if (!rows.isEmpty()) {
				obj.setPagareId((Integer) rows.get(0).get("pagare_id"));
				obj.setCuenta((String) rows.get(0).get("cuenta"));
				obj.setNumero((Long) rows.get(0).get("numero"));
				obj.setMonto((Double) rows.get(0).get("monto"));
				obj.setFechaInicio((Date) rows.get(0).get("fecha_inicio"));
				obj.setFechaFinal((Date) rows.get(0).get("fecha_final"));
				obj.setCreadoPor((Integer) rows.get(0).get("creado_por"));
				obj.setFechaCreacion((Date) rows.get(0).get("fecha_creacion"));
				obj.setModificadoPor((Integer) rows.get(0).get("modificado_por"));
				obj.setFechaModificacion((Date) rows.get(0).get("fecha_modificacion"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}
	
	private Integer sigSeq(){
		return jdbcTemplatePr.queryForObject(sigSeqAhorroPagare, Integer.class);
	}

	public Integer nuevo(AhorroPagare ap) {
		try{
			ap.setPagareId(sigSeq());
			jdbcTemplatePr.update(nuevoAhorroPagare, ap.getPagareId(),ap.getCuenta(), ap.getNumero(), ap.getMonto(), ap.getFechaInicio(),ap.getFechaFinal(),ap.getCreadoPor(),ap.getFechaCreacion(),ap.getModificadoPor(),ap.getFechaModificacion());
		}catch(Exception e){
			return null;
		}
		
		return ap.getPagareId();
	}
	
	public Integer buscarMaxNumPagareByCuenta(String cuenta){
		return jdbcTemplatePr.queryForObject(buscarMaxNumPagareByCuenta,new Object[] { cuenta}, Integer.class);
	}

	public AhorroPagare buscarByCuentaNumero(String cuenta, Integer numero) {
		AhorroPagare obj = new AhorroPagare();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarPagareByCuentaNumero, cuenta, numero);

			if (!rows.isEmpty()) {
				obj.setPagareId((Integer) rows.get(0).get("pagare_id"));
				obj.setCuenta((String) rows.get(0).get("cuenta"));
				obj.setNumero((Long) rows.get(0).get("numero"));
				obj.setMonto((Double) rows.get(0).get("monto"));
				obj.setFechaInicio((Date) rows.get(0).get("fecha_inicio"));
				obj.setFechaFinal((Date) rows.get(0).get("fecha_final"));
				obj.setCreadoPor((Integer) rows.get(0).get("creado_por"));
				obj.setFechaCreacion((Date) rows.get(0).get("fecha_creacion"));
				obj.setModificadoPor((Integer) rows.get(0).get("modificado_por"));
				obj.setFechaModificacion((Date) rows.get(0).get("fecha_modificacion"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
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
	 * @return the buscarPagareByCuenta
	 */
	public String getBuscarPagareByCuenta() {
		return buscarPagareByCuenta;
	}

	/**
	 * @param buscarPagareByCuenta the buscarPagareByCuenta to set
	 */
	public void setBuscarPagareByCuenta(String buscarPagareByCuenta) {
		this.buscarPagareByCuenta = buscarPagareByCuenta;
	}

	/**
	 * @return the sigSeqAhorroPagare
	 */
	public String getSigSeqAhorroPagare() {
		return sigSeqAhorroPagare;
	}

	/**
	 * @param sigSeqAhorroPagare the sigSeqAhorroPagare to set
	 */
	public void setSigSeqAhorroPagare(String sigSeqAhorroPagare) {
		this.sigSeqAhorroPagare = sigSeqAhorroPagare;
	}

	/**
	 * @return the nuevoAhorroPagare
	 */
	public String getNuevoAhorroPagare() {
		return nuevoAhorroPagare;
	}

	/**
	 * @param nuevoAhorroPagare the nuevoAhorroPagare to set
	 */
	public void setNuevoAhorroPagare(String nuevoAhorroPagare) {
		this.nuevoAhorroPagare = nuevoAhorroPagare;
	}

	/**
	 * @return the buscarMaxNumPagareByCuenta
	 */
	public String getBuscarMaxNumPagareByCuenta() {
		return buscarMaxNumPagareByCuenta;
	}

	/**
	 * @param buscarMaxNumPagareByCuenta the buscarMaxNumPagareByCuenta to set
	 */
	public void setBuscarMaxNumPagareByCuenta(String buscarMaxNumPagareByCuenta) {
		this.buscarMaxNumPagareByCuenta = buscarMaxNumPagareByCuenta;
	}

	/**
	 * @return the buscarPagareByCuentaNumero
	 */
	public String getBuscarPagareByCuentaNumero() {
		return buscarPagareByCuentaNumero;
	}

	/**
	 * @param buscarPagareByCuentaNumero the buscarPagareByCuentaNumero to set
	 */
	public void setBuscarPagareByCuentaNumero(String buscarPagareByCuentaNumero) {
		this.buscarPagareByCuentaNumero = buscarPagareByCuentaNumero;
	}

	
}
