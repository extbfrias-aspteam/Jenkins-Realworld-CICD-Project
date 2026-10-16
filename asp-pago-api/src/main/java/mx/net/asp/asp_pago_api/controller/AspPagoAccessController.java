package mx.net.asp.asp_pago_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.model.CambioPassword;
import mx.net.asp.asp_pago_api.model.TokenPassword;
import mx.net.asp.asp_pago_api.service.AspPagoAccessService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AspPagoAccessController {

    private final AspPagoAccessService aspPagoAccessService;
    private final ErrorHandler errorHandler;

    public AspPagoAccessController(
            AspPagoAccessService aspPagoAccessService,
            ErrorHandler errorHandler) {
        this.aspPagoAccessService = aspPagoAccessService;
        this.errorHandler = errorHandler;
    }

    @Operation(summary = "Servicio para realizar la petición del cambio de contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/peticionCambioPassV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> peticionCambioPassV2(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.peticionCambioPassV2(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para realizar el cambio de contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/cambioPasswordV2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaDTO> cambioPasswordV2(@RequestBody CambioPassword request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = aspPagoAccessService.cambioPasswordV2(request.getRequest());
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuestaDTO);
    }

    @Operation(summary = "Servicio para realizar el login de la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/logInV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> logInV3(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.logInV3(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para validar el código de Autorización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/validarCodigoAutorizacion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validarCodigoAutorizacion(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.validarCodigoAutorizacion(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para cambiar el código de Autorización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/cambiarCodigoAutorizacion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> cambiarCodigoAutorizacion(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.cambiarCodigoAutorizacion(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para validar la versión de la app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/validaVersionApp", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validaVersionApp(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.validaVersionApp(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para realizar el cierre de sesion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/cierraSesion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> cierraSesion(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.cierraSesion(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para obtener los datos iniciales que requiere la app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/obtenerDatosInicialesV", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obtenerDatosIniciales(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoAccessService.obtenerDatosIniciales(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para validar el token de cambio de password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/validarTokenCambioPass", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaDTO> validarTokenCambioPass(@RequestBody TokenPassword request) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            respuestaDTO = aspPagoAccessService.validarTokenCambioPass(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuestaDTO);
    }
}
