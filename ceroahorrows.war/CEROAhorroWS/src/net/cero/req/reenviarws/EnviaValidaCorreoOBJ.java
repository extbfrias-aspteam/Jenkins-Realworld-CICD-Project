package net.cero.req.reenviarws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnviaValidaCorreoOBJ {
    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Email(message = "El campo %s no tiene un formato de email correcto")
    private String correoElectronico;
    private String rfc;
    private String curp;
    private String nombre;
    private String token;
}
