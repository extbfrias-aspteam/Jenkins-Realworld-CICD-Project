package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidaFotoIneReq {
    private String credencial;
	private String captura;
	private String tipo;
    private String curp;
	private String limiteInferior;
}
