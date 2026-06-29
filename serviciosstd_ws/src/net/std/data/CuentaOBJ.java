package net.std.data;

import java.io.Serializable;
import java.util.Date;

public class CuentaOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String cuenta;
	private Integer estatusId;
	private String personaId;
	private Integer productoAhorroId;
	private String productoAhorro;
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
	private String tipoCliente;
	private String clabeEje;
	private Integer bloqueadoId;
	private String bloqueado;
	private String fecha_bloqueado;
	private Integer pan_id;
	private String pan;
	private String con_plastico;
	private String base;
	private String cuenta_referencia;
	private String tarjeta_principal;
	
	public CuentaOBJ(){
		
	}
		
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

	public String getProductoAhorro() {
		return productoAhorro;
	}

	public void setProductoAhorro(String productoAhorro) {
		this.productoAhorro = productoAhorro;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getClabeEje() {
		return clabeEje;
	}

	public void setClabeEje(String clabeEje) {
		this.clabeEje = clabeEje;
	}

	public Integer getBloqueadoId() {
		return bloqueadoId;
	}

	public void setBloqueadoId(Integer bloqueadoId) {
		this.bloqueadoId = bloqueadoId;
	}

	public String getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getFecha_bloqueado() {
		return fecha_bloqueado;
	}

	public void setFecha_bloqueado(String fecha_bloqueado) {
		this.fecha_bloqueado = fecha_bloqueado;
	}

	public Integer getPan_id() {
		return pan_id;
	}

	public void setPan_id(Integer pan_id) {
		this.pan_id = pan_id;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getCon_plastico() {
		return con_plastico;
	}

	public void setCon_plastico(String con_plastico) {
		this.con_plastico = con_plastico;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCuenta_referencia() {
		return cuenta_referencia;
	}

	public void setCuenta_referencia(String cuenta_referencia) {
		this.cuenta_referencia = cuenta_referencia;
	}

	public String getTarjeta_principal() {
		return tarjeta_principal;
	}

	public void setTarjeta_principal(String tarjeta_principal) {
		this.tarjeta_principal = tarjeta_principal;
	}

}
