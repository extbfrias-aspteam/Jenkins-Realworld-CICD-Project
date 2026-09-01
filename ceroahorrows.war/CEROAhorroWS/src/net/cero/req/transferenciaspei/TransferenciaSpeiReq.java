package net.cero.req.transferenciaspei;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import net.cero.req.general.HeaderWS;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaSpeiReq {
    @NotNull(message = "El %S es obligatorio")
    private HeaderWS header;

    /*Cuenta ASP o Cuenta CLABE*/
    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Pattern(regexp = "[\\d]{10}|[\\d]{18}",message = "El campo %s solo acepta Cuenta ASP(10 digitos) o CLABE(18 digitos)")
    private String cuentaOrigen;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Size(min = 18,max=18,message = "El campo %s debe tener 18 digitos(CLABE)")
    @Pattern(regexp = "[\\d]*",message = "El campo %s solo acepta digitos")
    private String cuentaBeneficiaria;

    @NotNull(message = "El campo %s es obligatorio")
    @DecimalMin(value = "0.01",message = "El valor del campo %s debe ser mayor de 0")
    @Digits(integer=20,fraction = 2,message = "El campo %s no puede tener mas de 20 enteros y 2 decimales")
    private Double monto;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Size(min=1,max=40,message = "El campo %s no puede tener mas de 40 caracteres")
    @Pattern(regexp = "^[ a-zA-Z0-9äöüÄÖÜ]*$",message = "El campo %s no acepta caracteres especiales ni acentos")
    private String nombreBenefSpeiDestino;


    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Size(min = 13,max = 14, message = "El campo %s no puede tener menos de 13 caracteres y no mas de 14")
    @Pattern(regexp = "^[ a-zA-Z0-9äöüÄÖÜ]*$",message = "El campo %s no acepta caracteres especiales ni acentos")
    private String rfcBenefSpeiDestino;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Size(message = "El campo %s no puede ser mayor de 40 caracteres ni acentos",max = 40)
    @Pattern(regexp = "^[ a-zA-Z0-9äöüÄÖÜ]*$",message = "El campo %s no acepta caracteres especiales ni acentos")
    private String conceptoPago;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Email(message = "El campo %s debe tener un formato de correo valido")
    private String correoBenefSpeiDestino;
}
