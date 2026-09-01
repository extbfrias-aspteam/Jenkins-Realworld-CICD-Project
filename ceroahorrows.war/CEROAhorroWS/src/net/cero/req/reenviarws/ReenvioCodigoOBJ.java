package net.cero.req.reenviarws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReenvioCodigoOBJ {
    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Pattern(regexp = "^[0-9]*$",message = "El campo %s solo acepta numeros")
    @Size(min = 10,max = 10, message = "El campo %s debe tener 10 digitos")
    private String telefono;
}
