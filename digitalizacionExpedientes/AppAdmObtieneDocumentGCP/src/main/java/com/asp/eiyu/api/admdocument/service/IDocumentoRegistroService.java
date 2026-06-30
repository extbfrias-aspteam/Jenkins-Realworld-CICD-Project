package com.asp.eiyu.api.admdocument.service;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.dto.DocumentoDescargaDto;
import com.google.cloud.storage.Storage;

/**
 * Banco ASP
 * Project: eiyu
 * Class: IDocumentoRegistroService.java
 *
 * Description: Interfaz donde se crearan los metodos de negocio el cual guardara informacion en la tabla documentos_registro
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category 
 *
 */
public interface IDocumentoRegistroService {


	/**
	 * Metodo el cual se encarga del envio - registro del documento en la base de datos
	 * @param documentoDescarga - objeto el cual contiene la informacion necesaria para agregar a la base de datos el registro
	 * @return retorna un ok - en caso de ser exitoso, y un ko - en caso contrario
	 */
	DocumentoDescargaDto descargaDocumentoRegistro(DocumentoDescargaDto documentoDescarga, DataSource pool, Storage storage);
	
}
