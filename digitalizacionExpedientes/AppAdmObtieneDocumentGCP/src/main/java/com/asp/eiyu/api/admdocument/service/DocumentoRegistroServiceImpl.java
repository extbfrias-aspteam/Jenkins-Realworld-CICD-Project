package com.asp.eiyu.api.admdocument.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.core.io.ByteArrayResource;

import com.asp.eiyu.api.admdocument.dto.DocumentoDescargaDto;
import com.asp.eiyu.api.admdocument.entity.DocumentoRegistroEntity;
import com.asp.eiyu.api.admdocument.repository.DocumentoRegistroRepository;
import com.google.cloud.storage.Storage;

/**
 * Banco ASP Project: eiyu Class: DocumentoRegistroServiceImpl.java
 *
 * Description: Clase el cual se encarga de la logia de negocio para realizar el
 * insert en la tabla entre otras cosas enviar al google cloud los archivos
 *
 * @author Herwin TR
 * @company ICORPTTI
 * @created Sep 3, 2023
 * @since JDK17
 *
 * @version Control de cambios:
 * @version 1.0 Sep 3, 2023 Herwin: Creacion de la clase
 *
 * @category Service
 *
 */
public class DocumentoRegistroServiceImpl implements IDocumentoRegistroService {

	/**
	 * documentoRegistroRepository - variable de tipo DocumentoRegistroRepository
	 * para usarlo como
	 */
	DocumentoRegistroRepository documentoRegistroRepository = new DocumentoRegistroRepository();

	/**
	 * fileService - variable de tipo IFileService para usarlo como
	 */
	IFileService fileService = new FileServiceImpl();

	/**
	 * Metodo el cual se encarga del envio - registro del documento en la base de
	 * datos
	 */
	@Override
	public DocumentoDescargaDto descargaDocumentoRegistro(DocumentoDescargaDto documentoDescarga, DataSource pool,
			Storage storage) {

		try {
			String bucketName = System.getenv("BUCKET_NAME");
			String existeArchivoBuck = fileService.documentoExiste(bucketName, storage,
					documentoDescarga.getCodigo() + "/" + documentoDescarga.getDatoContenido(),
					documentoDescarga.getCodigo());
			System.out.println(bucketName + " : " + bucketName.toString());

			List<DocumentoRegistroEntity> documento = this.documentoRegistroRepository
					.consultaExistencia(documentoDescarga.getDatoContenido(), documentoDescarga.getCodigo(), pool);

			if (existeArchivoBuck == null && documento != null && !documento.isEmpty() && documento.get(0) != null
					&& documento.get(0).getRuta_storage() != null
					&& !documento.get(0).getRuta_storage().trim().isEmpty()) {
				String eliminado = this.documentoRegistroRepository.eliminarScarchivodigital(documento.get(0).getId(),
						pool);
				documento = null;
				System.out.println(eliminado);
			}

			if (existeArchivoBuck != null && documento != null && !documento.isEmpty() && documento.get(0) != null
					&& documento.get(0).getRuta_storage() != null
					&& !documento.get(0).getRuta_storage().trim().isEmpty()) {
				documentoDescarga.setDocumentoBase64(this.obtenerDocumento(existeArchivoBuck, storage));
			} else {
				documentoDescarga.setDocumentoBase64("BAD");
			}
		} catch (SQLException e) {
			System.out.println(
					"Ocurrio un error al momento de consulta los registros enla base de dato::: " + e.getMessage());
			e.printStackTrace();
		}

		return documentoDescarga;
	}

	private String obtenerDocumento(String fileName, Storage storage) {
		String bucketName = System.getenv("BUCKET_NAME");
		try {
			return this.encodeFileToBase64Binary(this.fileService.downloadFile(fileName, storage, bucketName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	private String encodeFileToBase64Binary(ByteArrayResource documento) throws IOException {
		try {
			return Base64.getEncoder().encodeToString(documento.getByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
