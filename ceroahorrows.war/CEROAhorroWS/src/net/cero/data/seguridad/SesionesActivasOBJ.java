package net.cero.data.seguridad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SesionesActivasOBJ {
    private Long id;
    private String aplicativo;
    private String fechaCreacion;
}
