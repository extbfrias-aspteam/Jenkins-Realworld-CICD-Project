package mx.net.asp.asp_pago_api.ws.asp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidaOTPPasswordResetReq {
    private String telefono;
    private String device_id;
    private String otp;
}
