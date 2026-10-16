package net.cero.data;

import java.util.Date;


public class InsertaChequeReqOBJ {
	
	private String control;
	private String tipoCheque;
	private Double monto;
	private Integer folio;
	private Integer bancoId;	
	private String usuarioId;
	private Date fecha;
	private Integer bancoClie;
	private String tipoOperacion;
	private Integer dispAhorroId;
	private Integer speiTransferenciaId;
	
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
	}
	public String getTipoCheque() {
		return tipoCheque;
	}
	public void setTipoCheque(String tipoCheque) {
		this.tipoCheque = tipoCheque;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Integer getFolio() {
		return folio;
	}
	public void setFolio(Integer folio) {
		this.folio = folio;
	}
	public Integer getBancoId() {
		return bancoId;
	}
	public void setBancoId(Integer bancoId) {
		this.bancoId = bancoId;
	}
	public String getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Integer getBancoClie() {
		return bancoClie;
	}
	public void setBancoClie(Integer bancoClie) {
		this.bancoClie = bancoClie;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Integer getDispAhorroId() {
		return dispAhorroId;
	}
	public void setDispAhorroId(Integer dispAhorroId) {
		this.dispAhorroId = dispAhorroId;
	}
	public Integer getSpeiTransferenciaId() {
		return speiTransferenciaId;
	}
	public void setSpeiTransferenciaId(Integer speiTransferenciaId) {
		this.speiTransferenciaId = speiTransferenciaId;
	}
}