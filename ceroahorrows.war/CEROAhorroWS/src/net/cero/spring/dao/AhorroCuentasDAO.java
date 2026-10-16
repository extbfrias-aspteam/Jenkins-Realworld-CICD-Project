package net.cero.spring.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroSolicitanteOBJ;
import net.cero.data.ReporteContratoServiciosElectronicosOBJ;

public class AhorroCuentasDAO {
	/**
	 * Jdbc para las conexiones a CERO
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * Consulta para obtener los datos necesarios para generar el Contrato de
	 * Servicios Electrónicos, dicha consulta es inyectada por spring.
	 */
	private String queryDatosClienteCuentaAhorro;

	/**
	 * Jdbc para las conexiones a PROCREA
	 */
	private JdbcTemplate jdbcTemplatePr;

	private String queryDatosPersonaCuentaNoAhorro;
	
	private String obtenerMovimientoIdSpei;
	private String obtenerCuentasCero;

	/**
	 * Consulta la información necesaria para generar el reporte de Contrato de
	 * Servicios Electrónicos para una cuenta de ahorro.
	 * 
	 * @param cuenta Número de cuenta
	 * @return Datos que necesita el reporte para ser generado.
	 */
	public ReporteContratoServiciosElectronicosOBJ consultarDatosClienteCuentaAhorro(String cuenta) {

		ReporteContratoServiciosElectronicosOBJ obj = new ReporteContratoServiciosElectronicosOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(queryDatosClienteCuentaAhorro, cuenta);

			if (!rows.isEmpty()) {
				obj.setCuenta(cuenta);
				obj.setNombreCliente((String) rows.get(0).get("nombre"));
				obj.setCelular((String) rows.get(0).get("celular"));
				obj.setSucursalid((Integer) rows.get(0).get("region_id"));
				obj.setEmpresaid((Long) rows.get(0).get("empresa"));
			}

		} catch (EmptyResultDataAccessException e) {
			obj = null;
		}
		return obj;
	}

	/**
	 * Obtiene información parcial, necesaria para generar el reporte de Contrato de
	 * Servicios Electrónicos para una cuenta de debito; posterior a ejecutar esta
	 * consulta se requiere ejecutar SolicitanteDAO : obtenerDatosSolicitante para
	 * completar la información.
	 * 
	 * @param cuenta Número de cuenta
	 * @return Datos parciales para generar el reporte
	 */
	public ReporteContratoServiciosElectronicosOBJ consultarDatosClienteCuentaNoAhorro(String cuenta) {
		ReporteContratoServiciosElectronicosOBJ obj = new ReporteContratoServiciosElectronicosOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(queryDatosPersonaCuentaNoAhorro, cuenta);

			if (!rows.isEmpty()) {
				obj.setCuenta(cuenta);
				obj.setPersonaId((String) rows.get(0).get("persona_id"));
				obj.setSucursalid((Integer) rows.get(0).get("sucursal_id"));
				obj.setEmpresaid((Long) rows.get(0).get("idempresa"));
			}
		} catch (EmptyResultDataAccessException e) {
			obj = null;
		}
		return obj;
	}
	
	public AhorroSolicitanteOBJ buscarSolicitanteAhorro(String cuenta){
		AhorroSolicitanteOBJ obj=null;
		List<Map<String,Object>> rows;
		
		try{
			rows=jdbcTemplatePr.queryForList(queryDatosClienteCuentaAhorro, cuenta);
			
			if(!rows.isEmpty()){
				Map<String,Object> row=rows.get(0);
				
				obj=new AhorroSolicitanteOBJ();
				obj.setCuenta(cuenta);
				obj.setPersonaId((String)row.get("numero"));
				
			}
			
			return obj;
			
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	public AhorroSolicitanteOBJ buscarSolicitanteDebito(String cuenta){
		AhorroSolicitanteOBJ obj=null;
		List<Map<String,Object>> rows;
		
		try{
			rows=jdbcTemplate.queryForList(queryDatosPersonaCuentaNoAhorro, cuenta);
			
			if(!rows.isEmpty()){
				Map<String,Object> row=rows.get(0);
				
				obj=new AhorroSolicitanteOBJ();
				obj.setCuenta(cuenta);
				obj.setPersonaId((String)row.get("persona_id"));
				
			}
			
			return obj;
			
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	/**
	 * 
	 * @param idSpei
	 * @param tipoTransaccion
	 * @return
	 */
	public String obtenerMovimientoIdSpei(int idSpei, String tipoTransaccion) {
		Map<String, Object> row = new HashMap<String, Object>();
		String fecha;
		try {
			row = jdbcTemplatePr.queryForMap(obtenerMovimientoIdSpei, idSpei, tipoTransaccion);
			fecha = (String) row.get("fecha_creacion");
		} catch(Exception e) {
			e.printStackTrace();
			fecha = null;
		}
		return fecha;
	}
	public List<Map<String, Object>> obtenerCuentasCero(String solicitanteId){
		List<Map<String, Object>> rows = new ArrayList<>();
		try {
			rows = jdbcTemplate.queryForList(obtenerCuentasCero, solicitanteId);
		}catch (Exception e){
			e.printStackTrace();

		}
		return rows;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getQueryDatosClienteCuentaAhorro() {
		return queryDatosClienteCuentaAhorro;
	}

	public void setQueryDatosClienteCuentaAhorro(String queryDatosClienteCuentaAhorro) {
		this.queryDatosClienteCuentaAhorro = queryDatosClienteCuentaAhorro;
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getQueryDatosPersonaCuentaNoAhorro() {
		return queryDatosPersonaCuentaNoAhorro;
	}

	public void setQueryDatosPersonaCuentaNoAhorro(String queryDatosPersonaCuentaNoAhorro) {
		this.queryDatosPersonaCuentaNoAhorro = queryDatosPersonaCuentaNoAhorro;
	}

	public String getObtenerCuentasCero() {
		return obtenerCuentasCero;
	}

	public void setObtenerCuentasCero(String obtenerCuentasCero) {
		this.obtenerCuentasCero = obtenerCuentasCero;
	}
}
