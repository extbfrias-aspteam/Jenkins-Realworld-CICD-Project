package mx.net.asp.asp_pago_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoUpgradeAccountService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class AspPagoUpgradeAccountController {

    private AspPagoUpgradeAccountService aspPagoUpgradeAccountService;
    private ErrorHandler errorHandler;
    @Value("${ms.services.consultar.ocupaciones}")
    private String pathConsultarOcupaciones;
    @Value("${ms.services.consultar.actividades}")
    private String pathConsultarActividades;
    @Value("${ms.services.consultar.objetivos}")
    private String pathConsultarObjetivos;
    @Value("${ms.services.consultar.ingresos.mensuales}")
    private String pathConsultarIngresosMensuales;
    @Value("${ms.services.consultar.frecuencia}")
    private String pathConsultarFrecuencia;
    @Value("${ms.services.consultar.ahorros.mensuales}")
    private String pathConsultarAhorrosMensuales;

    public AspPagoUpgradeAccountController(
            AspPagoUpgradeAccountService aspPagoUpgradeAccountService,
            ErrorHandler errorHandler) {
        this.aspPagoUpgradeAccountService = aspPagoUpgradeAccountService;
        this.errorHandler = errorHandler;
    }

    @Operation(summary = "Servicio para consultar el catalogo de ocupaciones para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarOcupaciones", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarOcupaciones() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarOcupaciones);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de objetivos para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarObjetivos", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarObjetivos() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarObjetivos);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de ingresos mensuales para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarIngresosMensuales", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarIngresosMensuales() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarIngresosMensuales);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de frecuencia para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarFrecuencia", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarFrecuencia() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarFrecuencia);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de ahorros mensuales para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarAhorrosMensuales", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarAhorrosMensuales() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarAhorrosMensuales);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de actividades para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarActividades", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarActividades() {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(pathConsultarActividades);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el catalogo de giros por actividad para formulario de PLD en Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarGirosByActividadId", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarGirosByActividadId(@Valid @RequestParam String actividadId) {
        String respuesta = "";
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("actividadId", actividadId);
            respuesta = aspPagoUpgradeAccountService.consultarCatalogos(queryParams);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar si hay una sesión activa de Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/consultarSesionIncode", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> consultarSesionIncode(@Valid @RequestParam("cuentaah") String cuentaah) {
        String respuesta = "";
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("cuentaah", cuentaah);
            respuesta = aspPagoUpgradeAccountService.consultarSesionIncode(queryParams);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para realizar la petición del cambio de contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping(value = "/actualizarSesionIncode", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> actualizarSesionIncode(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.actualizarSesionIncode(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para finalizar una sesión de incode de Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping(value = "/finalizarSesionIncode", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> finalizarSesionIncode(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.finalizarSesionIncode(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para guardar una sesión de incode de Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/guardarSesionIncode", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> guardarSesionIncode(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.guardarSesionIncode(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para subir de nivel una cuenta a Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/subirNivelCuentaRemota", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> subirNivelCuentaRemota(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.subirNivelCuentaRemota(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para validar el curp en el proceso de Cuentas remotas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/validaCurpCuenta", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validaCurpCuenta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.validaCurpCuenta(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para guardar el formulario de PLD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/guardarFormulario", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> guardarFormulario(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.guardarFormulario(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para obtener el cambio de estado de una sesión de incode (Webhook)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/finalizarSesionIncodeManual", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> finalizarSesionIncodeManual(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = aspPagoUpgradeAccountService.finalizarSesionIncodeManual(request);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }
}
