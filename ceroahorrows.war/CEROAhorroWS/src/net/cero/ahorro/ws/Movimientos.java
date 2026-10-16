package net.cero.ahorro.ws;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import net.cero.ahorro.logica.MovimientosLogic;
import net.cero.data.AuthException;
import net.cero.data.MovimientoDenegadoObj;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@RestController
public class Movimientos {
	private static final Logger log = LogManager.getLogger(Movimientos.class);
	/**
	 * logger
	 */
	//private final Logger LOG = LogManager.getLogger(Movimientos.class);
	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	String msgValidacion = "";
	private MovimientosLogic mov = new MovimientosLogic();
	
	@GetMapping(path = "/consultaCatalogoMovimientos", produces = "application/json")
	public Respuesta consultaCatalogoMovimientosManuales() {
		Respuesta respuesta = new Respuesta();
		try {
			validaSeguridad();
			
			respuesta= mov.consultaCatalogoMovimientosManuales();
		} catch (AuthException e) {
			log.error("Error al validar la seguridad ", e);
		}
		
		return respuesta;
	}
	
	@PostMapping(path = "/consultaMovimientosDenegados", produces = "application/json")
	public Respuesta consultaMovimientosDenegados(@RequestBody MovimientoDenegadoObj movimientosDenegado) {
		Respuesta respuesta = new Respuesta();
		try {
			validaSeguridad();
			
			respuesta= mov.consultaMovimientosDenegados(movimientosDenegado);
		} catch (AuthException e) {
			log.error("Error al validar la seguridad ", e);
		}
		
		return respuesta;
	}

	/**
	 * funcion interna para validar la seguridad
	 * 
	 * @throws AuthException
	 */
	private void validaSeguridad() throws AuthException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (authenticate == null || !authenticate.isAuthenticated()) {
			throw new AuthException();
		}
	}

}
