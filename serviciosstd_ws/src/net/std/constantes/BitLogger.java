package net.std.constantes;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import net.cero.ws.data.RespuestaSVC;
import net.spring.config.Apps;
import net.spring.config.IPAuthenticationProvider;
import net.std.constantes.Errores;
import net.std.dao.BitacoraLogStdDAO;
import net.std.dao.BitacoraStdDAO;


public class BitLogger implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(BitLogger.class);

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static Apps apps = null;
	private static BitacoraLogStdDAO dao = null;
	private static BitacoraStdDAO daoSti = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) apps = s; 
			}
			dao = (BitacoraLogStdDAO)s.getApplicationContext().getBean("BitacoraLogStdDAO");
			daoSti = (BitacoraStdDAO)s.getApplicationContext().getBean("BitacoraStdDAO");

		} catch (Exception ex) {
			log.error("ERROR [INICIALIZADO] : ", ex);
		}
	}

	public static void info(String proceso, String dato, String observaciones){
		
		log.info(String.format("IN -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		try{
			if(dao == null) initialized();
			if(dao != null){
				String etiqueta = "".equals(Comun._T(proceso)) ? Comun._T(Constantes.PROCESO) : Comun._T(proceso);
				RespuestaSVC respuesta = dao.insertarbitacoraLogDao(etiqueta, dato, observaciones, Comun._I(Constantes.USUARIO_ID));
				if(respuesta.getErrores().getCodigoError() != 0){
					log.error(respuesta.getErrores().getDescError());
				}
			}else{
				log.error(Errores.desc(Errores.ERROR_SIN_CONEXION_BD, "BitLogger"));
			}
			
			log.info(String.format("[Proceso] %s, [Dato] %s, [Observaciones] %s", Comun._T(Constantes.PROCESO), dato, observaciones));
			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(Errores.desc(Errores.ERROR_BITACORA, String.format("[Proceso] %s, [Dato] %s, [Observaciones] %s", Comun._T(Constantes.PROCESO), dato, observaciones)));
		}
		
		log.info(String.format("OUT -> %s :: %s",new Object(){}.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName()));
		return;
	}
	
	public static void  bitacora(String descripcion, String clave_rastreo)
	{
		try{
			if(daoSti == null) initialized();
			if(daoSti != null){
				daoSti.insertarSpeiBitacoraDao(descripcion, clave_rastreo);
			}else{
				log.error(Errores.desc(Errores.ERROR_SIN_CONEXION_BD, "BitLogger -- BitacoraStdDAO"));
			}
			
			try {			
				log.info(String.format("%s  %s  %s", "SERVICIOS_STD_WS", descripcion, clave_rastreo ));
			} 
			catch (Exception e) {
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

