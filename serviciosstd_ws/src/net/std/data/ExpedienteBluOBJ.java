package net.std.data;

import java.io.Serializable;

public class ExpedienteBluOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String extension;
	private String documento;
	private String repLegal;

	public ExpedienteBluOBJ(){

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getRepLegal() {
		return repLegal;
	}

	public void setRepLegal(String repLegal) {
		this.repLegal = repLegal;
	}
}
