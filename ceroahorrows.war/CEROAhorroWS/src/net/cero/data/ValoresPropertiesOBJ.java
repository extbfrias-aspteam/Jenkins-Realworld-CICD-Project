package net.cero.data;

import java.security.Timestamp;

public class ValoresPropertiesOBJ {

	private Integer id;
	private String clave;
	private String valor;
	private Integer usuarioCreacion;
	private Timestamp fechaCreacion;
	private Integer usuarioModificacion;
	private Timestamp fechaModificacion;
	/**
	 * 
	 */
	public ValoresPropertiesOBJ() {
		super();
	}
	/**
	 * @param id
	 * @param clave
	 * @param valor
	 * @param usuarioCreacion
	 * @param fechaCreacion
	 * @param usuarioModificacion
	 * @param fechaModificacion
	 */
	public ValoresPropertiesOBJ(Integer id, String clave, String valor, Integer usuarioCreacion,
			Timestamp fechaCreacion, Integer usuarioModificacion, Timestamp fechaModificacion) {
		super();
		this.id = id;
		this.clave = clave;
		this.valor = valor;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaModificacion = fechaModificacion;
	}
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
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
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
