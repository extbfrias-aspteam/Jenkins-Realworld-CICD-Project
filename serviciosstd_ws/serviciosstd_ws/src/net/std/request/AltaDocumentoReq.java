package net.std.request;

import java.io.Serializable;

public class AltaDocumentoReq implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String documento;
	private String rutaArchivo;
	private String nombreArchivo;
	private String tipoArchivo;
	
	public AltaDocumentoReq(){
		
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

}
