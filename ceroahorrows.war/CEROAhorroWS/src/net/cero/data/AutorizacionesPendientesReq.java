package net.cero.data;

import java.io.Serializable;
import java.util.Date;

import net.cero.seguridad.utilidades.HeaderWS;

import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AutorizacionesPendientesReq implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long Id;
	private HeaderWS headerWS;
	private String operacion;
	private String clave_tipo_opera;
	private String clave_canal;
	private String clave_modulo;
	private String clave_aplicacion;	
	private String sucursal;
	private int id_tipo_operacion; 
	private String id_persona;
	private String persona;
	private int id_canal;
	private String canal;
	private String desc_canal;
	private int id_auestatus;
	private String modulo;
	private int producto_id;
	private String desc_producto;
	private int id_aplicacion;
	private String aplicacion;	
	private String observacion;
	private String parametros;	
	private String motivo_rechazo;
	private Date fecha_creacion;
	private int usuario_creacion;
	private String nom_usu_crea;
	private Date fecha_modificacion;
	private int usuario_modificacion;
	private String nom_usu_mod;
	
	private String[] aoperacion;
	private String[] adesc_operacion;
	private String[] ausuario;
	private String[] acanal;
	private String[] apersona;
	private Date fecha_inicio;
	private Date fecha_final;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}
	/**
	 * @return the headerWS
	 */
	public HeaderWS getHeaderWS() {
		return headerWS;
	}
	/**
	 * @param headerWS the headerWS to set
	 */
	public void setHeaderWS(HeaderWS headerWS) {
		this.headerWS = headerWS;
	}
	/**
	 * @return the operacion
	 */
	public String getOperacion() {
		return operacion;
	}
	/**
	 * @param operacion the operacion to set
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	/**
	 * @return the clave_tipo_opera
	 */
	public String getClave_tipo_opera() {
		return clave_tipo_opera;
	}
	/**
	 * @param clave_tipo_opera the clave_tipo_opera to set
	 */
	public void setClave_tipo_opera(String clave_tipo_opera) {
		this.clave_tipo_opera = clave_tipo_opera;
	}
	/**
	 * @return the clave_canal
	 */
	public String getClave_canal() {
		return clave_canal;
	}
	/**
	 * @param clave_canal the clave_canal to set
	 */
	public void setClave_canal(String clave_canal) {
		this.clave_canal = clave_canal;
	}
	/**
	 * @return the clave_modulo
	 */
	public String getClave_modulo() {
		return clave_modulo;
	}
	/**
	 * @param clave_modulo the clave_modulo to set
	 */
	public void setClave_modulo(String clave_modulo) {
		this.clave_modulo = clave_modulo;
	}
	/**
	 * @return the clave_aplicacion
	 */
	public String getClave_aplicacion() {
		return clave_aplicacion;
	}
	/**
	 * @param clave_aplicacion the clave_aplicacion to set
	 */
	public void setClave_aplicacion(String clave_aplicacion) {
		this.clave_aplicacion = clave_aplicacion;
	}
	/**
	 * @return the sucursal
	 */
	public String getSucursal() {
		return sucursal;
	}
	/**
	 * @param sucursal the sucursal to set
	 */
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	/**
	 * @return the id_tipo_operacion
	 */
	public int getId_tipo_operacion() {
		return id_tipo_operacion;
	}
	/**
	 * @param id_tipo_operacion the id_tipo_operacion to set
	 */
	public void setId_tipo_operacion(int id_tipo_operacion) {
		this.id_tipo_operacion = id_tipo_operacion;
	}
	/**
	 * @return the id_persona
	 */
	public String getId_persona() {
		return id_persona;
	}
	/**
	 * @param id_persona the id_persona to set
	 */
	public void setId_persona(String id_persona) {
		this.id_persona = id_persona;
	}
	/**
	 * @return the persona
	 */
	public String getPersona() {
		return persona;
	}
	/**
	 * @param persona the persona to set
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}
	/**
	 * @return the id_canal
	 */
	public int getId_canal() {
		return id_canal;
	}
	/**
	 * @param id_canal the id_canal to set
	 */
	public void setId_canal(int id_canal) {
		this.id_canal = id_canal;
	}
	/**
	 * @return the canal
	 */
	public String getCanal() {
		return canal;
	}
	/**
	 * @param canal the canal to set
	 */
	public void setCanal(String canal) {
		this.canal = canal;
	}
	/**
	 * @return the desc_canal
	 */
	public String getDesc_canal() {
		return desc_canal;
	}
	/**
	 * @param desc_canal the desc_canal to set
	 */
	public void setDesc_canal(String desc_canal) {
		this.desc_canal = desc_canal;
	}
	/**
	 * @return the id_auestatus
	 */
	public int getId_auestatus() {
		return id_auestatus;
	}
	/**
	 * @param id_auestatus the id_auestatus to set
	 */
	public void setId_auestatus(int id_auestatus) {
		this.id_auestatus = id_auestatus;
	}
	/**
	 * @return the modulo
	 */
	public String getModulo() {
		return modulo;
	}
	/**
	 * @param modulo the modulo to set
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	/**
	 * @return the producto_id
	 */
	public int getProducto_id() {
		return producto_id;
	}
	/**
	 * @param producto_id the producto_id to set
	 */
	public void setProducto_id(int producto_id) {
		this.producto_id = producto_id;
	}
	/**
	 * @return the desc_producto
	 */
	public String getDesc_producto() {
		return desc_producto;
	}
	/**
	 * @param desc_producto the desc_producto to set
	 */
	public void setDesc_producto(String desc_producto) {
		this.desc_producto = desc_producto;
	}
	/**
	 * @return the id_aplicacion
	 */
	public int getId_aplicacion() {
		return id_aplicacion;
	}
	/**
	 * @param id_aplicacion the id_aplicacion to set
	 */
	public void setId_aplicacion(int id_aplicacion) {
		this.id_aplicacion = id_aplicacion;
	}
	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}
	/**
	 * @param aplicacion the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}
	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	/**
	 * @return the parametros
	 */
	public String getParametros() {
		return parametros;
	}
	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(String parametros) {
		this.parametros = parametros;
	}
	/**
	 * @return the motivo_rechazo
	 */
	public String getMotivo_rechazo() {
		return motivo_rechazo;
	}
	/**
	 * @param motivo_rechazo the motivo_rechazo to set
	 */
	public void setMotivo_rechazo(String motivo_rechazo) {
		this.motivo_rechazo = motivo_rechazo;
	}
	/**
	 * @return the fecha_creacion
	 */
	public Date getFecha_creacion() {
		return fecha_creacion;
	}
	/**
	 * @param fecha_creacion the fecha_creacion to set
	 */
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	/**
	 * @return the usuario_creacion
	 */
	public int getUsuario_creacion() {
		return usuario_creacion;
	}
	/**
	 * @param usuario_creacion the usuario_creacion to set
	 */
	public void setUsuario_creacion(int usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}
	/**
	 * @return the nom_usu_crea
	 */
	public String getNom_usu_crea() {
		return nom_usu_crea;
	}
	/**
	 * @param nom_usu_crea the nom_usu_crea to set
	 */
	public void setNom_usu_crea(String nom_usu_crea) {
		this.nom_usu_crea = nom_usu_crea;
	}
	/**
	 * @return the fecha_modificacion
	 */
	public Date getFecha_modificacion() {
		return fecha_modificacion;
	}
	/**
	 * @param fecha_modificacion the fecha_modificacion to set
	 */
	public void setFecha_modificacion(Date fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	/**
	 * @return the usuario_modificacion
	 */
	public int getUsuario_modificacion() {
		return usuario_modificacion;
	}
	/**
	 * @param usuario_modificacion the usuario_modificacion to set
	 */
	public void setUsuario_modificacion(int usuario_modificacion) {
		this.usuario_modificacion = usuario_modificacion;
	}
	/**
	 * @return the nom_usu_mod
	 */
	public String getNom_usu_mod() {
		return nom_usu_mod;
	}
	/**
	 * @param nom_usu_mod the nom_usu_mod to set
	 */
	public void setNom_usu_mod(String nom_usu_mod) {
		this.nom_usu_mod = nom_usu_mod;
	}
	/**
	 * @return the aoperacion
	 */
	public String[] getAoperacion() {
		return aoperacion;
	}
	/**
	 * @param aoperacion the aoperacion to set
	 */
	public void setAoperacion(String[] aoperacion) {
		this.aoperacion = aoperacion;
	}
	/**
	 * @return the adesc_operacion
	 */
	public String[] getAdesc_operacion() {
		return adesc_operacion;
	}
	/**
	 * @param adesc_operacion the adesc_operacion to set
	 */
	public void setAdesc_operacion(String[] adesc_operacion) {
		this.adesc_operacion = adesc_operacion;
	}
	/**
	 * @return the ausuario
	 */
	public String[] getAusuario() {
		return ausuario;
	}
	/**
	 * @param ausuario the ausuario to set
	 */
	public void setAusuario(String[] ausuario) {
		this.ausuario = ausuario;
	}
	/**
	 * @return the acanal
	 */
	public String[] getAcanal() {
		return acanal;
	}
	/**
	 * @param acanal the acanal to set
	 */
	public void setAcanal(String[] acanal) {
		this.acanal = acanal;
	}
	/**
	 * @return the apersona
	 */
	public String[] getApersona() {
		return apersona;
	}
	/**
	 * @param apersona the apersona to set
	 */
	public void setApersona(String[] apersona) {
		this.apersona = apersona;
	}
	/**
	 * @return the fecha_inicio
	 */
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	/**
	 * @param fecha_inicio the fecha_inicio to set
	 */
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	/**
	 * @return the fecha_final
	 */
	public Date getFecha_final() {
		return fecha_final;
	}
	/**
	 * @param fecha_final the fecha_final to set
	 */
	public void setFecha_final(Date fecha_final) {
		this.fecha_final = fecha_final;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}