package net.cero.ahorro.ws;

import net.cero.data.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import net.cero.ahorro.logica.SolicitanteLogic;
import net.cero.spring.config.IPAuthenticationProvider;


@RestController
public class SolicitanteWS {
	/**
	 * logger
	 */
	private final Logger LOG = LogManager.getLogger(SolicitanteWS.class);
	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@PostMapping(path = "/cuentahabiente", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> consultaDomicilio(@RequestBody CuentaAmbienteReferenciaDTO body) throws AuthException,Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		
		ResponseEntity<String> response;
		Gson gson = new Gson();
	
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		LOG.info("cuentahabiente req: " + gson.toJson(body));
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		
		SolicitanteLogic logic = new SolicitanteLogic();
	
		return new ResponseEntity<String>(gson.toJson(logic.cuentaAmbienteReferencia(body)), HttpStatus.OK);

	}

	@PostMapping(path = "/actualizarSolicitante", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> actualizarSolicitante(@RequestBody ActualizarSolicitanteDTO body) throws AuthException,Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;

        ResponseEntity<String> response;
        Gson gson = new Gson();

        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
        LOG.info("actualizarSolicitante req: " + gson.toJson(	body));
        if (!authenticate.isAuthenticated()) {
            response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            return response;
        }


        SolicitanteLogic logic = new SolicitanteLogic();

        return new ResponseEntity<String>(gson.toJson(logic.actualizarSolicitante(body)), HttpStatus.OK);

    }

	@PostMapping(path = "/actualizarSolicitanteTelefono", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> actualizarSolicitanteTelefono(@RequestBody ActualizarSolicitanteTelefonoReq body) throws AuthException,Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;

		ResponseEntity<String> response;
		Gson gson = new Gson();

		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		LOG.info("actualizarSolicitanteTelefono req: " + gson.toJson(body));
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}


		SolicitanteLogic logic = new SolicitanteLogic();

		return new ResponseEntity<String>(gson.toJson(logic.actualizarSolicitanteTelefono(body)), HttpStatus.OK);

	}

	@PostMapping(path = "/actualizarSolicitanteCorreo", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> actualizarSolicitanteCorreo(@RequestBody ActualizarSolicitanteCorreoReq body) throws AuthException,Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;

		ResponseEntity<String> response;
		Gson gson = new Gson();

		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		LOG.info("actualizarSolicitanteCorreo req: " + gson.toJson(body));
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}


		SolicitanteLogic logic = new SolicitanteLogic();

		return new ResponseEntity<String>(gson.toJson(logic.actualizarSolicitanteCorreo(body)), HttpStatus.OK);

	}

	@PostMapping(path = "/actualizarSolicitanteDomicilio", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> actualizarSolicitanteDomicilio(@RequestBody ActualizarSolicitanteDomicilioReq body) throws AuthException,Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;

		ResponseEntity<String> response;
		Gson gson = new Gson();

		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		LOG.info("actualizarSolicitanteDomicilio req: " + gson.toJson(body));
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		SolicitanteLogic logic = new SolicitanteLogic();

		return new ResponseEntity<String>(gson.toJson(logic.actualizarSolicitanteDomicilio(body)), HttpStatus.OK);

	}
	@GetMapping(path = "/getDomicilioSolicitante", produces = "application/json")
	public ResponseEntity<String> consultaCatalogoMovimientosManuales(@RequestParam String solicitanteId) {
		LOG.info("getDomicilioSolicitante requestParam: " + solicitanteId );
		Respuesta respuesta = new Respuesta();
		ResponseEntity<String> response;
		Gson gson = new Gson();
		SolicitanteLogic logic = new SolicitanteLogic();
		return new ResponseEntity<String>(gson.toJson(logic.getDomicilioSolicitante(solicitanteId)), HttpStatus.OK);

	}

}
