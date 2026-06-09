package net.std.data;

import java.io.Serializable;

public class AutorizacionOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String fecha;
	private String claveRastreo;
	private String cuentaOrd;
	private String cuentaDes;
	private Double monto;
	private String fechaOperacion;
	private String autorizado;
	private String fechaRespuesta;
	private String observaciones;
	private Integer usuarioCreacion;
	private String fechaCreacion;
	private Integer usuarioModificacion;
	private String fechaModificacion;
	
	public AutorizacionOBJ() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getClaveRastreo() {
		return claveRastreo;
	}

	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}

	public String getCuentaOrd() {
		return cuentaOrd;
	}

	public void setCuentaOrd(String cuentaOrd) {
		this.cuentaOrd = cuentaOrd;
	}

	public String getCuentaDes() {
		return cuentaDes;
	}

	public void setCuentaDes(String cuentaDes) {
		this.cuentaDes = cuentaDes;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getFechaOperacion() {
		return fechaOperacion;
	}

	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	public String getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(String autorizado) {
		this.autorizado = autorizado;
	}

	public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
	
