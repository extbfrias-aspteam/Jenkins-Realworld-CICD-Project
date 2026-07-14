package net.cero.ahorro.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.cero.utilidades.ValidacionesAhorroUtils;
import net.cero.ahorro.data.ConsultarEstatusTarjetasOBJ;
import net.cero.ahorro.data.ValidaOperacionCeroReq;
import net.cero.ahorro.servicios.ServicioOperacionesTarjetaDock;
import net.cero.ahorro.servicios.ServiciosDepositoAhorro;
import net.cero.ahorro.servicios.ServiciosTransaccionesWS;
import net.cero.ahorro.servicios.ServiciosWSAdminPlasticos;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class ProcesoDepositarAhorro implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoDepositarAhorro.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	private final ServiciosDepositoAhorro logic;
	private final ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock;
	private final ValidacionesAhorroUtils validacionesAhorroUtils;

	public ProcesoDepositarAhorro(ServiciosDepositoAhorro logic,
								  ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock,
								  ValidacionesAhorroUtils validacionesAhorroUtils) {
		this.logic = logic;
		this.servicioOperacionesTarjetaDock = servicioOperacionesTarjetaDock;
		this.validacionesAhorroUtils = validacionesAhorroUtils;
	}

	@RequestMapping(value="/depositarAhorro", method=RequestMethod.POST)
	public ResponseEntity<String> depositarAhorro(@RequestBody String json, HttpServletRequest request){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;

		resp.setCodigo(-1);
		try{
			try{
				map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
			}catch(Exception ex){
				log.error(ex.getMessage(),ex);
				resp.setMensaje("Proceso incorrecto");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}

			if(map.get("cuenta") == null || map.get("tipoTransaccionID")  == null || map.get("tipoClave") == null || map.get("fecha") == null ||
					map.get("monto") == null ||	map.get("descripcion") == null || map.get("estatusID") == null){

				resp.setMensaje("Parametros incorrectos");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}

			/*------IMPRIME LAS VARIABLES DE ENTRADA ----------*/
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				log.info(String.format("%20s - %s", entry.getKey(), entry.getValue()));
			}


			String valMonto = validarMonto(map);
			if(!"OK".equals(valMonto)){
				resp.setMensaje("Monto a Dep\u00F3sitar incorrecto");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}

			Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
			if(!authenticate.isAuthenticated()){
				resp.setMensaje("Servicio denegado");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
			}

			try{
				String cuenta = ToolsR._T(map.get("cuenta"));
				RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuenta(cuenta);
				if(respCuenta.getErrores().getCodigoError() != 0){
					resp.setMensaje(respCuenta.getErrores().getDescError());
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
				}

				String validar = validarCuenta(respCuenta);
				if(!"OK".equals(validar)){
					resp.setMensaje("Validaci\u00F3n de la cuenta incorrecta");
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
				}

				String cuentaID = ToolsR._T(respCuenta.getBody().getValor("CUENTA"));

				/*CHECAMOS SI EL MOVIMIENTO SOLICITADO NO ES DEL TIPO PARA DEVOLUCION. EN CASO DE SERLO, NO APLICA LA REGLA DE LIMITE A DEPOSITOS*/
				boolean validReglaMontoMaximo=validacionesAhorroUtils.validaReglaMontoMaximo(map);
				if(validReglaMontoMaximo)
				{
					boolean validarMontoMaximo=true;
					if(cuenta.substring(0, 2).equals("05")) {
						validarMontoMaximo= filtrarConcepto(cuentaID, "AHO_MAXIMO_DEPOSITO", ToolsR._T(map.get("monto")));
					}else if(cuenta.substring(0, 2).equals("07")) {
						validarMontoMaximo=true;
					}
					if(!validarMontoMaximo){
						resp.setMensaje("Error: monto a depositar es mayor al monto m\u00E1ximo");
						jsonResponse = gson.toJson(resp);
						return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
					}
				}

				String personaID = ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID"));
				RespuestaSVC respSolicitante = ServiciosAhorroWS.buscarSolicitante(personaID);

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
				
				//si la cuenta es producto 6 llamar nuevo servicio 
				String producto = (String)respCuenta.getBody().getValor("PRODUCTO_ID");
				if ("6".equals(producto)) { 
				    HeaderWS header = new HeaderWS();
				    net.cero.ahorro.data.Respuesta resultadoValida = ServiciosWSAdminPlasticos.validarMontoTransaccional((String)map.get("cuenta"),Double.valueOf(String.valueOf(map.get("monto"))),header);
				if(resultadoValida.getCodigo() != 0)
				{
					resp.setCodigo(1);
					resp.setMensaje(resultadoValida.getMensaje());
					resp.setData(null);
					log.info("Resultado AhorroSwitchCero: {}",resp);
					return new ResponseEntity<>(gson.toJson(resp), HttpStatus.OK);
				}
				log.info("FINALIZA VALIDACION DE MONTOS EN DEPOSITO");
				log.info("==========================================");
				log.info("==========================================");

				}
				String tarjeta = "";
				Respuesta consultaResp = servicioOperacionesTarjetaDock.consultaEstatusTarjeta(String.valueOf(map.get("cuenta")));
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
				respuestaSvc = "SI".equals(estatus) && !StringUtils.isBlank(tarjeta) ? logic.depositarProveedor(map) : logic.depositarLocal(map);

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
				log.error(ex.getMessage(),ex);
				resp.setMensaje("Error: Proceso de deposito incorrecto");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			catch (Exception e)
			{
				log.error(e.getMessage(),e);
				resp.setMensaje("Error: Proceso de deposito incorrecto");
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

	private String mensaje(String msg){
		return msg == null || "".equals(msg) ? "" : (msg+"\n");
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
			filtro = true;
		}
		return filtro;
	}

}
