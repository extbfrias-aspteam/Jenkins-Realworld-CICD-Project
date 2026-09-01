package net.cero.spring.dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuditoriaDAO {
public static final Logger LOG = LogManager.getLogger(AuditoriaDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String queryInsertAuditoria;
	private String registrarAuditoria;
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getQueryInsertAuditoria() {
		return queryInsertAuditoria;
	}
	public void setQueryInsertAuditoria(String queryInsertAuditoria) {
		this.queryInsertAuditoria = queryInsertAuditoria;
	}
	
	public String getRegistrarAuditoria() {
        return registrarAuditoria;
    }

    public void setRegistrarAuditoria(String registrarAuditoria) {
        this.registrarAuditoria = registrarAuditoria;
    }

	public Boolean registrarAuditoriaCuentaAhorro(String cuenta, String nombrePC) {
		try{
			jdbcTemplatePr.update(queryInsertAuditoria, cuenta, nombrePC);
		}catch(Exception e){
			return null;
		}
		return true;
	}
	
	/**
     *
     * @param personaId
     * @param usuarioId
     * @param claveAct
     * @param ip
     * @param valorActual
     * @param valorNuevo
     * @param observaciones
     * @return
     */
    public Boolean registrarAuditoria(String personaId, int usuarioId, String claveAct, String ip, String valorActual, String valorNuevo, String observaciones) {
        Boolean res = true;

        try{
            jdbcTemplatePr.update(registrarAuditoria, personaId, usuarioId, claveAct, usuarioId, ip, valorActual, valorNuevo, observaciones);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return res;
    }
	
}
