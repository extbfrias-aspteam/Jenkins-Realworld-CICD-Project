package net.cero.data;

import net.cero.seguridad.utilidades.HeaderWS;

public class RegistroCuentaAhorroSimplificadaReq {
	
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String rfc;
	private String curp;
	private String celular;
	private String email;
	private String ine;
	private String codigoPromocion;
	private String validacionOcrReq;
	private String domicilio;
	private String codigoPostal;
	private Integer coloniaId;
	private String coloniaNombre;
	private Boolean ineValidado;
	
	//Campo que contiene la información que se obtiene del INE a través del OCR.
	private String ineOcr;
	
	//Este campo se usa para determinar si ciertos campos de la solicitud de cuenta
	//simplificada se validan
	private Boolean validarInfo;
	
	private HeaderWS header;
	
	public RegistroCuentaAhorroSimplificadaReq() {
		super();
	}

	/**
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param rfc
	 * @param curp
	 * @param celular
	 * @param email
	 * @param ine
	 * @param codigoPromocion
	 * @param validacionOcrReq
	 */
	public RegistroCuentaAhorroSimplificadaReq(String primerNombre, String segundoNombre, String apellidoPaterno,
			String apellidoMaterno, String rfc, String curp, String celular, String email, String ine,String codigoPromocion,
			String validacionOcrReq,String domicilio,String codigoPostal,Integer coloniaId,String coloniaNombre,Boolean ineValidado) {
		super();
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.rfc = rfc;
		this.curp = curp;
		this.celular = celular;
		this.email = email;
		this.ine = ine;
		this.codigoPromocion = codigoPromocion;
		this.validacionOcrReq = validacionOcrReq;
		this.domicilio=domicilio;
		this.codigoPostal=codigoPostal;
		this.coloniaId=coloniaId;
		this.coloniaNombre=coloniaNombre;
		this.ineValidado=ineValidado;
	}

	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}

	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	/**
	 * @return the primerApellido
	 */
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
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
	 * @return the celular
	 */
	public String getCelular() {
		return celular;
	}

	/**
	 * @param celular the celular to set
	 */
	public void setCelular(String celular) {
		this.celular = celular;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the ine
	 */
	public String getIne() {
		return ine;
	}

	/**
	 * @param ine the ine to set
	 */
	public void setIne(String ine) {
		this.ine = ine;
	}
	
	public String getIneOcr() {
		return ineOcr;
	}

	public void setIneOcr(String ineOcr) {
		this.ineOcr = ineOcr;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Integer getColoniaId() {
		return coloniaId;
	}

	public void setColoniaId(Integer coloniaId) {
		this.coloniaId = coloniaId;
	}

	public String getColoniaNombre() {
		return coloniaNombre;
	}

	public void setColoniaNombre(String coloniaNombre) {
		this.coloniaNombre = coloniaNombre;
	}

	public Boolean getIneValidado() {
		return ineValidado;
	}

	public void setIneValidado(Boolean ineValidado) {
		this.ineValidado = ineValidado;
	}

	public Boolean getValidarInfo() {
		return validarInfo;
	}

	public void setValidarInfo(Boolean validarInfo) {
		this.validarInfo = validarInfo;
	}

	public HeaderWS getHeader() {
		return header;
	}

	public void setHeader(HeaderWS header) {
		this.header = header;
	}

	/**
	 * @return the codigoPromocion
	 */
	public String getCodigoPromocion() {
		return codigoPromocion;
	}

	/**
	 * @param codigoPromocion the codigoPromocion to set
	 */
	public void setCodigoPromocion(String codigoPromocion) {
		this.codigoPromocion = codigoPromocion;
	}

	/**
	 * @return the validacionOcrReq
	 */
	public String getValidacionOcrReq() {
		return validacionOcrReq;
	}

	/**
	 * @param validacionOcrReq the validacionOcrReq to set
	 */
	public void setValidacionOcrReq(String validacionOcrReq) {
		this.validacionOcrReq = validacionOcrReq;
	}
	
}