package net.std.data;

import java.io.Serializable;

public class CuentaCompletaOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String cuenta;
	private String referencia;
	private String producto_id;
	private String cve_producto;
	private String producto;
	private String estatus_id;
	private String estatus;
	private String persona_id;
	private String fecha_apertura;
	private String monto_apertura;
	private String sucursal_id;
	private String sucursal;
	private String clabe;
		
	public CuentaCompletaOBJ(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getProducto_id() {
		return producto_id;
	}

	public void setProducto_id(String producto_id) {
		this.producto_id = producto_id;
	}

	public String getCve_producto() {
		return cve_producto;
	}

	public void setCve_producto(String cve_producto) {
		this.cve_producto = cve_producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(String estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getPersona_id() {
		return persona_id;
	}

	public void setPersona_id(String persona_id) {
		this.persona_id = persona_id;
	}

	public String getFecha_apertura() {
		return fecha_apertura;
	}

	public void setFecha_apertura(String fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}

	public String getMonto_apertura() {
		return monto_apertura;
	}

	public void setMonto_apertura(String monto_apertura) {
		this.monto_apertura = monto_apertura;
	}

	public String getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(String sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getClabe() {
		return clabe;
	}

	public void setClabe(String clabe) {
		this.clabe = clabe;
	}
}
