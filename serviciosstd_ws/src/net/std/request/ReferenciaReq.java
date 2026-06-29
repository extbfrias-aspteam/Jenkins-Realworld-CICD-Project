package net.std.request;

import net.cero.ws.data.HeaderWS;

public class ReferenciaReq {

	private HeaderWS header;
	private Integer producto;
	private Integer tipoReferencia;

	public HeaderWS getHeader() {
		return header;
	}

	public void setHeader(HeaderWS header) {
		this.header = header;
	}

	public Integer getProducto() {
		return producto;
	}

	public void setProducto(Integer producto) {
		this.producto = producto;
	}

	public Integer getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(Integer tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}
	
	
}
