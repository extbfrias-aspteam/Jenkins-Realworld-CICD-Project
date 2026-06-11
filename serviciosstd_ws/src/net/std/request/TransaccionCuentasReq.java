package net.std.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransaccionCuentasReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cuentaClabe;		  //  DEPOSITO = CUENTA BENEFICIARIA ,  RETIRO = CUENTA ORDENANTE (cuentaClabe)
	private BigDecimal monto;
	private String identificador;
	private Integer actualizaSaldo;   // 0 = normal, 1 = recalcula
	private String claveRastreo;
	private Long id_spei;
	private String concepto;
	private String cuentaClabeEmiRec; // DEPOSITO = CUENTA ORDENANTE,	RETIRO = CUENTA BENEFICIARIA (cuentaClabeEmiRec)

	
	public TransaccionCuentasReq(){
		
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getActualizaSaldo() {
		return actualizaSaldo;
	}

	public void setActualizaSaldo(Integer actualizaSaldo) {
		this.actualizaSaldo = actualizaSaldo;
	}

	public String getCuentaClabe() {
		return cuentaClabe;
	}

	public void setCuentaClabe(String cuentaClabe) {
		this.cuentaClabe = cuentaClabe;
	}

	public String getClaveRastreo() {
		return claveRastreo;
	}

	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getCuentaClabeEmiRec() {
		return cuentaClabeEmiRec;
	}

	public void setCuentaClabeEmiRec(String cuentaClabeEmiRec) {
		this.cuentaClabeEmiRec = cuentaClabeEmiRec;
	}

	public Long getId_spei() {
		return id_spei;
	}

	public void setId_spei(Long id_spei) {
		this.id_spei = id_spei;
	}
}
