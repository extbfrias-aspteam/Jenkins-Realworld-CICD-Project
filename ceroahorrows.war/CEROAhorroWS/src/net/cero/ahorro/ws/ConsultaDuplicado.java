package net.cero.ahorro.ws;

import com.google.gson.Gson;
import net.cero.ahorro.logica.ConsultaDuplicadoLogic;
import net.cero.ahorro.logica.SolicitanteLogic;
import net.cero.data.AuthException;
import net.cero.data.ConsultaDuplicadoDTO;
import net.cero.data.CuentaAmbienteReferenciaDTO;
import net.cero.spring.config.IPAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultaDuplicado {
	/**
	 * logger
	 */
	private final Logger LOG = LogManager.getLogger(ConsultaDuplicado.class);
	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@PostMapping(path = "/consultaDuplicado", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> consultaDomicilio(@RequestBody ConsultaDuplicadoDTO body) throws AuthException,Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		
		ResponseEntity<String> response;
		Gson gson = new Gson();
	
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		LOG.info("consultaDuplicado req: " + gson.toJson(body));
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}


		ConsultaDuplicadoLogic logic = new ConsultaDuplicadoLogic();
	
		return new ResponseEntity<String>(gson.toJson(logic.consultaDuplicado(body)), HttpStatus.OK);

	}
}
