package net.cero.data;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;


/**
 * The persistent class for the movimientos_caja database table.
 * 
 */
public class AhorroDeposito implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer depositoId;
	private String cuenta;
	private Double monto;
	private Date fecha;
	private Integer formaPago;
	private Integer banco;
	private String noCheque;
	private String observaciones;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private String transaccionId;
	private String tarjetaOperativaId;
	private Integer app;
	private Integer transaccionVersionId;

	public AhorroDeposito() {
		super();
	}
	
	public AhorroDeposito(Integer depositoId, String cuenta, Double monto, Date fecha, Integer formaPago,
			Integer banco, String noCheque, String observaciones, Integer creadoPor, Timestamp fechaCreacion,
			Integer modificadoPor, Timestamp fechaModificacion, String transaccionId, String tarjetaOperativaId,
			Integer app, Integer transaccionVersionId) {
		super();
		this.depositoId = depositoId;
		this.cuenta = cuenta;
		this.monto = monto;
		this.fecha = fecha;
		this.formaPago = formaPago;
		this.banco = banco;
		this.noCheque = noCheque;
		this.observaciones = observaciones;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.transaccionId = transaccionId;
		this.tarjetaOperativaId = tarjetaOperativaId;
		this.app = app;
		this.transaccionVersionId = transaccionVersionId;
	}

	public Integer getDepositoId() {
		return depositoId;
	}

	public void setDepositoId(Integer depositoId) {
		this.depositoId = depositoId;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public String getNoCheque() {
		return noCheque;
	}

	public void setNoCheque(String noCheque) {
		this.noCheque = noCheque;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	public String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(String transaccionId) {
		this.transaccionId = transaccionId;
	}

	public String getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}

	public void setTarjetaOperativaId(String tarjetaOperativaId) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}