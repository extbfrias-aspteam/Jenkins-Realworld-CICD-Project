package mx.net.asp.asp_pago_api.filters;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Getter
public class HttpServletRequestCopier extends HttpServletRequestWrapper {
    private final byte[] body;

    public HttpServletRequestCopier(HttpServletRequest request) throws IOException {
        super(request);
        // Captura el cuerpo de la solicitud y lo convierte en un array de bytes
        this.body = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        // Crear un ServletInputStream envolviendo el array de bytes
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Implementación vacía para cumplir con la interfaz
                // Si necesitas una implementación real, puedes agregar la lógica aquí
            }
        };
    }

}
