package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;
import mx.net.asp.asp_pago_api.model.ProcesaMap;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntradaEnviaServiciosReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -246297738814938562L;

    private HeaderWS header;
    private ProcesaMap map;
}
