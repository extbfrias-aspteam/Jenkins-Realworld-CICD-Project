package net.cero.ahorro.data;

import lombok.Data;

@Data
public class WSAspClientRespuestaOBJ {
    private static final long serialVersionUID = 1L;
	private Integer resultado=null;
	private String respuesta="";
	private String msgHost="";
	private String referencia="";
	private String numAutorizacion="";
	private Object objeto = null;
	private Integer resultadoCatel = -1;

    
}
