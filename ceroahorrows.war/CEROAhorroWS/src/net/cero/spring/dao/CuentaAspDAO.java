package net.cero.spring.dao;

import java.util.List;
import java.util.Map;

import net.cero.model.CuentaAspOBJ;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.MedioDisposicion;
import net.cero.data.Respuesta;
import net.cero.spring.dao.excepcion.DaoException;

public class CuentaAspDAO {

	private static final Logger log = LogManager.getLogger(CuentaAspDAO.class);
	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplate;
	private String bloqueaCuentaAsp;
	private String desbloqueoCuentaAsp;
	private String buscaCuentaAsp;
	private String consultaBloqueoDesbloqueo;
	private String consultaCatalogoNivelCuenta;
	private String actualizaNivel;
	private String consultaNivel;
	private String consultaEstatusCuentaPorMes;
	private String consultaTotalEstatusCuentas;
	private String consultaCuentaPorCuentaOClabe;
	private String consultaCuentaPorCuentaOClabeProcrea;

	public void bloqueoCuenta(final String cuentaAsp) throws DaoException {
		try {
			Map<String, Object> estatusId = jdbcTemplate.queryForMap(this.consultaBloqueoDesbloqueo, cuentaAsp);

			if (Integer.parseInt(estatusId.get("estatus_id").toString()) == 3) {
				throw new DaoException("La cuenta ya ha sido bloqueada");
			} else {
				jdbcTemplate.queryForMap(this.buscaCuentaAsp, cuentaAsp);

				jdbcTemplate.update(this.bloqueaCuentaAsp, cuentaAsp);
			}
		} catch (DataAccessException e) {
			log.error("Error al acceder a datos de la cuenta", e);
			throw new DaoException("La cuenta no existe");
		}
	}

	public void desbloqueoCuenta(final String cuentaAsp) throws DaoException {
		
		try {
			Map<String, Object> estatusId = jdbcTemplate.queryForMap(this.consultaBloqueoDesbloqueo, cuentaAsp);
			
			if(Integer.parseInt(estatusId.get("estatus_id").toString()) == 1) {
				throw new DaoException("La cuenta se encuentra vigente");
			}else {
				jdbcTemplate.queryForMap(this.buscaCuentaAsp, cuentaAsp);
				
				jdbcTemplate.update(this.desbloqueoCuentaAsp, cuentaAsp);	
			}
			
		} catch (DataAccessException e) {
			throw new DaoException("La cuenta no existe");
		}	
	}
	
	public List<Map<String, Object>> consultaCatalogoNivelCuenta() throws DaoException {
		try {
			List<Map<String, Object>> nivelesCuentaRow = jdbcTemplate.queryForList(this.consultaCatalogoNivelCuenta);
			return nivelesCuentaRow;
		} catch (DataAccessException e) {
			log.error("Error al acceder a datos de la cuenta", e);
			throw new DaoException("No existe información de niveles de cuenta");
		}
	}

	public void cambiaNivelCuenta(final String cuentaAsp, final String nivelCuenta) throws DaoException{
		try {
			Map<String, Object> cuentaAspRow = jdbcTemplate.queryForMap(this.buscaCuentaAsp, cuentaAsp);
			
			consultaNivelCuenta(nivelCuenta);
			
			jdbcTemplate.update(this.actualizaNivel,nivelCuenta, Integer.valueOf(String.valueOf(cuentaAspRow.get("id"))));
		} catch (DataAccessException e) {
			throw new DaoException("La cuenta no existe");
		}
	}
	
	public void consultaNivelCuenta(final String nivelCuenta) throws DaoException{
		try {
			jdbcTemplate.queryForMap(this.consultaNivel, nivelCuenta);
		} catch (DataAccessException e) {
			throw new DaoException("El nivel proporcionado no existe");
		}
	}
	
	public Integer consultaCuentaPorMesYEstatusX(final String mes, final int anio, final int productoId, final String estatusCuenta){
		try {
			List<Map<String, Object>> row = jdbcTemplate.queryForList(this.consultaEstatusCuentaPorMes, Integer.valueOf(mes), anio, estatusCuenta, productoId);
			return row.size();
		} catch (DataAccessException e) {
			log.error("Error al consultar las cuentas por mes ", e);
			return 0;
		}
	}

	public List<Map<String, Object>> consultaListaProductos() throws DaoException {
		try {
			final String query = "select id id_producto, clave, descripcion  from productos.prproductos_ahorro p";
			return jdbcTemplate.queryForList(query);
		} catch (DataAccessException e) {
			log.error("error al consultar los datos ",e);
			throw new DaoException("No se encontró datos de los productos");
		}
	}

	public List<Map<String, Object>> consultaTotalEstatusCuentas(Integer anio, Integer productoId) throws DaoException{
		try {
			return jdbcTemplate.queryForList(String.format(this.consultaTotalEstatusCuentas, productoId, productoId,productoId));
		} catch (DataAccessException e) {
			log.error("error al consultar los datos ",e);
			throw new DaoException("No se encontró la cantidad de estatus de las cuentas");
		}
	}

