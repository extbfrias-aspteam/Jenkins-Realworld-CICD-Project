package net.cero.ahorro.ws;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.logica.BloqueoDesbloqueoAppLogic;
import net.cero.ahorro.ws.util.BloqueoDesbloqueo;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@RestController
@Log4j2
public class BloqueoDesbloqueoAppWs {
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value = "/bloqueoApp", method = RequestMethod.POST)
	public ResponseEntity<Respuesta> bloqueoApp(@RequestBody Map<String, String> body){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}
		BloqueoDesbloqueoAppLogic logic = new BloqueoDesbloqueoAppLogic();
		respuesta = logic.bloqueoApp(body);
		
		return ResponseEntity.ok(respuesta);
	}
	
	@RequestMapping(value = "/desbloqueoApp", method = RequestMethod.POST)
	public ResponseEntity<Respuesta> desbloqueoApp(@RequestBody Map<String, String> body){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");

			return ResponseEntity.ok(respuesta);
		}
		log.info(body);
		BloqueoDesbloqueoAppLogic logic = new BloqueoDesbloqueoAppLogic();
		respuesta = logic.desbloqueoApp(BloqueoDesbloqueo.DESBLOQUEA,body);
		
		return ResponseEntity.ok(respuesta);
	}

}
