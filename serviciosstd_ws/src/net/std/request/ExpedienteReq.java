package net.std.request;

import java.io.Serializable;
import java.util.List;

import net.std.data.ExpedienteOBJ;

public class ExpedienteReq implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String producto;
	private String cuenta;
	private String observaciones;  
	private List<ExpedienteOBJ> lstExpediente;
	
	public ExpedienteReq(){
		
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public List<ExpedienteOBJ> getLstExpediente() {
		return lstExpediente;
	}

	public void setLstExpediente(List<ExpedienteOBJ> lstExpediente) {
		this.lstExpediente = lstExpediente;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}
}
	
