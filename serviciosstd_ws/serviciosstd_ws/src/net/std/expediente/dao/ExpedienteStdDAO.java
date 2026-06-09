package net.std.expediente.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
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
import net.std.data.ExpedienteCompletoOBJ;
import net.std.data.ExpedienteOBJ;

public class ExpedienteStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ExpedienteStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplatePr;
	private String INSERT_ExpedienteCeroStd;
	private String READ_ExpedienteCeroStd;
	private String UPDATE_ExpedienteCeroStd;
	private String UPDATE_VerificadoCeroStd;
	private String INSERT_CuentaRefExpedienteCeroStd;
	private String READ_CuentaRefExpedienteCeroStd;
	private String UPDATE_CuentaRefExpedienteCeroStd;
	private String UPDATE_CuentaRefVerificadoCeroStd;
	private String INSERT_ClienteExpedienteStd;
	private String UPDATE_ClienteExpedienteStd;
	private String READ_ClienteExpedienteStd;
	private String READ_DocumentosNivelStd;
	
	
	public RespuestaSVC insertarExpedienteStdDao(ExpedienteOBJ exp) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_ExpedienteCeroStd, new String[]{"id"});
			ps.setInt(1, Comun._I(exp.getCuentaId()));
			ps.setInt(2, Comun._I(exp.getDocumentosAhorroId()));
			ps.setString(3, exp.getRutaAlfresco());
			ps.setString(4, Comun._T(exp.getIdArchivoAlfresco()));
			ps.setString(5, exp.getObservaciones());
			ps.setString(6, exp.getNombre());
			ps.setString(7, exp.getFechaExpedicion());
			ps.setString(8, exp.getFechaVigencia());
			ps.setInt(9, Comun._I(exp.getEstatusId()));
			ps.setInt(10, Comun._I(exp.getUsuarioId()));
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INSERTAR_EXPEDIENTE, ErrProd.desc(ErrProd.ERROR_INSERTAR_EXPEDIENTE));
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

	public RespuestaSVC actualizarExpedienteEstatusStdDao(Integer cuentaID, Integer documentoID, Integer estatusID) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_ExpedienteCeroStd, estatusID, cuentaID, documentoID);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE,  ErrProd.desc(ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC listExpedienteStdDao(Integer cuentaID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<ExpedienteCompletoOBJ> lst = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_ExpedienteCeroStd, cuentaID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					ExpedienteCompletoOBJ exp = new ExpedienteCompletoOBJ();
					exp.setId(_T(row.get("ID")));
					exp.setCuenta_id(_T(row.get("CUENTA_ID")));
					exp.setCuenta(_T(row.get("CUENTA")));
					exp.setFecha_apertura(_T(row.get("FECHA_APERTURA")));
					exp.setDocumento_id(_T(row.get("DOCUMENTOS_AHORRO_ID")));
					exp.setCve_documento(_T(row.get("CVE_DOCUMENTO")));
					exp.setDocumento(_T(row.get("DOCUMENTO")));
					exp.setRuta_alfresco(_T(row.get("RUTA_ALFRESCO")));
					exp.setAlfresco_id(_T(row.get("ID_ARCHIVO_ALFRESCO")));
					exp.setObservaciones(_T(row.get("OBSERVACIONES")));
					exp.setNombre_archivo(_T(row.get("NOMBRE_ARCHIVO")));
					exp.setFecha_expedicion(_T(row.get("FECHA_EXPEDICION")));
					exp.setFecha_vigencia(_T(row.get("FECHA_VIGENCIA")));
					exp.setEstatus_id(_T(row.get("ESTATUS_ID")));
					exp.setEstatus(_T(row.get("ESTATUS")));
					exp.setVerificado(_T(row.get("VERIFICADO")));
					exp.setFecha_verificado(_T(row.get("FECHA_VERIFICADO")));
					exp.setbVerificado("S".equals(Comun._T(exp.getVerificado())) ? true : false);

					if(lst == null) lst = new ArrayList<>();
					lst.add(exp);
				}
			}

			if(lst != null){
				respuesta.getBody().addValor("EXPEDIENTE", lst);
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_LEER_EXPEDIENTE,   ErrProd.desc(ErrProd.ERROR_LEER_EXPEDIENTE));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC listDocumentosNivel(String tipoCuentaNivel, String tipoPersonaSol) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_DocumentosNivelStd, tipoCuentaNivel,tipoPersonaSol);
			if(rows != null && !rows.isEmpty()){
				respuesta.getBody().addValor("DOCTOS", rows);
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_LEER_EXPEDIENTE,   ErrProd.desc(ErrProd.ERROR_LEER_EXPEDIENTE));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC verificarExpedienteEstatusStdDao(String observaciones, String verificado, String fechaVerificado, Integer id, Integer cuentaId) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_VerificadoCeroStd, observaciones, verificado, fechaVerificado, id, cuentaId);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE,  ErrProd.desc(ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	/**********************************************
	 * EXPEDIENTE CUENTAS REFERENCIADAS
	 * ********************************************/
	public RespuestaSVC insertarCuentaRefExpedienteStdDao(String solicitanteId, String cuentaReferencia, String claveArchivo, String rutaArchivo,
			String nombreArchivo, String fechaExpedicion, String fechaVigencia, Integer usuario) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			log.info(solicitanteId + " :: " + cuentaReferencia + " :: " + claveArchivo + " :: " + rutaArchivo);
			
			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_CuentaRefExpedienteCeroStd, new String[]{"id"});
			ps.setString(1, solicitanteId);
			ps.setString(2, cuentaReferencia);
			ps.setString(3, claveArchivo);
			ps.setString(4, rutaArchivo);
			ps.setString(5, nombreArchivo);
			ps.setString(6, fechaExpedicion);
			ps.setString(7, fechaVigencia);
			ps.setInt(8, usuario);
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INSERTAR_EXPEDIENTE, ErrProd.desc(ErrProd.ERROR_INSERTAR_EXPEDIENTE, claveArchivo));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC actualizarCuentaRefExpedienteEstatusStdDao(Integer cuentaID, Integer documentoID, Integer estatusID) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_CuentaRefExpedienteCeroStd, estatusID, cuentaID, documentoID);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE,  ErrProd.desc(ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC listCuentaRefExpedienteStdDao(Integer cuentaID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<ExpedienteCompletoOBJ> lst = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_CuentaRefExpedienteCeroStd, cuentaID);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					ExpedienteCompletoOBJ exp = new ExpedienteCompletoOBJ();
					exp.setId(_T(row.get("ID")));
					exp.setCuenta_id(_T(row.get("CUENTA_ID")));
					exp.setCuenta(_T(row.get("CUENTA")));
					exp.setFecha_apertura(_T(row.get("FECHA_APERTURA")));
					exp.setDocumento_id(_T(row.get("DOCUMENTOS_AHORRO_ID")));
					exp.setCve_documento(_T(row.get("CVE_DOCUMENTO")));
					exp.setDocumento(_T(row.get("DOCUMENTO")));
					exp.setRuta_alfresco(_T(row.get("RUTA_ALFRESCO")));
					exp.setAlfresco_id(_T(row.get("ID_ARCHIVO_ALFRESCO")));
					exp.setObservaciones(_T(row.get("OBSERVACIONES")));
					exp.setNombre_archivo(_T(row.get("NOMBRE_ARCHIVO")));
					exp.setFecha_expedicion(_T(row.get("FECHA_EXPEDICION")));
					exp.setFecha_vigencia(_T(row.get("FECHA_VIGENCIA")));
					exp.setEstatus_id(_T(row.get("ESTATUS_ID")));
					exp.setEstatus(_T(row.get("ESTATUS")));
					exp.setVerificado(_T(row.get("VERIFICADO")));
					exp.setFecha_verificado(_T(row.get("FECHA_VERIFICADO")));
					exp.setbVerificado("S".equals(Comun._T(exp.getVerificado())) ? true : false);

					if(lst == null) lst = new ArrayList<>();
					lst.add(exp);
				}
			}

			if(lst != null){
				respuesta.getBody().addValor("EXPEDIENTE", lst);
			}else{
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_LEER_EXPEDIENTE,   ErrProd.desc(ErrProd.ERROR_LEER_EXPEDIENTE));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC verificarCuentaRefExpedienteEstatusStdDao(String observaciones, String verificado, String fechaVerificado, Integer id, Integer cuentaId) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_CuentaRefVerificadoCeroStd, observaciones, verificado, fechaVerificado, id, cuentaId);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE,  ErrProd.desc(ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	
	/*****************   EXPEDIENTE CLIENTE PROCREA  **********************************/
	public RespuestaSVC insertarClienteExpedienteStdDao(String solicitanteId, String rutaArchivo, String tipoArchivo, String claveArchivo, Integer usuario) {
		
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			log.info(solicitanteId + " :: " + claveArchivo + " :: " + rutaArchivo);
			
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList("select d.id from nucleocentral.ncdocumentos d where d.clave = ?", claveArchivo);
			int idClave = 0;
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					idClave = Comun._I(row.get("id"));
				}
			}
			final Integer idClaveArchivo = idClave;
			
			Integer id = listClienteExpedienteStdDao(solicitanteId, idClaveArchivo);
			
			if(id > 0){
				actualizarClienteExpedienteStdDao(solicitanteId, idClaveArchivo, tipoArchivo, rutaArchivo, usuario);
				
			}else{		
				
				jdbcTemplatePr.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_ClienteExpedienteStd, new String[]{"id"});
				ps.setString(1, solicitanteId);
				ps.setInt(2, usuario);
				ps.setString(3, rutaArchivo);
				ps.setInt(4, idClaveArchivo);
				ps.setString(5, tipoArchivo);
				
				return ps;
				}, keyHolder);
	
				Number keyId = keyHolder.getKey();
				if(keyId.longValue() == 0L){
					respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INSERTAR_EXPEDIENTE, ErrProd.desc(ErrProd.ERROR_INSERTAR_EXPEDIENTE, claveArchivo));
				}else{
					respuesta.getBody().addValor("ID", keyId.longValue());
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}


	public RespuestaSVC actualizarClienteExpedienteStdDao(String solId, Integer idClaveArchivo, String tipoArchivo, String rutaArchivo, Integer usuarioId) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplatePr.update(UPDATE_ClienteExpedienteStd, tipoArchivo, rutaArchivo, usuarioId, solId, idClaveArchivo);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE,  ErrProd.desc(ErrProd.ERROR_ACTUALIZAR_EXPEDIENTE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, ErrProd.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public Integer listClienteExpedienteStdDao(String solId, Integer idClaveArchivo) {
		Integer id = -1;
		List<Map<String, Object>> rows = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplatePr.queryForList(READ_ClienteExpedienteStd, solId, idClaveArchivo);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					id = Comun._I(row.get("id"));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return id;
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

	public String getINSERT_ExpedienteCeroStd() {
		return INSERT_ExpedienteCeroStd;
	}

	public void setINSERT_ExpedienteCeroStd(String iNSERT_ExpedienteCeroStd) {
		INSERT_ExpedienteCeroStd = iNSERT_ExpedienteCeroStd;
	}

	public String getREAD_ExpedienteCeroStd() {
		return READ_ExpedienteCeroStd;
	}

	public void setREAD_ExpedienteCeroStd(String rEAD_ExpedienteCeroStd) {
		READ_ExpedienteCeroStd = rEAD_ExpedienteCeroStd;
	}

	public String getUPDATE_ExpedienteCeroStd() {
		return UPDATE_ExpedienteCeroStd;
	}

	public void setUPDATE_ExpedienteCeroStd(String uPDATE_ExpedienteCeroStd) {
		UPDATE_ExpedienteCeroStd = uPDATE_ExpedienteCeroStd;
	}

	public String getUPDATE_VerificadoCeroStd() {
		return UPDATE_VerificadoCeroStd;
	}

	public void setUPDATE_VerificadoCeroStd(String uPDATE_VerificadoCeroStd) {
		UPDATE_VerificadoCeroStd = uPDATE_VerificadoCeroStd;
	}

	public String getINSERT_CuentaRefExpedienteCeroStd() {
		return INSERT_CuentaRefExpedienteCeroStd;
	}

	public void setINSERT_CuentaRefExpedienteCeroStd(String iNSERT_CuentaRefExpedienteCeroStd) {
		INSERT_CuentaRefExpedienteCeroStd = iNSERT_CuentaRefExpedienteCeroStd;
	}

	public String getREAD_CuentaRefExpedienteCeroStd() {
		return READ_CuentaRefExpedienteCeroStd;
	}

	public void setREAD_CuentaRefExpedienteCeroStd(String rEAD_CuentaRefExpedienteCeroStd) {
		READ_CuentaRefExpedienteCeroStd = rEAD_CuentaRefExpedienteCeroStd;
	}

	public String getUPDATE_CuentaRefExpedienteCeroStd() {
		return UPDATE_CuentaRefExpedienteCeroStd;
	}

	public void setUPDATE_CuentaRefExpedienteCeroStd(String uPDATE_CuentaRefExpedienteCeroStd) {
		UPDATE_CuentaRefExpedienteCeroStd = uPDATE_CuentaRefExpedienteCeroStd;
	}

	public String getUPDATE_CuentaRefVerificadoCeroStd() {
		return UPDATE_CuentaRefVerificadoCeroStd;
	}

	public void setUPDATE_CuentaRefVerificadoCeroStd(String uPDATE_CuentaRefVerificadoCeroStd) {
		UPDATE_CuentaRefVerificadoCeroStd = uPDATE_CuentaRefVerificadoCeroStd;
	}

	public String getINSERT_ClienteExpedienteStd() {
		return INSERT_ClienteExpedienteStd;
	}

	public void setINSERT_ClienteExpedienteStd(String iNSERT_ClienteExpedienteStd) {
		INSERT_ClienteExpedienteStd = iNSERT_ClienteExpedienteStd;
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getUPDATE_ClienteExpedienteStd() {
		return UPDATE_ClienteExpedienteStd;
	}

	public void setUPDATE_ClienteExpedienteStd(String uPDATE_ClienteExpedienteStd) {
		UPDATE_ClienteExpedienteStd = uPDATE_ClienteExpedienteStd;
	}

	public String getREAD_ClienteExpedienteStd() {
		return READ_ClienteExpedienteStd;
	}

	public void setREAD_ClienteExpedienteStd(String rEAD_ClienteExpedienteStd) {
		READ_ClienteExpedienteStd = rEAD_ClienteExpedienteStd;
	}

	public String getREAD_DocumentosNivelStd() {
		return READ_DocumentosNivelStd;
	}

	public void setREAD_DocumentosNivelStd(String rEAD_DocumentosNivelStd) {
		READ_DocumentosNivelStd = rEAD_DocumentosNivelStd;
	}
}

