package net.cero.ahorro.ws;

import java.util.ArrayList;
import java.util.Map;

import net.cero.ahorro.servicios.CodiService;
import net.cero.data.ConsultaOperacionesCodiReqDTO;
import net.cero.data.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import net.cero.ahorro.logica.SpeiLogic;
import net.cero.data.RespuestaDataList;
import net.cero.spring.config.IPAuthenticationProvider;

import javax.validation.Valid;

@RestController
public class SpeiWs {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	@Autowired
	private CodiService codiService;
	
	@PostMapping(value = "/consultaSpei")
	public ResponseEntity<RespuestaDataList> consultaSpei(@RequestBody Map<String, String> json) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		//Respuesta respuesta = new Respuesta();
		RespuestaDataList respuesta = new RespuestaDataList();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		Gson gson = new Gson();
		
		String res;

		if (!authenticate.isAuthenticated()) {
			respuesta.setCodigo(4);
			respuesta.setData(new ArrayList<Object>());
			respuesta.setMensaje("No autorizado");
			return ResponseEntity.ok(respuesta);
		}else {
			SpeiLogic logic = new SpeiLogic();
			return ResponseEntity.ok(logic.consultaSpei(json));
		}

	}

	@PostMapping(value = "/consultaOperacionesCODI")
	public ResponseEntity<Respuesta> consultaOperacionesCODI(@RequestBody @Valid ConsultaOperacionesCodiReqDTO consultaOperacionesCodiDTO, BindingResult bindingResult){
		return ResponseEntity.ok(codiService.consultaOperacionesCodi(consultaOperacionesCodiDTO, bindingResult));
	}
}
