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

@Controller
public class ParticipanteSpeiSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ParticipanteSpeiSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/participanteSpeiStd", method=RequestMethod.POST)
	public ResponseEntity<String> participanteSpeiStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		log.info(String.format("/participanteSpeiStd :: %s", json));
		
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			if(map == null || map.get("cuentaParticipante") == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "cuentaParticipante|dimension"), HttpStatus.FORBIDDEN);
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
			RespuestaSVC respuestaSvc = dao.participanteSpeiDao(Comun._T(map.get("cuentaParticipante")), null);
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				return response;
			}
			
			List<Map<String,String>> list = (List<Map<String,String>>)respuestaSvc.getBody().getValor("CUENTA");
			String cuenta_eje = Comun._T(list.get(0).get("CUENTA_EJE"));
			String bloqueado = Comun._T(list.get(0).get("BLOQUEADO"));
			String tipoCliente  = Comun._T(list.get(0).get("TIPO_CLIENTE"));
			
			if("CUENTA_BLU".equals(tipoCliente)){
				if(!"".equals(cuenta_eje)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_PARTICIPANTE_ASOCIADO, Errores.desc(Errores.ERROR_CUENTA_PARTICIPANTE_ASOCIADO, Comun._T(map.get("cuentaParticipante"))));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
				if("BLOQUEADO".equals(bloqueado)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_BLOQUEADA, Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, Comun._T(map.get("cuentaParticipante"))));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
			}
			
			if("CUENTA_FINAL".equals(tipoCliente)){
				if("".equals(cuenta_eje)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_SIN_CUENTA_PADRE, Errores.desc(Errores.ERROR_CUENTA_SIN_CUENTA_PADRE, Comun._T(map.get("cuentaParticipante"))));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
				if("BLOQUEADO".equals(bloqueado)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_BLOQUEADA, Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, Comun._T(map.get("cuentaParticipante"))));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
				RespuestaSVC respEje = dao.participanteSpeiDao(cuenta_eje, null);
				if(respEje.getErrores().getCodigoError() != 0){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_PARTICIPANTE_NO_EXISTE, Errores.desc(Errores.ERROR_CUENTA_PARTICIPANTE_NO_EXISTE, Comun._T(map.get("cuentaParticipante"))));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
				List<Map<String,String>> listEje = (List<Map<String,String>>)respEje.getBody().getValor("CUENTA");
				String bloqueadoEje = Comun._T(listEje.get(0).get("BLOQUEADO"));
				String tipoClienteEje  = Comun._T(listEje.get(0).get("TIPO_CLIENTE"));
				
				if("BLOQUEADO".equals(bloqueadoEje)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_PADRE_BLOQUEADA, Errores.desc(Errores.ERROR_CUENTA_PADRE_BLOQUEADA, cuenta_eje));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
				if("CUENTA_FINAL".equals(tipoClienteEje)){
					respuestaSvc.getErrores().addCodigo(null, Errores.ERROR_CUENTA_PARTICIPANTE_MAL_IDENTIFICADA, Errores.desc(Errores.ERROR_CUENTA_PARTICIPANTE_MAL_IDENTIFICADA, cuenta_eje));
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.FORBIDDEN);
					return response;
				}
				
			}
				
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
}
