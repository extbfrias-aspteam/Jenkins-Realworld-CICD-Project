package net.std.request;

import java.io.Serializable;

public class AltaCuentasReq implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String identificador;
	private String cuentas;      /* streaming encriptados */
	
	public AltaCuentasReq(){
		
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getCuentas() {
		return cuentas;
	}

	public void setCuentas(String cuentas) {
		this.cuentas = cuentas;
	}
}
