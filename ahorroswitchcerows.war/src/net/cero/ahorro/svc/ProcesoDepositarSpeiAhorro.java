package net.cero.ahorro.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.cero.ahorro.data.ConsultarEstatusTarjetasOBJ;
import net.cero.ahorro.data.ValidaOperacionCeroReq;
import net.cero.ahorro.servicios.ServicioOperacionesTarjetaDock;
import net.cero.ahorro.servicios.ServiciosDepositoAhorro;
import net.cero.utilidades.ValidacionesAhorroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cero.ahorro.servicios.ServiciosAhorroWS;

import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Constantes;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;

import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("unused")
@Controller
public class ProcesoDepositarSpeiAhorro implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoDepositarSpeiAhorro.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	private final ServiciosDepositoAhorro logic;
	private final ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock;
	private final ValidacionesAhorroUtils validacionesAhorroUtils;

	public ProcesoDepositarSpeiAhorro(ServiciosDepositoAhorro logic,
									  ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock,
									  ValidacionesAhorroUtils validacionesAhorroUtils) {
		this.logic = logic;
		this.servicioOperacionesTarjetaDock = servicioOperacionesTarjetaDock;
		this.validacionesAhorroUtils = validacionesAhorroUtils;
	}

	@RequestMapping(value="/depositarSpeiAhorro", method=RequestMethod.POST)
	public ResponseEntity<String> depositarSpeiAhorro(@RequestBody String json, HttpServletRequest request){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;

		try{
			try{
				map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
			}catch(Exception ex){
				//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
				respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
				respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.FALTA_INFORMACION);
				respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.FALTA_INFORMACION));

				resp.setCodigo(400);
				resp.setMensaje("ERROR");
				resp.setData(gson.toJson(respuestaSvc));
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}

			if(map != null){
				log.info("*********************************************************");
				log.info("*         REGISTRANDO LAS VARIABLES DE ENTRADA          *");
				log.info("*********************************************************");

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					//log.info(String.format("%20s - %s", entry.getKey(), entry.getValue()));
					log.info(String.format("%20s - %s", entry.getKey(), entry.getValue()));
				}
			}

			if(map.get("cuenta") == null || map.get("tipoTransaccionID")  == null || map.get("tipoClave") == null || map.get("fecha") == null ||
					map.get("monto") == null ||	map.get("descripcion") == null || map.get("estatusID") == null ||  map.get("formaPagoID") == null || map.get("conciliado") == null){

				//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
				respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
				respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.FALTA_INFORMACION);
				respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.FALTA_INFORMACION));

				resp.setCodigo(206);
				resp.setMensaje("ERROR");
				resp.setData(gson.toJson(respuestaSvc));
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.PARTIAL_CONTENT);
			}

			String valMonto = validarMonto(map);
			if(!"OK".equals(valMonto)){
				//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
				respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
				respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.NO_PROCESADA);
				respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.NO_PROCESADA));

				resp.setCodigo(406);
				resp.setMensaje("ERROR");
				resp.setData(gson.toJson(respuestaSvc));
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_ACCEPTABLE);
			}

			Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
			if(!authenticate.isAuthenticated()){
				//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
				respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
				respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.FALTA_INFORMACION);
				respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.FALTA_INFORMACION));

				resp.setCodigo(401);
				resp.setMensaje("ERROR");
				resp.setData(gson.toJson(respuestaSvc));
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
			}

			try{
				String cuenta = ToolsR._T(map.get("cuenta"));
				RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuentaSpei(cuenta);
				if(respCuenta.getErrores().getCodigoError() != 0){
					//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.CUENTA_INEXISTENTE);
					respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.CUENTA_INEXISTENTE));

					resp.setCodigo(400);
					resp.setMensaje("ERROR");
					resp.setData(gson.toJson(respuestaSvc));
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
				}

				respuestaSvc.getBody().addValor("REFERENCIA", respCuenta.getBody().getValor("REFERENCIA"));
				//respuestaSvc.getBody().addValor("PRODUCTO", respCuenta.getBody().getValor("PRODUCTO"));
				respuestaSvc.getBody().addValor("PRODUCTO", "cuenta ");

				String personaID = ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID"));
				RespuestaSVC respSolicitante = ServiciosAhorroWS.buscarSolicitante(personaID);
				if(respCuenta.getErrores().getCodigoError() != 0){
					//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.CUENTA_INEXISTENTE);
					respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.CUENTA_INEXISTENTE));

					resp.setCodigo(400);
					resp.setMensaje("ERROR");
					resp.setData(gson.toJson(respuestaSvc));
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
				}

				respuestaSvc.getBody().addValor("CELULAR", respSolicitante.getBody().getValor("CELULAR"));
				respuestaSvc.getBody().addValor("CORREO", respSolicitante.getBody().getValor("CORREO"));


				String validar = validarCuenta(respCuenta);
				if(!"OK".equals(validar)){
					//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.NO_PROCESADA);
					respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.NO_PROCESADA));

					resp.setCodigo(400);
					resp.setMensaje("ERROR");
					resp.setData(gson.toJson(respuestaSvc));
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
				}


				String cuentaID = ToolsR._T(respCuenta.getBody().getValor("CUENTA"));
				/*CHECAMOS SI EL MOVIMIENTO SOLICITADO NO ES DEL TIPO PARA DEVOLUCION. EN CASO DE SERLO, NO APLICA LA REGLA DE LIMITE A DEPOSITOS*/
				boolean validReglaMontoMaximo=validacionesAhorroUtils.validaReglaMontoMaximo(map);
				if(validReglaMontoMaximo)
				{
					Boolean validarMontoMaximo = filtrarConcepto(cuentaID, "AHO_MAXIMO_DEPOSITO", ToolsR._T(map.get("monto")));
					if(!validarMontoMaximo){
						//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
						respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
						respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.MONTO_MAXIMO);
						respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.MONTO_MAXIMO));

						resp.setCodigo(400);
						resp.setMensaje("ERROR");
						resp.setData(gson.toJson(respuestaSvc));
						jsonResponse = gson.toJson(resp);
						return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
					}
				}

				String estatus = "NO";
				RespuestaSVC respConcepto = ServiciosAhorroWS.buscarCuentaConcepto(cuentaID, "AHO_PLASTICO");
				if(respConcepto.getErrores().getCodigoError() == 0){
					estatus = ToolsR._T(respConcepto.getBody().getValor("VALOR"));
				}

				map.put("ID", ToolsR._T(respCuenta.getBody().getValor("CUENTA_ID")));
				map.put("cuentaID", ToolsR._T(respCuenta.getBody().getValor("CUENTA")));
				map.put("clienteID", ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID")));

				log.info("DATOS map: {}",map);
				log.info("==========================================");
				log.info("==========================================");
				log.info("PASO PREVIO A VALIDACION DE MONTOS EN DEPOSITO");
				/*VALIDAMOS SI LA OPERACION CORRESPONDE A UNA CUENTA FACIL CON LIMITE DE DEPOSITOS EN UDIS*/
				ValidaOperacionCeroReq req = new ValidaOperacionCeroReq();
				req.setCuenta(map.get("cuenta").toString());
				req.setClaveTransaccionAhorro(String.valueOf(map.get("tipoClave")));
				req.setMontoOperacion(Double.valueOf(String.valueOf(map.get("monto"))));
				Respuesta resultadoValida = ServiciosAhorroWS.validarMontoOperacion(req);
				if(resultadoValida.getCodigo() != 0)
				{
					resp.setCodigo(1);
					resp.setMensaje(resultadoValida.getMensaje());
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.NO_PROCESADA);
					respuestaSvc.getBody().addValor("DEVOLUCION", null);
					resp.setData(gson.toJson(respuestaSvc));
					log.info("Resultado AhorroSwitchCero: {}",resp);
					return new ResponseEntity<>(gson.toJson(resp), HttpStatus.OK);
				}
				log.info("FINALIZA VALIDACION DE MONTOS EN DEPOSITO");
				log.info("==========================================");
				log.info("==========================================");
				if(!map.containsKey("claveMovimientoDock") && map.containsKey("tipoClave"))
				{
					String tipoClave = map.get("tipoClave").toString();
					if(!StringUtils.isBlank(tipoClave))
						switch(tipoClave){
							case "DEP_TRANS":
								map.put("claveMovimientoDock","ASPDS");
								break;
						}
					log.info("VALOR ASIGNADO AL CAMPO claveMovimientoDock: {}",map.get("tipoClave"));
				}

				String tarjeta = "";
				Respuesta consultaResp = servicioOperacionesTarjetaDock.consultaEstatusTarjeta(String.valueOf(map.get("cuentaID")));
				ConsultarEstatusTarjetasOBJ obj = null;
				if(consultaResp.getCodigo() == 0)
				{
					if(!StringUtils.isBlank(consultaResp.getData()))
					{
						obj = gson.fromJson(consultaResp.getData(), ConsultarEstatusTarjetasOBJ.class);
						if(!StringUtils.isBlank(obj.getTarjetaPrincipal()) && StringUtils.isBlank(tarjeta))
						{
							tarjeta = obj.getTarjetaPrincipal();
						}
					}
				}
				/*AGREGAR CODIGO AQUI PARA MANDAR LLAMAR AL ORQUESTADOR DE ALI*/
				RespuestaSVC respuestaDeposito = "SI".equals(estatus) && !StringUtils.isBlank(tarjeta) ? logic.depositarProveedor(map) : logic.depositarLocal(map);


				if(respuestaDeposito.getErrores().getCodigoError() == 0){
					respuestaSvc.getBody().addValor("SALDO_ACTUAL", respuestaDeposito.getBody().getValor("SALDO_ACTUAL"));
					respuestaSvc.getBody().addValor("SALDO_ANTERIOR", respuestaDeposito.getBody().getValor("SALDO_ANTERIOR"));
					respuestaSvc.getBody().addValor("AUTORIZACION", respuestaDeposito.getBody().getValor("AUTORIZACION"));

					//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_NORMAL);
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_ABONADA);
					respuestaSvc.getBody().addValor("DEVOLUCION", "");
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", "0");   // se agrega para verificar el estatus
				}else{
					//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
					respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
					respuestaSvc.getBody().addValor("ID_DEVOLUCION", "");
					respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.NO_PROCESADA));
					respuestaSvc.setErrores(respuestaDeposito.getErrores());
				}
				if(respuestaSvc != null)
					if(respuestaSvc.getErrores().getCodigoError() != 0)
					{
						resp.setCodigo((int)respuestaSvc.getErrores().getCodigoError());
						resp.setMensaje(respuestaSvc.getErrores().getDescError());
					}
					else
					{
						resp.setCodigo(0);
						resp.setMensaje("OK");
					}
				else
				{
					resp.setCodigo(0);
					resp.setMensaje("OK");
				}

				resp.setData(gson.toJson(respuestaSvc));

			}catch(DataAccessException ex){
				log.error("Error [depositarSpeiAhorro] : ", ex);
				//respuestaSvc.getBody().addValor("ID", Constantes.EJECUCION_ERRONEA);
				respuestaSvc.getBody().addValor("ESTATUS", Constantes.ESTATUS_DEVUELTA);
				respuestaSvc.getBody().addValor("ID_DEVOLUCION", Constantes.FALTA_INFORMACION);
				respuestaSvc.getBody().addValor("DEVOLUCION", Constantes.getDescripcion(Constantes.FALTA_INFORMACION));
				resp.setCodigo(400);
				resp.setMensaje("ERROR");
				resp.setData(gson.toJson(respuestaSvc));
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		}
		catch(Exception e)
		{
			resp.setCodigo(400);
			resp.setMensaje("ERROR");
			resp.setData(gson.toJson(respuestaSvc));
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String validarCuenta(RespuestaSVC resp){
		if("".equals(ToolsR._T(resp.getBody().getValor("CUENTA")))) return "INVALIDA";
		if(!"ALTA".equals(ToolsR._T(resp.getBody().getValor("ESTATUS")))) return ToolsR._T(resp.getBody().getValor("ESTATUS"));
		return "OK";
	}

	private String validarTarjeta(RespuestaSVC resp){
		if("".equals(ToolsR._T(resp.getBody().getValor("TARJETA")))) return "INVALIDA";
		if(!"ACT".equals(ToolsR._T(resp.getBody().getValor("ESTATUS_CLAVE")))) return ToolsR._T(resp.getBody().getValor("ESTATUS"));
		return "OK";
	}

	private String validarMonto(Map<String, Object> map){
		if("".equals(ToolsR._T(map.get("monto")))) return "MONTO INVALIDO";
		if(ToolsR._D(map.get("monto")).doubleValue() <= 0.00d) return "MONTO CERO O INFERIOR";

		return "OK";
	}

	/*********************************************************
	 * Filtra el valor maximo permitido, si ocurre un error, 
	 * el filtro no deja pasar
	 * @param cuenta
	 * @param concepto
	 * @param monto
	 * @return 
	 *********************************************************/
	private Boolean filtrarConcepto(String cuenta, String concepto, String monto){
		Boolean filtro = true;
		RespuestaSVC respConcepto = ServiciosAhorroWS.buscarCuentaConcepto(cuenta, concepto);
		if(respConcepto.getErrores().getCodigoError() == 0){
			try{
				Double valor = ToolsR._D(respConcepto.getBody().getValor("VALOR"));
				if(ToolsR._D(monto).doubleValue() > valor.doubleValue()){
					filtro = false;
				}
			}catch(Exception ex){
				log.error(ex.getMessage()); 
				filtro = false;
			}
		}else{
			filtro = false;
		}
		return filtro;
	}

}
