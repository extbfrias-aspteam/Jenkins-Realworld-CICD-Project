package net.std.data;

import net.cero.ws.data.HeaderWS;
import net.std.request.DomicilioReq;

public class AltaSolicitanteReq {
	HeaderWS header;
	SolicitanteOBJ solicitante;
	DomicilioReq domicilio;
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
	public DomicilioReq getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(DomicilioReq domicilio) {
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
