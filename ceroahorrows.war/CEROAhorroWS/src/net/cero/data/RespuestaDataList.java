package net.cero.data;

import java.util.List;

public class RespuestaDataList {
	private int codigo;
	private String mensaje;
	private List<Object> data;
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Respuesta{" +
				"codigo=" + codigo +
				", mensaje='" + mensaje + '\'' +
				", data='" + data + '\'' +
				'}';
	}
}
