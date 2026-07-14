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
public class RefreshTokenReq {
    private HeaderWS header;
    @NotBlank(message = "El campo 'telefono' es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El campo 'telefono' debe contener exactamente 10 dígitos numéricos")
    private String telefono;
    @NotBlank(message = "El campo 'dispositivoId' es obligatorio")
    private String dispositivoId;
    @NotBlank(message = "El campo 'path' es obligatorio")
    private String path;
    @NotBlank(message = "El campo 'refresh_token' es obligatorio")
    private String refresh_token;
    @NotNull(message = "El campo 'idcanal' es obligatorio")
    @Min(value = 1, message = "El campo 'idcanal' no debe ser 0")
    private Integer idCanal;

}
