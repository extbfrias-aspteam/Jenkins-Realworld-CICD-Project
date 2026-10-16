package net.cero.pin.svc;

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
import net.cero.ahorro.servicios.ServiciosTransaccionesWS;
import net.cero.ahorro.tools.PinBlockEncryptionUtil;
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.soap.plasticos.servicios.AsignarNIPSoapServiciosSW;
import net.soap.plasticos.servicios.ObtenerSaldoSoapServiciosSW;



@SuppressWarnings("unused")
@Controller
public class EncriptarPin implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(EncriptarPin.class);


	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@RequestMapping(value="/encriptarPIN", method=RequestMethod.POST)
	public ResponseEntity<String> encriptarPIN(@RequestBody String json){
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

		if(map.get("cuenta") == null || map.get("pin") == null){
			response = new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
			return response;
		}

		try{
			String pinblock = PinBlockEncryptionUtil.format0Encode(ToolsR._T(map.get("pin")),  ToolsR._T(map.get("cuenta")));
			if(pinblock != null){
				respuestaSvc.getBody().addValor("PINBLOCK", pinblock);
			}else{
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,"Error: No se gener\u00F3 el PinBlock");
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error(ex.getMessage(),ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
}
