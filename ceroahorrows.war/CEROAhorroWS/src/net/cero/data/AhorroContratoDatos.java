package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AhorroContratoDatos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String cuenta;
	private String comoEnteroDesc;
	private Integer usuarioCreacion;
	private Date fechaCreacion;
	private Integer usuarioModificacion;
	private Date fechaModificacion;
	private String respReferencia;
	private String respSucursal;
	private Integer idProspectadoPor;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}
	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	/**
	 * @return the comoEnteroDesc
	 */
	public String getComoEnteroDesc() {
		return comoEnteroDesc;
	}
	/**
	 * @param comoEnteroDesc the comoEnteroDesc to set
	 */
	public void setComoEnteroDesc(String comoEnteroDesc) {
		this.comoEnteroDesc = comoEnteroDesc;
	}
	/**
	 * @return the usuarioCreacion
	 */
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	/**
	 * @param usuarioCreacion the usuarioCreacion to set
	 */
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	/**
	 * @return the usuarioModificacion
	 */
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	/**
	 * @return the fechaModificacion
	 */
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the respReferencia
	 */
	public String getRespReferencia() {
		return respReferencia;
	}
	/**
	 * @param respReferencia the respReferencia to set
	 */
	public void setRespReferencia(String respReferencia) {
		this.respReferencia = respReferencia;
	}
	/**
	 * @return the respSucursal
	 */
	public String getRespSucursal() {
		return respSucursal;
	}
	/**
	 * @param respSucursal the respSucursal to set
	 */
	public void setRespSucursal(String respSucursal) {
		this.respSucursal = respSucursal;
	}
	/**
	 * @return the idProspectadoPor
	 */
	public Integer getIdProspectadoPor() {
		return idProspectadoPor;
	}
	/**
	 * @param idProspectadoPor the idProspectadoPor to set
	 */
	public void setIdProspectadoPor(Integer idProspectadoPor) {
		this.idProspectadoPor = idProspectadoPor;
	}
}