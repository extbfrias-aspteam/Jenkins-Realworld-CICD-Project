package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.InformacionAdicional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodiDataReq {

    private Long nc;
    private Integer dv;
    private String idH;
    private InformacionAdicional ia;
    private String idN;
    private String hmac;
    private String codR;
    private Integer actualizaCodR;
    private String idNI;
    private Integer dvR;
    private Integer dvOmision;
    private Integer edoPet;
}
