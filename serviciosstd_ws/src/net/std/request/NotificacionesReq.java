package net.std.request;

import java.io.Serializable;

public class NotificacionesReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cuentaId;
	private String usuarioId;
	private String notificacion;
	
	public NotificacionesReq(){
		
	}
	
	public NotificacionesReq(String cuentaId, String notificacion, String usuarioId){
		this.cuentaId = cuentaId;
		this.notificacion = notificacion;
		this.usuarioId = usuarioId;
	}

	public String getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(String cuentaId) {
		this.cuentaId = cuentaId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}
}
