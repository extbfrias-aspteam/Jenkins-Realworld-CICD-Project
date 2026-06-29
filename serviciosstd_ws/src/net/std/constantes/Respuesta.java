package net.std.constantes;

public class Respuesta {

	private int codigo;
	private String mensaje;
	private String data;
	
	public Respuesta(){
		this.codigo = 0;
		this.mensaje = "OK";
	}
	
	public Respuesta(int codigo, String mensaje, String data){
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.data = data;
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
}
