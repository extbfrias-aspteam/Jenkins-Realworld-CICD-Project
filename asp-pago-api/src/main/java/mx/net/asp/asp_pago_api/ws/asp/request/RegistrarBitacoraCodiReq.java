package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.DetalleCodi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarBitacoraCodiReq {
    private String alias;
    private Integer dv;
    private String fechaHora;
    private Long epoch;
    private String accion;
    private String evento;
    private DetalleCodi detalle;
}
