package net.std.data;

import java.io.Serializable;
import java.util.Date;

public class TransaccionOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer cuentaID; 
	private String cuenta;
	private Integer tipoTransaccionID; 
	private String tipoClave;
	private Date fecha;
	private Double monto; 
	private String descripcion;
	private String autorizacion; 
	private Integer estatusID;
	private Integer formaPagoID; 
	private String conciliado;
	private Integer sucursalID; 
	private Integer canalID; 
	private Integer clienteID; 
	private String host;
	private Integer usuarioID;
	private String tipoCuenta;
	private String cuentaDendienteId;
		
	public Integer getId() {
		return id;
	}

	public Integer getCuentaID() {
		return cuentaID;
	}

	public void setCuentaID(Integer cuentaID) {
		this.cuentaID = cuentaID;
	}

	public Integer getTipoTransaccionID() {
		return tipoTransaccionID;
	}

	public void setTipoTransaccionID(Integer tipoTransaccionID) {
		this.tipoTransaccionID = tipoTransaccionID;
	}

	public String getTipoClave() {
		return tipoClave;
	}

	public void setTipoClave(String tipoClave) {
		this.tipoClave = tipoClave;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAutorizacion() {
		return autorizacion;
	}

	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	public Integer getEstatusID() {
		return estatusID;
	}

	public void setEstatusID(Integer estatusID) {
		this.estatusID = estatusID;
	}

	public Integer getFormaPagoID() {
		return formaPagoID;
	}

	public void setFormaPagoID(Integer formaPagoID) {
		this.formaPagoID = formaPagoID;
	}

	public String getConciliado() {
		return conciliado;
	}

	public void setConciliado(String conciliado) {
		this.conciliado = conciliado;
	}

	public Integer getSucursalID() {
		return sucursalID;
	}

	public void setSucursalID(Integer sucursalID) {
		this.sucursalID = sucursalID;
	}

	public Integer getCanalID() {
		return canalID;
	}

	public void setCanalID(Integer canalID) {
		this.canalID = canalID;
	}

	public Integer getClienteID() {
		return clienteID;
	}

	public void setClienteID(Integer clienteID) {
		this.clienteID = clienteID;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getUsuarioID() {
		return usuarioID;
	}

	public void setUsuarioID(Integer usuarioID) {
		this.usuarioID = usuarioID;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getCuentaDendienteId() {
		return cuentaDendienteId;
	}

	public void setCuentaDendienteId(String cuentaDendienteId) {
		this.cuentaDendienteId = cuentaDendienteId;
	}
}
