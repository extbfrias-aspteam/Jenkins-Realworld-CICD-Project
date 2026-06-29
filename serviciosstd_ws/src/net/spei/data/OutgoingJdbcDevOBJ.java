package net.spei.data;

import java.io.Serializable;

public class OutgoingJdbcDevOBJ implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nombre_institucion_ben;
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
	
	
	
	/**
	 * @return the id_spei_outgoing
	 */
	public java.lang.Long getId_spei_outgoing() {
		return id_spei_outgoing;
	}
	/**
	 * @param id_spei_outgoing the id_spei_outgoing to set
	 */
	public void setId_spei_outgoing(java.lang.Long id_spei_outgoing) {
		this.id_spei_outgoing = id_spei_outgoing;
	}
	/**
	 * @return the nombre_ordenante
	 */
	public String getNombre_ordenante() {
		return nombre_ordenante;
	}
	/**
	 * @param nombre_ordenante the nombre_ordenante to set
	 */
	public void setNombre_ordenante(String nombre_ordenante) {
		this.nombre_ordenante = nombre_ordenante;
	}
	/**
	 * @return the id_tipo_cuenta_ordenante
	 */
	public String getId_tipo_cuenta_ordenante() {
		return id_tipo_cuenta_ordenante;
	}
	/**
	 * @param id_tipo_cuenta_ordenante the id_tipo_cuenta_ordenante to set
	 */
	public void setId_tipo_cuenta_ordenante(String id_tipo_cuenta_ordenante) {
		this.id_tipo_cuenta_ordenante = id_tipo_cuenta_ordenante;
	}
	/**
	 * @return the cuenta_ordenante
	 */
	public String getCuenta_ordenante() {
		return cuenta_ordenante;
	}
	/**
	 * @param cuenta_ordenante the cuenta_ordenante to set
	 */
	public void setCuenta_ordenante(String cuenta_ordenante) {
		this.cuenta_ordenante = cuenta_ordenante;
	}
	/**
	 * @return the rfc_ordenante
	 */
	public String getRfc_ordenante() {
		return rfc_ordenante;
	}
	/**
	 * @param rfc_ordenante the rfc_ordenante to set
	 */
	public void setRfc_ordenante(String rfc_ordenante) {
		this.rfc_ordenante = rfc_ordenante;
	}
	/**
	 * @return the nombre_beneficiario
	 */
	public String getNombre_beneficiario() {
		return nombre_beneficiario;
	}
	/**
	 * @param nombre_beneficiario the nombre_beneficiario to set
	 */
	public void setNombre_beneficiario(String nombre_beneficiario) {
		this.nombre_beneficiario = nombre_beneficiario;
	}
	/**
	 * @return the id_tipo_cuenta_beneficiario
	 */
	public String getId_tipo_cuenta_beneficiario() {
		return id_tipo_cuenta_beneficiario;
	}
	/**
	 * @param id_tipo_cuenta_beneficiario the id_tipo_cuenta_beneficiario to set
	 */
	public void setId_tipo_cuenta_beneficiario(String id_tipo_cuenta_beneficiario) {
		this.id_tipo_cuenta_beneficiario = id_tipo_cuenta_beneficiario;
	}
	/**
	 * @return the cuenta_beneficiario
	 */
	public String getCuenta_beneficiario() {
		return cuenta_beneficiario;
	}
	/**
	 * @param cuenta_beneficiario the cuenta_beneficiario to set
	 */
	public void setCuenta_beneficiario(String cuenta_beneficiario) {
		this.cuenta_beneficiario = cuenta_beneficiario;
	}
	/**
	 * @return the rfc_beneficiario
	 */
	public String getRfc_beneficiario() {
		return rfc_beneficiario;
	}
	/**
	 * @param rfc_beneficiario the rfc_beneficiario to set
	 */
	public void setRfc_beneficiario(String rfc_beneficiario) {
		this.rfc_beneficiario = rfc_beneficiario;
	}
	/**
	 * @return the concepto_pago
	 */
	public String getConcepto_pago() {
		return concepto_pago;
	}
	/**
	 * @param concepto_pago the concepto_pago to set
	 */
	public void setConcepto_pago(String concepto_pago) {
		this.concepto_pago = concepto_pago;
	}
	/**
	 * @return the monto
	 */
	public String getMonto() {
		return monto;
	}
	/**
	 * @param monto the monto to set
	 */
	public void setMonto(String monto) {
		this.monto = monto;
	}
	/**
	 * @return the iva
	 */
	public String getIva() {
		return iva;
	}
	/**
	 * @param iva the iva to set
	 */
	public void setIva(String iva) {
		this.iva = iva;
	}
	/**
	 * @return the referencia_numerica
	 */
	public Integer getReferencia_numerica() {
		return referencia_numerica;
	}
	/**
	 * @param referencia_numerica the referencia_numerica to set
	 */
	public void setReferencia_numerica(Integer referencia_numerica) {
		this.referencia_numerica = referencia_numerica;
	}
	/**
	 * @param referencia the referencia to set
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	/**
	 * @return the referencia
	 */
	public String getReferencia() {
		return referencia;
	}
	/**
	 * @return the id_tipo_pago
	 */
	public String getId_tipo_pago() {
		return id_tipo_pago;
	}
	/**
	 * @param id_tipo_pago the id_tipo_pago to set
	 */
	public void setId_tipo_pago(String id_tipo_pago) {
		this.id_tipo_pago = id_tipo_pago;
	}
	/**
	 * @return the id_institucion_ben
	 */
	public Integer getId_institucion_ben() {
		return id_institucion_ben;
	}
	/**
	 * @param id_institucion_ben the id_institucion_ben to set
	 */
	public void setId_institucion_ben(Integer id_institucion_ben) {
		this.id_institucion_ben = id_institucion_ben;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the envio_automatico
	 */
	public String getEnvio_automatico() {
		return envio_automatico;
	}
	/**
	 * @param envio_automatico the envio_automatico to set
	 */
	public void setEnvio_automatico(String envio_automatico) {
		this.envio_automatico = envio_automatico;
	}
	/**
	 * @return the tipo_operacion
	 */
	public Integer getTipo_operacion() {
		return tipo_operacion;
	}
	/**
	 * @param tipo_operacion the tipo_operacion to set
	 */
	public void setTipo_operacion(Integer tipo_operacion) {
		this.tipo_operacion = tipo_operacion;
	}
	/**
	 * @return the status_operacion
	 */
	public Integer getStatus_operacion() {
		return status_operacion;
	}
	/**
	 * @param status_operacion the status_operacion to set
	 */
	public void setStatus_operacion(Integer status_operacion) {
		this.status_operacion = status_operacion;
	}
	/**
	 * @return the clave_rastreo
	 */
	public String getClave_rastreo() {
		return clave_rastreo;
	}
	/**
	 * @param clave_rastreo the clave_rastreo to set
	 */
	public void setClave_rastreo(String clave_rastreo) {
		this.clave_rastreo = clave_rastreo;
	}
	/**
	 * @return the id_devolucion
	 */
	public Integer getId_devolucion() {
		return id_devolucion;
	}
	/**
	 * @param id_devolucion the id_devolucion to set
	 */
	public void setId_devolucion(Integer id_devolucion) {
		this.id_devolucion = id_devolucion;
	}
	/**
	 * @return the fecha_captura
	 */
	public java.util.Date getFecha_captura() {
		return fecha_captura;
	}
	/**
	 * @param fecha_captura the fecha_captura to set
	 */
	public void setFecha_captura(java.util.Date fecha_captura) {
		this.fecha_captura = fecha_captura;
	}
	/**
	 * @return the fh_operacion
	 */
	public java.util.Date getFh_operacion() {
		return fh_operacion;
	}
	/**
	 * @param fh_operacion the fh_operacion to set
	 */
	public void setFh_operacion(java.util.Date fh_operacion) {
		this.fh_operacion = fh_operacion;
	}
	/**
	 * @return the id_operacion
	 */
	public Long getId_operacion() {
		return id_operacion;
	}
	/**
	 * @param id_operacion the id_operacion to set
	 */
	public void setId_operacion(Long id_operacion) {
		this.id_operacion = id_operacion;
	}
	/**
	 * @return the referencia_cobranza
	 */
	public String getReferencia_cobranza() {
		return referencia_cobranza;
	}
	/**
	 * @param referencia_cobranza the referencia_cobranza to set
	 */
	public void setReferencia_cobranza(String referencia_cobranza) {
		this.referencia_cobranza = referencia_cobranza;
	}
	/**
	 * @return the id_area_emite
	 */
	public Integer getId_area_emite() {
		return id_area_emite;
	}
	/**
	 * @param id_area_emite the id_area_emite to set
	 */
	public void setId_area_emite(Integer id_area_emite) {
		this.id_area_emite = id_area_emite;
	}
	/**
	 * @return the topologia
	 */
	public String getTopologia() {
		return topologia;
	}
	/**
	 * @param topologia the topologia to set
	 */
	public void setTopologia(String topologia) {
		this.topologia = topologia;
	}
	/**
	 * @return the verificado
	 */
	public Integer getVerificado() {
		return verificado;
	}
	/**
	 * @param verificado the verificado to set
	 */
	public void setVerificado(Integer verificado) {
		this.verificado = verificado;
	}
	/**
	 * @return the clave_pago
	 */
	public String getClave_pago() {
		return clave_pago;
	}
	/**
	 * @param clave_pago the clave_pago to set
	 */
	public void setClave_pago(String clave_pago) {
		this.clave_pago = clave_pago;
	}
	/**
	 * @return the prioridad
	 */
	public Integer getPrioridad() {
		return prioridad;
	}
	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}
	/**
	 * @return the inf_adicional
	 */
	public String getInf_adicional() {
		return inf_adicional;
	}
	/**
	 * @param inf_adicional the inf_adicional to set
	 */
	public void setInf_adicional(String inf_adicional) {
		this.inf_adicional = inf_adicional;
	}
	/**
	 * @return the app
	 */
	public String getApp() {
		return app;
	}
	/**
	 * @param app the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}
	/**
	 * @return the modificado_por
	 */
	public java.lang.Long getModificado_por() {
		return modificado_por;
	}
	/**
	 * @param modificado_por the modificado_por to set
	 */
	public void setModificado_por(java.lang.Long modificado_por) {
		this.modificado_por = modificado_por;
	}
	/**
	 * @return the fecha_modificacion
	 */
	public java.util.Date getFecha_modificacion() {
		return fecha_modificacion;
	}
	/**
	 * @param fecha_modificacion the fecha_modificacion to set
	 */
	public void setFecha_modificacion(java.util.Date fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	/**
	 * @return the usuario_id
	 */
	public Long getUsuario_id() {
		return usuario_id;
	}
	/**
	 * @param usuario_id the usuario_id to set
	 */
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	/**
	 * @return the sucursal_id
	 */
	public java.lang.Long getSucursal_id() {
		return sucursal_id;
	}
	/**
	 * @param sucursal_id the sucursal_id to set
	 */
	public void setSucursal_id(java.lang.Long sucursal_id) {
		this.sucursal_id = sucursal_id;
	}
	/**
	 * @return the empresa_id
	 */
	public java.lang.Long getEmpresa_id() {
		return empresa_id;
	}
	/**
	 * @param empresa_id the empresa_id to set
	 */
	public void setEmpresa_id(java.lang.Long empresa_id) {
		this.empresa_id = empresa_id;
	}
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @param correo_electronico the correo_electronico to set
	 */
	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}
	/**
	 * @return the correo_electronico
	 */
	public String getCorreo_electronico() {
		return correo_electronico;
	}
	/**
	 * @param correo_beneficiario the correo_beneficiario to set
	 */
	public void setCorreo_beneficiario(String correo_beneficiario) {
		this.correo_beneficiario = correo_beneficiario;
	}
	/**
	 * @return the correo_beneficiario
	 */
	public String getCorreo_beneficiario() {
		return correo_beneficiario;
	}
	/**
	 * @param procesado the procesado to set
	 */
	public void setProcesado(java.lang.Integer procesado) {
		this.procesado = procesado;
	}
	/**
	 * @return the procesado
	 */
	public java.lang.Integer getProcesado() {
		return procesado;
	}
	/**
	 * @param core_id the core_id to set
	 */
	public void setCore_id(java.lang.Long core_id) {
		this.core_id = core_id;
	}
	/**
	 * @return the core_id
	 */
	public java.lang.Long getCore_id() {
		return core_id;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param firma the firma to set
	 */
	public void setFirma(String firma) {
		this.firma = firma;
	}
	/**
	 * @return the firma
	 */
	public String getFirma() {
		return firma;
	}
	/**
	 * @param llave_aes the llave_aes to set
	 */
	public void setLlave_aes(String llave_aes) {
		this.llave_aes = llave_aes;
	}
	/**
	 * @return the llave_aes
	 */
	public String getLlave_aes() {
		return llave_aes;
	}
	/**
	 * @param cuenta_ordenante_cf the cuenta_ordenante_cf to set
	 */
	public void setCuenta_ordenante_cf(String cuenta_ordenante_cf) {
		this.cuenta_ordenante_cf = cuenta_ordenante_cf;
	}
	/**
	 * @return the cuenta_ordenante_cf
	 */
	public String getCuenta_ordenante_cf() {
		return cuenta_ordenante_cf;
	}
	/**
	 * @param nombre_ordenante_cf the nombre_ordenante_cf to set
	 */
	public void setNombre_ordenante_cf(String nombre_ordenante_cf) {
		this.nombre_ordenante_cf = nombre_ordenante_cf;
	}
	/**
	 * @return the nombre_ordenante_cf
	 */
	public String getNombre_ordenante_cf() {
		return nombre_ordenante_cf;
	}
	/**
	 * @param cuenta_beneficiario_cf the cuenta_beneficiario_cf to set
	 */
	public void setCuenta_beneficiario_cf(String cuenta_beneficiario_cf) {
		this.cuenta_beneficiario_cf = cuenta_beneficiario_cf;
	}
	/**
	 * @return the cuenta_beneficiario_cf
	 */
	public String getCuenta_beneficiario_cf() {
		return cuenta_beneficiario_cf;
	}
	/**
	 * @param nombre_beneficiario_cf the nombre_beneficiario_cf to set
	 */
	public void setNombre_beneficiario_cf(String nombre_beneficiario_cf) {
		this.nombre_beneficiario_cf = nombre_beneficiario_cf;
	}
	/**
	 * @return the nombre_beneficiario_cf
	 */
	public String getNombre_beneficiario_cf() {
		return nombre_beneficiario_cf;
	}
	/**
	 * @param monto_cf the monto_cf to set
	 */
	public void setMonto_cf(String monto_cf) {
		this.monto_cf = monto_cf;
	}
	/**
	 * @return the monto_cf
	 */
	public String getMonto_cf() {
		return monto_cf;
	}
	/**
	 * @param iva_cf the iva_cf to set
	 */
	public void setIva_cf(String iva_cf) {
		this.iva_cf = iva_cf;
	}
	/**
	 * @return the iva_cf
	 */
	public String getIva_cf() {
		return iva_cf;
	}
	/**
	 * @param concepto_pago_cf the concepto_pago_cf to set
	 */
	public void setConcepto_pago_cf(String concepto_pago_cf) {
		this.concepto_pago_cf = concepto_pago_cf;
	}
	/**
	 * @return the concepto_pago_cf
	 */
	public String getConcepto_pago_cf() {
		return concepto_pago_cf;
	}
	/**
	 * @return the movil_ordenante
	 */
	public String getMovil_ordenante() {
		return movil_ordenante;
	}
	/**
	 * @param movil_ordenante the movil_ordenante to set
	 */
	public void setMovil_ordenante(String movil_ordenante) {
		this.movil_ordenante = movil_ordenante;
	}
	/**
	 * @return the movil_ordenante_cfr
	 */
	public String getMovil_ordenante_cfr() {
		return movil_ordenante_cfr;
	}
	/**
	 * @param movil_ordenante_cfr the movil_ordenante_cfr to set
	 */
	public void setMovil_ordenante_cfr(String movil_ordenante_cfr) {
		this.movil_ordenante_cfr = movil_ordenante_cfr;
	}
	/**
	 * @return the movil_beneficiario
	 */
	public String getMovil_beneficiario() {
		return movil_beneficiario;
	}
	/**
	 * @param movil_beneficiario the movil_beneficiario to set
	 */
	public void setMovil_beneficiario(String movil_beneficiario) {
		this.movil_beneficiario = movil_beneficiario;
	}
	/**
	 * @return the movil_beneficiario_cfr
	 */
	public String getMovil_beneficiario_cfr() {
		return movil_beneficiario_cfr;
	}
	/**
	 * @param movil_beneficiario_cfr the movil_beneficiario_cfr to set
	 */
	public void setMovil_beneficiario_cfr(String movil_beneficiario_cfr) {
		this.movil_beneficiario_cfr = movil_beneficiario_cfr;
	}
	/**
	 * @return the num_folio_esquema_codi
	 */
	public String getNum_folio_esquema_codi() {
		return num_folio_esquema_codi;
	}
	/**
	 * @param num_folio_esquema_codi the num_folio_esquema_codi to set
	 */
	public void setNum_folio_esquema_codi(String num_folio_esquema_codi) {
		this.num_folio_esquema_codi = num_folio_esquema_codi;
	}
	/**
	 * @return the num_folio_esquema_codi_cfr
	 */
	public String getNum_folio_esquema_codi_cfr() {
		return num_folio_esquema_codi_cfr;
	}
	/**
	 * @param num_folio_esquema_codi_cfr the num_folio_esquema_codi_cfr to set
	 */
	public void setNum_folio_esquema_codi_cfr(String num_folio_esquema_codi_cfr) {
		this.num_folio_esquema_codi_cfr = num_folio_esquema_codi_cfr;
	}
	/**
	 * @return the pago_comision
	 */
	public String getPago_comision() {
		return pago_comision;
	}
	/**
	 * @param pago_comision the pago_comision to set
	 */
	public void setPago_comision(String pago_comision) {
		this.pago_comision = pago_comision;
	}
	/**
	 * @return the pago_comision_cfr
	 */
	public String getPago_comision_cfr() {
		return pago_comision_cfr;
	}
	/**
	 * @param pago_comision_cfr the pago_comision_cfr to set
	 */
	public void setPago_comision_cfr(String pago_comision_cfr) {
		this.pago_comision_cfr = pago_comision_cfr;
	}
	/**
	 * @return the monto_comision
	 */
	public String getMonto_comision() {
		return monto_comision;
	}
	/**
	 * @param monto_comision the monto_comision to set
	 */
	public void setMonto_comision(String monto_comision) {
		this.monto_comision = monto_comision;
	}
	/**
	 * @return the monto_comision_cfr
	 */
	public String getMonto_comision_cfr() {
		return monto_comision_cfr;
	}
	/**
	 * @param monto_comision_cfr the monto_comision_cfr to set
	 */
	public void setMonto_comision_cfr(String monto_comision_cfr) {
		this.monto_comision_cfr = monto_comision_cfr;
	}
	/**
	 * @return the fh_hr_limite_de_pago
	 */
	public String getFh_hr_limite_de_pago() {
		return fh_hr_limite_de_pago;
	}
	/**
	 * @param fh_hr_limite_de_pago the fh_hr_limite_de_pago to set
	 */
	public void setFh_hr_limite_de_pago(String fh_hr_limite_de_pago) {
		this.fh_hr_limite_de_pago = fh_hr_limite_de_pago;
	}
	/**
	 * @return the num_serie_cert_comercio
	 */
	public String getNum_serie_cert_comercio() {
		return num_serie_cert_comercio;
	}
	/**
	 * @param num_serie_cert_comercio the num_serie_cert_comercio to set
	 */
	public void setNum_serie_cert_comercio(String num_serie_cert_comercio) {
		this.num_serie_cert_comercio = num_serie_cert_comercio;
	}
	/**
	 * @return the num_serie_cert_comercio_cfr
	 */
	public String getNum_serie_cert_comercio_cfr() {
		return num_serie_cert_comercio_cfr;
	}
	/**
	 * @param num_serie_cert_comercio_cfr the num_serie_cert_comercio_cfr to set
	 */
	public void setNum_serie_cert_comercio_cfr(String num_serie_cert_comercio_cfr) {
		this.num_serie_cert_comercio_cfr = num_serie_cert_comercio_cfr;
	}
	/**
	 * @return the nombre_beneficiario2
	 */
	public String getNombre_beneficiario2() {
		return nombre_beneficiario2;
	}
	/**
	 * @param nombre_beneficiario2 the nombre_beneficiario2 to set
	 */
	public void setNombre_beneficiario2(String nombre_beneficiario2) {
		this.nombre_beneficiario2 = nombre_beneficiario2;
	}
	/**
	 * @return the nombre_beneficiario2_cfr
	 */
	public String getNombre_beneficiario2_cfr() {
		return nombre_beneficiario2_cfr;
	}
	/**
	 * @param nombre_beneficiario2_cfr the nombre_beneficiario2_cfr to set
	 */
	public void setNombre_beneficiario2_cfr(String nombre_beneficiario2_cfr) {
		this.nombre_beneficiario2_cfr = nombre_beneficiario2_cfr;
	}
	/**
	 * @return the id_tipo_cuenta_beneficiario2
	 */
	public String getId_tipo_cuenta_beneficiario2() {
		return id_tipo_cuenta_beneficiario2;
	}
	/**
	 * @param id_tipo_cuenta_beneficiario2 the id_tipo_cuenta_beneficiario2 to set
	 */
	public void setId_tipo_cuenta_beneficiario2(String id_tipo_cuenta_beneficiario2) {
		this.id_tipo_cuenta_beneficiario2 = id_tipo_cuenta_beneficiario2;
	}
	/**
	 * @return the cuenta_beneficiario2
	 */
	public String getCuenta_beneficiario2() {
		return cuenta_beneficiario2;
	}
	/**
	 * @param cuenta_beneficiario2 the cuenta_beneficiario2 to set
	 */
	public void setCuenta_beneficiario2(String cuenta_beneficiario2) {
		this.cuenta_beneficiario2 = cuenta_beneficiario2;
	}
	/**
	 * @return the cuenta_beneficiario2_cfr
	 */
	public String getCuenta_beneficiario2_cfr() {
		return cuenta_beneficiario2_cfr;
	}
	/**
	 * @param cuenta_beneficiario2_cfr the cuenta_beneficiario2_cfr to set
	 */
	public void setCuenta_beneficiario2_cfr(String cuenta_beneficiario2_cfr) {
		this.cuenta_beneficiario2_cfr = cuenta_beneficiario2_cfr;
	}
	/**
	 * @return the rfc_beneficiario2
	 */
	public String getRfc_beneficiario2() {
		return rfc_beneficiario2;
	}
	/**
	 * @param rfc_beneficiario2 the rfc_beneficiario2 to set
	 */
	public void setRfc_beneficiario2(String rfc_beneficiario2) {
		this.rfc_beneficiario2 = rfc_beneficiario2;
	}
	/**
	 * @return the rfc_beneficiario2_cfr
	 */
	public String getRfc_beneficiario2_cfr() {
		return rfc_beneficiario2_cfr;
	}
	/**
	 * @param rfc_beneficiario2_cfr the rfc_beneficiario2_cfr to set
	 */
	public void setRfc_beneficiario2_cfr(String rfc_beneficiario2_cfr) {
		this.rfc_beneficiario2_cfr = rfc_beneficiario2_cfr;
	}
	/**
	 * @return the dv_movil_ordenante
	 */
	public String getDv_movil_ordenante() {
		return dv_movil_ordenante;
	}
	/**
	 * @param dv_movil_ordenante the dv_movil_ordenante to set
	 */
	public void setDv_movil_ordenante(String dv_movil_ordenante) {
		this.dv_movil_ordenante = dv_movil_ordenante;
	}
	/**
	 * @return the dv_movil_ordenante_cfr
	 */
	public String getDv_movil_ordenante_cfr() {
		return dv_movil_ordenante_cfr;
	}
	/**
	 * @param dv_movil_ordenante_cfr the dv_movil_ordenante_cfr to set
	 */
	public void setDv_movil_ordenante_cfr(String dv_movil_ordenante_cfr) {
		this.dv_movil_ordenante_cfr = dv_movil_ordenante_cfr;
	}
	/**
	 * @return the dv_movil_beneficiario
	 */
	public String getDv_movil_beneficiario() {
		return dv_movil_beneficiario;
	}
	/**
	 * @param dv_movil_beneficiario the dv_movil_beneficiario to set
	 */
	public void setDv_movil_beneficiario(String dv_movil_beneficiario) {
		this.dv_movil_beneficiario = dv_movil_beneficiario;
	}
	/**
	 * @return the dv_movil_beneficiario_cfr
	 */
	public String getDv_movil_beneficiario_cfr() {
		return dv_movil_beneficiario_cfr;
	}
	/**
	 * @param dv_movil_beneficiario_cfr the dv_movil_beneficiario_cfr to set
	 */
	public void setDv_movil_beneficiario_cfr(String dv_movil_beneficiario_cfr) {
		this.dv_movil_beneficiario_cfr = dv_movil_beneficiario_cfr;
	}
	public void setNombre_institucion_ben(String nombre_institucion_ben) {
		this.nombre_institucion_ben = nombre_institucion_ben;
	}
	public String getNombre_institucion_ben() {
		return nombre_institucion_ben;
	}
		
}

/** !OutgoingJdbcDev.java */

	