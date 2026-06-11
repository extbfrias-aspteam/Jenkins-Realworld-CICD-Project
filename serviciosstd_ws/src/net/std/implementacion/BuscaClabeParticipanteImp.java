package net.std.implementacion;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Comun;
import net.std.constantes.Errores;
import net.std.dao.AhorroProcreaStdDAO;
import net.std.dao.AhorroStdDAO;

public class BuscaClabeParticipanteImp implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(BuscaClabeParticipanteImp.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static AhorroStdDAO dao = null;
	private static AhorroProcreaStdDAO daoProcrea = null;
	
	private static Boolean initialized() {
		Boolean valida = true;
		if(dao != null && daoProcrea == null) return valida;
		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (AhorroStdDAO)s.getApplicationContext().getBean("AhorroStdDAO");
			daoProcrea = (AhorroProcreaStdDAO)s.getApplicationContext().getBean("AhorroProcreaStdDAO");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(dao == null || daoProcrea == null) valida = false;
		return valida;
	}

	public static RespuestaSVC procesar(String clabe){
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		
		/* VALIDA PARAMETROS DE ENTRADA */
		if("".equals(Comun._T(clabe))){
			return Comun.RespError(Errores.ERROR_CAMPOS_REQUERIDOS, Errores.desc(Errores.ERROR_CAMPOS_REQUERIDOS, "CLABE"));
		}
		
		/* VALIDA LA CONEXION A LOS BEANS DAO */
		if(!initialized()) {
			return Comun.RespError(Errores.ERROR_SIN_CONEXION_BD, Errores.desc(Errores.ERROR_SIN_CONEXION_BD));
		}
		
		try{
			/* BUSCA LA CLABE ( CUENTA INTERBANCARIA ) EN EL CORE DE CERO */
			respuestaSvc = dao.leerCuentaAhorroClabeDao(Comun._T(clabe));
			if(respuestaSvc.getErrores().getCodigoError() != 0){
				/* NO LA ENCONTRO, BUSCA EN EL CORE DE PROCREA */
				respuestaSvc = daoProcrea.leerCuentaAhorroClabePrStdDao(Comun._T(clabe));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return Comun.RespError(Errores.ERROR_INESPERADO, Errores.desc(Errores.ERROR_INESPERADO, ex.getMessage()));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return respuestaSvc;
	}
}
