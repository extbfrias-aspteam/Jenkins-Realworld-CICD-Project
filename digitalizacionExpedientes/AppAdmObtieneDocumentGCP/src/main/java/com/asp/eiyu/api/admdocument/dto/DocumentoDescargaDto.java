package com.asp.eiyu.api.admdocument.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Banco ASP
 * Project: eiyu
 * Class: DocumentoRegistroDto.java
 *
 * Description: Mapeo del jeson recibido como en el dtometro en el dto almacenar la informacion en la tabla Documento Registro
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoDescargaDto {

	/**
	 * codigo - Variable de tipo String que se ocupa en el dto 
	 */
	private String codigo;

	/**
	 * datocontenido - Variable de tipo String que se ocupa en el dto
	 */
	private String datoContenido;
	
	
	private String documentoBase64;

}
