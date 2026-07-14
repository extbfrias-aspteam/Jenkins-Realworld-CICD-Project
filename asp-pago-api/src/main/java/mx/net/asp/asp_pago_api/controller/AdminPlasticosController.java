package mx.net.asp.asp_pago_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AdminPlasticosService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AdminPlasticosController {

    private final AdminPlasticosService adminPlasticosService;
    private final ErrorHandler errorHandler;
    @Value("${ms.admin.plasticos.services.consultar.tarjeta}")
    private String pathConsultarTarjeta;
    @Value("${ms.admin.plasticos.services.asignar.tarjeta.fisica}")
    private String pathAsignartarjetaFisica;
    @Value("${ms.admin.plasticos.services.bloquear.tarjeta}")
    private String pathBloquearTarjeta;
    @Value("${ms.admin.plasticos.services.reasignar.tarjeta}")
    private String pathReasignarTarjeta;
    @Value("${ms.admin.plasticos.services.asignar.otra.tarjeta}")
    private String pathAsignarOtraTarjeta;
    @Value("${ms.admin.plasticos.services.asignar.cuenta}")
    private String pathAsignarCuenta;
    @Value("${ms.admin.plasticos.services.solicitar.reposicion}")
    private String pathSolicitarReposicion;
    @Value("${ms.admin.plasticos.services.desbloquear.tarjeta}")
    private String pathDesbloquearTarjeta;
    @Value("${ms.admin.plasticos.services.consultar.detalle.movimiento}")
    private String pathConsultarDetalleMovimiento;
    @Value("${ms.admin.plasticos.services.consultar.movimientos.asp-pago}")
    private String pathConsultarDetalleMovimientoAspPago;
    @Value("${ms.admin.plasticos.services.solicitar.tarjeta.fisica}")
    private String pathSolicitarTarjetaFisica;
    @Value("${ms.admin.plasticos.services.vincular.tarjeta.fisica}")
    private String pathVincularTarjetaFisica;
    @Value("${ms.admin.plasticos.services.consultar.cp.entrega}")
    private String pathConsultarCpEntrega;
    @Value("${ms.admin.plasticos.services.consultar.mi.solicitud.tf}")
    private String pathConsultarMiSolicitudTf;
    @Value("${ms.admin.plasticos.services.solicitar.reposicion.tarjeta.fisica}")
    private String pathSolicitarReposicionTarjetaFisica;
    @Value("${ms.admin.plasticos.services.consultar.cvv.dinamico}")
    private String pathConsultarCvvDinamico;
    @Value("${ms.admin.plasticos.services.asignar.nip}")
    private String pathAsignarNip;

    public AdminPlasticosController(
            AdminPlasticosService adminPlasticosService,
            ErrorHandler errorHandler) {
        this.adminPlasticosService = adminPlasticosService;
        this.errorHandler = errorHandler;
    }

    @Operation(summary = "Servicio para consultar tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultaTarjeta")
    public ResponseEntity<String> consultaTarjeta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarTarjeta, request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para asignar una tarjeta física")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/asignarTarjetaFisica")
    public ResponseEntity<String> asignarTarjetaFisica(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathAsignartarjetaFisica, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para bloquear una tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/bloquearTarjeta")
    public ResponseEntity<String> bloquearTarjeta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathBloquearTarjeta, request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para re asignar una tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/reasignarTarjeta")
    public ResponseEntity<String> reasignarTarjeta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathReasignarTarjeta, request,true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para asignar otra tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/asignarOtraTarjeta")
    public ResponseEntity<String> asignarOtraTarjeta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathAsignarOtraTarjeta, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para asignar una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/asignarCuenta")
    public ResponseEntity<String> asignarCuenta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathAsignarCuenta, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para solicitar reposición de tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/solicitarReposicion")
    public ResponseEntity<String> solicitarReposicion(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathSolicitarReposicion, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para desbloquear tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/desbloquearTarjeta")
    public ResponseEntity<String> desbloquearTarjeta(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathDesbloquearTarjeta, request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar el detalle del movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarDetalleMovimiento")
    public ResponseEntity<String> consultarDetalleMovimiento(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarDetalleMovimiento, request,
                    false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar los movimientos de la tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarMovimientos")
    public ResponseEntity<String> consultarMovimientos(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.consultaMovimientos(request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar los movimientos Asp Pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarMovimientosAspPago")
    public ResponseEntity<String> consultarMovimientosAspPago(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarDetalleMovimientoAspPago, request,
                    true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para solicitar una tarjeta física")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/solicitarTarjetaFisica")
    public ResponseEntity<String> solicitarTarjetaFisica(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathSolicitarTarjetaFisica, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para vincular una tarjeta física")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/vincularTarjetaFisica")
    public ResponseEntity<String> vincularTarjetaFisica(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathVincularTarjetaFisica, request, true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar CP de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarCPEntrega")
    public ResponseEntity<String> consultarCPEntrega(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarCpEntrega, request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar mi solicitud de TF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarMiSolicitudTF")
    public ResponseEntity<String> consultarMiSolicitudTF(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarMiSolicitudTf, request, false);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar mi solicitud de TF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/solicitarReposicionTarjetaFisica")
    public ResponseEntity<String> solicitarReposicionTarjetaFisica(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathSolicitarReposicionTarjetaFisica, request,
                    true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para consultar mi CVV Dinamico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/consultarCVVDinamico")
    public ResponseEntity<String> consultarCVVDinamico(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathConsultarCvvDinamico, request,
                    true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Servicio para asignar NIP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/asignarNIP")
    public ResponseEntity<String> asignarNIP(@RequestBody String request) {
        String respuesta = "";
        try {
            respuesta = adminPlasticosService.procesarPeticion(pathAsignarNip, request,
                    true);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return ResponseEntity.ok(respuesta);
    }

}
