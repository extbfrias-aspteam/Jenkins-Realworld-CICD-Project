package net.std.data;

import java.io.Serializable;
import java.sql.Date;

public class CuentasVolumenOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String identificador;
	private String productoAhorro;
	private String datosjason;
	private Date fechaRegistro;
	private String procesado;
	private String observaciones;
	
	public CuentasVolumenOBJ() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getProductoAhorro() {
		return productoAhorro;
	}

	public void setProductoAhorro(String productoAhorro) {
		this.productoAhorro = productoAhorro;
	}

	public String getDatosjason() {
		return datosjason;
	}

	public void setDatosjason(String datosjason) {
		this.datosjason = datosjason;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getProcesado() {
		return procesado;
	}

	public void setProcesado(String procesado) {
		this.procesado = procesado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
	
