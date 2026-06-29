package net.std.constantes;


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

	public static final ResourceBundle RESOURCE_CONFIGURACION = getProperties("std");
	
	/* SERVICIOS */
	public static final String REST_LOGIN = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_LOGIN"));
	public static final String REST_NOTIFICAR_ESTADO = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_NOTIFICAR_ESTADO"));
	public static final String REST_NOTIFICAR_ABONO = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_NOTIFICAR_ABONO"));
	public static final String REST_CAMBIAR_CONTRASENA = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_CAMBIAR_CONTRASENA"));
	
	public static final String REST_FECHA_OPERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_FECHA_OPERACION"));
	public static final String REST_CIERRE_OPERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_CIERRE_OPERACION"));
	public static final String REST_INICIO_OPERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("REST_INICIO_OPERACION"));

	/* SERVICIOS DE AHORRO CERO */
	public static final String PLASTICOS_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("PLASTICOS_WS"));
	public static final String AHORRO_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("AHORRO_WS"));
	public static final String NUCLEO_CARTERA_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("NUCLEO_CARTERA_WS"));
	public static final String SWITCHER_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("SWITCHER_WS"));
	public static final String SIMPLIFICADA_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("SIMPLIFICADA_WS"));
	public static final String CREDITO_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("CREDITO_WS"));
	public static final String ADMIN_SEG_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("ADMIN_SEG_WS"));
	public static final String ALFRESCO_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("ALFRESCO_WS"));	
	public static final String RUTA_ALFRESCO = Comun._TX(RESOURCE_CONFIGURACION.getString("RUTA_ALFRESCO"));
	public static final String CARPETA_ALFRESCO = Comun._TX(RESOURCE_CONFIGURACION.getString("CARPETA_ALFRESCO"));
	public static final String SERVICIO_STD_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("SERVICIO_STD_WS"));
	
	public static final String BYPASS = Comun._TX(RESOURCE_CONFIGURACION.getString("BYPASS"));
	
	/*AUTENCICACION SERVICIOS LOCALES */
	public static final String PALABRAUSU = Comun._TX(RESOURCE_CONFIGURACION.getString("PALABRAUSU"));
	public static final String PALABRAHID = Comun._TX(RESOURCE_CONFIGURACION.getString("PALABRAHID"));
	
	/* PARAMETROS */
	public static final String USER_Std = Comun._TX(RESOURCE_CONFIGURACION.getString("USER_Std"));
	public static final String PASS_Std = Comun._TX(RESOURCE_CONFIGURACION.getString("PASS_Std"));
	public static final String SECRET_KEY = Comun._TX(RESOURCE_CONFIGURACION.getString("SECRET_KEY"));
	
	/* SERVICIOS URI */
	public static final String SERVICIO_BLU_WS = RESOURCE_CONFIGURACION.getString("SERVICIO_BLU_WS");
	public static final String VERSION = Comun._TX(RESOURCE_CONFIGURACION.getString("VERSION"));
	public static final String PREFIJO = Comun._TX(RESOURCE_CONFIGURACION.getString("PREFIJO"));

	/* CERTIIFCADOS ASP  */
	public static final String LLAVE_PUBLICA_BLU = Comun._TX(RESOURCE_CONFIGURACION.getString("LLAVE_PUBLICA_BLU"));
	public static final String LLAVE_PUBLICA_ASP = Comun._TX(RESOURCE_CONFIGURACION.getString("LLAVE_PUBLICA_ASP"));
	public static final String LLAVE_PRIVADA_ASP = Comun._TX(RESOURCE_CONFIGURACION.getString("LLAVE_PRIVADA_ASP"));
	public static final String PASSWORD_ASP = Comun._TX(RESOURCE_CONFIGURACION.getString("PASSWORD_ASP"));
	
	/* SERVICIOS MULE WSSPEI */
	public static final String MULE_WSSPEI = Comun._TX(RESOURCE_CONFIGURACION.getString("MULE_WSSPEI"));
	public static final String MULE_WS = Comun._TX(RESOURCE_CONFIGURACION.getString("MULE_WS"));
	
	/* DATOS QUE LLENAN EL HEADER */
	public static final String EMPRESA_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("EMPRESA_ID"));
	public static final String SUCURSAL_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("SUCURSAL_ID"));
	public static final String USUARIO_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("USUARIO_ID"));
	public static final String HOST_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("HOST_ID"));
	public static final String UBICACION_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("UBICACION_ID"));
	public static final String AGENTE_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("AGENTE_ID"));
	public static final String APLICACION_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICACION_ID"));
	public static final String APLICACION_CERO_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICACION_CERO_ID"));
	public static final String TABLA_CORRELATIVO = Comun._TX(RESOURCE_CONFIGURACION.getString("TABLA_CORRELATIVO"));
	
	
	public static final String OBTENER_CLABE_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("OBTENER_CLABE_ID"));
	public static final String CANAL_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("CANAL_ID"));
	
	/* PRODUCTOS */
	public static final String CNBV_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("CNBV_ID"));
	public static final String TIPO_AHORRO_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_AHORRO_ID"));
	public static final String ALTA_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("ALTA_ID"));
	public static final String BAJA_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("BAJA_ID"));
	public static final String TRAMITE_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("TRAMITE_ID"));
	public static final String PRODUCTO = Comun._TX(RESOURCE_CONFIGURACION.getString("PRODUCTO"));
	public static final String PRODUCTO_PARTICIPANTE = Comun._TX(RESOURCE_CONFIGURACION.getString("PRODUCTO_PARTICIPANTE"));
	
	/* CUENTAS */
	public static final String GAT_NOMINAL = Comun._TX(RESOURCE_CONFIGURACION.getString("GAT_NOMINAL"));
	public static final String RENDIMIENTO = Comun._TX(RESOURCE_CONFIGURACION.getString("RENDIMIENTO"));
	public static final String GAT_REAL = Comun._TX(RESOURCE_CONFIGURACION.getString("GAT_REAL"));
	public static final String ASESOR_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("ASESOR_ID"));
	public static final String COMO_ENTERO_OBS = Comun._TX(RESOURCE_CONFIGURACION.getString("COMO_ENTERO_OBS"));
	public static final String MONTO_APERTURA = Comun._TX(RESOURCE_CONFIGURACION.getString("MONTO_APERTURA"));
	
	public static final String ESTATUS_CUENTA_ACTIVA = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_CUENTA_ACTIVA"));
	public static final String ESTATUS_CUENTA_CANCELADA = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_CUENTA_CANCELADA"));
	public static final String ESTATUS_CUENTA_BLOQUEDA = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_CUENTA_BLOQUEDA"));
	public static final String ESTATUS_CUENTA_SEGUIMIENTO = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_CUENTA_SEGUIMIENTO"));
	
	public static final String MONEDA_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("MONEDA_ID"));
	public static final String ID_SPEI_DEVOLUCION_PR = Comun._TX(RESOURCE_CONFIGURACION.getString("ID_SPEI_DEVOLUCION_PR"));
	
	
	/* DATOS PARA LLENAR EN LAS REFERENCIAS */
	public static final String TIPO_REFERENCIA_BLU = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_REFERENCIA_BLU"));
	public static final String TIPO_REFERENCIA = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_REFERENCIA"));
	
	/* PERMISOS TRANSACCIONES */
	public static final String TRX_CREAR_CUENTAS_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_CUENTAS_AHORRO"));
	public static final String TRX_CREAR_PRODUCTOS_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_PRODUCTOS_AHORRO"));
	public static final String TRX_CREAR_CONCEPTOS_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_CONCEPTOS_AHORRO"));
	public static final String TRX_RETIRO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_RETIRO_AHORRO"));
	public static final String TRX_DEPOSITO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_DEPOSITO_AHORRO"));
	public static final String TRX_SALDO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_SALDO_AHORRO"));
	public static final String TRX_DEVOLUCION_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_DEVOLUCION_AHORRO"));
	public static final String TRX_CREAR_CLABE = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_CLABE"));
	public static final String TRX_CREAR_EXPEDIENTE = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_EXPEDIENTE"));
	public static final String TRX_ACTUALIZAR_EXPEDIENTE = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_ACTUALIZAR_EXPEDIENTE"));
	public static final String TRX_LEER_DOCUMENTOS = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_LEER_DOCUMENTOS"));
	public static final String TRX_LEER_CATALOGOS = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_LEER_CATALOGOS"));
	public static final String TRX_LEER_CUENTAS = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_LEER_CUENTAS"));
	public static final String TRX_LEER_EXPEDIENTE = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_LEER_EXPEDIENTE"));
	public static final String TRX_CAMBIO_ESTADO_CUENTA = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CAMBIO_ESTADO_CUENTA"));
	
	public static final String TRX_NOTIFICACIONES = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_NOTIFICACIONES"));
	public static final String TRX_MENSAJERIA = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_MENSAJERIA"));
	
	public static final String TRX_CANCELAR_CUENTAS_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CANCELAR_CUENTAS_AHORRO"));
	public static final String TRX_CREAR_CUENTAS_REFERENCIADAS = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CREAR_CUENTAS_REFERENCIADAS"));
	
	
	
	/* TRANSACCIONES PERMITIDAS */
	public static final String TRX_CUENTA_BLU = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CUENTA_BLU"));
	public static final String TRX_CUENTA_FINAL = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CUENTA_FINAL"));
	public static final String APLICATIVO_BLU = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICATIVO_BLU"));
	public static final String APLICATIVO_BLU_FINAL = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICATIVO_BLU_FINAL"));
	
	public static final String TRX_CUENTAS_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TRX_CUENTAS_AHORRO"));
	public static final String APLICATIVO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICATIVO_AHORRO"));
	
	
	/* PROCESOS */
	public static final String PROCESO = Comun._TX(RESOURCE_CONFIGURACION.getString("PROCESO"));
	public static final String DIAS_OPERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("DIAS_OPERACION"));
	
	
	/* DATOS POR DEFAULT PLD */
	public static final String PLD_INGRESO_MENSUAL = Comun._TX(RESOURCE_CONFIGURACION.getString("PLD_INGRESO_MENSUAL"));
	public static final String PLD_MONTO_MAXIMO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("PLD_MONTO_MAXIMO_AHORRO"));
	public static final String PLD_PUESTO = Comun._TX(RESOURCE_CONFIGURACION.getString("PLD_PUESTO"));

	public static final String MR_ACT_CLAVE = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_ACT_CLAVE"));
	public static final String MR_ACT_DESCRIPCION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_ACT_DESCRIPCION"));
	public static final String MR_ACT_PONDERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_ACT_PONDERACION"));

	public static final String MR_GIR_CLAVE = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_GIR_CLAVE"));
	public static final String MR_GIR_DESCRIPCION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_GIR_DESCRIPCION"));
	public static final String MR_GIR_PONDERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_GIR_PONDERACION"));
	
	public static final String MR_DES_CLAVE = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_DES_CLAVE"));
	public static final String MR_DES_DESCRIPCION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_DES_DESCRIPCION"));
	public static final String MR_DES_PONDERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_DES_PONDERACION"));
	
	public static final String MR_LOC_CLAVE = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_LOC_CLAVE"));
	public static final String MR_LOC_DESCRIPCION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_LOC_DESCRIPCION"));
	public static final String MR_LOC_PONDERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_LOC_PONDERACION"));
	
	public static final String MR_OCU_CLAVE = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_OCU_CLAVE"));
	public static final String MR_OCU_DESCRIPCION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_OCU_DESCRIPCION"));
	public static final String MR_OCU_PONDERACION = Comun._TX(RESOURCE_CONFIGURACION.getString("MR_OCU_PONDERACION"));
	
	/* SERVICIOS PLASTICOS */
	public static final String SERVICIOS_SISCOOP = Comun._TX(RESOURCE_CONFIGURACION.getString("SERVICIOS_SISCOOP"));
	
	/* OPERACIONES CON IZEL STI */
	public static final String ESTATUS_ALTA_IZEL_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_ALTA_IZEL_ID"));
	public static final String ESTATUS_BAJA_IZEL_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("ESTATUS_BAJA_IZEL_ID"));
	public static final String APLICACION_IZEL_ID = Comun._TX(RESOURCE_CONFIGURACION.getString("APLICACION_IZEL_ID"));
	
	/* PRODUCTO AHORRO */
	public static final String PRODUCTO_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("PRODUCTO_AHORRO"));
	
	/* TIPO CUENTAS */
	public static final String TIPO_CUENTA_BLU = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_BLU"));
	public static final String TIPO_CUENTA_AHORRO = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_AHORRO"));
	public static final String TIPO_CUENTA_REFERENCIADA = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_REFERENCIADA"));
	public static final String TIPO_CUENTA_ENTRADA = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_ENTRADA"));
	public static final String TIPO_CUENTA_SALIDA = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_SALIDA"));
	public static final String TIPO_CUENTA_TRASPASO = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_TRASPASO"));
	
	/* PLAZAS */
	public static final String PLAZA_DISPONIBLE = Comun._TX(RESOURCE_CONFIGURACION.getString("PLAZA_DISPONIBLE"));
	public static final String PLAZA_NO_DISPONIBLE = Comun._TX(RESOURCE_CONFIGURACION.getString("PLAZA_NO_DISPONIBLE"));

	/* TIPO CUENTAS DE AHORRO */
	public static final String TIPO_CUENTA_AHO_N1 = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_AHO_N1"));
	public static final String TIPO_CUENTA_AHO_N2 = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_AHO_N2"));
	public static final String TIPO_CUENTA_AHO_N3 = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_AHO_N3"));
	public static final String TIPO_CUENTA_AHO_N4 = Comun._TX(RESOURCE_CONFIGURACION.getString("TIPO_CUENTA_AHO_N4"));
	
	/* CREDENCIALES SFTP */
	public static final String SFTP_USER =  Comun._TX(RESOURCE_CONFIGURACION.getString("SFTP_USER"));
	public static final String SFTP_PASS =  Comun._TX(RESOURCE_CONFIGURACION.getString("SFTP_PASS"));
	public static final String SFTP_PORT =  Comun._TX(RESOURCE_CONFIGURACION.getString("SFTP_PORT"));
	public static final String SFTP_HOST =  Comun._TX(RESOURCE_CONFIGURACION.getString("SFTP_HOST"));
	public static final String SFTP_RUTA =  Comun._TX(RESOURCE_CONFIGURACION.getString("SFTP_RUTA"));
	
	public static final String MEDIO_PAGO_VENTANILLA_FACIL = RESOURCE_CONFIGURACION.getString("MEDIO_PAGO_VENTANILLA_FACIL");
	public static final String MEDIO_PAGO_CAJA_ASP = RESOURCE_CONFIGURACION.getString("MEDIO_PAGO_CAJA_ASP");
	public static final String MEDIO_PAGO_SPEI = RESOURCE_CONFIGURACION.getString("MEDIO_PAGO_SPEI");

	public static final String CVE_MOV_DEP_SPEI = RESOURCE_CONFIGURACION.getString("CVE_MOV_DEP_SPEI");
	public static final String WS_ADMIN_PLA =  Comun._TX(RESOURCE_CONFIGURACION.getString("WS_ADMIN_PLA"));
	

	public static final Boolean BANDERA_DEPOSITOS_NUEVOS = Comun._B(RESOURCE_CONFIGURACION.getString("BANDERA_DEPOSITOS_NUEVOS"));
	public static final Boolean BANDERA_DEPOSITOS_TRASPASOS_NUEVOS = Comun._B(RESOURCE_CONFIGURACION.getString("BANDERA_DEPOSITOS_TRASPASOS_NUEVOS"));
	public static final Boolean BANDERA_RETIROS_NUEVOS = Comun._B(RESOURCE_CONFIGURACION.getString("BANDERA_RETIROS_NUEVOS"));
	public static final Boolean BANDERA_RETIROS_TRASPASOS_NUEVOS = Comun._B(RESOURCE_CONFIGURACION.getString("BANDERA_RETIROS_TRASPASOS_NUEVOS"));
	
	public static final String CVE_MOV_DEV_SPEI = Comun._TX(RESOURCE_CONFIGURACION.getString("CVE_MOV_DEV_SPEI"));
	public static final String ID_TIPO_TRANSACCIONES = Comun._TX(RESOURCE_CONFIGURACION.getString("ID_TIPO_TRANSACCIONES"));
	public static final Integer UPDATE_TIPO_ACTUALIZACION = Comun._I(RESOURCE_CONFIGURACION.getString("UPDATE_TIPO_ACTUALIZACION"));
	public static final String CVE_TIPO_TRANS_SPEI = Comun._TX(RESOURCE_CONFIGURACION.getString("CVE_TIPO_TRANS_SPEI"));
	public static final Long TIMEOUT_TRANSACCION_PLASTICOS_SEGUNDOS = Comun._L(RESOURCE_CONFIGURACION.getString("TIMEOUT_TRANSACCION_PLASTICOS_SEGUNDOS"));

	private Constantes(){
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
		log.info("Ruta a buscar el properties del proyecto: {}",ruta);

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
	
	@SuppressWarnings("unused")
	private static String getAmbiente(String bundle) {
		String ambiente = null;
		ResourceBundle rb = null;
		try{
			rb = ResourceBundle.getBundle(bundle, new Locale("es", "Mx"));
			ambiente = Comun._T(rb.getString("AMBIENTE"));
			log.info(String.format("Ambiente = %s : K = %s : %s", ambiente, bundle, rb.toString()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ambiente;
	}
}
