package net.std.request;

import java.io.Serializable;

public class CuentaCanalReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cuentaClabe;
	private String permiso;
	
	public CuentaCanalReq(){
		
	}

	public String getCuentaClabe() {
		return cuentaClabe;
	}

	public void setCuentaClabe(String cuentaClabe) {
		this.cuentaClabe = cuentaClabe;
	}

	public String getPermiso() {
		return permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}
}
