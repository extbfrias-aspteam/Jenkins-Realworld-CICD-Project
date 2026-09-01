package net.cero.ahorro.ws;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.cero.ahorro.logica.CuentaAspLogic;
import net.cero.data.BloqueoDesbloqueoCuentaDTO;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;

import javax.validation.Valid;

@RestController
public class CuentaAspWs {
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value = "/bloqueoCuenta", method = RequestMethod.POST)
	public ResponseEntity<Respuesta> bloqueoCuentaAsp(@RequestBody @Valid BloqueoDesbloqueoCuentaDTO body, BindingResult validacion) {
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
			CuentaAspLogic logic = new CuentaAspLogic();
			respuesta = logic.bloqueoDesbloqueo("bloqueo", body);
		}
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/desbloqueoCuenta", method = RequestMethod.POST)
	public ResponseEntity<Respuesta> desbloqueoCuentaAsp(@RequestBody @Valid BloqueoDesbloqueoCuentaDTO body,BindingResult validacion) {
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
			CuentaAspLogic logic = new CuentaAspLogic();
			respuesta = logic.bloqueoDesbloqueo("desbloqueo", body);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/catalogoNivelCuenta", method = RequestMethod.GET)
	public Respuesta catologoNivelCuenta() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}
		CuentaAspLogic logic = new CuentaAspLogic();
		respuesta = logic.catalogoNivelCuenta();
		
		return respuesta;
	}
	
	@RequestMapping(value = "/cambioNivelCuenta", method = RequestMethod.POST)
	public Respuesta cambioNivelCuenta(@RequestBody Map<String, String> json) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}
		CuentaAspLogic logic = new CuentaAspLogic();
		respuesta = logic.cambioNivelCuenta(json);
		
		return respuesta;
	}
	
	@RequestMapping(value = "/productos", method = RequestMethod.GET)
	public Respuesta consultaProductos() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}else {
			CuentaAspLogic logic = new CuentaAspLogic();
			logic.consultaProductos(respuesta);	
		}
		
		return respuesta;
	}
	
	@RequestMapping(value = "/dashboardCuentas", method = RequestMethod.POST)
	public ResponseEntity<Respuesta> dashboardCuentas(@RequestBody Map<String, Object> anio) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		Respuesta respuesta = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData("");
			respuesta.setMensaje("No autorizado");
		}else {
			CuentaAspLogic logic = new CuentaAspLogic();
			respuesta = logic.dashboardCuentas(anio);	
		}
		
		return ResponseEntity.ok(respuesta);
	}

}
