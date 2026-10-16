package net.cero.data;

import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class AhorroTransferenciaReq {

	private HeaderWS header;
	private String cuentaOrigen;
	private String cuentaDestino;
	private Date fecha;
	private Double monto;
	private CajaDepositoAhorroReq cajaDepAhorroReq;
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	public void setCuentaOrigen(String cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
	public String getCuentaDestino() {
		return cuentaDestino;
	}
	public void setCuentaDestino(String cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public CajaDepositoAhorroReq getCajaDepAhorroReq() {
		return cajaDepAhorroReq;
	}
	public void setCajaDepAhorroReq(CajaDepositoAhorroReq cajaDepAhorroReq) {
		this.cajaDepAhorroReq = cajaDepAhorroReq;
	}
	
	
}
