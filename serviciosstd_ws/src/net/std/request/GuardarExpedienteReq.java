package net.std.request;

import java.io.Serializable;

import net.cero.ws.data.HeaderWS;
import net.std.data.ExpedienteOBJ;


public class GuardarExpedienteReq  implements Serializable{
	private static final long serialVersionUID = 1L;

	private HeaderWS header;
	private ExpedienteOBJ expediente;
	
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public ExpedienteOBJ getExpediente() {
		return expediente;
	}
	public void setExpediente(ExpedienteOBJ expediente) {
		this.expediente = expediente;
	}
	
	
}
