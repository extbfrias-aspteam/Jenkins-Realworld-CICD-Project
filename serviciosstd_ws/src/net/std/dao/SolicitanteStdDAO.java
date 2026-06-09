package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.data.SolicitanteOBJ;

public class SolicitanteStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(SolicitanteStdDAO.class);
	
	private JdbcTemplate jdbcTemplatePr;
	private String solicitanteStd;
	private String solicitanteMoralStd;
	private String solicitanteFisicaStd;
	private String solicitanteFisicaRFCStd;
	private String solicitanteFisicaCURPStd;

	private NamedParameterJdbcTemplate namedJdbcTemplatePr;
	private String insertaPaisSolicitante;
	private String actualizaCamposSolicitante;
	private String insertaDenominacionSolicitante;
	private String domicilioCompletoSolStd;
	
	public RespuestaSVC solicitanteCoDiDao(String personaID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplatePr.queryForList(solicitanteStd, personaID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
					if(dato == null) dato = new ArrayList<>();
					dato.add(map);
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC BuscarSolicitanteDao(SolicitanteOBJ sol) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		String msg ="";

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try {
			if("M".equals(_T(sol.getTipoPersona()))){
				rows = jdbcTemplatePr.queryForList(solicitanteMoralStd, sol.getRfc());
			}else{
				//rows = jdbcTemplatePr.queryForList(solicitanteFisicaStd, sol.getRfc(), sol.getCurp());
				rows = jdbcTemplatePr.queryForList(solicitanteFisicaCURPStd, sol.getCurp());
			}
			
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
				}
			}else{	
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, msg = String.format("SIN REGISTROS PARA LA BUSQUEDA %s - %s - %s", _T(sol.getTipoPersona()), _T(sol.getRfc()), _T(sol.getCurp())));
				log.info(msg);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC BuscarSolicitanteByTipoDao(String curp,String rfc, String tipo) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		String msg ="";

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try {
			if("M".equals(_T(tipo))){
				rows = jdbcTemplatePr.queryForList(solicitanteMoralStd, rfc);
			}else{
				//rows = jdbcTemplatePr.queryForList(solicitanteFisicaStd, sol.getRfc(), sol.getCurp());
				rows = jdbcTemplatePr.queryForList(solicitanteFisicaCURPStd, curp);
			}
			
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
				}
			}else{	
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, msg = String.format("SIN REGISTROS PARA LA BUSQUEDA %s - %s - %s ", _T(tipo), _T(curp), _T(rfc)));
				log.info(msg);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC solicitanteStdDao(String personaID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplatePr.queryForList(solicitanteStd, personaID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
				}
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	
	
	/*************	CONCEPTOS SOLICITANTE ******************************************/
	public int insertaPaisSolicitante(String solId, Integer pais) {
		RespuestaSVC respuesta = new RespuestaSVC();
		int rows = 0;
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pais",pais);
            paramMap.put("solId", solId);
            paramMap.put("user", Comun._I(Constantes.USUARIO_ID));
            
            rows = namedJdbcTemplatePr.update(insertaPaisSolicitante,paramMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return rows;
	}
	public int actualizaCamposSolicitante(String solId, Integer estado, String geolocalizacion, Double ingresos, Double monto_max_aho) {
		RespuestaSVC respuesta = new RespuestaSVC();
		int rows = 0;
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
            
            rows = jdbcTemplatePr.update(actualizaCamposSolicitante, estado, geolocalizacion, ingresos, monto_max_aho, solId);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return rows;
	}
	public int insertaDenominacionSolicitante(String solId, Integer denominacion) {
		RespuestaSVC respuesta = new RespuestaSVC();
		int rows = 0;
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
            
            rows = jdbcTemplatePr.update(insertaDenominacionSolicitante,denominacion, solId);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return rows;
	}
	
	
	public RespuestaSVC domicilioCompletoSolicitante(String cp, String colonia) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(domicilioCompletoSolStd, cp,colonia);
			
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
				}
			}else{	
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}		
		return respuesta;
	}

	
	
	
	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getSolicitanteStd() {
		return solicitanteStd;
	}

	public void setSolicitanteStd(String solicitanteStd) {
		this.solicitanteStd = solicitanteStd;
	}

	public String getSolicitanteMoralStd() {
		return solicitanteMoralStd;
	}

	public void setSolicitanteMoralStd(String solicitanteMoralStd) {
		this.solicitanteMoralStd = solicitanteMoralStd;
	}

	public String getSolicitanteFisicaStd() {
		return solicitanteFisicaStd;
	}

	public void setSolicitanteFisicaStd(String solicitanteFisicaStd) {
		this.solicitanteFisicaStd = solicitanteFisicaStd;
	}

	public String getSolicitanteFisicaRFCStd() {
		return solicitanteFisicaRFCStd;
	}

	public void setSolicitanteFisicaRFCStd(String solicitanteFisicaRFCStd) {
		this.solicitanteFisicaRFCStd = solicitanteFisicaRFCStd;
	}

	public String getSolicitanteFisicaCURPStd() {
		return solicitanteFisicaCURPStd;
	}

	public void setSolicitanteFisicaCURPStd(String solicitanteFisicaCURPStd) {
		this.solicitanteFisicaCURPStd = solicitanteFisicaCURPStd;
	}

	public String getInsertaPaisSolicitante() {
		return insertaPaisSolicitante;
	}

	public void setInsertaPaisSolicitante(String insertaPaisSolicitante) {
		this.insertaPaisSolicitante = insertaPaisSolicitante;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplatePr() {
		return namedJdbcTemplatePr;
	}

	public void setNamedJdbcTemplatePr(NamedParameterJdbcTemplate namedJdbcTemplatePr) {
		this.namedJdbcTemplatePr = namedJdbcTemplatePr;
	}

	public String getInsertaDenominacionSolicitante() {
		return insertaDenominacionSolicitante;
	}

	public void setInsertaDenominacionSolicitante(String insertaDenominacionSolicitante) {
		this.insertaDenominacionSolicitante = insertaDenominacionSolicitante;
	}

	public String getActualizaCamposSolicitante() {
		return actualizaCamposSolicitante;
	}

	public void setActualizaCamposSolicitante(String actualizaCamposSolicitante) {
		this.actualizaCamposSolicitante = actualizaCamposSolicitante;
	}

	public String getDomicilioCompletoSolStd() {
		return domicilioCompletoSolStd;
	}

	public void setDomicilioCompletoSolStd(String domicilioCompletoSolStd) {
		this.domicilioCompletoSolStd = domicilioCompletoSolStd;
	}
}

