package mx.net.asp.asp_pago_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private String body;
    private HttpStatus status;
}
