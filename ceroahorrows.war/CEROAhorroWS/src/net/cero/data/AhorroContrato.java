package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AhorroContrato implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ahorroContratoId;
	private Integer actividadId;
	private Integer asociacionId;
	private String centroTrabajo;
	private Integer coloniaId;
	private String contrato;
	private String correoEdocuenta;
	private Integer creadoPor;
	private String ctaContable;
	private String cuenta;
	private String cuentaClabe;
	private String cuentaDestinoCap;
	private String cuentaDestinoRen;
	private String cuentaPadre;
	private Integer cveDestino;
	private String domicilio;
	private Integer errores;
	private String estatus;
	private Integer externaBanco;
	private String externaClabe;
	private String externaCuenta;
	private String externaTarjeta;
	private Date fechaApertura;
	private Date fechaCancelacion;
	private Timestamp fechaCreacion;
	private Date fechaDeposito;
	private Timestamp fechaModificacion;
	private Double gat;
	private Integer giroId;
	private Timestamp horaBloqueo;
	private Timestamp horaPrimerError;
	private Integer idComoEntero;
	private Double ingresos;
	private Double metaAportacion;
	private Integer metaDestinoId;
	private Date metaFecha;
	private Double metaMonto;
	private String metaMotivo;
	private String metaPeriodo;
	private Integer modificadoPor;
	private Integer monedaId;
	private Double montoApertura;
	private Double montoMaxAhorro;
	private Integer numBloqueo;
	private String numeroCasa;
	private Integer ocupacionId;
	private Integer oficialId;
	private String pin;
	private String pinAuto;
	private Timestamp pinFechaCambio;
	private Integer pinUsuarioCambio;
	private String provCentroTrabajo;
	private Integer provOcupacionId;
	private String provPuesto;
	private String provRecId;
	private Double provRecIngresos;
	private Double provRecMontoMaxAhorro;
	private Integer provRecRelId;
	private String puesto;
	private String referencia;
	private Integer rendimientoId;
	private Double requiereIdentificador;
	private String respaldoMd5;
	private Double saldo;
	private Integer statusBloqueo;
	private String sucursalApertura;
	private Integer tipoAhorroId;
	private String titularId;
	private String solicitante;
	private String solicitanteNombre;
	private Integer sucursalId;

	public AhorroContrato() {
	}

	public Integer getAhorroContratoId() {
		return this.ahorroContratoId;
	}

	public void setAhorroContratoId(Integer ahorroContratoId) {
		this.ahorroContratoId = ahorroContratoId;
	}

	public Integer getActividadId() {
		return this.actividadId;
	}

	public void setActividadId(Integer actividadId) {
		this.actividadId = actividadId;
	}

	public Integer getAsociacionId() {
		return this.asociacionId;
	}

	public void setAsociacionId(Integer asociacionId) {
		this.asociacionId = asociacionId;
	}

	public String getCentroTrabajo() {
		return this.centroTrabajo;
	}

	public void setCentroTrabajo(String centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}

	public Integer getColoniaId() {
		return this.coloniaId;
	}

	public void setColoniaId(Integer coloniaId) {
		this.coloniaId = coloniaId;
	}

	public String getContrato() {
		return this.contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	public String getCorreoEdocuenta() {
		return this.correoEdocuenta;
	}

	public void setCorreoEdocuenta(String correoEdocuenta) {
		this.correoEdocuenta = correoEdocuenta;
	}

	public Integer getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getCtaContable() {
		return this.ctaContable;
	}

	public void setCtaContable(String ctaContable) {
		this.ctaContable = ctaContable;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getCuentaClabe() {
		return this.cuentaClabe;
	}

	public void setCuentaClabe(String cuentaClabe) {
		this.cuentaClabe = cuentaClabe;
	}

	public String getCuentaDestinoCap() {
		return this.cuentaDestinoCap;
	}

	public void setCuentaDestinoCap(String cuentaDestinoCap) {
		this.cuentaDestinoCap = cuentaDestinoCap;
	}

	public String getCuentaDestinoRen() {
		return this.cuentaDestinoRen;
	}

	public void setCuentaDestinoRen(String cuentaDestinoRen) {
		this.cuentaDestinoRen = cuentaDestinoRen;
	}

	public String getCuentaPadre() {
		return this.cuentaPadre;
	}

	public void setCuentaPadre(String cuentaPadre) {
		this.cuentaPadre = cuentaPadre;
	}

	public Integer getCveDestino() {
		return this.cveDestino;
	}

	public void setCveDestino(Integer cveDestino) {
		this.cveDestino = cveDestino;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Integer getErrores() {
		return this.errores;
	}

	public void setErrores(Integer errores) {
		this.errores = errores;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Integer getExternaBanco() {
		return this.externaBanco;
	}

	public void setExternaBanco(Integer externaBanco) {
		this.externaBanco = externaBanco;
	}

	public String getExternaClabe() {
		return this.externaClabe;
	}

	public void setExternaClabe(String externaClabe) {
		this.externaClabe = externaClabe;
	}

	public String getExternaCuenta() {
		return this.externaCuenta;
	}

	public void setExternaCuenta(String externaCuenta) {
		this.externaCuenta = externaCuenta;
	}

	public String getExternaTarjeta() {
		return this.externaTarjeta;
	}

	public void setExternaTarjeta(String externaTarjeta) {
		this.externaTarjeta = externaTarjeta;
	}

	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCancelacion() {
		return this.fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaDeposito() {
		return this.fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public Timestamp getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Double getGat() {
		return this.gat;
	}

	public void setGat(Double gat) {
		this.gat = gat;
	}

	public Integer getGiroId() {
		return this.giroId;
	}

	public void setGiroId(Integer giroId) {
		this.giroId = giroId;
	}

	public Timestamp getHoraBloqueo() {
		return this.horaBloqueo;
	}

	public void setHoraBloqueo(Timestamp horaBloqueo) {
		this.horaBloqueo = horaBloqueo;
	}

	public Timestamp getHoraPrimerError() {
		return this.horaPrimerError;
	}

	public void setHoraPrimerError(Timestamp horaPrimerError) {
		this.horaPrimerError = horaPrimerError;
	}

	public Integer getIdComoEntero() {
		return this.idComoEntero;
	}

	public void setIdComoEntero(Integer idComoEntero) {
		this.idComoEntero = idComoEntero;
	}

	public Double getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Double ingresos) {
		this.ingresos = ingresos;
	}

	public Double getMetaAportacion() {
		return this.metaAportacion;
	}

	public void setMetaAportacion(Double metaAportacion) {
		this.metaAportacion = metaAportacion;
	}

	public Integer getMetaDestinoId() {
		return this.metaDestinoId;
	}

	public void setMetaDestinoId(Integer metaDestinoId) {
		this.metaDestinoId = metaDestinoId;
	}

	public Date getMetaFecha() {
		return this.metaFecha;
	}

	public void setMetaFecha(Date metaFecha) {
		this.metaFecha = metaFecha;
	}

	public Double getMetaMonto() {
		return this.metaMonto;
	}

	public void setMetaMonto(Double metaMonto) {
		this.metaMonto = metaMonto;
	}

	public String getMetaMotivo() {
		return this.metaMotivo;
	}

	public void setMetaMotivo(String metaMotivo) {
		this.metaMotivo = metaMotivo;
	}

	public String getMetaPeriodo() {
		return this.metaPeriodo;
	}

	public void setMetaPeriodo(String metaPeriodo) {
		this.metaPeriodo = metaPeriodo;
	}

	public Integer getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Integer getMonedaId() {
		return this.monedaId;
	}

	public void setMonedaId(Integer monedaId) {
		this.monedaId = monedaId;
	}

	public Double getMontoApertura() {
		return this.montoApertura;
	}

	public void setMontoApertura(Double montoApertura) {
		this.montoApertura = montoApertura;
	}

	public Double getMontoMaxAhorro() {
		return this.montoMaxAhorro;
	}

	public void setMontoMaxAhorro(Double montoMaxAhorro) {
		this.montoMaxAhorro = montoMaxAhorro;
	}

	public Integer getNumBloqueo() {
		return this.numBloqueo;
	}

	public void setNumBloqueo(Integer numBloqueo) {
		this.numBloqueo = numBloqueo;
	}

	public String getNumeroCasa() {
		return this.numeroCasa;
	}

	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}

	public Integer getOcupacionId() {
		return this.ocupacionId;
	}

	public void setOcupacionId(Integer ocupacionId) {
		this.ocupacionId = ocupacionId;
	}

	public Integer getOficialId() {
		return this.oficialId;
	}

	public void setOficialId(Integer oficialId) {
		this.oficialId = oficialId;
	}

	public String getPin() {
		return this.pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPinAuto() {
		return this.pinAuto;
	}

	public void setPinAuto(String pinAuto) {
		this.pinAuto = pinAuto;
	}

	public Timestamp getPinFechaCambio() {
		return this.pinFechaCambio;
	}

	public void setPinFechaCambio(Timestamp pinFechaCambio) {
		this.pinFechaCambio = pinFechaCambio;
	}

	public Integer getPinUsuarioCambio() {
		return this.pinUsuarioCambio;
	}

	public void setPinUsuarioCambio(Integer pinUsuarioCambio) {
		this.pinUsuarioCambio = pinUsuarioCambio;
	}

	public String getProvCentroTrabajo() {
		return this.provCentroTrabajo;
	}

	public void setProvCentroTrabajo(String provCentroTrabajo) {
		this.provCentroTrabajo = provCentroTrabajo;
	}

	public Integer getProvOcupacionId() {
		return this.provOcupacionId;
	}

	public void setProvOcupacionId(Integer provOcupacionId) {
		this.provOcupacionId = provOcupacionId;
	}

	public String getProvPuesto() {
		return this.provPuesto;
	}

	public void setProvPuesto(String provPuesto) {
		this.provPuesto = provPuesto;
	}

	public String getProvRecId() {
		return this.provRecId;
	}

	public void setProvRecId(String provRecId) {
		this.provRecId = provRecId;
	}

	public Double getProvRecIngresos() {
		return this.provRecIngresos;
	}

	public void setProvRecIngresos(Double provRecIngresos) {
		this.provRecIngresos = provRecIngresos;
	}

	public Double getProvRecMontoMaxAhorro() {
		return this.provRecMontoMaxAhorro;
	}

	public void setProvRecMontoMaxAhorro(Double provRecMontoMaxAhorro) {
		this.provRecMontoMaxAhorro = provRecMontoMaxAhorro;
	}

	public Integer getProvRecRelId() {
		return this.provRecRelId;
	}

	public void setProvRecRelId(Integer provRecRelId) {
		this.provRecRelId = provRecRelId;
	}

	public String getPuesto() {
		return this.puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Integer getRendimientoId() {
		return this.rendimientoId;
	}

	public void setRendimientoId(Integer rendimientoId) {
		this.rendimientoId = rendimientoId;
	}

	public Double getRequiereIdentificador() {
		return this.requiereIdentificador;
	}

	public void setRequiereIdentificador(Double requiereIdentificador) {
		this.requiereIdentificador = requiereIdentificador;
	}

	public String getRespaldoMd5() {
		return this.respaldoMd5;
	}

	public void setRespaldoMd5(String respaldoMd5) {
		this.respaldoMd5 = respaldoMd5;
	}

	public Double getSaldo() {
		return this.saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Integer getStatusBloqueo() {
		return this.statusBloqueo;
	}

	public void setStatusBloqueo(Integer statusBloqueo) {
		this.statusBloqueo = statusBloqueo;
	}

	public String getSucursalApertura() {
		return this.sucursalApertura;
	}

	public void setSucursalApertura(String sucursalApertura) {
		this.sucursalApertura = sucursalApertura;
	}

	public Integer getTipoAhorroId() {
		return this.tipoAhorroId;
	}

	public void setTipoAhorroId(Integer tipoAhorroId) {
		this.tipoAhorroId = tipoAhorroId;
	}

	public String getTitularId() {
		return this.titularId;
	}

	public void setTitularId(String titularId) {
		this.titularId = titularId;
	}

	public String getSolicitante() {
		return this.solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getSolicitanteNombre() {
		return solicitanteNombre;
	}

	public void setSolicitanteNombre(String solicitanteNombre) {
		this.solicitanteNombre = solicitanteNombre;
	}

	/**
	 * @return the sucursalId
	 */
	public Integer getSucursalId() {
		return sucursalId;
	}

	/**
	 * @param sucursalId the sucursalId to set
	 */
	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

}