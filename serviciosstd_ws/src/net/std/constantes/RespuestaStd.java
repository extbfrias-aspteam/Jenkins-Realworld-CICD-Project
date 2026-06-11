package net.std.constantes;

public class RespuestaStd {

	private int estado_pago;
	private String descripcion;
	private String error;
	
	public RespuestaStd(){
		this.estado_pago = Errores.PENDIENTE;
		this.descripcion = Errores.desc(Errores.PENDIENTE);
	}
	
	public RespuestaStd(int estado_pago, String descripcion, String error){
		this.estado_pago = estado_pago;
		this.descripcion = descripcion;
		this.error = error;
	}

	public int getEstado_pago() {
		return estado_pago;
	}

	public void setEstado_pago(int estado_pago) {
		this.estado_pago = estado_pago;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
