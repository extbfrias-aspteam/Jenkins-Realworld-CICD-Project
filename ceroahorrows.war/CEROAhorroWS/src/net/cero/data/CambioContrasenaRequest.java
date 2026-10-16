package net.cero.data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CambioContrasenaRequest {
	
	@Valid
	@NotNull(message = "La contraseña nueva es requerida")
	@NotEmpty(message = "La contraseña nueva es requerida")
	private String nuevaContrasena;
	
	@Valid
	@NotNull(message = "El usuario es requerido")
	@NotEmpty(message = "El usuario es requerido")
	private String usuario;
	
	public String getNuevaContrasena() {
		return nuevaContrasena;
	}
	public void setNuevaContrasena(String nuevaContrasena) {
		this.nuevaContrasena = nuevaContrasena;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
