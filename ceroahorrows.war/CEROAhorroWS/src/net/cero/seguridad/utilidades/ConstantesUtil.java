/**
 * ASP Integra Opciones
 * Todos los derechos reservados
 *  com.asp.corresponsal.utils.ConstantesUtil
 *
 * Control de versiones:
 *
 * Version 	Date	 	By 		        Company 	Description
 * ------- 	--------    -------------   ----------  -----------
 * 1.0		07-16		Inver-Tu		Inver-Tu		Creacion
 */
package net.cero.seguridad.utilidades;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Clase de constantes
 * 
 * @author Inver-Tu
 * 
 */
public class ConstantesUtil {
    /** Constante RESOURCE_CONFIGURACION */

	private static final Logger log = LogManager.getLogger(ConstantesUtil.class);

	public static final ResourceBundle RESOURCE_CONFIGURACION = getProperties("configuracion");
	/** Constante WS_LOGIN */
	public static final String WS_CERO_NOTIF = RESOURCE_CONFIGURACION.getString("WS_CERO_NOTIF");

	public static final String WS_CERO_AHORRO = RESOURCE_CONFIGURACION.getString("WS_CERO_AHORRO");

	// OPERACIONES
	public static final String ALFRESCO_WS = RESOURCE_CONFIGURACION.getString("ALFRESCO_WS");
	
	public static final String AUTORIZACION_WS = RESOURCE_CONFIGURACION.getString("AUTORIZACION_WS");
	public static final String CONSULTA_SALDO_CUENTA = RESOURCE_CONFIGURACION.getString("CONSULTA_SALDO_CUENTA");
	public static final String CONSULTA_TARJETAS_DOCK = RESOURCE_CONFIGURACION.getString("CONSULTA_TARJETAS_DOCK");
	public static final String BLOQUEA_TARJETA_DOCK = RESOURCE_CONFIGURACION.getString("BLOQUEA_TARJETA_DOCK");
	public static final String DESBLOQUEA_TARJETA_DOCK = RESOURCE_CONFIGURACION.getString("DESBLOQUEA_TARJETA_DOCK");
	public static final String PALABRAUSU = RESOURCE_CONFIGURACION.getString("PALABRAUSU");
	public static final String PALABRAHID = RESOURCE_CONFIGURACION.getString("PALABRAHID");

	public static final String NUCLEO_CARTERA_WS = RESOURCE_CONFIGURACION.getString("NUCLEO_CARTERA_WS");

	public static final String SWITCHER_WS = RESOURCE_CONFIGURACION.getString("SWITCHER_WS");

	public static final String SIMPLIFICADA_WS = RESOURCE_CONFIGURACION.getString("SIMPLIFICADA_WS");

	public static final String SERVICIOS_SPEI_OUT = RESOURCE_CONFIGURACION.getString("SERVICIOS_SPEI_OUT");
	public static final String AHORRO_WS = RESOURCE_CONFIGURACION.getString("AHORRO_WS");

	public static final String SERVICIO_TOKEN = RESOURCE_CONFIGURACION.getString("SERVICIO_TOKEN");

	public static final String CERO_CODI_WS = RESOURCE_CONFIGURACION.getString("CERO_CODI_WS");
	public static final String CERO_CODI_AES = RESOURCE_CONFIGURACION.getString("CERO_CODI_AES");

	public static final String CERO_AHORRO_WS_CLAVE = RESOURCE_CONFIGURACION.getString("CERO_AHORRO_WS_CLAVE");

	public static final String CAMBIO_CONTRASENA_LDAP = RESOURCE_CONFIGURACION.getString("CAMBIO_CONTRASENA_LDAP");
	public static final String WS_SEGURIDAD_ASP = RESOURCE_CONFIGURACION.getString("WS_SEGURIDAD_ASP");
	public static final String CONCENTRADO_MOVIMIENTOS_WS = RESOURCE_CONFIGURACION.getString("CONCENTRADO_MOVIMIENTOS_WS");

	private ConstantesUtil(){
		throw new IllegalStateException("Utility class");
	}

	/** Obtiene propiedades */
	private static ResourceBundle getProperties(String bundle) {
		String ruta = "";
		ResourceBundle rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));

		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		log.info("so info {}", OS);
		if(OS.indexOf("win") >= 0){
			ruta =  path.getString("valueWin");
		}else{
			ruta = path.getString("value");
		}

		File file = new File(ruta);

		try {
			URL[] urls = { file.toURI().toURL() };
			rb=loadProp(urls, bundle);
		} catch (SecurityException | MalformedURLException e) {
			log.error(e.getMessage());
		}
		return rb;
	}

	private static ResourceBundle loadProp(URL[] urls,String bundle) {
		ResourceBundle rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));
		try (URLClassLoader loader = new URLClassLoader(urls);) {

			rb= ResourceBundle.getBundle(bundle, new Locale("es", "Mx"), loader);
		} catch (Exception e) {
			log.info("No ha sido posible cargar la configuracion: " + e.getMessage());
		}
		
		return rb;
	}
}
