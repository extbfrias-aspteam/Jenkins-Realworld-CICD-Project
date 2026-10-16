package net.cero.data;

import java.io.Serializable;

import java.util.Date;

/**
 * The persistent class for the ahorro_movimientos database table.
 * 
 */
public class AhorroMovimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer movimientoId;
	private String descripcion;
	private String operacion;
	private Integer creadoPor;
	private Date fechaCreacion;
	private Integer modificadoPor;
	private Date fechaModificacion;
	private String salvoBuenCobro;
	private String cuentaContable;
	private String provisionar;
	private String manual;

	public AhorroMovimiento() {
		super();
	}

	public Integer getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(Integer movimientoId) {
		this.movimientoId = movimientoId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public Integer getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getSalvoBuenCobro() {
		return salvoBuenCobro;
	}

	public void setSalvoBuenCobro(String salvoBuenCobro) {
		this.salvoBuenCobro = salvoBuenCobro;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getProvisionar() {
		return provisionar;
	}

	public void setProvisionar(String provisionar) {
		this.provisionar = provisionar;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
