package net.std.data;

import java.io.Serializable;

import net.std.constantes.Comun;

public class ExpedienteCompletoOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String cuenta_id;
	private String cuenta;
	private String fecha_apertura;
	private String documento_id;
	private String cve_documento;
	private String documento;
	private String ruta_alfresco;
	private String alfresco_id;
	private String observaciones;
	private String nombre_archivo;
	private String fecha_expedicion;
	private String fecha_vigencia;
	private String estatus_id;
	private String estatus;
	private String verificado;
	private String fecha_verificado;
	private byte[] imagen;
	//private StreamedContent doctoAlfresco;
	private Object doctoAlfresco;
	private String doctoTipo;
	
	private String nuevas_observaciones;
	private Boolean bVerificado; 
	
	public ExpedienteCompletoOBJ(){
		nuevas_observaciones = "";
		bVerificado = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(String cuenta_id) {
		this.cuenta_id = cuenta_id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getFecha_apertura() {
		return fecha_apertura;
	}

	public void setFecha_apertura(String fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}

	public String getDocumento_id() {
		return documento_id;
	}

	public void setDocumento_id(String documento_id) {
		this.documento_id = documento_id;
	}

	public String getCve_documento() {
		return cve_documento;
	}

	public void setCve_documento(String cve_documento) {
		this.cve_documento = cve_documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getRuta_alfresco() {
		return ruta_alfresco;
	}

	public void setRuta_alfresco(String ruta_alfresco) {
		this.ruta_alfresco = ruta_alfresco;
	}

	public String getAlfresco_id() {
		return alfresco_id;
	}

	public void setAlfresco_id(String alfresco_id) {
		this.alfresco_id = alfresco_id;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getNombre_archivo() {
		return nombre_archivo;
	}

	public void setNombre_archivo(String nombre_archivo) {
		this.nombre_archivo = nombre_archivo;
	}

	public String getFecha_expedicion() {
		return fecha_expedicion;
	}

	public void setFecha_expedicion(String fecha_expedicion) {
		this.fecha_expedicion = fecha_expedicion;
	}

	public String getFecha_vigencia() {
		return fecha_vigencia;
	}

	public void setFecha_vigencia(String fecha_vigencia) {
		this.fecha_vigencia = fecha_vigencia;
	}

	public String getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(String estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNuevas_observaciones() {
		return nuevas_observaciones;
	}

	public void setNuevas_observaciones(String nuevas_observaciones) {
		this.nuevas_observaciones = nuevas_observaciones;
	}
	
	public void addObservaciones(String nuevas_observaciones){
		this.observaciones = String.format("%s\n%s", Comun._T(this.observaciones), Comun._T(nuevas_observaciones));
	}

	public Object getDoctoAlfresco() {
		return doctoAlfresco;
	}

	public void setDoctoAlfresco(Object doctoAlfresco) {
		this.doctoAlfresco = doctoAlfresco;
	}

	public String getDoctoTipo() {
		return doctoTipo;
	}

	public void setDoctoTipo(String doctoTipo) {
		this.doctoTipo = doctoTipo;
	}

	public String getVerificado() {
		return verificado;
	}

	public void setVerificado(String verificado) {
		this.verificado = verificado;
	}

	public String getFecha_verificado() {
		return fecha_verificado;
	}

	public void setFecha_verificado(String fecha_verificado) {
		this.fecha_verificado = fecha_verificado;
	}

	public Boolean getbVerificado() {
		//this.bVerificado =  "S".equals(Comun._T(this.verificado)) ? true : false;
		return bVerificado;
	}

	public void setbVerificado(Boolean bVerificado) {
		this.bVerificado = bVerificado;
	}
}
