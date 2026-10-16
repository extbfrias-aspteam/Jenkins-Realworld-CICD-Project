package net.cero.spring.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class PosicionGlobalDAO {

	private JdbcTemplate jdbcTemplate;
	private String queryBuscarTipoCuenta;
	private String queryBuscarCuentaPorTarjeta;
	private String queryBuscarCuentaPorClabe;
	
	/**
	 * Obtiene el tipo de cuenta (posicion)
	 * 
	 * @param cuenta Número de cuenta
	 * @return "DEBITO", "AHORRO" o ""
	 */
	public String obtenerPosicionGlobalCuenta(String cuenta) {
		String tipoCuenta = "";
		try {
			tipoCuenta = jdbcTemplate.queryForObject(queryBuscarTipoCuenta, String.class, cuenta);
			return tipoCuenta;
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
	}
	public String obtenerCuentaPorTarjeta(String tarjeta) {
		String tipoCuenta = "";
		try {
			tipoCuenta = jdbcTemplate.queryForObject(queryBuscarCuentaPorTarjeta, String.class, tarjeta, tarjeta);
			return tipoCuenta;
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
	}
	public String obtenerCuentaPorClabe(String clabe) {
		String tipoCuenta = "";
		try {
			tipoCuenta = jdbcTemplate.queryForObject(queryBuscarCuentaPorClabe, String.class, clabe);
			return tipoCuenta;
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getQueryBuscarTipoCuenta() {
		return queryBuscarTipoCuenta;
	}

	public void setQueryBuscarTipoCuenta(String queryBuscarTipoCuenta) {
		this.queryBuscarTipoCuenta = queryBuscarTipoCuenta;
	}

	public String getQueryBuscarCuentaPorTarjeta() {
		return queryBuscarCuentaPorTarjeta;
	}

	public void setQueryBuscarCuentaPorTarjeta(String queryBuscarCuentaPorTarjeta) {
		this.queryBuscarCuentaPorTarjeta = queryBuscarCuentaPorTarjeta;
	}

	public String getQueryBuscarCuentaPorClabe() {
		return queryBuscarCuentaPorClabe;
	}

	public void setQueryBuscarCuentaPorClabe(String queryBuscarCuentaPorClabe) {
		this.queryBuscarCuentaPorClabe = queryBuscarCuentaPorClabe;
	}
}
