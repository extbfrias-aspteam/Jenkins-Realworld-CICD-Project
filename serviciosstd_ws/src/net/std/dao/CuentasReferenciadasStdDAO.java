package net.std.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.google.gson.Gson;

import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.data.ClabeIzelOBJ;
import net.std.data.CuentaReferenciadaOBJ;
import net.std.data.CuentaReferenciadaVolumenOBJ;

public class CuentasReferenciadasStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CuentasReferenciadasStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	private JdbcTemplate jdbcTemplateSti;
	private String READ_CuentaReferenciadaStd;
	private String READ_CuentaReferenciadaMasivaStd;
	private String LIST_CuentaReferenciadaMasivaStd;
	private String INSERT_CuentaReferenciadaStd;
	private String INSERT_CuentaReferenciadaMasivasStd;
	private String INSERT_CuentaReferenciadaRepresentanteStd;
	private String UPDATE_CuentaReferenciadaMasivasStd;
	private String READ_IzelCuentaClabeStd;
	private String INSERT_IzelCuentaClabeStd;
	private String DELETE_CuentaReferenciadaMasivasStd;
	private String UPDATE_CuentaReferenciadaStd;
	private String READ_TipoCuentaIDReferenciadaStd;
	private String READ_CuentaConcentradoraReferenciadaStd;
	private String LIST_TipoCuentaStd;
	private String DELETE_CuentaReferenciadaCeroStd;
	private String READ_TiposCuentasAhorroStd;
	private String READ_BanderaValidaStd;
	private String INSERT_DatosSolicitanteCuentaReferenciadaStd;
	private String READ_CuentaReferenciadaSolicitanteStd;
	private String UPDATE_CuentaReferenciadaSolicitanteStd;
	
	private String READ_IzelCuentaClabeCorrelativoStd;
	private String UPDATE_IzelCuentaClabeCorrelativoStd;
	private String DELETE_CuentaReferenciadaIzelStd;
	
	public RespuestaSVC leerCuentaConcentradoraReferenciadaStdDao(String cuentaConcentradora, String cuentaReferenciada, String tipoCuenta) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaReferenciadaOBJ obj = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_CuentaConcentradoraReferenciadaStd, cuentaConcentradora, cuentaReferenciada,tipoCuenta);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					obj = new CuentaReferenciadaOBJ();
					obj.setId(Comun._L(row.get("ID")));
					obj.setCuenta_id(Comun._I(row.get("CUENTA_ID")));
					obj.setTipo_cliente(Comun._T(row.get("TIPO_CLIENTE")));
					obj.setCuenta(Comun._T(row.get("CUENTA")));
					obj.setClabe_interbancaria(Comun._T(row.get("CLABE_INTERBANCARIA")));
					obj.setCuenta_referencia(Comun._T(row.get("CUENTA_REFERENCIA")));
					obj.setNombre_referencia(Comun._T(row.get("NOMBRE_REFERENCIA")));
					obj.setRfc_referencia(Comun._T(row.get("RFC_REFERENCIA")));
					obj.setCurp_referencia(Comun._T(row.get("CURP_REFERENCIA")));
					obj.setCorreo_referencia(Comun._T(row.get("CORREO_REFERENCIA")));
					obj.setTelefono_referencia(Comun._T(row.get("TELEFONO_REFERENCIA")));
					obj.setEstatus_id(Comun._I(row.get("ESTATUS_ID")));
					obj.setObservaciones(Comun._T(row.get("OBSERVACIONES")));
					obj.setFecha(Comun._T(row.get("FECHA")));
					obj.setControl(Comun._T(row.get("CONTROL")));
					obj.setTipo_cuenta_id(Comun._L(row.get("TIPO_CUENTA_ID")));
					obj.setTipo_cuenta(Comun._T(row.get("TIPO_CUENTA")));
					obj.setValor(Comun._T(row.get("VALOR")));
					break;
				}
			}
			
			if(obj != null){
				respuesta.getBody().addValor("CUENTA", obj);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_CONCENTRADORA_REFERENCIADA,  Errores.desc(Errores.ERROR_LEER_CUENTA_CONCENTRADORA_REFERENCIADA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCuentaReferenciadaStdDao(String cuentaReferenciada, String tipoCuenta) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaReferenciadaOBJ obj = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			
			rows = jdbcTemplate.queryForList(READ_CuentaReferenciadaStd, tipoCuenta, cuentaReferenciada);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					obj = new CuentaReferenciadaOBJ();
					obj.setId(Comun._L(row.get("ID")));
					obj.setCuenta_id(Comun._I(row.get("CUENTA_ID")));
					obj.setTipo_cliente(Comun._T(row.get("TIPO_CLIENTE")));
					obj.setCuenta(Comun._T(row.get("CUENTA")));
					obj.setClabe_interbancaria(Comun._T(row.get("CLABE_INTERBANCARIA")));
					obj.setCuenta_referencia(Comun._T(row.get("CUENTA_REFERENCIA")));
					obj.setNombre_referencia(Comun._T(row.get("NOMBRE_REFERENCIA")));
					obj.setRfc_referencia(Comun._T(row.get("RFC_REFERENCIA")));
					obj.setCurp_referencia(Comun._T(row.get("CURP_REFERENCIA")));
					obj.setCorreo_referencia(Comun._T(row.get("CORREO_REFERENCIA")));
					obj.setTelefono_referencia(Comun._T(row.get("TELEFONO_REFERENCIA")));
					obj.setEstatus_id(Comun._I(row.get("ESTATUS_ID")));
					obj.setObservaciones(Comun._T(row.get("OBSERVACIONES")));
					obj.setFecha(Comun._T(row.get("FECHA")));
					obj.setControl(Comun._T(row.get("CONTROL")));
					obj.setTipo_cuenta_id(Comun._L(row.get("TIPO_CUENTA_ID")));
					obj.setTipo_cuenta(Comun._T(row.get("TIPO_CUENTA")));
					obj.setValor(Comun._T(row.get("VALOR")));
					break;
				}
			}
			
			if(obj != null){
				respuesta.getBody().addValor("CUENTA", obj);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCuentaReferenciadaSolicitanteStdDao(String cuentaReferenciada) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		Map<String, String> mapa = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			
			rows = jdbcTemplate.queryForList(READ_CuentaReferenciadaSolicitanteStd, cuentaReferenciada);
			if(rows != null && !rows.isEmpty()){
				 mapa = new HashMap<>();
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						mapa.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
				}
			}
			
			if(mapa != null){
				respuesta.getBody().addValor("CUENTA", mapa);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCuentaReferenciadaMasivaStdDao(Long control, String cuentaReferenciada) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaReferenciadaVolumenOBJ obj = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_CuentaReferenciadaMasivaStd, control, cuentaReferenciada);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					obj = new CuentaReferenciadaVolumenOBJ();
					obj.setControl(Comun._T(row.get("CONTROL")));
					obj.setConsecutivo(Comun._I(row.get("CONSECUTIVO")));
					obj.setCuenta_concentradora(Comun._T(row.get("CUENTA_CONCENTRADORA")));
					obj.setCuenta_referencia(Comun._T(row.get("CUENTA_REFERENCIA")));
					obj.setNombre_referencia(Comun._T(row.get("NOMBRE_REFERENCIA")));
					obj.setRfc_referencia(Comun._T(row.get("RFC_REFERENCIA")));
					obj.setCurp_referencia(Comun._T(row.get("CURP_REFERENCIA")));
					obj.setCorreo_referencia(Comun._T(row.get("CORREO_REFERENCIA")));
					obj.setTelefono_referencia(Comun._T(row.get("TELEFONO_REFERENCIA")));
					obj.setFecha(Comun._T(row.get("FECHA")));
					obj.setProcesado(Comun._I(row.get("PROCESADO")));
					obj.setError(Comun._I(row.get("ERROR")));
					obj.setObservaciones(Comun._T(row.get("OBSERVACIONES")));
					break;
				}
			}
			
			if(obj != null){
				respuesta.getBody().addValor("CUENTA", obj);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA_MASIVA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	
	public RespuestaSVC listarCuentaReferenciadaMasivaStdDao(String control, Integer procesado) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<CuentaReferenciadaVolumenOBJ> lst = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			//rows = jdbcTemplate.queryForList(LIST_CuentaReferenciadaMasivaStd, control, procesado);
			rows = jdbcTemplate.queryForList(LIST_CuentaReferenciadaMasivaStd, control);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					CuentaReferenciadaVolumenOBJ obj = new CuentaReferenciadaVolumenOBJ();
					obj.setControl(Comun._T(row.get("CONTROL")));
					obj.setConsecutivo(Comun._I(row.get("CONSECUTIVO")));
					obj.setCuenta_concentradora(Comun._T(row.get("CUENTA_CONCENTRADORA")));
					obj.setCuenta_referencia(Comun._T(row.get("CUENTA_REFERENCIA")));
					obj.setNombre_referencia(Comun._T(row.get("NOMBRE_REFERENCIA")));
					obj.setRfc_referencia(Comun._T(row.get("RFC_REFERENCIA")));
					obj.setCurp_referencia(Comun._T(row.get("CURP_REFERENCIA")));
					obj.setCorreo_referencia(Comun._T(row.get("CORREO_REFERENCIA")));
					obj.setTelefono_referencia(Comun._T(row.get("TELEFONO_REFERENCIA")));
					obj.setFecha(Comun._T(row.get("FECHA")));
					obj.setProcesado(Comun._I(row.get("PROCESADO")));
					obj.setError(Comun._I(row.get("ERROR")));
					obj.setObservaciones(Comun._T(row.get("OBSERVACIONES")));
					obj.setTipoCuenta(Comun._T(row.get("TIPO_CUENTA")));
					obj.setValor(Comun._T(row.get("VALOR")));
					obj.setAccion(Comun._T(row.get("ACCION")));
					
					
					if(lst == null) lst = new ArrayList<>();
					lst.add(obj);
				}
			}
			
			if(lst != null){
				respuesta.getBody().addValor("LISTA_CUENTA", lst);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA_MASIVA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	
	public RespuestaSVC insertarCuentaReferenciadaStdDao(CuentaReferenciadaOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_CuentaReferenciadaStd, new String[]{"id"});
			ps.setObject(1, Comun._I(obj.getCuenta_id()), java.sql.Types.INTEGER);
			ps.setString(2, Comun._T(obj.getCuenta_referencia()));
			ps.setString(3, Comun._T(obj.getNombre_referencia()));
			ps.setString(4, Comun._T(obj.getRfc_referencia()));
			ps.setString(5, Comun._T(obj.getCurp_referencia()));
			ps.setString(6, Comun._T(obj.getCorreo_referencia()));
			ps.setString(7, Comun._T(obj.getTelefono_referencia()));
			ps.setString(8, Comun._T(obj.getObservaciones()));
			ps.setString(9, Comun._T(obj.getControl()));
			ps.setObject(10, Comun._I(Constantes.USUARIO_ID), java.sql.Types.INTEGER);
			ps.setString(11, Comun._T(obj.getTipo_cuenta()));
			ps.setString(12, Comun._T(obj.getTipo_cuenta()));
			ps.setString(13, Comun._T(obj.getValor()));
			
			ps.setString(14, Comun._T(obj.getPersona_id()));			
			ps.setString(15, Comun._T(obj.getTipo_cuenta_nivel()));
			ps.setString(16, Comun._T(obj.getUnidad_negocio()));
			
			ps.setString(17, Comun._T(obj.getCuenta_referencia()));
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA));
			}else{
				respuesta.getBody().addValor("CUENTA_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertarCuentaReferenciadaRepresentanteStdDao(String cuentaReferencia, String solId, String repLegalId, Integer principal) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_CuentaReferenciadaRepresentanteStd, new String[]{"id"});
			ps.setString(1, cuentaReferencia);
			ps.setString(2, solId);
			ps.setString(3, repLegalId);
			ps.setInt(4, principal);
			ps.setObject(5, Comun._I(Constantes.USUARIO_ID), java.sql.Types.INTEGER);
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA));
			}else{
				respuesta.getBody().addValor("CUENTA_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertarCuentaReferenciadaMasivaStdDao(CuentaReferenciadaVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_CuentaReferenciadaMasivasStd, new String[]{"id"});
			ps.setString(1, Comun._T(obj.getControl()));
			ps.setObject(2, Comun._I(obj.getConsecutivo()), java.sql.Types.INTEGER);
			ps.setString(3, Comun._T(obj.getCuenta_concentradora()));
			ps.setString(4, Comun._T(obj.getCuenta_referencia()));
			ps.setString(5, Comun._T(obj.getNombre_referencia()));
			ps.setString(6, Comun._T(obj.getRfc_referencia()));
			ps.setString(7, Comun._T(obj.getCurp_referencia()));
			ps.setString(8, Comun._T(obj.getCorreo_referencia()));
			ps.setString(9, Comun._T(obj.getTelefono_referencia()));
			ps.setObject(10, Comun._I(obj.getProcesado()), java.sql.Types.INTEGER);
			ps.setObject(11, Comun._I(obj.getError()), java.sql.Types.INTEGER);
			ps.setObject(12, Comun._I(obj.getObservaciones()), java.sql.Types.INTEGER);
			
			ps.setString(13, Comun._T(obj.getTipoCuenta()));
			ps.setString(14, Comun._T(obj.getValor()));
			ps.setString(15, Comun._T(obj.getAccion()));
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA));
			}else{
				respuesta.getBody().addValor("CUENTA_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertarDatosSolcitanteCuentaReferenciadaStdDao(CuentaReferenciadaVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();
		int rows = 0;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cuenta_referencia", obj.getCuenta_referencia());
			
			paramMap.put("nombre_cuenta",Comun._T(obj.getSolicitante().getNombre_cuenta()));
			paramMap.put("tipo_persona_cuenta",Comun._T(obj.getSolicitante().getTipo_persona_cuenta()));
			paramMap.put("pr_apellido_cuenta",Comun._T(obj.getSolicitante().getPr_apellido_cuenta()));
			paramMap.put("sg_apellido_cuenta",Comun._T(obj.getSolicitante().getSg_apellido_cuenta()));
			paramMap.put("denominacion_cuenta",Comun._T(obj.getSolicitante().getDenominacion_cuenta()));
			paramMap.put("rfc_cuenta",Comun._T(obj.getSolicitante().getRfc_cuenta()));
			paramMap.put("curp_cuenta",Comun._T(obj.getSolicitante().getCurp_cuenta()));
			paramMap.put("calle_principal_cuenta",Comun._T(obj.getSolicitante().getCalle_principal_cuenta()));
			paramMap.put("calle_secundaria_cuenta",Comun._T(obj.getSolicitante().getCalle_secundaria_cuenta()));
			paramMap.put("calle_secundaria2_cuenta",Comun._T(obj.getSolicitante().getCalle_secundaria2_cuenta()));
			paramMap.put("no_interior_cuenta",Comun._T(obj.getSolicitante().getNo_interior_cuenta()));
			paramMap.put("no_exterior_cuenta",Comun._T(obj.getSolicitante().getNo_exterior_cuenta()));
			paramMap.put("coloniaId_cuenta",Comun._T(obj.getSolicitante().getColoniaId_cuenta()));
			paramMap.put("colonia_cuenta",Comun._T(obj.getSolicitante().getColonia_cuenta()));
			paramMap.put("cp_cuenta",Comun._T(obj.getSolicitante().getCp_cuenta()));
			paramMap.put("ciudad_cuenta",Comun._T(obj.getSolicitante().getCiudad_cuenta()));
			paramMap.put("celular_cuenta",Comun._T(obj.getSolicitante().getCelular_cuenta()));
			paramMap.put("correo_cuenta",Comun._T(obj.getSolicitante().getCorreo_cuenta()));
			paramMap.put("genero_cuenta",Comun._T(obj.getSolicitante().getGenero_cuenta()));
			paramMap.put("tipo_identId_cuenta",Comun._T(obj.getSolicitante().getTipo_identId_cuenta()));
			paramMap.put("num_ident_cuenta",Comun._T(obj.getSolicitante().getNum_ident_cuenta()));
			paramMap.put("fecha_nac_cuenta",Comun._T(obj.getSolicitante().getFecha_nac_cuenta()));
			paramMap.put("entidad_nacId_cuenta",Comun._T(obj.getSolicitante().getEntidad_nacId_cuenta()));
			paramMap.put("pais_nacId_cuenta",Comun._T(obj.getSolicitante().getPais_nacId_cuenta()));
			paramMap.put("pais_nac_cuenta",Comun._T(obj.getSolicitante().getPais_nac_cuenta()));
			paramMap.put("nacionalidadId_cuenta",Comun._T(obj.getSolicitante().getNacionalidadId_cuenta()));
			paramMap.put("nacionalidad_cuenta",Comun._T(obj.getSolicitante().getNacionalidad_cuenta()));
			paramMap.put("serie_firma_elect_cuenta",Comun._T(obj.getSolicitante().getSerie_firma_elect_cuenta()));
			paramMap.put("ocupacionId_cuenta",Comun._T(obj.getSolicitante().getOcupacionId_cuenta()));
			paramMap.put("ocupacion_cuenta",Comun._T(obj.getSolicitante().getOcupacion_cuenta()));
			paramMap.put("telefono_cuenta",Comun._T(obj.getSolicitante().getTelefono_cuenta()));
			paramMap.put("geolocalizacion_cuenta",Comun._T(obj.getSolicitante().getGeolocalizacion_cuenta()));
			paramMap.put("unidad_negocio_cuenta",Comun._T(obj.getSolicitante().getUnidad_negocio_cuenta()));
			paramMap.put("monto_max_aho_cuenta",Comun._T(obj.getSolicitante().getMonto_max_aho_cuenta()));
			paramMap.put("ingresos_cuenta",Comun._T(obj.getSolicitante().getIngresos_cuenta()));
			paramMap.put("nivel_cuenta",Comun._T(obj.getSolicitante().getNivel_cuenta()));

			paramMap.put("nombre_cuenta_replegal",Comun._T(obj.getRepLegal().getNombre_cuenta()));
			paramMap.put("tipo_persona_cuenta_replegal",Comun._T(obj.getRepLegal().getTipo_persona_cuenta()));
			paramMap.put("pr_apellido_cuenta_replegal",Comun._T(obj.getRepLegal().getPr_apellido_cuenta()));
			paramMap.put("sg_apellido_cuenta_replegal",Comun._T(obj.getRepLegal().getSg_apellido_cuenta()));
			paramMap.put("denominacion_cuenta_replegal",Comun._T(obj.getRepLegal().getDenominacion_cuenta()));
			paramMap.put("rfc_cuenta_replegal",Comun._T(obj.getRepLegal().getRfc_cuenta()));
			paramMap.put("curp_cuenta_replegal",Comun._T(obj.getRepLegal().getCurp_cuenta()));
			paramMap.put("calle_principal_cuenta_replegal",Comun._T(obj.getRepLegal().getCalle_principal_cuenta()));
			paramMap.put("calle_secundaria_cuenta_replegal",Comun._T(obj.getRepLegal().getCalle_secundaria_cuenta()));
			paramMap.put("calle_secundaria2_cuenta_replegal",Comun._T(obj.getRepLegal().getCalle_secundaria2_cuenta()));
			paramMap.put("no_interior_cuenta_replegal",Comun._T(obj.getRepLegal().getNo_interior_cuenta()));
			paramMap.put("no_exterior_cuenta_replegal",Comun._T(obj.getRepLegal().getNo_exterior_cuenta()));
			paramMap.put("coloniaId_cuenta_replegal",Comun._T(obj.getRepLegal().getColoniaId_cuenta()));
			paramMap.put("colonia_cuenta_replegal",Comun._T(obj.getRepLegal().getColonia_cuenta()));
			paramMap.put("cp_cuenta_replegal",Comun._T(obj.getRepLegal().getCp_cuenta()));
			paramMap.put("ciudad_cuenta_replegal",Comun._T(obj.getRepLegal().getCiudad_cuenta()));
			paramMap.put("celular_cuenta_replegal",Comun._T(obj.getRepLegal().getCelular_cuenta()));
			paramMap.put("correo_cuenta_replegal",Comun._T(obj.getRepLegal().getCorreo_cuenta()));
			paramMap.put("genero_cuenta_replegal",Comun._T(obj.getRepLegal().getGenero_cuenta()));
			paramMap.put("tipo_identId_cuenta_replegal",Comun._T(obj.getRepLegal().getTipo_identId_cuenta()));
			paramMap.put("num_ident_cuenta_replegal",Comun._T(obj.getRepLegal().getNum_ident_cuenta()));
			paramMap.put("fecha_nac_cuenta_replegal",Comun._T(obj.getRepLegal().getFecha_nac_cuenta()));
			paramMap.put("entidad_nacId_cuenta_replegal",Comun._T(obj.getRepLegal().getEntidad_nacId_cuenta()));
			paramMap.put("pais_nacId_cuenta_replegal",Comun._T(obj.getRepLegal().getPais_nacId_cuenta()));
			paramMap.put("pais_nac_cuenta_replegal",Comun._T(obj.getRepLegal().getPais_nac_cuenta()));
			paramMap.put("nacionalidadId_cuenta_replegal",Comun._T(obj.getRepLegal().getNacionalidadId_cuenta()));
			paramMap.put("nacionalidad_cuenta_replegal",Comun._T(obj.getRepLegal().getNacionalidad_cuenta()));
			paramMap.put("serie_firma_elect_cuenta_replegal",Comun._T(obj.getRepLegal().getSerie_firma_elect_cuenta()));
			paramMap.put("ocupacionId_cuenta_replegal",Comun._T(obj.getRepLegal().getOcupacionId_cuenta()));
			paramMap.put("ocupacion_cuenta_replegal",Comun._T(obj.getRepLegal().getOcupacion_cuenta()));
			paramMap.put("telefono_cuenta_replegal",Comun._T(obj.getRepLegal().getTelefono_cuenta()));
			paramMap.put("geolocalizacion_cuenta_replegal",Comun._T(obj.getRepLegal().getGeolocalizacion_cuenta()));
			paramMap.put("unidad_negocio_cuenta_replegal",Comun._T(obj.getRepLegal().getUnidad_negocio_cuenta()));
			paramMap.put("monto_max_aho_cuenta_replegal",Comun._T(obj.getRepLegal().getMonto_max_aho_cuenta()));
			paramMap.put("ingresos_cuenta_replegal",Comun._T(obj.getRepLegal().getIngresos_cuenta()));
			paramMap.put("nivel_cuenta_replegal",Comun._T(obj.getRepLegal().getNivel_cuenta()));

	           
	        rows = namedJdbcTemplate.update(INSERT_DatosSolicitanteCuentaReferenciadaStd,paramMap);
	        


		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s  :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC actualizarCuentaReferenciadaSolicitanteStdDao(String cuenta_referencia, String solId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try {
			int row = jdbcTemplate.update(UPDATE_CuentaReferenciadaSolicitanteStd,  solId, cuenta_referencia);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		return respuesta;
	}
	public RespuestaSVC actualizarCuentaReferenciadaMasivaStdDao(CuentaReferenciadaVolumenOBJ obj) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_CuentaReferenciadaMasivasStd,  Comun._I(obj.getProcesado()),
																				Comun._I(obj.getError()),
																				Comun._T(obj.getObservaciones()),
																				Comun._L(obj.getControl()));
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC eliminarCuentaReferenciadaMasivaStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(DELETE_CuentaReferenciadaMasivasStd);
			if(row < 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC eliminarCuentaReferenciadaCeroStdDao(String cuentaReferencia) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(DELETE_CuentaReferenciadaCeroStd,cuentaReferencia);
			if(row < 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC eliminarCuentaReferenciadaIzelStdDao(String cuentaReferencia) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplateSti.update(DELETE_CuentaReferenciadaIzelStd,cuentaReferencia);
			if(row < 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ELIMINAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerIzelCuentaClabeStdDao(String cuentaReferenciada) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		ClabeIzelOBJ obj = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplateSti.queryForList(READ_IzelCuentaClabeStd, cuentaReferenciada);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					obj = new ClabeIzelOBJ();
					obj.setId(Comun._L(row.get("ID")));
					obj.setEstatus_id(Comun._T(row.get("ESTATUS_ID")));
					obj.setFecha_baja(Comun._T(row.get("FECHA_BAJA")));
					obj.setMotivo_baja_id(Comun._T(row.get("MOTIVO_BAJA_ID")));
					obj.setFecha_creacion(Comun._T(row.get("FECHA_CREACION")));
					obj.setEmpresa_id(Comun._T(row.get("EMPRESA_ID")));
					obj.setCuenta_clabe(Comun._T(row.get("CUENTA_CLABE")));
					obj.setAplicacion_id(Comun._T(row.get("APLICACION_ID")));
					obj.setClabe_2(Comun._T(row.get("CLABE_2")));
					obj.setTarjeta(Comun._T(row.get("TARJETA")));
					obj.setTelefono(Comun._T(row.get("TELEFONO")));
					break;
				}
			}
			
			if(obj != null){
				respuesta.getBody().addValor("CUENTA", obj);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CUENTA_REFERENCIADA,  Errores.desc(Errores.ERROR_LEER_CUENTA_REFERENCIADA));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	
	public RespuestaSVC leerTipoCuentaID(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		Long id = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_TipoCuentaIDReferenciadaStd, clave);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					id = Comun._L(row.get("ID"));
					break;
				}
			}
			
			if(id != null){
				respuesta.getBody().addValor("ID", id);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_TIPO_CUENTA_ID,  Errores.desc(Errores.ERROR_LEER_TIPO_CUENTA_ID));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	public Boolean leerVariableExpedienteStdDao() {
		Boolean bandera = false;
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplate.queryForList(READ_BanderaValidaStd);
			if(rows != null && !rows.isEmpty()){
				bandera = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bandera;
	}
	public RespuestaSVC listarTiposCuentasAhorroStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		
		try {
			rows = jdbcTemplate.queryForList(READ_TiposCuentasAhorroStd);
			if(rows != null && !rows.isEmpty()){
				respuesta.getBody().addValor("LISTA", rows);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO,  Errores.desc(Errores.ERROR_INESPERADO,"TIPOS CUENTAS DE AHORRO"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC insertarIzelCuentaClabeStdDao(String cuentaReferencia, String aplicacionId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			//String correlativo = GeneraCorrelativo();
			jdbcTemplateSti.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_IzelCuentaClabeStd, new String[]{"aa"});
			ps.setObject(1, Comun._L(GeneraCorrelativo()), java.sql.Types.BIGINT);
			ps.setObject(2, Comun._L(Constantes.ESTATUS_ALTA_IZEL_ID), java.sql.Types.BIGINT);
			ps.setObject(3, Comun._I(Constantes.EMPRESA_ID), java.sql.Types.INTEGER);
			ps.setString(4, Comun._T(cuentaReferencia));
			ps.setString(5, Comun._T(aplicacionId));
			return ps;
			}, keyHolder);
			
			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA, Errores.desc(Errores.ERROR_INSERTAR_CUENTA_REFERENCIADA));
			}else{
				respuesta.getBody().addValor("CUENTA_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	private String GeneraCorrelativo(){
		String correlativo = null;
		int numeroDigitos = 15;
		List<Map<String, Object>> rows = null;
		Long lCorrelativo = -1L;
		try{
			/* LEE EL CORRELATIVO ACTUAL */
			rows = jdbcTemplateSti.queryForList(READ_IzelCuentaClabeCorrelativoStd, Comun._L(Constantes.EMPRESA_ID), Comun._T(Constantes.TABLA_CORRELATIVO));
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					lCorrelativo = Comun._L(row.get("N_NUMCOR"));
				}
			}
			
			if(lCorrelativo == null || lCorrelativo.longValue() <= 0){
				return correlativo;
			}
			
			lCorrelativo = lCorrelativo + 1L;
			
			int row = jdbcTemplateSti.update(UPDATE_IzelCuentaClabeCorrelativoStd,  lCorrelativo, Comun._L(Constantes.EMPRESA_ID), Comun._T(Constantes.TABLA_CORRELATIVO));
			if(row < 0){
				return correlativo;
			}
			
			String tmpCorrelativo = Comun._T(lCorrelativo);
	        String correlativosdeCeros = (llenaCeros("0", numeroDigitos - 3) + tmpCorrelativo).substring(tmpCorrelativo.length(),tmpCorrelativo.length()+ numeroDigitos- 3);
		    correlativo = Comun._T(Constantes.EMPRESA_ID) + Comun._T(correlativosdeCeros);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return correlativo; 
	}

	public RespuestaSVC actualizarEstadoIDCuentasRefStdDao(CuentaReferenciadaOBJ obj, String estatus) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_CuentaReferenciadaStd, estatus, obj.getCuenta_id(), obj.getCuenta_referencia(), obj.getTipo_cuenta_id()); 
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA,  Errores.desc(Errores.ERROR_ACTUALIZAR_CUENTA_REFERENCIADA_MASIVA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC getTipoCuentaStdDao() {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(LIST_TipoCuentaStd);
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
	
	private static String llenaCeros(String strCad, int intNumero) {
		String strAcumulaCadena = "";
		for (int i=0; i<intNumero; i++){
			strAcumulaCadena = strAcumulaCadena + strCad;
		}
		return strAcumulaCadena;
	}
	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}
	
	@SuppressWarnings("unused")
	private java.sql.Date _DF(Object obj){
		return obj == null ? null : (obj instanceof java.sql.Date ? (java.sql.Date)obj : null);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getREAD_CuentaReferenciadaStd() {
		return READ_CuentaReferenciadaStd;
	}

	public void setREAD_CuentaReferenciadaStd(String rEAD_CuentaReferenciadaStd) {
		READ_CuentaReferenciadaStd = rEAD_CuentaReferenciadaStd;
	}

	public String getREAD_CuentaReferenciadaMasivaStd() {
		return READ_CuentaReferenciadaMasivaStd;
	}

	public void setREAD_CuentaReferenciadaMasivaStd(String rEAD_CuentaReferenciadaMasivaStd) {
		READ_CuentaReferenciadaMasivaStd = rEAD_CuentaReferenciadaMasivaStd;
	}

	public String getLIST_CuentaReferenciadaMasivaStd() {
		return LIST_CuentaReferenciadaMasivaStd;
	}

	public void setLIST_CuentaReferenciadaMasivaStd(String lIST_CuentaReferenciadaMasivaStd) {
		LIST_CuentaReferenciadaMasivaStd = lIST_CuentaReferenciadaMasivaStd;
	}

	public String getINSERT_CuentaReferenciadaStd() {
		return INSERT_CuentaReferenciadaStd;
	}

	public void setINSERT_CuentaReferenciadaStd(String iNSERT_CuentaReferenciadaStd) {
		INSERT_CuentaReferenciadaStd = iNSERT_CuentaReferenciadaStd;
	}

	public String getINSERT_CuentaReferenciadaMasivasStd() {
		return INSERT_CuentaReferenciadaMasivasStd;
	}

	public void setINSERT_CuentaReferenciadaMasivasStd(String iNSERT_CuentaReferenciadaMasivasStd) {
		INSERT_CuentaReferenciadaMasivasStd = iNSERT_CuentaReferenciadaMasivasStd;
	}

	public String getUPDATE_CuentaReferenciadaMasivasStd() {
		return UPDATE_CuentaReferenciadaMasivasStd;
	}

	public void setUPDATE_CuentaReferenciadaMasivasStd(String uPDATE_CuentaReferenciadaMasivasStd) {
		UPDATE_CuentaReferenciadaMasivasStd = uPDATE_CuentaReferenciadaMasivasStd;
	}

	public String getREAD_IzelCuentaClabeStd() {
		return READ_IzelCuentaClabeStd;
	}

	public void setREAD_IzelCuentaClabeStd(String rEAD_IzelCuentaClabeStd) {
		READ_IzelCuentaClabeStd = rEAD_IzelCuentaClabeStd;
	}

	public String getINSERT_IzelCuentaClabeStd() {
		return INSERT_IzelCuentaClabeStd;
	}

	public void setINSERT_IzelCuentaClabeStd(String iNSERT_IzelCuentaClabeStd) {
		INSERT_IzelCuentaClabeStd = iNSERT_IzelCuentaClabeStd;
	}

	public String getDELETE_CuentaReferenciadaMasivasStd() {
		return DELETE_CuentaReferenciadaMasivasStd;
	}

	public void setDELETE_CuentaReferenciadaMasivasStd(String dELETE_CuentaReferenciadaMasivasStd) {
		DELETE_CuentaReferenciadaMasivasStd = dELETE_CuentaReferenciadaMasivasStd;
	}

	public String getREAD_IzelCuentaClabeCorrelativoStd() {
		return READ_IzelCuentaClabeCorrelativoStd;
	}

	public void setREAD_IzelCuentaClabeCorrelativoStd(String rEAD_IzelCuentaClabeCorrelativoStd) {
		READ_IzelCuentaClabeCorrelativoStd = rEAD_IzelCuentaClabeCorrelativoStd;
	}

	public String getUPDATE_IzelCuentaClabeCorrelativoStd() {
		return UPDATE_IzelCuentaClabeCorrelativoStd;
	}

	public void setUPDATE_IzelCuentaClabeCorrelativoStd(String uPDATE_IzelCuentaClabeCorrelativoStd) {
		UPDATE_IzelCuentaClabeCorrelativoStd = uPDATE_IzelCuentaClabeCorrelativoStd;
	}

	public String getUPDATE_CuentaReferenciadaStd() {
		return UPDATE_CuentaReferenciadaStd;
	}

	public void setUPDATE_CuentaReferenciadaStd(String uPDATE_CuentaReferenciadaStd) {
		UPDATE_CuentaReferenciadaStd = uPDATE_CuentaReferenciadaStd;
	}

	public String getREAD_TipoCuentaIDReferenciadaStd() {
		return READ_TipoCuentaIDReferenciadaStd;
	}

	public void setREAD_TipoCuentaIDReferenciadaStd(String rEAD_TipoCuentaIDReferenciadaStd) {
		READ_TipoCuentaIDReferenciadaStd = rEAD_TipoCuentaIDReferenciadaStd;
	}

	public String getREAD_CuentaConcentradoraReferenciadaStd() {
		return READ_CuentaConcentradoraReferenciadaStd;
	}

	public void setREAD_CuentaConcentradoraReferenciadaStd(String rEAD_CuentaConcentradoraReferenciadaStd) {
		READ_CuentaConcentradoraReferenciadaStd = rEAD_CuentaConcentradoraReferenciadaStd;
	}

	public String getLIST_TipoCuentaStd() {
		return LIST_TipoCuentaStd;
	}

	public void setLIST_TipoCuentaStd(String lIST_TipoCuentaStd) {
		LIST_TipoCuentaStd = lIST_TipoCuentaStd;
	}

	public String getDELETE_CuentaReferenciadaCeroStd() {
		return DELETE_CuentaReferenciadaCeroStd;
	}

	public void setDELETE_CuentaReferenciadaCeroStd(String dELETE_CuentaReferenciadaCeroStd) {
		DELETE_CuentaReferenciadaCeroStd = dELETE_CuentaReferenciadaCeroStd;
	}

	public String getDELETE_CuentaReferenciadaIzelStd() {
		return DELETE_CuentaReferenciadaIzelStd;
	}

	public void setDELETE_CuentaReferenciadaIzelStd(String dELETE_CuentaReferenciadaIzelStd) {
		DELETE_CuentaReferenciadaIzelStd = dELETE_CuentaReferenciadaIzelStd;
	}

	public String getINSERT_CuentaReferenciadaRepresentanteStd() {
		return INSERT_CuentaReferenciadaRepresentanteStd;
	}

	public void setINSERT_CuentaReferenciadaRepresentanteStd(String iNSERT_CuentaReferenciadaRepresentanteStd) {
		INSERT_CuentaReferenciadaRepresentanteStd = iNSERT_CuentaReferenciadaRepresentanteStd;
	}

	public String getREAD_TiposCuentasAhorroStd() {
		return READ_TiposCuentasAhorroStd;
	}

	public void setREAD_TiposCuentasAhorroStd(String rEAD_TiposCuentasAhorroStd) {
		READ_TiposCuentasAhorroStd = rEAD_TiposCuentasAhorroStd;
	}

	public String getREAD_BanderaValidaStd() {
		return READ_BanderaValidaStd;
	}

	public void setREAD_BanderaValidaStd(String rEAD_BanderaValidaStd) {
		READ_BanderaValidaStd = rEAD_BanderaValidaStd;
	}

	public String getINSERT_DatosSolicitanteCuentaReferenciadaStd() {
		return INSERT_DatosSolicitanteCuentaReferenciadaStd;
	}

	public void setINSERT_DatosSolicitanteCuentaReferenciadaStd(String iNSERT_DatosSolicitanteCuentaReferenciadaStd) {
		INSERT_DatosSolicitanteCuentaReferenciadaStd = iNSERT_DatosSolicitanteCuentaReferenciadaStd;
	}

	public String getREAD_CuentaReferenciadaSolicitanteStd() {
		return READ_CuentaReferenciadaSolicitanteStd;
	}

	public void setREAD_CuentaReferenciadaSolicitanteStd(String rEAD_CuentaReferenciadaSolicitanteStd) {
		READ_CuentaReferenciadaSolicitanteStd = rEAD_CuentaReferenciadaSolicitanteStd;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	public String getUPDATE_CuentaReferenciadaSolicitanteStd() {
		return UPDATE_CuentaReferenciadaSolicitanteStd;
	}

	public void setUPDATE_CuentaReferenciadaSolicitanteStd(String uPDATE_CuentaReferenciadaSolicitanteStd) {
		UPDATE_CuentaReferenciadaSolicitanteStd = uPDATE_CuentaReferenciadaSolicitanteStd;
	}
}

