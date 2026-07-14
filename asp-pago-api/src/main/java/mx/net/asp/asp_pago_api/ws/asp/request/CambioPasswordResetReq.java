package mx.net.asp.asp_pago_api.ws.asp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioPasswordResetReq {
    private String nueva_password;
    private String device_id;
    private String resetSessionToken;
}
