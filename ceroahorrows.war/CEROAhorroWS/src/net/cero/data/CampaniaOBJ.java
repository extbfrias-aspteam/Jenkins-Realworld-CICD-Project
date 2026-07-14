package net.cero.data;

import java.sql.Date;

public class CampaniaOBJ {

	private String campania;
	private Integer influencerId;
	private Double costo;
	private Integer meta;
	private Double incentivo;
	private Date vigencia;
	private Integer acumulados;
	private String cuentaAhorro;

	public String getCampania() {
		return campania;
	}

	public void setCampania(String campania) {
		this.campania = campania;
	}

	public Integer getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Integer influencerId) {
		this.influencerId = influencerId;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Integer getMeta() {
		return meta;
	}

	public void setMeta(Integer meta) {
		this.meta = meta;
	}

	public Double getIncentivo() {
		return incentivo;
	}

	public void setIncentivo(Double incentivo) {
		this.incentivo = incentivo;
	}

	public Date getVigencia() {
		return vigencia;
	}

	public void setVigencia(Date vigencia) {
		this.vigencia = vigencia;
	}

	public Integer getAcumulados() {
		return acumulados;
	}

	public void setAcumulados(Integer acumulados) {
		this.acumulados = acumulados;
	}

	public String getCuentaAhorro() {
		return cuentaAhorro;
	}

	public void setCuentaAhorro(String cuentaAhorro) {
		this.cuentaAhorro = cuentaAhorro;
	}

}
