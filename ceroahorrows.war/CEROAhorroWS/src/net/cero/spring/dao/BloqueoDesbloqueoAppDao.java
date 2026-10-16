package net.cero.spring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.spring.dao.excepcion.DaoException;

@Log4j2
public class BloqueoDesbloqueoAppDao {
	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplate;

	private String consultaSolicitante;
	private String bloqueoDesbloqueoApp;
	private String consultaBloqueoDesbloqueoApp;
	private String desbloqueoTotalCuenta;
	private String desbloqueoTotalCuentaId;
	private String consultaBloqueoDesbloqueoAppCta;
	private String consultaRegistroCodiPorCuentaYSolicitantes;


	public String consultaBloqueoDesbloqueoApp(String idSolicitante) throws DaoException {
		try {

			return jdbcTemplate.queryForMap(consultaBloqueoDesbloqueoApp, String.valueOf(idSolicitante)).get("bloqueado").toString();
		} catch (DataAccessException e) {
			log.error("Error al consultar el bloqueo ", e);
			throw new DaoException("Error al consultar el bloqueo");
		}
	}
	
	/**
	 * Consulta si la cuenta indicada esta bloqueada
	 * 
	 * @param idSolicitante
	 * @param cuenta
	 * @return
	 * @throws DaoException
	 */
	public String consultaBloqueoDesbloqueoApp(String idSolicitante, String cuenta) throws DaoException {
	    Object bloqueado = null;
	    String salida=null;
	    try {
		log.debug(consultaBloqueoDesbloqueoAppCta);
		bloqueado = jdbcTemplate
			.queryForMap(consultaBloqueoDesbloqueoAppCta, String.valueOf(idSolicitante), cuenta)
			.get("bloqueado");

	    } catch (DataAccessException e) {
		log.error("Error al consultar el bloqueo ", e);
		throw new DaoException("Error al consultar el bloqueo");
	    }
	    if (null!=bloqueado) {
		salida=bloqueado.toString();
	    }
	    return salida;
	}
	
	public void bloqueoDesbloqueoApp(final String idSolicitante, int bloqueo) throws DaoException {

		try {
			jdbcTemplate.update(bloqueoDesbloqueoApp, bloqueo, String.valueOf(idSolicitante));
		} catch (DataAccessException e) {
			log.error("Error al actualizar el bloqueo/desbloqueo ", e);
			throw new DaoException("Error al actualizar el bloqueo/desbloqueo");
		}
	}

	public void desbloqueoTotalApp(String idSolicitante, int desbloqueo) throws DaoException {
		try {
			jdbcTemplate.update(desbloqueoTotalCuenta, desbloqueo, String.valueOf(idSolicitante));
		} catch (DataAccessException e) {
			log.error("Error al actualizar el bloqueo/desbloqueo total", e);
			throw new DaoException("Error al actualizar el bloqueo/desbloqueo");
		}
	}
	
	/**
	 * Desbloque la aplicacion usando el ID del solicitante y la cuenta
	 * @param idSolicitante
	 * @param cuenta
	 * @param desbloqueaAspPagoApp
	 * @throws DaoException
	 */
	public void desbloqueoTotalApp(String idSolicitante, String cuenta, int desbloqueaAspPagoApp)
		throws DaoException {
	    try {
		log.debug(desbloqueoTotalCuentaId);
		jdbcTemplate.update(desbloqueoTotalCuentaId, desbloqueaAspPagoApp, String.valueOf(idSolicitante), cuenta);
	    } catch (DataAccessException e) {
		log.error("Error al actualizar el bloqueo/desbloqueo total", e);
		throw new DaoException("Error al actualizar el bloqueo/desbloqueo");
	    }

	}
	

