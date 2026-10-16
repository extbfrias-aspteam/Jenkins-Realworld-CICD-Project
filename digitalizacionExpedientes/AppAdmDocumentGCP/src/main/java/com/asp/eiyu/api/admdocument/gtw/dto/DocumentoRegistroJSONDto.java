/**
 * 
 */
package com.asp.eiyu.api.admdocument.gtw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroDto.java
 *
 * Description: Mapeo del jeson recibido como en el dtometro en el dto almacenar
 * la informacion en la tabla Documento Registro
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category DTO
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoRegistroJSONDto {

	/**
	 * id - Variable de tipo Integer que se ocupa en el dto
	 */
	private Integer id;

	/**
	 * endpoint - Variable de tipo String que se ocupa en el dto
	 */
	private String rutaNotificacion;

	/**
	 * sistema - Variable de tipo String que se ocupa en el dto
	 */
	private String codSistema;

	/**
	 * uid - Variable de tipo String que se ocupa en el dto
	 */
	private String ruta_storage;

	/**
	 * tipoDocumento - Variable de tipo String que se ocupa en el dto
	 */
	private String tipoDocumento;
	
	/**
	 * codigo - Variable de tipo String que se ocupa en el dto
	 */
	private String codigo;
	
	/**
	 * usuarioCreacion - Variable de tipo String que se ocupa en el dto
	 */
	private String usuarioCreacion;

	/**
	 * file - Variable de tipo MultipartFile que se ocupa para recibir el archivo
	 * pdf, excel, jpeg
	 */
	private String archivo;
	
	private String nombreArchivo;
	
	private String extensionArchivo;

	@Override
	public String toString() {
		return "DocumentoRegistroJSONDto [id=" + id + ", rutaNotificacion=" + rutaNotificacion + ", codSistema="
				+ codSistema + ", ruta_storage=" + ruta_storage + ", tipoDocumento=" + tipoDocumento + ", codigo="
				+ codigo + ", usuarioCreacion=" + usuarioCreacion + ", archivo=" + archivo + ", nombreArchivo="
				+ nombreArchivo + ", extensionArchivo=" + extensionArchivo + "]";
	}

	

}
