package net.cero.req.reenviarws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cero.req.general.HeaderWS;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReenviarMensajeReq {
    @NotNull(message = "El %s es obligatorio")
    private HeaderWS header;

    @NotNull(message = "El campo %s es obligatorio")
    @NotBlank(message = "El campo %s no puede estar vacio")
    private String tipoReenvio;
    private String correoElectronico;
    private String telefono;
}
