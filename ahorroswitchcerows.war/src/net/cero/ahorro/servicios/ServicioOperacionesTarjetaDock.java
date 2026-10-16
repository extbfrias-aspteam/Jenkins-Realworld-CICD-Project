package net.cero.ahorro.servicios;

import com.google.gson.Gson;
import net.cero.ahorro.data.ConsultaSaldoOrquestadorOBJ;
import net.cero.ahorro.data.HeaderWS;
import net.cero.ahorro.data.TransaccionTarjetaOrquestadorOBJ;
import net.cero.spring.config.Respuesta;
import net.cero.utilidades.ReferenciasNumericas;
import net.cero.ws.data.Constantes;
import net.cero.ws.data.ToolsR;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Clase usada para modelar todas las operaciones a realizar con los servicios de DOCK para sus tarjetas
 * @author Alejandro Astorga
 * */
@Service
public class ServicioOperacionesTarjetaDock {

    private static Logger log = LogManager.getLogger(ServicioOperacionesTarjetaDock.class);

    /**
     * Trae información general de las tarjetas principal y adicional que pudiera tener asociado una cuenta
     * @param cuenta valor usado como identificación para una cuenta de ahorro en Cero.
     * @return Almacena el resultado de la operación.
     * */
    public  Respuesta consultaEstatusTarjeta(String cuenta)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("cuentaah",cuenta);
        Gson gson = new Gson();
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient cliente = new OkHttpClient();
        String auth = Credentials.basic("ASP", "a5p2017$");
        String url = Constantes.SERVICIO_BASE_ADMIN_PLASTICOS + "/consultarEstatusTarjetas";
        String body = gson.toJson(map);
        log.info("URL del WS: {}",url);
        log.info("Request body para servicio : {}",body);
        try{
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            Response response = cliente.newCall(request).execute();
            String obj = response.body().string();
            log.info("Respuesta orquestador: {}",obj);
            Respuesta respOrquesta = gson.fromJson(obj,Respuesta.class);
            response.body().close();
            return respOrquesta;
        }
        catch(IOException e)
        {
            log.error("Ocurrio un error interno al tratar de consultar la info de la cuenta",e);
            Respuesta respOrquesta = new Respuesta();
            respOrquesta.setCodigo(-1);
            respOrquesta.setData(null);
            respOrquesta.setMensaje("Ocurrio un error interno al tratar de consultar la info de la cuenta.");
            return respOrquesta;
        }
    }

    /**
     * Regresa información detallada de una tarjeta en particular
     * @param map Contiene los valores del request recibidos en el WS
     * @param tarjeta valor de 16 digitos relacionado al plastico a consultar
     * @param tipoTarjeta Indica si el plastico es Fisico(F) o Virtual(V)
     * @return Almacena el resultado de la operación.
     * */
    public Respuesta consultaDatosTarjeta(Map<String, Object> map,String tarjeta,String tipoTarjeta)
    {
        ConsultaSaldoOrquestadorOBJ reqTra = new ConsultaSaldoOrquestadorOBJ();
        net.cero.ahorro.data.HeaderWS header = new HeaderWS();

        header.setIdEmpresa(1L);
        header.setIdCanalAtencion(2L);
        header.setIdSucursal(1L);
        header.setIdUsuario(9L);
        header.setIpHost(ToolsR._T(map.get("host")));
        header.setNameHost("EQUIPO-PC");
        header.setIdTransaccion(0);
        reqTra.setNumeroTarjeta(tarjeta);
        reqTra.setTipoTarjeta(tipoTarjeta);
        reqTra.setHeader(header);
        Gson gson = new Gson();
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient cliente = new OkHttpClient();
        String auth = Credentials.basic("ASP", "a5p2017$");
        String url = Constantes.SERVICIO_BASE_ADMIN_PLASTICOS + "/consultar-tarjeta";
        String body = gson.toJson(reqTra);
        log.info("URL del WS: {}",url);
        log.info("Request body para servicio : {}",body);
        try{
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            Response response = cliente.newCall(request).execute();
            String obj = response.body().string();
            log.info("Respuesta orquestador: {}",obj);
            Respuesta respOrquesta = gson.fromJson(obj,Respuesta.class);
            response.body().close();
            return respOrquesta;
        }
        catch(IOException e)
        {
            log.error("Ocurrió un detalle al consumir el metodo de consultaSaldoProveedor del orquestador",e);
            Respuesta respOrquesta = new Respuesta();
            respOrquesta.setCodigo(-1);
            respOrquesta.setData(null);
            respOrquesta.setMensaje("Ocurrio un error interno al tratar de realizar la consulta de cuenta de la tarjeta.");
            return respOrquesta;
        }
    }

    /**
     * Realiza el proceso de retiro del plastico utilizando el servicio de DOCK
     * @param map Contiene los valores del request recibidos en el WS
     * @param tarjeta valor de 16 digitos relacionado al plastico a consultar
     * @return Almacena el resultado de la operación.
     * */
    public Respuesta retirarTarjeta(Map<String, Object> map,String tarjeta)
    {
        TransaccionTarjetaOrquestadorOBJ reqTra = new TransaccionTarjetaOrquestadorOBJ();
        net.cero.ahorro.data.HeaderWS header = new HeaderWS();
        if(map.containsKey("banco_id"))
            if(map.get("banco_id") != null && map.get("banco_id").toString().matches("[0-9]*"))
                header.setIdBanco(map.get("banco_id").toString());
        if(map.containsKey("idSpei"))
            if(map.get("idSpei") != null && map.get("idSpei").toString().matches("[0-9]*"))
                reqTra.setIdSpei(Long.valueOf(map.get("idSpei").toString()));
        if(map.containsKey("claveRastreo"))
            if(map.get("claveRastreo") != null )
                reqTra.setClave_rastreo(map.get("claveRastreo").toString());
        if(map.containsKey("descripcion"))
            if(map.get("descripcion") != null )
                reqTra.setObservaciones(map.get("descripcion").toString());

        header.setIdEmpresa(1L);
        header.setIdCanalAtencion(2L);
        header.setIdSucursal(1L);
        header.setIdUsuario(9L);
        header.setIpHost(ToolsR._T(map.get("host")));
        header.setNameHost("EQUIPO-PC");
        header.setIdTransaccion(ToolsR._L(map.get("tipoTransaccionID")));
        reqTra.setCuenta(String.valueOf(map.get("cuentaID")));
        reqTra.setImporte(String.valueOf(ToolsR._D(map.get("monto"))));
        reqTra.setMedioPago(Constantes.MEDIO_PAGO_CACAO);
        reqTra.setConcepto(Constantes.DEBITO_CON_RET);
        reqTra.setReferenciaNumerica(ReferenciasNumericas.generarRefNumerica());
        reqTra.setNumero_tarjeta(tarjeta);
        reqTra.setClaveMovimiento((map.containsKey("claveMovimientoDock") ? map.get("claveMovimientoDock").toString() : "" ));
        reqTra.setHeader(header);
        Gson gson = new Gson();
        MediaType media = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient cliente = new OkHttpClient
                .Builder()
                .connectTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .readTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .writeTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .build();

        String auth = Credentials.basic("ASP", "a5p2017$");
        String url = Constantes.SERVICIO_BASE_ADMIN_PLASTICOS + "/retirarTarjetaOrquestador";
        String body = gson.toJson(reqTra);
        log.info("URL del WS: {}",url);
        log.info("Request body para servicio : {}",body);
        try{
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            Response response = cliente.newCall(request).execute();
            String obj = response.body().string();
            log.info("Respuesta orquestador: {}",obj);
            Respuesta respOrquesta = gson.fromJson(obj,Respuesta.class);
            response.body().close();
            return respOrquesta;
        }
        catch(IOException e)
        {
            log.error("Ocurrió un detalle al consumir el metodo de retirarTarjetaOrquestador del orquestador",e);
            Respuesta respOrquesta = new Respuesta();
            respOrquesta.setCodigo(-1);
            respOrquesta.setData(null);
            respOrquesta.setMensaje("Ocurrio un error interno al tratar de realizar el retiro de la cuenta.");
            return respOrquesta;
        }
    }

    /**
     * Realiza el proceso de depósito del plastico utilizando el servicio de DOCK
     * @param map Contiene los valores del request recibidos en el WS
     * @param tarjeta valor de 16 digitos relacionado al plastico a consultar
     * @return Almacena el resultado de la operación.
     * */
    public Respuesta depositoTarjeta(Map<String, Object> map,String tarjeta)
    {
        TransaccionTarjetaOrquestadorOBJ reqTra = new TransaccionTarjetaOrquestadorOBJ();
        net.cero.ahorro.data.HeaderWS header = new HeaderWS();
        if(map.containsKey("banco_id"))
            if(map.get("banco_id") != null && map.get("banco_id").toString().matches("[0-9]*"))
                header.setIdBanco(map.get("banco_id").toString());
        if(map.containsKey("idSpei"))
            if(map.get("idSpei") != null && map.get("idSpei").toString().matches("[0-9]*"))
                reqTra.setIdSpei(Long.valueOf(map.get("idSpei").toString()));
        if(map.containsKey("claveRastreo"))
            if(map.get("claveRastreo") != null )
                reqTra.setClave_rastreo(map.get("claveRastreo").toString());
        if(map.containsKey("descripcion"))
            if(map.get("descripcion") != null )
                reqTra.setObservaciones(map.get("descripcion").toString());

        header.setIdEmpresa(1L);
        header.setIdCanalAtencion(2L);
        header.setIdSucursal(1L);
        header.setIdUsuario(9L);
        header.setIpHost(ToolsR._T(map.get("host")));
        header.setNameHost("EQUIPO-PC");
        header.setIdTransaccion(ToolsR._L(map.get("tipoTransaccionID")));
        reqTra.setCuenta(String.valueOf(map.get("cuentaID")));
        reqTra.setImporte(String.valueOf(ToolsR._D(map.get("monto"))));
        reqTra.setMedioPago(Constantes.MEDIO_PAGO_CACAO);
        reqTra.setConcepto(Constantes.DEBITO_CON_DEP);
        reqTra.setNumero_tarjeta(tarjeta);
        reqTra.setReferenciaNumerica(ReferenciasNumericas.generarRefNumerica());
        reqTra.setHeader(header);
        reqTra.setClaveMovimiento((map.containsKey("claveMovimientoDock") ? map.get("claveMovimientoDock").toString() : "" ));
        Gson gson = new Gson();
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient cliente = new OkHttpClient
                .Builder()
                .connectTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .readTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .writeTimeout(Constantes.TIEMPO_ESPERA_VALIDAR_OPERACION, TimeUnit.SECONDS)
                .build(); 
        String auth = Credentials.basic("ASP", "a5p2017$");
        String url = Constantes.SERVICIO_BASE_ADMIN_PLASTICOS + "/fondearTarjetaOrquestador";
        String body = gson.toJson(reqTra);
        log.info("URL del WS: {}",url);
        log.info("Request body para servicio : {}",body);
        
        try{
            Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
            Response response = cliente.newCall(request).execute();
            String obj = response.body().string();
            log.info("Respuesta orquestador: {}",obj);
            Respuesta respOrquesta = gson.fromJson(obj,Respuesta.class);
            response.body().close();
            return respOrquesta;
        }
        catch(IOException e)
        {
            log.error("Ocurrió un detalle al consumir el metodo de retirarTarjetaOrquestador del orquestador",e);
            Respuesta respOrquesta = new Respuesta();
            respOrquesta.setCodigo(-1);
            respOrquesta.setData(null);
            respOrquesta.setMensaje("Ocurrio un error interno al tratar de realizar el retiro de la cuenta.");
            return respOrquesta;
        }
    }
}
