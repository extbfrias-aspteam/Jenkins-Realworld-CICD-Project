package net.cero.spring.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ahorro.ws.util.WS_UTIL;

public class SpeiDao {

	private static final Logger log = LogManager.getLogger(SpeiDao.class);
	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplateSti;
	
	private String consultaSpei;
	private String consultaInstitucion;
	private String consultaSpeiOut;
	private String consultaNombreCliente;
	
	public List<Map<String, Object>> consultaSpeiWhere(final String WHERE, final String TIPO_SPEI, String cveRastreo, String fecha) {
		List<Map<String, Object>> list = TIPO_SPEI.equals(WS_UTIL.SPEI_IN) ? consultaSpeiIn(WHERE, cveRastreo, fecha) : TIPO_SPEI.equals(WS_UTIL.SPEI_OUT) ? consultaSpeiOut(WHERE, cveRastreo, fecha) : null;
		try {
			List<Map<String, Object>> detalleCliente = this.jdbcTemplate.queryForList(this.consultaNombreCliente);
			
			Map<Object, Object> clientesMap = detalleCliente.stream().collect(Collectors.toMap(s -> Integer.valueOf(String.valueOf(s.get("id_empresa"))), s -> String.valueOf(s.get("cliente"))));
			list.forEach(item->{
				item.put("cliente", clientesMap.get(item.get("empresa_id")));
			});
		} catch (DataAccessException e) {
			log.error("No se pudo consultar datos del cliente", e);
			
		}
		return list;
	}

	private List<Map<String, Object>> consultaSpeiIn(String wHERE, String cveRastreo, String fecha) {
		List<Map<String, Object>> referenciasIn = new ArrayList<>();
		
		try {
			
			referenciasIn = this.jdbcTemplateSti.queryForList(this.consultaSpei.concat(wHERE));
			
		} catch (DataAccessException e) {
			log.error("No se encontraron referencias de spei ", e);
		}
		return referenciasIn;
	}
	
	private List<Map<String, Object>> consultaSpeiOut(String wHERE, String cveRastreo, String fecha) {
		List<Map<String, Object>> referenciasOut = new ArrayList<>();
		try {
			referenciasOut = this.jdbcTemplateSti.queryForList(this.consultaSpeiOut.concat(wHERE));
		} catch (DataAccessException e2) {
			log.error("no se encontraron datos en spei out", e2);
		}
		return referenciasOut;
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

	public String getConsultaSpei() {
		return consultaSpei;
	}
	

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public void setConsultaSpei(String consultaSpei) {
		this.consultaSpei = consultaSpei;
	}

	public String getConsultaInstitucion() {
		return consultaInstitucion;
	}

	public void setConsultaInstitucion(String consultaInstitucion) {
		this.consultaInstitucion = consultaInstitucion;
	}

	public String getConsultaSpeiOut() {
		return consultaSpeiOut;
	}

	public void setConsultaSpeiOut(String consultaSpeiOut) {
		this.consultaSpeiOut = consultaSpeiOut;
	}

	public String getConsultaNombreCliente() {
		return consultaNombreCliente;
	}

	public void setConsultaNombreCliente(String consultaNombreCliente) {
		this.consultaNombreCliente = consultaNombreCliente;
	}
}
