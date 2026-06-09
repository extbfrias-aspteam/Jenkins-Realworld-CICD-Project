package net.std.catalogos.svc;

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
import net.std.constantes.BitLogger;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.CatalogoProcreaStdDAO;

@Controller
public class CatStdCodigoPostalSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CatStdCodigoPostalSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static CatalogoProcreaStdDAO dao = null;
	
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (CatalogoProcreaStdDAO)s.getApplicationContext().getBean("CatalogoProcreaStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/catPrCodigoPostalStd", method=RequestMethod.POST)
	public ResponseEntity<String> catPrCodigoPostalStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		
		BitLogger.info(null, "ENTRADA", String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			if(map == null || "".equals(Comun._T(map.get("codigoPostal")))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS), HttpStatus.FORBIDDEN);
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
		
		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_LEER_CATALOGOS), "TRX_LEER_CATALOGOS: ")){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			String cp = Comun._T(map.get("codigoPostal"));
			RespuestaSVC respuesta = dao.getCodigoPostalPrStdDao(cp);
			if(respuesta.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CATALOGO, "COLONIAS POR CODIGO POSGTAL"), HttpStatus.NOT_FOUND);
				return response;
			}
			response = new ResponseEntity<>(new Gson().toJson(respuesta), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		//BitLogger.info(null, "SALIDA", String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	
}

