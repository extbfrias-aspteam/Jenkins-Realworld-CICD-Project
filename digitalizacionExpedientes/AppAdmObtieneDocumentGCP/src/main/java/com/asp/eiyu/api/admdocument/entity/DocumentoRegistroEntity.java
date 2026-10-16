/**
 * 
 */
package com.asp.eiyu.api.admdocument.entity;


import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Banco ASP
 * Project: eiyu
 * Class: DocumentoRegistroEntity.java
 *
 * Description:Mapeo de la tabla Documento registro en el cual contiene la informacion
 * recibida de los documentos
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category Entity
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DocumentoRegistroEntity  implements Serializable{

	private static final long serialVersionUID = 1014578557961295355L;

	private int id;
	private String codsistema;
	private String persona_id;	
	private String codigo;
	private String ruta_storage;
	private int estado;
	private Date fechahoracarga;
	private String tipodocumento;
	private String rutanotificacion;
	private Date fecha_creacion;
	private Date fecha_modificacion;
	private String usuario_creacion;
	private String usuario_modificacion;
	
	
	@Override
	public String toString() {
		return "DocumentoRegistroEntity [id=" + id  + ", persona_id=" + persona_id
				+ ", codsistema=" + codsistema + ", ruta_storage=" + ruta_storage + ", tipodocumento=" + tipodocumento
				+ ", rutanotificacion=" + rutanotificacion + ", codigo=" + codigo + ", estado=" + estado
				+ ", fechahoracarga=" + fechahoracarga + ", fecha_creacion="
				+ fecha_creacion + ", fecha_modificacion=" + fecha_modificacion + ", usuario_creacion="
				+ usuario_creacion + ", usuario_modificacion=" + usuario_modificacion + "]";
	}


	/**
	 * @param id
	 * @param codsistema
	 * @param persona_id
	 * @param codigo
	 * @param ruta_storage
	 * @param estado
	 * @param fechahoracarga
	 * @param tipodocumento
	 * @param rutanotificacion
	 * @param fecha_creacion
	 * @param fecha_modificacion
	 * @param usuario_creacion
	 * @param usuario_modificacion
	 */
	public DocumentoRegistroEntity(int id, String codsistema, String persona_id, String codigo, String ruta_storage,
			int estado, Date fechahoracarga, String tipodocumento, String rutanotificacion, Date fecha_creacion,
			Date fecha_modificacion, String usuario_creacion, String usuario_modificacion) {
		super();
		this.id = id;
		this.codsistema = codsistema;
		this.persona_id = persona_id;
		this.codigo = codigo;
		this.ruta_storage = ruta_storage;
		this.estado = estado;
		this.fechahoracarga = fechahoracarga;
		this.tipodocumento = tipodocumento;
		this.rutanotificacion = rutanotificacion;
		this.fecha_creacion = fecha_creacion;
		this.fecha_modificacion = fecha_modificacion;
		this.usuario_creacion = usuario_creacion;
		this.usuario_modificacion = usuario_modificacion;
	}
}