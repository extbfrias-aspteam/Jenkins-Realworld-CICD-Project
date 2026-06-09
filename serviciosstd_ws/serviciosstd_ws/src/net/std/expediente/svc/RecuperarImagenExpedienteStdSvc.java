package net.std.expediente.svc;

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
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.request.ImagenExpedientesReq;
import net.std.servicios.ProcesoRecuperarImagen;

@Controller
public class RecuperarImagenExpedienteStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(RecuperarImagenExpedienteStdSvc.class);
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value="/imagenExpedienteStd", method=RequestMethod.POST)
	public ResponseEntity<String> imagenExpedienteStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		ImagenExpedientesReq req = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			req = new Gson().fromJson(json, ImagenExpedientesReq.class);
			String valida = validaParams(req);
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_LEER_EXPEDIENTE), "TRX_LEER_EXPEDIENTE: ")){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			
			/* EJECUTA LA ACCION SUBIDA DE IMAGEN DE ALFRESCO */
			RespuestaSVC respAlfresco = ProcesoRecuperarImagen.procesar(req.getAlfresco_id());
			if(respAlfresco.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respAlfresco.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				return response;
			}
			response = new ResponseEntity<>(Comun._T(respAlfresco.getBody().getValor("IMAGEN")), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	

	
	private String validaParams(ImagenExpedientesReq req){
		String valida = null;

		if(req == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(req.getAlfresco_id() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA ID DE RESPOSITORIO DE LA IMAGEN SOLICITADA");
		
		return valida;
	}
}	
	

