package net.cero.data;

import java.sql.Timestamp;
import java.util.Date;

public class AhorroAvisoRetiroOBJ {

	private Integer avisoId;
	private String cuenta;
	private Date fechaRetiro;
	private Double monto;
	private String estatus;
	private Integer autorizadoPor;
	private Integer creadoPor;
	private Timestamp fechaCrecion;
	private Integer eliminadoPor;
	private Timestamp fechaEliminacion;
	private String sucursal;
	private Integer region;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private Integer movimientoId;
	private String tipoTran;
	private Date fechaRevision;
	
	public Integer getAvisoId() {
		return avisoId;
	}
	public void setAvisoId(Integer avisoId) {
		this.avisoId = avisoId;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaRetiro() {
		return fechaRetiro;
	}
	public void setFechaRetiro(Date fechaRetiro) {
		this.fechaRetiro = fechaRetiro;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public Integer getAutorizadoPor() {
		return autorizadoPor;
	}
	public void setAutorizadoPor(Integer autorizadoPor) {
		this.autorizadoPor = autorizadoPor;
	}
	public Integer getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}
	public Timestamp getFechaCrecion() {
		return fechaCrecion;
	}
	public void setFechaCrecion(Timestamp fechaCrecion) {
		this.fechaCrecion = fechaCrecion;
	}
	public Integer getEliminadoPor() {
		return eliminadoPor;
	}
	public void setEliminadoPor(Integer eliminadoPor) {
		this.eliminadoPor = eliminadoPor;
	}
	public Timestamp getFechaEliminacion() {
		return fechaEliminacion;
	}
	public void setFechaEliminacion(Timestamp fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public Integer getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getMovimientoId() {
		return movimientoId;
	}
	public void setMovimientoId(Integer movimientoId) {
		this.movimientoId = movimientoId;
	}
	public String getTipoTran() {
		return tipoTran;
	}
	public void setTipoTran(String tipoTran) {
		this.tipoTran = tipoTran;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
}