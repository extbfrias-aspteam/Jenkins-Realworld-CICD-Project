package net.cero.data;

import java.util.Map;

public class Respuesta {

	private int codigo;
	private String mensaje;
	private String data;
	private Map<String, String> listaCamposFaltantes;

	public Respuesta(int codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public Respuesta(int codigo, String mensaje, String data) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.data = data;
    }
	public Respuesta(int codigo, String mensaje, String data, Map<String, String> listaCamposFaltantes) {
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.data = data;
		this.listaCamposFaltantes = listaCamposFaltantes;
	}

	public Respuesta() {

	}

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

	public Map<String, String> getlistaCamposFaltantes() {
		return listaCamposFaltantes;
	}

	public void setlistaCamposFaltantes(Map<String, String> listaCamposFaltantes) {
		this.listaCamposFaltantes = listaCamposFaltantes;
	}

	@Override
	public String toString() {
		return "Respuesta{" +
				"codigo=" + codigo +
				", mensaje='" + mensaje + '\'' +
				", data='" + data + '\'' +
				", listaCamposFaltantes=" + listaCamposFaltantes +
				'}';
	}
}
