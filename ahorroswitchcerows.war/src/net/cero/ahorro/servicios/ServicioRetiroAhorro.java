package net.cero.ahorro.servicios;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.cero.ahorro.data.*;
import net.cero.ahorro.data.HeaderWS;
import net.cero.spring.config.Respuesta;
import net.cero.utilidades.ReferenciasNumericas;
import net.cero.ws.data.*;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;


/**
 * Clase de partida usada para gestionar el camino a seguir dependiendo de si la operacion involucra un plastico o no para el proceso de retiro
 * @author
 * */
@Service
public class ServicioRetiroAhorro {
    private static Logger log = LogManager.getLogger(ServicioRetiroAhorro.class);

    private final ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock;

    public ServicioRetiroAhorro(ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock) {
        this.servicioOperacionesTarjetaDock = servicioOperacionesTarjetaDock;
    }

    public RespuestaSVC retirarProveedor(Map<String, Object> map) {
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        log.info("Inicia rutina para realizar retiro en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
        try {
            log.info("Constantes.INSERT_MOV_CERO: {},Constantes.VALID_TARJETA_DEB: {}"
                    ,Constantes.INSERT_MOV_CERO,Constantes.VALID_TARJETA_DEB);
            String tarjeta = null;
            Gson gson = new Gson();
            if(Constantes.VALID_TARJETA_DEB)
            {
                RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(ToolsR._T(map.get("cuentaID")));
                if(respTarjeta.getErrores().getCodigoError() != 0){
                    log.info("{} ({})",respTarjeta.getErrores().getErrores().stream().findFirst().get().getDescrError(),ToolsR._T(map.get("cuentaID")));
                    log.info("Finaliza rutina para realizar retiro en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
                    respuestaSvc.getErrores().setErrores(respTarjeta.getErrores().getErrores());
                    return respuestaSvc;
                }

                if(!"OK".equals(validarTarjeta(respTarjeta))){
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error: La tarjeta es NO valida");
                    log.info("Error: La tarjeta es NO valida ({})",ToolsR._T(map.get("cuentaID")));
                    log.info("Finaliza rutina para realizar retiro en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
                    return respuestaSvc;
                }
                tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));
            }
            tarjeta = (StringUtils.isBlank(tarjeta) ? null : tarjeta);
            log.info("tarjeta: {}",tarjeta);

            if(Constantes.INSERT_MOV_CERO)
            {
                respuestaSvc = consultarSaldoLocal(map);
                if(respuestaSvc.getErrores().getCodigoError() == 0)
                {
                    Double saldoActual = (map.containsKey("saldoActual") ? Double.parseDouble(map.get("saldoActual").toString()) : 0d);
                    Double montoOperacion = Double.parseDouble(map.get("monto").toString());

                    log.info("consulta de saldo de cuenta: {}, saldoActual: {}, montoOperacion: {}"
                            ,map.get("cuenta"),saldoActual,montoOperacion);
                    if(montoOperacion.doubleValue() > saldoActual.doubleValue())
                    {
                        respuestaSvc.getErrores().addCodigo("GENERICO",Errores.ERROR_INESPERADO, "Saldo Insuficiente para realizar la operación");
                        return respuestaSvc;
                    }
                }
                else
                {
                    return respuestaSvc;
                }
            }
            /*VALIDAMOS DE QUE PROVEEDOR ES LA TARJETA Y SU SALDO*/
            Respuesta consultaResp = servicioOperacionesTarjetaDock.consultaEstatusTarjeta(String.valueOf(map.get("cuenta")));
            ConsultarEstatusTarjetasOBJ obj = null;
            if(consultaResp.getCodigo() == 0)
            {
                if(!StringUtils.isBlank(consultaResp.getData()))
                {
                    obj = gson.fromJson(consultaResp.getData(), ConsultarEstatusTarjetasOBJ.class);
                    if(obj.getProveedor().equalsIgnoreCase("MASTERCARD-CACAO"))
                    {
                        Respuesta consultaTarjetaResp = servicioOperacionesTarjetaDock.consultaDatosTarjeta(map,obj.getTarjetaPrincipal(),obj.getTipoTarjeta());
                        if(!StringUtils.isBlank(consultaTarjetaResp.getData()))
                        {
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
                            }
                        }
                    }
                }
                else {
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "NO SE PUDO OBTENER EL PROVEEDOR DEL PLASTICO ASOCIADO A LA CUENTA");
                    return respuestaSvc;
                }
            }

            PlaHeaderWS plaHeader = new PlaHeaderWS();
            plaHeader.setIdUsuario(map.get("usuarioID") == null ? 99L : ToolsR._L(map.get("usuarioID")));
            plaHeader.setIdCanalAtencion(map.get("canalID") == null ? 99L : ToolsR._L(map.get("canalID")));
            plaHeader.setIdSucursal(map.get("sucursalID") == null ? 99L : ToolsR._L(map.get("sucursalID")));
            plaHeader.setIdCliente(map.get("clienteID") == null ? 99L : ToolsR._L(map.get("clienteID")));
            plaHeader.setIdCuenta(map.get("ID") == null ? 99L : ToolsR._L(map.get("ID")));

            plaHeader.setIpHost(ToolsR._T(map.get("host")));
            /*AQUI PONEMOS EL CODIGO PARA MANDAR LLAMAR EL ORQUESTADOR*/

            if(obj.getProveedor().equalsIgnoreCase("MASTERCARD-CACAO"))
            {
                log.info("CAMINO DE MASTERCARD-CACAO");
                Respuesta respOrquesta = servicioOperacionesTarjetaDock.retirarTarjeta(map,tarjeta);
                if(respOrquesta.getCodigo() == 0){
                    DataOrquestadorOBJ data = new DataOrquestadorOBJ();
                    if(!StringUtils.isBlank(respOrquesta.getData()))
                    {
                        data = gson.fromJson(respOrquesta.getData(),DataOrquestadorOBJ.class);
                    }

                    /* VALOR == 1 RETIRO CORRECTO */
                    if(!data.getDescRespuesta().equalsIgnoreCase("Aprobada")){
                        respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, data.getDescRespuesta());
                    }else{
                        respuestaSvc.getBody().addValor("CODIGO", ToolsR._T(data.getCodRespuesta()));
                        respuestaSvc.getBody().addValor("DESCRIPCION", ToolsR._T(data.getDescRespuesta()));
                        respuestaSvc.getBody().addValor("AUTORIZACION", ToolsR._T(data.getCodRespuesta()));
                        respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T(data.getSaldoFinal()));
                        respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(data.getSaldoFinal()+data.getImporte()));
                        respuestaSvc.getBody().addValor("ID_TRANSACCION", ToolsR._T(data.getMovimientoId()));

                        if(Constantes.INSERT_MOV_CERO == true)
                        {
                            /* GRABA REPLICA EN LA TABLA DE TRANSACCIONES */
                            map.put("usuarioID",  ToolsR._T(plaHeader.getIdUsuario()));
                            map.put("canalID",  ToolsR._T(plaHeader.getIdCanalAtencion()));
                            map.put("sucursalID",  ToolsR._T(plaHeader.getIdSucursal()));
                            map.put("clienteID",  ToolsR._T(plaHeader.getIdCliente()));
                            map.put("ID",  ToolsR._T(plaHeader.getIdCuenta()));

                            map.put("saldoActual",  ToolsR._T(data.getSaldoFinal()));
                            map.put("saldoAnterior", ToolsR._T(data.getSaldoFinal()+data.getImporte()));
                            map.put("autorizacion", ToolsR._T(data.getCodRespuesta()));


                            RespuestaSVC respTransacciones = ServiciosTransaccionesWS.TransaccionAhorro(map);
                            if(respTransacciones.getErrores().getCodigoError() == 0){
                                respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", "OK");

                                String idMovimiento = respTransacciones.getBody().getValor("ID").toString();
                                log.info("idMovimiento: {}",idMovimiento);

                                RespuestaSVC respBitacora = ServiciosTransaccionesWS.bitacoraAhorro(map);
                                if(respBitacora.getErrores().getCodigoError() == 0){
                                    respuestaSvc.getBody().addValor("RESULTADO_BITACORA", "OK");
                                }else{
                                    respuestaSvc.getBody().addValor("RESULTADO_BITACORA", respBitacora.getErrores().getDescError() );
                                }

                            }else{
                                respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", respTransacciones.getErrores().getDescError() );
                            }
                        }
                        else{
                            log.info("El movimiento de deposito no se registrará en la base de cero");
                        }
                    }
                }else{
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, respOrquesta.getMensaje());
                }
            }
            else if(obj.getProveedor().equalsIgnoreCase("SISCOOP"))
            {
                log.info("SISCOOP");
                respuestaSvc = ServiciosOperacionesSiscoop.retirarProveedorSiscoop(map);
            }
            log.info("Finaliza rutina para realizar retiro en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
        } catch (Exception ex) {
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
            log.error("Termina con error la rutina para realizar retiro en el servicio del proveedor para la cuenta {}",map.get("cuenta"),ex);
        }

        return respuestaSvc;
    }


    public RespuestaSVC retirarLocal(Map<String, Object> map) {
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        RespuestaSVC respBitacora = new RespuestaSVC();

        try {
            respuestaSvc = consultarSaldoLocal(map);
            if(respuestaSvc.getErrores().getCodigoError() == 0)
            {
                Double saldoActual = (map.containsKey("saldoActual") ? Double.parseDouble(map.get("saldoActual").toString()) : 0d);
                Double montoOperacion = Double.parseDouble(map.get("monto").toString());
                log.info("consulta de saldo de cuenta: {}, saldoActual: {}, montoOperacion: {}"
                        ,map.get("cuenta"),saldoActual,montoOperacion);
                if(montoOperacion.doubleValue() > saldoActual.doubleValue())
                {
                    
                    //respuestaSvc.getErrores().addCodigo("GENERICO",Errores.ERROR_INESPERADO, "Saldo Insuficiente para realizar la operación");
                    //return respuestaSvc;
                    return actualizaRespuesta(Errores.ERROR_INESPERADO, "Saldo Insuficiente para realizar la operación");
                }
            }
            else
            {
                return respuestaSvc;
            }

            respuestaSvc = ServiciosTransaccionesWS.retirarAhorro(map);
            if (respuestaSvc.getErrores().getCodigoError() == 0) {
                respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", "OK");

                String idMovimiento = respuestaSvc.getBody().getValor("ID_TRANSACCION").toString();
                log.info("idMovimiento: {}",idMovimiento);

                if (map.get("cuenta").toString().substring(0, 2).equals("05")) {
                    respBitacora = ServiciosTransaccionesWS.bitacoraAhorro(map);
                    if (respBitacora.getErrores().getCodigoError() == 0) {
                        respuestaSvc.getBody().addValor("RESULTADO_BITACORA", "OK");
                    } else {
                        respuestaSvc.getBody().addValor("RESULTADO_BITACORA", respBitacora.getErrores().getDescError());
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
        }
        return respuestaSvc;
    }

    private RespuestaSVC consultarSaldoLocal(Map<String, Object> map){
        RespuestaSVC respuestaSvc = new RespuestaSVC();

        Map<String, Object> mapParametros = new HashMap<>();
        mapParametros.put("cuenta",map.get("cuenta"));
        mapParametros.put("fecha",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        respuestaSvc = ServiciosTransaccionesWS.leerSaldoCuentaTabla(mapParametros);
        if(respuestaSvc.getErrores().getCodigoError() == 0){
            if(respuestaSvc.getBody().getValor("SALDO_FECHA") != null && !StringUtils.isBlank(respuestaSvc.getBody().getValor("SALDO_FECHA").toString()))
            {
                map.put("saldoActual", ToolsR._T(respuestaSvc.getBody().getValor("SALDO_FECHA")));
            }
            else
            {
                map.put("saldoActual", "0.00");
            }

        }
        return respuestaSvc;
    }

    private String validarTarjeta(RespuestaSVC resp) {
        if ("".equals(ToolsR._T(resp.getBody().getValor("TARJETA"))))
            return "INVALIDA";
        if (!"ACT".equals(ToolsR._T(resp.getBody().getValor("ESTATUS_CLAVE"))))
            return ToolsR._T(resp.getBody().getValor("ESTATUS"));
        return "OK";
    }
	private RespuestaSVC actualizaRespuesta(Long error, String descripcion) {
		RespuestaSVC respuestaSvc = new RespuestaSVC();
		respuestaSvc.getErrores().getErrores().clear();
		Cqueue tmpcqueue = new Cqueue();
		tmpcqueue.setCodigoError(error);
		tmpcqueue.setDescrError(descripcion);
		Queue<Cqueue> qErrores = new ArrayDeque<Cqueue>();
		qErrores.add(tmpcqueue);
		RespuestaErrorSVC resvc = new RespuestaErrorSVC();
		resvc.setErrores(qErrores);
		respuestaSvc.setErrores(resvc);
		return respuestaSvc;
	}
}
