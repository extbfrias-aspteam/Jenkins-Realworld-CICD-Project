package net.std.expediente.svc;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Constantes;
import net.std.constantes.ErrProd;
import net.std.constantes.Errores;
import net.std.constantes.ValidaPermisos;
import net.std.dao.AhorroStdDAO;
import net.std.servicios.ClaveValorWS;
import net.std.servicios.ProcesoGeneraExpediente;
import net.std.data.CuentaCompletaOBJ;
import net.std.data.ExpedienteOBJ;
import net.std.data.TipoDocumentoCompletoOBJ;
import net.std.expediente.dao.ExpedienteStdDAO;
import net.std.productos.dao.CatalogosProdAhorroCeroStdDAO;
import net.std.productos.dao.ProductosAhorroCeroStdDAO;
import net.std.request.ExpedienteReq;
import net.std.response.ExpedienteRes;

@Controller
public class SubirExpedienteStdSvc implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(SubirExpedienteStdSvc.class);
	
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static ExpedienteStdDAO daoExp = null;
	private static ProductosAhorroCeroStdDAO daoProd = null;
	private static CatalogosProdAhorroCeroStdDAO daoCat = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoExp = (ExpedienteStdDAO)s.getApplicationContext().getBean("ExpedienteStdDAO");
			daoProd = (ProductosAhorroCeroStdDAO)s.getApplicationContext().getBean("ProductosAhorroCeroStdDAO");
			daoCat = (CatalogosProdAhorroCeroStdDAO)s.getApplicationContext().getBean("CatalogosProdAhorroCeroStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/subirExpedienteStd", method=RequestMethod.POST)
	public ResponseEntity<String> subirExpedienteStd(@RequestBody String json){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		ResponseEntity<String> response = null;
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		Map<String, String> mapResultado = new HashMap<>();
		ExpedienteReq altaExp = null;
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		try{
			if(dao == null || daoProd == null || daoCat == null || daoExp == null) initialized();
			
			/* PERSISTENCIA NULA DEL DAO */
			if(dao == null || daoProd == null || daoCat == null || daoExp == null){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_SIN_CONEXION_BD), HttpStatus.FORBIDDEN);
				return response;
			}
			
			altaExp = new Gson().fromJson(json, ExpedienteReq.class);
			String valida = validaParams(altaExp);
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
		if(!ValidaPermisos.valida(Comun._L(Constantes.TRX_CREAR_EXPEDIENTE), "TRX_CREAR_EXPEDIENTE: ")){
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PERMISO), HttpStatus.FORBIDDEN);
			return response;
		}
		
		/* SI PASO VALIDACION, VERIFICAR LA CONSTANTE BYPASS */
		
		try{
			/* OBTIENE EL ID DEL ESTATUS ALTA */
			RespuestaSVC respEst = ClaveValorWS.getEstatus(Comun._T(Constantes.ALTA_ID));
			Integer estatusID = respEst.getErrores().getCodigoError() == 0 ? Comun._I(respEst.getBody().getValor("ID")) : 0;
			
			/* OBTIENE EL ID DEL ESTATUS BAJA */
			RespuestaSVC respEstBaja = ClaveValorWS.getEstatus(Comun._T(Constantes.BAJA_ID));
			Integer estatusBajaID = respEstBaja.getErrores().getCodigoError() == 0 ? Comun._I(respEstBaja.getBody().getValor("ID")) : 0;
			
			/* COMPRUEBA QUE EL PRODUCTO EXISTA */
			RespuestaSVC respProd = daoProd.leerProductosCeroStdDao(altaExp.getProducto(), Comun._T(Constantes.ALTA_ID));
			if(respProd.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PRODUCTO_NO_EXISTE, respProd.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response;
			}
			Integer productoAhorroId = Comun._I(respProd.getBody().getValor("ID"));
			String productoAhorro = Comun._T(respProd.getBody().getValor("CLAVE"));
			
			/* OBTIENE LOS TIPOS DE DOCUMENTOS DEL PRODUCTO */
			RespuestaSVC respDoc = daoProd.leerDocumentosStdDao(productoAhorroId, estatusID);
			if(respDoc.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_TIPO_DOCUMENTO, respDoc.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response;
			}
			List<TipoDocumentoCompletoOBJ> lstDoctos = (List<TipoDocumentoCompletoOBJ>) respDoc.getBody().getValor("DOCUMENTOS");
			
			/* OBTIENE LOS DATOS DE LA CUENTA */
			RespuestaSVC respCta = dao.leerCuentaAhorroDao(altaExp.getCuenta());
			if(respCta.getErrores().getCodigoError() != 0){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_CUENTA_DESCONOCIDA, respCta.getErrores().getDescError()), HttpStatus.FORBIDDEN);
				return response;
			}
			CuentaCompletaOBJ cta = (CuentaCompletaOBJ)respCta.getBody().getValor("CUENTA");
			mapResultado.put("CUENTA_ID", Comun._T(cta.getId()));
			mapResultado.put("CUENTA", Comun._T(cta.getCuenta()));
			
			/* VERIFICA COMPATIBILIDAD DE PRODUCTOS, ENTRE EL ENVIADO Y EL REGISTRADO EN LA CUENTA */
			if(!productoAhorro.equals(cta.getCve_producto())){
				response = new ResponseEntity<>(Errores.desc(Errores.ERROR_PRODUCTO_NO_CORRESPOND, productoAhorro), HttpStatus.FORBIDDEN);
				return response;
			}
			
			for(ExpedienteOBJ exp : altaExp.getLstExpediente()){
				TipoDocumentoCompletoOBJ doc = getDocID(lstDoctos, exp.getCve_Documento());
				if(doc != null){
					RespuestaSVC respAct = daoExp.actualizarExpedienteEstatusStdDao(Comun._I(cta.getId()), Comun._I(doc.getDocumento_id()), estatusBajaID);
					if(respCta.getErrores().getCodigoError() != 0){
						log.info(respAct.getErrores().getDescError());
						mapResultado.put("EXPEDIENTE_1", String.format("%s - %s", Comun._T(doc.getCve_documento()), Comun._T(respAct.getErrores().getDescError())));
					}
					
					String carpeta = String.format(Comun._T(Constantes.CARPETA_ALFRESCO), cta.getPersona_id(), cta.getCuenta());
					exp.setCuentaId(Comun._T(cta.getId()));
					exp.setCuenta(Comun._T(cta.getCuenta()));
					exp.setDocumentosAhorroId(Comun._T(doc.getDocumento_id()));
					exp.setCve_Documento(Comun._T(doc.getCve_documento()));
					exp.setRutaAlfresco(carpeta);
					exp.setObservaciones("".equals(Comun._T(exp.getObservaciones())) ? Comun._T(altaExp.getObservaciones()) : Comun._T(exp.getObservaciones()));
					exp.setNombre(String.format("%s-%s-%s", getVersion(), Comun._T(doc.getCve_documento()), Comun._T(exp.getNombre())));
					exp.setEstatusId(Comun._T(estatusID));
					exp.setUsuarioId(Comun._T(Constantes.USUARIO_ID));
					

					/* EJECUTA LA ACCION SUBIDA DE IMAGEN DE ALFRESCO */
					RespuestaSVC respAlfresco = ProcesoGeneraExpediente.procesar(exp);
					if(respAlfresco.getErrores().getCodigoError() != 0){
						log.info(respAlfresco.getErrores().getDescError());
						mapResultado.put("EXPEDIENTE_2", String.format("%s - %s", Comun._T(exp.getCve_Documento()), Comun._T(respAlfresco.getErrores().getDescError())));
					}else{
						ExpedienteRes expImagen = (ExpedienteRes)respAlfresco.getBody().getValor("RESULTADO");
						exp.setIdArchivoAlfresco(expImagen.getImagenAlfresco() != null ? Comun._T(expImagen.getImagenAlfresco().getIdImagen()) : null);

						RespuestaSVC respInsDoc = daoExp.insertarExpedienteStdDao(exp);
						if(respInsDoc.getErrores().getCodigoError() != 0){
							log.info(respInsDoc.getErrores().getDescError());
							mapResultado.put("EXPEDIENTE_3", String.format("%s - %s", Comun._T(exp.getCve_Documento()), Comun._T(respInsDoc.getErrores().getDescError())));
						}else{
							mapResultado.put("EXPEDIENTE_4", String.format("%s - %s", Comun._T(exp.getCve_Documento()), "EXPEDIENTE INSERTADO OK"));
						}
					}
				}else{
					mapResultado.put("EXPEDIENTE_5", String.format("%s - %s", Comun._T(exp.getCve_Documento()), Comun._T(Errores.desc(Errores.ERROR_TIPO_DOCUMENTO,exp.getCve_Documento()))));
				}
			}
			
			respuestaSvc.getBody().addValor("RESULTADO", mapResultado);
			response = new ResponseEntity<>(new Gson().toJson(respuestaSvc), HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			response = new ResponseEntity<>(Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return response;
	}
	
	private TipoDocumentoCompletoOBJ getDocID(List<TipoDocumentoCompletoOBJ> lst, String claveDoc){
		TipoDocumentoCompletoOBJ doc = null;
		if(claveDoc == null) return doc;
		
		try{
			for(int i = 0; i < lst.size(); i++){
				if(claveDoc.equals(lst.get(i).getCve_documento())){
					doc = lst.get(i);
					break;
				}
			}
		}catch(Exception ex){
			log.error(ex);
		}
		
		return doc;
	}
	
	private String validaParams(ExpedienteReq req){
		String valida = null;

		if(req == null) return ErrProd.desc(ErrProd.ERROR_DOCUMENTOS, "SIN PARAMETROS");
		if(req.getProducto() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA PRODUCTO");
		if(req.getCuenta() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA LA CUENTA");
		if(req.getObservaciones() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE PROPORCIONA OBSERVACIONES");
		if(req.getLstExpediente() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "SIN DOCUMENTOS PARA AGREGAR");
		
		for(int i = 0; i < req.getLstExpediente().size(); i++){
			if(req.getLstExpediente().get(i).getCve_Documento() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "CVE TIPO DOCUMENTO");
			if(req.getLstExpediente().get(i).getNombre() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NOMBRE ARCHIVO");
			if(req.getLstExpediente().get(i).getImagen() == null) return ErrProd.desc(ErrProd.ERROR_PARAMETROS, "NO SE ENUENTRA LA IAMGEN DIGITALIZADA");		
		}
		
		return valida;
	}
	
	private static String getVersion(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
	}
}

