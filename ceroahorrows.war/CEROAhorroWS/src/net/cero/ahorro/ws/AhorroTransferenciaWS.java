package net.cero.ahorro.ws;

import org.springframework.beans.factory.annotation.Autowired;
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

import net.cero.ahorro.logica.AhorroTransferenciaLogic;
import net.cero.ahorro.logica.CajaDepositoAhorro;
import net.cero.data.AhorroTransferenciaReq;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class AhorroTransferenciaWS {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	private static AhorroTransferenciaLogic ahorroTransferencia;
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/ahorroTransferencia", method = RequestMethod.POST)
	public ResponseEntity<String> ahorroTransferencia(@RequestBody String json) {
		ahorroTransferencia = new AhorroTransferenciaLogic();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		AhorroTransferenciaReq req = gson.fromJson(json, AhorroTransferenciaReq.class);
		
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		try 
		{
			validaReq(req);
			if (!continua) {				
				resp.setCodigo(3);
				resp.setMensaje("Faltan datos obligatorios: " + msgValidacion);
			} else {
				resp = ahorroTransferencia.ahorroTransferencia(req);
			}
		}
		catch(Exception e)
		{
			resp.setCodigo(-1);
			resp.setMensaje("Ocurrio un error interno al registrar el deposioto de ahorro:: "+ e.getMessage());			
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		
		return response;
	}
	
	private void validaReq(AhorroTransferenciaReq req){
		continua = true;
		if (req.getHeader() != null ){			
			if(req.getHeader().getIdUsuario() <= 0L){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : msgValidacion + ", Usuario");
			}
			
			if(req.getHeader().getUsuarioClave() == null || req.getHeader().getUsuarioClave().isEmpty()){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : msgValidacion + ", Usuario");
			}
		}else{
			continua = false;
			msgValidacion = "Header";
		}
		if(req.getCuentaOrigen() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cuenta de origen" : msgValidacion + ", Cuenta de origen");
		}
		if(req.getCuentaDestino() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cuenta de destino" : msgValidacion + ", Cuenta de destino");
		}
		if(req.getFecha() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Fecha" : msgValidacion + ", Fecha");
		}
		if(req.getMonto() == null){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Monto" : msgValidacion + ", Monto");
		}
	}
}

