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
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;



@Controller
public class ProcesoConsultaCuenta implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoConsultaCuenta.class);


	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@RequestMapping(value="/consultaCuenta", method=RequestMethod.POST)
	public ResponseEntity<String> consultaCuenta(@RequestBody String json){
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

		if(map.get("cuenta") == null || map.get("usuarioID") == null || map.get("ip") == null || map.get("canalID") == null){
			response = new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
			return response;
		}

		try{
			String cuenta = ToolsR._T(map.get("cuenta"));
			RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuenta(cuenta);
			if(respCuenta.getErrores().getCodigoError() == 0){
				RespuestaSVC respTarjeta = new RespuestaSVC();

				String cuentaID = ToolsR._T(respCuenta.getBody().getValor("CUENTA"));
				String personaID = ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID"));

				RespuestaSVC respSolicitante = ServiciosAhorroWS.buscarSolicitante(personaID);
				RespuestaSVC respConcepto = ServiciosAhorroWS.buscarCuentaConcepto(cuentaID, "AHO_PLASTICO");
				if(respConcepto.getErrores().getCodigoError() == 0){
					String estatus = ToolsR._T(respConcepto.getBody().getValor("VALOR"));
					if("SI".equals(estatus)){
						respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(cuentaID);
					}else{
						respTarjeta.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Sin tarjeta");
					}
				}

				respuestaSvc.getBody().addValor("CUENTA_ID", respCuenta.getBody().getValor("CUENTA_ID")); 
				respuestaSvc.getBody().addValor("CUENTA", respCuenta.getBody().getValor("CUENTA"));
				respuestaSvc.getBody().addValor("PRODUCTO_ID", respCuenta.getBody().getValor("PRODUCTO_ID"));
				respuestaSvc.getBody().addValor("PRODUCTO_CLAVE", respCuenta.getBody().getValor("PRODUCTO_CLAVE"));
				respuestaSvc.getBody().addValor("PRODUCTO", respCuenta.getBody().getValor("PRODUCTO"));
				respuestaSvc.getBody().addValor("REFERENCIA", respCuenta.getBody().getValor("REFERENCIA"));
				respuestaSvc.getBody().addValor("ESTATUS_ID", respCuenta.getBody().getValor("ESTATUS_ID")); 
				respuestaSvc.getBody().addValor("ESTATUS_CUENTA", respCuenta.getBody().getValor("ESTATUS"));
				respuestaSvc.getBody().addValor("PERSONA_ID", respCuenta.getBody().getValor("PERSONA_ID"));
				respuestaSvc.getBody().addValor("FECHA_APERTURA", respCuenta.getBody().getValor("FECHA_APERTURA"));
				respuestaSvc.getBody().addValor("MONTO_APERTURA", respCuenta.getBody().getValor("MONTO_APERTURA"));
				respuestaSvc.getBody().addValor("SUCURSAL_ID", respCuenta.getBody().getValor("SUCURSAL_ID"));
				respuestaSvc.getBody().addValor("SUCURSAL", respCuenta.getBody().getValor("SUCURSAL"));
				respuestaSvc.getBody().addValor("CLABE", respCuenta.getBody().getValor("CLABE"));

				if(respSolicitante.getErrores().getCodigoError() == 0){
					respuestaSvc.getBody().addValor("NOMBRE", respSolicitante.getBody().getValor("NOMBRE"));
					respuestaSvc.getBody().addValor("TIPO_PERSONA", respSolicitante.getBody().getValor("TIPO_PERSONA"));
					respuestaSvc.getBody().addValor("RFC", respSolicitante.getBody().getValor("RFC"));
					respuestaSvc.getBody().addValor("TELEFONO", respSolicitante.getBody().getValor("TELEFONO"));
					respuestaSvc.getBody().addValor("CELULAR", respSolicitante.getBody().getValor("CELULAR"));
					respuestaSvc.getBody().addValor("DOMICILIO", respSolicitante.getBody().getValor("DOMICILIO"));
					respuestaSvc.getBody().addValor("CORREO", respSolicitante.getBody().getValor("CORREO"));
				}

				if(respTarjeta.getErrores().getCodigoError() == 0){
					respuestaSvc.getBody().addValor("TARJETA_ID", respTarjeta.getBody().getValor("TARJETA_ID"));
					respuestaSvc.getBody().addValor("TARJETA", respTarjeta.getBody().getValor("TARJETA"));
					respuestaSvc.getBody().addValor("ESTATUS_PLA_CLAVE", respTarjeta.getBody().getValor("ESTATUS_CLAVE"));
					respuestaSvc.getBody().addValor("ESTATUS_PLA", respTarjeta.getBody().getValor("ESTATUS"));
				}
			}else{
				log.error(String.format("Error: Cuenta %s No existe", cuenta));
				respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Cuenta %s No existe", cuenta));
			}

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error("Error [consultaCuenta] : ", ex);
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}

		jsonResponse = gson.toJson(resp);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

/*
	private String mensaje(String msg){
		return msg == null || "".equals(msg) ? "" : (msg+"\n");
	}
*/	
	
}
