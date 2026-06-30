package com.asp.eiyu.api.admdocument.dto;

public class DocumentoRegistro {

	private String tipoDocumento;
	
	private String codigo;
	
	private String extension;

	public String getTipodocumento() {
		return tipoDocumento;
	}

	public void setTipodocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	
}
