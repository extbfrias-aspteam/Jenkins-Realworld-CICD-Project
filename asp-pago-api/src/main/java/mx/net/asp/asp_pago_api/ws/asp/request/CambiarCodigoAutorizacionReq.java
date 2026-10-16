package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambiarCodigoAutorizacionReq {
    private String codigoNuevo;
    private String codigoAnterior;
    private String usuarioId;
    private boolean biometrico;
}
