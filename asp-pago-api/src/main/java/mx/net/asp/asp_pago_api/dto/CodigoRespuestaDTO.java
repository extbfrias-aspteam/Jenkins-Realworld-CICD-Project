package mx.net.asp.asp_pago_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodigoRespuestaDTO {
    private Integer codigoResponse;
    private String cadenaCifrada;
}
