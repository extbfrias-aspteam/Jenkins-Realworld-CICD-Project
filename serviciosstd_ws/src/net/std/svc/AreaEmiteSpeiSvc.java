package net.std.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Errores;
import net.std.dao.BancosStdDAO;

@Controller
public class AreaEmiteSpeiSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AreaEmiteSpeiSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static BancosStdDAO dao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (BancosStdDAO)s.getApplicationContext().getBean("BancosStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/areaEmiteSpei", method=RequestMethod.POST)
	public ResponseEntity<String> areaEmiteSpei(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		log.info(String.format("/areaEmiteSpei :: %s", json));
		
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			if(map == null || map.get("valor") == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "valor"), HttpStatus.FORBIDDEN);
				return response;
			}
		}catch(Exception ex){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}
		
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}

		try{
			RespuestaSVC respuestaSvc = dao.leerAreaEmiteSpeiDao(Comun._T(map.get("valor")));
			if(respuestaSvc.getErrores().getCodigoError() == 0){
				response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}else{
				response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
}		
		
