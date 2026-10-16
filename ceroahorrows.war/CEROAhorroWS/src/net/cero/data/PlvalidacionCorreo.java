package net.cero.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlvalidacionCorreo {
    private Long id;
    private String correo;
    private String estado;
    private String rfc;
    private String curp;
    private String token;
}
