package net.cero.data.nuevospei;


import java.util.Date;

//import java.sql.Date;

public class AsignaCuentaPanOBJ {
	
	private Integer id;
	private Integer cuenta_id;
	private Integer pan_id;
	private String pan;
	private Integer estatus_id;
	private Integer vigente;
	private Date fecha_entrega;
	private Date fecha_activacion;
	private Date fecha_bloqueo;
	private Date fecha_cancelacion;
	private Integer usuario_creacion;
	private Date fecha_creacion;
	private Integer usuario_modificacion;
	private Date fecha_modificacion;
	
	public AsignaCuentaPanOBJ() {
			//Constructor
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPan_id() {
		return pan_id;
	}

	public void setPan_id(Integer pan_id) {
		this.pan_id = pan_id;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Integer getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(Integer estatus_id) {
		this.estatus_id = estatus_id;
	}

	public Integer getVigente() {
		return vigente;
	}

	public void setVigente(Integer vigente) {
		this.vigente = vigente;
	}

	public Date getFecha_entrega() {
		return fecha_entrega;
	}

	public void setFecha_entrega(Date fecha_entrega) {
		this.fecha_entrega = fecha_entrega;
	}

	public Date getFecha_activacion() {
		return fecha_activacion;
	}

	public void setFecha_activacion(Date fecha_activacion) {
		this.fecha_activacion = fecha_activacion;
	}

	public Date getFecha_bloqueo() {
		return fecha_bloqueo;
	}

	public void setFecha_bloqueo(Date fecha_bloqueo) {
		this.fecha_bloqueo = fecha_bloqueo;
	}

	public Date getFecha_cancelacion() {
		return fecha_cancelacion;
	}

	public void setFecha_cancelacion(Date fecha_cancelacion) {
		this.fecha_cancelacion = fecha_cancelacion;
	}

	public Integer getUsuario_creacion() {
		return usuario_creacion;
	}

	public void setUsuario_creacion(Integer usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Integer getUsuario_modificacion() {
		return usuario_modificacion;
	}

	public void setUsuario_modificacion(Integer usuario_modificacion) {
		this.usuario_modificacion = usuario_modificacion;
	}

	public Date getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(Date fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}

	public Integer getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(Integer cuenta_id) {
		this.cuenta_id = cuenta_id;
	}
}
