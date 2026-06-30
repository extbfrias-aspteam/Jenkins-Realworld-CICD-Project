package com.asp.eiyu.api.admdocument.gtw.dto;

/**
 * Banco ASP
 * Project: eiyu
 * Class: SolicitudDocumento.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Nov 30, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Nov 30, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public record SolicitudDocumento(String tipo, DocumentoRegistroJSONDto documentoRegistro, DocumentoDescargaDto documentoDescarga,DocumentoRegistro _documentoRegistro ,ConsultaEstatusCuentaDocs documentoConsulta) {
}