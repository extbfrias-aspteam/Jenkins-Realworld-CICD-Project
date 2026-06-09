package net.std.procrea.svc;

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
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroProcreaStdDAO;
import net.std.implementacion.InsertarControlDevolucionesImp;

@Controller
public class AhorroDevolucionSpeiPrStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(AhorroDevolucionSpeiPrStdSvc.class);
	private static String[] _OBSERVACIONES = {
			"INICIO PROCESO DEVOLUCION CUENTA %s - %s : %s", 
			"OK, SE DEVOLVIO EL SALDO CUENTA PLASTICO, NO LOCAL A LA CUENTA ORDENANTE %s - %s : %s", 
			"OK, DEVOLUCION CORRECTA A LA CUENTA ORDENANTE %s - %s : %s", 
			"ERROR EN DEVOLUCION CUENTA ORDENANTE %s - %s : %s",
			"NO PROCEDE DEVOLUCION POR ESTAR FUERA DE PERIODO %s - %s : %s"
	};

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroProcreaStdDAO dao = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroProcreaStdDAO)s.getApplicationContext().getBean("AhorroProcreaStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@RequestMapping(value="/ahorroDevolucionSpeiPrStd", method=RequestMethod.POST)
	public ResponseEntity<String> procesar(@RequestBody String json){
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
			if(map == null || map.get("cuentaOri") == null || map.get("cuentaDes") == null || map.get("fecha") == null || map.get("monto") == null ||
			   map.get("usuarioID") == null || map.get("movimientoID") == null || map.get("observaciones") == null ||
			   map.get("tipoMovto") == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "?"), HttpStatus.FORBIDDEN);
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_DEVOLUCION_AHORRO), "TRX_DEVOLUCION_AHORRO: " + Comun._T(map.get("cuentaOri")))){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		insertarControlDevoluciones(-1, null, Comun._T(map.get("cuentaOri")), null, Comun._T(map.get("monto")), Comun._T(map.get("observaciones")), 0, new Gson().toJson(map));
		
		try{
			RespuestaSVC respuestaSvc = dao.speiDevolucionAhorroPrStdDao(
											Comun._T(map.get("cuentaOri")), 
											Comun._T(map.get("cuentaDes")), 
											Comun._T(map.get("fecha")), 
											Comun._D(map.get("monto")), 
											_LNull(map.get("usuarioID")), 
											_LNull(map.get("movimientoID")), 
											Comun._T(map.get("observaciones")),
											Comun._I(map.get("tipoMovto")));
			
			if(respuestaSvc.getErrores().getCodigoError() == 0){
				insertarControlDevoluciones(-1, null, Comun._T(map.get("cuentaOri")), null, Comun._T(map.get("monto")), Comun._T(map.get("observaciones")), 2, new Gson().toJson(map));
				response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}else{
				insertarControlDevoluciones(-1, null, Comun._T(map.get("cuentaOri")), null, Comun._T(map.get("monto")), Comun._T(map.get("observaciones")), 3, new Gson().toJson(map));
				response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		return response;
	}
	
	public Double _DNull(Object valor) {
		return _NBoolean(valor)  ? null : new Double(String.valueOf(valor));
	}
	
	public Long _LNull(Object valor) {
		return _NBoolean(valor)  ? null : Long.parseLong(String.valueOf(valor.toString()));
	}
	
	public Boolean _NBoolean(Object valor){
		return valor == null || "".equals(valor) ? true : false;
	}
	
	/* 0 INICIO DE PROCESO DE DEVOLUCION, 1 = DEVOLUCION PROVEEDOR, 2 = DEVOLUCION INSERTADA Y SATISFACTORIA,  3 = ERROR, NO SE DEVOLVIO */
	private static void insertarControlDevoluciones(Integer estatusOperacion, String claveRastreo, String cuentaOrdenante, Long speiOutgoingId, 
			                                        String monto, String descripcion, Integer control, String cadena){
		try{
			String observaciones = String.format(_OBSERVACIONES[control], Comun._T(cuentaOrdenante), Comun._T(monto), Comun._T(descripcion));

			log.info(observaciones);
			RespuestaSVC respInsertarDev = InsertarControlDevolucionesImp.procesar(estatusOperacion, claveRastreo, speiOutgoingId, monto, observaciones, control, cadena, cuentaOrdenante);
			if(respInsertarDev.getErrores().getCodigoError() != 0L){
				log.info(respInsertarDev.getErrores().getDescError());
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return;
	}
	
}

