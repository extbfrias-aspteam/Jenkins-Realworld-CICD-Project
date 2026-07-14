package net.cero.ws.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Constantes {
	private static final Logger log = LogManager.getLogger(Constantes.class);


	public static final ResourceBundle RESOURCE_CONFIGURACION = getProperties("ahorro");

	public static final String SERVICIOS_SISCOOP = RESOURCE_CONFIGURACION.getString("SERVICIOS_SISCOOP");
	public static final String AHORRO_SIMPLIFICADA_WS = RESOURCE_CONFIGURACION.getString("AHORRO_SIMPLIFICADA_WS");
	public static final String AHORRO_WS = RESOURCE_CONFIGURACION.getString("AHORRO_WS");
	public static final String NUCLEO_CARTERA_WS = RESOURCE_CONFIGURACION.getString("NUCLEO_CARTERA_WS");
	public static final String VENTANILLA_WS = RESOURCE_CONFIGURACION.getString("VENTANILLA_WS");
	public static final String PALABRAUSU = RESOURCE_CONFIGURACION.getString("PALABRAUSU");
	public static final String PALABRAHID = RESOURCE_CONFIGURACION.getString("PALABRAHID");

	public static final String NO_PROCESADA = RESOURCE_CONFIGURACION.getString("NO_PROCESADA");
	public static final String CUENTA_INEXISTENTE = RESOURCE_CONFIGURACION.getString("CUENTA_INEXISTENTE");
	public static final String CUENTA_BLOQUEADA = RESOURCE_CONFIGURACION.getString("CUENTA_BLOQUEADA");
	public static final String CUENTA_CANCELADA = RESOURCE_CONFIGURACION.getString("CUENTA_CANCELADA");
	public static final String CUENTA_OTRA_DIVISA = RESOURCE_CONFIGURACION.getString("CUENTA_OTRA_DIVISA");
	public static final String CUENTA_NO_PERTENECE_BANCO_EMISOR = RESOURCE_CONFIGURACION.getString("CUENTA_NO_PERTENECE_BANCO_EMISOR");
	public static final String BENEFICIARIO_NO_RECONOCE_PAGO = RESOURCE_CONFIGURACION.getString("BENEFICIARIO_NO_RECONOCE_PAGO");
	public static final String FALTA_INFORMACION = RESOURCE_CONFIGURACION.getString("FALTA_INFORMACION");
	public static final String TIPO_PAGO_ERRONEO = RESOURCE_CONFIGURACION.getString("TIPO_PAGO_ERRONEO");
	public static final String TIPO_OPERACION_ERRONEA = RESOURCE_CONFIGURACION.getString("TIPO_OPERACION_ERRONEA");
	public static final String TIPO_CUENTA_NO_CORRESPONDE = RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_NO_CORRESPONDE");
	public static final String SOLICITUD_EMISOR = RESOURCE_CONFIGURACION.getString("SOLICITUD_EMISOR");
	public static final String CARACTER_INVALIDO = RESOURCE_CONFIGURACION.getString("CARACTER_INVALIDO");
	public static final String SALDO_INSUFICIENTE = RESOURCE_CONFIGURACION.getString("SALDO_INSUFICIENTE");
	public static final String MONTO_MAXIMO = RESOURCE_CONFIGURACION.getString("MONTO_MAXIMO");
	public static final String GENERICO = RESOURCE_CONFIGURACION.getString("GENERICO");

	public static final String ESTATUS_ABONADA = RESOURCE_CONFIGURACION.getString("ESTATUS_ABONADA");
	public static final String ESTATUS_CARGADA = RESOURCE_CONFIGURACION.getString("ESTATUS_CARGADA");
	public static final String ESTATUS_DEVUELTA = RESOURCE_CONFIGURACION.getString("ESTATUS_DEVUELTA");

	public static final String EJECUCION_NORMAL = RESOURCE_CONFIGURACION.getString("EJECUCION_NORMAL");
	public static final String EJECUCION_ERRONEA = RESOURCE_CONFIGURACION.getString("EJECUCION_ERRONEA");

	public static final String BANCO_PAGO_CREDITO_CF = RESOURCE_CONFIGURACION.getString("BANCO_PAGO_CREDITO_CF");
	public static final String MEDIO_PAGO_CACAO = RESOURCE_CONFIGURACION.getString("MEDIO_PAGO_CACAO");
	public static final String SERVICIO_BASE_ADMIN_PLASTICOS = RESOURCE_CONFIGURACION.getString("SERVICIO_BASE_ADMIN_PLASTICOS");

	public static final String DEBITO_CON_DEP = RESOURCE_CONFIGURACION.getString("DEBITO_CON_DEP");
	public static final String DEBITO_CON_RET = RESOURCE_CONFIGURACION.getString("DEBITO_CON_RET");

	public static final Boolean VALID_TARJETA_DEB = Boolean.parseBoolean(RESOURCE_CONFIGURACION.getString("VALID_TARJETA_DEB"));

	public static final Boolean INSERT_MOV_CERO =  Boolean.parseBoolean(RESOURCE_CONFIGURACION.getString("INSERT_MOV_CERO"));
	public static final String WS_VALIDAR_MONTO_TRANSACCIONAL =RESOURCE_CONFIGURACION.getString("WS_VALIDAR_MONTO_TRANSACCIONAL");
	public static String getDescripcion(String des){
		return RESOURCE_CONFIGURACION.getString(des);
	}

    public static Long TIEMPO_ESPERA_VALIDAR_OPERACION = Long.parseLong(RESOURCE_CONFIGURACION.getString("TIEMPO_ESPERA_VALIDAR_OPERACION"));

	private Constantes(){
		throw new IllegalStateException("Utility class");
	}

	private static ResourceBundle getProperties(String bundle) {
		String ruta = "";
		ResourceBundle rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));
		ResourceBundle path = ResourceBundle.getBundle("path", new Locale("es", "Mx"));
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		
		ruta = OS.indexOf("win") >= 0 ? path.getString("valueWin") : path.getString("value"); 
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
		try(URLClassLoader loader = new URLClassLoader(urls);) {
			rb= ResourceBundle.getBundle(bundle, new Locale("es", "Mx"), loader);
		} catch (Exception e) {
			log.info("No ha sido posible cargar la configuracion: " + e.getMessage());
		}
		return rb;
	}
}
