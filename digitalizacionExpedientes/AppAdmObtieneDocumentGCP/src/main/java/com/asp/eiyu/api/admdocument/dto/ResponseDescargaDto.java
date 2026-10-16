package com.asp.eiyu.api.admdocument.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Banco ASP
 * Project: eiyu
 * Class: ResponseDescargaDto.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Dec 22, 2024
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Dec 22, 2024 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDescargaDto {

	String estatusCodigo;
	String descripcion;
	DocumentoDescargaDto respuesta;
	
}
