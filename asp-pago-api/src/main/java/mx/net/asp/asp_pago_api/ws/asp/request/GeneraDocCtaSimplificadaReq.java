package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneraDocCtaSimplificadaReq {
    private HeaderWS header;
    private String cuentaah;
    private Boolean subirDoc;
}
