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

import net.cero.ahorro.logica.RegistroCuentaAhorroSimplificadaLogic;
import net.cero.ahorro.logica.RegistroImagenesCuentaAhorroSimplificadaLogic;
import net.cero.data.IneOcrRespOBJ;
import net.cero.data.RegistroCuentaAhorroSimplificadaReq;
import net.cero.data.RegistroImagenesCuentaAhorroSimplificadaReq;
import net.cero.data.ResponseService;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class RegistroImagenesCuentaAhorroSimplificada {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	private static RegistroImagenesCuentaAhorroSimplificadaLogic registro;
	Boolean continua = false;
	String msgValidacion = "";
	
	
	@RequestMapping(value = "/registroImagenesCuentaAhorroSimplificada", method = RequestMethod.POST)
	public ResponseEntity<String> registroCuentaAhorroSimplificada(@RequestBody String json) {
		registro = new RegistroImagenesCuentaAhorroSimplificadaLogic();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		ResponseService resp = new ResponseService();
		
		RegistroImagenesCuentaAhorroSimplificadaReq req = gson.fromJson(json, RegistroImagenesCuentaAhorroSimplificadaReq.class);
		
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		try 
		{
			
			validaReq(req);
			if (!continua) {				
				resp.setCode(3);
				resp.setMenssage("Faltan datos obligatorios: " + msgValidacion);
			} else {
				resp = registro.registroImagenesCuentaAhorroSimplificadaReq(req);
			}
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			resp.setCode(-1);
			resp.setMenssage("Ocurrio un error interno al registrar cuenta de ahorro:: "+ e.getMessage());			
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		
		return response;
	}
	
	private void validaReq(RegistroImagenesCuentaAhorroSimplificadaReq req){
		continua = true;
		
	}
}

