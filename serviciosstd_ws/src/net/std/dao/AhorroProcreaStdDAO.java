package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.std.constantes.Comun;
import net.std.data.CuentaOBJ;

public class AhorroProcreaStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbcTemplatePr;
	private String leerCuentaAhorroClabeStdPr;
	private String leerSaldoDisponibleStdPr;
	private String cajaDisposicionAhorroStdPr;
	private String cajaDepositoAhorroStdPr;
	private String speiDevolucionAhorroStdPr;
	private String speiClabeObtenBanco;
	
	public RespuestaSVC ahorroClabePrStdDao(String clabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplatePr.queryForList(leerCuentaAhorroClabeStdPr, clabe);
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
	
	
	public RespuestaSVC leerCuentaAhorroClabePrStdDao(String clabe) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		CuentaOBJ cta = null;

		try {
			rows = jdbcTemplatePr.queryForList(leerCuentaAhorroClabeStdPr, clabe);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(cta == null) cta = new CuentaOBJ();
					cta.setCuenta(Comun._T(row.get("CUENTA")));
					cta.setReferencia(Comun._T(row.get("REFERENCIA")));
					cta.setClabeInterbancaria(Comun._T(row.get("CUENTA_CLABE")));
					cta.setEstatus(Comun._T(row.get("ESTATUS")));
					
					cta.setProductoAhorroId(Comun._I(row.get("PRODUCTO_ID")));
					cta.setPersonaId(Comun._T(row.get("NUMERO")));
					cta.setStrFechaApertura(Comun._T(row.get("FECHA_INICIO")));
					//cta.setSucursalId(Comun._I(row.get("SUCURSAL")));
					cta.setBloqueado(Comun._T(row.get("BLOQUEADO")));
					cta.setBase(Comun._T(row.get("BASE")));
				}
			}
			
			if(cta != null){
				respuesta.getBody().addValor("CUENTA", cta);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "SIN REGISTROS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
		}
		
		return respuesta;
	}
	
	
	public RespuestaSVC ahorroSaldoDisponiblePrStdDao(String cuenta) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplatePr.queryForList(leerSaldoDisponibleStdPr, cuenta, cuenta);
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
	
	public RespuestaSVC ahorroRetiroPrStdDao(String cuentaOri, String cuentaDes, String fecha, Double monto, Long usuarioID, Long movimientoID, String observaciones) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplatePr.queryForList(cajaDisposicionAhorroStdPr, cuentaOri, cuentaDes, fecha, monto, usuarioID, movimientoID, observaciones);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(!("".equals(_T(row.get("VIDMOV2"))) || "0".equals(_T(row.get("VIDMOV2"))))){
						Map<String, String> map = new HashMap<>();
						for (Map.Entry<String, Object> entry : row.entrySet()) {
							map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
						}
						if(dato == null) dato = new ArrayList<>();
						dato.add(map);
					}
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "NO SE REGISTRO EL RETIRO");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC ahorroDepositoPrStdDao(String cuentaOri, String cuentaDes, String fecha, Double monto, Long usuarioID, Long movimientoID, String observaciones) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		try {
			rows = jdbcTemplatePr.queryForList(cajaDepositoAhorroStdPr,cuentaOri, cuentaDes, fecha, monto, usuarioID, movimientoID, observaciones);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(!("".equals(_T(row.get("VIDMOV2"))) || "0".equals(_T(row.get("VIDMOV2"))))){
						Map<String, String> map = new HashMap<>();
						for (Map.Entry<String, Object> entry : row.entrySet()) {
							map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
						}
						if(dato == null) dato = new ArrayList<>();
						dato.add(map);
					}
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "NO SE REGISTRO EL DEPOSITO");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public RespuestaSVC speiDevolucionAhorroPrStdDao(String cuentaOri, String cuentaDes, String fecha, Double monto, Long usuarioID, 
			                                         Long movimientoID, String observaciones, Integer tipoMovto) {
		RespuestaSVC respuesta = new RespuestaSVC();
		List<Map<String, Object>> rows = null;
		List<Map<String, String >> dato = null;

		System.out.println("cuentaOri : " + cuentaOri);
		System.out.println("cuentaDes : " + cuentaDes);
		System.out.println("fecha : " + fecha);
		System.out.println("monto : " + monto);
		System.out.println("usuarioID : " + usuarioID);
		System.out.println("movimientoID : " + movimientoID);
		System.out.println("observaciones : " + observaciones);
		System.out.println("tipoMovto : " + tipoMovto);
		
		try {
			rows = jdbcTemplatePr.queryForList(speiDevolucionAhorroStdPr,cuentaOri, cuentaDes, fecha, monto, usuarioID, movimientoID, observaciones, tipoMovto);
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					if(!("".equals(_T(row.get("VIDMOV2"))) || "0".equals(_T(row.get("VIDMOV2"))))){
						Map<String, String> map = new HashMap<>();
						for (Map.Entry<String, Object> entry : row.entrySet()) {
							map.put(_T(entry.getKey()).toUpperCase(), _T(entry.getValue()));
						}
						if(dato == null) dato = new ArrayList<>();
						dato.add(map);
					}
				}
			}
			
			if(dato != null){
				respuesta.getBody().addValor("CUENTA", dato);
			}else{
				respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, "NO SE REGISTRO EL DEPOSITO");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta.getErrores().addCodigo(null, Errores.ERROR_INESPERADO, ex.getMessage());
		}
		return respuesta;
	}
	
	public Map<String, Object> speiClabeObtenBanco(String clabe) {
		List<Map<String, Object>> rows = null;

		try {
			rows = jdbcTemplatePr.queryForList(speiClabeObtenBanco, clabe);
			if(rows != null && !rows.isEmpty()){
				return rows.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
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

	public String getLeerCuentaAhorroClabeStdPr() {
		return leerCuentaAhorroClabeStdPr;
	}

	public void setLeerCuentaAhorroClabeStdPr(String leerCuentaAhorroClabeStdPr) {
		this.leerCuentaAhorroClabeStdPr = leerCuentaAhorroClabeStdPr;
	}

	public String getLeerSaldoDisponibleStdPr() {
		return leerSaldoDisponibleStdPr;
	}

	public void setLeerSaldoDisponibleStdPr(String leerSaldoDisponibleStdPr) {
		this.leerSaldoDisponibleStdPr = leerSaldoDisponibleStdPr;
	}

	public String getCajaDisposicionAhorroStdPr() {
		return cajaDisposicionAhorroStdPr;
	}

	public void setCajaDisposicionAhorroStdPr(String cajaDisposicionAhorroStdPr) {
		this.cajaDisposicionAhorroStdPr = cajaDisposicionAhorroStdPr;
	}

	public String getCajaDepositoAhorroStdPr() {
		return cajaDepositoAhorroStdPr;
	}

	public void setCajaDepositoAhorroStdPr(String cajaDepositoAhorroStdPr) {
		this.cajaDepositoAhorroStdPr = cajaDepositoAhorroStdPr;
	}


	public String getSpeiDevolucionAhorroStdPr() {
		return speiDevolucionAhorroStdPr;
	}


	public void setSpeiDevolucionAhorroStdPr(String speiDevolucionAhorroStdPr) {
		this.speiDevolucionAhorroStdPr = speiDevolucionAhorroStdPr;
	}


	public String getSpeiClabeObtenBanco() {
		return speiClabeObtenBanco;
	}


	public void setSpeiClabeObtenBanco(String speiClabeObtenBanco) {
		this.speiClabeObtenBanco = speiClabeObtenBanco;
	}
}

