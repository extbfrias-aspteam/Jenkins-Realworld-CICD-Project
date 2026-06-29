package mx.net.asp.procesaRendimientosCero.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.procesaRendimientosCero.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSaldoReq {
    private HeaderWS header;
    private String cuenta;
    private Integer idCanal;
}
