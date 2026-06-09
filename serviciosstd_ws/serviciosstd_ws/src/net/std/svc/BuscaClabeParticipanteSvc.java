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
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.dao.AhorroProcreaStdDAO;
import net.std.dao.AhorroStdDAO;
import net.std.dao.CuentasReferenciadasStdDAO;
import net.std.data.CuentaReferenciadaOBJ;

@Controller
public class BuscaClabeParticipanteSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(BuscaClabeParticipanteSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static AhorroProcreaStdDAO daoProcrea = null;
	private static CuentasReferenciadasStdDAO daoRef = null; 

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoProcrea = (AhorroProcreaStdDAO)s.getApplicationContext().getBean("AhorroProcreaStdDAO");
			daoRef = (CuentasReferenciadasStdDAO)s.getApplicationContext().getBean("CuentasReferenciadasStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/buscaClabeParticipanteStd", method=RequestMethod.POST)
	public ResponseEntity<String> buscaClabeParticipanteStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		log.info(String.format("/buscaClabeParticipanteStd :: %s", json));

		try{
			if(dao == null || daoProcrea == null || daoRef == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoProcrea == null || daoRef == null){
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
			RespuestaSVC respuestaSvc = null;
			String tipoCuenta = map.get("tipoCuenta");

			respuestaSvc = dao.participanteSpeiDao(Comun._TX(map.get("clabe")), null);
			if(respuestaSvc.getErrores().getCodigoError() == 0){
				response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}else{
				/* NO SE ENCONTRO, BUSCA EN REFERENCIA */
				//RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(map.get("clabe")), Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA));
				RespuestaSVC respRef = daoRef.leerCuentaReferenciadaStdDao(Comun._TX(map.get("clabe")), 
						                                                   "".equals(Comun._T(tipoCuenta)) ? Comun._TX(Constantes.TIPO_CUENTA_REFERENCIADA) : Comun._T(tipoCuenta));
				if(respRef.getErrores().getCodigoError() == 0){
					CuentaReferenciadaOBJ obj = (CuentaReferenciadaOBJ)respRef.getBody().getValor("CUENTA");
					respuestaSvc = dao.participanteSpeiDao(Comun._TX(obj.getClabe_interbancaria()), null);
					if(respuestaSvc.getErrores().getCodigoError() == 0){
						response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
					}else{
						RespuestaSVC respuestaPr = daoProcrea.ahorroClabePrStdDao(Comun._T(map.get("clabe")));
						if(respuestaPr.getErrores().getCodigoError() == 0){
							response = new ResponseEntity<>(new Gson().toJson(respuestaPr), HttpStatus.OK);
						}else{
							response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
						}
					}
				}else{
					RespuestaSVC respuestaPr = daoProcrea.ahorroClabePrStdDao(Comun._T(map.get("clabe")));
					if(respuestaPr.getErrores().getCodigoError() == 0){
						response = new ResponseEntity<>(new Gson().toJson(respuestaPr), HttpStatus.OK);
					}else{
						response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		return response;
	}
}
