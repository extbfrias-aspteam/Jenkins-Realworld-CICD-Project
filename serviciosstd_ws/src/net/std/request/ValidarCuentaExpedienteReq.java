package net.std.request;

import java.io.Serializable;


public class ValidarCuentaExpedienteReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cuentaId;
	private String cuenta;
	private String estado;
	private String usuarioId;
	private String concepto;
	private String validar;
	
	public ValidarCuentaExpedienteReq(){
		
	}

	public String getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(String cuentaId) {
		this.cuentaId = cuentaId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getValidar() {
		return validar;
	}

	public void setValidar(String validar) {
		this.validar = validar;
	}
}
