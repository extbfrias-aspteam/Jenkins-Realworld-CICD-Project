package com.asp.eiyu.api.admdocument.gtw.dto;

/**
 * Banco ASP
 * Project: eiyu
 * Class: ParticipantesIndirectosRespDTO.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created 31 ago. 2024
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 31 ago. 2024 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public record DocumentoRespDTO(String eiyuResponseCode, String descripcion, String content){
	public DocumentoRespDTO actualizaContent(String content) {
		return new DocumentoRespDTO(eiyuResponseCode(), descripcion(), content);
	}
	
	public DocumentoRespDTO actualizaEstatusError() {
		return new DocumentoRespDTO("Error", "Error", null);
	}
}
