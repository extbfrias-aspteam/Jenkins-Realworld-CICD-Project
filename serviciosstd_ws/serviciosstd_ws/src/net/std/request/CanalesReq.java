package net.std.request;

import java.io.Serializable;

public class CanalesReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String aplicativo_clave;
	private String transaccion_clave;
	private String cuenta;
	private Boolean status;
	private Integer usuario_id;
	
	public CanalesReq(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAplicativo_clave() {
		return aplicativo_clave;
	}

	public void setAplicativo_clave(String aplicativo_clave) {
		this.aplicativo_clave = aplicativo_clave;
	}

	public String getTransaccion_clave() {
		return transaccion_clave;
	}

	public void setTransaccion_clave(String transaccion_clave) {
		this.transaccion_clave = transaccion_clave;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Integer usuario_id) {
		this.usuario_id = usuario_id;
	}
}
