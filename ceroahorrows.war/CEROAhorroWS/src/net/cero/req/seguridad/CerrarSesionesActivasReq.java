package net.cero.req.seguridad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cero.req.general.HeaderWS;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CerrarSesionesActivasReq {
    @NotNull(message = "El parametro header es obligatorio")
    private HeaderWS header;
    @NotBlank(message = "El parametro usuario no puede ser vacio")
    @NotNull(message = "El parametro usuario es obligatorio")
    private String usuario;
    @NotNull(message = "El idAplicativo usuario es obligatorio")
    private Integer idAplicativo;
}
