package mx.net.asp.asp_pago_api.config;

import lombok.Getter;

@Getter
public class ServicioException extends RuntimeException {
    private final int codigo;
    private final String mensaje;

    public ServicioException(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

}
