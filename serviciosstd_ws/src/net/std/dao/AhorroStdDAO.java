package net.std.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import net.std.constantes.Errores;
import net.std.data.CuentaCompletaOBJ;
import net.std.data.CuentaOBJ;
import net.std.data.CuentaProductoOBJ;
import net.std.data.DatosMatrizRiesgoOBJ;
import net.std.data.DatosPldOBJ;
import net.std.data.NotificacionOBJ;
import net.std.data.SolicitanteOBJ;
import net.std.request.CambioEstadoCuentaReq;
import net.std.request.ValidarCuentaExpedienteReq;

public class AhorroStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AhorroStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplatePr;
	
	private String complementarioCoDiStd;
	private String complementarioCoDiReferenciadaStd;
	
	private String participanteSpeiTerceroStd;
	private String estatusAhorroStd;
	private String verificaEjeAhorroStd;
	private String actualizaEjeAhorroStd;
	private String INSERT_cuentasPldStd;
	private String INSERT_posicionGlobalStd;
	private String READ_ConceptosStd;
	private String INSERT_ConceptosStd;
	private String INSERT_cuentaStd;
	
	private String conceptosAhorroStd;
	private String conceptosProductoStd;
	private String copiarConceptosStd;
	
	private String READ_CuentaAhorroStd;
	
	private String READ_ProductoAhorroEstatusStd;
	private String READ_nombreSolicitanteStd;
	private String READ_diasFestivosStd;
	private String INSERT_representanteStd;
	
	private String INSERT_notificacionStd;
	private String UPDATE_notificacionEstatusStd;
	private String READ_notificacionesStd;
	
	private String UPDATE_cambiaEstadoCuentaStd;
	private String UPDATE_validarCuentaExpedienteStd;
	
	private String READ_participanteSpeiStd;
	private String referenciaQR;
	public String getReferenciaQR() {
		return referenciaQR;
	}

	public void setReferenciaQR(String referenciaQR) {
		this.referenciaQR = referenciaQR;
	}

	public Integer referenciaQR(){
		return jdbcTemplate.queryForObject(referenciaQR, Integer.class);
	}
	
	public RespuestaSVC leerCuentaAhorroClabeDao(String clabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaOBJ cta = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		log.info("AHORRO STD DAO LN 76 CONSULTA READ_PARTICIPANTESPEISTD");
		try {
			
			if("".equals(Comun._T(clabe))){
				respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_CUENTA_VACIA, Errores.desc(Errores.ERROR_CUENTA_VACIA, clabe));
				return respuesta;
			}
				
			rows = jdbcTemplate.queryForList(READ_participanteSpeiStd, clabe);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(cta == null) cta = new CuentaOBJ();
					cta.setId(Comun._I(row.get("ID")));
					cta.setCuenta(Comun._T(row.get("CUENTA")));
					cta.setReferencia(Comun._T(row.get("REFERENCIA")));
					cta.setClabeInterbancaria(Comun._T(row.get("CLABE")));
					cta.setEstatus(Comun._T(row.get("ESTATUS")));
					cta.setEstatusClave(Comun._T(row.get("CVE_ESTATUS")));
					
					cta.setProductoAhorroId(Comun._I(row.get("PRODUCTO_AHORRO_ID")));
					cta.setProductoAhorro(Comun._T(row.get("PRODUCTO")));
					
					cta.setPersonaId(Comun._T(row.get("PERSONA_ID")));
					cta.setClabeEje(Comun._T(row.get("CLABE_EJE")));
					cta.setStrFechaApertura(Comun._T(row.get("FECHA_APERTURA")));
					cta.setSucursalId(Comun._I(row.get("SUCURSAL_ID")));
					cta.setBase(Comun._T(row.get("BASE")));
					
					cta.setBloqueadoId(Comun._I(row.get("BLOQUEADO_ID")));
					cta.setBloqueado(Comun._T(row.get("BLOQUEADO")));
					cta.setFecha_bloqueado(Comun._T(row.get("FECHA_BLOQUEADO")));
					cta.setTipoCliente(Comun._T(row.get("TIPO_CLIENTE")));
					
					cta.setPan_id(Comun._I(row.get("PAN_ID")));
					cta.setPan(Comun._T(row.get("PAN")));
					cta.setCon_plastico(Comun._T(row.get("CON_PLASTICO")));
					cta.setTarjeta_principal(Comun._T(row.get("tarjeta_principal")));
				}
			}
			
			if(cta != null){
				respuesta.getBody().addValor("CUENTA", cta);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_CUENTA, clabe));
			
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC participanteSpeiDao(String cuentaParticipante, Integer dimension) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplate.queryForList(READ_participanteSpeiStd, cuentaParticipante);
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
		return respuesta;
	}

	public RespuestaSVC complementarioCoDiDao(String clabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplate.queryForList(complementarioCoDiStd, clabe);
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
		return respuesta;
	}
	
	public RespuestaSVC complementarioCoDiReferenciadaDao(String clabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplate.queryForList(complementarioCoDiReferenciadaStd, clabe);
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
		return respuesta;
	}


	public RespuestaSVC estatusAhorroDao(String clave) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		try {
			rows = jdbcTemplate.queryForList(estatusAhorroStd, clave);
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
		return respuesta;
	}


	public RespuestaSVC verificaEjeAhorroStdDao(String cuentaClabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		try {
			rows = jdbcTemplate.queryForList(verificaEjeAhorroStd, cuentaClabe);
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
		return respuesta;
	}

	public RespuestaSVC actualizarEjeAhorroStdDao(String cuenta, String clabeEje) {
		RespuestaSVC respuesta = new RespuestaSVC();

		try {
			int rows = jdbcTemplate.update(actualizaEjeAhorroStd, clabeEje, cuenta);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS INSERTADOS");
			else respuesta.getBody().addValor("RESULTADO", "OK");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}


	public RespuestaSVC crearCuentasPldStdDao(DatosPldOBJ pld) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_cuentasPldStd, new String[]{"id"});
			ps.setInt(1, Comun._IX(pld.getCuenta_id()));
			ps.setDouble(2, Comun._D(pld.getIngreso_mensual()));
			ps.setDouble(3, Comun._D(pld.getMonto_maximo_ahorro()));
			ps.setString(4, pld.getPuesto());
			ps.setInt(5, Comun._IX(pld.getEstatus_id()));
			ps.setBoolean(6, pld.getIndicador_Prov_Recursos() == null ? false : pld.getIndicador_Prov_Recursos());
			ps.setString(7, pld.getProv_recursos_id());
			ps.setDouble(8, Comun._D(pld.getMonto_maximo_ahorro_prov()));
			ps.setDouble(9, Comun._D(pld.getIngreso_mensual_prov()));
			ps.setString(10, pld.getPuesto_prov());
			ps.setInt(11, Comun._IX(pld.getRelacion_id()));
			ps.setInt(12, Comun._IX(pld.getUsuario_id()));
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_REGISTRO_CUENTAS_PLD, Errores.desc(Errores.ERROR_REGISTRO_CUENTAS_PLD));
			}else{
				respuesta.getBody().addValor("CUENTAS_PLD_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC crearPosicionGlobalStdDao(CuentaOBJ cta) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_posicionGlobalStd, new String[]{"id"});
			ps.setString(1, cta.getEstatusClave());
			ps.setString(2, cta.getProductoAhorro());
			ps.setString(3, cta.getCuenta());
			ps.setString(4, cta.getReferencia());
			ps.setDouble(5, 0.00d);
			ps.setDouble(6, 0.00d);
			ps.setString(7, cta.getPersonaId());
			ps.setString(8, cta.getClabeInterbancaria());

			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_REGISTRO_POSICION_GLOBAL, Errores.desc(Errores.ERROR_REGISTRO_POSICION_GLOBAL));
			}else{
				respuesta.getBody().addValor("CUENTAS_PLD_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}


	@SuppressWarnings("unused")
	public RespuestaSVC crearMatrizRiesgoPldStdDao(List<DatosMatrizRiesgoOBJ> lstMR, CuentaOBJ cuenta, Integer usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		Map<String, Map<String, String>> mapCon = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try {
			rows = jdbcTemplate.queryForList(READ_ConceptosStd, "PLD");
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					Map<String, String> map = new HashMap<>();
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
					}
					if(mapCon == null) mapCon = new HashMap<>();
					mapCon.put(map.get("CLAVE"), map);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}

		if(mapCon != null){
			try{
				for(DatosMatrizRiesgoOBJ mr : lstMR){
					Integer id_1 = null;  String  idClave_1 = null;
					Integer id_2 = null;  String  idClave_2 = null;
					
					if("ACT".equals(mr.getTipo())){
						id_1 = Comun._I(mapCon.get("PLD_ACTIVIDAD_ID").get("ID"));
						id_2 = Comun._I(mapCon.get("PLD_ACTIVIDAD_DESC").get("ID"));
					}

					if("GIR".equals(mr.getTipo())){
						id_1 = Comun._I(mapCon.get("PLD_GIRO_ID").get("ID"));
						id_2 = Comun._I(mapCon.get("PLD_GIRO_DESC").get("ID"));
					}

					if("DES".equals(mr.getTipo())){
						id_1 = Comun._I(mapCon.get("PLD_DESTINO_ID").get("ID"));
						id_2 = Comun._I(mapCon.get("PLD_DESTINO_DESC").get("ID"));
					}

					if("LOC".equals(mr.getTipo())){
						id_1 = Comun._I(mapCon.get("PLD_LOCALIDAD_ID").get("ID"));
						id_2 = Comun._I(mapCon.get("PLD_LOCALIDAD_DESC").get("ID"));
					}

					if("OCU".equals(mr.getTipo())){
						id_1 = Comun._I(mapCon.get("PLD_OCUPACION_ID").get("ID"));
						id_2 = Comun._I(mapCon.get("PLD_OCUPACION_DESC").get("ID"));
					}

					if(id_1 != null && id_2 != null){
						int total1 = jdbcTemplate.update(INSERT_ConceptosStd, cuenta.getId(), id_1, mr.getClave(), usuarioID);
						int total2 = jdbcTemplate.update(INSERT_ConceptosStd, cuenta.getId(), id_2, mr.getDescripcion(), usuarioID);
					}else{
						respuesta.getErrores().addCodigo(null, Errores.ERROR_REGISTRO_MATRIZ_PLD,  
								                               Errores.desc(Errores.ERROR_REGISTRO_MATRIZ_PLD, mr.getTipo() ));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
			}
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC crearCuentaStdDao(CuentaOBJ cuenta, Integer usuarioID) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_cuentaStd, new String[]{"id"});
			ps.setString(1, cuenta.getCuenta());
			ps.setDouble(2, cuenta.getEstatusId());
			ps.setString(3, cuenta.getPersonaId());
			ps.setInt(4, cuenta.getProductoAhorroId());
			ps.setDouble(5, cuenta.getMontoApertura());
			ps.setInt(6, cuenta.getSucursalId());
			ps.setDouble(7, cuenta.getRendimiento());
			ps.setDouble(8, cuenta.getMonedaId());
			ps.setDouble(9, cuenta.getGatNominal());
			ps.setDouble(10, cuenta.getGatReal());
			ps.setInt(11, cuenta.getAsesorId());
			ps.setInt(12, cuenta.getComoEnteroId());
			ps.setString(13, cuenta.getComoEnteroObs());
			ps.setString(14, cuenta.getClabeInterbancaria());
			ps.setString(15, cuenta.getReferencia());
			ps.setString(16, cuenta.getTipoCliente());
			ps.setInt(17, usuarioID);
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_CREAR_CUENTA, Errores.desc(Errores.ERROR_CREAR_CUENTA));
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
	
	public RespuestaSVC copiarByProductoID2CuentaIDDao(Integer cuentaID, Integer usuarioID, Integer productoID,	String estatus) {
		RespuestaSVC respuesta = new RespuestaSVC();
		// RespuestaSVC actualiza = updateEstatusDao(cuentaID, "BAJA", null);
		List<Integer> listaAhorro = new ArrayList<>();
		List<Integer> listaProducto = new ArrayList<>();
		List<Map<String, Object>> rows1 = null;
		List<Map<String, Object>> rows2 = null;
		int rowsuma = 0;
		int rows = 0;
		boolean valc = false;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			//Consulta ahorro.ahcuentas_ahorro_datos
			rows1 = jdbcTemplate.queryForList(conceptosAhorroStd, cuentaID);
			if (rows1 != null && !rows1.isEmpty()) {
				for (Map<String, Object> rowA : rows1) {
					Integer valorA = 0;

					valorA = ((Integer) (rowA.get("concepto_id")));

					if (listaAhorro == null)
						listaAhorro = new ArrayList<>();
					listaAhorro.add(valorA);
				}
			}
			//Consulta productos.prproductos_ahorro_datos
			rows2 = jdbcTemplate.queryForList(conceptosProductoStd, productoID);
			if (rows2 != null && !rows2.isEmpty()) {
				for (Map<String, Object> rowP : rows2) {
					Integer valorP = 0;

					valorP = ((Integer) (rowP.get("concepto_id")));

					if (listaProducto == null)
						listaProducto = new ArrayList<>();
					listaProducto.add(valorP);
				}
			}
			if (!listaProducto.isEmpty()) {
				for (Integer valProducto : listaProducto) {
					valc = false;
					for (Integer valAhorro : listaAhorro) {
						if (valProducto.intValue() == valAhorro.intValue()) {
							valc = true;
						}
					}
					if (!valc) {
						rowsuma = jdbcTemplate.update(copiarConceptosStd, cuentaID, usuarioID, productoID, estatus, valProducto);
						rows = rowsuma + rows;
					}
				}
			}
			
			if(rows<=0) {
				log.info("Registros de conceptos ya existentes");
			}else {
				log.info("Registros copiados correctamente de conceptos");
			}
		} catch (Exception ex) {
			log.error(String.format("Error: copiarByProductoID2CuentaIDDao::%d\n%s", cuentaID, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
					"Error al procesar : copiarByProductoID2CuentaIDDao");
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCuentaAhorroDao(String cuenta) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaCompletaOBJ cta = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_CuentaAhorroStd, cuenta);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(cta == null) cta = new CuentaCompletaOBJ();
					cta.setId(_T(row.get("cuenta_id")));
					cta.setCuenta(_T(row.get("cuenta")));
					cta.setReferencia(_T(row.get("referencia")));
					cta.setProducto_id(_T(row.get("producto_id")));
					cta.setCve_producto(_T(row.get("producto_clave")));
					cta.setProducto(_T(row.get("producto")));
					cta.setEstatus_id(_T(row.get("estatus_id")));
					cta.setEstatus(_T(row.get("estatus")));
					cta.setPersona_id(_T(row.get("persona_id")));
					cta.setFecha_apertura(_T(row.get("fecha_apertura")));
					cta.setMonto_apertura(_T(row.get("monto_apertura")));
					cta.setSucursal_id(_T(row.get("sucursal_id")));
					cta.setSucursal(_T(row.get("sucursal")));
					cta.setClabe(_T(row.get("clabe")));
				}
			}
			
			if(cta != null){
				respuesta.getBody().addValor("CUENTA", cta);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
			
			
		} catch (Exception ex) {
			log.error(String.format("Error: leerCuentaAhorroDao::%s\n%s", cuenta, ex.getMessage()));
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error al procesar : leerCuentaAhorroDao");
			
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC listCuentaProductoAhorroStdDao(String tipo, Integer productoAhorroId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<CuentaProductoOBJ> lstProd = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			rows = jdbcTemplate.queryForList(READ_ProductoAhorroEstatusStd, productoAhorroId, tipo, tipo );
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					CuentaProductoOBJ pr = new CuentaProductoOBJ();
					pr.setId(_T(row.get("ID")));
					pr.setCuenta(_T(row.get("CUENTA")));
					pr.setEstatus_id(_T(row.get("ESTATUS_ID")));
					pr.setEstatus_cve(_T(row.get("ESTATUS_CVE")));
					pr.setEstatus(_T(row.get("ESTATUS")));
					pr.setPersona_id(_T(row.get("PERSONA_ID")));
					pr.setProducto_ahorro_id(_T(row.get("PRODUCTO_AHORRO_ID")));
					pr.setProducto(_T(row.get("PRODUCTO")));
					pr.setFecha_apertura(_T(row.get("FECHA_APERTURA")));
					pr.setMonto_apertura(_T(row.get("MONTO_APERTURA")));
					pr.setClabe_interbancaria(_T(row.get("CLABE_INTERBANCARIA")));
					pr.setClabe_eje(_T(row.get("CLABE_EJE")));
					pr.setReferencia(_T(row.get("REFERENCIA")));
					pr.setBloqueado_id(_T(row.get("BLOQUEADO_ID")));
					pr.setBloqueado(_T(row.get("BLOQUEADO")));
					pr.setTipo_cliente(_T(row.get("TIPO_CLIENTE")));
					pr.setFecha_bloqueado(_T(row.get("FECHA_BLOQUEADO")));
					pr.setVal_exp(_T(row.get("VAL_EXP")));
					pr.setPermite_transacciones(_T(row.get("PERMITE_TRANSACCIONES")));
					pr.setTotal_expedientes(_T(row.get("TOTAL_EXPEDIENTES")));
					pr.setDias_para_transaccionar(_T(row.get("DIAS_PARA_TRANSACCIONAR")));
					pr.setDias_para_cancelar(_T(row.get("DIAS_PARA_CANCELAR")));
					
					pr.setDias_habiles(getDiasFestivosStd(_T(row.get("FECHA_APERTURA"))));
					pr.setNotificaciones(_T(row.get("NOTIFICACIONES")));
					
					SolicitanteOBJ sol = getNombreStd(_T(row.get("PERSONA_ID")));
					if(sol != null){
						pr.setNombre(sol.getNombre());
						pr.setCorreo(sol.getCorreo());
						pr.setTipo_persona(sol.getTipoPersona());
						pr.setCelular(sol.getCelular());
						pr.setCurp(sol.getCurp());
						pr.setRfc(sol.getRfc());
					}

					if(lstProd == null) lstProd = new ArrayList<>();
					lstProd.add(pr);
				}
			}
			
			if(lstProd != null){
				respuesta.getBody().addValor("CUENTA", lstProd);
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
	
	private SolicitanteOBJ getNombreStd(String numero){
		List<Map<String, Object>> rows = null;
		SolicitanteOBJ sol = null;

		try {
			rows = jdbcTemplatePr.queryForList(READ_nombreSolicitanteStd, numero);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(sol == null) sol = new SolicitanteOBJ();
					sol.setSolicitanteId(_T(row.get("NUMERO")));
					sol.setNombre(_T(row.get("NOMBRE")));
					sol.setCorreo(_T(row.get("CORREO")));
					sol.setTipoPersona(_T(row.get("T_PERSONA")));
					sol.setCelular(_T(row.get("CELULAR")));
					sol.setCurp(_T(row.get("CURP")));
					sol.setRfc(_T(row.get("RFC")));
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sol;
	}
	
	private String getDiasFestivosStd(String fechaIni){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> rows = null;
		String nombre = null;
		
		try {
			String fechaFin = sdf.format(Calendar.getInstance().getTime());
			rows = jdbcTemplatePr.queryForList(READ_diasFestivosStd, fechaIni, fechaFin, fechaIni, fechaIni, fechaIni, fechaIni, fechaFin);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					nombre = _T(row.get("DIAS_HABILES"));
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return nombre;
	}
	
	public RespuestaSVC crearRepresentanteStdDao(Integer cuentaId, String solicitanteId, String representanteId, Integer usuarioId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_representanteStd, new String[]{"id"});
			ps.setInt(1, cuentaId);
			ps.setString(2, solicitanteId);
			ps.setString(3, representanteId);
			ps.setInt(4, usuarioId);

			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_REPRESENTANTE_LEGAL, Errores.desc(Errores.ERROR_REPRESENTANTE_LEGAL));
			}else{
				respuesta.getBody().addValor("REPRESENTANTE_ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertaNotificacionesStdDao(Integer cuentaId, String nota, Integer usuarioId ) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		/* PRIMERO ACTUALIZA LOS ESTATUS DEL CONCEPTO Y CUENTA A 2 - INHABILITADO */
		try {
			int rows = jdbcTemplate.update(UPDATE_notificacionEstatusStd, "BAJA", cuentaId);
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		/* INSERTA LA NUEVA NOTIFICACION */
		try {
			int rows = jdbcTemplate.update(INSERT_notificacionStd, cuentaId, nota, usuarioId);
			if(rows < 1){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");
			}else{
				respuesta.getBody().addValor("NOTIFICACION", "OK");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		return respuesta;
	}

	public RespuestaSVC listNotificacionesStdDao(Integer cuentaId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<NotificacionOBJ> lstNota = null;

		try {
			rows = jdbcTemplate.queryForList(READ_notificacionesStd, cuentaId);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					NotificacionOBJ nota = new NotificacionOBJ(_T(row.get("FECHA")), _T(row.get("NOTIFICACION")));
					
					if(lstNota == null) lstNota = new ArrayList<>();
					lstNota.add(nota);
				}
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
			
			if(lstNota == null){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}else{
				respuesta.getBody().addValor("NOTIFICACION", lstNota);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	
	public RespuestaSVC cambiaEstadoCuentaStdDao(CambioEstadoCuentaReq req ) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplate.update(UPDATE_cambiaEstadoCuentaStd, req.getEstado(), Comun._I(req.getUsuarioId()), Comun._I(req.getCuentaId()));
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		return respuesta;
	}
	
	public RespuestaSVC validarCuentaExpedienteStdDao(ValidarCuentaExpedienteReq req ) {
		RespuestaSVC respuesta = new RespuestaSVC();
		
		try {
			int rows = jdbcTemplate.update(UPDATE_validarCuentaExpedienteStd, 
					                       req.getEstado(),
					                       req.getValidar(),
					                       Comun._I(req.getUsuarioId()), 
					                       Comun._I(req.getCuentaId()));
			if(rows < 1)  respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN DATOS ACTUALIZADOS");

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
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

	public String getComplementarioCoDiStd() {
		return complementarioCoDiStd;
	}

	public void setComplementarioCoDiStd(String complementarioCoDiStd) {
		this.complementarioCoDiStd = complementarioCoDiStd;
	}

	public String getParticipanteSpeiTerceroStd() {
		return participanteSpeiTerceroStd;
	}

	public void setParticipanteSpeiTerceroStd(String participanteSpeiTerceroStd) {
		this.participanteSpeiTerceroStd = participanteSpeiTerceroStd;
	}

	public String getEstatusAhorroStd() {
		return estatusAhorroStd;
	}

	public void setEstatusAhorroStd(String estatusAhorroStd) {
		this.estatusAhorroStd = estatusAhorroStd;
	}

	public String getVerificaEjeAhorroStd() {
		return verificaEjeAhorroStd;
	}

	public void setVerificaEjeAhorroStd(String verificaEjeAhorroStd) {
		this.verificaEjeAhorroStd = verificaEjeAhorroStd;
	}

	public String getActualizaEjeAhorroStd() {
		return actualizaEjeAhorroStd;
	}

	public void setActualizaEjeAhorroStd(String actualizaEjeAhorroStd) {
		this.actualizaEjeAhorroStd = actualizaEjeAhorroStd;
	}

	public String getINSERT_posicionGlobalStd() {
		return INSERT_posicionGlobalStd;
	}

	public void setINSERT_posicionGlobalStd(String iNSERT_posicionGlobalStd) {
		INSERT_posicionGlobalStd = iNSERT_posicionGlobalStd;
	}

	public String getINSERT_cuentasPldStd() {
		return INSERT_cuentasPldStd;
	}

	public void setINSERT_cuentasPldStd(String iNSERT_cuentasPldStd) {
		INSERT_cuentasPldStd = iNSERT_cuentasPldStd;
	}

	public String getREAD_ConceptosStd() {
		return READ_ConceptosStd;
	}

	public void setREAD_ConceptosStd(String rEAD_ConceptosStd) {
		READ_ConceptosStd = rEAD_ConceptosStd;
	}

	public String getINSERT_ConceptosStd() {
		return INSERT_ConceptosStd;
	}

	public void setINSERT_ConceptosStd(String iNSERT_ConceptosStd) {
		INSERT_ConceptosStd = iNSERT_ConceptosStd;
	}

	public String getINSERT_cuentaStd() {
		return INSERT_cuentaStd;
	}

	public void setINSERT_cuentaStd(String iNSERT_cuentaStd) {
		INSERT_cuentaStd = iNSERT_cuentaStd;
	}

	public String getConceptosAhorroStd() {
		return conceptosAhorroStd;
	}

	public void setConceptosAhorroStd(String conceptosAhorroStd) {
		this.conceptosAhorroStd = conceptosAhorroStd;
	}

	public String getConceptosProductoStd() {
		return conceptosProductoStd;
	}

	public void setConceptosProductoStd(String conceptosProductoStd) {
		this.conceptosProductoStd = conceptosProductoStd;
	}

	public String getCopiarConceptosStd() {
		return copiarConceptosStd;
	}

	public void setCopiarConceptosStd(String copiarConceptosStd) {
		this.copiarConceptosStd = copiarConceptosStd;
	}

	public String getREAD_CuentaAhorroStd() {
		return READ_CuentaAhorroStd;
	}

	public void setREAD_CuentaAhorroStd(String rEAD_CuentaAhorroStd) {
		READ_CuentaAhorroStd = rEAD_CuentaAhorroStd;
	}

	public String getREAD_ProductoAhorroEstatusStd() {
		return READ_ProductoAhorroEstatusStd;
	}

	public void setREAD_ProductoAhorroEstatusStd(String rEAD_ProductoAhorroEstatusStd) {
		READ_ProductoAhorroEstatusStd = rEAD_ProductoAhorroEstatusStd;
	}

	public String getREAD_nombreSolicitanteStd() {
		return READ_nombreSolicitanteStd;
	}

	public void setREAD_nombreSolicitanteStd(String rEAD_nombreSolicitanteStd) {
		READ_nombreSolicitanteStd = rEAD_nombreSolicitanteStd;
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public String getREAD_diasFestivosStd() {
		return READ_diasFestivosStd;
	}

	public void setREAD_diasFestivosStd(String rEAD_diasFestivosStd) {
		READ_diasFestivosStd = rEAD_diasFestivosStd;
	}

	public String getINSERT_representanteStd() {
		return INSERT_representanteStd;
	}

	public void setINSERT_representanteStd(String iNSERT_representanteStd) {
		INSERT_representanteStd = iNSERT_representanteStd;
	}

	public String getINSERT_notificacionStd() {
		return INSERT_notificacionStd;
	}

	public void setINSERT_notificacionStd(String iNSERT_notificacionStd) {
		INSERT_notificacionStd = iNSERT_notificacionStd;
	}

	public String getUPDATE_notificacionEstatusStd() {
		return UPDATE_notificacionEstatusStd;
	}

	public void setUPDATE_notificacionEstatusStd(String uPDATE_notificacionEstatusStd) {
		UPDATE_notificacionEstatusStd = uPDATE_notificacionEstatusStd;
	}

	public String getREAD_notificacionesStd() {
		return READ_notificacionesStd;
	}

	public void setREAD_notificacionesStd(String rEAD_notificacionesStd) {
		READ_notificacionesStd = rEAD_notificacionesStd;
	}

	public String getUPDATE_cambiaEstadoCuentaStd() {
		return UPDATE_cambiaEstadoCuentaStd;
	}

	public void setUPDATE_cambiaEstadoCuentaStd(String uPDATE_cambiaEstadoCuentaStd) {
		UPDATE_cambiaEstadoCuentaStd = uPDATE_cambiaEstadoCuentaStd;
	}

	public String getUPDATE_validarCuentaExpedienteStd() {
		return UPDATE_validarCuentaExpedienteStd;
	}

	public void setUPDATE_validarCuentaExpedienteStd(String uPDATE_validarCuentaExpedienteStd) {
		UPDATE_validarCuentaExpedienteStd = uPDATE_validarCuentaExpedienteStd;
	}

	public String getREAD_participanteSpeiStd() {
		return READ_participanteSpeiStd;
	}

	public void setREAD_participanteSpeiStd(String rEAD_participanteSpeiStd) {
		READ_participanteSpeiStd = rEAD_participanteSpeiStd;
	}

	public String getComplementarioCoDiReferenciadaStd() {
		return complementarioCoDiReferenciadaStd;
	}

	public void setComplementarioCoDiReferenciadaStd(String complementarioCoDiReferenciadaStd) {
		this.complementarioCoDiReferenciadaStd = complementarioCoDiReferenciadaStd;
	}
}

