package mx.net.asp.asp_pago_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoJwtService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AspPagoJwtController {

    private final AspPagoJwtService aspPagoJwtService;
    private final ErrorHandler errorHandler;

    public AspPagoJwtController(
            AspPagoJwtService aspPagoJwtService,
            ErrorHandler errorHandler) {
        this.aspPagoJwtService = aspPagoJwtService;
        this.errorHandler = errorHandler;
    }

    @Operation(summary = "Servicio para generar jwt de autenticación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/login-auth", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loginAuth(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoJwtService.loginAuth(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para generar nuevo access token a partir de refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/refresh-token", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> refreshToken(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoJwtService.refreshToken(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }
}
