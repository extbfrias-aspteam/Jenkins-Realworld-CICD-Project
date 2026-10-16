package mx.net.asp.asp_pago_api.ws.asp.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroImagenesCuentaSimplificadaReq {
    private String numeroCuenta;
    private List<String> rutasIne;
    private String fotoSelfie;
}
