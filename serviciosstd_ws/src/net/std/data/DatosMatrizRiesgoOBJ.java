package net.std.data;

import java.io.Serializable;
/*
 * tipo --- clave ----- descripcion -------------- factor_riesgo -------- clave_cnbv ------
 * ACT      1000001      SERVICIOS                  10                     1000001
 * GIR      2000001      SERVICIOS DE TAXIS         20                     2000001
 * LOC      3000001      RANCHO LOS TEKATES         10					   3000001  
 * OCU      4000001      TAXISTA                    10                     4000001   
 * DES      5000001      REMODELACION DE TAXI       10                     5000001
 */

public class DatosMatrizRiesgoOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tipo;
	private String clave;
	private String descripcion;
	private Integer factor_riesgo;
	private String clave_cnbv;
	
	public DatosMatrizRiesgoOBJ(){
		
	}
	
	public DatosMatrizRiesgoOBJ(String tipo, String clave, String descripcion){
		this.tipo = tipo;
		this.clave = clave;
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getFactor_riesgo() {
		return factor_riesgo;
	}

	public void setFactor_riesgo(Integer factor_riesgo) {
		this.factor_riesgo = factor_riesgo;
	}

	public String getClave_cnbv() {
		return clave_cnbv;
	}

	public void setClave_cnbv(String clave_cnbv) {
		this.clave_cnbv = clave_cnbv;
	}
}
	
