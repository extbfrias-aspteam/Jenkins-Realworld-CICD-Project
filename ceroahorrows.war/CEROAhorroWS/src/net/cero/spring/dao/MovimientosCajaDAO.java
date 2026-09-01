package net.cero.spring.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.MovimientosCaja;

@Stateless
public class MovimientosCajaDAO {	
	public static final Logger LOG = LogManager.getLogger(MovimientosCajaDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSeqMC;
	private String nuevoMC;
	private String findMovimientoByCuentaAndFechas;
	private String findMovimientoByReferenciaAndFechas;
	private String obtenerIdeAnterior;
	private String obtenerMovimientoId;
	private String findMovimientoById;
	private String actualizaMovimiento;
	private String obtenerMovimientoClaveRastreo;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getSigSeqMC() {
		return sigSeqMC;
	}

	public void setSigSeqMC(String sigSeqMC) {
		this.sigSeqMC = sigSeqMC;
	}

	public String getNuevoMC() {
		return nuevoMC;
	}

	public void setNuevoMC(String nuevoMC) {
		this.nuevoMC = nuevoMC;
	}
	
	public String getFindMovimientoByCuentaAndFechas() {
		return findMovimientoByCuentaAndFechas;
	}

	public void setFindMovimientoByCuentaAndFechas(String findMovimientoByCuentaAndFechas) {
		this.findMovimientoByCuentaAndFechas = findMovimientoByCuentaAndFechas;
	}

	public String getFindMovimientoByReferenciaAndFechas() {
		return findMovimientoByReferenciaAndFechas;
	}

	public void setFindMovimientoByReferenciaAndFechas(String findMovimientoByReferenciaAndFechas) {
		this.findMovimientoByReferenciaAndFechas = findMovimientoByReferenciaAndFechas;
	}
	
	public String getObtenerIdeAnterior() {
		return obtenerIdeAnterior;
	}

	public void setObtenerIdeAnterior(String obtenerIdeAnterior) {
		this.obtenerIdeAnterior = obtenerIdeAnterior;
	}
	
	public String getFindMovimientoById() {
		return findMovimientoById;
	}

	public void setFindMovimientoById(String findMovimientoById) {
		this.findMovimientoById = findMovimientoById;
	}
	
	public String getActualizaMovimiento() {
		return actualizaMovimiento;
	}

	public void setActualizaMovimiento(String actualizaMovimiento) {
		this.actualizaMovimiento = actualizaMovimiento;
	}
	
	private Integer sigSeq(){
		return jdbcTemplatePr.queryForObject(sigSeqMC, Integer.class);
	}
	
	public String getObtenerMovimientoId() {
		return obtenerMovimientoId;
	}

	public void setObtenerMovimientoId(String obtenerMovimientoId) {
		this.obtenerMovimientoId = obtenerMovimientoId;
	}

	public String getObtenerMovimientoClaveRastreo() {
		return obtenerMovimientoClaveRastreo;
	}

	public void setObtenerMovimientoClaveRastreo(String obtenerMovimientoClaveRastreo) {
		this.obtenerMovimientoClaveRastreo = obtenerMovimientoClaveRastreo;
	}

	public Integer nuevo(MovimientosCaja mc) {
		try{
			mc.setMovimientoId(sigSeq());
			jdbcTemplatePr.update(nuevoMC,mc.getMovimientoId(),mc.getCajaId(),mc.getCajeroId(),mc.getFecha(),mc.getTipoMovId(),mc.getMonedaId(),mc.getMonto(),mc.getTipoCambio(),mc.getFormaPago(),mc.getCuenta(),mc.getControl(),mc.getCreadoPor(),mc.getFechaCreacion(),mc.getModificadoPor(),mc.getFechaModificacion(),mc.getBancoId(),mc.getCheque(),mc.getEstatus(),mc.getObs(),mc.getPoliza(),mc.getReferenciaMovimientoId(),mc.getBancoOrigen(),mc.getFechaDeposito(),mc.getPolizaProvision(),mc.getTransaccionId(),mc.getTarjetaOperativaOd(),mc.getRegionId(),mc.getIdImportacion(),mc.getMonedaValor(),mc.getMonedaNombre(),mc.getMonedaFechaValor(),mc.getMontoOriginal(),mc.getMontoDestino(),mc.getMonedaError());
		}catch(Exception e){
			return null;
		}
		
		return mc.getMovimientoId();
	}
	
	public List<MovimientosCaja> findByCuentaAndFechas(String cuenta, Date fechaDesde, Date fechaHasta){
		
		List<MovimientosCaja> result = new ArrayList();
		MovimientosCaja obj = new MovimientosCaja();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(findMovimientoByCuentaAndFechas, cuenta,fechaDesde,fechaHasta);

			for (Map<String, Object> row : rows) {
				obj.setMovimientoId((Integer) row.get("movimiento_id"));
				obj.setCajaId((Integer) row.get("caja_id"));
				obj.setCajeroId((Integer) row.get("cajero_id"));
				obj.setFecha((Date) row.get("fecha"));
				obj.setTipoMovId((Integer) row.get("tipo_mov_id"));
				obj.setMonedaId((Integer) row.get("moneda_id"));
				obj.setMonto((Double) row.get("monto"));
				obj.setTipoCambio((Double) row.get("tipo_cambio"));
				obj.setFormaPago((Integer) row.get("forma_pago"));
				obj.setCuenta((String) row.get("cuenta"));
				obj.setControl((String) row.get("control"));
				obj.setCreadoPor((Integer) row.get("creado_por"));
				obj.setFechaCreacion((Timestamp) row.get("fecha_creacion"));
				obj.setModificadoPor((Integer) row.get("modificado_por"));
				obj.setFechaModificacion((Timestamp) row.get("fecha_modificacion"));
				obj.setBancoId((Integer) row.get("banco_id"));
				obj.setCheque((String) row.get("cheque"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setObs((String) row.get("obs"));
				obj.setPoliza((String) row.get("poliza"));
				obj.setReferenciaMovimientoId((Integer) row.get("referencia_movimiento_id"));
				obj.setBancoOrigen((Integer) row.get("banco_origen"));
				obj.setFechaDeposito((Date) row.get("fecha_deposito"));
				obj.setPolizaProvision((String) row.get("poliza_provision"));
				obj.setTransaccionId((String) row.get("transaccion_id"));
				obj.setTarjetaOperativaOd((String) row.get("tarjeta_operativa_id"));
				obj.setRegionId((Integer) row.get("region_id"));
				obj.setIdImportacion((Long) row.get("id_importacion"));
				obj.setMonedaValor((Double) row.get("moneda_valor"));
				obj.setMonedaNombre((String) row.get("moneda_nombre"));
				obj.setMonedaFechaValor((Date) row.get("moneda_fecha_valor"));
				obj.setMontoOriginal((Double) row.get("monto_original"));
				obj.setMontoDestino((Double) row.get("monto_destino"));
				obj.setMonedaError((String) row.get("moneda_error"));
				
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}
	
	public Double obtenerIdeAnterior(String cuenta, Date fechaDesde, Date fechaHasta){
		Double ideAnt = (double) 0;
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplatePr.queryForList(obtenerIdeAnterior, cuenta,fechaDesde,fechaHasta);

			if (!rows.isEmpty()) {
				ideAnt = (Double) rows.get(0).get("ide_ant");
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}
		return ideAnt;
	}

	public List<MovimientosCaja> findByReferenciaAndFechas(Integer movimientoId, Date fechaDesde, Date fechaHasta) {
		List<MovimientosCaja> result = new ArrayList();
		MovimientosCaja obj = new MovimientosCaja();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(findMovimientoByReferenciaAndFechas, movimientoId,fechaDesde,fechaHasta);

			for (Map<String, Object> row : rows) {
				obj.setMovimientoId((Integer) row.get("movimiento_id"));
				obj.setCajaId((Integer) row.get("caja_id"));
				obj.setCajeroId((Integer) row.get("cajero_id"));
				obj.setFecha((Date) row.get("fecha"));
				obj.setTipoMovId((Integer) row.get("tipo_mov_id"));
				obj.setMonedaId((Integer) row.get("moneda_id"));
				obj.setMonto((Double) row.get("monto"));
				obj.setTipoCambio((Double) row.get("tipo_cambio"));
				obj.setFormaPago((Integer) row.get("forma_pago"));
				obj.setCuenta((String) row.get("cuenta"));
				obj.setControl((String) row.get("control"));
				obj.setCreadoPor((Integer) row.get("creado_por"));
				obj.setFechaCreacion((Timestamp) row.get("fecha_creacion"));
				obj.setModificadoPor((Integer) row.get("modificado_por"));
				obj.setFechaModificacion((Timestamp) row.get("fecha_modificacion"));
				obj.setBancoId((Integer) row.get("banco_id"));
				obj.setCheque((String) row.get("cheque"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setObs((String) row.get("obs"));
				obj.setPoliza((String) row.get("poliza"));
				obj.setReferenciaMovimientoId((Integer) row.get("referencia_movimiento_id"));
				obj.setBancoOrigen((Integer) row.get("banco_origen"));
				obj.setFechaDeposito((Date) row.get("fecha_deposito"));
				obj.setPolizaProvision((String) row.get("poliza_provision"));
				obj.setTransaccionId((String) row.get("transaccion_id"));
				obj.setTarjetaOperativaOd((String) row.get("tarjeta_operativa_id"));
				obj.setRegionId((Integer) row.get("region_id"));
				obj.setIdImportacion((Long) row.get("id_importacion"));
				obj.setMonedaValor((Double) row.get("moneda_valor"));
				obj.setMonedaNombre((String) row.get("moneda_nombre"));
				obj.setMonedaFechaValor((Date) row.get("moneda_fecha_valor"));
				obj.setMontoOriginal((Double) row.get("monto_original"));
				obj.setMontoDestino((Double) row.get("monto_destino"));
				obj.setMonedaError((String) row.get("moneda_error"));
				
				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return result;
	}

	public Integer obtenerMovimientoId(String cuentaAhorro, Double monto, Date fecha) {
		try{
		return jdbcTemplatePr.queryForObject(obtenerMovimientoId, Integer.class, cuentaAhorro, monto, fecha, fecha, fecha, fecha, fecha, fecha);
		}catch(Exception e){
			LOG.error(e.getMessage());
			return null;
		}
	}
	
	public MovimientosCaja findMovimientoById(Integer movimientoId) {
		MovimientosCaja obj = new MovimientosCaja();
		List<Map<String, Object>> row;

		try {
			row = jdbcTemplatePr.queryForList(findMovimientoById, movimientoId);

			if(row != null){
				obj.setMovimientoId((Integer) row.get(0).get("movimiento_id"));
				obj.setCajaId((Integer) row.get(0).get("caja_id"));
				obj.setCajeroId((Integer) row.get(0).get("cajero_id"));
				obj.setFecha((Date) row.get(0).get("fecha"));
				obj.setTipoMovId((Integer) row.get(0).get("tipo_mov_id"));
				obj.setMonedaId((Integer) row.get(0).get("moneda_id"));
				obj.setMonto((Double) row.get(0).get("monto"));
				obj.setTipoCambio((Double) row.get(0).get("tipo_cambio"));
				obj.setFormaPago((Integer) row.get(0).get("forma_pago"));
				obj.setCuenta((String) row.get(0).get("cuenta"));
				obj.setControl((String) row.get(0).get("control"));
				obj.setCreadoPor((Integer) row.get(0).get("creado_por"));
				obj.setFechaCreacion((Timestamp) row.get(0).get("fecha_creacion"));
				obj.setModificadoPor((Integer) row.get(0).get("modificado_por"));
				obj.setFechaModificacion((Timestamp) row.get(0).get("fecha_modificacion"));
				obj.setBancoId((Integer) row.get(0).get("banco_id"));
				obj.setCheque((String) row.get(0).get("cheque"));
				obj.setEstatus((String) row.get(0).get("estatus"));
				obj.setObs((String) row.get(0).get("obs"));
				obj.setPoliza((String) row.get(0).get("poliza"));
				obj.setReferenciaMovimientoId((Integer) row.get(0).get("referencia_movimiento_id"));
				obj.setBancoOrigen((Integer) row.get(0).get("banco_origen"));
				obj.setFechaDeposito((Date) row.get(0).get("fecha_deposito"));
				obj.setPolizaProvision((String) row.get(0).get("poliza_provision"));
				obj.setTransaccionId((String) row.get(0).get("transaccion_id"));
				obj.setTarjetaOperativaOd((String) row.get(0).get("tarjeta_operativa_id"));
				obj.setRegionId((Integer) row.get(0).get("region_id"));
				obj.setIdImportacion((Long) row.get(0).get("id_importacion"));
				obj.setMonedaValor((Double) row.get(0).get("moneda_valor"));
				obj.setMonedaNombre((String) row.get(0).get("moneda_nombre"));
				obj.setMonedaFechaValor((Date) row.get(0).get("moneda_fecha_valor"));
				obj.setMontoOriginal((Double) row.get(0).get("monto_original"));
				obj.setMontoDestino((Double) row.get(0).get("monto_destino"));
				obj.setMonedaError((String) row.get(0).get("moneda_error"));
				
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}

	public void actualizaMovimiento(MovimientosCaja mc) {
		try{
			jdbcTemplatePr.update(actualizaMovimiento,mc.getEstatus(),mc.getBancoId(),mc.getBancoId(),mc.getFechaDeposito(), mc.getModificadoPor());
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
	}

	/**
	 * 
	 * @param claveRastreo
	 * @return
	 */
	public String obtenerMovimientoClaveRastreo(String claveRastreo) {
		Map<String, Object> row = new HashMap<String, Object>();
		String fecha;
		try {
			row = jdbcTemplatePr.queryForMap(obtenerMovimientoClaveRastreo, claveRastreo);
			fecha = (String) row.get("fecha_creacion");
		} catch(Exception e) {
			LOG.error(e.getMessage());
			fecha = null;
		}
		return fecha;
	}
	
	
}
