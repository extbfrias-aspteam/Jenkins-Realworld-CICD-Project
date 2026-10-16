package mx.net.asp.asp_pago_api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Log4j2
@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String datosErrorMessage;

    public GlobalExceptionHandler() {
        datosErrorMessage = "Faltan datos obligatorios.";
    }

    // Método auxiliar para generar la respuesta
    private ResponseEntity<RespuestaDTO> generarRespuesta(int codigo, String mensaje, List<String> errores) throws JsonProcessingException {
        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setCodigo(codigo);
        respuesta.setMensaje(mensaje);
        respuesta.setData(objectMapper.writeValueAsString(errores));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON) // Fuerza JSON en la respuesta
                .body(respuesta);
    }

    // Método para procesar errores comunes
    private void procesarErroresGenerales(List<? extends ObjectError> errores, List<String> camposObligatorios, List<String> otrosErrores) {
        for (ObjectError error : errores) {
            String fieldName;
            String errorMessage;

            if (error instanceof FieldError fieldError) {
                fieldName = fieldError.getField().contains(".")
                        ? fieldError.getField().substring(fieldError.getField().lastIndexOf(".") + 1)
                        : fieldError.getField();
                errorMessage = fieldError.getDefaultMessage();
            } else {
                // Procesar otro tipo de errores si es necesario
                continue;
            }

            if ("obligatorio".contains(errorMessage)) { // NotNull o NotBlank
                camposObligatorios.add(fieldName);
            } else { // Otras validaciones como @Size, @Pattern, etc.
                otrosErrores.add(fieldName + ": " + errorMessage);
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<RespuestaDTO> handleValidationExceptions(MethodArgumentNotValidException ex) throws JsonProcessingException {
        List<String> camposObligatorios = new ArrayList<>();
        List<String> otrosErrores = new ArrayList<>();

        procesarErroresGenerales(ex.getBindingResult().getAllErrors(), camposObligatorios, otrosErrores);

        if (!otrosErrores.isEmpty()) {
            return generarRespuesta(-301, "No se cumplen las condiciones del atributo.", otrosErrores);
        } else {
            return generarRespuesta(-300, datosErrorMessage, camposObligatorios);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<RespuestaDTO> handleConstraintViolationException(ConstraintViolationException ex) throws JsonProcessingException {
        List<String> camposObligatorios = new ArrayList<>();
        List<String> otrosErrores = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            if (fieldName.contains(".")) {
                String[] fieldParts = fieldName.split("\\.");
                fieldName = fieldParts[fieldParts.length - 1]; // Tomamos el último segmento
            }

            String errorMessage = violation.getMessage();
            if (errorMessage.contains("obligatorio")) { // NotNull o NotBlank
                camposObligatorios.add(fieldName);
            } else { // Otras validaciones
                otrosErrores.add(fieldName + ": " + errorMessage);
            }
        }

        if (!otrosErrores.isEmpty()) {
            return generarRespuesta(-301, "No se cumplen las condiciones del atributo.", otrosErrores);
        } else {
            return generarRespuesta(-300, datosErrorMessage, camposObligatorios);
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<RespuestaDTO> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) throws JsonProcessingException {
        List<String> faltantes = Collections.singletonList(ex.getParameterName());
        log.error("Parametro faltante: {}", ex.getParameterName());
        return generarRespuesta(-300, datosErrorMessage, faltantes);
    }
}
