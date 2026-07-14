package net.cero.spring.dao;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.data.AhorroIdeValores;


public class AhorroIdeValoresDAO {	

	public static final Logger LOG = LogManager.getLogger(AhorroIdeValoresDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String sigSeqIde;
	private String nuevoAhorroIdeValores;
	private String findValorByFechas;
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getSigSeq() {
		return sigSeqIde;
	}

	public void setSigSeqIde(String sigSeqIde) {
		this.sigSeqIde = sigSeqIde;
	}

	public String getNuevoAhorroIdeValores() {
		return nuevoAhorroIdeValores;
	}

	public void setNuevoAhorroIdeValores(String nuevoAhorroIdeValores) {
		this.nuevoAhorroIdeValores = nuevoAhorroIdeValores;
	}

	public String getFindValorByFechas() {
		return findValorByFechas;
	}

	public void setFindValorByFechas(String findValorByFechas) {
		this.findValorByFechas = findValorByFechas;
	}
		
	private Integer sigSeq(){
		return jdbcTemplatePr.queryForObject(sigSeqIde, Integer.class);
	}

	public Integer nuevo(AhorroIdeValores av) {
		try{
			av.setAhorroIdeValoresId(sigSeq());
			jdbcTemplatePr.update(nuevoAhorroIdeValores,av.getAhorroIdeValoresId(),av.getFechaInicio(),av.getFechaFinal(),av.getMonto(),av.getPorcentaje(),av.getCreadoPor(),av.getFechaCreacion(),av.getModificadoPor(),av.getFechaModificacion());
		}catch(Exception e){
			return null;
		}
		
		return av.getAhorroIdeValoresId();
	}
	
	public AhorroIdeValores findByFechas(Date fechaDesde, Date fechaHasta){
		AhorroIdeValores obj = new AhorroIdeValores();
		List<Map<String, Object>> rows;
		
		try {
			rows = jdbcTemplatePr.queryForList(findValorByFechas, fechaDesde,fechaHasta);

			if (!rows.isEmpty()) {
				obj.setAhorroIdeValoresId((Integer) rows.get(0).get("ahorro_ide_valores_id"));
				obj.setFechaInicio((Date) rows.get(0).get("fecha_inicio"));
				obj.setFechaFinal((Date) rows.get(0).get("fecha_final"));
				obj.setMonto((Double) rows.get(0).get("monto"));
				obj.setPorcentaje((Double) rows.get(0).get("porcentaje"));
				obj.setCreadoPor((Integer) rows.get(0).get("creado_por"));
				obj.setFechaCreacion((Timestamp) rows.get(0).get("fecha_creacion"));
				obj.setFechaModificacion((Timestamp) rows.get(0).get("modificado_por"));
				obj.setModificadoPor((Integer) rows.get(0).get("fecha_modificacion"));
			}

		} catch (EmptyResultDataAccessException e) {
			LOG.error(e.getMessage());
		}

		return obj;
	}
}
