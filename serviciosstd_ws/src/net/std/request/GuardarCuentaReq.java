package net.std.request;

import net.cero.ws.data.HeaderWS;
import net.std.data.CuentaOBJ;

//import net.cero.seguridad.utilidades.HeaderWS;

public class GuardarCuentaReq {
	
	private HeaderWS header;
	private CuentaOBJ cuenta;
	
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public CuentaOBJ getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaOBJ cuenta) {
		this.cuenta = cuenta;
	}
	
	
	
}
