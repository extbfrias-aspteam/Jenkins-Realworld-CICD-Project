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
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.dao.TransaccionesStdDAO;
import net.std.data.CuentaOBJ;
import net.std.request.CanalesReq;
import net.std.request.CuentaCanalReq;


@Controller
public class PermisoCanalTrxCuentaStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(PermisoCanalTrxCuentaStdSvc.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static TransaccionesStdDAO dao = null;
	private static AhorroStdDAO daoAho = null;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (TransaccionesStdDAO)s.getApplicationContext().getBean("TransaccionesStdDAO");
			daoAho = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}
	
	@RequestMapping(value="/permisoCanalTrxStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> mapResultado = new HashMap<>();
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		CuentaCanalReq req = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null || daoAho == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoAho == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			req = new Gson().fromJson(json, CuentaCanalReq.class);
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEPOSITO_AHORRO), "TRX_DEPOSITO_AHORRO: " + Comun._T(req.getCuentaClabe()))){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		try{
			/* OBTIENE LOS DATOS COMPLETOS DE LA CUENTA */
			RespuestaSVC respCtaDep = daoAho.leerCuentaAhorroClabeDao(Comun._TX(req.getCuentaClabe()));
			if(respCtaDep.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA, req.getCuentaClabe()), HttpStatus.FORBIDDEN);
				return response;
			}
			
			CuentaOBJ cta = (CuentaOBJ) respCtaDep.getBody().getValor("CUENTA");
			CanalesReq canal = new CanalesReq();
			
			/* VERIFICA QUE LA CUENTA NO ESTE VIGENTE */
			if("VIG".equals(Comun._T(cta.getEstatus()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_NO_ACTIVADA, String.format("%s - %s", cta.getClabeInterbancaria(), Comun._T(cta.getEstatus()))), HttpStatus.FORBIDDEN);
				return response;
			}
			
			/* VERIFICA QUE LA CUENTA NO ESTE BLOQUEADA */
			if("BLOQUEADO".equals(Comun._T(cta.getBloqueado()))){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_BLOQUEADA, String.format("%s - %s", cta.getClabeInterbancaria(), Comun._T(cta.getBloqueado()))), HttpStatus.FORBIDDEN);
				return response;
			}
			
			if("CUENTA_FINAL".equals(cta.getTipoCliente())){
				canal.setCuenta(cta.getCuenta());
				canal.setAplicativo_clave(Comun._T(Constantes.APLICATIVO_BLU_FINAL));
				//canal.setTransaccion_clave(Comun._T(Constantes.TRX_CUENTA_FINAL));
				canal.setTransaccion_clave(Comun._T(Constantes.TRX_CUENTA_BLU));
				canal.setStatus("BLOQUEA_TRX".equals(Comun._T(req.getPermiso())) ? true : false);
				canal.setUsuario_id(Comun._I(Constantes.USUARIO_ID));
			}else{
				canal.setCuenta(cta.getCuenta());
				canal.setAplicativo_clave(Comun._T(Constantes.APLICATIVO_BLU));
				canal.setTransaccion_clave(Comun._T(Constantes.TRX_CUENTA_BLU));
				canal.setStatus("BLOQUEA_TRX".equals(Comun._T(req.getPermiso())) ? true : false);
				canal.setUsuario_id(Comun._I(Constantes.USUARIO_ID));
			}
			
			RespuestaSVC respPermiso = dao.leerCanalStdDao(cta.getCuenta(), canal.getAplicativo_clave(), canal.getTransaccion_clave()); 
			if(respPermiso.getErrores().getCodigoError() == 0){
				RespuestaSVC respCanal = dao.actualizarCanalStdDao(canal);
				mapResultado.put("ESTATUS",  respCanal.getErrores().getCodigoError() == 0 ? "OK" : respCanal.getErrores().getDescError());
			}else{
				RespuestaSVC respCanal = dao.insertarCanalStdDao(canal);
				mapResultado.put("ESTATUS",  respCanal.getErrores().getCodigoError() == 0 ? "OK" : respCanal.getErrores().getDescError());
			}
			
			mapResultado.put("CUENTA", cta.getCuenta());
			mapResultado.put("CLABE",  cta.getClabeInterbancaria());
			mapResultado.put("PERMISO", req.getPermiso());
			
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}

	
	private String validaParams(CuentaCanalReq obj){
		String valida = null;
		if(obj == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN PARAMETROS");
		if("".equals(Comun._T(obj.getCuentaClabe()))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS DE LA CUENTA");
		if("".equals(Comun._T(obj.getPermiso()))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATOS DEL PERMISO");
		if(!("BLOQUEA_TRX".equals(Comun._T(obj.getPermiso())) || "DESBLOQUEA_TRX".equals(Comun._T(obj.getPermiso())))) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DATO VALIDO DEL PERMISO");
		return valida;
	}
	
	
}

