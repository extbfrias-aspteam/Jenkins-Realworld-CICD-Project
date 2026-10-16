package net.cero.data.nuevospei;

import net.cero.ws.data.HeaderWS;

//import net.cero.seguridad.utilidades.HeaderWS;

public class BuscarSolicitanteCompletoRequest {

	private HeaderWS header;
	private String numero;
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
}
