package net.std.request;

import java.io.Serializable;

import net.cero.ws.data.HeaderWS;
import net.std.data.DomicilioOBJ;
import net.std.data.SolicitanteOBJ;

public class AltaSolicitanteReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	HeaderWS header;
	SolicitanteOBJ solicitante;
	DomicilioOBJ domicilio;
	boolean proveedor;
	boolean ignorarRfcCurp;
	
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public SolicitanteOBJ getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(SolicitanteOBJ solicitante) {
		this.solicitante = solicitante;
	}
	public DomicilioOBJ getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(DomicilioOBJ domicilio) {
		this.domicilio = domicilio;
	}
	public boolean isProveedor() {
		return proveedor;
	}
	public void setProveedor(boolean proveedor) {
		this.proveedor = proveedor;
	}
	public boolean isIgnorarRfcCurp() {
		return ignorarRfcCurp;
	}
	public void setIgnorarRfcCurp(boolean ignorarRfcCurp) {
		this.ignorarRfcCurp = ignorarRfcCurp;
	}
}
