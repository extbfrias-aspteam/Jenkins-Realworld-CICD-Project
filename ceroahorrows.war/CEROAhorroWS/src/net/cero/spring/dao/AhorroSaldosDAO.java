package net.cero.spring.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroSaldos;

@Log4j2
public class AhorroSaldosDAO {	
	public static final Logger LOG = LogManager.getLogger(AhorroSaldosDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSeqAS;
	private String nuevoAS;
	private String buscarSaldoByCuenta;
	private String actualizarAS;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getSigSeqAS() {
		return sigSeqAS;
	}

	public void setSigSeqAS(String sigSeqAS) {
		this.sigSeqAS = sigSeqAS;
	}

	public String getNuevoAS() {
		return nuevoAS;
	}

	public void setNuevoAS(String nuevoAS) {
		this.nuevoAS = nuevoAS;
	}

	public String getBuscarSaldoByCuenta() {
		return buscarSaldoByCuenta;
	}

	public void setBuscarSaldoByCuenta(String buscarSaldoByCuenta) {
		this.buscarSaldoByCuenta = buscarSaldoByCuenta;
	}

	public String getActualizarAS() {
		return actualizarAS;
	}

	public void setActualizarAS(String actualizarAS) {
		this.actualizarAS = actualizarAS;
	}
	
	private Integer sigSeq(){
		return jdbcTemplatePr.queryForObject(sigSeqAS, Integer.class);
	}

	public Integer nuevo(AhorroSaldos as) {
		try{
			as.setAhorroSaldosId(sigSeq());
			jdbcTemplatePr.update(nuevoAS,as.getCuenta(),as.getSolicitanteId(),as.getSaldoReal(),as.getSaldoPromedio(),as.getSaldoAcumulado(),as.getFechaCorte(),as.getFechaDeposito(),as.getIntereses(),as.getIva(),as.getIsr(),as.getRetenciones(),as.getDesviacion(),as.getDias(),as.getSaldoDisponible(), as.getAhorroSaldosId(),as.getIde());
		}catch(Exception e){
			return 0;
		}
		
		return as.getAhorroSaldosId();
	}
	
	public AhorroSaldos buscarByCuenta(String cuenta) {
		AhorroSaldos obj = new AhorroSaldos();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarSaldoByCuenta, cuenta);

			if (!rows.isEmpty()) {
				obj.setCuenta((String) rows.get(0).get("cuenta"));
				obj.setSolicitanteId((String) rows.get(0).get("solicitante_id"));
				obj.setSaldoReal((Double) rows.get(0).get("saldo_real"));
				obj.setSaldoPromedio((Double) rows.get(0).get("saldo_promedio"));
				obj.setSaldoAcumulado((Double) rows.get(0).get("saldo_acumulado"));
				obj.setFechaCorte((Date) rows.get(0).get("fecha_corte"));
				obj.setFechaDeposito((Date) rows.get(0).get("fecha_deposito"));
				obj.setIntereses((Double) rows.get(0).get("intereses"));
				obj.setIva((Double) rows.get(0).get("iva"));
				obj.setIsr((Double) rows.get(0).get("isr"));
				obj.setRetenciones((Double) rows.get(0).get("retenciones"));
				obj.setDesviacion((Double) rows.get(0).get("desviacion"));
				obj.setDias((Integer) rows.get(0).get("dias"));
				obj.setSaldoDisponible((Double) rows.get(0).get("saldo_disponible"));
				obj.setAhorroSaldosId((Integer) rows.get(0).get("ahorro_saldos_id"));
				//obj.setIde((Double) rows.get(0).get("ide"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}

	public void actualizar(AhorroSaldos as) {
		if(as.getAhorroSaldosId() > 0){			
			try{
				jdbcTemplatePr.update(actualizarAS,as.getSaldoReal(),as.getSaldoAcumulado(),as.getSaldoDisponible(),as.getAhorroSaldosId());
			}catch(Exception e){
				log.info("Error actualizando Ahorro saldos" + e.getMessage());
			}
			
		}
	}

	
}
