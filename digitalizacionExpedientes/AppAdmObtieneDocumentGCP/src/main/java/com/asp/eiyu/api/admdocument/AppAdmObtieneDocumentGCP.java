package com.asp.eiyu.api.admdocument;

import java.io.BufferedWriter;

import javax.sql.DataSource;

import com.asp.eiyu.api.admdocument.configuration.cloudsql.CloudSqlConnectionPool;
import com.asp.eiyu.api.admdocument.dto.DocumentoDescargaDto;
import com.asp.eiyu.api.admdocument.dto.DocumentoRegistro;
import com.asp.eiyu.api.admdocument.dto.ResponseDescargaDto;
import com.asp.eiyu.api.admdocument.dto.RespuestaDto;
import com.asp.eiyu.api.admdocument.service.DocumentoRegistroServiceImpl;
import com.asp.eiyu.api.admdocument.service.IDocumentoRegistroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;

public class AppAdmObtieneDocumentGCP implements HttpFunction {
	private IDocumentoRegistroService documentoRegistroService = new DocumentoRegistroServiceImpl();
	private static final Gson gson = new Gson();

	private static final String DB_USER = System.getenv("DB_USER");
	private static final String DB_PASS = System.getenv("DB_PASS");
	private static final String DB_NAME = System.getenv("DB_NAME");

	private static final String INSTANCE_HOST = System.getenv("INSTANCE_HOST");// prj-digitalizacion-expedientes:us-central1:admdocument-db

  @Override
  public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    System.out.println("parametros recibidos son: " + request.getReader());
	DataSource pool = CloudSqlConnectionPool.createConnectionPool(DB_USER, DB_PASS, DB_NAME, INSTANCE_HOST);

	Storage storage = PoolStorageHolder.getInstance();
	// Deserialization into the `Employee` class
	System.out.println("request.getReader():: " + request.getReader());
	DocumentoRegistro documen = gson.fromJson(request.getReader(), DocumentoRegistro.class);
	DocumentoDescargaDto body=this.setDocumentoDescargaDto(documen);
	System.out.println("body:: " + body);
	
	DocumentoDescargaDto documentoDownload = this.documentoRegistroService.descargaDocumentoRegistro(body, pool, storage);
	
	ResponseDescargaDto responseDescarga = respuestaDescargaDocumento(documentoDownload);
	RespuestaDto respFinal =this.setRespuesta(responseDescarga);
	ObjectMapper objMapper = new ObjectMapper();
	String documentoDownloadRsp = "";
	try {
		documentoDownloadRsp = objMapper.writeValueAsString(respFinal);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	}
	final BufferedWriter writer = response.getWriter();
	writer.write(documentoDownloadRsp);
}
  
  public RespuestaDto setRespuesta(ResponseDescargaDto responseDescargaDto) {
	  RespuestaDto rd = new RespuestaDto();
	  rd.setCodigoRespuesta(responseDescargaDto.getEstatusCodigo());
	  rd.setArchivo(responseDescargaDto.getRespuesta().getDocumentoBase64());
	  rd.setDescripcion(responseDescargaDto.getDescripcion());
	  return rd; 
  }
  public DocumentoDescargaDto setDocumentoDescargaDto(DocumentoRegistro body) {
	  DocumentoDescargaDto ddd = new DocumentoDescargaDto();
	  ddd.setCodigo(body.getCodigo());
	  ddd.setDatoContenido(body.getTipodocumento());
	  ddd.setDocumentoBase64(body.getExtension());
	  
	  return ddd;
  }
  
private ResponseDescargaDto respuestaDescargaDocumento(DocumentoDescargaDto documentoDownload) {
	ResponseDescargaDto response = new ResponseDescargaDto();
	response.setEstatusCodigo("OK");
	response.setDescripcion("Documento generado de manera exitosa.");
	response.setRespuesta(documentoDownload);
	if(documentoDownload != null && documentoDownload.getDocumentoBase64()!=null && "BAD".equalsIgnoreCase(documentoDownload.getDocumentoBase64())) {
		response.setEstatusCodigo("ERROR");
		response.setDescripcion("No hay documento con datos ingresados favor de validar.");
	}else if(documentoDownload==null 
			|| (documentoDownload != null && documentoDownload.getDocumentoBase64()==null) 
			|| (documentoDownload != null && documentoDownload.getDocumentoBase64().isEmpty())) {
		response.setEstatusCodigo("ERROR");
		response.setDescripcion("No existe documento en cloud para descargar.");
	}
	
	return response;
}

private static class PoolStorageHolder {

	private PoolStorageHolder() {
	}

	private static Storage STORAGE = setupPool();

	private static Storage setupPool() {
		String projectId = System.getenv("PROJECT_ID");
		STORAGE = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		return STORAGE;
	}

	private static Storage getInstance() {
		if (STORAGE != null) {
			STORAGE = setupPool();
		}
		return STORAGE;
	}
}
}