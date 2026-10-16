package net.cero.data;

import java.io.Serializable;

import java.util.Date;

/**
 * The persistent class for the ahorro_movimientos database table.
 * 
 */
public class AhorroSaldos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ahorroSaldosId;
	private String cuenta;
	private String solicitanteId;
	private Double saldoReal;
	private Double saldoPromedio;
	private Double saldoAcumulado;
	private Date fechaCorte;
	private Date fechaDeposito;
	private Double intereses;
	private Double iva;
	private Double isr;
	private Double retenciones;
	private Double desviacion;
	private Integer dias;
	private Double saldoDisponible;
	private Double ide;

	public AhorroSaldos() {
		super();
	}

	public AhorroSaldos(Integer ahorroSaldosId, String cuenta, String solicitanteId, Double saldoReal,
			Double saldoPromedio, Double saldoAcumulado, Date fechaCorte, Date fechaDeposito, Double intereses,
			Double iva, Double isr, Double retenciones, Double desviacion, Integer dias, Double saldoDisponible,
			Double ide) {
		super();
		this.ahorroSaldosId = ahorroSaldosId;
		this.cuenta = cuenta;
		this.solicitanteId = solicitanteId;
		this.saldoReal = saldoReal;
		this.saldoPromedio = saldoPromedio;
		this.saldoAcumulado = saldoAcumulado;
		this.fechaCorte = fechaCorte;
		this.fechaDeposito = fechaDeposito;
		this.intereses = intereses;
		this.iva = iva;
		this.isr = isr;
		this.retenciones = retenciones;
		this.desviacion = desviacion;
		this.dias = dias;
		this.saldoDisponible = saldoDisponible;
		this.ide = ide;
	}

	public Integer getAhorroSaldosId() {
		return ahorroSaldosId;
	}

	public void setAhorroSaldosId(Integer ahorroSaldosId) {
		this.ahorroSaldosId = ahorroSaldosId;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getSolicitanteId() {
		return solicitanteId;
	}

	public void setSolicitanteId(String solicitanteId) {
		this.solicitanteId = solicitanteId;
	}

	public Double getSaldoReal() {
		return saldoReal;
	}

	public void setSaldoReal(Double saldoReal) {
		this.saldoReal = saldoReal;
	}

	public Double getSaldoPromedio() {
		return saldoPromedio;
	}

	public void setSaldoPromedio(Double saldoPromedio) {
		this.saldoPromedio = saldoPromedio;
	}

	public Double getSaldoAcumulado() {
		return saldoAcumulado;
	}

	public void setSaldoAcumulado(Double saldoAcumulado) {
		this.saldoAcumulado = saldoAcumulado;
	}

	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public Double getIntereses() {
		return intereses;
	}

	public void setIntereses(Double intereses) {
		this.intereses = intereses;
	}

	public Double getIva() {
		return iva;
	}

	public void setIva(Double iva) {
		this.iva = iva;
	}

	public Double getIsr() {
		return isr;
	}

	public void setIsr(Double isr) {
		this.isr = isr;
	}

	public Double getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(Double retenciones) {
		this.retenciones = retenciones;
	}

	public Double getDesviacion() {
		return desviacion;
	}

	public void setDesviacion(Double desviacion) {
		this.desviacion = desviacion;
	}

	public Integer getDias() {
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Double getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(Double saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	public Double getIde() {
		return ide;
	}

	public void setIde(Double ide) {
		this.ide = ide;
	}
}
