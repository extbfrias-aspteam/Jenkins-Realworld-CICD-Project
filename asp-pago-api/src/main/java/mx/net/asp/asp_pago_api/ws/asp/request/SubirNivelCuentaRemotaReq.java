package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubirNivelCuentaRemotaReq {
    private HeaderWS header;
    private String cuentaah;
    private Integer actividadId;
    private Integer giroId;
    private Integer ocupacionId;
    private Integer objetivoId;
    private Integer frecuenciaId;
    private Integer ahorroId;
    private Integer ingresoId;
    private Boolean acepta_term;
    private String latitud;
    private String longitud;
}
