package com.asp.eiyu.api.admdocument.service;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.dto.SolicitudDocumento;

/**
 * Banco ASP
 * Project: eiyu
 * Class: IDocumentosInfoServices.java
 *
 * Description:
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Feb 29, 2024
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Feb 29, 2024 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public interface IDocumentosInfoServices {
	
	SolicitudDocumento obtenerListaDocumentos(String clabe, DataSource pool) throws SQLException;

}
