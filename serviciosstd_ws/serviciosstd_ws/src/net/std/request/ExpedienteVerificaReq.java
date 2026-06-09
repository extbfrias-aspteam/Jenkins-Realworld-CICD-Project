package net.std.request;

import java.io.Serializable;

public class ExpedienteVerificaReq implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String cuentaId;
	private String observaciones;
	private String verificado;
	private String fechaVerificado;
	
	public ExpedienteVerificaReq(){
		
	}
	
	public ExpedienteVerificaReq(String id, String cuentaId, String observaciones, String verificado, String fechaVerificado){
		this.id = id;             
		this.cuentaId = cuentaId;       
		this.observaciones = observaciones;  
		this.verificado = verificado;     
		this.fechaVerificado = fechaVerificado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(String cuentaId) {
		this.cuentaId = cuentaId;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getVerificado() {
		return verificado;
	}

	public void setVerificado(String verificado) {
		this.verificado = verificado;
	}

	public String getFechaVerificado() {
		return fechaVerificado;
	}

	public void setFechaVerificado(String fechaVerificado) {
		this.fechaVerificado = fechaVerificado;
	}
}
	
