package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class DirectorioTelefonicoOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer idDirectorioTelefonico;
	private String idSolicitante;
	private Integer idCatTelefono;
	private Integer idCompaniaTel;
	private String telefono;
	private String extension;
	private String observaciones;
	private Integer portado;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	/**
	 * 
	 */
	public DirectorioTelefonicoOBJ() {
		super();
	}
	
	/**
	 * @param idDirectorioTelefonico
	 * @param idSolicitante
	 * @param idCatTelefono
	 * @param idCompaniaTel
	 * @param telefono
	 * @param extension
	 * @param observaciones
	 * @param portado
	 */
	public DirectorioTelefonicoOBJ(Integer idDirectorioTelefonico, String idSolicitante, Integer idCatTelefono,
			Integer idCompaniaTel, String telefono, String extension, String observaciones, Integer portado) {
		super();
		this.idDirectorioTelefonico = idDirectorioTelefonico;
		this.idSolicitante = idSolicitante;
		this.idCatTelefono = idCatTelefono;
		this.idCompaniaTel = idCompaniaTel;
		this.telefono = telefono;
		this.extension = extension;
		this.observaciones = observaciones;
		this.portado = portado;
	}

	/**
	 * @return the idDirectorioTelefonico
	 */
	public Integer getIdDirectorioTelefonico() {
		return idDirectorioTelefonico;
	}

	/**
	 * @param idDirectorioTelefonico the idDirectorioTelefonico to set
	 */
	public void setIdDirectorioTelefonico(Integer idDirectorioTelefonico) {
		this.idDirectorioTelefonico = idDirectorioTelefonico;
	}

	/**
	 * @return the idSolicitante
	 */
	public String getIdSolicitante() {
		return idSolicitante;
	}

	/**
	 * @param idSolicitante the idSolicitante to set
	 */
	public void setIdSolicitante(String idSolicitante) {
		this.idSolicitante = idSolicitante;
	}

	/**
	 * @return the idCatTelefono
	 */
	public Integer getIdCatTelefono() {
		return idCatTelefono;
	}

	/**
	 * @param idCatTelefono the idCatTelefono to set
	 */
	public void setIdCatTelefono(Integer idCatTelefono) {
		this.idCatTelefono = idCatTelefono;
	}

	/**
	 * @return the idCompaniaTel
	 */
	public Integer getIdCompaniaTel() {
		return idCompaniaTel;
	}

	/**
	 * @param idCompaniaTel the idCompaniaTel to set
	 */
	public void setIdCompaniaTel(Integer idCompaniaTel) {
		this.idCompaniaTel = idCompaniaTel;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the portado
	 */
	public Integer getPortado() {
		return portado;
	}

	/**
	 * @param portado the portado to set
	 */
	public void setPortado(Integer portado) {
		this.portado = portado;
	}

	/**
	 * @return the creadoPor
	 */
	public Integer getCreadoPor() {
		return creadoPor;
	}

	/**
	 * @param creadoPor the creadoPor to set
	 */
	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return the modificadoPor
	 */
	public Integer getModificadoPor() {
		return modificadoPor;
	}

	/**
	 * @param modificadoPor the modificadoPor to set
	 */
	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	/**
	 * @return the fechaModificacion
	 */
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
}