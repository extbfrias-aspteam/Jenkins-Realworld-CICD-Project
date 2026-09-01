package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroAlfrescoOBJ;

public class AhorroAlfrescoDAO {
public static final Logger LOG = LogManager.getLogger(AhorroAlfrescoDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String updateAhorroAlfresco;
	
	public boolean insertaAlfresco(AhorroAlfrescoOBJ objeto){
		int rows = 0;
		
		try {
			rows = jdbcTemplatePr.update(updateAhorroAlfresco, objeto.getCuenta(), objeto.getDocumentos_ahorro_id(),
					objeto.getRuta_alfresco()
					, objeto.getId_archivo_alfresco(), objeto.getObservaciones(), objeto.getNombre(),
					objeto.getFecha_expedicion(), objeto.getFecha_vigencia());
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getUpdateAhorroAlfresco() {
		return updateAhorroAlfresco;
	}
	public void setUpdateAhorroAlfresco(String updateAhorroAlfresco) {
		this.updateAhorroAlfresco = updateAhorroAlfresco;
	}
	
	
	
}