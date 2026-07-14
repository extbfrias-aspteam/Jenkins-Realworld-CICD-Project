package mx.net.asp.asp_pago_api.ws.asp.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthReq {
    private HeaderWS header;
    private String telefono;
    private String dispositivoId;
    private String tenantId;
    @NotBlank(message = "El campo 'path' es obligatorio")
    private String path;
    @NotNull(message = "El campo 'idcanal' es obligatorio")
    @Min(value = 1, message = "El campo 'idcanal' no debe ser 0")
    private Integer idCanal;
}
