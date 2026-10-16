package net.cero.data;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

public class IneOcrRespOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private String primerApellido; // Primer apellido de la persona 
	private String segundoApellido; // Segundo apellido de la persona 
	private String nombres; // Nombre o nombres de la persona
	private String calle; // Calle del domicilio de la persona 
	private String colonia; // Colonia del domicilio de la persona 
	private String ciudad; // Ciudad del domicilio de la persona 
	private String edad; // Edad de la persona al momento de la emisión. 
	private String fechaNacimiento; // Fecha de nacimiento de la persona 
	private String sexo; // Sexo de la persona {“H” | “M”} 
	private String folio; //Folio de la credencial registro Año de registro en el padrón y número de registro 
	private String claveElector; // Clave de elector de la persona 
	private String curp; // Curp de la persona 
	private String estado; // Clave del estado de la credencial 
	private String municipio; // Clave de municipio de la credencial 
	private String distrito; // Clave de distrito de la credencial 
	private String localidad; // Clave de la localidad de la credencial 
	private String seccion; // Clave de la sección de la credencial 
	private String emision; // Año de emisión de la credencial 
	private String vigencia; // Año de vigencia de la credencial 
	private String ocr; // Clave OCR del reverso 
	private String cic; // Clave CIC del reverso 
	private String codigoValidacion; // Código único de ejecución de la validación. 
	private String estatus; // “ERROR”
	private String mensaje; // “No se identifico el documento” 
	
	/**
	 * 
	 */
	public IneOcrRespOBJ() {
		super();
	}

	/**
	 * @param primerApellido
	 * @param segundoApellido
	 * @param nombres
	 * @param calle
	 * @param colonia
	 * @param ciudad
	 * @param edad
	 * @param fechaNacimiento
	 * @param sexo
	 * @param folio
	 * @param claveElector
	 * @param curp
	 * @param estado
	 * @param municipio
	 * @param distrito
	 * @param localidad
	 * @param seccion
	 * @param emision
	 * @param vigencia
	 * @param ocr
	 * @param cic
	 * @param codigoValidacion
	 * @param estatus
	 * @param mensaje
	 */
	public IneOcrRespOBJ(String primerApellido, String segundoApellido, String nombres, String calle, String colonia,
			String ciudad, String edad, String fechaNacimiento, String sexo, String folio, String claveElector,
			String curp, String estado, String municipio, String distrito, String localidad, String seccion,
			String emision, String vigencia, String ocr, String cic, String codigoValidacion, String estatus,
			String mensaje) {
		super();
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.nombres = nombres;
		this.calle = calle;
		this.colonia = colonia;
		this.ciudad = ciudad;
		this.edad = edad;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.folio = folio;
		this.claveElector = claveElector;
		this.curp = curp;
		this.estado = estado;
		this.municipio = municipio;
		this.distrito = distrito;
		this.localidad = localidad;
		this.seccion = seccion;
		this.emision = emision;
		this.vigencia = vigencia;
		this.ocr = ocr;
		this.cic = cic;
		this.codigoValidacion = codigoValidacion;
		this.estatus = estatus;
		this.mensaje = mensaje;
	}

	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the nombres
	 */
	public String getNombres() {
		return nombres;
	}

	/**
	 * @param nombres the nombres to set
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * @return the calle
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * @param calle the calle to set
	 */
	public void setCalle(String calle) {
		this.calle = calle;
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

	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the folio
	 */
	public String getFolio() {
		return folio;
	}

	/**
	 * @param folio the folio to set
	 */
	public void setFolio(String folio) {
		this.folio = folio;
	}

	/**
	 * @return the claveElector
	 */
	public String getClaveElector() {
		return claveElector;
	}

	/**
	 * @param claveElector the claveElector to set
	 */
	public void setClaveElector(String claveElector) {
		this.claveElector = claveElector;
	}

	/**
	 * @return the curp
	 */
	public String getCurp() {
		return curp;
	}

	/**
	 * @param curp the curp to set
	 */
	public void setCurp(String curp) {
		this.curp = curp;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the distrito
	 */
	public String getDistrito() {
		return distrito;
	}

	/**
	 * @param distrito the distrito to set
	 */
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the emision
	 */
	public String getEmision() {
		return emision;
	}

	/**
	 * @param emision the emision to set
	 */
	public void setEmision(String emision) {
		this.emision = emision;
	}

	/**
	 * @return the vigencia
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia the vigencia to set
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return the ocr
	 */
	public String getOcr() {
		return ocr;
	}

	/**
	 * @param ocr the ocr to set
	 */
	public void setOcr(String ocr) {
		this.ocr = ocr;
	}

	/**
	 * @return the cic
	 */
	public String getCic() {
		return cic;
	}

	/**
	 * @param cic the cic to set
	 */
	public void setCic(String cic) {
		this.cic = cic;
	}

	/**
	 * @return the codigoValidacion
	 */
	public String getCodigoValidacion() {
		return codigoValidacion;
	}

	/**
	 * @param codigoValidacion the codigoValidacion to set
	 */
	public void setCodigoValidacion(String codigoValidacion) {
		this.codigoValidacion = codigoValidacion;
	}

	/**
	 * @return the estatus
	 */
	public String getEstatus() {
		return estatus;
	}

	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}