package net.cero.data;

import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class CajaDisposicionAhorroReq {
	
	private Integer cajaId;
	private Date fecha;
	private Integer usuarioId;
	private String cuentaAhorro;
	private Double monto;
	private Integer formaPago;
	private Integer bancoId;
	private String observacion;
	private String cheque;
	private Integer movtoId;
	private Integer transaccionId;
	private Integer tarjetaOperativaId;
	private Integer app;
	private Integer transaccionVersionId;
	private Integer bancoClie;
	private Integer avisoId;
	private Integer speiTransferenciaId;
	
	public Integer getCajaId() {
		return cajaId;
	}
	public void setCajaId(Integer cajaId) {
		this.cajaId = cajaId;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Integer getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	public String getCuentaAhorro() {
		return cuentaAhorro;
	}
	public void setCuentaAhorro(String cuentaAhorro) {
		this.cuentaAhorro = cuentaAhorro;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Integer getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}
	public Integer getBancoId() {
		return bancoId;
	}
	public void setBancoId(Integer bancoId) {
		this.bancoId = bancoId;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	public Integer getBancoClie() {
		return bancoClie;
	}
	public void setBancoClie(Integer bancoClie) {
		this.bancoClie = bancoClie;
	}
	public Integer getAvisoId() {
		return avisoId;
	}
	public void setAvisoId(Integer avisoId) {
		this.avisoId = avisoId;
	}
	public Integer getSpeiTransferenciaId() {
		return speiTransferenciaId;
	}
	public void setSpeiTransferenciaId(Integer speiTransferenciaId) {
		this.speiTransferenciaId = speiTransferenciaId;
	}
	
	
}
