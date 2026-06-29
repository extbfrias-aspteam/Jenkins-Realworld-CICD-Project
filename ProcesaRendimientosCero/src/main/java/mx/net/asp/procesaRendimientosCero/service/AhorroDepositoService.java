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
import mx.net.asp.procesaRendimientosCero.ws.asp.WSFondeaTarjetaOrquestador;
import mx.net.asp.procesaRendimientosCero.ws.asp.request.TransaccionTarjetaOrquestadorReq;
import mx.net.asp.procesaRendimientosCero.ws.asp.response.TransaccionTarjetaOrquestadorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Service
@Log4j2
@RequiredArgsConstructor
public class AhorroDepositoService {

    private final MovimientosCajaDAO movimientosCajaDAO;
    private final ProcreaDAO procreaDAO;
    private final Gson gson;
    private final ErrorHandler errorHandler;
    private final WSFondeaTarjetaOrquestador wsFondeaTarjetaOrquestador;

    @Value("${ip.host.transaccion}")
    private String ipHost;
    @Value("${medio.caja.pago.asp}")
    private String medioCajaPago;

    @Value("${usuario.dep.trans.ahorro}")
    private Integer usuarioDepTransAhorro;
    @Value("${caja.dep.trans.ahorro}")
    private Integer cajaDepTransAhorro;
    @Value("${forma.pago.disp.trans.ahorro}")
    private Integer formaPagoDsipTransAhorro;

    public RespuestaDTO procesaDepositoCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            log.info("cve_movimiento:: {}", cve_movimiento);

            TransaccionTarjetaOrquestadorReq reqTra = new TransaccionTarjetaOrquestadorReq();
            HeaderWS header = new HeaderWS();
            header.setIdEmpresa(1L);
            header.setIdCanalAtencion(2L);
            header.setIdSucursal(1L);
            header.setIdUsuario(9L);
            header.setLatitud(transferenciaCuentasOBJ.getLatitud());
            header.setLongitud(transferenciaCuentasOBJ.getLongitud());
            header.setIpHost(ipHost);
            header.setIdBanco(String.valueOf(bancoId));
            header.setIdTransaccion(transferenciaCuentasOBJ.getDatos().getTipoTransaccionId());
            reqTra.setCuenta(transferenciaCuentasOBJ.getCuentaDestino());
            reqTra.setImporte(String.valueOf(transferenciaCuentasOBJ.getMonto()));
            reqTra.setMedioPago(medioCajaPago);
            reqTra.setConcepto(transferenciaCuentasOBJ.getConceptoDestino());
            reqTra.setObservaciones(transferenciaCuentasOBJ.getConceptoDestino());
            reqTra.setClave_rastreo(transferenciaCuentasOBJ.getClaveRastreo());
            reqTra.setReferenciaNumerica(transferenciaCuentasOBJ.getReferenciaNumerica());
            reqTra.setClaveMovimiento(cve_movimiento);
            reqTra.setHeader(header);

            respuesta = wsFondeaTarjetaOrquestador.fondeaTarjetaOrquestador(reqTra);

            if (respuesta.getCodigo() == 0) {
                TransaccionTarjetaOrquestadorResponse trxResponse = gson.fromJson(respuesta.getData(), TransaccionTarjetaOrquestadorResponse.class);
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(String.valueOf(trxResponse.getMovimientoId()));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    public RespuestaDTO procesaDevolucionCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            log.info("cve_movimiento:: {}", cve_movimiento);

            NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));
            String conceptoDevolucion = generaConceptoDevolucion(transferenciaCuentasOBJ, formatoImporte);

            TransaccionTarjetaOrquestadorReq reqTra = new TransaccionTarjetaOrquestadorReq();
            HeaderWS header = new HeaderWS();
            header.setIdEmpresa(1L);
            header.setIdCanalAtencion(2L);
            header.setIdSucursal(1L);
            header.setIdUsuario(9L);
            header.setLatitud(transferenciaCuentasOBJ.getLatitud());
            header.setLongitud(transferenciaCuentasOBJ.getLongitud());
            header.setIpHost(ipHost);
            header.setIdBanco(String.valueOf(bancoId));
            header.setIdTransaccion(transferenciaCuentasOBJ.getDatos().getTipoTransaccionId());
            reqTra.setCuenta(transferenciaCuentasOBJ.getCuentaOrigen());
            reqTra.setImporte(String.valueOf(transferenciaCuentasOBJ.getMonto()));
            reqTra.setMedioPago(medioCajaPago);
            reqTra.setConcepto(conceptoDevolucion);
            reqTra.setObservaciones(conceptoDevolucion);
            reqTra.setClave_rastreo(transferenciaCuentasOBJ.getClaveRastreo());
            reqTra.setReferenciaNumerica(transferenciaCuentasOBJ.getReferenciaNumerica());
            reqTra.setClaveMovimiento(cve_movimiento);
            reqTra.setHeader(header);

            respuesta = wsFondeaTarjetaOrquestador.fondeaTarjetaOrquestador(reqTra);

            if (respuesta.getCodigo() == 0) {
                TransaccionTarjetaOrquestadorResponse trxResponse = gson.fromJson(respuesta.getData(), TransaccionTarjetaOrquestadorResponse.class);
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(String.valueOf(trxResponse.getMovimientoId()));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    public RespuestaDTO procesaDepositoAhorro(TransferenciaCuentasOBJ transferenciaCuentasOBJ, Integer movId) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            Integer resultado = movimientosCajaDAO.ejecutarDepositoAhorro(1, LocalDate.now(), 9, transferenciaCuentasOBJ.getCuentaDestino(),
                    transferenciaCuentasOBJ.getMonto(), transferenciaCuentasOBJ.getConceptoDestino(), movId);
            if (resultado <= 0) {
                RespuestaUtils.asignarError(respuesta,
                        ErroresGenerales.ERROR_INTERNO,
                        ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
            } else
                RespuestaUtils.respuestaExito(respuesta, String.valueOf(resultado));
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO, ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    private static String generaConceptoDevolucion(TransferenciaCuentasOBJ transferenciaCuentasOBJ, NumberFormat formatoImporte) {
        return String.format(
                "DEVOLUCION TRANSFERENCIA ENVIADA A CTA. DE INVERSIÓN %s POR UN MONTO DE %s CTA. ORDENANTE %s REF. %s",
                transferenciaCuentasOBJ.getCuentaDestino(), formatoImporte.format(transferenciaCuentasOBJ.getMonto()),
                transferenciaCuentasOBJ.getCuentaOrigen(), transferenciaCuentasOBJ.getReferenciaNumerica()
        );
    }
}