package net.std.data;

import java.io.Serializable;

public class ImagenOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String mensaje;
	private String codigoError;
	private String idImagen;
	private String nombre;
	
	public ImagenOBJ(){
		
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getIdImagen() {
		return idImagen;
	}

	public void setIdImagen(String idImagen) {
		this.idImagen = idImagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
	
