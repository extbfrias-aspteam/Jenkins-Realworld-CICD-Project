package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.InformacionAdicional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroInicialDataReq {
    private Long nc;
    private String idH;
    private InformacionAdicional ia;
    private String gId;
    private Integer dv;
    private Integer edoPet;
    private String ncR;
}
