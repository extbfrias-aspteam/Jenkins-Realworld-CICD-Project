package net.std.request;

import java.util.List;

import net.cero.ws.data.HeaderWS;
import net.std.data.ConceptoPLD;
import net.std.data.CuentaPLDOBJ;

//import net.cero.seguridad.utilidades.HeaderWS;

public class GuardarPLDReq {

	private HeaderWS header;
	private CuentaPLDOBJ datos;
	private List<ConceptoPLD> conceptos;
	
	
	public HeaderWS getHeader() {
		return header;
	}
	public void setHeader(HeaderWS header) {
		this.header = header;
	}
	public CuentaPLDOBJ getDatos() {
		return datos;
	}
	public void setDatos(CuentaPLDOBJ datos) {
		this.datos = datos;
	}
	public List<ConceptoPLD> getConceptos() {
		return conceptos;
	}
	public void setConceptos(List<ConceptoPLD> conceptos) {
		this.conceptos = conceptos;
	}

}
