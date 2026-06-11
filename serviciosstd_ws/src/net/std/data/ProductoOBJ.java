package net.std.data;

import java.io.Serializable;

public class ProductoOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String clave;
	private String descripcion;
	private String monto_minimo;
	private String monto_maximo;
	private String fecha_activacion;
	private String estatus_id;
	private String estatus;
	private String moneda_id;
	private String moneda;
	private String clave_cnbv;
	private String tipo_producto_ahorro_id;
	
	public ProductoOBJ(){
		
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMonto_minimo() {
		return monto_minimo;
	}

	public void setMonto_minimo(String monto_minimo) {
		this.monto_minimo = monto_minimo;
	}

	public String getMonto_maximo() {
		return monto_maximo;
	}

	public void setMonto_maximo(String monto_maximo) {
		this.monto_maximo = monto_maximo;
	}

	public String getFecha_activacion() {
		return fecha_activacion;
	}

	public void setFecha_activacion(String fecha_activacion) {
		this.fecha_activacion = fecha_activacion;
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

	public String getMoneda_id() {
		return moneda_id;
	}

	public void setMoneda_id(String moneda_id) {
		this.moneda_id = moneda_id;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getClave_cnbv() {
		return clave_cnbv;
	}

	public void setClave_cnbv(String clave_cnbv) {
		this.clave_cnbv = clave_cnbv;
	}

	public String getTipo_producto_ahorro_id() {
		return tipo_producto_ahorro_id;
	}

	public void setTipo_producto_ahorro_id(String tipo_producto_ahorro_id) {
		this.tipo_producto_ahorro_id = tipo_producto_ahorro_id;
	}
}
