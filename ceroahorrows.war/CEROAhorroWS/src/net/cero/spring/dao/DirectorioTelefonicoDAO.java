package net.cero.spring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.DirectorioTelefonicoOBJ;

@Log4j2
public class DirectorioTelefonicoDAO{	
	public static final Logger LOG = LogManager.getLogger(DirectorioTelefonicoDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSecDirTel;
	private String nuevoDirectorioTelefonico;
	private String actualizaDirectorioTelefonico;
	private String obtenerTelfonoCelularCoDi;
	private String obtenerSolicitantePorTelefono;
	
	public Integer sigSecDirTel(){
		try {
			return jdbcTemplatePr.queryForObject(sigSecDirTel, Integer.class);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer nuevoDirectorioTelefonico(DirectorioTelefonicoOBJ d) {
		try {
			Integer id = sigSecDirTel();
			d.setIdDirectorioTelefonico(id);
			jdbcTemplatePr.update(nuevoDirectorioTelefonico,d.getIdDirectorioTelefonico(),d.getIdSolicitante(),d.getIdCatTelefono(),d.getIdCompaniaTel(),d.getTelefono(),d.getExtension(),d.getObservaciones(),d.getCreadoPor());
			return d.getIdDirectorioTelefonico();
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void actualizaDirectorioTelefonico(DirectorioTelefonicoOBJ d) {
		try {
			jdbcTemplatePr.update(actualizaDirectorioTelefonico,d.getTelefono(),d.getModificadoPor(),d.getIdDirectorioTelefonico());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int actualizaDirectorioTelefonico(String telefono, int modificadoPor, int idDirectorioTelefonico) {
		Integer res = 0;
		try {

			res = jdbcTemplatePr.update(actualizaDirectorioTelefonico,telefono, modificadoPor, idDirectorioTelefonico);

		}catch(Exception e) {

			e.printStackTrace();
		}
		return res;
	}
	
	public DirectorioTelefonicoOBJ obtenerTelfonoCelularCoDi(String idSolicitante) {
		List<Map<String, Object>> rows;
		DirectorioTelefonicoOBJ result = new DirectorioTelefonicoOBJ();
		
		try {
			rows = jdbcTemplatePr.queryForList(obtenerTelfonoCelularCoDi, idSolicitante);

			if (!rows.isEmpty()) {
				result.setIdDirectorioTelefonico((Integer) rows.get(0).get("id_directorio_telefonico"));
				result.setIdSolicitante((String) rows.get(0).get("id_solicitante"));
				result.setIdCatTelefono((Integer) rows.get(0).get("id_cat_telefono"));
				result.setIdCompaniaTel((Integer) rows.get(0).get("id_compania_tel"));
				result.setExtension((String) rows.get(0).get("extension"));
				result.setObservaciones((String) rows.get(0).get("observaciones"));
				result.setPortado((Integer) rows.get(0).get("portado"));
				result.setTelefono((String) rows.get(0).get("telefono"));
				log.info("#Si encontro el numero de celular");
			}else {
				log.info("#No encontro el numero de celular");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			//log.info(e.getMessage());
		}
		
		return result;
	}

	public List<String> obtenerSolicitantePorTelefono(String telefono) {
		List<String> solicitantes = new ArrayList<String>();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerSolicitantePorTelefono, telefono);

			for (Map<String, Object> row : rows){
				solicitantes.add((String) row.get("id_solicitante"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			//log.info(e.getMessage());
		}

		return solicitantes;
	}
	
	/**
	 * @return the jdbcTemplatePr
	 */
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	/**
	 * @param jdbcTemplatePr the jdbcTemplatePr to set
	 */
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	/**
	 * @return the sigSecDirTel
	 */
	public String getSigSecDirTel() {
		return sigSecDirTel;
	}

	/**
	 * @param sigSecDirTel the sigSecDirTel to set
	 */
	public void setSigSecDirTel(String sigSecDirTel) {
		this.sigSecDirTel = sigSecDirTel;
	}

	/**
	 * @return the nuevoDirectorioTelefonico
	 */
	public String getNuevoDirectorioTelefonico() {
		return nuevoDirectorioTelefonico;
	}

	/**
	 * @param nuevoDirectorioTelefonico the nuevoDirectorioTelefonico to set
	 */
	public void setNuevoDirectorioTelefonico(String nuevoDirectorioTelefonico) {
		this.nuevoDirectorioTelefonico = nuevoDirectorioTelefonico;
	}

	/**
	 * @return the actualizaDirectorioTelefonico
	 */
	public String getActualizaDirectorioTelefonico() {
		return actualizaDirectorioTelefonico;
	}

	/**
	 * @param actualizaDirectorioTelefonico the actualizaDirectorioTelefonico to set
	 */
	public void setActualizaDirectorioTelefonico(String actualizaDirectorioTelefonico) {
		this.actualizaDirectorioTelefonico = actualizaDirectorioTelefonico;
	}

	/**
	 * @return the obtenerTelfonoCelularCoDi
	 */
	public String getObtenerTelfonoCelularCoDi() {
		return obtenerTelfonoCelularCoDi;
	}

	/**
	 * @param obtenerTelfonoCelularCoDi the obtenerTelfonoCelularCoDi to set
	 */
	public void setObtenerTelfonoCelularCoDi(String obtenerTelfonoCelularCoDi) {
		this.obtenerTelfonoCelularCoDi = obtenerTelfonoCelularCoDi;
	}

	public String getObtenerSolicitantePorTelefono() {
		return obtenerSolicitantePorTelefono;
	}

	public void setObtenerSolicitantePorTelefono(String obtenerSolicitantePorTelefono) {
		this.obtenerSolicitantePorTelefono = obtenerSolicitantePorTelefono;
	}
}
