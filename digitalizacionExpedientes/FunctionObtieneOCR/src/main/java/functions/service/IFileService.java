package functions.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

/**
 * Banco ASP Project: eiyu Class: FileService.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Sep 8, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Sep 8, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public interface IFileService {
	
	List<String> listOfFiles(Storage storage, String bucketName);

	ByteArrayResource downloadFile(String fileName, Storage storage, String bucketName);

	boolean deleteFile(String fileName, Storage storage, String bucketName);

	Blob uploadFile(File file, Storage storage, String bucketName, String filePath) throws IOException;
}
