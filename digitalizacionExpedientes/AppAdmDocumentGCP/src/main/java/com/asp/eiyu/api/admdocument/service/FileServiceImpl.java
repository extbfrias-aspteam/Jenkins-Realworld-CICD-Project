package com.asp.eiyu.api.admdocument.service;

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
		Blob blob = storage.get(bucketName, fileName);
		ByteArrayResource resource = new ByteArrayResource(blob.getContent());

		return resource;
	}

	@Override
	public boolean deleteFile(String fileName, Storage storage, String bucketName) {
	    System.out.println("deleteFile fileName -> " + fileName);
	    System.out.println("deleteFile bucketName -> " + bucketName);
	    
	    Blob blob = storage.get(bucketName, fileName);

	    if (blob == null) {
	        System.out.println("No se encontró el archivo: " + fileName);
	        return false;
	    }

	    return blob.delete();
	}

	@Override
	public String documentoExiste(String bucketName, Storage storage, String nombreBase,String clienteId) {
		System.out.println("nombreBase->>>>>>>>>>>" + nombreBase);
		List<String> list =this.archivosDeCliente(bucketName, storage, clienteId);
		for (String nombre : list) {
			String nombreArchivo = nombre;
			System.out.println("nombreArchivo->>>>>>>>>>>" + nombreArchivo);
			if (nombreArchivo.equalsIgnoreCase(nombreBase)) {
				return nombreArchivo;
			}
			if (nombreArchivo.startsWith(nombreBase + ".")) {
				return nombreArchivo; // Se encontró al menos un archivo con ese nombre base y alguna extensión
			}
		}
		return null;
	}
	public List<String> archivosDeCliente(String bucketName, Storage storage, String clienteId) {
	    List<String> archivosCliente = new ArrayList<>();
	    Page<Blob> blobs = storage.list(bucketName);

	    for (Blob blob : blobs.iterateAll()) {
	        String nombreArchivo = blob.getName();
	        System.out.println("nombreArchivo  de archivosDeCliente->>>>>>>>>>>" + nombreArchivo);
	        if (nombreArchivo.startsWith(clienteId + "/")) {
	            archivosCliente.add(nombreArchivo);
	        }
	    }

	    return archivosCliente;
	}

	@Override
	public Blob uploadFile(File file, Storage storage, String bucketName, String filePath) throws IOException {
		System.out.println("file.getName()>>> " + filePath);
		BlobId blobId = BlobId.of(bucketName, filePath);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		System.out.println("blobInfo>>> " + blobInfo);
		System.out.println("path>> " + file.getPath());
		Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()));

		System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + file.getName());
		return blob;
	}

}
