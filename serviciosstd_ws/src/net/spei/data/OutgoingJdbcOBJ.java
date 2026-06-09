package net.spei.data;

import java.io.Serializable;

public class OutgoingJdbcOBJ implements Serializable{
	private static final long serialVersionUID = 1L;

	private java.lang.Long id_spei_outgoing;
	private String nombre_ordenante;
	private String id_tipo_cuenta_ordenante;
	private String cuenta_ordenante;
	private String rfc_ordenante;
	private String nombre_beneficiario;
	private String id_tipo_cuenta_beneficiario;
	private String cuenta_beneficiario;
	private String rfc_beneficiario;
	private String concepto_pago;
	private String monto;
	private String iva;
	private Integer referencia_numerica;
	private String referencia;
	private String id_tipo_pago;
	private Integer id_institucion_ben;
	private String status;
	private String envio_automatico;
	private Integer tipo_operacion;
	private Integer status_operacion;
	private String clave_rastreo;
	private Integer id_devolucion;
	private java.util.Date fecha_captura;
	private java.util.Date fh_operacion;
	private Long id_operacion;
	private String referencia_cobranza;
	private Integer id_area_emite;
	private String topologia;	
	private Integer verificado;	
	private String clave_pago;
	private Integer prioridad;	
	private String inf_adicional;
	private String app;
	private java.lang.Long modificado_por;
	private java.util.Date fecha_modificacion;
	private Long usuario_id;
	private java.lang.Long sucursal_id;
	private java.lang.Long empresa_id;
	private String usuario;
	private String correo_electronico;
	private String correo_beneficiario;
	private java.lang.Integer procesado;
	private java.lang.Long core_id;
	private String observaciones;
	private String firma;
	private String llave_aes;
	private String cuenta_ordenante_cf;
	private String nombre_ordenante_cf;
	private String cuenta_beneficiario_cf;
	private String nombre_beneficiario_cf;	
	private String monto_cf;
	private String iva_cf;
	private String concepto_pago_cf;
	private String movil_ordenante;
	private String movil_ordenante_cfr;
	private String movil_beneficiario;
	private String movil_beneficiario_cfr;
	private String num_folio_esquema_codi;
	private String num_folio_esquema_codi_cfr;
	private String pago_comision;
	private String pago_comision_cfr;
	private String monto_comision;
	private String monto_comision_cfr;
	private String fh_hr_limite_de_pago;
	private String num_serie_cert_comercio;
	private String num_serie_cert_comercio_cfr;
	private String nombre_beneficiario2;
	private String nombre_beneficiario2_cfr;
	private String id_tipo_cuenta_beneficiario2;
	private String cuenta_beneficiario2;
	private String cuenta_beneficiario2_cfr;
	private String rfc_beneficiario2;
	private String rfc_beneficiario2_cfr;
	private String dv_movil_ordenante;	
	private String dv_movil_ordenante_cfr;
	private String dv_movil_beneficiario;
	private String dv_movil_beneficiario_cfr;
	
	public OutgoingJdbcOBJ(){
		
	}

	public java.lang.Long getId_spei_outgoing() {
		return id_spei_outgoing;
	}

	public void setId_spei_outgoing(java.lang.Long id_spei_outgoing) {
		this.id_spei_outgoing = id_spei_outgoing;
	}

	public String getNombre_ordenante() {
		return nombre_ordenante;
	}

	public void setNombre_ordenante(String nombre_ordenante) {
		this.nombre_ordenante = nombre_ordenante;
	}

	public String getId_tipo_cuenta_ordenante() {
		return id_tipo_cuenta_ordenante;
	}

	public void setId_tipo_cuenta_ordenante(String id_tipo_cuenta_ordenante) {
		this.id_tipo_cuenta_ordenante = id_tipo_cuenta_ordenante;
	}

	public String getCuenta_ordenante() {
		return cuenta_ordenante;
	}

	public void setCuenta_ordenante(String cuenta_ordenante) {
		this.cuenta_ordenante = cuenta_ordenante;
	}

	public String getRfc_ordenante() {
		return rfc_ordenante;
	}

	public void setRfc_ordenante(String rfc_ordenante) {
		this.rfc_ordenante = rfc_ordenante;
	}

	public String getNombre_beneficiario() {
		return nombre_beneficiario;
	}

	public void setNombre_beneficiario(String nombre_beneficiario) {
		this.nombre_beneficiario = nombre_beneficiario;
	}

