package net.cero.spring.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.EmpresaOBJ;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * Clase de la capa de datos usada para obtener info del listado de empresas de tarjetas que tenemos registrados en nuestros
 * catalogos
 */
@Repository
@Log4j2
public class EmpresaComboDAO {

	private final JdbcTemplate jdbcTemplate;
	private final String obtenerEmpresaCombo;

	public EmpresaComboDAO(@Qualifier("ceroJdbcTemplate") JdbcTemplate jdbcTemplate,
						   @Value("${empresa.dao.get}") String obtenerEmpresaCombo) {
		this.jdbcTemplate = jdbcTemplate;
		this.obtenerEmpresaCombo = obtenerEmpresaCombo;
	}

	public List<EmpresaOBJ> obtenerEmpresaCombo() {
		
		List<EmpresaOBJ> resultado = new ArrayList<>();
		List<Map<String,Object>> rows;
		
		try {
			rows = jdbcTemplate.queryForList(obtenerEmpresaCombo);
			
			for(Map<String,Object> objeto:rows) {
				EmpresaOBJ result = new EmpresaOBJ();
				result.setId((Integer) objeto.get("id"));
				result.setClaveEmpresa((String) objeto.get("clave_empresa"));
				result.setNombre((String) objeto.get("nombre_corto"));
				resultado.add(result);
			}
		}catch(EmptyResultDataAccessException e) {
			log.error(e.getMessage());
		}
		return resultado;
	}
}
