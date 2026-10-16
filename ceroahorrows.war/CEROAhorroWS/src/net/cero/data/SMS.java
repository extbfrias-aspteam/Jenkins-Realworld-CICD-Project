package net.cero.data;

import java.sql.Timestamp;

public class SMS {

	private Integer id;
	private String solicitanteId;
	private String celular;
	private Integer usuarioId;
	private Timestamp fecha;
	private String operacion;
	private Integer idMovimiento;
	private Integer tipo;
	private Integer idAplicacion;
	private Integer codigoId;
	private String mensaje;
	private Integer idMensajePaynani;
	private Integer catTiposMensajesId;
	private String descripcion;
	private Integer serviciosActivosId;
	private Integer transaccionId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSolicitanteId() {
		return solicitanteId;
	}
	public void setSolicitanteId(String solicitanteId) {
		this.solicitanteId = solicitanteId;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public Integer getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Timestamp getFecha() {
		return fecha;
	}
	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public Integer getIdMovimiento() {
		return idMovimiento;
	}
	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getIdAplicacion() {
		return idAplicacion;
	}
	public void setIdAplicacion(Integer idAplicacion) {
		this.idAplicacion = idAplicacion;
	}
	public Integer getCodigoId() {
		return codigoId;
	}
	public void setCodigoId(Integer codigoId) {
		this.codigoId = codigoId;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Integer getIdMensajePaynani() {
		return idMensajePaynani;
	}
	public void setIdMensajePaynani(Integer idMensajePaynani) {
		this.idMensajePaynani = idMensajePaynani;
	}
	public Integer getCatTiposMensajesId() {
		return catTiposMensajesId;
	}
	public void setCatTiposMensajesId(Integer catTiposMensajesId) {
		this.catTiposMensajesId = catTiposMensajesId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getTransaccionId() {
		return transaccionId;
	}
	public void setTransaccionId(Integer transaccionId) {
		this.transaccionId = transaccionId;
	}
	public Integer getServiciosActivosId() {
		return serviciosActivosId;
	}
	public void setServiciosActivosId(Integer serviciosActivosId) {
		this.serviciosActivosId = serviciosActivosId;
	}
	
	
}
