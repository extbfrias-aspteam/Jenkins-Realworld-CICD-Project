package net.std.data;

import java.io.Serializable;

public class CuentaReferenciadaVolumenOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String control;
	private Integer consecutivo;
	private String cuenta_concentradora;
	private String cuenta_referencia;
	private String nombre_referencia;
	private String rfc_referencia;
	private String curp_referencia;
	private String correo_referencia;
	private String telefono_referencia;
	private String fecha;
	private Integer procesado;		/* 0 = sin procesar, 1 = procesado */
	private Integer error;			/* 0 = sin error , dif a 0 codigo de error */
	private String observaciones;
	private String tipoCuenta;		/* VALOR DE LA TABLA AHORRO.AHTIPO_CUENTA */
	private String valor;
	private String accion;			/* ELIMINAR / AGREGAR */
	
	private PersonaOBJ solicitante;
	private PersonaOBJ repLegal;
	
	public CuentaReferenciadaVolumenOBJ() {
		
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getCuenta_concentradora() {
		return cuenta_concentradora;
	}

	public void setCuenta_concentradora(String cuenta_concentradora) {
		this.cuenta_concentradora = cuenta_concentradora;
	}

	public String getCuenta_referencia() {
		return cuenta_referencia;
	}

	public void setCuenta_referencia(String cuenta_referencia) {
		this.cuenta_referencia = cuenta_referencia;
	}

	public String getNombre_referencia() {
		return nombre_referencia;
	}

	public void setNombre_referencia(String nombre_referencia) {
		this.nombre_referencia = nombre_referencia;
	}

	public String getRfc_referencia() {
		return rfc_referencia;
	}

	public void setRfc_referencia(String rfc_referencia) {
		this.rfc_referencia = rfc_referencia;
	}

	public String getCurp_referencia() {
		return curp_referencia;
	}

	public void setCurp_referencia(String curp_referencia) {
		this.curp_referencia = curp_referencia;
	}

	public String getCorreo_referencia() {
		return correo_referencia;
	}

	public void setCorreo_referencia(String correo_referencia) {
		this.correo_referencia = correo_referencia;
	}

	public String getTelefono_referencia() {
		return telefono_referencia;
	}

	public void setTelefono_referencia(String telefono_referencia) {
		this.telefono_referencia = telefono_referencia;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Integer getProcesado() {
		return procesado;
	}

	public void setProcesado(Integer procesado) {
		this.procesado = procesado;
	}

	public Integer getError() {
		return error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public PersonaOBJ getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(PersonaOBJ solicitante) {
		this.solicitante = solicitante;
	}

	public PersonaOBJ getRepLegal() {
		return repLegal;
	}

	public void setRepLegal(PersonaOBJ repLegal) {
		this.repLegal = repLegal;
	}
}
	
