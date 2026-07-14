package net.cero.spring.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.AhorroRendimientoVigentes;


public class AhorroRendimientoVigenteDAO {
	public static final Logger LOG = LogManager.getLogger(AhorroRendimientoVigenteDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSeqARV;
	private String nuevoARV;
	private String buscarRendimientoByCuenta;
	private String actualizarARV;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getSigSeqARV() {
		return sigSeqARV;
	}

	public void setSigSeqARV(String sigSeqARV) {
		this.sigSeqARV = sigSeqARV;
	}

	public String getNuevoARV() {
		return nuevoARV;
	}

	public void setNuevoARV(String nuevoARV) {
		this.nuevoARV = nuevoARV;
	}

	public String getBuscarRendimientoByCuenta() {
		return buscarRendimientoByCuenta;
	}

	public void setBuscarRendimientoByCuenta(String buscarRendimientoByCuenta) {
		this.buscarRendimientoByCuenta = buscarRendimientoByCuenta;
	}

	public String getActualizarARV() {
		return actualizarARV;
	}

	public void setActualizarARV(String actualizarARV) {
		this.actualizarARV = actualizarARV;
	}
	
	private Integer sigSeq(){
		return jdbcTemplatePr.queryForObject(sigSeqARV, Integer.class);
	}

	public Integer nuevo(AhorroRendimientoVigentes arv) {
		try{
			arv.setRendimientoVigenteId(sigSeq());
			jdbcTemplatePr.update(nuevoARV, arv.getRendimientoVigenteId(),arv.getCuenta(),arv.getTipoAhorroId(),arv.getPeriodicidad(),arv.getPlazo(),arv.getCalculoIva(),arv.getTasaInt(),arv.getTasaId(),arv.getBase(),arv.getFormula(),
					arv.getTipoCorte(),arv.getDeposito(),arv.getNumDias(),arv.getDiasGracia(),arv.getCtaContable(),arv.getVigenciaDesde(),arv.getVigenciaHasta(),arv.getCondicionesRetiros(),arv.getCondicionesApertura(),arv.getCreadoPor(),
					arv.getFechaCreacion(),arv.getModificadoPor(),arv.getFechaModificacion(),arv.getRendimientoId(),arv.getGat(),arv.getTipoCapitalizarId(),arv.getNoDisposicion(),arv.getEstatus(),arv.getTipo(),arv.getCapital(),
					arv.getFechaInicio(),arv.getFechaFinal(),arv.getInteres(),arv.getFechaDeposito());
		}catch(Exception e){
			return null;
		}
		
		return arv.getRendimientoVigenteId();
	}
	
	public AhorroRendimientoVigentes buscarByCuenta(String cuenta) {
		
		
		AhorroRendimientoVigentes obj = new AhorroRendimientoVigentes();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarRendimientoByCuenta, cuenta);

			if (!rows.isEmpty()) {
				obj.setRendimientoVigenteId((Integer) rows.get(0).get("rendimiento_vigente_id"));
				obj.setCuenta((String) rows.get(0).get("cuenta"));
				obj.setTipoAhorroId((Integer) rows.get(0).get("tipo_ahorro_id"));
				obj.setPeriodicidad((Integer) rows.get(0).get("periodicidad"));
				obj.setPlazo((Integer) rows.get(0).get("plazo"));
				obj.setTipoTasa((String) rows.get(0).get("tipo_tasa"));
				obj.setCalculoIva((String) rows.get(0).get("calculo_iva"));
				obj.setTasaInt((Double) rows.get(0).get("tasa_int"));
				obj.setTasaId((Integer) rows.get(0).get("tasa_id"));
				obj.setBase((Integer) rows.get(0).get("base"));
				obj.setFormula((String) rows.get(0).get("formula"));
				obj.setTipoCorte((String) rows.get(0).get("tipo_corte"));
				obj.setDeposito((String) rows.get(0).get("deposito"));
				obj.setNumDias((Integer) rows.get(0).get("num_dias"));
				obj.setDiasGracia((Integer) rows.get(0).get("dias_gracia"));
				obj.setCtaContable((String) rows.get(0).get("cta_contable"));
				obj.setVigenciaDesde((Date) rows.get(0).get("vigencia_desde"));
				obj.setVigenciaHasta((Date) rows.get(0).get("vigencia_hasta"));
				obj.setCondicionesRetiros((String) rows.get(0).get("condiciones_retiros"));
				obj.setCondicionesApertura((String) rows.get(0).get("condiciones_apertura"));
				obj.setCreadoPor((Integer) rows.get(0).get("creado_por"));
				obj.setFechaCreacion((Timestamp) rows.get(0).get("fecha_creacion"));
				obj.setModificadoPor((Integer) rows.get(0).get("modificado_por"));
				obj.setFechaModificacion((Timestamp) rows.get(0).get("fecha_modificacion"));
				obj.setRendimientoId((Integer) rows.get(0).get("rendimiento_id"));
				obj.setGat((Double) rows.get(0).get("gat"));
				obj.setTipoCapitalizarId((Integer) rows.get(0).get("tipo_capitalizar_id"));
				obj.setNoDisposicion((Integer) rows.get(0).get("no_disposicion"));
				obj.setEstatus((String) rows.get(0).get("estatus"));
				obj.setTipo((Integer) rows.get(0).get("tipo"));
				obj.setCapital((Double) rows.get(0).get("capital"));
				obj.setFechaInicio((Date) rows.get(0).get("fecha_inicio"));
				obj.setFechaFinal((Date) rows.get(0).get("fecha_final"));
				obj.setInteres((Double) rows.get(0).get("interes"));
				obj.setFechaDeposito((Date) rows.get(0).get("fecha_deposito"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}

	public void actualizar(AhorroRendimientoVigentes arv) {
		if(arv.getRendimientoVigenteId() > 0){			
			try{
				jdbcTemplatePr.update(actualizarARV, arv.getRendimientoVigenteId(),arv.getCuenta(),arv.getTipoAhorroId(),arv.getPeriodicidad(),arv.getPlazo(),arv.getCalculoIva(),arv.getTasaInt(),arv.getTasaId(),arv.getBase(),arv.getFormula(),
						arv.getTipoCorte(),arv.getDeposito(),arv.getNumDias(),arv.getDiasGracia(),arv.getCtaContable(),arv.getVigenciaDesde(),arv.getVigenciaHasta(),arv.getCondicionesRetiros(),arv.getCondicionesApertura(),arv.getCreadoPor(),
						arv.getFechaCreacion(),arv.getModificadoPor(),arv.getFechaModificacion(),arv.getRendimientoId(),arv.getGat(),arv.getTipoCapitalizarId(),arv.getNoDisposicion(),arv.getEstatus(),arv.getTipo(),arv.getCapital(),
						arv.getFechaInicio(),arv.getFechaFinal(),arv.getInteres(),arv.getFechaDeposito());
			}catch(Exception e){
				
			}
			
		}
	}

	
}
