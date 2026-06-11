package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;

public class CatalogoProcreaStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CatalogoProcreaStdDAO.class);

	private JdbcTemplate jdbcTemplatePr;
	private String catPrActividadStd;
	private String catPrGiroStd;
	private String catPrDestinoStd;
	private String catPrOcupacionStd;
	private String catPrLocalidadStd;
	private String catPrColoniasStd;
	private String catPrIdentificadorStd;
	private String catPrTipoIdentificadorStd;
	private String catPrDomicilioStd;
	private String catPrEstudioStd;
	private String catPrEstadoCivilStd;
	private String catPrPaisesStd;
	private String catPrNacionalidadStd;
	private String catPrCodigoPostalStd;
	
	private String validaOcupacionId;
	private String validaEstatalId;
	private String validaNacionalidadId;
	private String validaPaisId;
	private String validaTipoIdenId;
	private String validaDenominacionId;
	private String validaColoniaIdCP;
	
	public RespuestaSVC getActividadesPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrActividadStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getGirosPrStdDao(String actividad, String giro) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todosAct = actividad == null ? true : false;
		Boolean todos = giro == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrGiroStd, todosAct, actividad, todos, giro);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getDestinosPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrDestinoStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getOcupacionesPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrOcupacionStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	
	public RespuestaSVC getLocalidadesPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;
	
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrLocalidadStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getColoniasPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;
	
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrColoniasStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getIdentificadoresPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrIdentificadorStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getTipoIdentificadoresPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrTipoIdentificadorStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getDomiciliosPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrDomicilioStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getEstudiosPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrEstudioStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getEstadoCivilPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrEstadoCivilStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getPaisesPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrPaisesStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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
	
	public RespuestaSVC getNacionalidadPrStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplatePr.queryForList(catPrNacionalidadStd, todos, clave);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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

	public RespuestaSVC getCodigoPostalPrStdDao(String codigoPostal) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;
	
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplatePr.queryForList(catPrCodigoPostalStd, codigoPostal);
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
				respuesta.getBody().addValor("CATALOGO", dato);
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

	public boolean validaOcupacionIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaOcupacionId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validaColoniaIdCPDAO(String id, String cp) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaColoniaIdCP, id, cp);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validaNacionalidadIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaNacionalidadId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validaEstatalIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaEstatalId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}

	public boolean validaDenominacionIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaDenominacionId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validaPaisIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaPaisId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validaTipoIdenIdDAO(String id) {
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplatePr.queryForList(validaTipoIdenId, id);
			if(rows != null && !rows.isEmpty()){
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
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

	public String getCatPrActividadStd() {
		return catPrActividadStd;
	}

	public void setCatPrActividadStd(String catPrActividadStd) {
		this.catPrActividadStd = catPrActividadStd;
	}

	public String getCatPrGiroStd() {
		return catPrGiroStd;
	}

	public void setCatPrGiroStd(String catPrGiroStd) {
		this.catPrGiroStd = catPrGiroStd;
	}

	public String getCatPrDestinoStd() {
		return catPrDestinoStd;
	}

	public void setCatPrDestinoStd(String catPrDestinoStd) {
		this.catPrDestinoStd = catPrDestinoStd;
	}

	public String getCatPrOcupacionStd() {
		return catPrOcupacionStd;
	}

	public void setCatPrOcupacionStd(String catPrOcupacionStd) {
		this.catPrOcupacionStd = catPrOcupacionStd;
	}

	public String getCatPrLocalidadStd() {
		return catPrLocalidadStd;
	}

	public void setCatPrLocalidadStd(String catPrLocalidadStd) {
		this.catPrLocalidadStd = catPrLocalidadStd;
	}

	public String getCatPrColoniasStd() {
		return catPrColoniasStd;
	}

	public void setCatPrColoniasStd(String catPrColoniasStd) {
		this.catPrColoniasStd = catPrColoniasStd;
	}

	public String getCatPrIdentificadorStd() {
		return catPrIdentificadorStd;
	}

	public void setCatPrIdentificadorStd(String catPrIdentificadorStd) {
		this.catPrIdentificadorStd = catPrIdentificadorStd;
	}

	public String getCatPrTipoIdentificadorStd() {
		return catPrTipoIdentificadorStd;
	}

	public void setCatPrTipoIdentificadorStd(String catPrTipoIdentificadorStd) {
		this.catPrTipoIdentificadorStd = catPrTipoIdentificadorStd;
	}

	public String getCatPrDomicilioStd() {
		return catPrDomicilioStd;
	}

	public void setCatPrDomicilioStd(String catPrDomicilioStd) {
		this.catPrDomicilioStd = catPrDomicilioStd;
	}

	public String getCatPrEstudioStd() {
		return catPrEstudioStd;
	}

	public void setCatPrEstudioStd(String catPrEstudioStd) {
		this.catPrEstudioStd = catPrEstudioStd;
	}

	public String getCatPrEstadoCivilStd() {
		return catPrEstadoCivilStd;
	}

	public void setCatPrEstadoCivilStd(String catPrEstadoCivilStd) {
		this.catPrEstadoCivilStd = catPrEstadoCivilStd;
	}

	public String getCatPrPaisesStd() {
		return catPrPaisesStd;
	}

	public void setCatPrPaisesStd(String catPrPaisesStd) {
		this.catPrPaisesStd = catPrPaisesStd;
	}

	public String getCatPrNacionalidadStd() {
		return catPrNacionalidadStd;
	}

	public void setCatPrNacionalidadStd(String catPrNacionalidadStd) {
		this.catPrNacionalidadStd = catPrNacionalidadStd;
	}

	public String getCatPrCodigoPostalStd() {
		return catPrCodigoPostalStd;
	}

	public void setCatPrCodigoPostalStd(String catPrCodigoPostalStd) {
		this.catPrCodigoPostalStd = catPrCodigoPostalStd;
	}

	public String getValidaOcupacionId() {
		return validaOcupacionId;
	}

	public void setValidaOcupacionId(String validaOcupacionId) {
		this.validaOcupacionId = validaOcupacionId;
	}

	public String getValidaEstatalId() {
		return validaEstatalId;
	}

	public void setValidaEstatalId(String validaEstatalId) {
		this.validaEstatalId = validaEstatalId;
	}

	public String getValidaNacionalidadId() {
		return validaNacionalidadId;
	}

	public void setValidaNacionalidadId(String validaNacionalidadId) {
		this.validaNacionalidadId = validaNacionalidadId;
	}

	public String getValidaPaisId() {
		return validaPaisId;
	}

	public void setValidaPaisId(String validaPaisId) {
		this.validaPaisId = validaPaisId;
	}

	public String getValidaTipoIdenId() {
		return validaTipoIdenId;
	}

	public void setValidaTipoIdenId(String validaTipoIdenId) {
		this.validaTipoIdenId = validaTipoIdenId;
	}

	public String getValidaDenominacionId() {
		return validaDenominacionId;
	}

	public void setValidaDenominacionId(String validaDenominacionId) {
		this.validaDenominacionId = validaDenominacionId;
	}

	public String getValidaColoniaIdCP() {
		return validaColoniaIdCP;
	}

	public void setValidaColoniaIdCP(String validaColoniaIdCP) {
		this.validaColoniaIdCP = validaColoniaIdCP;
	}
}

