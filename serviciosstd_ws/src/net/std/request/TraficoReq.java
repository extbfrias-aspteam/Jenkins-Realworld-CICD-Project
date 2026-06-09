package net.std.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class TraficoReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String proceso;
	private String cuentaOrd;
	private String cuentaDes;
	private String claveRastreo;
	private BigDecimal monto;
	private String concepto;
	
	public TraficoReq(){
		
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getCuentaOrd() {
		return cuentaOrd;
	}

	public void setCuentaOrd(String cuentaOrd) {
		this.cuentaOrd = cuentaOrd;
	}

	public String getCuentaDes() {
		return cuentaDes;
	}

	public void setCuentaDes(String cuentaDes) {
		this.cuentaDes = cuentaDes;
	}

	public String getClaveRastreo() {
		return claveRastreo;
	}

	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
}
