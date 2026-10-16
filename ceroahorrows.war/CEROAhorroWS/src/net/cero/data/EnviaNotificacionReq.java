package net.cero.data;

public class EnviaNotificacionReq {

	private Integer tipoNotificacion; 
	private SMS mensaje;
	
	/**
	 * @return the tipoNotificacion
	 */
	public Integer getTipoNotificacion() {
		return tipoNotificacion;
	}
	/**
	 * @param tipoNotificacion the tipoNotificacion to set
	 */
	public void setTipoNotificacion(Integer tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}
	/**
	 * @return the mensaje
	 */
	public SMS getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(SMS mensaje) {
		this.mensaje = mensaje;
	}
}
