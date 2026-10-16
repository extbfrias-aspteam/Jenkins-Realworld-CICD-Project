package net.cero.spring.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.TarjetaOBJ;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Clase de la capa de datos para operaciones realizadas a consulta de plasticos del stock que se tienen
 */
@Repository
@Log4j2
public class TarjetaDAO {
	
	/**
	 * Jdbc para las conexiones a CERO
	 */
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final String obtenerListadoTarjetas;


	public TarjetaDAO(@Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate,
					  @Value("${tarjetas.dao.get.listado}") String obtenerListadoTarjetas) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.obtenerListadoTarjetas = obtenerListadoTarjetas;
	}

	public List<TarjetaOBJ> obtenerListadoTarjetas(int empresaId) {
		List<TarjetaOBJ> listadoFinal = new ArrayList<>();

		List<Map<String, Object>> rows;
		Map<String,Object> sqlParameters=new HashMap<>();
		sqlParameters.put("empresaId",empresaId);
		
		try {
			rows = namedParameterJdbcTemplate.queryForList(obtenerListadoTarjetas,sqlParameters);
			
			for(Map<String, Object> row: rows) {
				TarjetaOBJ result = new TarjetaOBJ();
				result.setPan((String) row.get("pan"));
				result.setEstatus((String)row.get("estatus"));
				result.setTipoTarjeta((String)row.get("tipo_tarjeta"));
				result.setCuenta((String)row.get("cuenta"));
				if(row.get("clave_empresa") != null)
					result.setClaveEmpresa((String)row.get("clave_empresa"));
				else
					result.setClaveEmpresa("");
				result.setNombreCorto((String)row.get("nombre_corto"));
				listadoFinal.add(result);
			}
			
		}catch(EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}		
		
		return listadoFinal;
	}
}
