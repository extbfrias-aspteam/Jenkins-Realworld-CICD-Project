package functions.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

/**
 * Banco ASP Project: eiyu Class: FileServiceImpl.java
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
public class FileServiceImpl implements IFileService {


	@Override
	public List<String> listOfFiles(Storage storage, String bucketName) {
		return null;
	}

	@Override
	public ByteArrayResource downloadFile(String fileName, Storage storage, String bucketName) {
		Blob blob = storage.get(BlobId.of(bucketName, fileName));
		ByteArrayResource resource = new ByteArrayResource(blob.getContent());

		return resource;
	}

	@Override
	public boolean deleteFile(String fileName, Storage storage, String bucketName) {
		return false;
	}

	@Override
	public Blob uploadFile(File file, Storage storage, String bucketName, String filePath) throws IOException {
		return null;
	}
}
