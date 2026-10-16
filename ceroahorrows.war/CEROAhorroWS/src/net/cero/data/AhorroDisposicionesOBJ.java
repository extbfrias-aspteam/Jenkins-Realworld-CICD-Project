package net.cero.data;

import java.security.Timestamp;
import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class AhorroDisposicionesOBJ {
	
	private Integer id;
	private String cuenta;
	private Date fecha;	
	private Double monto;
	private Integer formaPagoId;
	private Integer bancoId;	
	private String cheque;
	private Integer movtoId;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private Integer avisoId;
	private Integer transaccionId;
	private Integer tarjetaOperativaId;
	private Integer app;
	private Integer transaccionVersionId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Integer getFormaPagoId() {
		return formaPagoId;
	}
	public void setFormaPagoId(Integer formaPagoId) {
		this.formaPagoId = formaPagoId;
	}
	public Integer getBancoId() {
		return bancoId;
	}
	public void setBancoId(Integer bancoId) {
		this.bancoId = bancoId;
	}
	public String getCheque() {
		return cheque;
	}
	public void setCheque(String cheque) {
		this.cheque = cheque;
	}
	public Integer getMovtoId() {
		return movtoId;
	}
	public void setMovtoId(Integer movtoId) {
		this.movtoId = movtoId;
	}
	public Integer getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	public Integer getAvisoId() {
		return avisoId;
	}
	public void setAvisoId(Integer avisoId) {
		this.avisoId = avisoId;
	}
	public Integer getTransaccionId() {
		return transaccionId;
	}
	public void setTransaccionId(Integer transaccionId) {
		this.transaccionId = transaccionId;
	}
	public Integer getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}
	public void setTarjetaOperativaId(Integer tarjetaOperativaId) {
		this.tarjetaOperativaId = tarjetaOperativaId;
	}
	public Integer getApp() {
		return app;
	}
	public void setApp(Integer app) {
		this.app = app;
	}
	public Integer getTransaccionVersionId() {
		return transaccionVersionId;
	}
	public void setTransaccionVersionId(Integer transaccionVersionId) {
		this.transaccionVersionId = transaccionVersionId;
	}
}