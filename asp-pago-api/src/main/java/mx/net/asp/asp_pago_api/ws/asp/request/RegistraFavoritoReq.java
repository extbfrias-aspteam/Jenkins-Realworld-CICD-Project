package mx.net.asp.asp_pago_api.ws.asp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistraFavoritoReq {

    private Integer id;
    private String numeroCuentaCoDi;
    private String nombreBeneficiario;
    private String numeroCuentaBeneficiario;
    private Integer tipoCuentaBeneficiario;
    private Integer idInstitucionBeneficiario;
    private String nombreInstitucion;
    private String correoBeneficiario;
    private Integer idEstatus;
    private String alias;
    private boolean guardarFavorito;
    private String personaId;
}
