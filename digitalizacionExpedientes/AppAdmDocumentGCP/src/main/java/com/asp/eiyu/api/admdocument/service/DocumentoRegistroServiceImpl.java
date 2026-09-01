
package com.asp.eiyu.api.admdocument.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;

import com.asp.eiyu.api.admdocument.entity.DocumentoRegistroEntity;
import com.asp.eiyu.api.admdocument.entity.DocumentoRegistroResponse;
import com.asp.eiyu.api.admdocument.entity.DocumentoRespDTO;
import com.asp.eiyu.api.admdocument.entity.ResponseDTO;
import com.asp.eiyu.api.admdocument.entity.SCbitacora;
import com.asp.eiyu.api.admdocument.gtw.dto.DocumentoRegistroJSONDto;
import com.asp.eiyu.api.admdocument.repository.DocumentoRegistroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

public class DocumentoRegistroServiceImpl implements IDocumentoRegistroService {
    private static final int LONGITUD_MAX_NOMBRE_ARCHIVO = 120;

    DocumentoRegistroRepository documentoRegistroRepository = new DocumentoRegistroRepository();
    IFileService fileService = new FileServiceImpl();
    private ObjectMapper objetMapper = new ObjectMapper();

    @Override
    public String cargaDocumentoRegistro(DocumentoRegistroJSONDto documentoRegistro, DataSource pool, Storage storage) {
        documentoRegistro = this.limpiarNulos(documentoRegistro);
        System.out.println("Consultando existencia>> " + documentoRegistro.getTipoDocumento() + ", " + documentoRegistro.getCodigo());
        String errorNombreArchivo = validarNombreArchivoLibre(documentoRegistro);
        if (errorNombreArchivo != null) {
            DocumentoRespDTO respuestaNombreArchivo = new DocumentoRespDTO("ERROR",
                    "Error al cargar documento, " + errorNombreArchivo, null);
            try {
                return objetMapper.writeValueAsString(respuestaNombreArchivo);
            } catch (JsonProcessingException e) {
                return "{\"eiyuResponseCode\":\"ERROR\",\"descripcion\":\"Error al cargar documento, " + errorNombreArchivo + "\"}";
            }
        }
        String bucketName = System.getenv("BUCKET_NAME");
        ResponseDTO<DocumentoRegistroResponse> rdt = new ResponseDTO<>();
        DocumentoRegistroResponse dr = new DocumentoRegistroResponse();
        DocumentoRespDTO respuestaDto = new DocumentoRespDTO("OK", "Carga documento exitoso", null);
        DocumentoRegistroEntity docRegistro = new DocumentoRegistroEntity();
        docRegistro.setCodigo(documentoRegistro.getCodigo().trim());
        docRegistro.setEstado(1);
        docRegistro.setCodSistema(documentoRegistro.getCodSistema().trim());
        docRegistro.setTipoDocumento(documentoRegistro.getTipoDocumento().trim());
        docRegistro.setUsuario_creacion(documentoRegistro.getUsuarioCreacion().trim());
        docRegistro.setRutaNotificacion(documentoRegistro.getRutaNotificacion().trim());

        String rutaGuardado = documentoRegistro.getNombreArchivo();
        String respuesta = "";
        try {
            String nombreArchivoCatalogo = documentoRegistroRepository
                    .consultarNombreArchivoCatalogo(documentoRegistro.getTipoDocumento().trim(), pool);
            if (nombreArchivoCatalogo == null || nombreArchivoCatalogo.isBlank()) {
                nombreArchivoCatalogo = construirNombreArchivoCatalogo(documentoRegistro);
            }
            String rutaCatalogo = documentoRegistro.getCodigo().trim() + "/" + nombreArchivoCatalogo.trim();
            String existeArchivoBuck = fileService.documentoExiste(bucketName, storage, rutaCatalogo,
                    documentoRegistro.getCodigo());
            System.out.println("Agregando.....");
            System.out.println("existe archivo....."+existeArchivoBuck);

            File file = obtenerArchivoFromBase64(documentoRegistro.getArchivo(), rutaGuardado);

            String rutaEnBucket = rutaCatalogo;
            System.out.println("rutaEnBucket->>>>>>>>>>>  "+rutaEnBucket);
            System.out.println("Ruta Guardada->>>>>>>>>>>  "+rutaGuardado);
            if (!rutaEnBucket.equalsIgnoreCase(existeArchivoBuck) && existeArchivoBuck!=null ) {
                System.out.println("Archivo existente encontrado, eliminando: " + rutaEnBucket);
                fileService.deleteFile(existeArchivoBuck, storage, bucketName);
                String rutaFileCloud = saveUploadedFile(file, storage, bucketName, rutaEnBucket);
                docRegistro.setRuta_storage(rutaFileCloud);
            }else {
            	 System.out.println("que dato se guarda: " + rutaEnBucket);
            	 String rutaFileCloud = saveUploadedFile(file, storage, bucketName, rutaEnBucket);
                 docRegistro.setRuta_storage(rutaFileCloud);
                 System.out.println("Se prepara para guardar documento: " + docRegistro);
            }

           
            SCbitacora objeto = new SCbitacora(0, docRegistro.getCodSistema(), docRegistro.getCodigo(), 0, "Guardando documento...");
            this.documentoRegistroRepository.guardarBitacora(objeto, pool);
            if (this.documentoRegistroRepository.existeDocumentos(docRegistro, pool)) {
                this.documentoRegistroRepository.actualizaDocumentoCompleto(docRegistro, pool);
            } else {
                this.documentoRegistroRepository.guardarDocumento(docRegistro, pool);
            }

            docRegistro = this.documentoRegistroRepository.consulta(docRegistro.getTipoDocumento(), docRegistro.getCodigo(), docRegistro.getCodSistema(), pool);

            objeto = new SCbitacora(0, docRegistro.getCodSistema(), docRegistro.getCodigo(), 0, "Documento guardado ");
            UUID uuid = this.documentoRegistroRepository.guardarBitacora(objeto, pool);
            dr.setCodigo(docRegistro.getCodigo());
            dr.setFechaHoraCarga(docRegistro.getFechaHoraCarga());
            dr.setUuid(uuid.toString());
            rdt.setContent(dr);
            rdt.setDescripcion("Carga documento exitoso.");
            rdt.setEiyuResponseCode("OK");
            respuesta = objetMapper.writeValueAsString(rdt);
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            respuestaDto = new DocumentoRespDTO("ERROR", "Error al cargar documento", null);
            try {
                respuesta = objetMapper.writeValueAsString(respuestaDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return respuesta;
        }
    }

    private File obtenerArchivoFromBase64(String fileBase64String, String nombreFile) {
        byte[] data = Base64.decodeBase64(fileBase64String);
        File file = new File(nombreFile);
        try (OutputStream stream = new FileOutputStream(file.getAbsoluteFile())) {
            stream.write(data);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return file;
    }

    private String saveUploadedFile(File file, Storage storage, String bucketName, String rutaGuardado) throws IOException {
        if (file != null) {
            Blob blob = this.fileService.uploadFile(file, storage, bucketName, rutaGuardado);
            System.out.println(blob.getSelfLink());
            rutaGuardado = blob.getSelfLink();
        }
        return rutaGuardado;
    }

    public DocumentoRegistroJSONDto limpiarNulos(DocumentoRegistroJSONDto documentoRegistro) {
        if (documentoRegistro.getRutaNotificacion() == null) documentoRegistro.setRutaNotificacion("");
        if (documentoRegistro.getCodSistema() == null) documentoRegistro.setCodSistema("");
        if (documentoRegistro.getRuta_storage() == null) documentoRegistro.setRuta_storage("");
        if (documentoRegistro.getTipoDocumento() == null) documentoRegistro.setTipoDocumento("");
        if (documentoRegistro.getCodigo() == null) documentoRegistro.setCodigo("");
        if (documentoRegistro.getUsuarioCreacion() == null) documentoRegistro.setUsuarioCreacion("");
        if (documentoRegistro.getArchivo() == null) documentoRegistro.setArchivo("");
        if (documentoRegistro.getNombreArchivo() == null) documentoRegistro.setNombreArchivo("");
        if (documentoRegistro.getExtensionArchivo() == null) documentoRegistro.setExtensionArchivo("");

        return documentoRegistro;
    }

    private String construirNombreArchivoCatalogo(DocumentoRegistroJSONDto documentoRegistro) {
        String extensionArchivo = documentoRegistro.getExtensionArchivo() != null
                ? documentoRegistro.getExtensionArchivo().trim()
                : "";
        if (extensionArchivo.isEmpty()) {
            extensionArchivo = ".pdf";
        }
        return documentoRegistro.getTipoDocumento().trim() + extensionArchivo;
    }

    private String validarNombreArchivoLibre(DocumentoRegistroJSONDto documentoRegistro) {
        String nombreArchivo = documentoRegistro.getNombreArchivo() != null ? documentoRegistro.getNombreArchivo().trim() : "";
        String extensionArchivo = documentoRegistro.getExtensionArchivo() != null ? documentoRegistro.getExtensionArchivo().trim() : "";

        if (nombreArchivo.isEmpty()) {
            return "el nombre del archivo es obligatorio.";
        }

        if (nombreArchivo.length() > LONGITUD_MAX_NOMBRE_ARCHIVO) {
            return "el nombre del archivo excede la longitud máxima permitida de " + LONGITUD_MAX_NOMBRE_ARCHIVO + " caracteres.";
        }

        if (nombreArchivo.contains("/") || nombreArchivo.contains("\\")) {
            return "el nombre del archivo no puede contener diagonales ni rutas.";
        }

        if (!nombreArchivo.matches("^[A-Za-z0-9 _.-]+$")) {
            return "el nombre del archivo contiene caracteres no permitidos. Solo se permiten letras, números, espacios, guion, guion bajo y punto.";
        }

        if (!extensionArchivo.isEmpty() && !nombreArchivo.toUpperCase().endsWith(extensionArchivo.toUpperCase())) {
            return "la extensión del nombre del archivo no coincide con la extensión reportada.";
        }

        int lastDotIndex = nombreArchivo.lastIndexOf('.');
        String nombreSinExtension = lastDotIndex > 0 ? nombreArchivo.substring(0, lastDotIndex).trim() : nombreArchivo;
        if (nombreSinExtension.isEmpty()) {
            return "el nombre del archivo no puede contener solo la extensión.";
        }

        return null;
    }
}
