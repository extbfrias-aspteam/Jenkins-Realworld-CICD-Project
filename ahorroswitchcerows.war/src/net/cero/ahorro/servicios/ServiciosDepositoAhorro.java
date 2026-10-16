package net.cero.ahorro.servicios;

import com.google.gson.Gson;
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
import java.util.Map;
import java.util.UUID;


/**
 * Clase de partida usada para gestionar el camino a seguir dependiendo de si la operacion involucra un plastico o no para el proceso de deposito
 * @author
 * */
@Service
public class ServiciosDepositoAhorro {

    private static Logger log = LogManager.getLogger(ServiciosDepositoAhorro.class);

    private final ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock;

    public ServiciosDepositoAhorro(ServicioOperacionesTarjetaDock servicioOperacionesTarjetaDock) {
        this.servicioOperacionesTarjetaDock = servicioOperacionesTarjetaDock;
    }

    public RespuestaSVC depositarProveedor(Map<String, Object> map){
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        try{
            log.info("Inicia rutina para realizar deposito en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
            String tarjeta = null;
            if(Constantes.VALID_TARJETA_DEB)
            {
                RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(ToolsR._T(map.get("cuentaID")));
                if(respTarjeta.getErrores().getCodigoError() != 0){
                    respuestaSvc.getErrores().setErrores(respTarjeta.getErrores().getErrores());
                    log.info("{} ({})",respTarjeta.getErrores().getErrores().stream().findFirst().get().getDescrError(),ToolsR._T(map.get("cuentaID")));
                    log.info(" Finaliza rutina para realizar deposito en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
                    return respuestaSvc;
                }

                if(!"OK".equals(validarTarjeta(respTarjeta))){
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error: La tarjeta es NO valida");
                    log.info("Error: La tarjeta es NO valida ({})",ToolsR._T(map.get("cuentaID")));
                    log.info("Finaliza rutina para realizar deposito en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
                    return respuestaSvc;
                }
                tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));
            }
            tarjeta = (StringUtils.isBlank(tarjeta) ? null : tarjeta);
            log.info("tarjeta: {}",tarjeta);
            /*VALIDAMOS DE QUE PROVEEDOR ES LA TARJETA Y SU SALDO*/
            Gson gson = new Gson();
            Respuesta consultaResp = servicioOperacionesTarjetaDock.consultaEstatusTarjeta(String.valueOf(map.get("cuentaID")));
            ConsultarEstatusTarjetasOBJ obj = null;
            if(consultaResp.getCodigo() == 0)
            {
                if(!StringUtils.isBlank(consultaResp.getData()))
                {
                    obj = gson.fromJson(consultaResp.getData(), ConsultarEstatusTarjetasOBJ.class);
                    if(obj.getProveedor().equalsIgnoreCase("MASTERCARD-CACAO"))
                    {
                        Respuesta consultaTarjetaResp = servicioOperacionesTarjetaDock.consultaDatosTarjeta(map,obj.getTarjetaPrincipal(),obj.getTipoTarjeta());
                        if(!StringUtils.isBlank(obj.getTarjetaPrincipal()) && StringUtils.isBlank(tarjeta))
                        {
                            tarjeta = obj.getTarjetaPrincipal();
                        }
                    }
                }
                else {
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "NO SE PUDO OBTENER EL PROVEEDOR DEL PLASTICO ASOCIADO A LA CUENTA");
                    return respuestaSvc;
                }
            }
            if(obj.getProveedor().equalsIgnoreCase("MASTERCARD-CACAO"))
            {
                log.info("CAMINO DE MASTERCARD-CACAO");
                /*AQUI PONEMOS EL CODIGO PARA MANDAR LLAMAR EL ORQUESTADOR*/
                Respuesta respOrquesta = servicioOperacionesTarjetaDock.depositoTarjeta(map,tarjeta);
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
                        respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(data.getSaldoFinal()-data.getImporte()));
                        respuestaSvc.getBody().addValor("ID_TRANSACCION", ToolsR._T(data.getMovimientoId()));

                        log.info("Constantes.INSERT_MOV_CERO: {}"
                                ,Constantes.INSERT_MOV_CERO);
                        if(Constantes.INSERT_MOV_CERO)
                        {

                            PlaHeaderWS plaHeader = new PlaHeaderWS();
                            plaHeader.setIdUsuario( map.get("usuarioID") == null ? 99L : ToolsR._L(map.get("usuarioID")));
                            plaHeader.setIdCanalAtencion( map.get("canalID") == null ? 99L : ToolsR._L(map.get("canalID")));
                            plaHeader.setIdSucursal( map.get("sucursalID") == null ? 99L : ToolsR._L(map.get("sucursalID")));
                            plaHeader.setIdCliente( map.get("clienteID") == null ? 99L : ToolsR._L(map.get("clienteID")));
                            plaHeader.setIdCuenta( map.get("ID") == null ? 99L : ToolsR._L(map.get("ID")));
                            plaHeader.setIpHost(ToolsR._T(map.get("host")));

                            /* GRABA REPLICA EN LA TABLA DE TRANSACCIONES */
                            map.put("usuarioID",  ToolsR._T(plaHeader.getIdUsuario()));
                            map.put("canalID",  ToolsR._T(plaHeader.getIdCanalAtencion()));
                            map.put("sucursalID",  ToolsR._T(plaHeader.getIdSucursal()));
                            map.put("clienteID",  ToolsR._T(plaHeader.getIdCliente()));
                            map.put("ID",  ToolsR._T(plaHeader.getIdCuenta()));

                            map.put("saldoActual",  ToolsR._T(data.getSaldoFinal()));
                            map.put("saldoAnterior", ToolsR._T(data.getSaldoFinal()-data.getImporte()));
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
                log.info("CAMINO DE SISCOOP");
                respuestaSvc = ServiciosOperacionesSiscoop.depositarProveedorSiscoop(map);
            }

            log.info("Finaliza rutina para realizar deposito en el servicio del proveedor para la cuenta {}",map.get("cuenta"));
        }catch(Exception ex){
            log.error("Termina con error la rutina para realizar deposito en el servicio del proveedor para la cuenta {}",map.get("cuenta"),ex);
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
        }
        return respuestaSvc;
    }


