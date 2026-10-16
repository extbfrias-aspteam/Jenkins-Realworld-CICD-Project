package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcesaMap implements Serializable {

    @Serial
    private static final long serialVersionUID = -246297738814938562L;

    private String codBarra;
    private String codigo;
    private String cuenta;
    private String monto;
    private String montoCs;
    private String nemEmp;
    private String numMed;
    private String subEmp;
}
