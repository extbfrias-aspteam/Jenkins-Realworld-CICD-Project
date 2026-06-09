package net.std.constantes;

public enum SeControlEnum {
	SUCCESS("Validacion de Control exitosa"), FAIL("Fallo Validacion de Control"), PETICION(" en la Peticion"), CANAL(" del Canal"), 
	SUCURSAL(" de la Suscursal"), COMISIONISTA(" de Comisionista"), USUARIO(" del Usuario"),	TERMINAL(" de la Terminal"), TRANSACCION(" en la Transaccion");
	 
	private String resultado;
 
	private SeControlEnum(String s) {
		resultado = s;
	}
 
	public String getResultado() {
		return resultado;
	}
}
