package net.cero.req.codi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cero.req.general.HeaderWS;
import net.cero.validators.CheckDateFormat;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultarEstatusCodiReq {
    @NotNull(message = "El %S es obligatorio")
    private HeaderWS header;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @Pattern(regexp = "[P|C]+",message = "El campo %s solo acepta los valores P=Movimientos de Pagos o C=Movimientos de cobros")
    private String tipoOperacion;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    private String referencia;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    @CheckDateFormat(pattern = "uuuu-MM-dd",message = "El campo %s debe tener un valor valido con el formato de fecha yyyy-MM-dd")
    private String fechaOperacion;
}
