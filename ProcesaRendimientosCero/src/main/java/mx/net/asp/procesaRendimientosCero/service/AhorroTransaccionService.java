package mx.net.asp.procesaRendimientosCero.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dao.*;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.*;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresInversiones;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@Log4j2
@RequiredArgsConstructor
public class AhorroTransaccionService {

    private final AhorroDAO ahorroDAO;
    private final Gson gson;
    private final ErrorHandler errorHandler;
    private final AhorroDisposicionService ahorroDisposicionService;
    private final AhorroDepositoService ahorroDepositoService;

    //MOVIMIENTOS CERO
    @Value("${clave.retiro.traspaso.facil}")
    private String retiroTraspasoFacil;
    @Value("${clave.devolucion.retiro.traspaso.facil}")
    private String devolucionRetiroTraspasoFacil;

    //MOVIMIENTOS PROCREA
    @Value("${id.deposito.traspaso.ahorro}")
    private Integer depositoTraspasoAhorro;

    @Value("${dep.trans.codi.id}")
    private Integer depTransCoDiId;
    @Value("${dep.trans.codi.cve}")
    private String depTransCoDiCve;
    @Value("${ret.trans.codi.id}")
    private Integer retTransCoDiId;
    @Value("${ret.trans.codi.cve}")
    private String retTransCoDiCve;

    @Value("${id.banco.traspaso.cuentas}")
    private Integer idBancoTraspaso;
    @Value("${id.banco.devolucion}")
    private Integer idBancoDevolucion;

