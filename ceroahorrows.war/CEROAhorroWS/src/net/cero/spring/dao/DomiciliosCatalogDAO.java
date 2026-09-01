package net.cero.spring.dao;

import lombok.Data;
import net.cero.data.MinistraOBJ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class DomiciliosCatalogDAO {

	public static final Logger LOG = LogManager.getLogger(DomiciliosCatalogDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String obtenerCatalogoDomiciliosString;

	public List<Map<String, Object>> consultaDomiciliosCatalog(){
		List<Map<String, Object>> row;
		try {
			row = jdbcTemplatePr.queryForList(obtenerCatalogoDomiciliosString);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return row;
	}

}
