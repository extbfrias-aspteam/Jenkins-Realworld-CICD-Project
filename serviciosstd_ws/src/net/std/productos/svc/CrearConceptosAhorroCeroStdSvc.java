package net.std.productos.svc;

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
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.productos.dao.ProductosAhorroCeroStdDAO;

@Controller
public class CrearConceptosAhorroCeroStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CrearConceptosAhorroCeroStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static ProductosAhorroCeroStdDAO dao = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (ProductosAhorroCeroStdDAO)s.getApplicationContext().getBean("ProductosAhorroCeroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/crearConceptosAhorroCeroStdSvc", method=RequestMethod.POST)
	public ResponseEntity<String> crearConceptosAhorroCeroStdSvc(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			String valida = validaParams(map);
			if(valida != null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, valida), HttpStatus.FORBIDDEN);
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CREAR_CONCEPTOS_AHORRO), "TRX_CREAR_CONCEPTOS_AHORRO: ")){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		
		try{
			RespuestaSVC respModulo = dao.leerModulosStdDao(Comun._T(map.get("MODULO")));
			if(respModulo.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respModulo.getErrores().getDescError(), HttpStatus.NOT_FOUND);
			}else{
				map.put("MODULO_ID", Comun._T(respModulo.getBody().getValor("ID")));
				RespuestaSVC respuestaSvc = dao.crearConceptosAhorroCeroStdDao(map);
				if(respuestaSvc.getErrores().getCodigoError() == 0){
					response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
				}else{
					response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	private String validaParams(Map<String, String> map){
		String valida = null;
		if(map == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if("".equals(Comun._T(map.get("CLAVE")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CLAVE");
		if("".equals(Comun._T(map.get("DESCRIPCION")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "DESCRIPCION");
		if("".equals(Comun._T(map.get("MODULO")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MODULO");
		if("".equals(Comun._T(map.get("USUARIO_ID")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "USUARIO_ID");
		
		return valida;
	}
}

