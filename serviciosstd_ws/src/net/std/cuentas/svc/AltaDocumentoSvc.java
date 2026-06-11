package net.std.cuentas.svc;

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
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.request.AltaDocumentoReq;
import net.std.sftp.SFTPLogic;


@Controller
public class AltaDocumentoSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AltaDocumentoSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	
	@RequestMapping(value="/altaDocumento", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		AltaDocumentoReq req = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{			
			req = new Gson().fromJson(json, AltaDocumentoReq.class);
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

		try{
			respuestaSvc = SFTPLogic.procesar(req);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	
	private String validaParams(AltaDocumentoReq obj){
		String valida = null;
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if("".equals(Comun._T(obj.getDocumento()))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATO DEL DOCUMENTO A SUBIR");
		if("".equals(Comun._T(obj.getNombreArchivo()))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATO DEL NOMBRE DEL ARCHIVO");
		if("".equals(Comun._T(obj.getRutaArchivo()))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATO DE LA RUTA DEL ARCHIVO");
		return valida;
	}
	
	
}

