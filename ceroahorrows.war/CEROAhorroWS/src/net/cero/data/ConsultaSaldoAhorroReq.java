package net.cero.data;


import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class ConsultaSaldoAhorroReq {
	
	private HeaderWS header;
	private String cuenta;
	private Date fechaConsulta;
	
	public ConsultaSaldoAhorroReq() {
		super();
	}

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

	public Date getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	
}