    public RespuestaSVC depositarLocal(Map<String, Object> map){
        RespuestaSVC respuestaSvc = new RespuestaSVC();
        RespuestaSVC respBitacora = new RespuestaSVC();

        try{
            respuestaSvc = ServiciosTransaccionesWS.depositarAhorro(map);
            if(respuestaSvc.getErrores().getCodigoError() == 0){
                respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", "OK");

                String idMovimiento = respuestaSvc.getBody().getValor("ID_TRANSACCION").toString();
                log.info("idMovimiento: {}",idMovimiento);
                if(map.get("cuenta").toString().substring(0, 2).equals("05")) {
                    respBitacora = ServiciosTransaccionesWS.bitacoraAhorro(map);
                    if(respBitacora.getErrores().getCodigoError() == 0){
                        respuestaSvc.getBody().addValor("RESULTADO_BITACORA", "OK");
                    }else{
                        respuestaSvc.getBody().addValor("RESULTADO_BITACORA", respBitacora.getErrores().getDescError() );
                    }
                }
            }
        }catch(Exception ex){
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
        }
        return respuestaSvc;
    }

    private String validarTarjeta(RespuestaSVC resp){
        if("".equals(ToolsR._T(resp.getBody().getValor("TARJETA")))) return "INVALIDA";
        if(!"ACT".equals(ToolsR._T(resp.getBody().getValor("ESTATUS_CLAVE")))) return ToolsR._T(resp.getBody().getValor("ESTATUS"));
        return "OK";
    }

}
