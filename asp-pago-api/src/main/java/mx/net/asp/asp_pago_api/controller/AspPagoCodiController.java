package mx.net.asp.asp_pago_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoCodiService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AspPagoCodiController {

    private final AspPagoCodiService aspPagoCodiService;
    private final ErrorHandler errorHandler;

    public AspPagoCodiController(
            AspPagoCodiService aspPagoCodiService,
            ErrorHandler errorHandler) {
        this.aspPagoCodiService = aspPagoCodiService;
        this.errorHandler = errorHandler;
    }

    @Operation(summary = "Servicio para el registro inicial de CODI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/registroInicialV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registroInicialV2(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.registroInicialV2(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para el registro subsecuente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/registroSubsecuenteV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registroSubsecuenteV2(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.registroSubsecuenteV2(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para el registro de la app por omisión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/registraAppPorOmisionV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registraAppPorOmisionV2(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.registraAppPorOmisionV2(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para el registro de cuentas CODI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/validacionDeCuentasV2", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validacionDeCuentasV2(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.validacionDeCuentasV2(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para el consultar el estatus Codi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/consultarEstatusCoDi", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarEstatusCoDi(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.consultarOperacionesCoDi(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para el obtener referencia de serial de cobro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/obtenerReferenciaSerialCobro", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obtenerReferenciaSerialCobro(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.obtenerReferenciaSerialCobro(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para registrar bitacora codi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/registrarBitacoraCodi", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> registrarBitacoraCodi(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoCodiService.registrarBitacoraCodi(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }
}
