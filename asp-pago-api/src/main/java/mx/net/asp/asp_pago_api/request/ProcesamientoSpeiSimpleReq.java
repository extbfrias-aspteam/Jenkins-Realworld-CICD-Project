package mx.net.asp.asp_pago_api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.net.asp.asp_pago_api.model.HeaderWS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcesamientoSpeiSimpleReq {
    @Valid
    @NotNull(message = "El campo 'header' es obligatorio")
    private HeaderWS header;
    @NotBlank(message = "El campo 'personaIdOrdenante' es obligatorio")
    private String personaIdOrdenante;
    @NotBlank(message = "El campo 'cuentaAhorro' es obligatorio")
    private String cuentaAhorro;
    @NotBlank(message = "El campo 'nombreBeneficiario' es obligatorio")
    private String nombreBeneficiario;
    @NotNull(message = "El campo 'idTipoCuentaBeneficiario' es obligatorio")
    @Min(value = 1, message = "El campo 'idTipoCuentaBeneficiario' no debe ser 0")
    private Integer idTipoCuentaBeneficiario;
    @NotBlank(message = "El campo 'cuentaBeneficiario' es obligatorio")
    @Pattern(regexp = "^(\\d{10}|\\d{16}|\\d{18})$", message = "El campo 'cuentaBeneficiario' debe tener exactamente 10, 16 o 18 dígitos.")
    private String cuentaBeneficiario;
    @NotNull(message = "El campo 'idInstitucionBeneficiario' es obligatorio")
    @Min(value = 1, message = "El campo 'idInstitucionBeneficiario' no debe ser 0")
    private Integer idInstitucionBeneficiario;
    @NotBlank(message = "El campo 'conceptoPago' es obligatorio")
    private String conceptoPago;
    @NotNull(message = "El campo 'monto' es obligatorio")
    @DecimalMin(value = "0.01", message = "El 'monto' debe ser mayor a 0")
    private Double monto;
    private Integer referenciaNumerica;
    @NotNull(message = "El campo 'idTipoPago' es obligatorio")
    @Min(value = 1, message = "El campo 'idTipoPago' no debe ser 0")
    private Integer idTipoPago;
    private String personaIdBeneficiario;
}
