package mx.net.asp.asp_pago_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.ws.asp.request.CodiDataReq;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSubsecuenteReq {
    private String numeroCuentaAhorro;
    private String dataCif;
    private CodiDataReq data;
    private Integer idCanal;
}
