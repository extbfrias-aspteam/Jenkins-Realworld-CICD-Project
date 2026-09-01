package net.cero.data;
import java.util.Date;

public class AhorroAlfrescoOBJ {
	
	private String cuenta;
	private int documentos_ahorro_id;
	private String ruta_alfresco;
	private String id_archivo_alfresco;
	private String observaciones;
	private String nombre;
	private Date fecha_expedicion;
	private Date fecha_vigencia;
	
	
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public int getDocumentos_ahorro_id() {
		return documentos_ahorro_id;
	}
	public void setDocumentos_ahorro_id(int documentos_ahorro_id) {
		this.documentos_ahorro_id = documentos_ahorro_id;
	}
	public String getRuta_alfresco() {
		return ruta_alfresco;
	}
	public void setRuta_alfresco(String ruta_alfresco) {
		this.ruta_alfresco = ruta_alfresco;
	}
	public String getId_archivo_alfresco() {
		return id_archivo_alfresco;
	}
	public void setId_archivo_alfresco(String id_archivo_alfresco) {
		this.id_archivo_alfresco = id_archivo_alfresco;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha_expedicion() {
		return fecha_expedicion;
	}
	public void setFecha_expedicion(Date fecha_expedicion) {
		this.fecha_expedicion = fecha_expedicion;
	}
	public Date getFecha_vigencia() {
		return fecha_vigencia;
	}
	public void setFecha_vigencia(Date fecha_vigencia) {
		this.fecha_vigencia = fecha_vigencia;
	}

	
}
