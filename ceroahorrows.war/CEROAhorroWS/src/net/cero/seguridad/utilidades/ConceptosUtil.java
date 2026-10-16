/**
 * ASP Integra Opciones
 * Todos los derechos reservados
 *  net.cero.seguridad.utilidades.ConceptosUtil
 *
 * Control de versiones:
 *
 * Version 	Date	 	By 		        Company 	Description
 * ------- 	--------    -------------   ----------  -----------
 * 1.0		07-18		Israel			ASP			Creacion
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
public class ConceptosUtil {

	/** Constante RESOURCE_CONFIGURACION */

	private static final Logger log = LogManager.getLogger(ConceptosUtil.class);

	public static final ResourceBundle RESOURCE_CONCEPTOS = getProperties("conceptosAhorro");
	
	public static final Integer CAJA_DEP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("CAJA_DEP_TRANSFERENCIA_AHORRO"));
	public static final Integer FORMA_APGO_DEP_TRANSFERENCIA_AHO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("FORMA_APGO_DEP_TRANSFERENCIA_AHO"));
	public static final Integer FORMA_APGO_DISP_TRANSFERENCIA_AHO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("FORMA_APGO_DISP_TRANSFERENCIA_AHO"));
	public static final Integer MOVIMIENTO_DEP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("MOVIMIENTO_DEP_TRANSFERENCIA_AHORRO"));
	public static final Integer MOVIMIENTO_DISP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("MOVIMIENTO_DISP_TRANSFERENCIA_AHORRO"));
	public static final Integer MOVIMIENTO_DISP_TRANSFERENCIA_SPEI = Integer.valueOf(RESOURCE_CONCEPTOS.getString("MOVIMIENTO_DISP_TRANSFERENCIA_SPEI"));
	public static final Integer USUARIO_DEP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("USUARIO_DEP_TRANSFERENCIA_AHORRO"));
	public static final Integer BANCO_DEP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("BANCO_DEP_TRANSFERENCIA_AHORRO"));
	public static final Integer BANCO_DISP_TRANSFERENCIA_AHORRO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("BANCO_DISP_TRANSFERENCIA_AHORRO"));
	
	public static final Integer VAL_OCR_INE = Integer.valueOf(RESOURCE_CONCEPTOS.getString("VAL_OCR_INE"));
	
	public static final Integer SUCURSAL_VIRTUAL_CODI = Integer.valueOf(RESOURCE_CONCEPTOS.getString("SUCURSAL_VIRTUAL_CODI"));
	//public static final String SUCURSAL_VIRTUAL_CODI = RESOURCE_CONCEPTOS.getString("SUCURSAL_VIRTUAL_CODI");
	
	public static final Integer ACTIVIDAD_ID = Integer.valueOf(RESOURCE_CONCEPTOS.getString("ACTIVIDAD_ID"));
	public static final Integer GIRO_ID = Integer.valueOf(RESOURCE_CONCEPTOS.getString("GIRO_ID"));
	public static final Integer CVE_DESTINO = Integer.valueOf(RESOURCE_CONCEPTOS.getString("CVE_DESTINO"));
	public static final Integer OCUPACION_ID = Integer.valueOf(RESOURCE_CONCEPTOS.getString("OCUPACION_ID"));
	
	public static final String REMITENTE_EMAIL_CALLCENTER = RESOURCE_CONCEPTOS.getString("REMITENTE_EMAIL_CALLCENTER");
	public static final String DESTINATARIO_EMAIL_CALLCENTER = RESOURCE_CONCEPTOS.getString("DESTINATARIO_EMAIL_CALLCENTER");
	public static final String ASUNTO_EMAIL_CALLCENTER = RESOURCE_CONCEPTOS.getString("ASUNTO_EMAIL_CALLCENTER");
	public static final String PRODUCTO_CUENTA_FACIL = RESOURCE_CONCEPTOS.getString("PRODUCTO_CUENTA_FACIL");
	public static final Integer ID_PRODUCTO_CUENTA_FACIL = Integer.valueOf(RESOURCE_CONCEPTOS.getString("ID_PRODUCTO_CUENTA_FACIL"));
	public static final Integer TRX_VERIFICA_DATOS_CUENTA_FACIL = Integer.valueOf(RESOURCE_CONCEPTOS.getString("TRX_VERIFICA_DATOS_CUENTA_FACIL"));
	public static final String USR_SERV_AUT = RESOURCE_CONCEPTOS.getString("USR_SERV_AUT");
	public static final String PSW_SERV_AUT = RESOURCE_CONCEPTOS.getString("PSW_SERV_AUT");
	
	public static final Integer ID_EMPRESA = Integer.valueOf(RESOURCE_CONCEPTOS.getString("ID_EMPRESA"));
	public static final String USR_CLAVE = RESOURCE_CONCEPTOS.getString("USR_CLAVE");
	public static final Long ID_USR = Long.valueOf(RESOURCE_CONCEPTOS.getString("ID_USR"));
	public static final Long ID_SUCURSAL = Long.valueOf(RESOURCE_CONCEPTOS.getString("ID_SUCURSAL"));
	public static final String IP_HOST = RESOURCE_CONCEPTOS.getString("IP_HOST");
	public static final Long ID_CANAL = Long.valueOf(RESOURCE_CONCEPTOS.getString("ID_CANAL"));
	public static final String CAMBIO_TEL_CODI_CLV = RESOURCE_CONCEPTOS.getString("CAMBIO_TEL_CODI_CLV");
	public static final String CAMBIO_COL_ID_CLV = RESOURCE_CONCEPTOS.getString("CAMBIO_COL_ID_CLV");
	public static final String CAMBIO_CORREO_CLV = RESOURCE_CONCEPTOS.getString("CAMBIO_CORREO_CLV");
	public static final String CAMBIO_DOMICILIO_CLV = RESOURCE_CONCEPTOS.getString("CAMBIO_DOMICILIO_CLV");

	
	private ConceptosUtil(){
		throw new IllegalStateException("Utility class");
	}

	/** Obtiene propiedades */
	private static ResourceBundle getProperties(String bundle) {
		String ruta = "";
		ResourceBundle rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));

		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
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
