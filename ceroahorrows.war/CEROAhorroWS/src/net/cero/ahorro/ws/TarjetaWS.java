package net.cero.ahorro.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import net.cero.ahorro.logica.TarjetasLogic;
import net.cero.spring.config.IPAuthenticationProvider;
import javax.validation.constraints.NotNull;

/**
 * Clase empleada para exponer endpoints relacionados al listado de tarjetas y empresas de proveedores de estas mismas para el
 * modulo de iris o cualquier otro sistema que lo use.
 * @author AASTORGA
 */

@Log4j2
@RestController
@RequiredArgsConstructor
@Validated
public class TarjetaWS {
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private final TarjetasLogic tarjetasLogic;

	/**
	 * Servicio para obtener todo un listado de tarjetas registradas en el stock filtrados por la empresa a la que estén asociadas
	 * @param empresaId Id de la empresa obtenido del WS obtenEmpresaCombo
	 * @return Regresa el resultado de la operacion serializado en un json
	 */
	@RequestMapping(value = "/listadoTarjetas", method = RequestMethod.GET)
	public ResponseEntity<String> consultaStockTarjetas(@RequestParam(name= "empresaId",required = false)
															Integer empresaId) {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		

		if (!authenticate.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if(empresaId == null)
		{
			resp.setCodigo(2);
			resp.setMensaje("El campo empresaId es obligatorio");
			return new ResponseEntity<>(gson.toJson(resp), HttpStatus.BAD_REQUEST);
		}

		resp = tarjetasLogic.obtenerListadoTarjetas(empresaId);
		jsonResponse = gson.toJson(resp);
		if(resp.getCodigo() == -1)
			return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * Servicio para proporcionar el listado de empresas para tarjetas fisicas registradas en nuestros catalogos
	 * @return Regresa el resultado de la operacion serializado en un json
	 */
	@RequestMapping(value = "/comboEmpresa", method = RequestMethod.GET)
	public ResponseEntity<String> obtenEmpresaCombo() {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = new Gson();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		Respuesta resp = tarjetasLogic.obtenerEmpresaCombo();
		jsonResponse = gson.toJson(resp);
		log.info("obtenEmpresaCombo:: Resultado: {}",jsonResponse);
		if(resp.getCodigo() == -1)
			return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
}
