package net.std.request;

import java.io.Serializable;

public class TransaccionCuentasProcreaReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cuentaOri;
	private String cuentaDes;
	private String fecha;
	private String monto;
	private String usuarioId;
	private String movimientoId;
	private String observaciones;
	private String tipoMovto;

	public TransaccionCuentasProcreaReq(){
		
	}

	public String getCuentaOri() {
		return cuentaOri;
	}

	public void setCuentaOri(String cuentaOri) {
		this.cuentaOri = cuentaOri;
	}

	public String getCuentaDes() {
		return cuentaDes;
	}

	public void setCuentaDes(String cuentaDes) {
		this.cuentaDes = cuentaDes;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(String movimientoId) {
		this.movimientoId = movimientoId;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipoMovto() {
		return tipoMovto;
	}

	public void setTipoMovto(String tipoMovto) {
		this.tipoMovto = tipoMovto;
	}
}