    public RespuestaDTO procesaDepositoRendimientoCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, Integer idMov) {
        RespuestaDTO respuesta = new RespuestaDTO();
        int idRetiro = 0;
        int idDeposito;
        String cve_movimiento = "";
        try {
            switch (idMov) {
                case 23:
                    cve_movimiento = "ASPRDN"; //TRANSFERENCIA RENDIMIENTOS (+)
                    break;
                case 24:
                    cve_movimiento = "ASPRPD"; //TRANSF. DE PLAZO FIJO - CTA A LA VISTA
                    break;
                default:
                    cve_movimiento = "ASPDE";
                    break;
            }

            respuesta = procesarDepositoCtaFacil(transferenciaCuentasOBJ, cve_movimiento, idBancoTraspaso);

            if (respuesta.getCodigo() != 0) {
                return respuesta;
            }

            idDeposito = Integer.parseInt(respuesta.getData());

            grabaTraspasoCuentas(transferenciaCuentasOBJ, idRetiro, idDeposito);

            return RespuestaUtils.respuestaExito(respuesta, respuesta.getData());
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    public RespuestaDTO procesaRetiroReinversionCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ) {
        RespuestaDTO respuesta = new RespuestaDTO();
        int idRetiro = 0;
        int idDeposito;

        try {
            respuesta = procesarRetiroCtaFacil(transferenciaCuentasOBJ, "ASPRPR", idBancoTraspaso);
            if (respuesta.getCodigo() != 0) {
                return respuesta;
            }
            idRetiro = Integer.parseInt(respuesta.getData());

            respuesta = procesarDepositoCtaInversion(transferenciaCuentasOBJ, 26);

            if (respuesta.getCodigo() != 0) {
                return procesarDevolucion(transferenciaCuentasOBJ, devolucionRetiroTraspasoFacil, idBancoDevolucion);
            }

            idDeposito = Integer.parseInt(respuesta.getData());

            grabaTraspasoCuentas(transferenciaCuentasOBJ, idRetiro, idDeposito);

            return RespuestaUtils.respuestaExito(respuesta, respuesta.getData());
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    private RespuestaDTO procesarDepositoCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        transferenciaCuentasOBJ.getDatos().setTipoTransaccionId(depTransCoDiId);
        transferenciaCuentasOBJ.getDatos().setTipoClave(depTransCoDiCve);

        return ahorroDepositoService.procesaDepositoCtaFacil(transferenciaCuentasOBJ, cve_movimiento, bancoId);
    }

    private RespuestaDTO procesarRetiroCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        transferenciaCuentasOBJ.getDatos().setTipoTransaccionId(retTransCoDiId);
        transferenciaCuentasOBJ.getDatos().setTipoClave(retTransCoDiCve);

        return ahorroDisposicionService.procesaDisposicionCtaFacil(transferenciaCuentasOBJ, cve_movimiento, bancoId);
    }

    private RespuestaDTO procesarDepositoCtaInversion(TransferenciaCuentasOBJ transferenciaCuentasOBJ, Integer idMovimiento) {
        return ahorroDepositoService.procesaDepositoAhorro(transferenciaCuentasOBJ, idMovimiento);
    }

    private RespuestaDTO procesarDevolucion(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        log.info("*** Se procede a hacer la devolucion a la cuenta origen: {}", transferenciaCuentasOBJ.getCuentaOrigen());
        transferenciaCuentasOBJ.getDatos().setTipoTransaccionId(depTransCoDiId);
        transferenciaCuentasOBJ.getDatos().setTipoClave(depTransCoDiCve);
        RespuestaDTO respuesta = new RespuestaDTO();

        respuesta = ahorroDepositoService.procesaDevolucionCtaFacil(transferenciaCuentasOBJ, cve_movimiento, bancoId);

//        if (esCuentaAhorro(transferenciaCuentasOBJ.getTipoCuentaOrigen())) {
//            respuesta = ahorroDepositoService.procesaDevolucionAhorro(transferenciaCuentasOBJ, idMovimiento, bancoId);
//        } else {
//            respuesta = ahorroDepositoService.procesaDevolucionCtaFacil(transferenciaCuentasOBJ, cve_movimiento, bancoId);
//        }

        if (respuesta.getCodigo() == 0) {
            RespuestaUtils.asignarError(respuesta,
                    ErroresInversiones.DEVOLUCION_CUENTA_ORIGEN,
                    ErroresInversiones.descError.get(ErroresInversiones.DEVOLUCION_CUENTA_ORIGEN));
        } else {
            log.error("*** No se logro hacer la devolucion a la cuenta de ahorro {}, monto {}",
                    transferenciaCuentasOBJ.getCuentaOrigen(), transferenciaCuentasOBJ.getMonto());
            RespuestaUtils.asignarError(respuesta,
                    ErroresInversiones.ERROR_DEVOLUCION_CUENTA_ORIGEN,
                    ErroresInversiones.descError.get(ErroresInversiones.ERROR_DEVOLUCION_CUENTA_ORIGEN));
        }

        return respuesta;
    }

    public RespuestaDTO grabaTraspasoCuentas(TransferenciaCuentasOBJ transferenciaCuentasOBJ, Integer idRetiro, Integer idDeposito) {
        RespuestaDTO respuesta = new RespuestaDTO();
        log.info("******** REGISTRA TRASPASO CUENTAS *********");
        try {
            String origen = transferenciaCuentasOBJ.getTipoCuentaOrigen().equals("AHORRO") ? "AHO" : "DEB";
            String destino = transferenciaCuentasOBJ.getTipoCuentaDestino().equals("AHORRO") ? "AHO" : "DEB";

            ahorroDAO.grabarTraspasoCuentas(origen + destino, transferenciaCuentasOBJ.getCuentaOrigen(), transferenciaCuentasOBJ.getCuentaDestino(), idRetiro, idDeposito, transferenciaCuentasOBJ.getMonto());
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    @NotNull
    public TransferenciaCuentasOBJ getTransferenciaCuentasOBJ(String ctaOrigen, String ctaDestino, Double monto, Double latitud,
                                                              Double longitud, boolean plasticoOrigen, boolean plasticoDestino, String idPersona, String tipoCuentaOrigen, String tipoCuentaDestino) {
        TransferenciaCuentasOBJ transferenciaCuentasOBJ = new TransferenciaCuentasOBJ();
        TransaccionesCuentasDatosOBJ datos = new TransaccionesCuentasDatosOBJ();
        transferenciaCuentasOBJ.setDatos(datos);
        transferenciaCuentasOBJ.setCuentaOrigen(ctaOrigen);
        transferenciaCuentasOBJ.setCuentaDestino(ctaDestino);
        transferenciaCuentasOBJ.setMonto(monto);
        transferenciaCuentasOBJ.setLatitud(latitud);
        transferenciaCuentasOBJ.setLongitud(longitud);
        //Origen es la cuenta de inversion
        transferenciaCuentasOBJ.setTienePlasticoOrigen(plasticoOrigen ? "SI" : "NO");
        transferenciaCuentasOBJ.setTienePlasticoDestino(plasticoDestino ? "SI" : "NO");
        transferenciaCuentasOBJ.setIdClienteOrigen(idPersona);
        transferenciaCuentasOBJ.setIdClienteDestino(idPersona);
        transferenciaCuentasOBJ.setTipoCuentaOrigen(tipoCuentaOrigen);
        transferenciaCuentasOBJ.setTipoCuentaDestino(tipoCuentaDestino);

        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));
        transferenciaCuentasOBJ.setConceptoOrigen(generaConceptoOrigen(transferenciaCuentasOBJ, formatoImporte));
        transferenciaCuentasOBJ.setConceptoDestino(generaConceptoDestino(transferenciaCuentasOBJ, formatoImporte));

        return transferenciaCuentasOBJ;
    }

    private String generaConceptoOrigen(TransferenciaCuentasOBJ transferenciaCuentasOBJ, NumberFormat formatoImporte) {
        return String.format(
                "TRANSFERENCIA ENVIADA A CTA. DE INVERSIÓN %s POR UN MONTO DE %s CTA. ORDENANTE %s REF. %s",
                transferenciaCuentasOBJ.getCuentaDestino(), formatoImporte.format(transferenciaCuentasOBJ.getMonto()),
                transferenciaCuentasOBJ.getCuentaOrigen(), transferenciaCuentasOBJ.getReferenciaNumerica()
        );
    }

    private String generaConceptoDestino(TransferenciaCuentasOBJ transferenciaCuentasOBJ, NumberFormat formatoImporte) {
        return String.format(
                "TRANSFERENCIA RECIBIDA DE SU CTA. DE INVERSIÓN %s POR UN MONTO DE %s CTA. ORDENANTE %s REF. %s",
                transferenciaCuentasOBJ.getCuentaOrigen(), formatoImporte.format(transferenciaCuentasOBJ.getMonto()),
                transferenciaCuentasOBJ.getCuentaOrigen(), transferenciaCuentasOBJ.getReferenciaNumerica()
        );
    }

    private boolean esCuentaAhorro(String tipoCuenta) {
        return "AHORRO".equals(tipoCuenta);
    }
}