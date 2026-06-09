package net.std.data;

import java.io.Serializable;

/**
 * Clase que representa el modelado del objeto con los datos de entrada para las peticiones de los servicios
 */
public class HeaderWS implements Serializable {

	/**
	 * Variable para serializar la clase
	 */
	private static final long serialVersionUID = -8333732507463499246L;
	
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
	private long latitud;
	private long longitud;
	private String idBanco;
	private String numCuenta;
	
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
	public String getUsuarioClave() {
		return usuarioClave;
	}
	public void setUsuarioClave(String usuarioClave) {
		this.usuarioClave = usuarioClave;
	}
	public long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public long getIdClaseCanalAtencion() {
		return idClaseCanalAtencion;
	}
	public void setIdClaseCanalAtencion(long idClaseCanalAtencion) {
		this.idClaseCanalAtencion = idClaseCanalAtencion;
	}
	public long getIdCanalAtencion() {
		return idCanalAtencion;
	}
	public void setIdCanalAtencion(long idCanalAtencion) {
		this.idCanalAtencion = idCanalAtencion;
	}
	public long getIdPuntoAtencion() {
		return idPuntoAtencion;
	}
	public void setIdPuntoAtencion(long idPuntoAtencion) {
		this.idPuntoAtencion = idPuntoAtencion;
	}
	public long getIdUbicacion() {
		return idUbicacion;
	}
	public void setIdUbicacion(long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}
	public long getIdSucursal() {
		return idSucursal;
	}
	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}
	public long getIdComisionista() {
		return idComisionista;
	}
	public void setIdComisionista(long idComisionista) {
		this.idComisionista = idComisionista;
	}
	public long getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(long idTransaccion) {
		this.idTransaccion = idTransaccion;
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
	public long getLatitud() {
		return latitud;
	}
	public void setLatitud(long latitud) {
		this.latitud = latitud;
	}
	public long getLongitud() {
		return longitud;
	}
	public void setLongitud(long longitud) {
		this.longitud = longitud;
	}
	public String getIdBanco() {
		return idBanco;
	}
	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
	}
	public String getNumCuenta() {
		return numCuenta;
	}
	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	
}