package net.std.request;

import java.io.Serializable;
import java.util.List;

import net.std.data.CuentaReferenciadaVolumenOBJ;

public class AltaReferenciaReq  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<CuentaReferenciadaVolumenOBJ> lstReferencias;
	
	public AltaReferenciaReq(){
		
	}

	public List<CuentaReferenciadaVolumenOBJ> getLstReferencias() {
		return lstReferencias;
	}

	public void setLstReferencias(List<CuentaReferenciadaVolumenOBJ> lstReferencias) {
		this.lstReferencias = lstReferencias;
	}
}
