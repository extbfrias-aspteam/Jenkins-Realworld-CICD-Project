package mx.net.asp.procesaRendimientosCero.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnviaEmailReq {
    private String destinatario;
    private String asunto;
    private String contenido;
}
