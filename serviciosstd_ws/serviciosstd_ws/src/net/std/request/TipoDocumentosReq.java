package net.std.request;

import java.io.Serializable;


public class TipoDocumentosReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String productoId;
	private String producto;
	private String CLAVE;
	
	public TipoDocumentosReq(){
		
	}

	public String getProductoId() {
		return productoId;
	}

	public void setProductoId(String productoId) {
		this.productoId = productoId;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getCLAVE() {
		return CLAVE;
	}

	public void setCLAVE(String cLAVE) {
		CLAVE = cLAVE;
	}
}
