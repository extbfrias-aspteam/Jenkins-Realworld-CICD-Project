package net.std.data;

import java.io.Serializable;

public class CuentaProductoOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String cuenta;
	private String estatus_id;
	private String estatus_cve;
	private String estatus;
	private String persona_id;
	private String nombre;
	private String correo;
	private String tipo_persona;
	private String celular;
	private String curp;
	private String rfc;
	private String producto_ahorro_id;
	private String producto;
	private String fecha_apertura;
	private String monto_apertura;
	private String sucursal_id;
	private String clabe_interbancaria;
	private String referencia;
	private String clabe_eje;
	private String bloqueado_id;
	private String bloqueado;
	private String tipo_cliente;
	private String bloqueado_anterior_id;
	private String fecha_bloqueado;
	private String val_exp;
	private String permite_transacciones;
	private String total_expedientes;
	private String dias_para_cancelar;
	private String dias_para_transaccionar;
	
	private String dias_habiles;
	private Boolean total_documentos_validados;
	private String notificaciones;
	private String notificaciones_anteriores;

	public CuentaProductoOBJ(){
		this.total_documentos_validados = false;
		this.notificaciones = "";
		this.notificaciones_anteriores = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getEstatus_id() {
		return estatus_id;
	}

	public void setEstatus_id(String estatus_id) {
		this.estatus_id = estatus_id;
	}

	public String getPersona_id() {
		return persona_id;
	}

	public void setPersona_id(String persona_id) {
		this.persona_id = persona_id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProducto_ahorro_id() {
		return producto_ahorro_id;
	}

	public void setProducto_ahorro_id(String producto_ahorro_id) {
		this.producto_ahorro_id = producto_ahorro_id;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getFecha_apertura() {
		return fecha_apertura;
	}

	public void setFecha_apertura(String fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}

	public String getMonto_apertura() {
		return monto_apertura;
	}

	public void setMonto_apertura(String monto_apertura) {
		this.monto_apertura = monto_apertura;
	}

	public String getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(String sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public String getClabe_interbancaria() {
		return clabe_interbancaria;
	}

	public void setClabe_interbancaria(String clabe_interbancaria) {
		this.clabe_interbancaria = clabe_interbancaria;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getClabe_eje() {
		return clabe_eje;
	}

	public void setClabe_eje(String clabe_eje) {
		this.clabe_eje = clabe_eje;
	}

	public String getBloqueado_id() {
		return bloqueado_id;
	}

	public void setBloqueado_id(String bloqueado_id) {
		this.bloqueado_id = bloqueado_id;
	}

	public String getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getTipo_cliente() {
		return tipo_cliente;
	}

	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}

	public String getBloqueado_anterior_id() {
		return bloqueado_anterior_id;
	}

	public void setBloqueado_anterior_id(String bloqueado_anterior_id) {
		this.bloqueado_anterior_id = bloqueado_anterior_id;
	}

	public String getFecha_bloqueado() {
		return fecha_bloqueado;
	}

	public void setFecha_bloqueado(String fecha_bloqueado) {
		this.fecha_bloqueado = fecha_bloqueado;
	}

	public String getVal_exp() {
		return val_exp;
	}

	public void setVal_exp(String val_exp) {
		this.val_exp = val_exp;
	}

	public String getPermite_transacciones() {
		return permite_transacciones;
	}

	public void setPermite_transacciones(String permite_transacciones) {
		this.permite_transacciones = permite_transacciones;
	}

	public String getEstatus_cve() {
		return estatus_cve;
	}

	public void setEstatus_cve(String estatus_cve) {
		this.estatus_cve = estatus_cve;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getTotal_expedientes() {
		return total_expedientes;
	}

	public void setTotal_expedientes(String total_expedientes) {
		this.total_expedientes = total_expedientes;
	}

	public String getDias_para_cancelar() {
		return dias_para_cancelar;
	}

	public void setDias_para_cancelar(String dias_para_cancelar) {
		this.dias_para_cancelar = dias_para_cancelar;
	}

	public String getDias_para_transaccionar() {
		return dias_para_transaccionar;
	}

	public void setDias_para_transaccionar(String dias_para_transaccionar) {
		this.dias_para_transaccionar = dias_para_transaccionar;
	}

	public String getDias_habiles() {
		return dias_habiles;
	}

	public void setDias_habiles(String dias_habiles) {
		this.dias_habiles = dias_habiles;
	}

	public Boolean getTotal_documentos_validados() {
		return total_documentos_validados;
	}

	public void setTotal_documentos_validados(Boolean total_documentos_validados) {
		this.total_documentos_validados = total_documentos_validados;
	}

	public String getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(String notificaciones) {
		this.notificaciones = notificaciones;
	}

	public String getNotificaciones_anteriores() {
		return notificaciones_anteriores;
	}

	public void setNotificaciones_anteriores(String notificaciones_anteriores) {
		this.notificaciones_anteriores = notificaciones_anteriores;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTipo_persona() {
		return tipo_persona;
	}

	public void setTipo_persona(String tipo_persona) {
		this.tipo_persona = tipo_persona;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
}

