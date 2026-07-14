package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulaInversionReq {
	private Double monto;
	private Integer plazo;
	private Integer tipoModalidadId;
}
