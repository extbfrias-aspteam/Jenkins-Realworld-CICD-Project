package net.cero.data;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;

/**
 * The persistent class for the ahorro_movimientos database table.
 * 
 */
public class AhorroIdeValores implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ahorroIdeValoresId;
	private Date fechaInicio;
	private Date fechaFinal;
	private Double monto;
	private Double porcentaje;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;

	public AhorroIdeValores() {
		super();
	}

	public AhorroIdeValores(Integer ahorroIdeValoresId, Date fechaInicio, Date fechaFinal, Double monto,
			Double porcentaje, Integer creadoPor, Timestamp fechaCreacion, Integer modificadoPor,
			Timestamp fechaModificacion) {
		super();
		this.ahorroIdeValoresId = ahorroIdeValoresId;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.monto = monto;
		this.porcentaje = porcentaje;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}

	public Integer getAhorroIdeValoresId() {
		return ahorroIdeValoresId;
	}

	public void setAhorroIdeValoresId(Integer ahorroIdeValoresId) {
		this.ahorroIdeValoresId = ahorroIdeValoresId;
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

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
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
}
