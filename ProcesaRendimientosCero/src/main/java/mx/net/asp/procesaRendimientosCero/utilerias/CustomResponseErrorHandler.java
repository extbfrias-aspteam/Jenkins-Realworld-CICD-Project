package mx.net.asp.procesaRendimientosCero.utilerias;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    // Configurar el ResponseErrorHandler para que ignore el error 400
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        // Solo considera error si el código no es 400 y pertenece a una serie de errores
        return statusCode != HttpStatus.BAD_REQUEST && (
                statusCode.series() == HttpStatus.Series.CLIENT_ERROR ||
                        statusCode.series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Solo lanza excepción si el código de estado no es 400
        if (response.getStatusCode() != HttpStatus.BAD_REQUEST) {
            throw new HttpClientErrorException(response.getStatusCode(), response.getStatusText());
        }
    }

}