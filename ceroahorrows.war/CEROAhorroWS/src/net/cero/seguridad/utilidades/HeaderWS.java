package net.cero.seguridad.utilidades;

import java.io.Serializable;

public class HeaderWS implements Serializable {
	private static final long serialVersionUID = 1L;

	private String idSesion;
	private long idEmpresa;
	private long idResponsabilidad;
	protected String usuarioClave;
	private long idUsuario;
	private long idClaseCanalAtencion;
	private long idCanalAtencion;
	private long idPuntoAtencion;
	private long idUbicacion;
	private long idSucursal;
	private long idComisionista;
	private long idTransaccion;
	protected String ipHost;
	protected String nameHost;


	public String getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public long getIdResponsabilidad() {
		return idResponsabilidad;
	}

	public void setIdResponsabilidad(long idResponsabilidad) {
		this.idResponsabilidad = idResponsabilidad;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setIdClaseCanalAtencion(long idClaseCanalAtencion) {
		this.idClaseCanalAtencion = idClaseCanalAtencion;
	}

	public long getIdClaseCanalAtencion() {
		return idClaseCanalAtencion;
	}

	public void setIdCanalAtencion(long idCanalAtencion) {
		this.idCanalAtencion = idCanalAtencion;
	}

	public long getIdCanalAtencion() {
		return idCanalAtencion;
	}

	public void setIdPuntoAtencion(long idPuntoAtencion) {
		this.idPuntoAtencion = idPuntoAtencion;
	}

	public long getIdPuntoAtencion() {
		return idPuntoAtencion;
	}

	public void setIdUbicacion(long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public long getIdUbicacion() {
		return idUbicacion;
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

	/**
	 * @return the idComisionista
	 */
	public long getIdComisionista() {
		return idComisionista;
	}

	/**
	 * @param idComisionista the idComisionista to set
	 */
	public void setIdComisionista(long idComisionista) {
		this.idComisionista = idComisionista;
	}

	/**
	 * @return the idTransaccion
	 */
	public long getIdTransaccion() {
		return idTransaccion;
	}

	/**
	 * @param idTransaccion the idTransaccion to set
	 */
	public void setIdTransaccion(long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getUsuarioClave() {
		return usuarioClave;
	}

	public void setUsuarioClave(String usuarioClave) {
		this.usuarioClave = usuarioClave;
	}

	public String getIpHost() {
		return ipHost;
	}

	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}

	public String getNameHost() {
		return nameHost;
	}

	public void setNameHost(String nameHost) {
		this.nameHost = nameHost;
	}
	public String toString(){
		StringBuilder auditoria = new StringBuilder("");
		auditoria.append(",USUARIO:");
		auditoria.append(idUsuario);
		auditoria.append(",CANAL:");
		auditoria.append(idCanalAtencion);
		auditoria.append(",SUCURSAL:");
		auditoria.append(idSucursal);
		auditoria.append(",COMISIONISTA:");
		auditoria.append(idComisionista);
		auditoria.append(",TERMINAL:");
		auditoria.append(ipHost);
		
		return auditoria.toString();
	}
}
