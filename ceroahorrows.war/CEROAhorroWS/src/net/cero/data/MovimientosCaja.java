package net.cero.data;

import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;


/**
 * The persistent class for the movimientos_caja database table.
 * 
 */
public class MovimientosCaja implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer movimientoId;
	private Integer cajaId;
	private Integer cajeroId;
	private Date fecha;
	private Integer tipoMovId;
	private Integer monedaId;
	private Double monto;
	private Double tipoCambio;
	private Integer formaPago;
	private String cuenta;
	private String control;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private Integer bancoId;
	private String cheque;
	private String estatus;
	private String obs;
	private String poliza;
	private Integer referenciaMovimientoId;
	private Integer bancoOrigen;
	private Date fechaDeposito;
	private String polizaProvision;
	private String transaccionId;
	private String tarjetaOperativaOd;
	private Integer regionId;
	private Long idImportacion;
	private Double monedaValor;
	private String monedaNombre;
	private Date monedaFechaValor;
	private Double montoOriginal;
	private Double montoDestino;
	private String monedaError;

	public MovimientosCaja() {
		super();
	}

	public MovimientosCaja(Integer movimientoId, Integer cajaId, Integer cajeroId, Date fecha, Integer tipoMovId,
			Integer monedaId, Double monto, Double tipoCambio, Integer formaPago, String cuenta, String control,
			Integer creadoPor, Timestamp fechaCreacion, Integer modificadoPor, Timestamp fechaModificacion,
			Integer bancoId, String cheque, String estatus, String obs, String poliza, Integer referenciaMovimientoId,
			Integer bancoOrigen, Date fechaDeposito, String polizaProvision, String transaccionId,
			String tarjetaOperativaOd, Integer regionId, Long idImportacion, Double monedaValor, String monedaNombre,
			Date monedaFechaValor, Double montoOriginal, Double montoDestino, String monedaError) {
		super();
		this.movimientoId = movimientoId;
		this.cajaId = cajaId;
		this.cajeroId = cajeroId;
		this.fecha = fecha;
		this.tipoMovId = tipoMovId;
		this.monedaId = monedaId;
		this.monto = monto;
		this.tipoCambio = tipoCambio;
		this.formaPago = formaPago;
		this.cuenta = cuenta;
		this.control = control;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.bancoId = bancoId;
		this.cheque = cheque;
		this.estatus = estatus;
		this.obs = obs;
		this.poliza = poliza;
		this.referenciaMovimientoId = referenciaMovimientoId;
		this.bancoOrigen = bancoOrigen;
		this.fechaDeposito = fechaDeposito;
		this.polizaProvision = polizaProvision;
		this.transaccionId = transaccionId;
		this.tarjetaOperativaOd = tarjetaOperativaOd;
		this.regionId = regionId;
		this.idImportacion = idImportacion;
		this.monedaValor = monedaValor;
		this.monedaNombre = monedaNombre;
		this.monedaFechaValor = monedaFechaValor;
		this.montoOriginal = montoOriginal;
		this.montoDestino = montoDestino;
		this.monedaError = monedaError;
	}

	public Integer getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(Integer movimientoId) {
		this.movimientoId = movimientoId;
	}

	public Integer getCajaId() {
		return cajaId;
	}

	public void setCajaId(Integer cajaId) {
		this.cajaId = cajaId;
	}

	public Integer getCajeroId() {
		return cajeroId;
	}

	public void setCajeroId(Integer cajeroId) {
		this.cajeroId = cajeroId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getTipoMovId() {
		return tipoMovId;
	}

	public void setTipoMovId(Integer tipoMovId) {
		this.tipoMovId = tipoMovId;
	}

	public Integer getMonedaId() {
		return monedaId;
	}

	public void setMonedaId(Integer monedaId) {
		this.monedaId = monedaId;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public Integer getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public Integer getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Integer getBancoId() {
		return bancoId;
	}

	public void setBancoId(Integer bancoId) {
		this.bancoId = bancoId;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getPoliza() {
		return poliza;
	}

	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}

	public Integer getReferenciaMovimientoId() {
		return referenciaMovimientoId;
	}

	public void setReferenciaMovimientoId(Integer referenciaMovimientoId) {
		this.referenciaMovimientoId = referenciaMovimientoId;
	}

	public Integer getBancoOrigen() {
		return bancoOrigen;
	}

	public void setBancoOrigen(Integer bancoOrigen) {
		this.bancoOrigen = bancoOrigen;
	}

	public Date getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getPolizaProvision() {
		return polizaProvision;
	}

	public void setPolizaProvision(String polizaProvision) {
		this.polizaProvision = polizaProvision;
	}

	public String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(String transaccionId) {
		this.transaccionId = transaccionId;
	}

	public String getTarjetaOperativaOd() {
		return tarjetaOperativaOd;
	}

	public void setTarjetaOperativaOd(String tarjetaOperativaOd) {
		this.tarjetaOperativaOd = tarjetaOperativaOd;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Long getIdImportacion() {
		return idImportacion;
	}

	public void setIdImportacion(Long idImportacion) {
		this.idImportacion = idImportacion;
	}

	public Double getMonedaValor() {
		return monedaValor;
	}

	public void setMonedaValor(Double monedaValor) {
		this.monedaValor = monedaValor;
	}

	public String getMonedaNombre() {
		return monedaNombre;
	}

	public void setMonedaNombre(String monedaNombre) {
		this.monedaNombre = monedaNombre;
	}

	public Date getMonedaFechaValor() {
		return monedaFechaValor;
	}

	public void setMonedaFechaValor(Date monedaFechaValor) {
		this.monedaFechaValor = monedaFechaValor;
	}

	public Double getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(Double montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public Double getMontoDestino() {
		return montoDestino;
	}

	public void setMontoDestino(Double montoDestino) {
		this.montoDestino = montoDestino;
	}

	public String getMonedaError() {
		return monedaError;
	}

	public void setMonedaError(String monedaError) {
		this.monedaError = monedaError;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}