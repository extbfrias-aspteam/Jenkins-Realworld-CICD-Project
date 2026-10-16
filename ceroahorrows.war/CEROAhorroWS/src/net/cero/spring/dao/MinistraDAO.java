package net.cero.spring.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.MinistraOBJ;


public class MinistraDAO {	

	public static final Logger LOG = LogManager.getLogger(MinistraDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String obtenerMinistraByControlFechaMin;
	private String actualizaMinistraChequeBanco;
	
	public MinistraOBJ obtenerMinistraByControlFechaMin(String control, Date fecha) {
		MinistraOBJ obj = new MinistraOBJ();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(obtenerMinistraByControlFechaMin, control, fecha);

			if (!rows.isEmpty()) {
				obj.setControl((String) rows.get(0).get("control"));
				obj.setFecMin((Date) rows.get(0).get("fec_min"));
				obj.setMotoMin((Double) rows.get(0).get("mto_min"));
				obj.setNumero((Integer) rows.get(0).get("numero"));
				obj.setCheque((String) rows.get(0).get("cheque"));
				obj.setBanco((Integer) rows.get(0).get("banco"));
				obj.setChequeRet((String) rows.get(0).get("cheque_ret"));
				obj.setBancoRet((String) rows.get(0).get("banco_ret"));
				obj.setFechaDeposito((Date) rows.get(0).get("fecha_deposito"));
				obj.setBancoDeposito((Integer) rows.get(0).get("banco_deposito"));
				obj.setMontoDeposito((Double) rows.get(0).get("monto_deposito"));
				obj.setPoliza((String) rows.get(0).get("poliza"));
				obj.setMinistraId((Integer) rows.get(0).get("ministra_id"));
				obj.setPolizaReg((String) rows.get(0).get("poliza_reg"));
				obj.setTransaccionId((String) rows.get(0).get("transaccion_id"));
				obj.setTarjetaOperativaId((String) rows.get(0).get("tarjeta_operativa_id"));
				obj.setApp((Integer) rows.get(0).get("app"));
				obj.setTransaccionVersionId((Integer) rows.get(0).get("transaccion_version_id"));
				
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}
	

	public void actualizaMinistraChequeBanco(String cheque, Integer banco, String control) {
		try{
			jdbcTemplatePr.update(actualizaMinistraChequeBanco, cheque, banco, control, banco, cheque);
		}catch(Exception e){
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}



	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getObtenerMinistraByControlFechaMin() {
		return obtenerMinistraByControlFechaMin;
	}
	public void setObtenerMinistraByControlFechaMin(String obtenerMinistraByControlFechaMin) {
		this.obtenerMinistraByControlFechaMin = obtenerMinistraByControlFechaMin;
	}
	public String getActualizaMinistraChequeBanco() {
		return actualizaMinistraChequeBanco;
	}
	public void setActualizaMinistraChequeBanco(String actualizaMinistraChequeBanco) {
		this.actualizaMinistraChequeBanco = actualizaMinistraChequeBanco;
	}
}
