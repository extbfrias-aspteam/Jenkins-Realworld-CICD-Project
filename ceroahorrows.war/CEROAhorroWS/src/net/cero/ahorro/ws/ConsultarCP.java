package net.cero.ahorro.ws;


import com.google.gson.Gson;
import net.cero.ahorro.logica.ConsultaCPLogic;
import net.cero.data.BuscarColoniasPorCPReq;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.spring.config.IPAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@Controller
public class ConsultarCP {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	private static final Logger log = LogManager.getLogger(ConsultarCP.class);
	
	private String msgValidacion = "";

	@PostMapping(value = "/consultarCP", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> consultarCP(@RequestBody String json) {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		String respuesta = "";
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try {

			BuscarColoniasPorCPReq req = gson.fromJson(json, BuscarColoniasPorCPReq.class);
			log.error("Ejecutando consulta de cp" + req);
			ConsultaCPLogic logic = new ConsultaCPLogic();
			resp = logic.obtenerDatosColoniaByCp(req.getCp());

		} catch (Exception e) {
			log.error("Error al ejecutar consultarCP" + e.getMessage());
			e.printStackTrace();
			resp.setCodigo(-1);
			resp.setMensaje("Error en servidor");
		}
		log.info("Respuesta consultaCP: " + respuesta);
		return new ResponseEntity<>(gson.toJson(resp), HttpStatus.OK);

	}

}

