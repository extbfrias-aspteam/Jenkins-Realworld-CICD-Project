package net.std.productos.svc;

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
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.productos.dao.CatalogosProdAhorroCeroStdDAO;
import net.std.productos.dao.ProductosAhorroCeroStdDAO;

@Controller
public class LeerDocumentosCeroStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(LeerDocumentosCeroStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static ProductosAhorroCeroStdDAO dao = null;
	private static CatalogosProdAhorroCeroStdDAO daoCat = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (ProductosAhorroCeroStdDAO)s.getApplicationContext().getBean("ProductosAhorroCeroStdDAO");
			daoCat = (CatalogosProdAhorroCeroStdDAO)s.getApplicationContext().getBean("CatalogosProdAhorroCeroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings({"unchecked" })
	@RequestMapping(value="/leerDocumentosCeroStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null || daoCat == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoCat == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
			if(map == null || "".equals(Comun._T(map.get("CLAVE")))){
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_LEER_DOCUMENTOS), "TRX_LEER_DOCUMENTOS: " + Comun._T(map.get("CLAVE")))){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			RespuestaSVC respEstatus = daoCat.claveValorStdDao("ESTATUS_ID", Comun._T(Constantes.ALTA_ID));
			Integer estatusID = respEstatus.getErrores().getCodigoError() == 0 ? Comun._I(respEstatus.getBody().getValor("ID")) : 0;
			
			RespuestaSVC respProd = dao.leerProductosCeroStdDao(Comun._T(map.get("CLAVE")), Comun._T(Constantes.ALTA_ID));
			if(respProd.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respProd.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				return response;
			}
			
			Integer productoID = Comun._I(respProd.getBody().getValor("ID"));
			RespuestaSVC respDoctos = dao.leerDocumentosStdDao(productoID, estatusID);
			if(respDoctos.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respDoctos.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				return response;
			}
			
			List<Map<String, String>> dato = (List<Map<String, String>> )respDoctos.getBody().getValor("DOCUMENTOS");
			response = new ResponseEntity<>(new Gson().toJson(dato), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	
}

