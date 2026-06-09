package net.std.request;

import java.io.Serializable;

public class ListExpedientesCuentaReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer cuenta_id;
	
	public ListExpedientesCuentaReq(){
		
	}
	
	public ListExpedientesCuentaReq(Integer cuenta_id){
		this.setCuenta_id(cuenta_id);
	}

	public Integer getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(Integer cuenta_id) {
		this.cuenta_id = cuenta_id;
	}
}
