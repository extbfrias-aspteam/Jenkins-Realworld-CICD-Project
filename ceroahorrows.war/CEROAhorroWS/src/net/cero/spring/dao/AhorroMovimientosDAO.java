package net.cero.spring.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroMovimiento;

public class AhorroMovimientosDAO {
	
	public static final Logger LOG = LogManager.getLogger(AhorroMovimientosDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String buscarAhorroMovimientosById;

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getBuscarById() {
		return buscarAhorroMovimientosById;
	}

	public void setBuscarAhorroMovimientosById(String buscarAhorroMovimientosById) {
		this.buscarAhorroMovimientosById = buscarAhorroMovimientosById;
	}
	
	public AhorroMovimiento buscarById(Integer id) {
		AhorroMovimiento obj = new AhorroMovimiento();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarAhorroMovimientosById, id);

			if (!rows.isEmpty()) {
				obj.setMovimientoId((Integer) rows.get(0).get("movimiento_id"));
				obj.setDescripcion((String) rows.get(0).get("descripcion"));
				obj.setOperacion((String) rows.get(0).get("operacion"));
				obj.setCreadoPor((Integer) rows.get(0).get("creado_por"));
				obj.setFechaCreacion((Date) rows.get(0).get("fecha_creacion"));
				obj.setModificadoPor((Integer) rows.get(0).get("modificado_por"));
				obj.setFechaModificacion((Date) rows.get(0).get("fecha_modificacion"));
				obj.setSalvoBuenCobro((String) rows.get(0).get("salvo_buen_cobro"));
				obj.setCuentaContable((String) rows.get(0).get("cuenta_contable"));
				obj.setProvisionar((String) rows.get(0).get("provisionar"));
				obj.setManual((String) rows.get(0).get("manual"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}

	
}
