package net.cero.data.nuevospei;

public class ComisionPendienteOBJ {

	private Integer id;
	private String cuentaId;
	private Integer pan_id;
	private String pan;
	private Double cobradas;
	private Double pendientes;
	private String estatus;
	private Integer usuarioId;
	
	public ComisionPendienteOBJ(){
		// Constructor
		this.cobradas = 0.00d;
		this.pendientes = 0.00d;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(String cuentaId) {
		this.cuentaId = cuentaId;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Double getCobradas() {
		return cobradas;
	}

	public void setCobradas(Double cobradas) {
		this.cobradas = cobradas;
	}

	public Double getPendientes() {
		return pendientes;
	}

	public void setPendientes(Double pendientes) {
		this.pendientes = pendientes;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Integer getPan_id() {
		return pan_id;
	}

	public void setPan_id(Integer pan_id) {
		this.pan_id = pan_id;
	}
}
