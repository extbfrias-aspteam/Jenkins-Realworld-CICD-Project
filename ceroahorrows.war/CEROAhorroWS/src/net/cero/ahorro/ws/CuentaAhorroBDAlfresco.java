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

import net.cero.ahorro.logica.CuentaAhorroBDAlfrescoLogic;
import net.cero.ahorro.logica.CuentaAhorroEstadoSwitchLogic;
import net.cero.ahorro.logica.ObtenerDatosIneOcrLogic;
import net.cero.data.AhorroAlfrescoOBJ;
import net.cero.data.IneOcrReqOBJ;
import net.cero.data.ResponseService;
import net.cero.spring.config.IPAuthenticationProvider;

@Controller
public class CuentaAhorroBDAlfresco {
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value = "/CuentaAhorroBDAlfresco", method = RequestMethod.POST)
	public ResponseEntity<String> AhorroAlfrescoOCR(@RequestBody String obj) {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;	
		String jsonResponse;
		Gson gson = new Gson();
		ResponseService resp = new ResponseService();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		AhorroAlfrescoOBJ objeto = gson.fromJson(obj, AhorroAlfrescoOBJ.class);

		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		CuentaAhorroBDAlfrescoLogic logic = new CuentaAhorroBDAlfrescoLogic();
		ResponseService estado = logic.insertaAhorroAlfresco(objeto);
		
		
		jsonResponse = gson.toJson(estado);
		response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		return response;
	}
}
