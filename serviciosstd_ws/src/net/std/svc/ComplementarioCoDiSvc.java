package net.std.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
import net.std.dao.AhorroStdDAO;
import net.std.dao.SolicitanteStdDAO;

@Controller
public class ComplementarioCoDiSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ComplementarioCoDiSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static SolicitanteStdDAO sodao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			sodao = (SolicitanteStdDAO)s.getApplicationContext().getBean("SolicitanteStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/complementarioCoDiStd", method=RequestMethod.POST)
	public ResponseEntity<String> complementarioCoDiStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		log.info(String.format("/complementarioCoDiStd :: %s", json));

		try{
			if(dao == null || sodao == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || sodao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}

			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			if(map == null || map.get("clabe") == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "clabe"), HttpStatus.FORBIDDEN);
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
			RespuestaSVC respCodi = dao.complementarioCoDiDao(Comun._T(map.get("clabe")));
			if(respCodi.getErrores().getCodigoError() == 0){
				try{
					Map<String, String> mapCodi = ((List<Map<String,String>>)respCodi.getBody().getValor("CUENTA")).get(0);
					RespuestaSVC respSol = sodao.solicitanteCoDiDao(Comun._T(mapCodi.get("PERSONA_ID")));
					if(respSol.getErrores().getCodigoError() == 0){
						Map<String, String> mapPersona = ((List<Map<String,String>>)respSol.getBody().getValor("CUENTA")).get(0);

						RespuestaSVC respuestaSvc = new RespuestaSVC();
						respuestaSvc.getBody().addValor("PERSONA_ID", Comun._T(mapCodi.get("PERSONA_ID")));
						respuestaSvc.getBody().addValor("NOMBRE", Comun._T(mapPersona.get("NOMBRE")));
						respuestaSvc.getBody().addValor("RFC", Comun._T(mapPersona.get("RFC")));
						response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
					}else{
						response = new ResponseEntity<>(respSol.getErrores().getDescError(), HttpStatus.NOT_FOUND);
					}
				}catch(Exception ex){
					ex.printStackTrace();
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
				}
			}else{
				/* BUSCA COMO CUENTA REFERENCIADA */
				respCodi = dao.complementarioCoDiReferenciadaDao(Comun._T(map.get("clabe")));
				if(respCodi.getErrores().getCodigoError() == 0){
					Map<String, String> mapPersona = ((List<Map<String,String>>)respCodi.getBody().getValor("CUENTA")).get(0);

					RespuestaSVC respuestaSvc = new RespuestaSVC();
					respuestaSvc.getBody().addValor("PERSONA_ID", Comun._T(mapPersona.get("PERSONA_ID")));
					respuestaSvc.getBody().addValor("NOMBRE", Comun._T(mapPersona.get("NOMBRE")));
					respuestaSvc.getBody().addValor("RFC", Comun._T(mapPersona.get("RFC")));
					response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}else{
					response = new ResponseEntity<>(respCodi.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}

/*

try{
RespuestaSVC respCodi = dao.complementarioCoDiDao(Comun._T(map.get("clabe")));
if(respCodi.getErrores().getCodigoError() == 0){
	try{
		String personaId = ((List<Map<String,String>>)respCodi.getBody().getValor("CUENTA")).get(0).get("PERSONA_ID");
		RespuestaSVC respSol = sodao.solicitanteCoDiDao(personaId);
		if(respSol.getErrores().getCodigoError() == 0){
			Map<String, String> mapPersona = ((List<Map<String,String>>)respCodi.getBody().getValor("CUENTA")).get(0);
			mapPersona.putAll(((List<Map<String,String>>)respSol.getBody().getValor("CUENTA")).get(0));

			RespuestaSVC respuestaSvc = new RespuestaSVC();
			respuestaSvc.getBody().addValor("CUENTA", mapPersona);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}else{
			response = new ResponseEntity<>(respSol.getErrores().getDescError(), HttpStatus.NOT_FOUND);
		}
	}catch(Exception ex){
		ex.printStackTrace();
		response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
	}
}else{
	response = new ResponseEntity<>(respCodi.getErrores().getDescError(), HttpStatus.NOT_FOUND);
}
}catch(Exception ex){
ex.printStackTrace();
response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
}

 */