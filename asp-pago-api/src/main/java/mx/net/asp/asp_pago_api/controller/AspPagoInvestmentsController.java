package mx.net.asp.asp_pago_api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoInvestmentsService;
import mx.net.asp.asp_pago_api.service.AspPagoManagementService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;

@Log4j2
@RestController
public class AspPagoInvestmentsController {

	private final AspPagoInvestmentsService aspPagoInvestmentsService;
	private final ErrorHandler errorHandler;

	public AspPagoInvestmentsController(AspPagoInvestmentsService aspPagoInvestmentsService,
			ErrorHandler errorHandler) {
		this.aspPagoInvestmentsService = aspPagoInvestmentsService;
		this.errorHandler = errorHandler;
	}

	@Operation(summary = "Servicio para consultar la lista de modalidades")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerModalidades", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerModalidades() {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerModalidades();
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}

	@Operation(summary = "Servicio para obtener plazos y tazas de inversiones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerPlazosPorcentajes", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerPlazosPorcentajes(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerPlazosPorcentajes(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}

	@Operation(summary = "Servicio para obtener tipos de reinversiones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerTiposReinversiones", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerTiposReinversiones(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerTiposReinversiones(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}

	@Operation(summary = "Servicio para obtener las inversiones de una cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerInversiones", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerInversiones(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerInversiones(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio para obtener el detallado de una inversion")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerDetalleInversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerDetalleInversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerDetalleInversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio para dar de alta una cuenta de inversión")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/altaCuentaInversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> altaCuentaInversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.altaCuentaInversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio para obtener las inversiones de una cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/obtenerMovimientosInversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> obtenerMovimientosInversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.obtenerMovimientosInversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio Operar Reinversiones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/altaReinversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> altaReinversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.altaReinversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio Cancelar Reinversiones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/cancelarReinversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> cancelarReinversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.cancelarReinversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio para Simular inversión")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/simularInversion", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> simularInversion(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.simularInversion(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}
	
	@Operation(summary = "Servicio para envío Estado de Cuenta por Correo")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@PostMapping(value = "/enviarEstadoCuenta", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> enviarEstadoCuenta(@RequestBody String request) {
		String respuesta = "";
		try {
			respuesta = aspPagoInvestmentsService.enviarEstadoCuenta(request);
		} catch (Exception e) {
			errorHandler.handleException(e);
		}
		return ResponseEntity.ok(respuesta);
	}

}
