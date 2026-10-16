package net.cero.ahorro.spei.enviospei.nuevospeicero;

import java.io.Serializable;

public class RespuestaSoap implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Integer code;
    private String descripcion;
    
    public RespuestaSoap(){
    	
    }
    
    public RespuestaSoap(Integer code){
    	this.code = code;
    }
    
    public RespuestaSoap(Integer code, String descripcion){
    	this.code = code;
    	this.descripcion = descripcion;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
