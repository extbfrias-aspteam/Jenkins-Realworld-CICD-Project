package mx.net.asp.asp_pago_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCif {
    private String nombreOrdenante;
    private Integer idTipoCuentaOrdenante;
    private String cuentaOrdenante;
    private Integer idInstitucionOrdenante;
    private String nombreBeneficiario;
    private Integer idTipoCuentaBeneficiario;
    private String cuentaBeneficiario;
    private String rfcBeneficiario;
    private Integer idInstitucionBeneficiario;
    private String correoBeneficiario;
    private String conceptoPago;
    private Double monto;
    private Integer referenciaNumerica;
    private Integer idTipoPago;
    private Long fechaCreacion;
    private FechaNacimiento fechaNacimiento;
    private String latitud;
    private String longitud;
}
