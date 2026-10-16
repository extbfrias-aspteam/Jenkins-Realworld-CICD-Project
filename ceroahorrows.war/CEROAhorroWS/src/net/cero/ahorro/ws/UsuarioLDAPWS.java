package net.cero.ahorro.ws;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.cero.ahorro.logica.CuentaAspLogic;
import net.cero.ahorro.logica.UsuarioLDAPLogic;
import net.cero.data.CambioContrasenaRequest;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

@RestController
public class UsuarioLDAPWS {
	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@PostMapping("/cambioContrasenaIRIS")
	public ResponseEntity<Respuesta> cambioContrasena(@RequestBody @Valid CambioContrasenaRequest cambioContrasenaRequest, BindingResult validacion){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}

		if(validacion.hasErrors()) {
			respuesta = new Respuesta();
			respuesta.setCodigo(1);
			respuesta.setMensaje(validacion.getFieldError().getDefaultMessage());
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}else {
			UsuarioLDAPLogic logic= new UsuarioLDAPLogic();
			return ResponseEntity.ok(logic.cambioContrasenaLDAP(cambioContrasenaRequest));
		}
	}
}
