package functions.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
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
		List<String> list = new ArrayList<>();
		
		Page<Blob> blobs = storage.list(bucketName);
		for (Blob blob : blobs.iterateAll()) {
			list.add(blob.getName());
		}
		return list;
	}

	@Override
	public ByteArrayResource downloadFile(String fileName, Storage storage, String bucketName) {
		Blob blob = storage.get(BlobId.of(bucketName, fileName));
		ByteArrayResource resource = new ByteArrayResource(blob.getContent());

		return resource;
	}

	@Override
	public boolean deleteFile(String fileName, Storage storage, String bucketName) {
		Blob blob = storage.get(bucketName, fileName);

		return blob.delete();
	}

	@Override
	public Blob uploadFile(File file, Storage storage, String bucketName, String filePath) throws IOException {
//		BlobId blobId = BlobId.of(bucketName, file.getName());
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file)).build();
//		Blob blob = storage.create(blobInfo, file.getBytes());
//		return blob;
//	}
		System.out.println("file.getName()>>> " + file.getName());
//		 Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		    BlobId blobId = BlobId.of(bucketName, file.getName());
		    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

//		    // Optional: set a generation-match precondition to avoid potential race
//		    // conditions and data corruptions. The request returns a 412 error if the
//		    // preconditions are not met.
//		    Storage.BlobWriteOption precondition;
//		    if (storage.get(bucketName, file.getName()) == null) {
//		      // For a target object that does not yet exist, set the DoesNotExist precondition.
//		      // This will cause the request to fail if the object is created before the request runs.
//		      precondition = Storage.BlobWriteOption.doesNotExist();
//		    } else {
//		      // If the destination already exists in your bucket, instead set a generation-match
//		      // precondition. This will cause the request to fail if the existing object's generation
//		      // changes before the request runs.
//		      precondition =
//		          Storage.BlobWriteOption.generationMatch(
//		              storage.get(bucketName, file.getName()).getGeneration());
//		    }
		    System.out.println("blobInfo>>> " + blobInfo);
		    System.out.println("path>> " + file.getPath());
		    Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()));

		    System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + file.getName());
		    return blob;
		  }

}
