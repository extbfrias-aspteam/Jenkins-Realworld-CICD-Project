package net.std.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.std.constantes.Comun;
import net.std.data.AltaCuentasOBJ;
import net.std.data.CuentaOBJ;
import net.std.data.DatosMatrizRiesgoOBJ;
import net.std.data.DatosPldOBJ;
import net.std.data.DomicilioOBJ;
import net.std.data.ExpedienteOBJ;
import net.std.data.RepresentantesOBJ;
import net.std.data.SolicitanteOBJ;

public class SolicitanteReq implements Serializable{
	private static final long serialVersionUID = 1L;

	private String identificador;
	private CuentaOBJ cuenta;
	private SolicitanteOBJ solicitante;
	private DomicilioOBJ domicilio;
	private DatosPldOBJ pld;
	private List<DatosMatrizRiesgoOBJ> lstMatriz;
	private List<ExpedienteOBJ> lstExpediente;
	private String cuenta_clabe_eje;
	private String cuenta_eje;
	
	/* REPRESENTANTS LEGEGAS DE 1 A N */
	private List<RepresentantesOBJ> lstRepresentantes;
	
	public SolicitanteReq(){

	}

	public SolicitanteReq(AltaCuentasOBJ cta){
		this.identificador = cta.getIdentificador();
		this.cuenta_clabe_eje = cta.getCuenta_clabe_eje();
		
		this.cuenta = new CuentaOBJ();
		this.cuenta.setProductoAhorro(cta.getProductoAhorro());
		
		this.solicitante = new SolicitanteOBJ();
		this.solicitante.setTipoPersona(cta.getTipoPersona());
		this.solicitante.setNombre(cta.getNombre());
		this.solicitante.setApellidoPaterno(cta.getApellidoPaterno());
		this.solicitante.setApellidoMaterno(cta.getApellidoMaterno());
		this.solicitante.setNombreCompleto(cta.getNombreCompleto());
		this.solicitante.setRfc(cta.getRfc());
		this.solicitante.setCurp(cta.getCurp());
		this.solicitante.setLugarNacimiento(cta.getLugarNacimiento());
		this.solicitante.setNacionalidadId(Comun._I(cta.getNacionalidadId()));
		this.solicitante.setEstadoCivilId(cta.getEstadoCivilId());
		this.solicitante.setTelefono(cta.getTelefono());
		this.solicitante.setCelular(cta.getCelular());
		this.solicitante.setCorreo(cta.getCorreo());
		this.solicitante.setOcupacionId(Comun._I(cta.getOcupacionId()));
		this.solicitante.setFechaNacimiento(cta.getFechaNacimiento());
		this.solicitante.setSexo(cta.getSexo());
		this.solicitante.setGradoEstudiosId(Comun._I(cta.getGradoEstudiosId()));
		this.solicitante.setTipoIdentificacionId(Comun._I(cta.getTipoIdentificacionId()));
		this.solicitante.setIdentificacion(cta.getIdentificacion());

		this.domicilio = new DomicilioOBJ();
		this.domicilio.setIdUbicacion1(Comun._I(cta.getIdUbicacion1()));
		this.domicilio.setDescripcionUbicacion1(cta.getDescripcionUbicacion1());
		this.domicilio.setIdUbicacion2(Comun._I(cta.getIdUbicacion2()));
		this.domicilio.setDescripcionUbicacion2(cta.getDescripcionUbicacion2());
		this.domicilio.setIdUbicacion3(Comun._I(cta.getIdUbicacion3()));
		this.domicilio.setDescripcionUbicacion3(cta.getDescripcionUbicacion3());
		this.domicilio.setReferencia(cta.getReferencia());
		this.domicilio.setIdUbicacion4(Comun._I(cta.getIdUbicacion4()));
		this.domicilio.setDescripcionUbicacion4(cta.getDescripcionUbicacion4());
		this.domicilio.setIdUbicacion5(Comun._I(cta.getIdUbicacion5()));
		this.domicilio.setDescripcionUbicacion5(cta.getDescripcionUbicacion5());
		this.domicilio.setIdColonia(Comun._I(cta.getIdColonia()));
		this.domicilio.setDomicilioCompleto(cta.getDomicilioCompleto());

		this.pld = new DatosPldOBJ();
		this.pld.setIngreso_mensual(Comun._D(cta.getIngreso_mensual()));
		this.pld.setMonto_maximo_ahorro(Comun._D(cta.getMonto_maximo_ahorro()));
		this.pld.setPuesto(cta.getPuesto());
		
		this.lstMatriz = new ArrayList<DatosMatrizRiesgoOBJ>();
		this.lstMatriz.add(new DatosMatrizRiesgoOBJ(cta.getTipo1(), cta.getClave1(), cta.getDescripcion1()));
		this.lstMatriz.add(new DatosMatrizRiesgoOBJ(cta.getTipo2(), cta.getClave2(), cta.getDescripcion2()));
		this.lstMatriz.add(new DatosMatrizRiesgoOBJ(cta.getTipo3(), cta.getClave3(), cta.getDescripcion3()));
		this.lstMatriz.add(new DatosMatrizRiesgoOBJ(cta.getTipo4(), cta.getClave4(), cta.getDescripcion4()));
		this.lstMatriz.add(new DatosMatrizRiesgoOBJ(cta.getTipo5(), cta.getClave5(), cta.getDescripcion5()));
	}

	public SolicitanteOBJ getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(SolicitanteOBJ solicitante) {
		this.solicitante = solicitante;
	}

	public DomicilioOBJ getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(DomicilioOBJ domicilio) {
		this.domicilio = domicilio;
	}

	public CuentaOBJ getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaOBJ cuenta) {
		this.cuenta = cuenta;
	}

	public DatosPldOBJ getPld() {
		return pld;
	}

	public void setPld(DatosPldOBJ pld) {
		this.pld = pld;
	}

	public List<DatosMatrizRiesgoOBJ> getLstMatriz() {
		return lstMatriz;
	}

	public void setLstMatriz(List<DatosMatrizRiesgoOBJ> lstMatriz) {
		this.lstMatriz = lstMatriz;
	}

	public String getCuenta_clabe_eje() {
		return cuenta_clabe_eje;
	}

	public void setCuenta_clabe_eje(String cuenta_clabe_eje) {
		this.cuenta_clabe_eje = cuenta_clabe_eje;
	}

	public String getCuenta_eje() {
		return cuenta_eje;
	}

	public void setCuenta_eje(String cuenta_eje) {
		this.cuenta_eje = cuenta_eje;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<ExpedienteOBJ> getLstExpediente() {
		return lstExpediente;
	}

	public void setLstExpediente(List<ExpedienteOBJ> lstExpediente) {
		this.lstExpediente = lstExpediente;
	}

	public List<RepresentantesOBJ> getLstRepresentantes() {
		return lstRepresentantes;
	}

	public void setLstRepresentantes(List<RepresentantesOBJ> lstRepresentantes) {
		this.lstRepresentantes = lstRepresentantes;
	}

}
