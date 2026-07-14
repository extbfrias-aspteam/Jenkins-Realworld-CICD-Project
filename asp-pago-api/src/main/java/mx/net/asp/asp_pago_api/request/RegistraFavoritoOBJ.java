package mx.net.asp.asp_pago_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistraFavoritoOBJ {
    private String numeroCuentaCoDi;
    private String data;
    private Integer idCanal;
    private boolean guardarFavorito;
}
