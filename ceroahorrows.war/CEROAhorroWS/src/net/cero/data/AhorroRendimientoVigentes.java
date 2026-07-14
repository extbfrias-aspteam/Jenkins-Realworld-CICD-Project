package net.cero.data;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;

/**
 * The persistent class for the ahorro_movimientos database table.
 * 
 */
public class AhorroRendimientoVigentes implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer rendimientoVigenteId;
	private String cuenta;
	private Integer tipoAhorroId;
	private Integer periodicidad;
	private Integer plazo;
	private String tipoTasa;
	private String calculoIva;
	private Double tasaInt;
	private Integer tasaId;
	private Integer base;
	private String formula;
	private String tipoCorte;
	private String deposito;
	private Integer numDias;
	private Integer diasGracia;
	private String ctaContable;
	private Date vigenciaDesde;
	private Date vigenciaHasta;
	private String condicionesRetiros;
	private String condicionesApertura;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private Integer rendimientoId;
	private Double gat;
	private Integer tipoCapitalizarId;
	private Integer noDisposicion;
	private String estatus;
	private Integer tipo;
	private Double capital;
	private Date fechaInicio;
	private Date fechaFinal;
	private Double interes;
	private Date fechaDeposito;

	public AhorroRendimientoVigentes() {
		super();
	}

	public AhorroRendimientoVigentes(Integer rendimientoVigenteId, String cuenta, Integer tipoAhorroId,
			Integer periodicidad, Integer plazo, String tipoTasa, String calculoIva, Double tasaInt, Integer tasaId,
			Integer base, String formula, String tipoCorte, String deposito, Integer numDias, Integer diasGracia,
			String ctaContable, Date vigenciaDesde, Date vigenciaHasta, String condicionesRetiros,
			String condicionesApertura, Integer creadoPor, Timestamp fechaCreacion, Integer modificadoPor,
			Timestamp fechaModificacion, Integer rendimientoId, Double gat, Integer tipoCapitalizarId,
			Integer noDisposicion, String estatus, Integer tipo, Double capital, Date fechaInicio, Date fechaFinal,
			Double interes, Date fechaDeposito) {
		super();
		this.rendimientoVigenteId = rendimientoVigenteId;
		this.cuenta = cuenta;
		this.tipoAhorroId = tipoAhorroId;
		this.periodicidad = periodicidad;
		this.plazo = plazo;
		this.tipoTasa = tipoTasa;
		this.calculoIva = calculoIva;
		this.tasaInt = tasaInt;
		this.tasaId = tasaId;
		this.base = base;
		this.formula = formula;
		this.tipoCorte = tipoCorte;
		this.deposito = deposito;
		this.numDias = numDias;
		this.diasGracia = diasGracia;
		this.ctaContable = ctaContable;
		this.vigenciaDesde = vigenciaDesde;
		this.vigenciaHasta = vigenciaHasta;
		this.condicionesRetiros = condicionesRetiros;
		this.condicionesApertura = condicionesApertura;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.rendimientoId = rendimientoId;
		this.gat = gat;
		this.tipoCapitalizarId = tipoCapitalizarId;
		this.noDisposicion = noDisposicion;
		this.estatus = estatus;
		this.tipo = tipo;
		this.capital = capital;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.interes = interes;
		this.fechaDeposito = fechaDeposito;
	}

	public Integer getRendimientoVigenteId() {
		return rendimientoVigenteId;
	}

	public void setRendimientoVigenteId(Integer rendimientoVigenteId) {
		this.rendimientoVigenteId = rendimientoVigenteId;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Integer getTipoAhorroId() {
		return tipoAhorroId;
	}

	public void setTipoAhorroId(Integer tipoAhorroId) {
		this.tipoAhorroId = tipoAhorroId;
	}

	public Integer getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(Integer periodicidad) {
		this.periodicidad = periodicidad;
	}

	public Integer getPlazo() {
		return plazo;
	}

	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}

	public String getTipoTasa() {
		return tipoTasa;
	}

	public void setTipoTasa(String tipoTasa) {
		this.tipoTasa = tipoTasa;
	}

	public String getCalculoIva() {
		return calculoIva;
	}

	public void setCalculoIva(String calculoIva) {
		this.calculoIva = calculoIva;
	}

	public Double getTasaInt() {
		return tasaInt;
	}

	public void setTasaInt(Double tasaInt) {
		this.tasaInt = tasaInt;
	}

	public Integer getTasaId() {
		return tasaId;
	}

	public void setTasaId(Integer tasaId) {
		this.tasaId = tasaId;
	}

	public Integer getBase() {
		return base;
	}

	public void setBase(Integer base) {
		this.base = base;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getTipoCorte() {
		return tipoCorte;
	}

	public void setTipoCorte(String tipoCorte) {
		this.tipoCorte = tipoCorte;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public Integer getNumDias() {
		return numDias;
	}

	public void setNumDias(Integer numDias) {
		this.numDias = numDias;
	}

	public Integer getDiasGracia() {
		return diasGracia;
	}

	public void setDiasGracia(Integer diasGracia) {
		this.diasGracia = diasGracia;
	}

	public String getCtaContable() {
		return ctaContable;
	}

	public void setCtaContable(String ctaContable) {
		this.ctaContable = ctaContable;
	}

	public Date getVigenciaDesde() {
		return vigenciaDesde;
	}

	public void setVigenciaDesde(Date vigenciaDesde) {
		this.vigenciaDesde = vigenciaDesde;
	}

	public Date getVigenciaHasta() {
		return vigenciaHasta;
	}

	public void setVigenciaHasta(Date vigenciaHasta) {
		this.vigenciaHasta = vigenciaHasta;
	}

	public String getCondicionesRetiros() {
		return condicionesRetiros;
	}

	public void setCondicionesRetiros(String condicionesRetiros) {
		this.condicionesRetiros = condicionesRetiros;
	}

	public String getCondicionesApertura() {
		return condicionesApertura;
	}

	public void setCondicionesApertura(String condicionesApertura) {
		this.condicionesApertura = condicionesApertura;
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

	public Integer getRendimientoId() {
		return rendimientoId;
	}

	public void setRendimientoId(Integer rendimientoId) {
		this.rendimientoId = rendimientoId;
	}

	public Double getGat() {
		return gat;
	}

	public void setGat(Double gat) {
		this.gat = gat;
	}

	public Integer getTipoCapitalizarId() {
		return tipoCapitalizarId;
	}

	public void setTipoCapitalizarId(Integer tipoCapitalizarId) {
		this.tipoCapitalizarId = tipoCapitalizarId;
	}

	public Integer getNoDisposicion() {
		return noDisposicion;
	}

	public void setNoDisposicion(Integer noDisposicion) {
		this.noDisposicion = noDisposicion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
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

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}
}
