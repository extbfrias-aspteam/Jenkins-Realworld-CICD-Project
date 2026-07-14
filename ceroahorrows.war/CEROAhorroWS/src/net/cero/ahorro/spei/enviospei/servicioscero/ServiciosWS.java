package net.cero.ahorro.spei.enviospei.servicioscero;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Log4j2
@AllArgsConstructor
public class ServiciosWS extends BaseServicioWS {
    private final String wsUrlToken = ConstantesUtil.SERVICIO_TOKEN;

    public RespuestaSVC ValidaClabeSpeiDest(String clabe) {
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append("validaClabeSpeiDest").toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("CLABE", ToolsR._T(clabe));

            jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ValidaClabeSpeiDest");
        }
        return respuestaSvc;
    }

    // "/buscarCuentaPan"
    public List<CuentaPanOBJ> buscarCuentaPlastico(String servicio, Integer id) {
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append(servicio).toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();
        HeaderWS header = new HeaderWS();
        List<CuentaPanOBJ> lista = null;

        if (id == null)
            return lista;

        BuscarCuentaReq req = new BuscarCuentaReq();
        req.setId(id);
        try {
            jsonResponse = http(uri,gson.toJson(req),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            if (resp.getCodigo() == 0) {
                lista = gson.fromJson(resp.getData(), new TypeToken<List<CuentaPanOBJ>>() {
                }.getType());
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return lista == null || lista.isEmpty() ? null : lista;
    }

    // buscarCuentaPanRec
    public List<AsignaCuentaPanOBJ> BuscarCuentaPanRec(String servicio, AsignaCuentaPanReq req) {
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append(servicio).toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();
        HeaderWS header = new HeaderWS();
        List<AsignaCuentaPanOBJ> lista = null;

        try {
            jsonResponse = http(uri,gson.toJson(req),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            if (resp.getCodigo() == 0) {
                lista = gson.fromJson(resp.getData(), new TypeToken<List<AsignaCuentaPanOBJ>>() {
                }.getType());
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return lista == null || lista.isEmpty() ? null : lista;
    }

    // buscarSolicitanteCompleto
    public SolicitanteOBJ BuscarSolicitante(String servicio, BuscarSolicitanteCompletoRequest req) {
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.NUCLEO_CARTERA_WS).append("/").append(servicio).toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();
        SolicitanteOBJ solicitante = null;

        try {
            jsonResponse = http(uri,gson.toJson(req),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            if (resp.getCodigo() == 0) {
                solicitante = gson.fromJson(resp.getData(), SolicitanteOBJ.class);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return solicitante;
    }

    // "/buscarComisionPendiente";
    public List<ComisionPendienteOBJ> buscarComisionPendiente(String servicio, String cuentaId) {
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append(servicio).toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();
        HeaderWS header = new HeaderWS();
        List<ComisionPendienteOBJ> lista = null;

        if (ToolsR._T(cuentaId) == null)
            return lista;

        BuscarComisionPendienteReq req = new BuscarComisionPendienteReq();
        ComisionPendienteOBJ pen = new ComisionPendienteOBJ();
        pen.setCuentaId(cuentaId);
        req.setComisionPendiente(pen);

        try {
            jsonResponse = http(uri,gson.toJson(req),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            if (resp.getCodigo() == 0) {
                lista = gson.fromJson(resp.getData(), new TypeToken<List<ComisionPendienteOBJ>>() {
                }.getType());
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return lista == null || lista.isEmpty() ? null : lista;
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

    public RespuestaSVC obtenerAutorizacionSpei(HeaderWS header, Integer productoId, Double monto) {
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        Gson gson = ToolsR.GBuilder();
        String uri = new StrBuilder(ConstantesUtil.AHORRO_WS).append("/").append("obtenerAutorizacionSpei").toString();
        log.info(uri);
        String jsonResponse;
        Respuesta resp = new Respuesta();

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("monto", ToolsR._T(monto));
            map.put("productoId", ToolsR._T(productoId));
            map.put("usuarioID", ToolsR._T(header.getIdUsuario()));
            map.put("sucursalID", ToolsR._T(header.getIdSucursal()));

            jsonResponse = http(uri,gson.toJson(map),ConstantesUtil.PALABRAUSU,ConstantesUtil.PALABRAHID);
            resp = gson.fromJson(jsonResponse, Respuesta.class);
            respuestaSvc = gson.fromJson(resp.getData(), RespuestaSVC.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "ValidaClabeSpeiDest");
        }
        return respuestaSvc;
    }
}


