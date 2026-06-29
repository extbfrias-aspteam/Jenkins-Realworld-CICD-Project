package net.std.data;

import java.io.Serializable;

public class PlazasClabeOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String clave;
	private String plaza;
	private String propias;
	private String disponible;
	private String statusId;
	private String usuarioId;
	
	public PlazasClabeOBJ(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getPlaza() {
		return plaza;
	}

	public void setPlaza(String plaza) {
		this.plaza = plaza;
	}

	public String getPropias() {
		return propias;
	}

	public void setPropias(String propias) {
		this.propias = propias;
	}

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
}
