package net.std.data;

import java.io.Serializable;

public class ClabeIzelOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String estatus_id;
	private String fecha_baja;
	private String motivo_baja_id;
	private String fecha_creacion;
	private String empresa_id;
	private String cuenta_clabe;  	// dato a buscar
	private String aplicacion_id;	// dato a tomar en cuenta para grabar
	private String clabe_2;
	private String tarjeta;
	private String telefono;
	
	public ClabeIzelOBJ() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(String estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getFecha_baja() {
		return fecha_baja;
	}

	public void setFecha_baja(String fecha_baja) {
		this.fecha_baja = fecha_baja;
	}

	public String getMotivo_baja_id() {
		return motivo_baja_id;
	}

	public void setMotivo_baja_id(String motivo_baja_id) {
		this.motivo_baja_id = motivo_baja_id;
	}

	public String getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public String getEmpresa_id() {
		return empresa_id;
	}

	public void setEmpresa_id(String empresa_id) {
		this.empresa_id = empresa_id;
	}

	public String getCuenta_clabe() {
		return cuenta_clabe;
	}

	public void setCuenta_clabe(String cuenta_clabe) {
		this.cuenta_clabe = cuenta_clabe;
	}

	public String getAplicacion_id() {
		return aplicacion_id;
	}

	public void setAplicacion_id(String aplicacion_id) {
		this.aplicacion_id = aplicacion_id;
	}

	public String getClabe_2() {
		return clabe_2;
	}

	public void setClabe_2(String clabe_2) {
		this.clabe_2 = clabe_2;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
	
