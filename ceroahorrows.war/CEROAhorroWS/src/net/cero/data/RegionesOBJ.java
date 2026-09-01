package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class RegionesOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer clave;
	private String nombre;
	private Integer depende_region_id;
	private Integer plazo_max_avio;
	private Integer plazo_max_refa;
	private Integer coordinacion_id;
	private String cuenta;
	private String subcuenta;
	private String ip;
	private String zona;
	private String domicilio;
	private String telefono;
	private String ciudad;
	
	/**
	 * 
	 */
	public RegionesOBJ() {
		super();
	}
	/**
	 * @param clave
	 * @param nombre
	 * @param depende_region_id
	 * @param plazo_max_avio
	 * @param plazo_max_refa
	 * @param coordinacion_id
	 * @param cuenta
	 * @param subcuenta
	 * @param ip
	 * @param zona
	 * @param domicilio
	 * @param telefono
	 * @param ciudad
	 */
	public RegionesOBJ(Integer clave, String nombre, Integer depende_region_id, Integer plazo_max_avio,
			Integer plazo_max_refa, Integer coordinacion_id, String cuenta, String subcuenta, String ip, String zona,
			String domicilio, String telefono,String ciudad) {
		super();
		this.clave = clave;
		this.nombre = nombre;
		this.depende_region_id = depende_region_id;
		this.plazo_max_avio = plazo_max_avio;
		this.plazo_max_refa = plazo_max_refa;
		this.coordinacion_id = coordinacion_id;
		this.cuenta = cuenta;
		this.subcuenta = subcuenta;
		this.ip = ip;
		this.zona = zona;
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.ciudad = ciudad;
	}
	/**
	 * @return the clave
	 */
	public Integer getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(Integer clave) {
		this.clave = clave;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the depende_region_id
	 */
	public Integer getDepende_region_id() {
		return depende_region_id;
	}
	/**
	 * @param depende_region_id the depende_region_id to set
	 */
	public void setDepende_region_id(Integer depende_region_id) {
		this.depende_region_id = depende_region_id;
	}
	/**
	 * @return the plazo_max_avio
	 */
	public Integer getPlazo_max_avio() {
		return plazo_max_avio;
	}
	/**
	 * @param plazo_max_avio the plazo_max_avio to set
	 */
	public void setPlazo_max_avio(Integer plazo_max_avio) {
		this.plazo_max_avio = plazo_max_avio;
	}
	/**
	 * @return the plazo_max_refa
	 */
	public Integer getPlazo_max_refa() {
		return plazo_max_refa;
	}
	/**
	 * @param plazo_max_refa the plazo_max_refa to set
	 */
	public void setPlazo_max_refa(Integer plazo_max_refa) {
		this.plazo_max_refa = plazo_max_refa;
	}
	/**
	 * @return the coordinacion_id
	 */
	public Integer getCoordinacion_id() {
		return coordinacion_id;
	}
	/**
	 * @param coordinacion_id the coordinacion_id to set
	 */
	public void setCoordinacion_id(Integer coordinacion_id) {
		this.coordinacion_id = coordinacion_id;
	}
	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}
	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}
	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the zona
	 */
	public String getZona() {
		return zona;
	}
	/**
	 * @param zona the zona to set
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}
	/**
	 * @return the domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}
	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	
}