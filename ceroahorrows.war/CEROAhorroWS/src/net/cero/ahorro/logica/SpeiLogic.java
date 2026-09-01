package net.cero.ahorro.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.RespuestaDataList;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroCuentasDAO;
import net.cero.spring.dao.DetalleSpei;
import net.cero.spring.dao.MovimientosCajaDAO;
import net.cero.spring.dao.SpeiDao;

public class SpeiLogic {
	private static final Logger log = LogManager.getLogger(SpeiLogic.class);
	private static Apps apps = null;
	private static SpeiDao speiDao;
	private static AhorroCuentasDAO acDao;
	private static MovimientosCajaDAO mcDao;

	private Function<Map<String, Object>, DetalleSpei> func = row -> {
		DetalleSpei detalleSpei = new DetalleSpei();
		String fecha = obtenerFecha(String.valueOf(row.get("cve_rastreo")), (int) row.get("id_spei"), String.valueOf(row.get("tipo_spei")));
		detalleSpei.setBancoEmisor(String.valueOf(row.get("banco_emisor")).trim());
		detalleSpei.setBancoDestino(String.valueOf(row.get("inst_ben_nombre")).trim());
		detalleSpei.setClaveRastreo(String.valueOf(row.get("cve_rastreo")));
		detalleSpei.setCuentaClabeOrigen(String.valueOf(row.get("cuenta_ordenante")));
		detalleSpei.setCuentaClabeDestino(String.valueOf(row.get("cuenta_beneficiario")));
		detalleSpei.setMonto(Double.valueOf(String.valueOf(row.get("monto"))));
		detalleSpei.setFechaOperacion(String.valueOf(row.get("fecha_captura")));
		detalleSpei.setFechaAplicacion(String.valueOf(row.get("fecha_captura")));
		detalleSpei.setEstatusOperacion((Integer) row.get("status"));
		detalleSpei.setDescripcionEstatus(String.valueOf(row.get("status2")));
		//detalleSpei.setCliente(String.valueOf(row.get("cliente")));
		detalleSpei.setNombreOrigen(String.valueOf(row.get("nombre_ordenante")));
		detalleSpei.setNombreDestino(String.valueOf(row.get("nombre_beneficiario")));
		detalleSpei.setDescripcionDetallada(String.valueOf(row.get("motivo_rechazo")));
		detalleSpei.setReferenciaCobranza(String.valueOf(row.get("referencia_numerica")));
		detalleSpei.setNombreUdn("");
		detalleSpei.setClabeUdn("");
		detalleSpei.setConceptoPago(String.valueOf(row.get("concepto_pago")));
		detalleSpei.setTipoSpei(String.valueOf(row.get("tipo_spei")));
		return detalleSpei;
	};

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null)
					apps = s;
			}
			speiDao = (SpeiDao) s.getApplicationContext().getBean("SpeiDao");
			acDao = (AhorroCuentasDAO) s.getApplicationContext().getBean("AhorroCuentasDAO");
			mcDao = (MovimientosCajaDAO) s.getApplicationContext().getBean("MovimientosCajaDAO");
			
		} catch (Exception e) {
			log.error("error al obtener el bean ", e);
		}
	}

	public RespuestaDataList consultaSpei(Map<String, String> json) {
		initialized();
		final String cveRastreo = json.get("cveRastreo");
		final String fecha = json.get("fecha");
		final String TIPO_SPEI = json.get("tipoSpei");

		RespuestaDataList respuesta = new RespuestaDataList();
		Gson gson = new Gson();
		if (Objects.nonNull(TIPO_SPEI) && validaTipoSpei(TIPO_SPEI)) {
				try {
					final String WHERE = WS_UTIL.CLAUSULA_WHERE_SPEI(cveRastreo, fecha, TIPO_SPEI);
					List<Map<String, Object>> speiRow = speiDao.consultaSpeiWhere(WHERE, TIPO_SPEI, cveRastreo, fecha);
					log.info(gson.toJson(speiRow));
					List<DetalleSpei> spei = speiRow.stream().map(row -> func.apply(row)).collect(Collectors.toList());
					List<Object> lista = new ArrayList<Object>(spei);
					
					if(lista.isEmpty()) {
						respuesta.setCodigo(5);
						respuesta.setMensaje("No existe información de la operación");
						respuesta.setData(lista);
						
					} else {
						respuesta.setCodigo(200);
						respuesta.setMensaje("Solicitud exitosa");
						respuesta.setData(lista);
						
					}

				} catch (Exception e) {
					log.error("Error al procesar spei ", e);
					respuesta.setCodigo(4);
					respuesta.setMensaje(e.getMessage());
					respuesta.setData(new ArrayList<Object>());	
				}
		}else {
			respuesta.setCodigo(4);
			respuesta.setMensaje("El tipo de SPEI es requerido");
			respuesta.setData(new ArrayList<Object>());
		}
		return respuesta;
	}

	private boolean validaTipoSpei(String tipoSpei) {
		return (tipoSpei.contains(WS_UTIL.SPEI_IN ) && tipoSpei.length() == 2) || (tipoSpei.contains(WS_UTIL.SPEI_OUT) && tipoSpei.length() == 3);
	}
	
	String obtenerFecha(String claveRastreo, int idSpei, String tipoSpei) {
		String tipoTransaccion = tipoSpei.equals("IN") ? "+" : "-"; 
		String fecha;
		fecha = acDao.obtenerMovimientoIdSpei(idSpei, tipoTransaccion);
		if (fecha == null)
			fecha = mcDao.obtenerMovimientoClaveRastreo(claveRastreo);
		
		return fecha;
		
	}
}
