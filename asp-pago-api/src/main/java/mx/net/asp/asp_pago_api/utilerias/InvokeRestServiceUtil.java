package mx.net.asp.asp_pago_api.utilerias;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.config.ErrorProperties;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Log4j2
@AllArgsConstructor
public class InvokeRestServiceUtil {

    private final ErrorProperties errorProperties;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final String causaMessage = "Causa: ";
    private final String noCausaErrorMessage = "No hay causa";

    public CompletableFuture<RespuestaDTO> enviarSolicitudAsync(String url, HttpEntity<?> requestEntity, HttpMethod method, Map<String, String> queryParams) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            // Construcción de la URL con parámetros si existen
            if (queryParams != null && !queryParams.isEmpty()) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                queryParams.forEach(builder::queryParam);
                url = builder.toUriString();
            }

            ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("-----> Respuesta exitosa: " + response.getBody());
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(response.getBody());
                return CompletableFuture.completedFuture(respuesta);
            } else {
                log.error("-----> Respuesta ERROR: " + response.getBody());
                respuesta = gson.fromJson(response.getBody(), RespuestaDTO.class);
                return CompletableFuture.completedFuture(respuesta);
            }
        } catch (ResourceAccessException e) {
            // Manejo de errores específicos de acceso al recurso
            respuesta = manejarExcepcionAcceso(e);
            return CompletableFuture.completedFuture(respuesta);
        } catch (Exception e) {
            // Manejo de errores generales
            log.error("-----> Error general al enviar la solicitud: " + e.getMessage());
            log.error(causaMessage + (e.getCause() != null ? e.getCause().getMessage() : noCausaErrorMessage));
            return CompletableFuture.completedFuture(crearRespuestaError("Error inesperado: " + e.getMessage()));
        }
    }

    public RespuestaDTO enviarSolicitud(String url, HttpEntity<?> requestEntity, HttpMethod method, Map<String, String> queryParams) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            // Construcción de la URL con parámetros si existen
            if (queryParams != null && !queryParams.isEmpty()) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                queryParams.forEach(builder::queryParam);
                url = builder.toUriString();
            }

            // Verificar si la URL contiene "asp-pago-documents"
            boolean peticionDocuments = url.contains("asp-pago-documents");

            // Realizar la solicitud al servicio
            ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
            // Manejar respuesta exitosa (2xx)
            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode() == HttpStatus.FOUND) {
                if (peticionDocuments) {
                    log.info("-----> Respuesta exitosa de asp-pago-documents (Body oculto)");
                } else {
                    log.info("-----> Respuesta exitosa: " + response.getBody());
                }

                String bodyRespuesta = response.getBody();

                // Validar respuestas internas de plásticos
                if (url.contains("admin-plasticos-services")
                        || url.contains("admin-plasticos-logistics")) {

                    RespuestaDTO respuestaInterna =
                            gson.fromJson(bodyRespuesta, RespuestaDTO.class);

                    if (respuestaInterna.getCodigo() != 0) {
                        agregarClaveError(respuestaInterna, url);
                        bodyRespuesta = gson.toJson(respuestaInterna);
                    }
                }

                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(bodyRespuesta);

                return respuesta;
            } else {
                // Manejar errores con códigos HTTP no exitosos
                log.error("-----> Respuesta ERROR: " + response.getBody());
                respuesta = gson.fromJson(response.getBody(), RespuestaDTO.class);
                agregarClaveError(respuesta, url);
                return respuesta;
            }
        } catch (ResourceAccessException e) {
            // Manejo de errores específicos de acceso al recurso
            respuesta = manejarExcepcionAcceso(e);
            return respuesta;
        } catch (Exception e) {
            // Manejo de errores generales
            log.error("-----> Error general al enviar la solicitud: " + e.getMessage());
            log.error(causaMessage + (e.getCause() != null ? e.getCause().getMessage() : noCausaErrorMessage));
            return crearRespuestaError("Error inesperado: " + e.getMessage());
        }
    }

    // Metodo para crear un objeto RespuestaDTO en caso de error
    private RespuestaDTO crearRespuestaError(String mensaje) {
        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setMensaje(mensaje);
        respuesta.setCodigo(-500);
        respuesta.setData(null);
        return respuesta;
    }

    // Metodo para manejar ResourceAccessException y retornarlo como JSON
    private RespuestaDTO manejarExcepcionAcceso(ResourceAccessException e) {
        RespuestaDTO respuesta = new RespuestaDTO();
        Throwable cause = e.getCause();
        if (cause instanceof java.net.SocketTimeoutException) {
            log.error("-----> Error SocketTimeoutException: " + e.getMessage());
            log.error(causaMessage + (e.getCause() != null ? e.getCause().getMessage() : noCausaErrorMessage));
            respuesta.setMensaje("El servicio no respondió en el tiempo esperado.");
            respuesta.setCodigo(-501);
        } else if (cause instanceof java.net.ConnectException) {
            log.error("-----> Error ConnectException: " + e.getMessage());
            log.error(causaMessage + (e.getCause() != null ? e.getCause().getMessage() : noCausaErrorMessage));
            respuesta.setMensaje("No se pudo establecer conexión con el servicio.");
            respuesta.setCodigo(-502);
        } else {
            log.error("-----> Error de acceso al recurso: " + e.getMessage());
            log.error(causaMessage + (e.getCause() != null ? e.getCause().getMessage() : noCausaErrorMessage));
            respuesta.setMensaje("Error al acceder al recurso: " + e.getMessage());
            respuesta.setCodigo(-503);
        }
        return respuesta;
    }

    public static String getURLRest(String ip, String puerto, String servicio) {
        // Declaracion de variable para la respuesta
        StringBuilder urlRest = new StringBuilder();
        // Validacion del parametro ip
        if (validaCadena(ip)) {
            // Se agrega la ip a la variable de respuesta
            urlRest.append(ip);
        }
        // Validacion del parametro puerto
        if (validaCadena(puerto)) {
            // Se agrega el puerto a la variable de respuesta
            urlRest.append(":").append(puerto);
        }
        // Validacion del parametro servicio
        if (validaCadena(servicio)) {
            // Se agrega el servicio a la variable de respuesta
            urlRest.append("/").append(servicio);
        }
        // Impresion de la respuesta
        log.info("[" + ip + "][" + puerto + "][" + servicio + "]:: [" + urlRest + "]");
        // Se regresa respuesta
        return urlRest.toString();
    }

    public static boolean validaCadena(String cadena) {
        // Declaracion de a variable para la respuesta
        boolean valida = false;
        // Validacion de null
        if (cadena != null) {
            // Eliminan espacios del inicio y final de la cadena
            cadena = cadena.trim();
            // Validacion de vacio
            if (!cadena.isEmpty()) {
                // Asignacion de true a la respuesta
                valida = true;
            }
        }
        // Se regresa respuesta
        return valida;
    }

    private void agregarClaveError(RespuestaDTO respuesta, String url) {

        // Validar exclusiones
        if (omitirClaveError(url, respuesta.getCodigo())) {
            return;
        }

        String claveError = obtenerClaveError(url);

        String mensajeFinal = String.format(
                "%s [%s-%d]",
                respuesta.getMensaje(),
                claveError,
                Math.abs(respuesta.getCodigo())
        );

        respuesta.setMensaje(mensajeFinal);
    }

    private String obtenerClaveError(String url) {
        return errorProperties.getRutas()
                .entrySet()
                .stream()
                .filter(entry -> url.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("API");
    }

    private boolean omitirClaveError(String url, Integer codigo) {
        if (errorProperties.getExclusiones() == null) {
            return false;
        }

        return errorProperties.getExclusiones()
                .stream()
                .anyMatch(exclusion ->
                        // Servicio exacto
                        url.contains(exclusion.getServicio())
                                // Endpoint exacto
                                && url.contains(exclusion.getEndpoint())
                                // Código exacto
                                && exclusion.getCodigos().contains(codigo)
                );
    }
}
