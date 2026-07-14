package net.cero.spring.dao;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Data
public class HistoricoDomiciliosDAO {

	public static final Logger LOG = LogManager.getLogger(HistoricoDomiciliosDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String insertarHistoricoDom;

	public boolean InsertarHistorico(String idSolicitante){
		LOG.info("Entrando a DAO");
		List<Map<String, Object>> row;
		try {
			jdbcTemplatePr.update(insertarHistoricoDom, idSolicitante);
		} catch(Exception e) {
			LOG.info("Ocurrió una excepción al tratar de insertar: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
