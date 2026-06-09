package net.std.data;

import java.io.Serializable;

public class DatosPldOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cuenta_id;
	private Integer estatus_id;
	private Double ingreso_mensual;
	private Double monto_maximo_ahorro;
	private String puesto;

	private String prov_recursos_id;
	private String proveedor_recursos_id;
	private Double monto_maximo_ahorro_prov;
	private Double ingreso_mensual_prov;
	private String puesto_prov;
	private Integer relacion_id;
	private Boolean indicador_Prov_Recursos;
	
	private Integer usuario_id;
	
	public DatosPldOBJ(){
		
	}

	public Double getIngreso_mensual() {
		return ingreso_mensual;
	}

	public void setIngreso_mensual(Double ingreso_mensual) {
		this.ingreso_mensual = ingreso_mensual;
	}

	public Double getMonto_maximo_ahorro() {
		return monto_maximo_ahorro;
	}

	public void setMonto_maximo_ahorro(Double monto_maximo_ahorro) {
		this.monto_maximo_ahorro = monto_maximo_ahorro;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getProveedor_recursos_id() {
		return proveedor_recursos_id;
	}

	public void setProveedor_recursos_id(String proveedor_recursos_id) {
		this.proveedor_recursos_id = proveedor_recursos_id;
	}

	public Double getMonto_maximo_ahorro_prov() {
		return monto_maximo_ahorro_prov;
	}

	public void setMonto_maximo_ahorro_prov(Double monto_maximo_ahorro_prov) {
		this.monto_maximo_ahorro_prov = monto_maximo_ahorro_prov;
	}

	public String getPuesto_prov() {
		return puesto_prov;
	}

	public void setPuesto_prov(String puesto_prov) {
		this.puesto_prov = puesto_prov;
	}

	public Integer getRelacion_id() {
		return relacion_id;
	}

	public void setRelacion_id(Integer relacion_id) {
		this.relacion_id = relacion_id;
	}

	public Integer getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(Integer cuenta_id) {
		this.cuenta_id = cuenta_id;
	}

	public Integer getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(Integer estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getProv_recursos_id() {
		return prov_recursos_id;
	}

	public void setProv_recursos_id(String prov_recursos_id) {
		this.prov_recursos_id = prov_recursos_id;
	}

	public Boolean getIndicador_Prov_Recursos() {
		return indicador_Prov_Recursos;
	}

	public void setIndicador_Prov_Recursos(Boolean indicador_Prov_Recursos) {
		this.indicador_Prov_Recursos = indicador_Prov_Recursos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Integer usuario_id) {
		this.usuario_id = usuario_id;
	}

	public Double getIngreso_mensual_prov() {
		return ingreso_mensual_prov;
	}

	public void setIngreso_mensual_prov(Double ingreso_mensual_prov) {
		this.ingreso_mensual_prov = ingreso_mensual_prov;
	}

}
