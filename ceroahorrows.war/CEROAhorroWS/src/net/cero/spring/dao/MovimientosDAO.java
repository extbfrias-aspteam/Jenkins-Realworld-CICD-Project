package net.cero.spring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;

import net.cero.data.CatalogoMovimientosOBJ;
import net.cero.data.Respuesta;

public class MovimientosDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovimientosDAO.class);

	/**
	 * JDBC a base de datos CERO
	 */
	private JdbcTemplate jdbcTemplate;
	/**
	 * JDBC a base de datos procrea
	 */
	private JdbcTemplate jdbcTemplatePr;
	
	private String consultaCatalagoMovimientosManuales;

	public Respuesta consultaCatalagoMovimientosManuales() {
		// TODO Auto-generated method stub
		Respuesta respuesta = new Respuesta();
		Gson gson = new Gson();
		try {
			List<Map<String, Object>> row = jdbcTemplate.queryForList(consultaCatalagoMovimientosManuales);
			List<CatalogoMovimientosOBJ> list = new ArrayList<>();
			
			for (Map<String, Object>  actual : row) {
				CatalogoMovimientosOBJ catalogo = new CatalogoMovimientosOBJ();
				catalogo.setClaveMovimiento(actual.get("clave_movimiento").toString());
				catalogo.setDescripcionMovimiento(actual.get("descripcion_movimiento").toString());
				String tipoMovimiento ="";
				if(Objects.nonNull(actual.get("tipo_movimiento"))) {
					tipoMovimiento = actual.get("tipo_movimiento").toString();
					if(tipoMovimiento.equals("+")) {
						tipoMovimiento = "abono";
					}else if(tipoMovimiento.equals("-")) {
						tipoMovimiento = "cargo";
					}
				}
				
				catalogo.setTipoMovimiento(tipoMovimiento);
				list.add(catalogo);
			}
			respuesta.setCodigo(0);
			respuesta.setData(gson.toJson(list));
			respuesta.setMensaje("Solicitud exitosa");
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("error al consultar catalogo {}", e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No existe la informacion solicitada");
		}
		return respuesta;
	}

	/**
	 * GET's y SET's necesarios para la inyeccion de script
	 */

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
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getConsultaCatalagoMovimientosManuales() {
		return consultaCatalagoMovimientosManuales;
	}

	public void setConsultaCatalagoMovimientosManuales(String consultaCatalagoMovimientosManuales) {
		this.consultaCatalagoMovimientosManuales = consultaCatalagoMovimientosManuales;
	}
}
