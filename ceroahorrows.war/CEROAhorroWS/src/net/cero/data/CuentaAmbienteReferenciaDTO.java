package net.cero.data;

public class CuentaAmbienteReferenciaDTO {

	private String rfc;
	
	private String curp;
	
	private String telefono;
	
	private String cuentaASP;
	
	private String cuentaCLABE;
	
	private String numeroTarjeta;

	private String claveProducto;
	private String correo;

	public CuentaAmbienteReferenciaDTO() {}

	public CuentaAmbienteReferenciaDTO(String rFC, String cURP, String telefono, String cuentaASP, String cuentaCLABE,
			String numeroTarjeta, String correo) {
		this.rfc = rFC;
		this.curp = cURP;
		this.telefono = telefono;
		this.cuentaASP = cuentaASP;
		this.cuentaCLABE = cuentaCLABE;
		this.numeroTarjeta = numeroTarjeta;
		this.correo = correo;
	}

	public String getRFC() {
		return rfc;
	}

	public void setRFC(String rfc) {
		this.rfc = rfc;
	}

	public String getCURP() {
		return this.curp;
	}

	public void setCURP(String curp) {
		this.curp = curp;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCuentaASP() {
		return cuentaASP;
	}

	public void setCuentaASP(String cuentaASP) {
		this.cuentaASP = cuentaASP;
	}

	public String getCuentaCLABE() {
		return cuentaCLABE;
	}

	public void setCuentaCLABE(String cuentaCLABE) {
		this.cuentaCLABE = cuentaCLABE;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getClaveProducto() {
		return claveProducto;
	}

	public void setClaveProducto(String claveProducto) {
		this.claveProducto = claveProducto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
