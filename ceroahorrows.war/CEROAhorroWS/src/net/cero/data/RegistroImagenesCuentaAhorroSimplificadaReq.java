package net.cero.data;


public class RegistroImagenesCuentaAhorroSimplificadaReq {
	
	private String numeroCuenta;
	private String validacionOcrReq;
	
	/**
	 * @return the numeroCuenta
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * @param numeroCuenta the numeroCuenta to set
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * @return the validacionOcrReq
	 */
	public String getValidacionOcrReq() {
		return validacionOcrReq;
	}
	/**
	 * @param validacionOcrReq the validacionOcrReq to set
	 */
	public void setValidacionOcrReq(String validacionOcrReq) {
		this.validacionOcrReq = validacionOcrReq;
	}
	
}