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

import net.cero.ahorro.logica.ObtenerDatosIneOcrLogic;
import net.cero.data.IneOcrReqOBJ;
import net.cero.data.ResponseService;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class ObtenerDatosIneOcr {
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value = "/obtenerDatosIneOcr", method = RequestMethod.POST)
	public ResponseEntity<String> obtenerDatosIneOcr(@RequestBody String json) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		ResponseService resp = new ResponseService();
		
		IneOcrReqOBJ req = gson.fromJson(json, IneOcrReqOBJ.class);
		
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		if(req == null || req.getId() == null) {
			resp.setCode(1);
			resp.setMenssage("Faltan la imagen del INE");
			
		}
		else {
			ObtenerDatosIneOcrLogic logic = new ObtenerDatosIneOcrLogic();
			String jsonIne = logic.obtenerDatosIne(req);
			
			if(jsonIne.equals("")){
				resp.setCode(1);
				resp.setMenssage("No se ha podido extraer la información");
			}
			else {
				resp.setCode(0);
				resp.setMenssage("OK");
				resp.setData(jsonIne);
			}
		}
		
		jsonResponse = gson.toJson(resp);		
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		return response;
	}
}
