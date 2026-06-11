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
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.expediente.dao.ExpedienteStdDAO;
import net.std.request.ExpedienteVerificaReq;

@Controller
public class VerificaExpedienteCuentaStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(VerificaExpedienteCuentaStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static ExpedienteStdDAO dao = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (ExpedienteStdDAO)s.getApplicationContext().getBean("ExpedienteStdDAO");
		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/verificarExpedientesCuentaStd", method=RequestMethod.POST)
	public ResponseEntity<String> verificarExpedientesCuentaStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		ExpedienteVerificaReq req = null;

		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null) initialized();

			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}

			req = new Gson().fromJson(json, ExpedienteVerificaReq.class);
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
			RespuestaSVC respuesta = dao.verificarExpedienteEstatusStdDao(Comun._T(req.getObservaciones()), 
																		  req.getVerificado(), 
					                                                      req.getFechaVerificado(), 
					                                                      Comun._I(req.getId()), 
					                                                      Comun._I(req.getCuentaId()));
			if(respuesta.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(respuesta.getErrores().getDescError(), HttpStatus.NOT_FOUND);
				return response;
			}
			response = new ResponseEntity<>(new Gson().toJson(respuesta.getBody().getValor("VERIFICA")), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}

		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	private String validaParams(ExpedienteVerificaReq req){
		String valida = null;

		if(req == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(req.getCuentaId() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA ID DE LA CUENTA");
		if(req.getId() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA ID DEL DOCUMENTO");
		return valida;
	}
}

