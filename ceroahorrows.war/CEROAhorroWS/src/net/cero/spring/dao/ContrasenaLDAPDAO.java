package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.spring.dao.excepcion.DaoException;

public class ContrasenaLDAPDAO {

	public static Logger log = LoggerFactory.getLogger(ContrasenaLDAPDAO.class);
	
	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplatePr;
	private String consultaUsuario;
	
	public Map<String, Object> consultaUsuario(String usuario) throws DaoException{
		List<Map<String, Object>> usuarioList = this.jdbcTemplatePr.queryForList(consultaUsuario, usuario);
		if(usuarioList.size()>1){
			throw new DaoException("Existen más de un usuario con la misma referencia");
		}
		
		if(usuarioList.size() == 0) {
			throw new DaoException(String.format("No existe el usuario %s", usuario));
		}
		return usuarioList.get(0);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getConsultaUsuario() {
		return consultaUsuario;
	}
	public void setConsultaUsuario(String consultaUsuario) {
		this.consultaUsuario = consultaUsuario;
	}
	
	
}
