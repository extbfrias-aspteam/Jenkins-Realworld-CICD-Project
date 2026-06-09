package net.std.svc;

import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.Errores;
import net.std.dao.PlazasClabeStdDAO;
import net.std.data.PlazasClabeOBJ;

@Controller
public class PlazaClabeDisponibleSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(PlazaClabeDisponibleSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static PlazasClabeStdDAO dao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (PlazasClabeStdDAO)s.getApplicationContext().getBean("PlazasClabeStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/plazaClabeDisponibleStd", method=RequestMethod.GET)
	public ResponseEntity<String> procesar(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				return new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
			}
		}catch(Exception ex){
			return new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			return new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
		}

		try{
			/* LEER PLAZAS PROPIAS */
			RespuestaSVC respPlazasPropias = dao.listPlazasPropiasClabeStdDao();
			if(respPlazasPropias.getErrores().getCodigoError() != 0){
				return new ResponseEntity<>(respPlazasPropias.getErrores().getDescError(), HttpStatus.FORBIDDEN);
			}
			
			RespuestaSVC respPlazas = dao.listPlazasClabeStdDao();
			if(respPlazas.getErrores().getCodigoError() != 0){
				//response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_CREAR_CUENTA), HttpStatus.FORBIDDEN);
				//return response;
				return new ResponseEntity<>(respPlazas.getErrores().getDescError(), HttpStatus.FORBIDDEN);
			}
			
			List<PlazasClabeOBJ> lstPlazas = (List<PlazasClabeOBJ>)respPlazas.getBody().getValor("PLAZAS");
			List<PlazasClabeOBJ> lstPlazasPropias = (List<PlazasClabeOBJ>)respPlazasPropias.getBody().getValor("PLAZAS");
			
			if(lstPlazas == null || lstPlazas.size() == 0){
				return new ResponseEntity<>(Errores.desc(Errores.ERROR_LEER_PLAZAS), HttpStatus.NOT_FOUND);
			}
			
			PlazasClabeOBJ plazaObj = plazaDisponible(lstPlazas, lstPlazasPropias);
			if(plazaObj != null){
				/* MARCA LA PLAZA COMO NO DISPONIBLE */
				plazaObj.setDisponible(Comun._T(Constantes.PLAZA_DISPONIBLE)); // 1 = NO DISPONIBLE , 0 = DISPONIBLE
				plazaObj.setUsuarioId(Comun._T(Constantes.USUARIO_ID));
				RespuestaSVC respActualiza = dao.actualizarPlazasClabeStdDao(plazaObj);
				if(respActualiza.getErrores().getCodigoError() == 0L){
					response = new ResponseEntity<>(new Gson().toJson(plazaObj), HttpStatus.OK);
				}else{
					response = new ResponseEntity<>(Errores.desc(Errores.ERROR_ACTUALIZAR_PLAZAS), HttpStatus.FORBIDDEN);
				}
			}else{
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_LEER_PLAZAS), HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	private PlazasClabeOBJ plazaDisponible(final List<PlazasClabeOBJ> lstPlazas, final List<PlazasClabeOBJ> lstPlazasPropias){
		PlazasClabeOBJ plzDisponible = null;
		for(PlazasClabeOBJ pza : lstPlazas){
			PlazasClabeOBJ plzTmp = lstPlazasPropias.stream()
					.filter( plazas -> pza.getClave().equals(plazas.getClave()))
					.findAny()
					.orElse(null);
			
			if(plzTmp == null){
				plzDisponible = pza;
				break;
			}
		}
		
		return plzDisponible;
	}
}
		

	