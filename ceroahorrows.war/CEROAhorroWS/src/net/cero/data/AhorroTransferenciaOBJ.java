package net.cero.data;

import java.security.Timestamp;
import java.util.Date;

public class AhorroTransferenciaOBJ {

	private Integer id;
	private String cuentaOrigen;
	private String cuentaDestino;
	private Date fecha;
	private Double monto;
	private Integer movimientoId;
	private Integer creadoPor;
	private Timestamp fechaCreacion;
	private Integer modificadoPor;
	private Timestamp fechaModificacion;
	private Integer depositoId;
	private Integer disposicionId;
	private Integer spei;
	/**
	 * 
	 */
	public AhorroTransferenciaOBJ() {
		super();
	}
	/**
	 * @param id
	 * @param cuentaOrigen
	 * @param cuentaDestino
	 * @param fecha
	 * @param monto
	 * @param movimientoId
	 * @param creadoPor
	 * @param fechaCreacion
	 * @param modificadoPor
	 * @param fechaModificacion
	 * @param depositoId
	 * @param disposicionId
	 * @param spei
	 */
	public AhorroTransferenciaOBJ(Integer id, String cuentaOrigen, String cuentaDestino, Date fecha, Double monto,
			Integer movimientoId, Integer creadoPor, Timestamp fechaCreacion, Integer modificadoPor,
			Timestamp fechaModificacion, Integer depositoId, Integer disposicionId, Integer spei) {
		super();
		this.id = id;
		this.cuentaOrigen = cuentaOrigen;
		this.cuentaDestino = cuentaDestino;
		this.fecha = fecha;
		this.monto = monto;
		this.movimientoId = movimientoId;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.depositoId = depositoId;
		this.disposicionId = disposicionId;
		this.spei = spei;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cuentaOrigen
	 */
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	/**
	 * @param cuentaOrigen the cuentaOrigen to set
	 */
	public void setCuentaOrigen(String cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
	/**
	 * @return the cuentaDestino
	 */
	public String getCuentaDestino() {
		return cuentaDestino;
	}
	/**
	 * @param cuentaDestino the cuentaDestino to set
	 */
	public void setCuentaDestino(String cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the monto
	 */
	public Double getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	/**
	 * @return the movimientoId
	 */
	public Integer getMovimientoId() {
		return movimientoId;
	}
	/**
	 * @param movimientoId the movimientoId to set
	 */
	public void setMovimientoId(Integer movimientoId) {
		this.movimientoId = movimientoId;
	}
	/**
	 * @return the creadoPor
	 */
	public Integer getCreadoPor() {
		return creadoPor;
	}
	/**
	 * @param creadoPor the creadoPor to set
	 */
	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}
	/**
	 * @return the fechaCreacion
	 */
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}
	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	/**
	 * @return the modificadoPor
	 */
	public Integer getModificadoPor() {
		return modificadoPor;
	}
	/**
	 * @param modificadoPor the modificadoPor to set
	 */
	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	/**
	 * @return the fechaModificacion
	 */
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the depositoId
	 */
	public Integer getDepositoId() {
		return depositoId;
	}
	/**
	 * @param depositoId the depositoId to set
	 */
	public void setDepositoId(Integer depositoId) {
		this.depositoId = depositoId;
	}
	/**
	 * @return the disposicionId
	 */
	public Integer getDisposicionId() {
		return disposicionId;
	}
	/**
	 * @param disposicionId the disposicionId to set
	 */
	public void setDisposicionId(Integer disposicionId) {
		this.disposicionId = disposicionId;
	}
	/**
	 * @return the spei
	 */
	public Integer getSpei() {
		return spei;
	}
	/**
	 * @param spei the spei to set
	 */
	public void setSpei(Integer spei) {
		this.spei = spei;
	}
	
}