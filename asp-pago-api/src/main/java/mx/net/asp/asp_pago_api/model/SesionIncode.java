package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionIncode {
    private String sesionIncodeId;
    private Integer moduloId;
    private String moduloDescripcion;
    private String token;
}
