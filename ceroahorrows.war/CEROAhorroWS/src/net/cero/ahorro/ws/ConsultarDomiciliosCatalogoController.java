package net.cero.ahorro.ws;


import com.google.gson.Gson;
import net.cero.ahorro.logica.ConsultaDomiciliosCatalogLogic;
import net.cero.data.Respuesta;
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

@Controller
@RequestMapping("/domiciliosCatalog")
public class ConsultarDomiciliosCatalogoController {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;

	private static final Logger log = LogManager.getLogger(ConsultarDomiciliosCatalogoController.class);


	@GetMapping(value = "/getDomiciliosCatalog", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> consultarCP() {
		log.info("Consultando catalogo ...: " );
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if (!authenticate.isAuthenticated()) {
			response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return response;
		}

		try {
			ConsultaDomiciliosCatalogLogic logic = new ConsultaDomiciliosCatalogLogic();
			resp = logic.consultaDomiciliosCatalog();

		} catch (Exception e) {
			log.error("Error al ejecutar consultaDomiciliosCatalog" + e.getMessage());
			e.printStackTrace();
			resp.setCodigo(-1);
			resp.setMensaje("Error en servidor");
		}
		log.info("Respuesta consulta Catalogo Domicilios: " + resp);
		return new ResponseEntity<>(gson.toJson(resp), HttpStatus.OK);

	}

}

