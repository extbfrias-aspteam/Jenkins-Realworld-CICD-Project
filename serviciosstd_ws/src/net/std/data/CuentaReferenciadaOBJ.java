package net.std.data;

import java.io.Serializable;

public class CuentaReferenciadaOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer cuenta_id;
	private String cuenta;
	private String clabe_interbancaria;
	private String cuenta_referencia;
	private String nombre_referencia;
	private String rfc_referencia;
	private String curp_referencia;
	private String correo_referencia;
	private String telefono_referencia;
	private Integer estatus_id;
	private String observaciones;
	private String fecha;
	private String control;
	private String tipo_cliente;
	private Long tipo_cuenta_id;
	private String tipo_cuenta;
	private String valor;
	private Integer usuario_creacion;

	private String persona_id;
	private Double monto_maximo_ahorro;
	private String unidad_negocio;
	private String tipo_cuenta_nivel;

	public CuentaReferenciadaOBJ() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(Integer cuenta_id) {
		this.cuenta_id = cuenta_id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getClabe_interbancaria() {
		return clabe_interbancaria;
	}

	public void setClabe_interbancaria(String clabe_interbancaria) {
		this.clabe_interbancaria = clabe_interbancaria;
	}

	public String getCuenta_referencia() {
		return cuenta_referencia;
	}

	public void setCuenta_referencia(String cuenta_referencia) {
		this.cuenta_referencia = cuenta_referencia;
	}

	public String getNombre_referencia() {
		return nombre_referencia;
	}

	public void setNombre_referencia(String nombre_referencia) {
		this.nombre_referencia = nombre_referencia;
	}

	public String getRfc_referencia() {
		return rfc_referencia;
	}

	public void setRfc_referencia(String rfc_referencia) {
		this.rfc_referencia = rfc_referencia;
	}

	public String getCurp_referencia() {
		return curp_referencia;
	}

	public void setCurp_referencia(String curp_referencia) {
		this.curp_referencia = curp_referencia;
	}

	public String getCorreo_referencia() {
		return correo_referencia;
	}

	public void setCorreo_referencia(String correo_referencia) {
		this.correo_referencia = correo_referencia;
	}

	public String getTelefono_referencia() {
		return telefono_referencia;
	}

	public void setTelefono_referencia(String telefono_referencia) {
		this.telefono_referencia = telefono_referencia;
	}

	public Integer getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(Integer estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public Integer getUsuario_creacion() {
		return usuario_creacion;
	}

	public void setUsuario_creacion(Integer usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}

	public Long getTipo_cuenta_id() {
		return tipo_cuenta_id;
	}

	public void setTipo_cuenta_id(Long tipo_cuenta_id) {
		this.tipo_cuenta_id = tipo_cuenta_id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo_cuenta() {
		return tipo_cuenta;
	}

	public void setTipo_cuenta(String tipo_cuenta) {
		this.tipo_cuenta = tipo_cuenta;
	}

	public String getTipo_cliente() {
		return tipo_cliente;
	}

	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}

	public String getPersona_id() {
		return persona_id;
	}

	public void setPersona_id(String persona_id) {
		this.persona_id = persona_id;
	}

	public Double getMonto_maximo_ahorro() {
		return monto_maximo_ahorro;
	}

	public void setMonto_maximo_ahorro(Double monto_maximo_ahorro) {
		this.monto_maximo_ahorro = monto_maximo_ahorro;
	}

	public String getUnidad_negocio() {
		return unidad_negocio;
	}

	public void setUnidad_negocio(String unidad_negocio) {
		this.unidad_negocio = unidad_negocio;
	}

	public String getTipo_cuenta_nivel() {
		return tipo_cuenta_nivel;
	}

	public void setTipo_cuenta_nivel(String tipo_cuenta_nivel) {
		this.tipo_cuenta_nivel = tipo_cuenta_nivel;
	}
}
