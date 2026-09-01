package net.cero.data;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the ahorro_pagares database table.
 * 
 */
public class AhorroPagare implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer PagareId;
	private String cuenta;
	private Long numero;
	private Double monto;
	private Double intereses;
	private Date fechaInicio;
	private Date fechaFinal;
	private Integer creadoPor;
	private Date fechaCreacion;
	private Integer modificadoPor;
	private Date fechaModificacion;

	public AhorroPagare() {
		super();
	}

	public Integer getPagareId() {
		return PagareId;
	}

	public void setPagareId(Integer pagareId) {
		PagareId = pagareId;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getIntereses() {
		return intereses;
	}

	public void setIntereses(Double intereses) {
		this.intereses = intereses;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}