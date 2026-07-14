package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidarCodigoAutorizacionReq {
    private String codigo;
    private String usuarioId;
    private int servicioId;
    private boolean biometrico;
}
