package net.cero.ahorro.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.HeaderWS;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.soap.plasticos.servicios.BloquearSoapServiciosSW;



@Controller
public class ProcesoCancelarCuenta implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoCancelarCuenta.class);


	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value="/rechazarCancelarCuentaSW", method=RequestMethod.POST)
	public ResponseEntity<String> rechazarCancelarCuentaSW(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;
		
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
		}catch(Exception ex){
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		if(map.get("operacion") == null || map.get("header") == null ){
			response = new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
			return response;
		}

		try{
			String cuenta = ToolsR._T(map.get("operacion"));
			RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuenta(cuenta);
			if(respCuenta.getErrores().getCodigoError() == 0){
				
				map.put("AUT", "N");
				RespuestaSVC respCancelar = ServiciosAhorroWS.autCancelarCuenta(map);
				if(respCancelar.getErrores().getCodigoError() != 0){
					log.error(String.format("Error: Al cancelar cuenta %s", cuenta));
					respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Al rechazar cancelacion cuenta %s", cuenta));
					resp.setData(gson.toJson(respuestaSvc));
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
				}

			}else{
				log.error(String.format("Error: Cuenta %s No existe", cuenta));
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Cuenta %s No existe", cuenta));
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error("Error [rechazaCancelarCuentaSW] : ", ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/autorizaCancelarCuentaSW", method=RequestMethod.POST)
	public ResponseEntity<String> autorizaCancelarCuentaSW(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;
		
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
		}catch(Exception ex){
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		if(map.get("operacion") == null || map.get("header") == null ){
			response = new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
			return response;
		}

		try{
			String cuenta = ToolsR._T(map.get("operacion"));
			RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuenta(cuenta);
			if(respCuenta.getErrores().getCodigoError() == 0){

				map.put("AUT", "S");
				RespuestaSVC respCancelar = ServiciosAhorroWS.autCancelarCuenta(map);
				if(respCancelar.getErrores().getCodigoError() != 0){
					log.error(String.format("Error: Al cancelar cuenta %s", cuenta));
					respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Al cancelar cuenta %s", cuenta));
					resp.setData(gson.toJson(respuestaSvc));
					jsonResponse = gson.toJson(resp);
					return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
				}
				Map<String, Object> mapDatos = (Map<String, Object>) respCancelar.getBody().getValor("DATOS");
				String motivoDescripcion = ToolsR._T(mapDatos.get("motivo_cancelacion"));
				
				String cuentaID = ToolsR._T(respCuenta.getBody().getValor("CUENTA"));
				String personaID = ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID"));

				RespuestaSVC respConcepto = ServiciosAhorroWS.buscarCuentaConcepto(cuentaID, "AHO_PLASTICO");
				RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(cuentaID);
				String tarjeta = "";
				String tarjeta_id = "";
				if(respTarjeta.getErrores().getCodigoError() == 0){
					tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));
					tarjeta_id = ToolsR._T( respTarjeta.getBody().getValor("TARJETA_ID"));
				}
				if(respConcepto.getErrores().getCodigoError() == 0){
					String estatus = ToolsR._T(respConcepto.getBody().getValor("VALOR"));
					if("SI".equals(estatus) && !tarjeta.equals("")){
						DatosPlasticoREQ datosPlasticoReq = new DatosPlasticoREQ();
						DatosPlasticoOBJ datosPlasticoObj = new DatosPlasticoOBJ();
						PlaHeaderWS plaHeader = setPlaSession((HeaderWS) map.get("header"));
						plaHeader.setIdCliente(ToolsR._LZ(personaID).longValue()); // Long.valueOf(cuentaPan.getPersonaId()));
						plaHeader.setIdPan(ToolsR._LZ(tarjeta_id).longValue()); // Long.valueOf(cuentaPan.getPan()));
						plaHeader.setIdCuenta(ToolsR._LZ(cuentaID).longValue()); // Long.valueOf(cuentaPan.getCuenta()));
						
						datosPlasticoReq.setPlastico(tarjeta);
						datosPlasticoReq.setMotivoDescripcion(motivoDescripcion);
						
						RespuestaSVC respuesta = BloquearServicio(plaHeader, datosPlasticoReq);
						if (respuesta.getErrores().getCodigoError() != 0) {
							log.error(String.format("Error: %s",  respuesta.getErrores().getDescError()));
							respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,  respuesta.getErrores().getDescError());
						}
						datosPlasticoObj = (DatosPlasticoOBJ) respuesta.getBody().getValor("DATOS_PLASTICO_OBJ");
						if (datosPlasticoObj.getCodigo().intValue() != 1) {
							log.error(String.format("Error: %s",  datosPlasticoObj.getDescripcion()));
							respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,  datosPlasticoObj.getDescripcion());
						}
						
					}
				}

			}else{
				log.error(String.format("Error: Cuenta %s No existe", cuenta));
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Cuenta %s No existe", cuenta));
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error("Error [autorizaCancelarCuentaSW] : ", ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
	
	public RespuestaSVC BloquearServicio(PlaHeaderWS header, DatosPlasticoREQ pla) {
		RespuestaSVC respuesta = new RespuestaSVC();
		try {
			respuesta = BloquearSoapServiciosSW.Bloquear(header, pla);
			if (respuesta.getErrores().getCodigoError() != 0) {
				respuesta.getErrores().setErrores(respuesta.getErrores().getErrores());
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			respuesta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
					String.format("%s\n%s", "BloquearSoapServicios::Bloquear", ex.getCause().toString()));
		}
		return respuesta;
	}
	
	private PlaHeaderWS setPlaSession(HeaderWS header) {
		/*
		 * COPIA LOS DATOS DEL HEADER A PLAHEADER PARA EFECTOS DE CONSUMO DE
		 * PLASTICOS CON EL PROVEEDOR SYSCOOP
		 */
		PlaHeaderWS plaHeader = new PlaHeaderWS();
		try {
			plaHeader = new PlaHeaderWS();
			plaHeader.setIdEmpresa(header.getIdEmpresa());
			plaHeader.setUsuarioClave(header.getUsuarioClave());
			plaHeader.setIdUsuario(header.getIdUsuario());
			plaHeader.setIdSucursal(header.getIdSucursal());
			plaHeader.setIpHost(header.getIpHost());
			plaHeader.setIdCanalAtencion(header.getIdCanalAtencion());

		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			return null;
		}
		return plaHeader;
	}
/*
	private String mensaje(String msg){
		return msg == null || "".equals(msg) ? "" : (msg+"\n");
	}
*/	
	
}
