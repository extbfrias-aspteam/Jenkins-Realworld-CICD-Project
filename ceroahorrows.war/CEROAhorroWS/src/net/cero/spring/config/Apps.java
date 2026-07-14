package net.cero.spring.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Apps 
{
	private static final Logger LOG = LoggerFactory.getLogger(Apps.class);

	// !PRIVATE
	private static ApplicationContext applicationContext = null;
	private static Apps instance = null;
	private static List<String> mapAuthIP= new ArrayList<> ();;

	
	private static String dirLog;
	private static String userAuthoriz;
	private static String passAuthoriz;	
	
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." 
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." 
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." 
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


	// CONSTRUCTORS	
	protected Apps() {

		getMapIpAuthoriz();

		try {			
			applicationContext = new ClassPathXmlApplicationContext("data/resources/Spring-Module.xml");
		} 
		catch (Exception e) {
			LOG.error(e.toString());
		}
	}


	/**
	 * only to defeat instantiation.
	 * @return
	 */
	public static Apps getInstance() {
		
		if(instance == null) {
			instance = new Apps();
		}
		return instance;
	}

	/**
	 * Valida que la direccion ip sea valida.
	 * @param ip
	 * @return
	 */
	private static boolean validate(final String ip){
		Pattern pattern;
		Matcher matcher;
		
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ip);
		return matcher.matches();	    	    
	}

	private static void getMapIpAuthoriz() 
	{
		Properties prop = new Properties();
		Class<Context> c = Context.class;
		StringTokenizer tokenstr;		
		InputStream inFile;
		String ipsAuthoriz;
		String ip;


		inFile= null;
		try {
			// Conexion a Servidor del JBOSS
			inFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("/src/net/cero/ahorro/cfg/Webconfig.properties");
			prop.load(inFile);
		} 
		catch (Exception e1) {
			// Version para conexion al servidor JBOSS LOCAL (ambiente de eclipse)
			try {
				inFile = c.getResourceAsStream("/net/cero/ahorro/cfg/Webconfig.properties");
				prop.load(inFile);
			} 
			catch (Exception e2) {
				LOG.error("Error en GETMapIpAuthoriz al obtener paramentros: " + e2.toString());
			}
		} 

		try {

			if (inFile != null && !prop.isEmpty()) {
				ipsAuthoriz = prop.getProperty("IPAUTH");
				userAuthoriz = prop.getProperty("USERAUTH");
				passAuthoriz = prop.getProperty("PASSAUTH");
						
				tokenstr = new StringTokenizer(ipsAuthoriz, ",", false);
				while ( tokenstr.hasMoreTokens() ) {
					ip= tokenstr.nextToken();				
					if (validate(ip)) {
						// Es una ip valida en el archivo de configuracion.
						mapAuthIP.add(ip);
					}		
					else {
						LOG.error("Error en GETMapIpAuthoriz, IP invalida : {}", ip);
					}
				}
			}
			else {
				LOG.error("Error en GETMapIpAuthoriz, Paramentros vacios ");
			}


		} 
		catch (Exception e) {
			LOG.error("Error en GETMapIpAuthoriz, Al obtener paramentros: " + e.toString()) ;			
		}					
	}

	/**
	 * Valida si la ip esta permitida.
	 * @param ip
	 * @return
	 */
	public boolean isAuthenticated (final String ip)
	{
		if(mapAuthIP.contains("0.0.0.0")){
			return true;
		}
		
		return 	mapAuthIP.contains(ip);
	}

	public static String getUserAuthoriz() {
		return userAuthoriz;
	}

	public static String getPassAuthoriz() {
		return passAuthoriz;
	}

	public String getDirLog() {
		return dirLog;
	}

	public void setDirLog(String dirLog) {
		Apps.dirLog = dirLog;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


}


/** !Apps.java */