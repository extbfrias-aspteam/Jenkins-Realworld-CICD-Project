package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Log4j2
public class ParametrosGeneralesDAO {
	private JdbcTemplate jdbcTemplatePr;
	private String validacionCuentaSimplificada;
	private String fondeaCuentaSimplificada;
	private String montoFondeoCuentaSimplificada;
	private String cuentaOrigenFondeoCuentaSimplificada;
	private String montoMaxAhorroCuentaSimplificada;

	public String validacionCuentaSimplificada() {
		String result = "";
		List<Map<String, Object>> rows;

		try {
			// log.info("validacionCuentaSimplificada QUERY :: " +
			// validacionCuentaSimplificada);
			rows = jdbcTemplatePr.queryForList(validacionCuentaSimplificada);
			if (!rows.isEmpty()) {
				// log.info("valor :: " + (String)
				// rows.get(0).get("valor"));
				result = (String) rows.get(0).get("valor");
			} else {
				// log.info("No encontro registros");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	public Boolean fondeaCuentaSimplificada() {
		Boolean result = false;
		String valor = "";
		List<Map<String, Object>> rows;

		try {
			// log.info("validacionCuentaSimplificada QUERY :: " +
			// validacionCuentaSimplificada);
			rows = jdbcTemplatePr.queryForList(fondeaCuentaSimplificada);
			if (!rows.isEmpty()) {
				// log.info("valor :: " + (String)
				// rows.get(0).get("valor"));
				valor = (String) rows.get(0).get("valor");
				if (valor.equals("1")) {
					result = true;
				} else {
					result = false;
				}
			} else {
				// log.info("No encontro registros");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	public Double montoFondeoCuentaSimplificada() {
		Double result = (double) 0;
		String valor = "";
		List<Map<String, Object>> rows;

		try {
			// log.info("validacionCuentaSimplificada QUERY :: " +
			// validacionCuentaSimplificada);
			rows = jdbcTemplatePr.queryForList(montoFondeoCuentaSimplificada);
			if (!rows.isEmpty()) {
				// log.info("valor :: " + (String)
				// rows.get(0).get("valor"));
				valor = (String) rows.get(0).get("valor");
				result = Double.valueOf(valor);
			} else {
				// log.info("No encontro registros");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	public String cuentaOrigenFondeoCuentaSimplificada() {
		String result = "";
		List<Map<String, Object>> rows;

		try {
			// log.info("validacionCuentaSimplificada QUERY :: " +
			// validacionCuentaSimplificada);
			rows = jdbcTemplatePr.queryForList(cuentaOrigenFondeoCuentaSimplificada);
			if (!rows.isEmpty()) {
				// log.info("valor :: " + (String)
				// rows.get(0).get("valor"));
				result = (String) rows.get(0).get("valor");
			} else {
				// log.info("No encontro registros");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}
	
	public String montoMaxAhorroCuentaSimplificada(){
		String result = "";
		List<Map<String, Object>> rows;

		try {
			
			rows = jdbcTemplatePr.queryForList(montoMaxAhorroCuentaSimplificada);
			if (!rows.isEmpty()) {
				
				result = (String) rows.get(0).get("valor");
			}

		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}

		return result;
	}

	/**
	 * @return the jdbcTemplatePr
	 */
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	/**
	 * @param jdbcTemplatePr
	 *            the jdbcTemplatePr to set
	 */
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	/**
	 * @return the validacionCuentaSimplificada
	 */
	public String getValidacionCuentaSimplificada() {
		return validacionCuentaSimplificada;
	}

	/**
	 * @param validacionCuentaSimplificada
	 *            the validacionCuentaSimplificada to set
	 */
	public void setValidacionCuentaSimplificada(String validacionCuentaSimplificada) {
		this.validacionCuentaSimplificada = validacionCuentaSimplificada;
	}

	/**
	 * @return the fondeaCuentaSimplificada
	 */
	public String getFondeaCuentaSimplificada() {
		return fondeaCuentaSimplificada;
	}

	/**
	 * @param fondeaCuentaSimplificada
	 *            the fondeaCuentaSimplificada to set
	 */
	public void setFondeaCuentaSimplificada(String fondeaCuentaSimplificada) {
		this.fondeaCuentaSimplificada = fondeaCuentaSimplificada;
	}

	/**
	 * @return the montoFondeoCuentaSimplificada
	 */
	public String getMontoFondeoCuentaSimplificada() {
		return montoFondeoCuentaSimplificada;
	}

	/**
	 * @param montoFondeoCuentaSimplificada
	 *            the montoFondeoCuentaSimplificada to set
	 */
	public void setMontoFondeoCuentaSimplificada(String montoFondeoCuentaSimplificada) {
		this.montoFondeoCuentaSimplificada = montoFondeoCuentaSimplificada;
	}

	/**
	 * @return the cuentaOrigenFondeoCuentaSimplificada
	 */
	public String getCuentaOrigenFondeoCuentaSimplificada() {
		return cuentaOrigenFondeoCuentaSimplificada;
	}

	/**
	 * @param cuentaOrigenFondeoCuentaSimplificada
	 *            the cuentaOrigenFondeoCuentaSimplificada to set
	 */
	public void setCuentaOrigenFondeoCuentaSimplificada(String cuentaOrigenFondeoCuentaSimplificada) {
		this.cuentaOrigenFondeoCuentaSimplificada = cuentaOrigenFondeoCuentaSimplificada;
	}

	public String getMontoMaxAhorroCuentaSimplificada() {
		return montoMaxAhorroCuentaSimplificada;
	}

	public void setMontoMaxAhorroCuentaSimplificada(String montoMaxAhorroCuentaSimplificada) {
		this.montoMaxAhorroCuentaSimplificada = montoMaxAhorroCuentaSimplificada;
	}

}
