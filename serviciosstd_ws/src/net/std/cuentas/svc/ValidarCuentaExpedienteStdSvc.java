package net.std.cuentas.svc;

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

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
//import net.std.constantes.Respuesta;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.request.ValidarCuentaExpedienteReq;
import net.std.servicios.ProcesoBitLogger;

@Controller
public class ValidarCuentaExpedienteStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(ValidarCuentaExpedienteStdSvc.class);
	
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

	@SuppressWarnings("unused")
	@RequestMapping(value="/validarCuentaExpedienteStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		ValidarCuentaExpedienteReq req = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			req = new Gson().fromJson(json, ValidarCuentaExpedienteReq.class);
			String valida = validaParams(req);
			if(valida != null){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_CAMPOS_REQUERIDOS, valida), HttpStatus.FORBIDDEN);
				return response;
			}
		}catch(Exception ex){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
			return response;
		}

		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_SIN_PERMISO), HttpStatus.UNAUTHORIZED);
			return response;
		}
		
		/* VERIFICA PERMISOS Y ESCRIBE A LA BITACORA */
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CAMBIO_ESTADO_CUENTA), "TRX_CAMBIO_ESTADO_CUENTA: " + Comun._T(req.getCuenta()))){
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			
			ProcesoBitLogger.procesar(Constantes.PROCESO, req.getConcepto(), json);
			
			RespuestaSVC respCuenta = dao.validarCuentaExpedienteStdDao(req);
			if(respCuenta.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_VALIDAR_CUENTA), HttpStatus.FORBIDDEN);
				return response;
			}
			
			respuestaSvc.getBody().addValor("RESULTADO", "CUENTA EXPEDIENTE VALIDADO");
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.descGson(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	private String validaParams(ValidarCuentaExpedienteReq req){
		String valida = null;
		if(req == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if(req.getCuentaId() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS CUENTA ID");
		if(req.getCuenta() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS CUENTA");
		if(req.getEstado() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS CAMBIO ESTADO");
		if(req.getValidar() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS VALIDAR");
		
		return valida;
	}
}

