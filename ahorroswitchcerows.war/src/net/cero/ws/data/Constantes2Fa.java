package net.cero.ws.data;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constantes2Fa {
	public static final ResourceBundle RESOURCE_CONFIGURACION = getProperties("offset");

	public static final String CLAVE_AES = RESOURCE_CONFIGURACION.getString("CLAVE_AES");
	public static final String CLAVE_1 = RESOURCE_CONFIGURACION.getString("CLAVE_1");
	public static final String CLAVE_2 = RESOURCE_CONFIGURACION.getString("CLAVE_2");
	
	private Constantes2Fa(){
		throw new IllegalStateException("Utility class");
	}
	
	private static ResourceBundle getProperties(String bundle) {
		ResourceBundle rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));
		return rb;
	}
}
