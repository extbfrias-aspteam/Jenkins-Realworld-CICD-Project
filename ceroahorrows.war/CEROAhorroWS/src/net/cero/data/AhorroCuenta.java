package net.cero.data;

import java.util.List;


import java.util.ArrayList;

public class AhorroCuenta {

	private String cuenta;	
	private String referencia;	
	private String solicitanteId;	
	private String cliente;
	
	public AhorroCuenta() {}
	
	public AhorroCuenta(String cuenta, String referencia, String solicitanteId, String cliente) {
		this.cuenta = cuenta;
		this.referencia = referencia;
		this.solicitanteId = solicitanteId;
		this.cliente = cliente;
	}	
	
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getSolicitanteId() {
		return solicitanteId;
	}
	public void setSolicitanteId(String solicitanteId) {
		this.solicitanteId = solicitanteId;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	
	@Override
	public String toString() {
		return "AhorroCuenta [cuenta=" + cuenta + ", referencia=" + referencia + ", solicitanteId=" + solicitanteId
				+ ", cliente=" + cliente + "]";
	}

	
	public static List<AhorroCuenta> obtenerListadoCuentas(List<AhorroContrato> listQuery)
	{
		List<AhorroCuenta> list = new ArrayList<AhorroCuenta>();
		for(AhorroContrato ac: listQuery)
		{
			AhorroCuenta ax = new AhorroCuenta(ac.getCuenta(), ac.getReferencia(), ac.getSolicitante()
					, ac.getSolicitanteNombre());
			
			list.add(ax);
		}
		
		return list;
	}

}
