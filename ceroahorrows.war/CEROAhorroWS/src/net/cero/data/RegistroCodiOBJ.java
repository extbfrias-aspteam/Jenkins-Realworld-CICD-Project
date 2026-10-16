package net.cero.data;

public class RegistroCodiOBJ {
	private Integer id;
	private String cuenta;
	private String folioContrato;
	private Integer estatus;
	private String idSolicitante;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getFolioContrato() {
		return folioContrato;
	}
	public void setFolioContrato(String folioContrato) {
		this.folioContrato = folioContrato;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public String getIdSolicitante() {
		return idSolicitante;
	}
	public void setIdSolicitante(String idSolicitante) {
		this.idSolicitante = idSolicitante;
	}
}
