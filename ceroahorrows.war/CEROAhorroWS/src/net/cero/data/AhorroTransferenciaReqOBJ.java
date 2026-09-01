package net.cero.data;

import java.util.Date;

public class AhorroTransferenciaReqOBJ {

	private String cuentaOrigen;
	private String cuentaDestino;
	private Date fecha;
	private Double monto;
	private Integer usuarioId;
	private String conceptoOrigen;
	private String conceptoDestino;
	
	/**
	 * @return the cuentaOrigen
	 */
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	/**
	 * @param cuentaOrigen the cuentaOrigen to set
	 */
	public void setCuentaOrigen(String cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
	/**
	 * @return the cuentaDestino
	 */
	public String getCuentaDestino() {
		return cuentaDestino;
	}
	/**
	 * @param cuentaDestino the cuentaDestino to set
	 */
	public void setCuentaDestino(String cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the monto
	 */
	public Double getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	/**
	 * @return the usuarioId
	 */
	public Integer getUsuarioId() {
		return usuarioId;
	}
	/**
	 * @param usuarioId the usuarioId to set
	 */
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	/**
	 * @return the conceptoOrigen
	 */
	public String getConceptoOrigen() {
		return conceptoOrigen;
	}
	/**
	 * @param conceptoOrigen the conceptoOrigen to set
	 */
	public void setConceptoOrigen(String conceptoOrigen) {
		this.conceptoOrigen = conceptoOrigen;
	}
	/**
	 * @return the conceptoDestino
	 */
	public String getConceptoDestino() {
		return conceptoDestino;
	}
	/**
	 * @param conceptoDestino the conceptoDestino to set
	 */
	public void setConceptoDestino(String conceptoDestino) {
		this.conceptoDestino = conceptoDestino;
	}
	
}