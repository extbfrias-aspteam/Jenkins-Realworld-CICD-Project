package net.cero.data;

import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

public class CajaDepositoAhorroReq {

	private HeaderWS header;
	private String cuentaCredito;
	private Integer cajaId;
	private Date fecha;
	private String cuentaAhorro;
	private Double monto;
	private Integer formaPago;
	private Integer bancoId;
	private String observacion;
	private String cheque;
	private Integer movtoId;
	private Integer transaccionId;
	private Integer tarjetaOperativaId;
	private Integer app;
	private Integer transaccionVersionId;
	private Integer paraConciliar;
	private Date fechaDeposito;
	private String control;
	private Integer usuarioId;
	private Integer sucursalId;
	/**
	 * @return the header
	 */
	public HeaderWS getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	/**
	 * @return the cuentaCredito
	 */
	public String getCuentaCredito() {
		return cuentaCredito;
	}
	/**
	 * @param cuentaCredito the cuentaCredito to set
	 */
	public void setCuentaCredito(String cuentaCredito) {
		this.cuentaCredito = cuentaCredito;
	}
	/**
	 * @return the cajaId
	 */
	public Integer getCajaId() {
		return cajaId;
	}
	/**
	 * @param cajaId the cajaId to set
	 */
	public void setCajaId(Integer cajaId) {
		this.cajaId = cajaId;
	}
	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the cuentaAhorro
	 */
	public String getCuentaAhorro() {
		return cuentaAhorro;
	}
	/**
	 * @param cuentaAhorro the cuentaAhorro to set
	 */
	public void setCuentaAhorro(String cuentaAhorro) {
		this.cuentaAhorro = cuentaAhorro;
	}
	/**
	 * @return the monto
	 */
	public Double getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	/**
	 * @return the formaPago
	 */
	public Integer getFormaPago() {
		return formaPago;
	}
	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}
	/**
	 * @return the bancoId
	 */
	public Integer getBancoId() {
		return bancoId;
	}
	/**
	 * @param bancoId the bancoId to set
	 */
	public void setBancoId(Integer bancoId) {
		this.bancoId = bancoId;
	}
	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}
	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	/**
	 * @return the cheque
	 */
	public String getCheque() {
		return cheque;
	}
	/**
	 * @param cheque the cheque to set
	 */
	public void setCheque(String cheque) {
		this.cheque = cheque;
	}
	/**
	 * @return the movtoId
	 */
	public Integer getMovtoId() {
		return movtoId;
	}
	/**
	 * @param movtoId the movtoId to set
	 */
	public void setMovtoId(Integer movtoId) {
		this.movtoId = movtoId;
	}
	/**
	 * @return the transaccionId
	 */
	public Integer getTransaccionId() {
		return transaccionId;
	}
	/**
	 * @param transaccionId the transaccionId to set
	 */
	public void setTransaccionId(Integer transaccionId) {
		this.transaccionId = transaccionId;
	}
	/**
	 * @return the tarjetaOperativaId
	 */
	public Integer getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}
	/**
	 * @param tarjetaOperativaId the tarjetaOperativaId to set
	 */
	public void setTarjetaOperativaId(Integer tarjetaOperativaId) {
		this.tarjetaOperativaId = tarjetaOperativaId;
	}
	/**
	 * @return the app
	 */
	public Integer getApp() {
		return app;
	}
	/**
	 * @param app the app to set
	 */
	public void setApp(Integer app) {
		this.app = app;
	}
	/**
	 * @return the transaccionVersionId
	 */
	public Integer getTransaccionVersionId() {
		return transaccionVersionId;
	}
	/**
	 * @param transaccionVersionId the transaccionVersionId to set
	 */
	public void setTransaccionVersionId(Integer transaccionVersionId) {
		this.transaccionVersionId = transaccionVersionId;
	}
	/**
	 * @return the paraConciliar
	 */
	public Integer getParaConciliar() {
		return paraConciliar;
	}
	/**
	 * @param paraConciliar the paraConciliar to set
	 */
	public void setParaConciliar(Integer paraConciliar) {
		this.paraConciliar = paraConciliar;
	}
	/**
	 * @return the fechaDeposito
	 */
	public Date getFechaDeposito() {
		return fechaDeposito;
	}
	/**
	 * @param fechaDeposito the fechaDeposito to set
	 */
	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}
	/**
	 * @return the control
	 */
	public String getControl() {
		return control;
	}
	/**
	 * @param control the control to set
	 */
	public void setControl(String control) {
		this.control = control;
	}
	/**
	 * @return the usuarioId
	 */
	public Integer getUsuarioId() {
		return usuarioId;
	}
	/**
	 * @param usuarioId the usuarioId to set
	 */
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	/**
	 * @return the sucursalId
	 */
	public Integer getSucursalId() {
		return sucursalId;
	}
	/**
	 * @param sucursalId the sucursalId to set
	 */
	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}
	
	
}
