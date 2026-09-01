package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoOBJ {
    private Integer id;
    private String numeroCuentaCoDi;
    private String nombreBeneficiario;
    private String numeroCuentaBeneficiario;
    private Integer tipoCuentaBeneficiario;
    private Integer idInstitucionBeneficiario;
    private String nombreInstitucion;
    private String correoBeneficiario;
    private Integer idEstatus;
    private String fechaHoraRegistro;
    private String alias;
}
