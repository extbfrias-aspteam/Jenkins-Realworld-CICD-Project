package net.std.request;

import java.io.Serializable;
import java.util.Date;

public class CuentaReq implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String cuenta;
	private Integer estatusId;
	private String personaId;
	private Integer productoAhorroId;
	private Date fechaApertura;
	private Double montoApertura;
	private Integer sucursalId;
	private Double rendimiento;
	private Integer monedaId;
	private Double gatNominal;
	private Double gatReal;
	private Integer asesorId;
	private Integer comoEnteroId;
	private String comoEnteroObs;
	private String clabeInterbancaria;
	private String referencia;
	private String strFechaApertura;
	private String estatusClave;
	private String estatus;
	private String valConcepto;
	private Integer conceptoID;
	private String conceptoClave;
	private String conceptoValor;
	
		
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
	public Integer getEstatusId() {
		return estatusId;
	}
	public void setEstatusId(Integer estatusId) {
		this.estatusId = estatusId;
	}
	public String getPersonaId() {
		return personaId;
	}
	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}
	public Integer getProductoAhorroId() {
		return productoAhorroId;
	}
	public void setProductoAhorroId(Integer productoAhorroId) {
		this.productoAhorroId = productoAhorroId;
	}
	public Date getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public Double getMontoApertura() {
		return montoApertura;
	}
	public void setMontoApertura(Double montoApertura) {
		this.montoApertura = montoApertura;
	}
	public Integer getSucursalId() {
		return sucursalId;
	}
	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}
	public Double getRendimiento() {
		return rendimiento;
	}
	public void setRendimiento(Double rendimiento) {
		this.rendimiento = rendimiento;
	}
	public Integer getMonedaId() {
		return monedaId;
	}
	public void setMonedaId(Integer monedaId) {
		this.monedaId = monedaId;
	}
	public Double getGatNominal() {
		return gatNominal;
	}
	public void setGatNominal(Double gatNominal) {
		this.gatNominal = gatNominal;
	}
	public Double getGatReal() {
		return gatReal;
	}
	public void setGatReal(Double gatReal) {
		this.gatReal = gatReal;
	}
	public Integer getAsesorId() {
		return asesorId;
	}
	public void setAsesorId(Integer asesorId) {
		this.asesorId = asesorId;
	}
	public Integer getComoEnteroId() {
		return comoEnteroId;
	}
	public void setComoEnteroId(Integer comoEnteroId) {
		this.comoEnteroId = comoEnteroId;
	}
	public String getComoEnteroObs() {
		return comoEnteroObs;
	}
	public void setComoEnteroObs(String comoEnteroObs) {
		this.comoEnteroObs = comoEnteroObs;
	}
	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}
	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public String getStrFechaApertura() {
		return strFechaApertura;
	}

	public void setStrFechaApertura(String strFechaApertura) {
		this.strFechaApertura = strFechaApertura;
	}

	public String getEstatusClave() {
		return estatusClave;
	}

	public void setEstatusClave(String estatusClave) {
		this.estatusClave = estatusClave;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Integer getConceptoID() {
		return conceptoID;
	}

	public void setConceptoID(Integer conceptoID) {
		this.conceptoID = conceptoID;
	}

	public String getConceptoClave() {
		return conceptoClave;
	}

	public void setConceptoClave(String conceptoClave) {
		this.conceptoClave = conceptoClave;
	}

	public String getConceptoValor() {
		return conceptoValor;
	}

	public void setConceptoValor(String conceptoValor) {
		this.conceptoValor = conceptoValor;
	}

	public String getValConcepto() {
		return valConcepto;
	}

	public void setValConcepto(String valConcepto) {
		this.valConcepto = valConcepto;
	}
}
