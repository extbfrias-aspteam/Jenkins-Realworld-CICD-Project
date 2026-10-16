package net.cero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidacionCorreoOBJ {
    private Long id;
    private String correo;
    private String rfc;
    private String curp;
    private String estado;
}
