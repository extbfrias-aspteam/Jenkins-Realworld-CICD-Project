package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Errores;
import net.std.data.PlazasClabeOBJ;

public class PlazasClabeStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(PlazasClabeStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplateSti;
	private String LIST_PlazaClabeStd;
	private String UPDATE_PlazaClabeStd;
	private String LIST_PlazasPropiasClabeStd;
	
	public RespuestaSVC listPlazasClabeStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<PlazasClabeOBJ> lst = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(LIST_PlazaClabeStd);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					PlazasClabeOBJ obj = new PlazasClabeOBJ();
					obj.setId(Comun._T(row.get("ID")));
					obj.setClave(Comun._T(row.get("CLAVE")));
					obj.setPlaza(Comun._T(row.get("PLAZA")));
					obj.setPropias(Comun._T(row.get("PROPIAS")));
					obj.setDisponible(Comun._T(row.get("DISPONIBLE")));
					
					if(lst == null) lst = new ArrayList<>();
					lst.add(obj);
				}
			}
			
			if(lst != null){
				respuesta.getBody().addValor("PLAZAS", lst);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_PLAZAS,  Errores.desc(Errores.ERROR_LEER_PLAZAS));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC actualizarPlazasClabeStdDao(PlazasClabeOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_PlazaClabeStd,  Comun._I(obj.getDisponible()), 
																 Comun._I(obj.getUsuarioId()), 
																 Comun._T(obj.getClave()));
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_PLAZAS,  Errores.desc(Errores.ERROR_ACTUALIZAR_PLAZAS));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC listPlazasPropiasClabeStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<PlazasClabeOBJ> lst = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplateSti.queryForList(LIST_PlazasPropiasClabeStd);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					PlazasClabeOBJ obj = new PlazasClabeOBJ();
					obj.setId(Comun._T(row.get("ID")));
					obj.setClave(Comun._T(row.get("CLAVE")));
					obj.setPlaza(Comun._T(row.get("PLAZA")));
					
					if(lst == null) lst = new ArrayList<>();
					lst.add(obj);
				}
			}
			
			if(lst != null){
				respuesta.getBody().addValor("PLAZAS", lst);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_PLAZAS_PROPIAS,  Errores.desc(Errores.ERROR_LEER_PLAZAS_PROPIAS));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getLIST_PlazaClabeStd() {
		return LIST_PlazaClabeStd;
	}

	public void setLIST_PlazaClabeStd(String lIST_PlazaClabeStd) {
		LIST_PlazaClabeStd = lIST_PlazaClabeStd;
	}

	public String getUPDATE_PlazaClabeStd() {
		return UPDATE_PlazaClabeStd;
	}

	public void setUPDATE_PlazaClabeStd(String uPDATE_PlazaClabeStd) {
		UPDATE_PlazaClabeStd = uPDATE_PlazaClabeStd;
	}

	public String getLIST_PlazasPropiasClabeStd() {
		return LIST_PlazasPropiasClabeStd;
	}

	public void setLIST_PlazasPropiasClabeStd(String lIST_PlazasPropiasClabeStd) {
		LIST_PlazasPropiasClabeStd = lIST_PlazasPropiasClabeStd;
	}
}

