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
import net.std.productos.dao.CatalogosProdAhorroCeroStdDAO;
import net.std.productos.dao.ProductosAhorroCeroStdDAO;

@Controller
public class CrearProductosAhorroCeroStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(CrearProductosAhorroCeroStdSvc.class);

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

	@SuppressWarnings("unused")
	@RequestMapping(value="/crearProductosCeroStd", method=RequestMethod.POST)
	public ResponseEntity<String> crearProductosCeroStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		Map<String, String> map;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null || daoCat == null) initialized();
			
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CREAR_PRODUCTOS_AHORRO), "TRX_CREAR_PRODUCTOS_AHORRO: " + Comun._T(map.get("CLAVE")))){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			RespuestaSVC ResEstatusId = daoCat.claveValorStdDao("ESTATUS_ID", map.get("ESTATUS_ID"));
			RespuestaSVC ResMonedaId = daoCat.claveValorStdDao("MONEDA_ID", map.get("MONEDA_ID"));
			
			//RespuestaSVC ResTipoAhorroId = daoCat.claveValorStdDao("TIPO_AHORRO_ID", map.get("TIPO_AHORRO_ID"));
			//RespuestaSVC ResCnbvId = daoCat.claveValorStdDao("CNBV_ID", map.get("CNBV_ID"));
			
			map.put("ESTATUS_ID", ResEstatusId.getErrores().getCodigoError() == 0 ? Comun._T(ResEstatusId.getBody().getValor("ID")) : "0");
			map.put("MONEDA_ID",  ResMonedaId.getErrores().getCodigoError() == 0 ? Comun._T(ResMonedaId.getBody().getValor("ID")) : "0");
			
			map.put("TIPO_AHORRO_ID", Comun._T(Constantes.TIPO_AHORRO_ID) );
			map.put("CNBV_ID", Comun._T(Constantes.CNBV_ID) );
			
			//map.put("TIPO_AHORRO_ID", ResTipoAhorroId.getErrores().getCodigoError() == 0 ? Comun._T(ResTipoAhorroId.getBody().getValor("ID")) : "0");
			//map.put("CNBV_ID", ResCnbvId.getErrores().getCodigoError() == 0 ? Comun._T(ResCnbvId.getBody().getValor("ID")) : "0");
			
			RespuestaSVC respuestaSvc = dao.crearProductosCeroStdDao(map);
			if(respuestaSvc.getErrores().getCodigoError() == 0){
				RespuestaSVC respConc = dao.crearConceptosProdStdDao(
                                                    Comun._I(respuestaSvc.getBody().getValor("PRODUCTO_ID")),
						                            Comun._T(map.get("CLAVE")), 
						                            ResEstatusId.getErrores().getCodigoError() == 0 ? Comun._I(ResEstatusId.getBody().getValor("ID")) : 0, 
													Comun._I(map.get("USUARIO_ID")));
				
				RespuestaSVC respDocto = dao.crearDocumentosStdDao(
													Comun._I(respuestaSvc.getBody().getValor("PRODUCTO_ID")),
													ResEstatusId.getErrores().getCodigoError() == 0 ? Comun._I(ResEstatusId.getBody().getValor("ID")) : 0,
													Comun._I(map.get("USUARIO_ID")));
						
				response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
			}else{
				response = new ResponseEntity<>(respuestaSvc.getErrores().getDescError(), HttpStatus.NOT_FOUND);
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
		if("".equals(Comun._T(map.get("MONTO_MINIMO")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MONTO_MINIMO");
		if("".equals(Comun._T(map.get("MONTO_MAXIMO")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MONTO_MAXIMO");
		if("".equals(Comun._T(map.get("FECHA_ACTIVACION")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "FECHA_ACTIVACION");
		if("".equals(Comun._T(map.get("ESTATUS_ID")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "ESTATUS_ID");
		if("".equals(Comun._T(map.get("MONEDA_ID")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "MONEDA_ID");
		if("".equals(Comun._T(map.get("CLAVE_CNBV")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CLAVE_CNBV");
		if("".equals(Comun._T(map.get("TIPO_PRODUCTO_AHORRO")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "TIPO_PRODUCTO_AHORRO");
		if("".equals(Comun._T(map.get("USUARIO_ID")))) ErrProd.desc(ErrProd.ERROR_PARAMETROS, "USUARIO_ID");
		
		return valida;
	}
}

