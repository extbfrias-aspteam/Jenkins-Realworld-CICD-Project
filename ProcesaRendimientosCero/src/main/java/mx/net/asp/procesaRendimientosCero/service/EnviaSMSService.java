package mx.net.asp.procesaRendimientosCero.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dao.*;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.*;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import mx.net.asp.procesaRendimientosCero.ws.asp.MSEnviarSMS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class EnviaSMSService {

    private final SolicitanteDAO solicitanteDAO;
    private final MSEnviarSMS msEnviarSMS;
    private final ErrorHandler errorHandler;

    @Value("${operacion.notificacion.sms}")
    private String operacionCoDiSMS;


    @Async
    public CompletableFuture<RespuestaDTO> procesaSMSAbono(TransferenciaCuentasOBJ transferenciaCuentasOBJ) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            //SMS Destino
            respuesta = enviarSMS(transferenciaCuentasOBJ.getCuentaDestino(), "ABONO",
                    transferenciaCuentasOBJ.getMonto(), transferenciaCuentasOBJ.getIdClienteDestino(),
                    "SI".equalsIgnoreCase(transferenciaCuentasOBJ.getTienePlasticoDestino()));
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return CompletableFuture.completedFuture(respuesta);
    }

    @Async
    public CompletableFuture<RespuestaDTO> procesaSMSDevolucion(TransferenciaCuentasOBJ transferenciaCuentasOBJ) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            //SMS Destino
            respuesta = enviarSMS(transferenciaCuentasOBJ.getCuentaOrigen(), "DEVOLUCION",
                    transferenciaCuentasOBJ.getMonto(), transferenciaCuentasOBJ.getIdClienteOrigen(),
                    "SI".equalsIgnoreCase(transferenciaCuentasOBJ.getTienePlasticoOrigen()));
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return CompletableFuture.completedFuture(respuesta);
    }

    @Async
    public CompletableFuture<RespuestaDTO> procesaSMSCargo(TransferenciaCuentasOBJ transferenciaCuentasOBJ) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            //SMS Origen
            respuesta = enviarSMS(transferenciaCuentasOBJ.getCuentaOrigen(), "CARGO",
                    transferenciaCuentasOBJ.getMonto(), transferenciaCuentasOBJ.getIdClienteOrigen(),
                    "SI".equalsIgnoreCase(transferenciaCuentasOBJ.getTienePlasticoOrigen()));
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return CompletableFuture.completedFuture(respuesta);
    }

    public RespuestaDTO enviarSMS(String cuenta, String tipoMov, Double monto, String personaId, Boolean plastico) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            if (Boolean.TRUE.equals(plastico)) {
                log.info("Se omite el envio de SMS debido a que la cuenta {} tiene plastico asociado", cuenta);

                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(null);
                return respuesta;
            }
            // Procesar el envío de SMS
            respuesta = procesarEnvioSMS(cuenta, tipoMov, monto, personaId);
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    private RespuestaDTO procesarEnvioSMS(String cuenta, String tipoMov, Double monto, String personaId) {
        RespuestaDTO respuesta = new RespuestaDTO();
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));
        String mensaje;
        String celular;
        try {
            mensaje = generaSMSTransaccion(cuenta, tipoMov, monto, formatoImporte);
            //obtenemos el celular del cliente
            SolicitanteOBJ solicitanteOBJ = solicitanteDAO.obtenerInfoBasicaSolicitanteById(personaId);
            if (solicitanteOBJ != null) {
                celular = solicitanteOBJ.getTelefonoCoDi();
            } else {
                log.info("No fue posible obtener el celular del cliente {}", personaId);
                respuesta.setCodigo(-100);
                respuesta.setMensaje("No fue posible obtener el celular del cliente " + personaId);
                respuesta.setData(null);
                return respuesta;
            }
            log.info("Se enviara SMS de {} a {}", tipoMov, celular);
            msEnviarSMS.enviarSMS(celular, mensaje, personaId, operacionCoDiSMS);
            respuesta.setCodigo(0);
            respuesta.setMensaje("OK");
            respuesta.setData(null);
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    private String generaSMSTransaccion(String cuenta, String tipoMovimiento, Double monto, NumberFormat formatoImporte) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fecha = LocalDateTime.now().format(formatter);
        return String.format(
                "%s a su cuenta ******%s por %s M.N. el %s CONTACTO: 8004627373",
                tipoMovimiento, cuenta.substring(cuenta.length() - 4), formatoImporte.format(monto),
                fecha
        );
    }
}