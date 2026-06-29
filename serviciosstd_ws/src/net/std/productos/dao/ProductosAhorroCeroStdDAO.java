package net.std.productos.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.data.TipoDocumentoCompletoOBJ;

//@Getter @Setter @NoArgsConstructor // <--- Lombok Auto Getter's y Setter's
public class ProductosAhorroCeroStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProductosAhorroCeroStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private String INSERT_ProdAhorroCeroStd;
	private String READ_ProdAhorroCeroStd;

	private String INSERT_ProdAhorroCeroConceptosStd;
	private String READ_ConceptosProdStd;
	private String INSERT_ConceptosProdStd;

	private String READ_ModuloStd;
	private String INSERT_DocumentosStd;
	private String READ_DocumentosStd;
	
	private String LIST_DocumentosStd;

	public RespuestaSVC leerProductosCeroStdDao(String clave, String estatus) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_ProdAhorroCeroStd, clave, estatus);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
				}
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC crearProductosCeroStdDao(Map<String, String> params) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_ProdAhorroCeroStd, new String[]{"id"});
			ps.setString(1, params.get("CLAVE"));
			ps.setString(2, params.get("DESCRIPCION"));
			ps.setString(3, params.get("MONTO_MINIMO"));
			ps.setString(4, params.get("MONTO_MAXIMO"));
			ps.setString(5, params.get("FECHA_ACTIVACION"));
			ps.setString(6, params.get("ESTATUS_ID"));
			ps.setString(7, params.get("MONEDA_ID"));
			ps.setString(8, params.get("CNBV_ID"));
			//ps.setString(9, params.get("TIPO_AHORRO_ID"));
			//ps.setString(10, params.get("USUARIO_ID"));
			ps.setString(9, params.get("USUARIO_ID"));
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_NOREGISTRO_PRODUCTO, ErrProd.desc(ErrProd.ERROR_NOREGISTRO_PRODUCTO));
			}else{
				respuesta.getBody().addValor("PRODUCTO_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	@SuppressWarnings("unused")
	public RespuestaSVC crearConceptosProdStdDao(Integer productoId, String linea, Integer estatusId, Integer usuarioId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String>> mapCon = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try {
			rows = jdbcTemplate.queryForList(READ_ConceptosProdStd, linea);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
					if(mapCon == null) mapCon = new ArrayList<>();
					mapCon.add(map);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		if(mapCon != null){
			try{
				for(Map<String, String> map : mapCon){
					int total = jdbcTemplate.update(INSERT_ConceptosProdStd, productoId, Comun._T(map.get("ID")), estatusId, null, usuarioId);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
			}
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}


	public RespuestaSVC crearConceptosAhorroCeroStdDao(Map<String, String> params) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_ProdAhorroCeroConceptosStd, new String[]{"id"});
			ps.setString(1, params.get("CLAVE"));
			ps.setString(2, params.get("DESCRIPCION"));
			ps.setString(3, params.get("MODULO_ID"));
			ps.setString(4, params.get("USUARIO_ID"));
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_CONCEPTOS, ErrProd.desc(ErrProd.ERROR_CONCEPTOS));
			}else{
				respuesta.getBody().addValor("CONCEPTO_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC leerModulosStdDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_ModuloStd, clave);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuesta.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
				}
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_MODULOS, ErrProd.desc(ErrProd.ERROR_MODULOS, clave));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}


	public RespuestaSVC crearDocumentosStdDao(Integer productoID, Integer statusID, Integer usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			/*
			 	ArrayList<String> list = new ArrayList<String>();
				PreparedStatement pstmt = 	
				            conn.prepareStatement("select * from employee where id in (?)");
				Array array = conn.createArrayOf("VARCHAR", list.toArray());
				pstmt.setArray(1, array);
				ResultSet rs = pstmt.executeQuery();
			 */

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_DocumentosStd, new String[]{"id"});
			ps.setInt(1, productoID);
			ps.setInt(2, statusID);
			ps.setInt(3, usuarioID);
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_DOCUMENTOS, ErrProd.desc(ErrProd.ERROR_DOCUMENTOS));
			}else{
				respuesta.getBody().addValor("CONCEPTO_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC leerDocumentosStdDao(Integer productoID, Integer estatusID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<TipoDocumentoCompletoOBJ> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_DocumentosStd, productoID, estatusID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					TipoDocumentoCompletoOBJ doc = new TipoDocumentoCompletoOBJ();
					doc.setProducto_ahorro_id(_T(row.get("PRODUCTO_AHORRO_ID")));
					doc.setCve_producto(_T(row.get("CVE_PRODUCTO")));
					doc.setProducto(_T(row.get("PRODUCTO")));
					doc.setDocumento_id(_T(row.get("DOCUMENTO_ID")));
					doc.setCve_documento(_T(row.get("CVE_DOCUMENTO")));
					doc.setDocumento(_T(row.get("DOCUMENTO")));
					doc.setObligatorio(_T(row.get("OBLIGATORIO")));
					doc.setEstatus_id(_T(row.get("ESTATUS_ID")));
					doc.setEstatus(_T(row.get("ESTATUS")));
					
					if(dato == null) dato = new ArrayList<>();
					dato.add(doc);
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("DOCUMENTOS", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC listDocumentosStdDao(Integer productoID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<TipoDocumentoCompletoOBJ> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
		rows = jdbcTemplate.queryForList(LIST_DocumentosStd, productoID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					TipoDocumentoCompletoOBJ doc = new TipoDocumentoCompletoOBJ();
					doc.setProducto_ahorro_id(_T(row.get("PRODUCTO_AHORRO_ID")));
					doc.setCve_producto(_T(row.get("CVE_PRODUCTO")));
					doc.setProducto(_T(row.get("PRODUCTO")));
					doc.setDocumento_id(_T(row.get("DOCUMENTO_ID")));
					doc.setCve_documento(_T(row.get("CVE_DOCUMENTO")));
					doc.setDocumento(_T(row.get("DOCUMENTO")));
					doc.setObligatorio(_T(row.get("OBLIGATORIO")));
					doc.setEstatus_id(_T(row.get("ESTATUS_ID")));
					doc.setEstatus(_T(row.get("ESTATUS")));
					
					if(dato == null) dato = new ArrayList<>();
					dato.add(doc);
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("DOCUMENTOS", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
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


	public String getINSERT_ProdAhorroCeroStd() {
		return INSERT_ProdAhorroCeroStd;
	}


	public void setINSERT_ProdAhorroCeroStd(String iNSERT_ProdAhorroCeroStd) {
		INSERT_ProdAhorroCeroStd = iNSERT_ProdAhorroCeroStd;
	}

	public String getREAD_ProdAhorroCeroStd() {
		return READ_ProdAhorroCeroStd;
	}

	public void setREAD_ProdAhorroCeroStd(String rEAD_ProdAhorroCeroStd) {
		READ_ProdAhorroCeroStd = rEAD_ProdAhorroCeroStd;
	}

	public String getINSERT_ProdAhorroCeroConceptosStd() {
		return INSERT_ProdAhorroCeroConceptosStd;
	}

	public void setINSERT_ProdAhorroCeroConceptosStd(String iNSERT_ProdAhorroCeroConceptosStd) {
		INSERT_ProdAhorroCeroConceptosStd = iNSERT_ProdAhorroCeroConceptosStd;
	}

	public String getREAD_ConceptosProdStd() {
		return READ_ConceptosProdStd;
	}

	public void setREAD_ConceptosProdStd(String rEAD_ConceptosProdStd) {
		READ_ConceptosProdStd = rEAD_ConceptosProdStd;
	}

	public String getINSERT_ConceptosProdStd() {
		return INSERT_ConceptosProdStd;
	}

	public void setINSERT_ConceptosProdStd(String iNSERT_ConceptosProdStd) {
		INSERT_ConceptosProdStd = iNSERT_ConceptosProdStd;
	}

	public String getREAD_ModuloStd() {
		return READ_ModuloStd;
	}

	public void setREAD_ModuloStd(String rEAD_ModuloStd) {
		READ_ModuloStd = rEAD_ModuloStd;
	}

	public String getINSERT_DocumentosStd() {
		return INSERT_DocumentosStd;
	}

	public void setINSERT_DocumentosStd(String iNSERT_DocumentosStd) {
		INSERT_DocumentosStd = iNSERT_DocumentosStd;
	}

	public String getREAD_DocumentosStd() {
		return READ_DocumentosStd;
	}

	public void setREAD_DocumentosStd(String rEAD_DocumentosStd) {
		READ_DocumentosStd = rEAD_DocumentosStd;
	}

	public String getLIST_DocumentosStd() {
		return LIST_DocumentosStd;
	}

	public void setLIST_DocumentosStd(String lIST_DocumentosStd) {
		LIST_DocumentosStd = lIST_DocumentosStd;
	}

}

