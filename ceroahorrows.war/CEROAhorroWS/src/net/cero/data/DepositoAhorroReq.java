package net.cero.data;

import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class DepositoAhorroReq {
	
	private HeaderWS header;
	private Integer cajaId;
	private Date fecha;
	private String cuenta;
	private Double monto;
	private Integer formaPago;
	private Integer bancoId;
	private String observaciones;
	private String cheque;
	private Integer movimientoId;
	private String cuentaCredito;
	
	public DepositoAhorroReq() {
		super();
	}

	public DepositoAhorroReq(HeaderWS header,Integer cajaId, Date fecha, Integer usuarioId, String cuenta, Double monto,
			Integer formaPago, Integer bancoId, String observaciones, String cheque, Integer movimientoId,String cuentaCredito) {
		super();
		this.header = header;
		this.cajaId = cajaId;
		this.fecha = fecha;
		this.cuenta = cuenta;
		this.monto = monto;
		this.formaPago = formaPago;
		this.bancoId = bancoId;
		this.observaciones = observaciones;
		this.cheque = cheque;
		this.movimientoId = movimientoId;
		this.cuentaCredito = cuentaCredito;
	}

	public HeaderWS getHeader() {
		return header;
	}

	public void setHeader(HeaderWS header) {
		this.header = header;
	}
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public Integer getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(Integer movimientoId) {
		this.movimientoId = movimientoId;
	}

	public String getCuentaCredito() {
		return cuentaCredito;
	}

	public void setCuentaCredito(String cuentaCredito) {
		this.cuentaCredito = cuentaCredito;
	}
}