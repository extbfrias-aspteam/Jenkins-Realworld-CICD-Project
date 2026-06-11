package net.std.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.data.CuentaOBJ;
import net.std.request.CanalesReq;
import net.std.request.TraficoReq;
import net.cero.ws.data.HeaderWS;


public class TransaccionesStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(TransaccionesStdDAO.class);

	private JdbcTemplate jdbcTemplate;
	private String OPBuscarSaldosCuentaStd;
	private String OPActualizarSaldoCuentaStd;
	private String OPGuardarSaldoCuentaStd;
	private String OPSaldosCuentaStd;
	private String INSERT_TransaccionStd;
	private String INSERT_TransaccionSpeiStd;
	private String DELETE_TransaccionStd;
	private String READ_SaldosCuentaStd;
	private String READ_SaldosCuentaStdByFecha;
	private String INSERT_TraficoStd;
	private String READ_validarDevolucion;
	private String UPDATE_IdSPei_Transacciones;
	private String INSERTA_TRANSACCION_SALDO;
	
	private String READ_CanalesStd;
	private String INSERT_CanalesStd;
	private String UPDATE_CanalesStd;
	private String validaCanal;

	private String validaCanalCuentas;

	public Boolean validarDevolucion(String clave_rastreo, BigDecimal monto){
		List<Map<String, Object>> rows = null;
		Boolean respuesta = true;
		Integer id = -1;
		try {
			rows = jdbcTemplate.queryForList(READ_validarDevolucion, clave_rastreo,monto);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					id = (Comun._I(row.get("id")));
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta = false;
		}
		if(id > -1) respuesta = false;
		return respuesta;
	}
	
	public RespuestaSVC leerSaldoCuentaStdDao(Integer cuentaId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		

		try {
			BigDecimal saldo = BigDecimal.ZERO;
			rows = jdbcTemplate.queryForList(OPSaldosCuentaStd, cuentaId);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					saldo = saldo.add(Comun._BD(row.get("saldo")));
					break;
				}
			}else{
				saldo = BigDecimal.ZERO;
			}
			
			//respuesta.getBody().addValor("ID", Comun._I(row.get("id")));
			respuesta.getBody().addValor("SALDO", saldo);
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC depositarStdDao(CuentaOBJ cta, String tipoTransaccion, Date fecha, BigDecimal monto, String observaciones, String autorizacion, HeaderWS header, String claveRastreo) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
	        ZoneId cdmxTimeZone = ZoneId.of("America/Mexico_City");
	        LocalDateTime nowCdmx = LocalDateTime.now(cdmxTimeZone);
	        Timestamp currentTimeFromDB = Timestamp.valueOf(nowCdmx);
	        
			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_TransaccionStd, new String[]{"id"});
			ps.setInt(1, cta.getId());
			ps.setBigDecimal(2, monto);
			ps.setTimestamp(3, currentTimeFromDB);
			ps.setString(4, observaciones);
			ps.setString(5, autorizacion);
			ps.setString(6, tipoTransaccion);
			ps.setString(7, tipoTransaccion);
			ps.setLong(8, header.getIdUsuario());
			ps.setLong(9, header.getIdSucursal());
			ps.setLong(10, header.getIdCanalAtencion());
			ps.setLong(11, Long.parseLong(cta.getPersonaId()));
			ps.setString(12, header.getIpHost());
			ps.setLong(13, header.getIdUsuario());
			ps.setTimestamp(14, currentTimeFromDB);
			ps.setString(15, claveRastreo);
			ps.setString(16, cta.getCuenta_referencia());
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO, Errores.desc(Errores.ERROR_DEPOSITO, cta.getCuenta()));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	public RespuestaSVC depositarSpeiStdDao(CuentaOBJ cta, String tipoTransaccion, Date fecha, BigDecimal monto, String observaciones, 
			String autorizacion, HeaderWS header, String claveRastreo, Long idSpeiIncoming) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
	        ZoneId cdmxTimeZone = ZoneId.of("America/Mexico_City");
	        LocalDateTime nowCdmx = LocalDateTime.now(cdmxTimeZone);
	        Timestamp currentTimeFromDB = Timestamp.valueOf(nowCdmx);

			jdbcTemplate.update(connection -> { 
				PreparedStatement ps = connection.prepareStatement(INSERT_TransaccionSpeiStd, new String[]{"id"});
				ps.setInt(1, cta.getId());
				ps.setBigDecimal(2, monto);
				ps.setTimestamp(3, currentTimeFromDB);
				ps.setString(4, observaciones);
				ps.setString(5, autorizacion);
				ps.setString(6, tipoTransaccion);
				ps.setString(7, tipoTransaccion);
				ps.setLong(8, header.getIdUsuario());
				ps.setLong(9, header.getIdSucursal());
				ps.setLong(10, header.getIdCanalAtencion());
				ps.setLong(11, Long.parseLong(cta.getPersonaId()));
				ps.setString(12, header.getIpHost());
				ps.setLong(13, header.getIdUsuario());
				ps.setTimestamp(14, currentTimeFromDB);
				ps.setString(15, claveRastreo);
				ps.setString(16, cta.getCuenta_referencia());
				ps.setLong(17, idSpeiIncoming);
			
				return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO, Errores.desc(Errores.ERROR_DEPOSITO, cta.getCuenta()));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public RespuestaSVC retirarStdDao(CuentaOBJ cta, String tipoTransaccion, Date fecha, BigDecimal monto, String observaciones, String autorizacion, HeaderWS header, String claveRastreo) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
	        ZoneId cdmxTimeZone = ZoneId.of("America/Mexico_City");
	        LocalDateTime nowCdmx = LocalDateTime.now(cdmxTimeZone);
	        Timestamp currentTimeFromDB = Timestamp.valueOf(nowCdmx);
	        
			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_TransaccionStd, new String[]{"id"});
			ps.setInt(1, cta.getId());
			ps.setBigDecimal(2, monto);
			ps.setTimestamp(3, currentTimeFromDB);
			ps.setString(4, observaciones);
			ps.setString(5, autorizacion);
			ps.setString(6, tipoTransaccion);
			ps.setString(7, tipoTransaccion);
			ps.setLong(8, header.getIdUsuario());
			ps.setLong(9, header.getIdSucursal());
			ps.setLong(10, header.getIdCanalAtencion());
			ps.setLong(11, Long.parseLong(cta.getPersonaId()));
			ps.setString(12, header.getIpHost());
			ps.setLong(13, header.getIdUsuario());
			ps.setTimestamp(14, currentTimeFromDB);
			ps.setString(15, claveRastreo);
			ps.setString(16, cta.getCuenta_referencia());
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_RETIRO, Errores.desc(Errores.ERROR_RETIRO, cta.getCuenta()));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC actualizarIdSpei(Long id_spei, String id_transacciones) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_IdSPei_Transacciones, id_spei,id_transacciones);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO,  Errores.desc(Errores.ERROR_INESPERADO, id_spei));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_ROLLBACK, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	
	public RespuestaSVC rollBackStdDao(String autorizacion) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(DELETE_TransaccionStd, autorizacion);
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ROLLBACK,  Errores.desc(Errores.ERROR_ROLLBACK, autorizacion));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_ROLLBACK, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	@SuppressWarnings("unused")
	public RespuestaSVC actualizaSaldoStdDao(CuentaOBJ cta, Integer calculaSaldo, String movto, Integer usuarioId) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));

		try {
			BigDecimal saldoCalculado = BigDecimal.ZERO;
			
			/* FORZA A CALCULAR EL SALDO */
			if(calculaSaldo.intValue() ==  1){ 
				try{
					rows = jdbcTemplate.queryForList(READ_SaldosCuentaStd, cta.getId());
					if(rows != null && !rows.isEmpty()){
						for (Map<String, Object> row : rows) {
							saldoCalculado = saldoCalculado.add(Comun._BD(row.get("saldo")));
							break;
						}
					}else{
						saldoCalculado = BigDecimal.ZERO;
					}
				}catch(Exception ex){
					saldoCalculado = BigDecimal.ZERO;
				}
			}
			
			Integer saldoId = null;
			BigDecimal saldo = BigDecimal.ZERO;
		  	rows = jdbcTemplate.queryForList(OPSaldosCuentaStd, cta.getId());
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					saldo = saldo.add(Comun._BD(row.get("saldo")));
					saldoId = Comun._I(row.get("id"));
					break;
				}
			}else{
				saldoId = null;
				saldo = BigDecimal.ZERO;
			}
			
			BigDecimal saldoActual = BigDecimal.ZERO;
			if(calculaSaldo.intValue() ==  1){ 
				saldoActual = saldoActual.add(saldoCalculado);
			}else{
				if("DEPOSITO".equals(movto)){
					saldoActual = saldoActual.add(saldo);
				}else{
					saldoActual = saldoActual.subtract(saldo);
				}
			}
			
			if(saldoId == null) {
				int rowsUpd  = jdbcTemplate.update(OPGuardarSaldoCuentaStd, cta.getId(), saldoActual, usuarioId);
			}else{
				int rowsUpd = jdbcTemplate.update(OPActualizarSaldoCuentaStd, saldoActual, usuarioId, saldoId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_SALDO_NO_ENCONTRADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	@SuppressWarnings("unused")
	public BigDecimal leerSaldoByFechaStdDao(Integer id, String fecha) {
		List<Map<String, Object>> rows = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		BigDecimal saldoCalculado = BigDecimal.ZERO;
		try{
			rows = jdbcTemplate.queryForList(READ_SaldosCuentaStdByFecha, id, fecha );
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					saldoCalculado = saldoCalculado.add(Comun._BD(row.get("SALDO_FECHA")));
					break;
				}
			}else{
				saldoCalculado = BigDecimal.ZERO;
			}
		}catch(Exception ex){
			saldoCalculado = BigDecimal.ZERO;
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return saldoCalculado;
	}

	public RespuestaSVC traficoStdDao(TraficoReq req) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_TraficoStd, new String[]{"id"});
			ps.setString(1, req.getProceso());
			ps.setString(2, req.getCuentaOrd());
			ps.setString(3, req.getCuentaDes());
			ps.setString(4, req.getClaveRastreo());
			ps.setDouble(5, req.getMonto() == null ? null : req.getMonto().doubleValue());
			ps.setString(6, req.getConcepto());
			ps.setInt(7, Comun._I(Constantes.USUARIO_ID));
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_TRAFICO, Errores.desc(Errores.ERROR_TRAFICO, req.getClaveRastreo()));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC insertarCanalStdDao(CanalesReq req) {
		RespuestaSVC respuesta = new RespuestaSVC();
		KeyHolder keyHolder = new GeneratedKeyHolder();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {

			jdbcTemplate.update(connection -> { PreparedStatement ps = connection.prepareStatement(INSERT_CanalesStd, new String[]{"id"});
			ps.setString(1, req.getAplicativo_clave());
			ps.setString(2, req.getTransaccion_clave());
			ps.setString(3, req.getCuenta());
			ps.setBoolean(4, req.getStatus());
			ps.setInt(6, req.getUsuario_id());
			
			return ps;
			}, keyHolder);

			Number keyId = keyHolder.getKey();
			if(keyId.longValue() == 0L){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_TRAFICO, Errores.desc(Errores.ERROR_TRAFICO, new Gson().toJson(req.getCuenta())));
			}else{
				respuesta.getBody().addValor("ID", keyId.longValue());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}
	
	public RespuestaSVC leerCanalStdDao(String cuenta, String aplicativo, String transaccion) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CanalesReq req = null;
		
		try {
			rows = jdbcTemplate.queryForList(READ_CanalesStd, cuenta, aplicativo, transaccion);
			if(rows != null && !rows.isEmpty()){
				req = new CanalesReq();
				for (Map<String, Object> row : rows) {
					req.setId(Comun._I(row.get("ID")));
					req.setAplicativo_clave(Comun._T(row.get("APLICATIVO_CLAVE")));
					req.setTransaccion_clave(Comun._T(row.get("TRANSACCION_CUENTA")));
					req.setCuenta(Comun._T(row.get("CUENTA")));
					req.setStatus(Comun._B(row.get("ESTATUS")));
					break;
				}
				respuesta.getBody().addValor("CANAL", req);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_LEER_CANAL, Errores.desc(Errores.ERROR_LEER_CANAL, cuenta));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC actualizarCanalStdDao(CanalesReq req) {
		RespuestaSVC respuesta = new RespuestaSVC();

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try {
			int row = jdbcTemplate.update(UPDATE_CanalesStd, 
					                      req.getStatus(),
					                      req.getUsuario_id(),
					                      req.getCuenta(),
					                      req.getAplicativo_clave(),
					                      req.getTransaccion_clave());
			if(row == 0){
				respuesta.getErrores().addCodigo(null, Errores.ERROR_ACTUALIZAR_CANAL, Errores.desc(Errores.ERROR_ACTUALIZAR_CANAL, req.getCuenta()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta;
	}

	public String validaCanales(String canal,String transaccion,String producto)
	{
		String respuesta="";
		try{
			List<Map<String,Object>> rows=jdbcTemplate.queryForList(validaCanal,canal,transaccion,producto);

			for(Map<String,Object> row:rows){
				respuesta=((String)row.get("res"));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respuesta;
	}
	public boolean validaCanalCuenta(String canal,String transaccion,String cuenta)
	{
		boolean respuesta=true;
		try{
			List<Map<String,Object>> rows=jdbcTemplate.queryForList(validaCanalCuentas,canal,transaccion,cuenta);

			for(Map<String,Object> row:rows){
				respuesta=((Boolean)row.get("estatus"));
			}

			System.out.println(respuesta);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respuesta;
	}
	
	public RespuestaSVC insertaTransaccionSaldo(CuentaOBJ cta, String tipoTransaccion, BigDecimal monto, String observaciones, 
			String autorizacion, HeaderWS header, String claveRastreo, Long idSpei, int movimiento) {
		// movimiento = 0 RETIRO (RESTA); 1 DEPOSITO (SUMA);
		RespuestaSVC respuesta = new RespuestaSVC();
	    List<Map<String, Object>> lista = null;
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
	    Connection connection = null;
		try {
	        connection = jdbcTemplate.getDataSource().getConnection();
			lista = jdbcTemplate.queryForList(INSERTA_TRANSACCION_SALDO,cta.getId(), monto, observaciones, autorizacion, tipoTransaccion, header.getIdUsuario(),
			header.getIdSucursal(),header.getIdCanalAtencion(), Long.parseLong(cta.getPersonaId()), header.getIpHost(), claveRastreo, cta.getCuenta_referencia(),
			idSpei, movimiento);

			log.info(lista);
			if(lista != null && !lista.isEmpty()) {
				if(Comun._I(lista.get(0).get("id_result")) == 0)
					respuesta.getBody().addValor("ID", lista.get(0).get("id_trx_result"));
				else {
					if(movimiento == 0)
						respuesta.getErrores().addCodigo(null, Errores.ERROR_RETIRO, Errores.desc(Errores.ERROR_RETIRO, cta.getCuenta()));
					else 
						respuesta.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO, Errores.desc(Errores.ERROR_DEPOSITO, cta.getCuenta()));
				}
			}else{
				if(movimiento == 0)
					respuesta.getErrores().addCodigo(null, Errores.ERROR_RETIRO, Errores.desc(Errores.ERROR_RETIRO, cta.getCuenta()));
				else 
					respuesta.getErrores().addCodigo(null, Errores.ERROR_DEPOSITO, Errores.desc(Errores.ERROR_DEPOSITO, cta.getCuenta()));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO);
		} finally {
	        try {
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuesta; 
	}

	@SuppressWarnings("unused")
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	public String getOPBuscarSaldosCuentaStd() {
		return OPBuscarSaldosCuentaStd;
	}


	public void setOPBuscarSaldosCuentaStd(String oPBuscarSaldosCuentaStd) {
		OPBuscarSaldosCuentaStd = oPBuscarSaldosCuentaStd;
	}


	public String getOPActualizarSaldoCuentaStd() {
		return OPActualizarSaldoCuentaStd;
	}


	public void setOPActualizarSaldoCuentaStd(String oPActualizarSaldoCuentaStd) {
		OPActualizarSaldoCuentaStd = oPActualizarSaldoCuentaStd;
	}


	public String getOPGuardarSaldoCuentaStd() {
		return OPGuardarSaldoCuentaStd;
	}


	public void setOPGuardarSaldoCuentaStd(String oPGuardarSaldoCuentaStd) {
		OPGuardarSaldoCuentaStd = oPGuardarSaldoCuentaStd;
	}


	public String getOPSaldosCuentaStd() {
		return OPSaldosCuentaStd;
	}


	public void setOPSaldosCuentaStd(String oPSaldosCuentaStd) {
		OPSaldosCuentaStd = oPSaldosCuentaStd;
	}

	public String getINSERT_TransaccionStd() {
		return INSERT_TransaccionStd;
	}

	public void setINSERT_TransaccionStd(String iNSERT_TransaccionStd) {
		INSERT_TransaccionStd = iNSERT_TransaccionStd;
	}

	public String getDELETE_TransaccionStd() {
		return DELETE_TransaccionStd;
	}

	public void setDELETE_TransaccionStd(String dELETE_TransaccionStd) {
		DELETE_TransaccionStd = dELETE_TransaccionStd;
	}

	public String getREAD_SaldosCuentaStd() {
		return READ_SaldosCuentaStd;
	}

	public void setREAD_SaldosCuentaStd(String rEAD_SaldosCuentaStd) {
		READ_SaldosCuentaStd = rEAD_SaldosCuentaStd;
	}

	public String getINSERT_TraficoStd() {
		return INSERT_TraficoStd;
	}

	public void setINSERT_TraficoStd(String iNSERT_TraficoStd) {
		INSERT_TraficoStd = iNSERT_TraficoStd;
	}

	public String getREAD_CanalesStd() {
		return READ_CanalesStd;
	}

	public void setREAD_CanalesStd(String rEAD_CanalesStd) {
		READ_CanalesStd = rEAD_CanalesStd;
	}

	public String getINSERT_CanalesStd() {
		return INSERT_CanalesStd;
	}

	public void setINSERT_CanalesStd(String iNSERT_CanalesStd) {
		INSERT_CanalesStd = iNSERT_CanalesStd;
	}

	public String getUPDATE_CanalesStd() {
		return UPDATE_CanalesStd;
	}

	public void setUPDATE_CanalesStd(String uPDATE_CanalesStd) {
		UPDATE_CanalesStd = uPDATE_CanalesStd;
	}

	public String getREAD_SaldosCuentaStdByFecha() {
		return READ_SaldosCuentaStdByFecha;
	}

	public void setREAD_SaldosCuentaStdByFecha(String rEAD_SaldosCuentaStdByFecha) {
		READ_SaldosCuentaStdByFecha = rEAD_SaldosCuentaStdByFecha;
	}

	public String getINSERT_TransaccionSpeiStd() {
		return INSERT_TransaccionSpeiStd;
	}

	public void setINSERT_TransaccionSpeiStd(String iNSERT_TransaccionSpeiStd) {
		INSERT_TransaccionSpeiStd = iNSERT_TransaccionSpeiStd;
	}

	public String getREAD_validarDevolucion() {
		return READ_validarDevolucion;
	}

	public void setREAD_validarDevolucion(String rEAD_validarDevolucion) {
		READ_validarDevolucion = rEAD_validarDevolucion;
	}

	public String getUPDATE_IdSPei_Transacciones() {
		return UPDATE_IdSPei_Transacciones;
	}

	public void setUPDATE_IdSPei_Transacciones(String uPDATE_IdSPei_Transacciones) {
		UPDATE_IdSPei_Transacciones = uPDATE_IdSPei_Transacciones;
	}

	public String getValidaCanal() {
		return validaCanal;
	}

	public void setValidaCanal(String validaCanal) {
		this.validaCanal = validaCanal;
	}

	public String getValidaCanalCuentas() {
		return validaCanalCuentas;
	}

	public void setValidaCanalCuentas(String validaCanalCuentas) {
		this.validaCanalCuentas = validaCanalCuentas;
	}

	public String getINSERTA_TRANSACCION_SALDO() {
		return INSERTA_TRANSACCION_SALDO;
	}

	public void setINSERTA_TRANSACCION_SALDO(String iNSERTA_TRANSACCION_SALDO) {
		INSERTA_TRANSACCION_SALDO = iNSERTA_TRANSACCION_SALDO;
	}

}
