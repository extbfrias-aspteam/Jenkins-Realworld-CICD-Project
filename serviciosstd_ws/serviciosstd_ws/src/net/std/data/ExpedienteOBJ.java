package net.std.data;

import java.io.Serializable;

public class ExpedienteOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String cuentaId;
	private String cuenta;
	private String documentosAhorroId;
	private String cve_Documento;
	private String rutaAlfresco;
	private String idArchivoAlfresco;
	private String observaciones;
	private String nombre;
	private String fechaExpedicion;
	private String fechaVigencia;
	private String usuarioId;
	private String estatusId;
	private String personaId;
	private String imagen;

	public ExpedienteOBJ(){

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(String cuentaId) {
		this.cuentaId = cuentaId;
	}

	public String getDocumentosAhorroId() {
		return documentosAhorroId;
	}

	public void setDocumentosAhorroId(String documentosAhorroId) {
		this.documentosAhorroId = documentosAhorroId;
	}

	public String getRutaAlfresco() {
		return rutaAlfresco;
	}

	public void setRutaAlfresco(String rutaAlfresco) {
		this.rutaAlfresco = rutaAlfresco;
	}

	public String getIdArchivoAlfresco() {
		return idArchivoAlfresco;
	}

	public void setIdArchivoAlfresco(String idArchivoAlfresco) {
		this.idArchivoAlfresco = idArchivoAlfresco;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFechaExpedicion() {
		return fechaExpedicion;
	}

	public void setFechaExpedicion(String fechaExpedicion) {
		this.fechaExpedicion = fechaExpedicion;
	}

	public String getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getEstatusId() {
		return estatusId;
	}

	public void setEstatusId(String estatusId) {
		this.estatusId = estatusId;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getCve_Documento() {
		return cve_Documento;
	}

	public void setCve_Documento(String cve_Documento) {
		this.cve_Documento = cve_Documento;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getPersonaId() {
		return personaId;
	}

	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}
}
