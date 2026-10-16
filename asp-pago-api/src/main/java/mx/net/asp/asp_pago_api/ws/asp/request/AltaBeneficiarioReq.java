package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.Beneficiario;
import mx.net.asp.asp_pago_api.model.HeaderWS;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaBeneficiarioReq {
    private HeaderWS header;
    private String cuenta;
    private List<Beneficiario> beneficiarios;
}
