package net.std.request;

import java.io.Serializable;

public class ImagenExpedientesReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String ruta_alfresco;
	private String alfresco_id;
	
	public ImagenExpedientesReq(){
		
	}
	
	public ImagenExpedientesReq(String ruta_alfresco, String alfresco_id){
		this.ruta_alfresco = ruta_alfresco;
		this.alfresco_id = alfresco_id;
	}

	public String getRuta_alfresco() {
		return ruta_alfresco;
	}

	public void setRuta_alfresco(String ruta_alfresco) {
		this.ruta_alfresco = ruta_alfresco;
	}

	public String getAlfresco_id() {
		return alfresco_id;
	}

	public void setAlfresco_id(String alfresco_id) {
		this.alfresco_id = alfresco_id;
	}
}
