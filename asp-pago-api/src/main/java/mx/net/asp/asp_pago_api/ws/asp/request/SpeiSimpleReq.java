package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeiSimpleReq {
    private HeaderWS header;
    private String cuentaAhorro;
    private String dataCif;
    private Integer idCanal;
}
