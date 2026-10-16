package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaCuentaInversionReq {

    private HeaderWS header;
    private String cuentaPadre;
    private String tituloInversion;
    private Double monto;
    private Integer plazo;
    private Integer tipoModalidadId;
    private Boolean reinvertir;
    private Integer tipoReinvertirId;
}