	public Map<String, Object> consultaSolicitante(String numeroTelefono) throws DaoException {
		return consultaSolicitantel(numeroTelefono).get(0);
	}
	
	
	/**
	 * RQ000069 se retorna el listado para vaidar si existe la cuenta asociada al telefono.
	 * @param numeroTelefono
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>> consultaSolicitantel(String numeroTelefono) throws DaoException {
	    List<Map<String, Object>> solicitanteList = new ArrayList<>();
	    try {
		log.info(consultaSolicitante);
		solicitanteList = jdbcTemplatePr.queryForList(consultaSolicitante, numeroTelefono);
		if (solicitanteList.isEmpty()) {
		    log.info("El número telefónico no existe");
		    throw new DaoException("El número telefónico no existe");
		}

	    } catch (DataAccessException e) {
		log.error("Error al consultar el solicitante", e);
		throw new DaoException("El número telefónico no existe");
	    }
	    return solicitanteList;
	}
	
	
	/**
	 * Consulta registros en codi.registro_codi filtrados por cuenta y lista de solicitante_id.
	 * @param cuenta Número de cuenta
	 * @param solicitantesList Lista de Map, cada Map debe contener la clave "solicitante_id"
	 * @return Lista de Map con los resultados
	 * @throws DaoException
	 */
	public List<Map<String, Object>> consultaRegistroCodiPorCuentaYSolicitantes(String cuenta, List<Map<String, Object>> solicitantesList) throws DaoException {
	    try {
	        // Extraer los solicitante_id en una lista de Strings
	        List<String> solicitanteIds = new ArrayList<>();
	        for (Map<String, Object> map : solicitantesList) {
	            Object idObj = map.get("id_solicitante");
	            if (idObj != null) {
	                solicitanteIds.add(String.valueOf(idObj));
	            }
	        }

	        if (solicitanteIds.isEmpty()) {
	            log.warn("La lista de solicitantes está vacía, no se ejecuta consulta");
	            return new ArrayList<>();
	        }

	        // Construir placeholders "?, ?, ..." usando StringBuilder
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < solicitanteIds.size(); i++) {
	            if (i > 0) sb.append(",");
	            sb.append("?");
	        }
	        String placeholders = sb.toString();
	        
	        String query = String.format(consultaRegistroCodiPorCuentaYSolicitantes, placeholders);

	        // Parámetros: primero la cuenta, luego cada solicitanteId
	        List<Object> params = new ArrayList<>();
	        params.add(cuenta);
	        params.addAll(solicitanteIds);

	        log.debug("Ejecutando consulta: {}", query);
	        log.debug("Parámetros: {}", params);

	        return jdbcTemplate.queryForList(query, params.toArray());
	    } catch (DataAccessException e) {
	        log.error("Error al consultar registro_codi por cuenta y lista de solicitantes", e);
	        throw new DaoException("Error al consultar información de bloqueo/desbloqueo");
	    }
	}
	
	

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getConsultaSolicitante() {
		return consultaSolicitante;
	}

	public void setConsultaSolicitante(String consultaSolicitante) {
		this.consultaSolicitante = consultaSolicitante;
	}

	public String getBloqueoDesbloqueoApp() {
		return bloqueoDesbloqueoApp;
	}

	public void setBloqueoDesbloqueoApp(String bloqueoDesbloqueoApp) {
		this.bloqueoDesbloqueoApp = bloqueoDesbloqueoApp;
	}

	public String getConsultaBloqueoDesbloqueoApp() {
		return consultaBloqueoDesbloqueoApp;
	}

	public void setConsultaBloqueoDesbloqueoApp(String consultaBloqueoDesbloqueoApp) {
		this.consultaBloqueoDesbloqueoApp = consultaBloqueoDesbloqueoApp;
	}
	public String getDesbloqueoTotalCuenta() {
		return desbloqueoTotalCuenta;
	}
	public void setDesbloqueoTotalCuenta(String desbloqueoTotalCuenta) {
		this.desbloqueoTotalCuenta = desbloqueoTotalCuenta;
	}
	/**
	 * @return the desbloqueoTotalCuentaId
	 */
	public String getDesbloqueoTotalCuentaId() {
	    return desbloqueoTotalCuentaId;
	}
	/**
	 * @param desbloqueoTotalCuentaId the desbloqueoTotalCuentaId to set
	 */
	public void setDesbloqueoTotalCuentaId(String desbloqueoTotalCuentaId) {
	    this.desbloqueoTotalCuentaId = desbloqueoTotalCuentaId;
	}

	/**
	 * @return the consultaBloqueoDesbloqueoAppCta
	 */
	public String getConsultaBloqueoDesbloqueoAppCta() {
	    return consultaBloqueoDesbloqueoAppCta;
	}

	/**
	 * @param consultaBloqueoDesbloqueoAppCta the consultaBloqueoDesbloqueoAppCta to set
	 */
	public void setConsultaBloqueoDesbloqueoAppCta(String consultaBloqueoDesbloqueoAppCta) {
	    this.consultaBloqueoDesbloqueoAppCta = consultaBloqueoDesbloqueoAppCta;
	}

	/**
	 * @return the consultaRegistroCodiPorCuentaYSolicitantes
	 */
	public String getConsultaRegistroCodiPorCuentaYSolicitantes() {
	    return consultaRegistroCodiPorCuentaYSolicitantes;
	}

	/**
	 * @param consultaRegistroCodiPorCuentaYSolicitantes the consultaRegistroCodiPorCuentaYSolicitantes to set
	 */
	public void setConsultaRegistroCodiPorCuentaYSolicitantes(String consultaRegistroCodiPorCuentaYSolicitantes) {
	    this.consultaRegistroCodiPorCuentaYSolicitantes = consultaRegistroCodiPorCuentaYSolicitantes;
	}
	
	
}
