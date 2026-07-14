package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AgenteOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private String clave;
	private String rfc;
	private String nombre;
	private String domicilio;
	private String colonia;
	private String telefono;
	private String responsa;
	private String puesto;
	private String correo;
	/**
	 * 
	 */
	public AgenteOBJ() {
		super();
	}
	/**
	 * @param clave
	 * @param rfc
	 * @param nombre
	 * @param domicilio
	 * @param colonia
	 * @param telefono
	 * @param responsa
	 * @param puesto
	 * @param correo
	 */
	public AgenteOBJ(String clave, String rfc, String nombre, String domicilio, String colonia, String telefono,
			String responsa, String puesto, String correo) {
		super();
		this.clave = clave;
		this.rfc = rfc;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.colonia = colonia;
		this.telefono = telefono;
		this.responsa = responsa;
		this.puesto = puesto;
		this.correo = correo;
	}
	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}
	/**
	 * @param rfc the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
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
	 * @return the colonia
	 */
	public String getColonia() {
		return colonia;
	}
	/**
	 * @param colonia the colonia to set
	 */
	public void setColonia(String colonia) {
		this.colonia = colonia;
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
	 * @return the responsa
	 */
	public String getResponsa() {
		return responsa;
	}
	/**
	 * @param responsa the responsa to set
	 */
	public void setResponsa(String responsa) {
		this.responsa = responsa;
	}
	/**
	 * @return the puesto
	 */
	public String getPuesto() {
		return puesto;
	}
	/**
	 * @param puesto the puesto to set
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}
	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}
}