package net.std.constantes;

public enum RespuestaEnum {
	SUCCESS("Operacion exitosa "), FAIL("Fallo la operacion "), PERMISOS("No cuenta con los permisos necesarios para esta operacion");
	 
	private String resultado;
 
	private RespuestaEnum(String s) {
		resultado = s;
	}
 
	public String getResultado() {
		return resultado;
	}
}
