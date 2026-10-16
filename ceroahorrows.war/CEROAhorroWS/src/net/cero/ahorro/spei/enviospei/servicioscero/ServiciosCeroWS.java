package net.cero.ahorro.spei.enviospei.servicioscero;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.servicioscero.base.BaseServicioWS;
import net.cero.data.Respuesta;
import net.cero.data.nuevospei.*;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.ws.data.Errores;
import net.cero.req.general.HeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@AllArgsConstructor
public class ServiciosCeroWS extends BaseServicioWS {
    private final ServicioConsultaSaldoCero servicioConsultaSaldoCero;
    private final ServiciosWS serviciosWS;
    private final ServSWRetiroCero servSWRetiroCero;

    public String procesoRetirar(String tipo, Map<String,Object> datos,
        String bancoDescSpeiDestino,String nombreBenefSpeiDestino, String clabeSpeiDestino,
                                  Double montoRetirar,String t_noCuenta,
                                 HeaderWS header,Long bancoId,String claveRastreo,Integer idSpei,String claveMovimientoDock) {

        String desc = "RETIRO";
        Boolean cancelacion = false;
        String trx = "RET";
        Boolean enviaSMS = false;
        String msgSMS = "";
        switch (tipo) {
            case "SPEI":
                trx = trx + "_TRANS";
                desc += String.format(" PAGO ENVIADO A %s PARA %s CTA. BENEFICIARIA %s REF. %s CLAVE DE RASTREO: %s",
                        bancoDescSpeiDestino,
                        nombreBenefSpeiDestino,
                        clabeSpeiDestino,
                        datos.get("REFERENCIA"),
                        datos.get("CVE_RASTREO"));
                break;
            case "RET":
                enviaSMS = true;
        }

        // ToDo: PROCESO DE COBRO COMISIONES POR RETIRO
        Double comisiones = getComisionesPorRetiro(t_noCuenta);

        try {
            montoRetirar = montoRetirar - comisiones;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            RespuestaSVC respRetiro = servSWRetiroCero.servSWRetiroCeroOperaciones(t_noCuenta, ToolsR._D(montoRetirar),
                    sdf.format(Calendar.getInstance().getTime()), trx, header, "",
                    desc, cancelacion, bancoId,claveRastreo,idSpei.toString(),claveMovimientoDock);
            if (respRetiro.getErrores().getCodigoError() != 0) {
                return respRetiro.getErrores().getDescError();
            }


        } catch (Exception ex) {
            log.error(ex);
            return "ERROR";
        }
        return "true";
    }

    public Double getComisionesPorRetiro(String cuenta) {
        Double comisiones = 0.00d;
        List<ComisionPendienteOBJ> lstComisionPendiente = serviciosWS.buscarComisionPendiente("buscarComisionPendiente",
                cuenta);
        if (lstComisionPendiente != null) {
            comisiones = lstComisionPendiente.get(0).getPendientes();
        }
        return comisiones == null ? 0.00d : comisiones;
    }

    public Double metodoConsultaSaldoCuenta(String cuenta) {
        RespuestaSVC respSaldoCuenta = servicioConsultaSaldoCero.consultaSaldoCero(cuenta);
        if (respSaldoCuenta.getErrores().getCodigoError() != 0) {
            log.error("Error:: "+respSaldoCuenta.getErrores().getDescError());
            return 0d;
        }
        Double montoDisponible = ToolsR._D(respSaldoCuenta.getBody().getValor("SALDO_ACTUAL"));
        return montoDisponible;
    }

    public RespuestaSVC obtenerReferenciaSpei() {
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append("obtenerReferenciaSpei").toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();

        try {
            Map<String, Object> map = new HashMap<>();
            jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ValidaClabeSpeiDest");
        }
        return respuestaSvc;
    }


    public CuentaPanOBJ cargarDatosCuentaSelec(Integer cuentaId) {
        Boolean resultado = false;
        CuentaPanOBJ cuentaPan= new CuentaPanOBJ();
        AsignaCuentaPanOBJ asignaCuenta = null;
        try {
            List<CuentaPanOBJ> listaCta = serviciosWS.buscarCuentaPlastico("buscarCuentaPan", cuentaId);
            if (listaCta != null) {
                cuentaPan = listaCta.get(0);
                buscarCuentaPan(cuentaPan.getId(), null);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return cuentaPan;
    }

    public AsignaCuentaPanOBJ buscarCuentaPan(Integer cuentaId, Integer panId) {
        Boolean resultado = true;
        AsignaCuentaPanOBJ asignaCuenta = null;
        AsignaCuentaPanReq req = new AsignaCuentaPanReq();
        req.setCuenta_id(cuentaId);
        req.setPan_id(panId);

        try {
            List<AsignaCuentaPanOBJ> listAsignaCuenta = serviciosWS.BuscarCuentaPanRec("buscarCuentaPanRec", req);
            if (listAsignaCuenta == null) {
                resultado = false;
            } else {
                asignaCuenta = listAsignaCuenta.get(0);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return asignaCuenta;
    }

    public SolicitanteOBJ cargarDatosSolicitante(String solicitanteId)
    {
        BuscarSolicitanteCompletoRequest req = new BuscarSolicitanteCompletoRequest();
        req.setNumero(solicitanteId);
        SolicitanteOBJ solicitante = serviciosWS.BuscarSolicitante("buscarSolicitanteCompleto", req);
        return solicitante;
    }
}
