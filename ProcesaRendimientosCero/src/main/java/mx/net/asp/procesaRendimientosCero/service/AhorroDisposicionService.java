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
import mx.net.asp.procesaRendimientosCero.ws.asp.WSRetiraTarjetaOrquestador;
import mx.net.asp.procesaRendimientosCero.ws.asp.request.TransaccionTarjetaOrquestadorReq;
import mx.net.asp.procesaRendimientosCero.ws.asp.response.TransaccionTarjetaOrquestadorResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AhorroDisposicionService {

    private final MovimientosCajaDAO movimientosCajaDAO;
    private final ProcreaDAO procreaDAO;
    private final Gson gson;
    private final ErrorHandler errorHandler;
    private final WSRetiraTarjetaOrquestador wsRetiraTarjetaOrquestador;

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

    public RespuestaDTO procesaDisposicionCtaFacil(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            log.info("cve_movimiento:: {}", cve_movimiento);
            TransaccionTarjetaOrquestadorReq reqTra = getTransaccionTarjetaOrquestadorReq(transferenciaCuentasOBJ, cve_movimiento, bancoId);

            respuesta = wsRetiraTarjetaOrquestador.retiraTarjetaOrquestador(reqTra);

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

    @NotNull
    private TransaccionTarjetaOrquestadorReq getTransaccionTarjetaOrquestadorReq(TransferenciaCuentasOBJ transferenciaCuentasOBJ, String cve_movimiento, Integer bancoId) {
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
        reqTra.setConcepto(transferenciaCuentasOBJ.getConceptoOrigen());
        reqTra.setObservaciones(transferenciaCuentasOBJ.getConceptoOrigen());
        reqTra.setClave_rastreo(transferenciaCuentasOBJ.getClaveRastreo());
        reqTra.setReferenciaNumerica(transferenciaCuentasOBJ.getReferenciaNumerica());
        reqTra.setClaveMovimiento(cve_movimiento);
        reqTra.setHeader(header);
        return reqTra;
    }
}