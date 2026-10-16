package net.cero.data.seguridad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeestatusOBJ {
    private String id;
    private Long usuarioCreacion;
    private Instant fechaCreacion;
    private Long usuarioModificacion;
    private Instant fechaModificacion;
    private String nombre;
}
