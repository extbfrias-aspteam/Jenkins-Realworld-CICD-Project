package mx.net.asp.procesaRendimientosCero.utilerias;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
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

    private final RestTemplate restTemplate;
    private final Gson gson;

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
                log.info("-----> Respuesta exitosa: {}", response.getBody());
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(response.getBody());
                return CompletableFuture.completedFuture(respuesta);
            } else {
                log.error("-----> Respuesta ERROR: {}", response.getBody());
                respuesta = gson.fromJson(response.getBody(), RespuestaDTO.class);
                return CompletableFuture.completedFuture(respuesta);
            }
        } catch (ResourceAccessException e) {
            // Manejo de errores específicos de acceso al recurso
            respuesta = manejarExcepcionAcceso(e);
            return CompletableFuture.completedFuture(respuesta);
        } catch (Exception e) {
            // Manejo de errores generales
            log.error("-----> Error general al enviar la solicitud: {}", e.getMessage());
            log.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa");
            return CompletableFuture.completedFuture(crearRespuestaError("Error inesperado: " + e.getMessage(), -500));
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
                    log.info("-----> Respuesta exitosa: {}", response.getBody());
                }
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(response.getBody());
                return respuesta;
            } else {
                // Manejar errores con códigos HTTP no exitosos
                log.error("-----> Respuesta ERROR: {}", response.getBody());
                respuesta = gson.fromJson(response.getBody(), RespuestaDTO.class);
                return respuesta;
            }
        } catch (ResourceAccessException e) {
            // Manejo de errores específicos de acceso al recurso
            respuesta = manejarExcepcionAcceso(e);
            return respuesta;
        } catch (Exception e) {
            // Manejo de errores generales
            log.error("-----> Error general al enviar la solicitud: {}", e.getMessage());
            log.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa");
            return crearRespuestaError("Error inesperado: " + e.getMessage(), -500);
        }
    }

    // Metodo para crear un objeto RespuestaDTO en caso de error
    private RespuestaDTO crearRespuestaError(String mensaje, int codigo) {
        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setMensaje(mensaje);
        respuesta.setCodigo(codigo);
        respuesta.setData(null);
        return respuesta;
    }

    // Metodo para manejar ResourceAccessException y retornarlo como JSON
    private RespuestaDTO manejarExcepcionAcceso(ResourceAccessException e) {
        RespuestaDTO respuesta = new RespuestaDTO();
        Throwable cause = e.getCause();
        if (cause instanceof java.net.SocketTimeoutException) {
            log.error("-----> Error SocketTimeoutException: {}", e.getMessage());
            log.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa");
            respuesta.setMensaje("El servicio no respondió en el tiempo esperado.");
            respuesta.setCodigo(-501);
        } else if (cause instanceof java.net.ConnectException) {
            log.error("-----> Error ConnectException: {}", e.getMessage());
            log.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa");
            respuesta.setMensaje("No se pudo establecer conexión con el servicio.");
            respuesta.setCodigo(-502);
        } else {
            log.error("-----> Error de acceso al recurso: {}", e.getMessage());
            log.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa");
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
        log.info("[{}][{}][{}]:: [{}]", ip, puerto, servicio, urlRest);
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
}
