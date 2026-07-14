package net.cero.data;

import net.cero.seguridad.utilidades.HeaderWS;

public class ConsultaCuentasAhorroRequest {
	private String idCliente;	
	private String referencia;
	private HeaderWS header;
	
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
}
