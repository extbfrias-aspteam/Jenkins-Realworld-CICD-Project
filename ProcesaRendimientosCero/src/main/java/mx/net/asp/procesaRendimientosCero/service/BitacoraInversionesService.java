package mx.net.asp.procesaRendimientosCero.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dao.InversionesDAO;
import mx.net.asp.procesaRendimientosCero.model.BitacoraInversionesOBJ;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class BitacoraInversionesService {

    private final InversionesDAO inversionesDAO;
    private final Environment env;
    private final ErrorHandler errorHandler;

    public Long registraBitacoraInversiones(BitacoraInversionesOBJ bitacoraOBJ) {
        try {
            return inversionesDAO.registraBitacoraInversiones(bitacoraOBJ);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return 0L;
    }

    @Async
    public CompletableFuture<Void> registraBitacoraInversionesAsync(BitacoraInversionesOBJ bitacoraOBJ) {
        try {
            inversionesDAO.registraBitacoraInversiones(bitacoraOBJ);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            errorHandler.handleException(e);
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    @Async
    public CompletableFuture<Void> actualizaIdBitacoraInversionesAsync(Long id) {
        try {
            inversionesDAO.actualizaIdBitacoraInversiones(id);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            errorHandler.handleException(e);
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    @NotNull
    public BitacoraInversionesOBJ getBitacoraInversionesOBJ(String valorReferencia, String evento, String cveTipo, String mensaje, Integer codigo, Long idProcesoBit) {
        BitacoraInversionesOBJ bitacoraOBJ = new BitacoraInversionesOBJ();
        bitacoraOBJ.setClaveEvento(evento);
        bitacoraOBJ.setObservaciones(mensaje);
        bitacoraOBJ.setCodigo(codigo);
        bitacoraOBJ.setValorReferencia(valorReferencia);
        bitacoraOBJ.setTipoReferencia(cveTipo);
        bitacoraOBJ.setIdProcesoBitacora(idProcesoBit);
        return bitacoraOBJ;
    }
}