	public CuentaAspOBJ buscaCuentaPorCuentaOCLABE(String valor)  {
		CuentaAspOBJ obj = null;
		try {

			List<Map<String, Object>> cuentasASP = jdbcTemplate.queryForList(consultaCuentaPorCuentaOClabe,valor,valor);
			if(cuentasASP != null && !cuentasASP.isEmpty())
				obj = inicializarCuentaOBJ(cuentasASP);
			else
			{
				cuentasASP = jdbcTemplatePr.queryForList(consultaCuentaPorCuentaOClabeProcrea,valor,valor);
				if(cuentasASP != null && !cuentasASP.isEmpty())
					obj = inicializarCuentaOBJ(cuentasASP);
			}
			return obj;
		} catch (DataAccessException e) {
			log.info("No se encontraron registros");
		}
		catch(Exception e)
		{
			log.error("Ocurrió un error interno durante la consulta buscaCuentaPorCuentaOCLABE",e);
		}
		return obj;
	}

	private CuentaAspOBJ inicializarCuentaOBJ(List<Map<String, Object>> cuentasASP)
	{
		CuentaAspOBJ obj = null;
		Map<String, Object> elemento = cuentasASP.get(0);
		obj = new CuentaAspOBJ();
		obj.setCuenta((String)elemento.get("cuenta"));
		obj.setEstatusId((int)elemento.get("estatus_id"));
		obj.setId((int)elemento.get("cuenta_id"));
		obj.setPersonaId((String)elemento.get("persona_id"));
		obj.setProductoAhorroId((int)elemento.get("producto_ahorro_id"));
		obj.setClabeInterbancaria((String)elemento.get("clabe_interbancaria"));
		obj.setEstatusDescripcion((String)elemento.get("estatusDescripcion"));
		return obj;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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

	public String getBloqueaCuentaAsp() {
		return bloqueaCuentaAsp;
	}

	public void setBloqueaCuentaAsp(String bloqueaCuentaAsp) {
		this.bloqueaCuentaAsp = bloqueaCuentaAsp;
	}

	public String getdesbloqueoCuentaAsp() {
		return desbloqueoCuentaAsp;
	}

	public void setdesbloqueoCuentaAsp(String desbloqueoCuentaAsp) {
		this.desbloqueoCuentaAsp = desbloqueoCuentaAsp;
	}

	public String getBuscaCuentaAsp() {
		return buscaCuentaAsp;
	}

	public void setBuscaCuentaAsp(String buscaCuentaAsp) {
		this.buscaCuentaAsp = buscaCuentaAsp;
	}

	public String getDesbloqueoCuentaAsp() {
		return desbloqueoCuentaAsp;
	}

	public void setDesbloqueoCuentaAsp(String desbloqueoCuentaAsp) {
		this.desbloqueoCuentaAsp = desbloqueoCuentaAsp;
	}

	public String getConsultaBloqueoDesbloqueo() {
		return consultaBloqueoDesbloqueo;
	}

	public void setConsultaBloqueoDesbloqueo(String consultaBloqueoDesbloqueo) {
		this.consultaBloqueoDesbloqueo = consultaBloqueoDesbloqueo;
	}

	public String getConsultaCatalogoNivelCuenta() {
		return consultaCatalogoNivelCuenta;
	}

	public void setConsultaCatalogoNivelCuenta(String consultaCatalogoNivelCuenta) {
		this.consultaCatalogoNivelCuenta = consultaCatalogoNivelCuenta;
	}

	public String getActualizaNivel() {
		return actualizaNivel;
	}

	public void setActualizaNivel(String actualizaNivel) {
		this.actualizaNivel = actualizaNivel;
	}

	public String getConsultaNivel() {
		return consultaNivel;
	}

	public void setConsultaNivel(String consultaNivel) {
		this.consultaNivel = consultaNivel;
	}

	public String getConsultaEstatusCuentaPorMes() {
		return consultaEstatusCuentaPorMes;
	}

	public void setConsultaEstatusCuentaPorMes(String consultaEstatusCuentaPorMes) {
		this.consultaEstatusCuentaPorMes = consultaEstatusCuentaPorMes;
	}

	public String getConsultaTotalEstatusCuentas() {
		return consultaTotalEstatusCuentas;
	}

	public void setConsultaTotalEstatusCuentas(String consultaTotalEstatusCuentas) {
		this.consultaTotalEstatusCuentas = consultaTotalEstatusCuentas;
	}

	public String getConsultaCuentaPorCuentaOClabe() {
		return consultaCuentaPorCuentaOClabe;
	}

	public void setConsultaCuentaPorCuentaOClabe(String consultaCuentaPorCuentaOClabe) {
		this.consultaCuentaPorCuentaOClabe = consultaCuentaPorCuentaOClabe;
	}

	public String getConsultaCuentaPorCuentaOClabeProcrea() {
		return consultaCuentaPorCuentaOClabeProcrea;
	}

	public void setConsultaCuentaPorCuentaOClabeProcrea(String consultaCuentaPorCuentaOClabeProcrea) {
		this.consultaCuentaPorCuentaOClabeProcrea = consultaCuentaPorCuentaOClabeProcrea;
	}
}
