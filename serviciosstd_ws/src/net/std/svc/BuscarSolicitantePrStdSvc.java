package net.std.svc;

import java.io.Serializable;
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

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Errores;
import net.std.dao.SolicitanteStdDAO;
import net.std.data.SolicitanteOBJ;

@Controller
public class BuscarSolicitantePrStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(BuscarSolicitantePrStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static SolicitanteStdDAO dao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/buscarSolicitantePrStdSvc", method=RequestMethod.POST)
	public ResponseEntity<String> buscarSolicitantePrStdSvc(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		SolicitanteOBJ solicitante = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			solicitante = new Gson().fromJson(json, SolicitanteOBJ.class);
			if(solicitante == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "clabe"), HttpStatus.FORBIDDEN);
				return response;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}
		
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}
	
		try{
			RespuestaSVC respSol = dao.BuscarSolicitanteDao(solicitante);
			if(respSol.getErrores().getCodigoError() == 0){
				response = new ResponseEntity<>(new Gson().toJson(respSol), HttpStatus.OK);
			}else{
				response = new ResponseEntity<>(respSol.getErrores().getDescError(), HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		return response;
	}
}

