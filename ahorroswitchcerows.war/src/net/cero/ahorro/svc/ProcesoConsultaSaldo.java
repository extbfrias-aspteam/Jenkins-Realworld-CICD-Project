package net.cero.ahorro.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.cero.ahorro.servicios.ServicioConsultaSaldo;
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
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.soap.plasticos.servicios.ObtenerSaldoSoapServiciosSW;


@SuppressWarnings("unused")
@Controller
public class ProcesoConsultaSaldo implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ProcesoConsultaSaldo.class);


	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	private final ServicioConsultaSaldo logic;

	public ProcesoConsultaSaldo(ServicioConsultaSaldo logic) {
		this.logic = logic;
	}

	@RequestMapping(value="/consultaSaldo", method=RequestMethod.POST)
	public ResponseEntity<String> consultaSaldo(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		RespuestaSVC respuestaSaldo = new RespuestaSVC();
		Respuesta resp = new Respuesta();
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = ToolsR.GBuilder();
		Map<String, Object> map;


		resp.setCodigo(-1);
		try{
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			resp.setMensaje("Proceso incorrecto");
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
		}

		if(map.get("cuenta") == null){
			resp.setMensaje("Parametros incorrectos");
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			resp.setMensaje("Servicio denegado");
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
		}

		/*------IMPRIME LAS VARIABLES DE ENTRADA ----------*/
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			log.info(String.format("%20s - %s", entry.getKey(), entry.getValue()));
		}
		
		try{
			String cuenta = ToolsR._T(map.get("cuenta"));
			RespuestaSVC respCuenta = ServiciosAhorroWS.buscarCuenta(cuenta);
			if(respCuenta.getErrores().getCodigoError() != 0){
				resp.setMensaje(respCuenta.getErrores().getDescError());
				jsonResponse = gson.toJson(resp);
				return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
			}


			String cuentaID = ToolsR._T(respCuenta.getBody().getValor("CUENTA"));
			String estatus = "NO";
			
			map.put("ID", ToolsR._T(respCuenta.getBody().getValor("CUENTA_ID")));
			map.put("cuentaID", ToolsR._T(respCuenta.getBody().getValor("CUENTA")));
			map.put("clienteID", ToolsR._T(respCuenta.getBody().getValor("PERSONA_ID")));
			
			RespuestaSVC respConcepto = ServiciosAhorroWS.buscarCuentaConcepto(cuentaID, "AHO_PLASTICO");
			if(respConcepto.getErrores().getCodigoError() == 0){
				estatus = ToolsR._T(respConcepto.getBody().getValor("VALOR"));
			}
			
			respuestaSvc = "SI".equals(estatus) ? logic.consultarSaldoProveedor(map) : logic.consultarSaldoLocal(map);

			resp.setCodigo(0);
			resp.setMensaje("OK");
			resp.setData(gson.toJson(respuestaSvc));
		}catch(DataAccessException ex){
			log.error(ex.getMessage(),ex);
			resp.setMensaje("Error: Proceso de consulta saldo incorrecto");
			jsonResponse = gson.toJson(resp);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		jsonResponse = gson.toJson(resp);
		log.info("Resultado del WS: {}",jsonResponse);
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}


	private String mensaje(String msg){
		return msg == null || "".equals(msg) ? "" : (msg+"\n");
	}

}
