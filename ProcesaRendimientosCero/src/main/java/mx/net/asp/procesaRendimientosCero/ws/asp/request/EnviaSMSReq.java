package mx.net.asp.procesaRendimientosCero.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnviaSMSReq {
    //	@NotNull(message = "El campo 'header' es obligatorio")
    private String celular;
    private String mensaje;
    private String personaId;
    private String operacion;
}
