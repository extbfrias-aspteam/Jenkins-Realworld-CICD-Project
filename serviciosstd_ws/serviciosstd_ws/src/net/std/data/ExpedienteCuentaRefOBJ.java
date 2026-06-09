package net.std.data;

import java.io.Serializable;
import java.util.List;

public class ExpedienteCuentaRefOBJ  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String cuenta_referencia;
	private List<ExpedienteBluOBJ> archivos;

	public ExpedienteCuentaRefOBJ(){

	}

	public String getCuenta_referencia() {
		return cuenta_referencia;
	}

	public void setCuenta_referencia(String cuenta_referencia) {
		this.cuenta_referencia = cuenta_referencia;
	}

	public List<ExpedienteBluOBJ> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<ExpedienteBluOBJ> archivos) {
		this.archivos = archivos;
	}

}
