package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneraAnexoDetalleBeneficiarioReq {
    private String cuentaah;
    private Boolean subirDoc;
}
