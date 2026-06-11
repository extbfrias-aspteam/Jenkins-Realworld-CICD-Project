package net.std.data;

import java.io.Serializable;

public class ErrorExpedienteOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String clave;
	
	public ErrorExpedienteOBJ(){
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
	
