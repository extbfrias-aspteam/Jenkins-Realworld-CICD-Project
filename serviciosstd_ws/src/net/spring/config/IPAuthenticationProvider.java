package net.spring.config;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class IPAuthenticationProvider implements AuthenticationProvider {
	private static Apps apps = null;
	private String ipLocalHost;
	private static Logger log = LoggerFactory.getLogger(IPAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) {
		WebAuthenticationDetails wad = null;
		String userIPAddress = null;
		boolean hr;

		Properties prop = new Properties();
		InputStream inFile=null;
		
		try {
			// Conexion a Servidor del JBOSS
			inFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("/src/net/spring/data/Webconfig.properties");
			prop.load(inFile);
		} catch (Exception e1) {
			// Version para conexion al servidor JBOSS LOCAL (ambiente de
			// eclipse)
			try {
				inFile = Context.class.getResourceAsStream("/net/spring/data/Webconfig.properties");
				prop.load(inFile);
			} catch (Exception e2) {
				log.error("Error en GETMapIpAuthoriz al obtener paramentros: " + e2.toString());
			}
		}
		if (inFile != null && !prop.isEmpty()) {
			ipLocalHost=prop.getProperty("LOCALHOST");
		} else {
			log.error("Error en GETMapIpAuthoriz, Paramentros vacios ");
		}

		// Obtiene la direccion ip desde el site.
		wad = (WebAuthenticationDetails) authentication.getDetails();
		userIPAddress = wad.getRemoteAddress();

		// Es la IP local dejala pasar.
		if (ipLocalHost.equals(userIPAddress)) {
			return authentication;
		}

		// OBTEN LAS IP PERMITIDAS
		Apps s = Apps.getInstance();
		synchronized (Apps.class) {
			if (apps == null) // si la referencia es null ...
				apps = s; // ... agrega la clase singleton
		}

		hr = s.isAuthenticated(userIPAddress);
		if (!hr) {
			authentication.setAuthenticated(false);
		}
		return authentication;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return false;
	}

}

/** !IPAuthenticationProvider.java */
