package net.cero.data.nuevospei;


public class AsignaCuentaPanReq {
	
	private AsignaCuentaPanOBJ asignaCuentaPan;
	private Integer cuenta_id;
	private Integer pan_id;
	private Integer sucursal_id;
	private Boolean resultado;
	
	private String pan_original;
	private String pan_confirmacion;
	
	
	public AsignaCuentaPanReq() {
		// Constructor
	}

	public Boolean getResultado() {
		return resultado;
	}

	public void setResultado(Boolean resultado) {
		this.resultado = resultado;
	}

	public AsignaCuentaPanOBJ getAsignaCuentaPan() {
		return asignaCuentaPan;
	}

	public void setAsignaCuentaPan(AsignaCuentaPanOBJ asignaCuentaPan) {
		this.asignaCuentaPan = asignaCuentaPan;
	}

	public Integer getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(Integer cuenta_id) {
		this.cuenta_id = cuenta_id;
	}

	public Integer getPan_id() {
		return pan_id;
	}

	public void setPan_id(Integer pan_id) {
		this.pan_id = pan_id;
	}

	public Integer getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(Integer sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public String getPan_confirmacion() {
		return pan_confirmacion;
	}

	public void setPan_confirmacion(String pan_confirmacion) {
		this.pan_confirmacion = pan_confirmacion;
	}

	public String getPan_original() {
		return pan_original;
	}

	public void setPan_original(String pan_original) {
		this.pan_original = pan_original;
	}
}

