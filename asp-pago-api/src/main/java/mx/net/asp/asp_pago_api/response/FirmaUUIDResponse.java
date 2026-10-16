package mx.net.asp.asp_pago_api.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmaUUIDResponse {
    private String uuid;
    private String uuidSigned;
}
