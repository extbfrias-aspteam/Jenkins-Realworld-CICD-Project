package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaTraspasoEiyu {
    private Integer estado;
    private String descripcion;
    private String respuesta;
    private String error;
    private String clave;
}
