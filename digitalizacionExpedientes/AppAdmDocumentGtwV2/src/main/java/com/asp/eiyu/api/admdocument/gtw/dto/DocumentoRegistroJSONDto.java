/**
 * 
 */
package com.asp.eiyu.api.admdocument.gtw.dto;

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
public record DocumentoRegistroJSONDto(Integer id, String rutaNotificacion, String codSistema, String ruta_storage,
		String tipoDocumento, String codigo, String usuarioCreacion, String archivo, String nombreArchivo,
		String extensionArchivo) {
}
