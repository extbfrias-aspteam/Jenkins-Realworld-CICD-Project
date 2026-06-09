package net.std.response;

import java.io.Serializable;

import net.std.data.ErrorExpedienteOBJ;
import net.std.data.ImagenOBJ;

public class ExpedienteRes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ErrorExpedienteOBJ error;
	private ImagenOBJ imagenAlfresco;
	
	public ExpedienteRes(){
		
	}

	public ImagenOBJ getImagenAlfresco() {
		return imagenAlfresco;
	}

	public void setImagenAlfresco(ImagenOBJ imagenAlfresco) {
		this.imagenAlfresco = imagenAlfresco;
	}

	public ErrorExpedienteOBJ getError() {
		return error;
	}

	public void setError(ErrorExpedienteOBJ error) {
		this.error = error;
	}
}
	
