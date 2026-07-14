package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class ColoniaOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer clave;
	private String nombre;
	private String cp;
	private String agenteId;
	private Integer localidadId;
	private Integer regionId;
	private String exclusiva;
	private Double comisionGestor;
	private Integer bloqueada;
	private Integer idSubsidio;
	private String claveCnbv;
	/**
	 * 
	 */
	public ColoniaOBJ() {
		super();
	}
	/**
	 * @param clave
	 * @param nombre
	 * @param cp
	 * @param agenteId
	 * @param localidadId
	 * @param regionId
	 * @param exclusiva
	 * @param comisionGestor
	 * @param bloqueado
	 * @param idSubsidio
	 * @param claveCnbv
	 */
	public ColoniaOBJ(Integer clave, String nombre, String cp, String agenteId, Integer localidadId, Integer regionId,
			String exclusiva, Double comisionGestor, Integer bloqueada, Integer idSubsidio, String claveCnbv) {
		super();
		this.clave = clave;
		this.nombre = nombre;
		this.cp = cp;
		this.agenteId = agenteId;
		this.localidadId = localidadId;
		this.regionId = regionId;
		this.exclusiva = exclusiva;
		this.comisionGestor = comisionGestor;
		this.bloqueada = bloqueada;
		this.idSubsidio = idSubsidio;
		this.claveCnbv = claveCnbv;
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
	 * @return the cp
	 */
	public String getCp() {
		return cp;
	}
	/**
	 * @param cp the cp to set
	 */
	public void setCp(String cp) {
		this.cp = cp;
	}
	/**
	 * @return the agenteId
	 */
	public String getAgenteId() {
		return agenteId;
	}
	/**
	 * @param agenteId the agenteId to set
	 */
	public void setAgenteId(String agenteId) {
		this.agenteId = agenteId;
	}
	/**
	 * @return the localidadId
	 */
	public Integer getLocalidadId() {
		return localidadId;
	}
	/**
	 * @param localidadId the localidadId to set
	 */
	public void setLocalidadId(Integer localidadId) {
		this.localidadId = localidadId;
	}
	/**
	 * @return the regionId
	 */
	public Integer getRegionId() {
		return regionId;
	}
	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	/**
	 * @return the exclusiva
	 */
	public String getExclusiva() {
		return exclusiva;
	}
	/**
	 * @param exclusiva the exclusiva to set
	 */
	public void setExclusiva(String exclusiva) {
		this.exclusiva = exclusiva;
	}
	/**
	 * @return the comisionGestor
	 */
	public Double getComisionGestor() {
		return comisionGestor;
	}
	/**
	 * @param comisionGestor the comisionGestor to set
	 */
	public void setComisionGestor(Double comisionGestor) {
		this.comisionGestor = comisionGestor;
	}
	/**
	 * @return the bloqueado
	 */
	public Integer getBloqueada() {
		return bloqueada;
	}
	/**
	 * @param bloqueado the bloqueado to set
	 */
	public void setBloqueada(Integer bloqueada) {
		this.bloqueada = bloqueada;
	}
	/**
	 * @return the idSubsidio
	 */
	public Integer getIdSubsidio() {
		return idSubsidio;
	}
	/**
	 * @param idSubsidio the idSubsidio to set
	 */
	public void setIdSubsidio(Integer idSubsidio) {
		this.idSubsidio = idSubsidio;
	}
	/**
	 * @return the claveCnbv
	 */
	public String getClaveCnbv() {
		return claveCnbv;
	}
	/**
	 * @param claveCnbv the claveCnbv to set
	 */
	public void setClaveCnbv(String claveCnbv) {
		this.claveCnbv = claveCnbv;
	}

}