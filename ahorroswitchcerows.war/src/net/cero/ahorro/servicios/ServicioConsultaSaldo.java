package net.cero.ahorro.servicios;

import com.google.gson.Gson;
import net.cero.ahorro.data.ConsultarEstatusTarjetasOBJ;
import net.cero.ahorro.data.RespuestaConsultarTarjetaOBJ;
import net.cero.ahorro.data.SaldoDatoOBJ;
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.spring.config.Respuesta;
import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.soap.plasticos.servicios.ObtenerSaldoSoapServiciosSW;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServicioConsultaSaldo {
    private static final Logger log = LogManager.getLogger(ServicioConsultaSaldo.class);

    private final ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock;

    public ServicioConsultaSaldo(ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock) {
        this.servicioOperacionesTarjetaDock = servicioOperacionesTarjetaDock;
    }

    public RespuestaSVC consultarSaldoProveedor(Map<String, Object> map){
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        String tarjeta = "";

        Gson gson = new Gson();
        Respuesta consultaResp = servicioOperacionesTarjetaDock.consultaEstatusTarjeta(String.valueOf(map.get("cuenta")));
        ConsultarEstatusTarjetasOBJ obj = null;
        if(consultaResp.getCodigo() == 0)
        {
            if(!StringUtils.isBlank(consultaResp.getData()))
            {
                obj = gson.fromJson(consultaResp.getData(), ConsultarEstatusTarjetasOBJ.class);
                if(!StringUtils.isBlank(obj.getTarjetaPrincipal()) && StringUtils.isBlank(tarjeta))
                {
                    tarjeta = obj.getTarjetaPrincipal();
                }
            }
        }

        if(obj != null && !StringUtils.isBlank(obj.getProveedor())
                && obj.getProveedor().equalsIgnoreCase("MASTERCARD-CACAO"))
        {
            Respuesta consultaTarjetaResp = servicioOperacionesTarjetaDock.consultaDatosTarjeta(map,obj.getTarjetaPrincipal(),obj.getTipoTarjeta());
            if(!StringUtils.isBlank(consultaTarjetaResp.getData()))
            {
                log.info("Consulta saldo proveedor DOCK");
                RespuestaConsultarTarjetaOBJ objConsulta = gson.fromJson(consultaTarjetaResp.getData(),RespuestaConsultarTarjetaOBJ.class);
                SaldoDatoOBJ saldo = objConsulta.getSaldoActual().stream()
                        .filter(x -> x.getClaveTipoCuenta().trim().equalsIgnoreCase("CCLC"))
                        .findFirst().orElse(null);
                if(saldo != null)
                {
                    log.info("Saldo Cacao: {},monto operacion: {}",saldo.getSaldo().doubleValue(),ToolsR._D(map.get("monto")));
                    if(saldo.getSaldo().doubleValue() < ToolsR._D(map.get("monto")))
                    {
                        respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,"La cuenta no tiene el saldo suficiente para realizar la operación");
                        return respuestaSvc;
                    }
                    else
                    {
                        respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T(saldo.getSaldo()));
                        respuestaSvc.getBody().addValor("SALDO_REAL", ToolsR._T(saldo.getSaldo()));
                        respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(0));
                        respuestaSvc.getBody().addValor("MONTO_DISPONIBLE", ToolsR._T(saldo.getSaldo()));
                    }
                }
            }
            else
                respuestaSvc = this.consultarSaldoLocal(map);
        }
        else{
            if(!StringUtils.isBlank(tarjeta))
                respuestaSvc = this.consultaSaldoSiscoop(map);
            else
                respuestaSvc = this.consultarSaldoLocal(map);
        }

        return respuestaSvc;
    }

    public RespuestaSVC consultaSaldoSiscoop(Map<String, Object> map)
    {
        log.info("Consulta saldo proveedor siscoop");
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(ToolsR._T(map.get("cuentaID")));
        if(respTarjeta.getErrores().getCodigoError() != 0){
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, String.format("Error: Cuenta %s No tiene agregado el plastico", ToolsR._T(map.get("cuenta"))));
            return respuestaSvc;
        }

        /* LLENA LOS PARAMETROS PARA SER INVOCADOS A LA CONSULTA DE MULE DEL PROVEEDOR */
        String tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));

        RespuestaSVC respBitacora = new RespuestaSVC();
        PlaHeaderWS plaHeader = new PlaHeaderWS();
        plaHeader.setIdUsuario( map.get("usuarioID") == null ? 99L : ToolsR._L(map.get("usuarioID")));
        plaHeader.setIdCanalAtencion( map.get("canalID") == null ? 99L : ToolsR._L(map.get("canalID")));
        plaHeader.setIdSucursal( map.get("sucursalID") == null ? 99L : ToolsR._L(map.get("sucursalID")));
        plaHeader.setIdCliente( map.get("clienteID") == null ? 99L : ToolsR._L(map.get("clienteID")));
        plaHeader.setIdCuenta( map.get("ID") == null ? 99L : ToolsR._L(map.get("ID")));

        plaHeader.setIpHost(ToolsR._T(map.get("host")));
        if(respTarjeta.getBody().getValor("TARJETA_ID") != null ) plaHeader.setIdPan(ToolsR._L(respTarjeta.getBody().getValor("TARJETA_ID")));

        DatosPlasticoREQ datosPlasticoReq = new DatosPlasticoREQ();
        datosPlasticoReq.setPlastico(tarjeta);
        respuestaSvc = ObtenerSaldoSoapServiciosSW.ObtenerSaldo(plaHeader, datosPlasticoReq);
        if(respuestaSvc.getErrores().getCodigoError() == 0){
            DatosPlasticoOBJ datosPlasticoObj = (DatosPlasticoOBJ)respuestaSvc.getBody().getValor("DATOS_PLASTICO_OBJ");

            /* VALOR == 1 RETIRO CORRECTO */
            if(datosPlasticoObj.getCodigo().intValue() != 1){
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, datosPlasticoObj.getDescripcion());
            }else{
                respuestaSvc.getBody().addValor("CODIGO", ToolsR._T(datosPlasticoObj.getCodigo()));
                respuestaSvc.getBody().addValor("DESCRIPCION", ToolsR._T(datosPlasticoObj.getDescripcion()));
                respuestaSvc.getBody().addValor("AUTORIZACION", ToolsR._T(datosPlasticoObj.getAutorizacion()));
                respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T(datosPlasticoObj.getMontoDisponible()));
                respuestaSvc.getBody().addValor("SALDO_REAL", ToolsR._T(datosPlasticoObj.getMontoDisponible()));
                respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(datosPlasticoObj.getMontoDisponible()));
                respuestaSvc.getBody().addValor("MONTO_DISPONIBLE", ToolsR._T(datosPlasticoObj.getMontoDisponible()));
                respBitacora = ServiciosTransaccionesWS.bitacoraAhorro(map);
            }
        }
        return respuestaSvc;
    }

    public RespuestaSVC consultarSaldoLocal(Map<String, Object> map){
        log.info("Consulta saldo local");
        RespuestaSVC respuestaSvc = new RespuestaSVC();

        Map<String, Object> mapParametros = new HashMap<>();
        mapParametros.put("cuenta",map.get("cuenta"));
        mapParametros.put("fecha",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        respuestaSvc = ServiciosTransaccionesWS.leerSaldoCuentaTabla(mapParametros);
        if(respuestaSvc.getErrores().getCodigoError() == 0){
            if(respuestaSvc.getBody().getValor("SALDO_FECHA") != null && !StringUtils.isBlank(respuestaSvc.getBody().getValor("SALDO_FECHA").toString()))
            {
                map.put("saldoActual", ToolsR._T(respuestaSvc.getBody().getValor("SALDO_ACTUAL")));
                log.info("Consulta de saldo local: {}",map);
                respuestaSvc.getBody().addValor("SALDO_ACTUAL",
                        (!StringUtils.isBlank(ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA"))) ? ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA")) : "0.00"));
                respuestaSvc.getBody().addValor("SALDO_REAL",
                        (!StringUtils.isBlank(ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA"))) ? ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA")) : "0.00"));
                respuestaSvc.getBody().addValor("MONTO_DISPONIBLE",
                        (!StringUtils.isBlank(ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA"))) ? ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA")) : "0.00"));
            }
            else
            {
                respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T("0.00"));
                respuestaSvc.getBody().addValor("SALDO_REAL", ToolsR._T("0.00"));
                respuestaSvc.getBody().addValor("MONTO_DISPONIBLE", ToolsR._T("0.00"));
            }
        }
        return respuestaSvc;
    }
}