	public String getId_tipo_cuenta_beneficiario() {
		return id_tipo_cuenta_beneficiario;
	}

	public void setId_tipo_cuenta_beneficiario(String id_tipo_cuenta_beneficiario) {
		this.id_tipo_cuenta_beneficiario = id_tipo_cuenta_beneficiario;
	}

	public String getCuenta_beneficiario() {
		return cuenta_beneficiario;
	}

	public void setCuenta_beneficiario(String cuenta_beneficiario) {
		this.cuenta_beneficiario = cuenta_beneficiario;
	}

	public String getRfc_beneficiario() {
		return rfc_beneficiario;
	}

	public void setRfc_beneficiario(String rfc_beneficiario) {
		this.rfc_beneficiario = rfc_beneficiario;
	}

	public String getConcepto_pago() {
		return concepto_pago;
	}

	public void setConcepto_pago(String concepto_pago) {
		this.concepto_pago = concepto_pago;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public Integer getReferencia_numerica() {
		return referencia_numerica;
	}

	public void setReferencia_numerica(Integer referencia_numerica) {
		this.referencia_numerica = referencia_numerica;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getId_tipo_pago() {
		return id_tipo_pago;
	}

	public void setId_tipo_pago(String id_tipo_pago) {
		this.id_tipo_pago = id_tipo_pago;
	}

	public Integer getId_institucion_ben() {
		return id_institucion_ben;
	}

	public void setId_institucion_ben(Integer id_institucion_ben) {
		this.id_institucion_ben = id_institucion_ben;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnvio_automatico() {
		return envio_automatico;
	}

	public void setEnvio_automatico(String envio_automatico) {
		this.envio_automatico = envio_automatico;
	}

	public Integer getTipo_operacion() {
		return tipo_operacion;
	}

	public void setTipo_operacion(Integer tipo_operacion) {
		this.tipo_operacion = tipo_operacion;
	}

	public Integer getStatus_operacion() {
		return status_operacion;
	}

	public void setStatus_operacion(Integer status_operacion) {
		this.status_operacion = status_operacion;
	}

	public String getClave_rastreo() {
		return clave_rastreo;
	}

	public void setClave_rastreo(String clave_rastreo) {
		this.clave_rastreo = clave_rastreo;
	}

	public Integer getId_devolucion() {
		return id_devolucion;
	}

	public void setId_devolucion(Integer id_devolucion) {
		this.id_devolucion = id_devolucion;
	}

	public java.util.Date getFecha_captura() {
		return fecha_captura;
	}

	public void setFecha_captura(java.util.Date fecha_captura) {
		this.fecha_captura = fecha_captura;
	}

	public java.util.Date getFh_operacion() {
		return fh_operacion;
	}

	public void setFh_operacion(java.util.Date fh_operacion) {
		this.fh_operacion = fh_operacion;
	}

	public Long getId_operacion() {
		return id_operacion;
	}

	public void setId_operacion(Long id_operacion) {
		this.id_operacion = id_operacion;
	}

	public String getReferencia_cobranza() {
		return referencia_cobranza;
	}

	public void setReferencia_cobranza(String referencia_cobranza) {
		this.referencia_cobranza = referencia_cobranza;
	}

	public Integer getId_area_emite() {
		return id_area_emite;
	}

	public void setId_area_emite(Integer id_area_emite) {
		this.id_area_emite = id_area_emite;
	}

	public String getTopologia() {
		return topologia;
	}

	public void setTopologia(String topologia) {
		this.topologia = topologia;
	}

	public Integer getVerificado() {
		return verificado;
	}

	public void setVerificado(Integer verificado) {
		this.verificado = verificado;
	}

	public String getClave_pago() {
		return clave_pago;
	}

	public void setClave_pago(String clave_pago) {
		this.clave_pago = clave_pago;
	}

	public Integer getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

	public String getInf_adicional() {
		return inf_adicional;
	}

	public void setInf_adicional(String inf_adicional) {
		this.inf_adicional = inf_adicional;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public java.lang.Long getModificado_por() {
		return modificado_por;
	}

	public void setModificado_por(java.lang.Long modificado_por) {
		this.modificado_por = modificado_por;
	}

	public java.util.Date getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(java.util.Date fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}

	public Long getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}

	public java.lang.Long getSucursal_id() {
		return sucursal_id;
	}

	public void setSucursal_id(java.lang.Long sucursal_id) {
		this.sucursal_id = sucursal_id;
	}

	public java.lang.Long getEmpresa_id() {
		return empresa_id;
	}

	public void setEmpresa_id(java.lang.Long empresa_id) {
		this.empresa_id = empresa_id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCorreo_electronico() {
		return correo_electronico;
	}

	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}

	public String getCorreo_beneficiario() {
		return correo_beneficiario;
	}

	public void setCorreo_beneficiario(String correo_beneficiario) {
		this.correo_beneficiario = correo_beneficiario;
	}

	public java.lang.Integer getProcesado() {
		return procesado;
	}

	public void setProcesado(java.lang.Integer procesado) {
		this.procesado = procesado;
	}

	public java.lang.Long getCore_id() {
		return core_id;
	}

	public void setCore_id(java.lang.Long core_id) {
		this.core_id = core_id;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getLlave_aes() {
		return llave_aes;
	}

	public void setLlave_aes(String llave_aes) {
		this.llave_aes = llave_aes;
	}

	public String getCuenta_ordenante_cf() {
		return cuenta_ordenante_cf;
	}

	public void setCuenta_ordenante_cf(String cuenta_ordenante_cf) {
		this.cuenta_ordenante_cf = cuenta_ordenante_cf;
	}

	public String getNombre_ordenante_cf() {
		return nombre_ordenante_cf;
	}

	public void setNombre_ordenante_cf(String nombre_ordenante_cf) {
		this.nombre_ordenante_cf = nombre_ordenante_cf;
	}

	public String getCuenta_beneficiario_cf() {
		return cuenta_beneficiario_cf;
	}

	public void setCuenta_beneficiario_cf(String cuenta_beneficiario_cf) {
		this.cuenta_beneficiario_cf = cuenta_beneficiario_cf;
	}

	public String getNombre_beneficiario_cf() {
		return nombre_beneficiario_cf;
	}

	public void setNombre_beneficiario_cf(String nombre_beneficiario_cf) {
		this.nombre_beneficiario_cf = nombre_beneficiario_cf;
	}

	public String getMonto_cf() {
		return monto_cf;
	}

	public void setMonto_cf(String monto_cf) {
		this.monto_cf = monto_cf;
	}

	public String getIva_cf() {
		return iva_cf;
	}

	public void setIva_cf(String iva_cf) {
		this.iva_cf = iva_cf;
	}

	public String getConcepto_pago_cf() {
		return concepto_pago_cf;
	}

	public void setConcepto_pago_cf(String concepto_pago_cf) {
		this.concepto_pago_cf = concepto_pago_cf;
	}

	public String getMovil_ordenante() {
		return movil_ordenante;
	}

	public void setMovil_ordenante(String movil_ordenante) {
		this.movil_ordenante = movil_ordenante;
	}

	public String getMovil_ordenante_cfr() {
		return movil_ordenante_cfr;
	}

	public void setMovil_ordenante_cfr(String movil_ordenante_cfr) {
		this.movil_ordenante_cfr = movil_ordenante_cfr;
	}

	public String getMovil_beneficiario() {
		return movil_beneficiario;
	}

	public void setMovil_beneficiario(String movil_beneficiario) {
		this.movil_beneficiario = movil_beneficiario;
	}

	public String getMovil_beneficiario_cfr() {
		return movil_beneficiario_cfr;
	}

	public void setMovil_beneficiario_cfr(String movil_beneficiario_cfr) {
		this.movil_beneficiario_cfr = movil_beneficiario_cfr;
	}

	public String getNum_folio_esquema_codi() {
		return num_folio_esquema_codi;
	}

	public void setNum_folio_esquema_codi(String num_folio_esquema_codi) {
		this.num_folio_esquema_codi = num_folio_esquema_codi;
	}

	public String getNum_folio_esquema_codi_cfr() {
		return num_folio_esquema_codi_cfr;
	}

	public void setNum_folio_esquema_codi_cfr(String num_folio_esquema_codi_cfr) {
		this.num_folio_esquema_codi_cfr = num_folio_esquema_codi_cfr;
	}

	public String getPago_comision() {
		return pago_comision;
	}

	public void setPago_comision(String pago_comision) {
		this.pago_comision = pago_comision;
	}

	public String getPago_comision_cfr() {
		return pago_comision_cfr;
	}

	public void setPago_comision_cfr(String pago_comision_cfr) {
		this.pago_comision_cfr = pago_comision_cfr;
	}

	public String getMonto_comision() {
		return monto_comision;
	}

	public void setMonto_comision(String monto_comision) {
		this.monto_comision = monto_comision;
	}

	public String getMonto_comision_cfr() {
		return monto_comision_cfr;
	}

	public void setMonto_comision_cfr(String monto_comision_cfr) {
		this.monto_comision_cfr = monto_comision_cfr;
	}

	public String getFh_hr_limite_de_pago() {
		return fh_hr_limite_de_pago;
	}

	public void setFh_hr_limite_de_pago(String fh_hr_limite_de_pago) {
		this.fh_hr_limite_de_pago = fh_hr_limite_de_pago;
	}

	public String getNum_serie_cert_comercio() {
		return num_serie_cert_comercio;
	}

	public void setNum_serie_cert_comercio(String num_serie_cert_comercio) {
		this.num_serie_cert_comercio = num_serie_cert_comercio;
	}

	public String getNum_serie_cert_comercio_cfr() {
		return num_serie_cert_comercio_cfr;
	}

	public void setNum_serie_cert_comercio_cfr(String num_serie_cert_comercio_cfr) {
		this.num_serie_cert_comercio_cfr = num_serie_cert_comercio_cfr;
	}

	public String getNombre_beneficiario2() {
		return nombre_beneficiario2;
	}

	public void setNombre_beneficiario2(String nombre_beneficiario2) {
		this.nombre_beneficiario2 = nombre_beneficiario2;
	}

	public String getNombre_beneficiario2_cfr() {
		return nombre_beneficiario2_cfr;
	}

	public void setNombre_beneficiario2_cfr(String nombre_beneficiario2_cfr) {
		this.nombre_beneficiario2_cfr = nombre_beneficiario2_cfr;
	}

	public String getId_tipo_cuenta_beneficiario2() {
		return id_tipo_cuenta_beneficiario2;
	}

	public void setId_tipo_cuenta_beneficiario2(String id_tipo_cuenta_beneficiario2) {
		this.id_tipo_cuenta_beneficiario2 = id_tipo_cuenta_beneficiario2;
	}

	public String getCuenta_beneficiario2() {
		return cuenta_beneficiario2;
	}

	public void setCuenta_beneficiario2(String cuenta_beneficiario2) {
		this.cuenta_beneficiario2 = cuenta_beneficiario2;
	}

	public String getCuenta_beneficiario2_cfr() {
		return cuenta_beneficiario2_cfr;
	}

	public void setCuenta_beneficiario2_cfr(String cuenta_beneficiario2_cfr) {
		this.cuenta_beneficiario2_cfr = cuenta_beneficiario2_cfr;
	}

	public String getRfc_beneficiario2() {
		return rfc_beneficiario2;
	}

	public void setRfc_beneficiario2(String rfc_beneficiario2) {
		this.rfc_beneficiario2 = rfc_beneficiario2;
	}

	public String getRfc_beneficiario2_cfr() {
		return rfc_beneficiario2_cfr;
	}

	public void setRfc_beneficiario2_cfr(String rfc_beneficiario2_cfr) {
		this.rfc_beneficiario2_cfr = rfc_beneficiario2_cfr;
	}

	public String getDv_movil_ordenante() {
		return dv_movil_ordenante;
	}

	public void setDv_movil_ordenante(String dv_movil_ordenante) {
		this.dv_movil_ordenante = dv_movil_ordenante;
	}

	public String getDv_movil_ordenante_cfr() {
		return dv_movil_ordenante_cfr;
	}

	public void setDv_movil_ordenante_cfr(String dv_movil_ordenante_cfr) {
		this.dv_movil_ordenante_cfr = dv_movil_ordenante_cfr;
	}

	public String getDv_movil_beneficiario() {
		return dv_movil_beneficiario;
	}

	public void setDv_movil_beneficiario(String dv_movil_beneficiario) {
		this.dv_movil_beneficiario = dv_movil_beneficiario;
	}

	public String getDv_movil_beneficiario_cfr() {
		return dv_movil_beneficiario_cfr;
	}

	public void setDv_movil_beneficiario_cfr(String dv_movil_beneficiario_cfr) {
		this.dv_movil_beneficiario_cfr = dv_movil_beneficiario_cfr;
	}
}
