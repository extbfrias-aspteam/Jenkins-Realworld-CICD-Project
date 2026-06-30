package com.asp.eiyu.api.admdocument.gtw.dto;


public class ResponseDTO <T>  {

	private String eiyuResponseCode;
	private String descripcion;
	private T content;
	
	
	
	public ResponseDTO(String eiyuResponseCode, String descripcion, T content) {
		super();
		this.eiyuResponseCode = eiyuResponseCode;
		this.descripcion = descripcion;
		this.content = content;
	}
	public String getEiyuResponseCode() {
		return eiyuResponseCode;
	}
	public void setEiyuResponseCode(String eiyuResponseCode) {
		this.eiyuResponseCode = eiyuResponseCode;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ResponseDTO [eiyuResponseCode=" + eiyuResponseCode + ", descripcion=" + descripcion + ", content="
				+ content + "]";
	}
	
}
