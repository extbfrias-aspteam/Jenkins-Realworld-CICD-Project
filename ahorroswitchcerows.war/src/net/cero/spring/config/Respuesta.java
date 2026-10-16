package net.cero.spring.config;

public class Respuesta {

	private int codigo;
	private String mensaje;
	private String data;
	
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
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
