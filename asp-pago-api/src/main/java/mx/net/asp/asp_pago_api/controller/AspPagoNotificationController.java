package mx.net.asp.asp_pago_api.controller;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.dto.ServiceResponse;
import mx.net.asp.asp_pago_api.service.AspPagoNotificationService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
import mx.net.asp.asp_pago_api.ws.asp.request.EnviaSMSReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
public class AspPagoNotificationController {

    private final AspPagoNotificationService aspPagoNotificationService;
    private final ErrorHandler errorHandler;
    private final Gson gson;

    public AspPagoNotificationController(
            AspPagoNotificationService aspPagoNotificationService,
            ErrorHandler errorHandler,
            Gson gson) {
        this.aspPagoNotificationService = aspPagoNotificationService;
        this.errorHandler = errorHandler;
        this.gson = gson;
    }

    @Operation(summary = "Servicio para generar el envío de email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/enviarEmail", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> enviarEmail(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoNotificationService.enviarEmail(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para generar el envío de sms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/enviarSMS", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> enviarSMS(@RequestBody EnviaSMSReq req) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            ServiceResponse serviceResponse = aspPagoNotificationService.enviarSMS(req);
            return ResponseEntity.status(serviceResponse.getStatus()).body(serviceResponse.getBody());
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(respuesta));
        }
    }

    @Operation(summary = "Servicio para generar el envío de sms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/enviarEmailAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> enviarEmailAttachment(@RequestParam @NotBlank(message = "El campo 'destinatario' es obligatorio") String destinatario,
                                                        @RequestParam @NotBlank(message = "El campo 'asunto' es obligatorio") String asunto,
                                                        @RequestParam @NotBlank(message = "El campo 'contenido' es obligatorio") String contenido,
                                                        @RequestParam(required = false) List<MultipartFile> archivos) {
        String respuesta = "";
        try {
            respuesta = aspPagoNotificationService.enviarEmailAttachment(destinatario, asunto, contenido, archivos);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }
}
