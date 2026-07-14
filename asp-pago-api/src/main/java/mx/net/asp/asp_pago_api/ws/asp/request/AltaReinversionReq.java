package mx.net.asp.asp_pago_api.ws.asp.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AltaReinversionReq {

	private String cuentaInversion;
    private String tituloReinversion;
    private Double monto;
    private Integer tipoReinvertirId;
}
