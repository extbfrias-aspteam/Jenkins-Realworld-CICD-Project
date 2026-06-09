package net.std.response;

import java.io.Serializable;
import java.util.Map;

public class AltaCuentasReferenciadaRes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long codigoError;  // 0 = OK , <> 0 = ERROR
	private String descError;
	private String nombre;
	private String rfc;
	private String curp;
	private Map<String, String> map;
	
	public AltaCuentasReferenciadaRes(){
		
	}
	
	public AltaCuentasReferenciadaRes(Long codigoError, String nombre, String rfc, String curp, Map<String, String> map){
		this.codigoError = codigoError;
		this.nombre = nombre;
		this.rfc = rfc;
		this.curp = curp;
		this.map = map;
	}
	
	public AltaCuentasReferenciadaRes(Long codigoError, String nombre, String rfc, String curp, String descError){
		this.codigoError = codigoError;
		this.nombre = nombre;
		this.rfc = rfc;
		this.curp = curp;
		this.descError = descError;
	}
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public Long getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(Long codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}
}
