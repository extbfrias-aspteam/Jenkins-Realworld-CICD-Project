package net.std.constantes;

import java.util.HashMap;

public class ErrProd { 

	public static final int ERROR_INESPERADO = 101;
	public static final int ERROR_NOREGISTRO_PRODUCTO = 102;
	public static final int ERROR_CATALOGO = 103;
	public static final int ERROR_PARAMETROS = 104;
	public static final int ERROR_CONCEPTOS = 105;
	public static final int ERROR_MODULOS = 106;
	public static final int ERROR_DOCUMENTOS = 107;
	public static final int ERROR_INSERTAR_EXPEDIENTE = 108;
	public static final int ERROR_LEER_EXPEDIENTE = 109;
	public static final int ERROR_ACTUALIZAR_EXPEDIENTE = 110;
	public static final int ERROR_INSERTAR_ALFRESCO = 111;
	public static final int ERROR_LEER_ALFRESCO = 112;
	public static final int ERROR_LONGITUD_ARCHIVO = 113;
	public static final int ERROR_DEVOLUCION_EXISTENTE = 114;
	
	
	
	
	
	private static HashMap<Integer, String> descError = new HashMap<Integer, String>();

	static {
		descError.put(ERROR_INESPERADO,"OCURRIO UN ERROR, CONSULTE CON EL SUPERVISOR DE AREA : %s");
		descError.put(ERROR_NOREGISTRO_PRODUCTO,"NO SE REGISTRO EL PRODUCTO : %s");
		descError.put(ERROR_CATALOGO,"NO EXISTE LA CLAVE : %s");
		descError.put(ERROR_PARAMETROS,"FALTA DE PARAMETRO POR RECIBIR : %s");
		descError.put(ERROR_CONCEPTOS,"NO SE CREO EL CONCEPTO PRODUCTO : %s");
		descError.put(ERROR_MODULOS,"NO EXISTE EL MODULO  : %s");
		descError.put(ERROR_DOCUMENTOS,"NO EXISTE EL DOCUMENTO  : %s");
		
		descError.put(ERROR_INSERTAR_EXPEDIENTE,"NO SE INSERTO EL EXPEDIENTE %s");
		descError.put(ERROR_LEER_EXPEDIENTE,"SIN REGISTROS %s");
		descError.put(ERROR_ACTUALIZAR_EXPEDIENTE,"NO SE ACTUALIZO EL EXPEDIENTE %s");
		
		descError.put(ERROR_INSERTAR_ALFRESCO,"NO SE INSERTO EL EXPEDIENTE EN RESPOSITORIO DE DOCUMENTOS ALFRESCO %s");
		descError.put(ERROR_LEER_ALFRESCO,"NO SE OBTUVO LA IMAGEN DEL EXPEDIENTE EN RESPOSITORIO DE DOCUMENTOS ALFRESCO %s");

		descError.put(ERROR_LONGITUD_ARCHIVO,"ERROR LIMITE ARCHIVO EXCEDIDO AL PERMITIDO HASTA %s");
		
		
		descError.put(ERROR_DEVOLUCION_EXISTENTE,"ERROR DEVOLUCION EXISTENTE");
		
		
		
	}
	
	public static String desc(Integer pos, Object ... args){
		return String.format(descError.get(pos), args == null || args.length == 0 ? "" : args[0]);
	}
}


