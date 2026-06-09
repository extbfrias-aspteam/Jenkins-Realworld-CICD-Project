package net.std.data;

import java.io.Serializable;

public class NotificacionOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String fecha;
	private String notificacion;
	
	public NotificacionOBJ() {
		
	}
	
	public NotificacionOBJ(String fecha, String notificacion) {
		this.fecha = fecha;
		this.notificacion = notificacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}
}
	
