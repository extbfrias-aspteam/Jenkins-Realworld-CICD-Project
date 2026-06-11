package net.std.data;

public class TransaccionTarjetaOrquestadorOBJ {
	private HeaderWS header;
	private String cuenta;
	private String concepto;
	private String importe;
	private String referenciaNumerica;
	private String medioPago;
	private String clave_rastreo;
	private Long idSpei;
	/*CLAVE DEL MOVIMIENTO REGISTRADO DEL LADO DE DOCK. EXISTE UN CAMPO EN LA TABLA DE AHORRO.AHTIPOS_TRANSACCIONES CON LA CLAVE TAMBIEN*/
	private String claveMovimiento;

	private String observaciones;
	private String numero_tarjeta;
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getReferenciaNumerica() {
		return referenciaNumerica;
	}
	public void setReferenciaNumerica(String referenciaNumerica) {
		this.referenciaNumerica = referenciaNumerica;
	}
	public String getMedioPago() {
		return medioPago;
	}
	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}
	public String getClave_rastreo() {
		return clave_rastreo;
	}
	public void setClave_rastreo(String clave_rastreo) {
		this.clave_rastreo = clave_rastreo;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getNumero_tarjeta() {
		return numero_tarjeta;
	}
	public void setNumero_tarjeta(String numero_tarjeta) {
		this.numero_tarjeta = numero_tarjeta;
	}

	public Long getIdSpei() {
		return idSpei;
	}

	public void setIdSpei(Long idSpei) {
		this.idSpei = idSpei;
	}

	public String getClaveMovimiento() {
		return claveMovimiento;
	}

	public void setClaveMovimiento(String claveMovimiento) {
		this.claveMovimiento = claveMovimiento;
	}
}
