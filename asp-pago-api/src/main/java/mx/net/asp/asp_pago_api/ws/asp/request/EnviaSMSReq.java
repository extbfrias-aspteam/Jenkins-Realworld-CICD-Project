package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnviaSMSReq {
    private String celular;
    private String mensaje;
    private String personaId;
    private String operacion;
    private Integer codigoId;
}
