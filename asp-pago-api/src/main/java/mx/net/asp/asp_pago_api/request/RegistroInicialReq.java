package mx.net.asp.asp_pago_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.ws.asp.request.RegistroInicialDataReq;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroInicialReq {

    private String numeroCuentaAhorro;
    private RegistroInicialDataReq data;
    private String dataCif;
    private Integer idCanal;
}
