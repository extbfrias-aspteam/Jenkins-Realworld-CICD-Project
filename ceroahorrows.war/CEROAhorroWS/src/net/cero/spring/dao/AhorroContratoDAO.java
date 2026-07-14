package net.cero.spring.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import net.cero.data.SaldoAhorroSimplificadaOBJ;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroContrato;
import org.springframework.security.core.parameters.P;

@Log4j2
public class AhorroContratoDAO{	
	public static final Logger LOG = LogManager.getLogger(AhorroContratoDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String buscarCuentasXClienteTipoAhorro;
	private String buscarCuentasXCliente;
	private String buscarCuentaXReferencia;
	private String buscarCuentaByCuenta;
	private String actualizarAhorroContrato;
	private String obtenerNumeroCelular;
	private String obtenerSecuenciaCuenta;
	private String obtenerCuentaContable;
	private String obtenerSecuenciaContrato;
	private String ahorroGeneraReferencia;
	private String calculoGatByCuenta;
	private String actualizaGatAhorroContrato;
	private String nuevoAhorroContrato;
	private String sigSecAhorroContrato;
	private String borraAhorroContrato;
	private String ahorroCopiaRendimientos;
	private String actualizaAhorroRendimientosVigntes;
	private String actualizaCuentaClabe;	
	private String sigSecAcceso;
	private String generaTarjetaAhorro;
	private String actualizarNipAhorroContrato;
	private String buscarCuentasSimplificadasAll;
	private String obtenerSaldoAcumuladoCuentaSimplificada;
	private String obtenerCuentasProcrea;

	public void setActualizarAhorroContrato(String actualizarAhorroContrato) {
		this.actualizarAhorroContrato = actualizarAhorroContrato;
	}
	
public List<AhorroContrato> buscarCuentasXClienteTipoAhorro(String solicitante, Integer tipoAhorroId) {
		
		List<AhorroContrato> result = new ArrayList();
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentasXClienteTipoAhorro, solicitante,tipoAhorroId);

			for (Map<String, Object> row : rows) {
				obj.setAhorroContratoId((Integer) row.get("ahorro_contrato_id,"));
				obj.setSaldo((Double) row.get("saldo"));
				obj.setSucursalApertura((String) row.get("sucursal_apertura"));
				obj.setActividadId((Integer) row.get("actividad_id"));
				
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}
	
	public List<AhorroContrato> buscarCuentasXCliente(String solicitante) {
		
		List<AhorroContrato> result = new ArrayList();
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentasXCliente, solicitante);

			for (Map<String, Object> row : rows) {
				obj.setAhorroContratoId((Integer) row.get("ahorro_contrato_id,"));
				obj.setSaldo((Double) row.get("saldo"));
				obj.setSucursalApertura((String) row.get("sucursal_apertura"));
				obj.setActividadId((Integer) row.get("actividad_id"));
				
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}

	public List<AhorroContrato> buscarCuentaXReferencia(String referencia) {
		
		List<AhorroContrato> result = new ArrayList();
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentaXReferencia, referencia);

			for (Map<String, Object> row : rows) {
				obj.setAhorroContratoId((Integer) row.get("ahorro_contrato_id,"));
				obj.setSaldo((Double) row.get("saldo"));
				obj.setSucursalApertura((String) row.get("sucursal_apertura"));
				obj.setActividadId((Integer) row.get("actividad_id"));
				
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}

	public AhorroContrato buscarByCuenta(String cuenta) {
			
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentaByCuenta, cuenta);

			if (!rows.isEmpty()) {
				//obj.set((String) rows.get(0).get(""));
				//obj.set((Date) rows.get(0).get(""));
				//obj.set((Integer) rows.get(0).get(""));
				
				obj.setSolicitanteNombre((String) rows.get(0).get("nombre"));
				obj.setSolicitante((String) rows.get(0).get("SOLICITANTE_ID"));
				obj.setCuenta((String) rows.get(0).get("CUENTA"));
				obj.setPin((String) rows.get(0).get("PIN"));
				obj.setReferencia((String) rows.get(0).get("REFERENCIA"));
				obj.setContrato((String) rows.get(0).get("CONTRATO"));
				obj.setTipoAhorroId((Integer) rows.get(0).get("TIPO_AHORRO_ID"));
				obj.setRendimientoId((Integer) rows.get(0).get("RENDIMIENTO_ID"));
				obj.setFechaApertura((Date) rows.get(0).get("FECHA_APERTURA"));
				obj.setFechaDeposito((Date) rows.get(0).get("FECHA_DEPOSITO"));
				obj.setTitularId((String) rows.get(0).get("TITULAR_ID"));
				obj.setSucursalApertura((String) rows.get(0).get("SUCURSAL_APERTURA"));
				
				obj.setAhorroContratoId((Integer) rows.get(0).get("AHORRO_CONTRATO_ID,"));
				obj.setSaldo((Double) rows.get(0).get("SALDO"));
				
				obj.setActividadId((Integer) rows.get(0).get("ACTIVIDAD_ID"));
				obj.setSucursalId((Integer) rows.get(0).get("SUCURSAL_ID"));
				obj.setCuentaClabe((String) rows.get(0).get("CUENTA_CLABE"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}

	public Boolean actualizar(AhorroContrato ahorroContrato) {
		try{
			jdbcTemplatePr.update(actualizarAhorroContrato, ahorroContrato.getSaldo(),ahorroContrato.getCuenta());
		}catch(Exception e){
			return null;
		}
		
		return true;
	}
	
	public String obtenerNumeroCelular(String cuenta) {
		String numeroCelular = "";
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerNumeroCelular, cuenta);

			if (!rows.isEmpty()) {
				numeroCelular = (String) rows.get(0).get("celular");
			}else {
				log.info("#No encontro el numero de celular");
				numeroCelular = "";
			}

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			//log.info(e.getMessage());
		}
		
		return numeroCelular;
	}
	
	public String obtenerSecuenciaCuenta(){
		try {
			return jdbcTemplatePr.queryForObject(obtenerSecuenciaCuenta, String.class);
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String obtenerCuentaContable(Integer rendimientoId, Integer productoId){
		try {
			return jdbcTemplatePr.queryForObject(obtenerCuentaContable, new Object[]{rendimientoId, productoId}, String.class);
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String obtenerSecuenciaContrato(){
		try {
			return jdbcTemplatePr.queryForObject(obtenerSecuenciaContrato, String.class);
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String ahorroGeneraReferencia(String cuenta, Integer usuarioId, String sucursal){
		try {
			return jdbcTemplatePr.queryForObject(ahorroGeneraReferencia, new Object[]{cuenta, usuarioId, sucursal}, String.class);
		}catch(Exception e) {
			log.info(e.getMessage());
			return "";
		}
	}
	
	public Double calculoGatByCuenta(String cuenta){
		try {
			return jdbcTemplatePr.queryForObject(calculoGatByCuenta, new Object[]{cuenta}, Double.class);
		}catch(Exception e) {
			e.printStackTrace();
			return (double) 0;
		}
	}
	
	public void actualizaGatAhorroContrato(Double gat, String cuenta) {
		try {
			jdbcTemplatePr.update(actualizaGatAhorroContrato, gat, cuenta);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer nuevoAhorroContrato(AhorroContrato a) {
		try {
			Integer id = sigSecAhorroContrato();
			a.setAhorroContratoId(id);
			jdbcTemplatePr.update(nuevoAhorroContrato,a.getAhorroContratoId(),a.getCuenta(),a.getContrato(),a.getTipoAhorroId(),a.getRendimientoId(),a.getFechaApertura(),a.getFechaDeposito(),a.getSolicitante(),a.getTitularId(),a.getMonedaId(),a.getSucursalApertura(),a.getAsociacionId(),a.getDomicilio(),a.getNumeroCasa(),a.getColoniaId(),a.getCtaContable(),a.getEstatus(),a.getSaldo(),a.getReferencia(),a.getOficialId(),a.getCreadoPor(),a.getFechaCancelacion(),a.getCuentaPadre(),a.getCuentaDestinoCap(),a.getCuentaDestinoRen(),a.getPin(),a.getPinFechaCambio(),a.getPinUsuarioCambio(),a.getMontoApertura(),a.getExternaCuenta(),a.getExternaClabe(),a.getExternaTarjeta(),a.getExternaBanco(),a.getMetaMonto(),a.getMetaFecha(),a.getMetaAportacion(),a.getMetaPeriodo(),a.getMetaDestinoId(),a.getMetaMotivo(),a.getGat(),a.getCorreoEdocuenta(),a.getCuentaClabe(),a.getIdComoEntero(),a.getPinAuto(),a.getCentroTrabajo(),a.getPuesto(),a.getMontoMaxAhorro(),a.getIngresos(),a.getProvRecId(),a.getProvRecRelId(),a.getProvRecMontoMaxAhorro(),a.getProvRecIngresos(),a.getProvCentroTrabajo(),a.getProvPuesto(),a.getActividadId(),a.getGiroId(),a.getOcupacionId(),a.getProvOcupacionId(),a.getCveDestino(),a.getErrores(),a.getStatusBloqueo(),a.getHoraPrimerError(),a.getHoraBloqueo(),a.getNumBloqueo(),a.getRequiereIdentificador(),a.getRespaldoMd5());
			return a.getAhorroContratoId();
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer sigSecAhorroContrato(){
		try {
			return jdbcTemplatePr.queryForObject(sigSecAhorroContrato, Integer.class);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void borraAhorroContrato(Integer ahorroContratoId) {
		try {
			jdbcTemplatePr.update(borraAhorroContrato, ahorroContratoId);
		}catch(Exception e) {
			log.info(e.getMessage());
		}
	}
	
	public String ahorroCopiaRendimientos(String cuenta, Integer rendimientoId, Double interes, Integer usuarioId, Double montoApertuta) {
		try {
			return jdbcTemplatePr.queryForObject(ahorroCopiaRendimientos, new Object[]{cuenta,rendimientoId,interes,usuarioId,montoApertuta}, String.class);
		}catch(Exception e) {
			log.info(e.getMessage());
			return "ERROR";
		}
	}
	
	public void actualizaAhorroRendimientosVigntes(Integer usuarioId, Integer tipoCapitalizarId, String cuenta) {
		try {
			jdbcTemplatePr.update(actualizaAhorroRendimientosVigntes, usuarioId, tipoCapitalizarId, cuenta);
		}catch(Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Boolean actualizaCuentaClabe(AhorroContrato ahorroContrato) {
		try{
			jdbcTemplatePr.update(actualizaCuentaClabe, ahorroContrato.getCuentaClabe(),ahorroContrato.getAhorroContratoId());
		}catch(Exception e){
			log.info(e.getMessage());
			return null;
		}
		
		return true;
	}
	
	public Integer sigSecAcceso(){
		try {
			return jdbcTemplatePr.queryForObject(sigSecAcceso, Integer.class);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Integer generaTarjetaAhorro(String cuenta) {
		String resultQuery;
		Integer accesoId = 0;
		try {
			accesoId = sigSecAcceso();
			resultQuery = jdbcTemplatePr.queryForObject(generaTarjetaAhorro, new Object[] {cuenta, accesoId,""}, String.class);
			return accesoId;
		}catch(Exception e) {
			log.info(e.getMessage());
			return 0;
		}
	}
	
	public Boolean actualizarNipAhorroContrato(String cuenta, String pin) {
		try{
			jdbcTemplatePr.update(actualizarNipAhorroContrato, pin, cuenta);
		}catch(Exception e){
			log.info(e.getMessage());
			return null;
		}
		return true;
	}
	
	public List<AhorroContrato> buscarCuentasSimplificadasAll() {
		List<AhorroContrato> result = new ArrayList<AhorroContrato>();
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentasSimplificadasAll);

			for (Map<String, Object> row : rows) {
				//obj.set((String) rows.get(0).get(""));
				//obj.set((Date) rows.get(0).get(""));
				//obj.set((Integer) rows.get(0).get(""));
				obj = new AhorroContrato();
				obj.setSolicitanteNombre((String) row.get("nombre"));
				obj.setSolicitante((String) row.get("SOLICITANTE_ID"));
				obj.setCuenta((String) row.get("CUENTA"));
				obj.setPin((String) row.get("PIN"));
				obj.setReferencia((String) row.get("REFERENCIA"));
				obj.setContrato((String) row.get("CONTRATO"));
				obj.setTipoAhorroId((Integer) row.get("TIPO_AHORRO_ID"));
				obj.setRendimientoId((Integer) row.get("RENDIMIENTO_ID"));
				obj.setFechaApertura((Date) row.get("FECHA_APERTURA"));
				obj.setFechaDeposito((Date) row.get("FECHA_DEPOSITO"));
				obj.setTitularId((String) row.get("TITULAR_ID"));
				obj.setSucursalApertura((String) row.get("SUCURSAL_APERTURA"));
				
				obj.setAhorroContratoId((Integer) row.get("AHORRO_CONTRATO_ID,"));
				obj.setSaldo((Double) row.get("SALDO"));
				
				obj.setActividadId((Integer) row.get("ACTIVIDAD_ID"));
				obj.setSucursalId((Integer) row.get("SUCURSAL_ID"));
				obj.setCuentaClabe((String) row.get("CUENTA_CLABE"));
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}
	public SaldoAhorroSimplificadaOBJ obtenerSaldoAcumuladoCuentaSimplificada(String cuenta) {

		SaldoAhorroSimplificadaOBJ result = new SaldoAhorroSimplificadaOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerSaldoAcumuladoCuentaSimplificada, cuenta);

			for (Map<String, Object> row : rows) {
				result.setSaldoAcumulado((Double) row.get("saldo_acumulado"));
				result.setSaldoMensualDisponible((Double) row.get("disponible_mensual"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
			return null;
		}

		return result;
	}

	public List<Map<String, Object>> obtenerCuentasProcrea(String solicitanteId){
		List<Map<String, Object>> rows = new ArrayList<>();
		try {
			rows = jdbcTemplatePr.queryForList(obtenerCuentasProcrea, solicitanteId);
		}catch (Exception e){
			log.error("Error en obtenerCuentasProcrea: " + e);
			e.printStackTrace();

		}
		return rows;
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
	 * @return the buscarCuentasXCliente
	 */
	public String getBuscarCuentasXCliente() {
		return buscarCuentasXCliente;
	}

	/**
	 * @param buscarCuentasXCliente the buscarCuentasXCliente to set
	 */
	public void setBuscarCuentasXCliente(String buscarCuentasXCliente) {
		this.buscarCuentasXCliente = buscarCuentasXCliente;
	}

	/**
	 * @return the buscarCuentaXReferencia
	 */
	public String getBuscarCuentaXReferencia() {
		return buscarCuentaXReferencia;
	}

	/**
	 * @param buscarCuentaXReferencia the buscarCuentaXReferencia to set
	 */
	public void setBuscarCuentaXReferencia(String buscarCuentaXReferencia) {
		this.buscarCuentaXReferencia = buscarCuentaXReferencia;
	}

	/**
	 * @return the buscarCuentaByCuenta
	 */
	public String getBuscarCuentaByCuenta() {
		return buscarCuentaByCuenta;
	}

	/**
	 * @param buscarCuentaByCuenta the buscarCuentaByCuenta to set
	 */
	public void setBuscarCuentaByCuenta(String buscarCuentaByCuenta) {
		this.buscarCuentaByCuenta = buscarCuentaByCuenta;
	}

	/**
	 * @return the obtenerNumeroCelular
	 */
	public String getObtenerNumeroCelular() {
		return obtenerNumeroCelular;
	}

	/**
	 * @param obtenerNumeroCelular the obtenerNumeroCelular to set
	 */
	public void setObtenerNumeroCelular(String obtenerNumeroCelular) {
		this.obtenerNumeroCelular = obtenerNumeroCelular;
	}

	/**
	 * @return the obtenerSecuenciaCuenta
	 */
	public String getObtenerSecuenciaCuenta() {
		return obtenerSecuenciaCuenta;
	}

	/**
	 * @param obtenerSecuenciaCuenta the obtenerSecuenciaCuenta to set
	 */
	public void setObtenerSecuenciaCuenta(String obtenerSecuenciaCuenta) {
		this.obtenerSecuenciaCuenta = obtenerSecuenciaCuenta;
	}

	/**
	 * @return the actualizarAhorroContrato
	 */
	public String getActualizarAhorroContrato() {
		return actualizarAhorroContrato;
	}

	/**
	 * @return the obtenerCuentaContable
	 */
	public String getObtenerCuentaContable() {
		return obtenerCuentaContable;
	}

	/**
	 * @param obtenerCuentaContable the obtenerCuentaContable to set
	 */
	public void setObtenerCuentaContable(String obtenerCuentaContable) {
		this.obtenerCuentaContable = obtenerCuentaContable;
	}

	/**
	 * @return the obtenerSecuenciaContrato
	 */
	public String getObtenerSecuenciaContrato() {
		return obtenerSecuenciaContrato;
	}

	/**
	 * @param obtenerSecuenciaContrato the obtenerSecuenciaContrato to set
	 */
	public void setObtenerSecuenciaContrato(String obtenerSecuenciaContrato) {
		this.obtenerSecuenciaContrato = obtenerSecuenciaContrato;
	}

	/**
	 * @return the ahorroGeneraReferencia
	 */
	public String getAhorroGeneraReferencia() {
		return ahorroGeneraReferencia;
	}

	/**
	 * @param ahorroGeneraReferencia the ahorroGeneraReferencia to set
	 */
	public void setAhorroGeneraReferencia(String ahorroGeneraReferencia) {
		this.ahorroGeneraReferencia = ahorroGeneraReferencia;
	}

	/**
	 * @return the calculoGatByCuenta
	 */
	public String getCalculoGatByCuenta() {
		return calculoGatByCuenta;
	}

	/**
	 * @param calculoGatByCuenta the calculoGatByCuenta to set
	 */
	public void setCalculoGatByCuenta(String calculoGatByCuenta) {
		this.calculoGatByCuenta = calculoGatByCuenta;
	}

	/**
	 * @return the actualizaGatAhorroContrato
	 */
	public String getActualizaGatAhorroContrato() {
		return actualizaGatAhorroContrato;
	}

	/**
	 * @param actualizaGatAhorroContrato the actualizaGatAhorroContrato to set
	 */
	public void setActualizaGatAhorroContrato(String actualizaGatAhorroContrato) {
		this.actualizaGatAhorroContrato = actualizaGatAhorroContrato;
	}

	/**
	 * @return the nuevoAhorroContrato
	 */
	public String getNuevoAhorroContrato() {
		return nuevoAhorroContrato;
	}

	/**
	 * @param nuevoAhorroContrato the nuevoAhorroContrato to set
	 */
	public void setNuevoAhorroContrato(String nuevoAhorroContrato) {
		this.nuevoAhorroContrato = nuevoAhorroContrato;
	}

	/**
	 * @return the sigSecAhorroContrato
	 */
	public String getSigSecAhorroContrato() {
		return sigSecAhorroContrato;
	}

	/**
	 * @param sigSecAhorroContrato the sigSecAhorroContrato to set
	 */
	public void setSigSecAhorroContrato(String sigSecAhorroContrato) {
		this.sigSecAhorroContrato = sigSecAhorroContrato;
	}

	/**
	 * @return the borraAhorroContrato
	 */
	public String getBorraAhorroContrato() {
		return borraAhorroContrato;
	}

	/**
	 * @param borraAhorroContrato the borraAhorroContrato to set
	 */
	public void setBorraAhorroContrato(String borraAhorroContrato) {
		this.borraAhorroContrato = borraAhorroContrato;
	}

	/**
	 * @return the ahorroCopiaRendimientos
	 */
	public String getAhorroCopiaRendimientos() {
		return ahorroCopiaRendimientos;
	}

	/**
	 * @param ahorroCopiaRendimientos the ahorroCopiaRendimientos to set
	 */
	public void setAhorroCopiaRendimientos(String ahorroCopiaRendimientos) {
		this.ahorroCopiaRendimientos = ahorroCopiaRendimientos;
	}

	/**
	 * @return the actualizaAhorroRendimientosVigntes
	 */
	public String getActualizaAhorroRendimientosVigntes() {
		return actualizaAhorroRendimientosVigntes;
	}

	/**
	 * @param actualizaAhorroRendimientosVigntes the actualizaAhorroRendimientosVigntes to set
	 */
	public void setActualizaAhorroRendimientosVigntes(String actualizaAhorroRendimientosVigntes) {
		this.actualizaAhorroRendimientosVigntes = actualizaAhorroRendimientosVigntes;
	}

	/**
	 * @return the buscarCuentasXClienteTipoAhorro
	 */
	public String getBuscarCuentasXClienteTipoAhorro() {
		return buscarCuentasXClienteTipoAhorro;
	}

	/**
	 * @param buscarCuentasXClienteTipoAhorro the buscarCuentasXClienteTipoAhorro to set
	 */
	public void setBuscarCuentasXClienteTipoAhorro(String buscarCuentasXClienteTipoAhorro) {
		this.buscarCuentasXClienteTipoAhorro = buscarCuentasXClienteTipoAhorro;
	}

	/**
	 * @return the actualizaCuentaClabe
	 */
	public String getActualizaCuentaClabe() {
		return actualizaCuentaClabe;
	}

	/**
	 * @param actualizaCuentaClabe the actualizaCuentaClabe to set
	 */
	public void setActualizaCuentaClabe(String actualizaCuentaClabe) {
		this.actualizaCuentaClabe = actualizaCuentaClabe;
	}

	/**
	 * @return the sigSecAcceso
	 */
	public String getSigSecAcceso() {
		return sigSecAcceso;
	}

	/**
	 * @param sigSecAcceso the sigSecAcceso to set
	 */
	public void setSigSecAcceso(String sigSecAcceso) {
		this.sigSecAcceso = sigSecAcceso;
	}

	/**
	 * @return the generaTarjetaAhorro
	 */
	public String getGeneraTarjetaAhorro() {
		return generaTarjetaAhorro;
	}

	/**
	 * @param generaTarjetaAhorro the generaTarjetaAhorro to set
	 */
	public void setGeneraTarjetaAhorro(String generaTarjetaAhorro) {
		this.generaTarjetaAhorro = generaTarjetaAhorro;
	}

	public String getActualizarNipAhorroContrato() {
		return actualizarNipAhorroContrato;
	}

	public void setActualizarNipAhorroContrato(String actualizarNipAhorroContrato) {
		this.actualizarNipAhorroContrato = actualizarNipAhorroContrato;
	}

	/**
	 * @return the buscarCuentasSimplificadasAll
	 */
	public String getBuscarCuentasSimplificadasAll() {
		return buscarCuentasSimplificadasAll;
	}

	/**
	 * @param buscarCuentasSimplificadasAll the buscarCuentasSimplificadasAll to set
	 */
	public void setBuscarCuentasSimplificadasAll(String buscarCuentasSimplificadasAll) {
		this.buscarCuentasSimplificadasAll = buscarCuentasSimplificadasAll;
	}

	public String getObtenerSaldoAcumuladoCuentaSimplificada() {
		return obtenerSaldoAcumuladoCuentaSimplificada;
	}

	public void setObtenerSaldoAcumuladoCuentaSimplificada(String obtenerSaldoAcumuladoCuentaSimplificada) {
		this.obtenerSaldoAcumuladoCuentaSimplificada = obtenerSaldoAcumuladoCuentaSimplificada;
	}

	public String getObtenerCuentasProcrea() {
		return obtenerCuentasProcrea;
	}

	public void setObtenerCuentasProcrea(String obtenerCuentasProcrea) {
		this.obtenerCuentasProcrea = obtenerCuentasProcrea;
	}
}
