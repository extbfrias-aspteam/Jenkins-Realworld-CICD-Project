package net.std.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import net.cero.ws.data.RespuestaSVC;
import net.std.catalogos.svc.CatStdActividadSvc;
import net.std.constantes.Comun;
import net.std.constantes.Errores;


@SuppressWarnings("unused")
public class SpeiStdDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(SpeiStdDAO.class);
	
	private JdbcTemplate jdbcTemplateSti;
	private String READ_IncomingDatosCuentaStd;
	private String READ_OutgoingDatosCuentaStd;
	private String ObtenerTipoPago;
	
	public RespuestaSVC consultaIncomingPorCveRastreoStdDao(Map<String, String> mapReq) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		try {
			rows = jdbcTemplateSti.queryForList(READ_IncomingDatosCuentaStd, Comun._T(mapReq.get("claveRastreo")), 
																			 Comun._T(mapReq.get("cuentaOrdenante")), 
																			 Comun._T(mapReq.get("cuentaBeneficiario")),
																			 Comun._BD(mapReq.get("monto")));
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuestaSvc.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
					break;
				}
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "SIN DATOS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", Comun._T(mapReq.get("claveRastreo")), ex.getMessage()));
		}
		
		return respuestaSvc;
	}
	
	public RespuestaSVC consultaOutgoingPorCveRastreoStdDao(Map<String, String> mapReq) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		List<Map<String, Object>> rows = null;

		try {
			rows = jdbcTemplateSti.queryForList(READ_OutgoingDatosCuentaStd, Comun._T(mapReq.get("claveRastreo")), 
																			 Comun._T(mapReq.get("cuentaOrdenante")), 
																			 Comun._T(mapReq.get("cuentaBeneficiario")),
																			 Comun._BD(mapReq.get("monto")));
			if(rows != null && !rows.isEmpty()){
				for (Map<String, Object> row : rows) {
					for (Map.Entry<String, Object> entry : row.entrySet()) {
						respuestaSvc.getBody().addValor(Comun._T(entry.getKey()).toUpperCase(), Comun._T(entry.getValue()));
					}
					break;
				}
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "SIN DATOS");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("[CR : %s] Error : %s", Comun._T(mapReq.get("claveRastreo")), ex.getMessage()));
		}
		
		return respuestaSvc;
	}

	/**
	 * Regresa tipo de pago de spei in
	 * @param id_spei id de spei-incoming
	 * @return tipo de pago encotrado en tabla spei_incoming
	 */
	public Integer ObtenerTipoPago(Long id_spei) {
		RespuestaSVC respuesta = new RespuestaSVC();
		Integer tipoPago = null;
		try {
			tipoPago = jdbcTemplateSti.queryForObject(ObtenerTipoPago, Integer.class, id_spei);

		} catch(Exception e) {
			e.printStackTrace();
			tipoPago = null;
		}

		return tipoPago;
	}

	
	private String _T(Object obj){
		return obj == null ? null : String.valueOf(obj);
	}

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	public String getREAD_IncomingDatosCuentaStd() {
		return READ_IncomingDatosCuentaStd;
	}

	public void setREAD_IncomingDatosCuentaStd(String rEAD_IncomingDatosCuentaStd) {
		READ_IncomingDatosCuentaStd = rEAD_IncomingDatosCuentaStd;
	}

	public String getREAD_OutgoingDatosCuentaStd() {
		return READ_OutgoingDatosCuentaStd;
	}

	public void setREAD_OutgoingDatosCuentaStd(String rEAD_OutgoingDatosCuentaStd) {
		READ_OutgoingDatosCuentaStd = rEAD_OutgoingDatosCuentaStd;
	}

	public String getObtenerTipoPago() {
		return ObtenerTipoPago;
	}

	public void setObtenerTipoPago(String obtenerTipoPago) {
		ObtenerTipoPago = obtenerTipoPago;
	}

}

