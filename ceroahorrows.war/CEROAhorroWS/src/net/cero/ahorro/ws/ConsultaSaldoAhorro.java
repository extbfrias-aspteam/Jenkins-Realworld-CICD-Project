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
import net.cero.ahorro.logica.SaldoAhorro;
import net.cero.data.ConsultaSaldoAhorroReq;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class ConsultaSaldoAhorro {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/consultaSaldoAhorro", method = RequestMethod.POST)
	public ResponseEntity<String> consultaSaldoAhorro(@RequestBody String json) {
		SaldoAhorro saldoAhorro = new SaldoAhorro();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		ConsultaSaldoAhorroReq req = gson.fromJson(json, ConsultaSaldoAhorroReq.class);
		
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
				resp = saldoAhorro.consultaSaldoAhorro(req.getCuenta());
			}
		}
		catch(Exception e)
		{
			resp.setCodigo(-1);
			resp.setMensaje("Ocurrio un error interno al obtener el saldo de ahorro:: "+ e.getMessage());			
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		
		return response;
	}
	
	private void validaReq(ConsultaSaldoAhorroReq req){
		continua = true;
		if (req.getHeader() == null ){
			continua = false;
			msgValidacion = "Header";
		}else{
			if(req.getHeader().getIdUsuario() <= 0L){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario id" : msgValidacion + ", Usuario id");
			}
			
			if(req.getHeader().getUsuarioClave() == null || req.getHeader().getUsuarioClave().isEmpty()){
				continua = false;
				msgValidacion = (msgValidacion.isEmpty() ? "Usuario" : msgValidacion + ", Usuario");
			}
		}
		
		if(req.getCuenta() == null || req.getCuenta().isEmpty()){
			continua = false;
			msgValidacion = (msgValidacion.isEmpty() ? "Cuenta" : msgValidacion + ", Cuenta");
		}
	}
}

