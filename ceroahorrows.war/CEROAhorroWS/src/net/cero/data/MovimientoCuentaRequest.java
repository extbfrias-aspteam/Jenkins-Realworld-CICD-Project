package net.cero.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.seguridad.utilidades.HeaderWS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoCuentaRequest {

    @NotNull(message = WS_UTIL.MENSAJE_HEADER)
    private HeaderWS header;
    private String cuentaASP;
    private String numeroTelefono;

    @Pattern(regexp = WS_UTIL.FECHA_REGEX, message = WS_UTIL.MENSAJE_FECHA_REGEX)
    @NotNull(message = WS_UTIL.MENSAJE_FECHA_INICIO)
    private String fechaInicio;

    @Pattern(regexp = WS_UTIL.FECHA_REGEX, message = WS_UTIL.MENSAJE_FECHA_REGEX)
    @NotNull(message = WS_UTIL.MENSAJE_FECHA_FIN)
    private String fechaFin;
}
