package net.std.request;

import java.io.Serializable;

public class ListCuentasProdAhorroReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tipoProducto;
	private Integer productoAhorroId;
	
	public ListCuentasProdAhorroReq(){
		
	}
	
	public ListCuentasProdAhorroReq(String tipoProducto, Integer productoAhorroId){
		this.tipoProducto = tipoProducto;
		this.setProductoAhorroId(productoAhorroId);
		
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProductoId(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public Integer getProductoAhorroId() {
		return productoAhorroId;
	}

	public void setProductoAhorroId(Integer productoAhorroId) {
		this.productoAhorroId = productoAhorroId;
	}
}
