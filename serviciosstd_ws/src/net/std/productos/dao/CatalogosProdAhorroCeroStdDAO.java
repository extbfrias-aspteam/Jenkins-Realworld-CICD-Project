package net.std.productos.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.ErrProd;
import net.std.data.ProductoOBJ;

public class CatalogosProdAhorroCeroStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CatalogosProdAhorroCeroStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private String getEstatusID;
	private String getMonedaID;
	private String getTipoAhorroID;
	private String getClaveCnbvID;
	private String getComoEnteroID;
	private String catCeroTipoDocumentoStd;
	private String catProductoAhorroStd;
	private String catBeanProductoAhorroStd;
	private String catComoEnteroStd;
	private String catMonedaStd;
	private String catEstatusStd;
	private String catEstatusAhorroStd;


	public RespuestaSVC claveValorStdDao(String catalogo,  String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		Map<String, String> catMap = getMapa();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(catMap.get(catalogo), clave);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
				}
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_CATALOGO, ErrProd.desc(ErrProd.ERROR_CATALOGO, clave));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}


	public RespuestaSVC getComoSeEnteroStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(getComoEnteroID, clave);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
				}
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_CATALOGO, ErrProd.desc(ErrProd.ERROR_CATALOGO, clave));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC catTipoDocumentosStdStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
				rows = jdbcTemplate.queryForList(catCeroTipoDocumentoStd, todos, clave);
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


	public RespuestaSVC catProductoAhorroStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {

				rows = jdbcTemplate.queryForList(catProductoAhorroStd, todos, clave);
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
	
	public RespuestaSVC catBeanProductoAhorroStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<ProductoOBJ> lstProd = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			
			rows = jdbcTemplate.queryForList(catBeanProductoAhorroStd);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					ProductoOBJ pr = new ProductoOBJ();
					pr.setId(_T(row.get("ID")));
					pr.setClave(_T(row.get("CLAVE")));
					pr.setDescripcion(_T(row.get("DESCRIPCION")));
					pr.setMonto_minimo(_T(row.get("MONTO_MINIMO")));
					pr.setMonto_maximo(_T(row.get("MONTO_MAXIMO")));
					pr.setFecha_activacion(_T(row.get("FECHA_ACTIVACION")));
					pr.setEstatus_id(_T(row.get("ESTATUS_ID")));
					pr.setEstatus(_T(row.get("ESTATUS")));
					pr.setMoneda_id(_T(row.get("MONEDA_ID")));
					pr.setMoneda(_T(row.get("MONEDA")));
					pr.setClave_cnbv(_T(row.get("CLAVE_CNBV")));
					pr.setTipo_producto_ahorro_id(_T(row.get("TIPO_PRODUCTO_AHORRO_ID")));
					
					if(lstProd == null) lstProd = new ArrayList<>();
					lstProd.add(pr);
				}
			}
			
			if(lstProd != null){
				respuesta.getBody().addValor("CATALOGO", lstProd);
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



	public RespuestaSVC catComoSeEnteroStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplate.queryForList(catComoEnteroStd, todos, clave);
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

	public RespuestaSVC catMonedasStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplate.queryForList(catMonedaStd, todos, clave);
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
	
	public RespuestaSVC catEstatusStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplate.queryForList(catEstatusStd, todos, clave);
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
	
	public RespuestaSVC catEstatusAhorroStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		Boolean todos = clave == null ? true : false;
		try {
			rows = jdbcTemplate.queryForList(catEstatusAhorroStd, todos, clave);
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

	private Map<String, String> getMapa(){
		Map<String, String> catMap = Stream.of(new String[][] {
			{ "ESTATUS_ID", getEstatusID }, 
			{ "MONEDA_ID", getMonedaID },
			{ "TIPO_AHORRO_ID", getTipoAhorroID }, 
			{ "CNBV_ID", getClaveCnbvID }, 
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

		return catMap;
	}


	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getGetEstatusID() {
		return getEstatusID;
	}

	public void setGetEstatusID(String getEstatusID) {
		this.getEstatusID = getEstatusID;
	}

	public String getGetMonedaID() {
		return getMonedaID;
	}

	public void setGetMonedaID(String getMonedaID) {
		this.getMonedaID = getMonedaID;
	}

	public String getGetTipoAhorroID() {
		return getTipoAhorroID;
	}

	public void setGetTipoAhorroID(String getTipoAhorroID) {
		this.getTipoAhorroID = getTipoAhorroID;
	}

	public String getGetClaveCnbvID() {
		return getClaveCnbvID;
	}

	public void setGetClaveCnbvID(String getClaveCnbvID) {
		this.getClaveCnbvID = getClaveCnbvID;
	}

	public String getGetComoEnteroID() {
		return getComoEnteroID;
	}

	public void setGetComoEnteroID(String getComoEnteroID) {
		this.getComoEnteroID = getComoEnteroID;
	}

	public String getCatCeroTipoDocumentoStd() {
		return catCeroTipoDocumentoStd;
	}

	public void setCatCeroTipoDocumentoStd(String catCeroTipoDocumentoStd) {
		this.catCeroTipoDocumentoStd = catCeroTipoDocumentoStd;
	}

	public String getCatProductoAhorroStd() {
		return catProductoAhorroStd;
	}


	public void setCatProductoAhorroStd(String catProductoAhorroStd) {
		this.catProductoAhorroStd = catProductoAhorroStd;
	}


	public String getCatComoEnteroStd() {
		return catComoEnteroStd;
	}


	public void setCatComoEnteroStd(String catComoEnteroStd) {
		this.catComoEnteroStd = catComoEnteroStd;
	}


	public String getCatMonedaStd() {
		return catMonedaStd;
	}


	public void setCatMonedaStd(String catMonedaStd) {
		this.catMonedaStd = catMonedaStd;
	}


	public String getCatEstatusStd() {
		return catEstatusStd;
	}


	public void setCatEstatusStd(String catEstatusStd) {
		this.catEstatusStd = catEstatusStd;
	}


	public String getCatEstatusAhorroStd() {
		return catEstatusAhorroStd;
	}


	public void setCatEstatusAhorroStd(String catEstatusAhorroStd) {
		this.catEstatusAhorroStd = catEstatusAhorroStd;
	}


	public String getCatBeanProductoAhorroStd() {
		return catBeanProductoAhorroStd;
	}


	public void setCatBeanProductoAhorroStd(String catBeanProductoAhorroStd) {
		this.catBeanProductoAhorroStd = catBeanProductoAhorroStd;
	}
}

