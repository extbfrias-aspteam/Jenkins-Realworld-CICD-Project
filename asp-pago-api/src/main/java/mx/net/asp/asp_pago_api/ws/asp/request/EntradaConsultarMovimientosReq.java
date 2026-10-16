package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntradaConsultarMovimientosReq implements Serializable {
    @Serial
    private static final long serialVersionUID = -246297738814938562L;

    private HeaderWS header;

    private String tipoTarjeta;

    private String numeroTarjeta;

    private String token;

    private String medioAcceso;
    private String tipoMedioAcceso;
    private String claveTipoCuenta;
    private String fechaInicial;
    private String fechaFinal;
    private String maxMovimientos;
}
