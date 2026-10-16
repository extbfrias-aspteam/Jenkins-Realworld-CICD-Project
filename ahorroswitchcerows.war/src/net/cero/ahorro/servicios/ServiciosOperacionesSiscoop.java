package net.cero.ahorro.servicios;

import net.soap.plasticos.servicios.RetirarSoapServiciosSW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;

import net.cero.ws.data.Errores;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import net.soap.plasticos.servicios.DepositarSoapServiciosSW;



/**
 * Clase usada para abstraer las operaciones relacionadas al proveedor de siscoop
 * @author Alejandro Astorga
 */
public class ServiciosOperacionesSiscoop {
    private static Logger log = LogManager.getLogger(ServiciosOperacionesSiscoop.class);

    /**
     * Metodo usado para realizar el proceso de deposito para una tarjeta cuyo proveedor sea SISCOOP
     * @param map Diccionario que contiene los valores enviados en el request que se recibieron para procesar la operacion
     * @return Objeto con el mensaje relacionado al resultado de la operacion.
     * */
    public static RespuestaSVC depositarProveedorSiscoop(Map<String, Object> map){
        RespuestaSVC respuestaSvc = new RespuestaSVC();

        try{
            RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(ToolsR._T(map.get("cuentaID")));
            if(respTarjeta.getErrores().getCodigoError() != 0){
                respuestaSvc.getErrores().setErrores(respTarjeta.getErrores().getErrores());
                return respuestaSvc;
            }

            if(!"OK".equals(validarTarjeta(respTarjeta))){
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, "Error: La tarjeta es NO valida");
                return respuestaSvc;
            }

            /* LLENA LOS PARAMETROS PARA SER INVOCADOS EN EL DEPOSITO DE MULE DEL PROVEEDOR */
            PlaHeaderWS plaHeader = new PlaHeaderWS();
            String tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));

            plaHeader.setIdUsuario( map.get("usuarioID") == null ? 99L : ToolsR._L(map.get("usuarioID")));
            plaHeader.setIdCanalAtencion( map.get("canalID") == null ? 99L : ToolsR._L(map.get("canalID")));
            plaHeader.setIdSucursal( map.get("sucursalID") == null ? 99L : ToolsR._L(map.get("sucursalID")));
            plaHeader.setIdCliente( map.get("clienteID") == null ? 99L : ToolsR._L(map.get("clienteID")));
            plaHeader.setIdCuenta( map.get("ID") == null ? 99L : ToolsR._L(map.get("ID")));

            plaHeader.setIpHost(ToolsR._T(map.get("host")));
            if(respTarjeta.getBody().getValor("TARJETA_ID") != null ) plaHeader.setIdPan(ToolsR._L(respTarjeta.getBody().getValor("TARJETA_ID")));

            DatosPlasticoREQ datosPlasticoReq = new DatosPlasticoREQ();
            datosPlasticoReq.setPlastico(tarjeta);
            datosPlasticoReq.setMonto(ToolsR._D(map.get("monto")));

            RespuestaSVC resPla = DepositarSoapServiciosSW.Depositar(plaHeader, datosPlasticoReq);
            if(resPla.getErrores().getCodigoError() == 0){
                DatosPlasticoOBJ datosPlasticoObj = (DatosPlasticoOBJ)resPla.getBody().getValor("DATOS_PLASTICO_OBJ");

                /* VALOR == 1 RETIRO CORRECTO */
                if(datosPlasticoObj.getCodigo().intValue() != 1){
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, datosPlasticoObj.getDescripcion());
                }else{
                    respuestaSvc.getBody().addValor("DATOS_PLASTICO_OBJ", datosPlasticoObj);
                    respuestaSvc.getBody().addValor("CODIGO", ToolsR._T(datosPlasticoObj.getCodigo()));
                    respuestaSvc.getBody().addValor("DESCRIPCION", ToolsR._T(datosPlasticoObj.getDescripcion()));
                    respuestaSvc.getBody().addValor("AUTORIZACION", ToolsR._T(datosPlasticoObj.getAutorizacion()));
                    respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T(datosPlasticoObj.getBalanceActual()));
                    respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(datosPlasticoObj.getBalance()));

                    /* GRABA REPLICA EN LA TABLA DE TRANSACCIONES */
                    map.put("usuarioID",  ToolsR._T(plaHeader.getIdUsuario()));
                    map.put("canalID",  ToolsR._T(plaHeader.getIdCanalAtencion()));
                    map.put("sucursalID",  ToolsR._T(plaHeader.getIdSucursal()));
                    map.put("clienteID",  ToolsR._T(plaHeader.getIdCliente()));
                    map.put("ID",  ToolsR._T(plaHeader.getIdCuenta()));

                    map.put("saldoActual",  ToolsR._T(datosPlasticoObj.getBalanceActual()));
                    map.put("saldoAnterior", ToolsR._T(datosPlasticoObj.getBalance()));
                    map.put("autorizacion", ToolsR._T(datosPlasticoObj.getAutorizacion()));

                    RespuestaSVC respTransacciones = ServiciosTransaccionesWS.TransaccionAhorro(map);
                    if(respTransacciones.getErrores().getCodigoError() == 0){
                        respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", "OK");

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
            }else{
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, resPla.getErrores().getDescError());
            }
        }catch(Exception ex){
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
        }
        return respuestaSvc;
    }

    /**
     * Metodo usado para realizar el proceso de retiro para una tarjeta cuyo proveedor sea SISCOOP
     * @param map Diccionario que contiene los valores enviados en el request que se recibieron para procesar la operacion
     * @return Objeto con el mensaje relacionado al resultado de la operacion.
     * */
    public static RespuestaSVC retirarProveedorSiscoop(Map<String, Object> map) {
        RespuestaSVC respuestaSvc = new RespuestaSVC();

        try {
            RespuestaSVC respTarjeta = ServiciosAhorroWS.buscarPlasticoCuenta(ToolsR._T(map.get("cuentaID")));
            if (respTarjeta.getErrores().getCodigoError() != 0) {
                respuestaSvc.getErrores().setErrores(respTarjeta.getErrores().getErrores());
                return respuestaSvc;
            }

            if (!"OK".equals(validarTarjeta(respTarjeta))) {
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
                        "ERROR: LA TARJETA ES NO VALIDA");
                return respuestaSvc;
            }

            /*
             * LLENA LOS PARAMETROS PARA SER INVOCADOS EN DEPOSITO CON EL
             * PROVEEDOR
             */
            PlaHeaderWS plaHeader = new PlaHeaderWS();
            String tarjeta = ToolsR._T(respTarjeta.getBody().getValor("TARJETA"));

            plaHeader.setIdUsuario(map.get("usuarioID") == null ? 99L : ToolsR._L(map.get("usuarioID")));
            plaHeader.setIdCanalAtencion(map.get("canalID") == null ? 99L : ToolsR._L(map.get("canalID")));
            plaHeader.setIdSucursal(map.get("sucursalID") == null ? 99L : ToolsR._L(map.get("sucursalID")));
            plaHeader.setIdCliente(map.get("clienteID") == null ? 99L : ToolsR._L(map.get("clienteID")));
            plaHeader.setIdCuenta(map.get("ID") == null ? 99L : ToolsR._L(map.get("ID")));

            plaHeader.setIpHost(ToolsR._T(map.get("host")));
            if (respTarjeta.getBody().getValor("TARJETA_ID") != null)
                plaHeader.setIdPan(ToolsR._L(respTarjeta.getBody().getValor("TARJETA_ID")));

            DatosPlasticoREQ datosPlasticoReq = new DatosPlasticoREQ();
            datosPlasticoReq.setPlastico(tarjeta);
            datosPlasticoReq.setMonto(ToolsR._D(map.get("monto")));

            RespuestaSVC resPla = RetirarSoapServiciosSW.Retirar(plaHeader, datosPlasticoReq);
            if (resPla.getErrores().getCodigoError() == 0) {
                DatosPlasticoOBJ datosPlasticoObj = (DatosPlasticoOBJ) resPla.getBody().getValor("DATOS_PLASTICO_OBJ");

                /* VALOR == 1 RETIRO CORRECTO */
                if (datosPlasticoObj.getCodigo().intValue() != 1) {
                    respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
                            datosPlasticoObj.getDescripcion());
                } else {
                    respuestaSvc.getBody().addValor("DATOS_PLASTICO_OBJ", datosPlasticoObj);
                    respuestaSvc.getBody().addValor("CODIGO", ToolsR._T(datosPlasticoObj.getCodigo()));
                    respuestaSvc.getBody().addValor("DESCRIPCION", ToolsR._T(datosPlasticoObj.getDescripcion()));
                    respuestaSvc.getBody().addValor("AUTORIZACION", ToolsR._T(datosPlasticoObj.getAutorizacion()));
                    respuestaSvc.getBody().addValor("SALDO_ACTUAL", ToolsR._T(datosPlasticoObj.getBalanceActual()));
                    respuestaSvc.getBody().addValor("SALDO_ANTERIOR", ToolsR._T(datosPlasticoObj.getBalance()));

                    /* GRABA REPLICA EN LA TABLA DE TRANSACCIONES */
                    map.put("usuarioID", ToolsR._T(plaHeader.getIdUsuario()));
                    map.put("canalID", ToolsR._T(plaHeader.getIdCanalAtencion()));
                    map.put("sucursalID", ToolsR._T(plaHeader.getIdSucursal()));
                    map.put("clienteID", ToolsR._T(plaHeader.getIdCliente()));
                    map.put("ID", ToolsR._T(plaHeader.getIdCuenta()));

                    map.put("saldoActual", ToolsR._T(datosPlasticoObj.getBalanceActual()));
                    map.put("saldoAnterior", ToolsR._T(datosPlasticoObj.getBalance()));
                    map.put("autorizacion", ToolsR._T(datosPlasticoObj.getAutorizacion()));

                    RespuestaSVC respTransacciones = ServiciosTransaccionesWS.TransaccionAhorro(map);

                    if (respTransacciones.getErrores().getCodigoError() == 0) {
                        respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION", "OK");
                        if (map.get("cuenta").toString().substring(0, 2).equals("05")) {
                            RespuestaSVC respBitacora = ServiciosTransaccionesWS.bitacoraAhorro(map);
                            if (respBitacora.getErrores().getCodigoError() == 0) {
                                respuestaSvc.getBody().addValor("RESULTADO_BITACORA", "OK");
                            } else {
                                respuestaSvc.getBody().addValor("RESULTADO_BITACORA",
                                        respBitacora.getErrores().getDescError());
                            }
                        }
                    } else {
                        respuestaSvc.getBody().addValor("RESULTADO_TRANSACCION",
                                respTransacciones.getErrores().getDescError());
                    }
                }
            } else {
                respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO,
                        resPla.getErrores().getDescError());
            }
        } catch (Exception ex) {
            respuestaSvc.getErrores().addCodigo("GENERICO", Errores.ERROR_INESPERADO, ex.getMessage());
        }

        return respuestaSvc;
    }


    private static String validarTarjeta(RespuestaSVC resp){
        if("".equals(ToolsR._T(resp.getBody().getValor("TARJETA")))) return "INVALIDA";
        if(!"ACT".equals(ToolsR._T(resp.getBody().getValor("ESTATUS_CLAVE")))) return ToolsR._T(resp.getBody().getValor("ESTATUS"));
        return "OK";
    }


}
