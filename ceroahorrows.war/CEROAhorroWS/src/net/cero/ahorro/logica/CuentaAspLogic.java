package net.cero.ahorro.logica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;

import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.BloqueoDesbloqueoCuentaDTO;
import net.cero.data.DashboardCuentasOBJ;
import net.cero.data.NivelCuenta;
import net.cero.data.Respuesta;
import net.cero.data.TarjetasDock;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.CuentaAspDAO;
import net.cero.spring.dao.excepcion.DaoException;

public class CuentaAspLogic {
	private static final Logger log = LogManager.getLogger(CuentaAspLogic.class);

	private static Apps apps = null;
	private static CuentaAspDAO cuentaAspDao;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}

			cuentaAspDao = (CuentaAspDAO) s.getApplicationContext().getBean("CuentaAspDao");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public Respuesta bloqueoDesbloqueo(final String operacion, BloqueoDesbloqueoCuentaDTO json) {
		initialized();
		Respuesta respuesta = new Respuesta();
		try {
			if (Objects.isNull(json.getCuentaAsp())) {
				respuesta.setCodigo(4);
				respuesta.setData("");
				respuesta.setMensaje("No se ha proporcionado la cuenta");
				return respuesta;
			}
			if (operacion.equals("bloqueo")) {
				cuentaAspDao.bloqueoCuenta(json.getCuentaAsp());
				bloqueaCuentaDock(json);
				
				respuesta.setCodigo(0);
				respuesta.setData("");
				respuesta.setMensaje("Bloqueo exitoso");

			} else if (operacion.equals("desbloqueo")) {
				cuentaAspDao.desbloqueoCuenta(json.getCuentaAsp());
				bloqueaDesbloqueaCuentaDock(false, json);
				
				respuesta.setCodigo(0);
				respuesta.setData("");
				respuesta.setMensaje("Desbloqueo exitoso");
			}

		} catch (Exception e) {
			log.error("Error al bloquear la cuenta {}", e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		}

		return respuesta;
	}

	private void bloqueaCuentaDock(BloqueoDesbloqueoCuentaDTO body) {
		bloqueaDesbloqueaCuentaDock(true, body);
	}
	private Respuesta bloqueaDesbloqueaCuentaDock(boolean bloqueaTarjeta, BloqueoDesbloqueoCuentaDTO body) {
		Gson gson = new Gson();
		Respuesta respuesta = new Respuesta();
		// consulta cuentas en dock
		Map<String, Object> jsonCuentasDock = new HashMap<>();
		jsonCuentasDock.put("header", body.getHeader());
		jsonCuentasDock.put("cuentaah", body.getCuentaAsp());
		
		try {
			String respuestaDock = WS_UTIL.doPostRequest(gson.toJson(jsonCuentasDock), ConstantesUtil.CONSULTA_TARJETAS_DOCK);
			if(StringUtils.isBlank(respuestaDock))
			{
				respuesta.setCodigo(1);
				respuesta.setMensaje("No se pudieron obtener el ó los plasticos asociados a la cuenta.");
				return respuesta;
			}
			
			Respuesta respuestaObj = gson.fromJson(respuestaDock, Respuesta.class);
			Respuesta respuestaBloqueoObj = new Respuesta();
			String respuestaBloqueo = "";
			Map<String, Object> bloqueTarjetaDock = new HashMap<>();
			
			final String URL_BLOQUEA_DESB_DOCK = (bloqueaTarjeta) ? ConstantesUtil.BLOQUEA_TARJETA_DOCK : ConstantesUtil.DESBLOQUEA_TARJETA_DOCK;
			if (respuestaObj.getCodigo() == WS_UTIL.CODIGO_EXITO) {
				TarjetasDock tarjetasDock = gson.fromJson(respuestaObj.getData(), TarjetasDock.class);
				if (!StringUtils.isBlank(tarjetasDock.getTarjetaPrincipal())) {

					bloqueTarjetaDock.put("header", body.getHeader());
					bloqueTarjetaDock.put("numeroTarjeta", tarjetasDock.getTarjetaPrincipal());
					bloqueTarjetaDock.put("motivoBloqueo", WS_UTIL.MOTIVO_BLOQUEO);
					bloqueTarjetaDock.put("token", "");
					 
					respuestaBloqueo = WS_UTIL.doPostRequest(gson.toJson(bloqueTarjetaDock),URL_BLOQUEA_DESB_DOCK);
					respuestaBloqueoObj = gson.fromJson(respuestaBloqueo, Respuesta.class);
					if (respuestaBloqueoObj.getCodigo() == WS_UTIL.CODIGO_EXITO) {
						log.info(respuestaBloqueoObj.getMensaje());
					} else {
						log.error("Error en bloqueo/desbloqueo " + respuestaBloqueoObj.getMensaje());
						return respuestaBloqueoObj;
					}
				}

				if (!StringUtils.isBlank(tarjetasDock.getTarjetaAdicional())) {
					bloqueTarjetaDock.put("header", body.getHeader());
					bloqueTarjetaDock.put("numeroTarjeta", tarjetasDock.getTarjetaAdicional());
					bloqueTarjetaDock.put("motivoBloqueo", WS_UTIL.MOTIVO_BLOQUEO);
					bloqueTarjetaDock.put("token", "");

					respuestaBloqueo = WS_UTIL.doPostRequest(gson.toJson(bloqueTarjetaDock),URL_BLOQUEA_DESB_DOCK);
					respuestaBloqueoObj = gson.fromJson(respuestaBloqueo, Respuesta.class);

					if (respuestaBloqueoObj.getCodigo() == WS_UTIL.CODIGO_EXITO) {
						log.info(respuestaBloqueoObj.getMensaje());
					} else {
						log.error("Error en bloqueo/desbloqueo " + respuestaBloqueoObj.getMensaje());
						return respuestaBloqueoObj;
					}
				}
			}

		} catch (IOException e) {
			log.error("Error al consultar tarjetas en dock ", e);
		}
		return respuesta;
	}

	public Respuesta catalogoNivelCuenta() {
		initialized();
		Respuesta respuesta = new Respuesta();
		Gson gson = new Gson();
		try {
			List<Map<String, Object>> nivelesCuenta = cuentaAspDao.consultaCatalogoNivelCuenta();
			List<NivelCuenta> nivelesCuentaList = nivelesCuenta.stream().map(row -> {
				NivelCuenta nivelCuenta = new NivelCuenta();
				nivelCuenta.setId(Integer.parseInt(row.get("id").toString()));
				nivelCuenta.setClave(String.valueOf(row.get("clave")));
				final String monto = String.valueOf(row.get("monto_max"));
				nivelCuenta.setMonto(monto.equals("null") ? "Sin límite" : monto);

				return nivelCuenta;
			}).collect(Collectors.toList());

			respuesta.setCodigo(0);
			respuesta.setMensaje("Solicitud exitosa");
			respuesta.setData(gson.toJson(nivelesCuentaList));
		} catch (DaoException e) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		} catch (NullPointerException | NumberFormatException e2) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("Ocurrió un error al consultar los niveles de cuenta");
		}
		return respuesta;
	}

	public Respuesta cambioNivelCuenta(Map<String, String> json) {
		initialized();
		Respuesta respuesta = new Respuesta();

		try {
			if (!json.containsKey("cuentaAsp")) {
				respuesta.setCodigo(4);
				respuesta.setData("");
				respuesta.setMensaje("No se ha proporcionado la cuenta");
				return respuesta;
			}

			if (!json.containsKey("nivelCuenta")) {
				respuesta.setCodigo(4);
				respuesta.setData("");
				respuesta.setMensaje("No se ha proporcionado el nivel de la cuenta");
				return respuesta;
			}

			cuentaAspDao.cambiaNivelCuenta(json.get("cuentaAsp").toString(), json.get("nivelCuenta").toString());
			respuesta.setCodigo(0);
			respuesta.setData("");
			respuesta.setMensaje("Actualización de nivel cuenta exitosa");
		} catch (DaoException e) {
			log.error("Error al actualizar el nivel de la cuenta {}", e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		}

		return respuesta;
	}

	public static CuentaAspDAO getCuentaAspDao() {
		return cuentaAspDao;
	}

	public static void setCuentaAspDao(CuentaAspDAO cuentaAspDao) {
		CuentaAspLogic.cuentaAspDao = cuentaAspDao;
	}

	public Respuesta dashboardCuentas(final Map<String, Object> body) {
		initialized();
		Gson gson = new Gson();
		
		Map<String, String> mesesAnioMap = getMesesAnio();
		Respuesta respuesta = new Respuesta();
		
		if(!body.containsKey("anio")) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No se ha ingresado un año para la consulta");
			
			return respuesta;
		}
		
		if(!body.containsKey("productoId")) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No se ha ingresado un id de producto");
			
			return respuesta;
		}
		Map<String, Object> responseBodyMap = new HashMap<>();
		
			
		List<DashboardCuentasOBJ> dashboardCuentasOBJs = new ArrayList<>();
		try {
			consultaCuentasVigentes(mesesAnioMap, Integer.valueOf(body.get("anio").toString()),Integer.parseInt(body.get("productoId").toString()),dashboardCuentasOBJs);
			
			List<Map<String, Object>> totalEstatusMap = cuentaAspDao.consultaTotalEstatusCuentas(Integer.valueOf(String.valueOf(body.get("anio"))), Integer.valueOf(String.valueOf(body.get("productoId"))));
		
			WS_UTIL.construyeResponseDashboardCuentas(totalEstatusMap, dashboardCuentasOBJs, responseBodyMap);
			
			respuesta.setCodigo(0);
			respuesta.setMensaje("Exito");
			respuesta.setData(gson.toJson(responseBodyMap));
		} catch (DaoException e) {
			log.error("Error al consultar el dashboard de cuentas", e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		}
		return respuesta;
	}

	private void consultaCuentasVigentes(Map<String, String> mesesAnioMap, final int anio, int productoId, List<DashboardCuentasOBJ> dashboardCuentasOBJs) throws DaoException {
		consultaEstatusCuentaPorMes(mesesAnioMap, anio, productoId,dashboardCuentasOBJs);
	}

	private void consultaEstatusCuentaPorMes(Map<String, String> mesesAnioMap, final int anio, int productoId, List<DashboardCuentasOBJ> dashboardCuentasOBJs){

		List<String> meses = Arrays.asList(WS_UTIL.ENERO, WS_UTIL.FEBRERO, WS_UTIL.MARZO,WS_UTIL.ABRIL, WS_UTIL.MAYO, WS_UTIL.JUNIO, WS_UTIL.JULIO, WS_UTIL.AGOSTO,
											WS_UTIL.SEPTIEMBRE, WS_UTIL.OCTUBRE, WS_UTIL.NOVIEMBRE, WS_UTIL.DICIEMBRE);
		
		for (String mes : meses) {
			DashboardCuentasOBJ cuentasOBJ = new DashboardCuentasOBJ();
			List<DashboardCuentasOBJ.EstatusCuentaDashboard> listEstatusCuentaDashboards = new ArrayList<>();

			DashboardCuentasOBJ.EstatusCuentaDashboard estatusCuentaDashboardVigente = cuentasOBJ.new EstatusCuentaDashboard();

			cuentasOBJ.setMes(mes);

			final int totalCuentasVigentes = cuentaAspDao.consultaCuentaPorMesYEstatusX(mesesAnioMap.get(mes), anio, productoId, WS_UTIL.CUENTA_VIGENTE);
			estatusCuentaDashboardVigente.setEstatus(WS_UTIL.CUENTA_VIGENTE);
			estatusCuentaDashboardVigente.setCantidad(totalCuentasVigentes);

			listEstatusCuentaDashboards.add(estatusCuentaDashboardVigente);	
			
			DashboardCuentasOBJ.EstatusCuentaDashboard estatusCuentaDashboardCancelada = cuentasOBJ.new EstatusCuentaDashboard();
			final int totalCuentasCancelada = cuentaAspDao.consultaCuentaPorMesYEstatusX(mesesAnioMap.get(mes), anio, productoId,WS_UTIL.CUENTA_CANLEADA);

			estatusCuentaDashboardCancelada.setEstatus(WS_UTIL.CUENTA_CANLEADA);
			estatusCuentaDashboardCancelada.setCantidad(totalCuentasCancelada);

			listEstatusCuentaDashboards.add(estatusCuentaDashboardCancelada);
			
			DashboardCuentasOBJ.EstatusCuentaDashboard estatusCuentaDashboardBloqueada = cuentasOBJ.new EstatusCuentaDashboard();
			final int totalCuentasBloqueda = cuentaAspDao.consultaCuentaPorMesYEstatusX(mesesAnioMap.get(mes), anio, productoId, WS_UTIL.CUENTA_BLOQUEDA);

			estatusCuentaDashboardBloqueada.setEstatus(WS_UTIL.CUENTA_BLOQUEDA);
			estatusCuentaDashboardBloqueada.setCantidad(totalCuentasBloqueda);

			listEstatusCuentaDashboards.add(estatusCuentaDashboardBloqueada);
			
			cuentasOBJ.setEstatusCuenta(listEstatusCuentaDashboards);
			
			dashboardCuentasOBJs.add(cuentasOBJ);
		}

	}
	
	public Respuesta consultaProductos(Respuesta respuesta) {
		initialized();
		try {
			Gson gson = new Gson();
			
			List<Map<String, Object>> productosLista = cuentaAspDao.consultaListaProductos().stream().map(item ->{
				Map<String, Object> row = new HashMap<>();
				row.put("idProducto", item.get("id_producto"));
				row.put("claveProducto", item.get("clave"));
				row.put("descProducto", item.get("descripcion"));
				return row;
			}).collect(Collectors.toList());
			
			respuesta.setCodigo(0);
			respuesta.setMensaje("Solicitud exitosa");
			respuesta.setData(gson.toJson(productosLista));
		} catch (DaoException e) {
			log.error("Error al consultar los productos ",e);
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje(e.getMessage());
		}
		return null;
	}

	private Map<String, String> getMesesAnio() {
		Map<String, String> meses = new HashMap<>();
		meses.put(WS_UTIL.ENERO, "01");
		meses.put(WS_UTIL.FEBRERO, "02");
		meses.put(WS_UTIL.MARZO, "03");
		meses.put(WS_UTIL.ABRIL, "04");
		meses.put(WS_UTIL.MAYO, "05");
		meses.put(WS_UTIL.JUNIO, "06");
		meses.put(WS_UTIL.JULIO, "07");
		meses.put(WS_UTIL.AGOSTO, "08");
		meses.put(WS_UTIL.SEPTIEMBRE, "09");
		meses.put(WS_UTIL.OCTUBRE, "10");
		meses.put(WS_UTIL.NOVIEMBRE, "11");
		meses.put(WS_UTIL.DICIEMBRE, "12");
		return meses;
	}
}
