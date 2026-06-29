package net.std.data;

import java.io.Serializable;

public class TipoDocumentoCompletoOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String producto_ahorro_id;
	private String cve_producto;
	private String producto;
	private String documento_id;
	private String cve_documento;
	private String documento;
	private String obligatorio;
	private String estatus_id;
	private String estatus;
		
	public TipoDocumentoCompletoOBJ(){
		
	}

	public String getProducto_ahorro_id() {
		return producto_ahorro_id;
	}

	public void setProducto_ahorro_id(String producto_ahorro_id) {
		this.producto_ahorro_id = producto_ahorro_id;
	}

	public String getCve_producto() {
		return cve_producto;
	}

	public void setCve_producto(String cve_producto) {
		this.cve_producto = cve_producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getDocumento_id() {
		return documento_id;
	}

	public void setDocumento_id(String documento_id) {
		this.documento_id = documento_id;
	}

	public String getCve_documento() {
		return cve_documento;
	}

	public void setCve_documento(String cve_documento) {
		this.cve_documento = cve_documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(String estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
