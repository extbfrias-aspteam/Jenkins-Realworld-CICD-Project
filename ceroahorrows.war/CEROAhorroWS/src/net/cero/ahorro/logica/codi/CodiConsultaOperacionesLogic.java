package net.cero.ahorro.logica.codi;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.data.codi.MovimientoCodiOBJ;
import net.cero.data.spei.InstitucionSpei;
import net.cero.model.CuentaAspOBJ;
import net.cero.req.codi.ConsultarOperacionesCodiReq;
import net.cero.res.codi.CodiMovimientoOBJ;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.ErroresWS;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.CatInstitucionesSpeiDAO;
import net.cero.spring.dao.CodiOperacionesDAO;
import net.cero.spring.dao.CuentaAspDAO;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase utilizada para modelar el comportamiento de la busqueda de operaciones Codi para una cuenta
 * @author AASTORGA
 */
@Log4j2
@Service
public class CodiConsultaOperacionesLogic {
    private final static Gson gson = ToolsR.GBuilder();
    private final CuentaAspDAO cuentaAspDao;
    private final CodiOperacionesDAO codiOperacionesDAO;
    private final CatInstitucionesSpeiDAO catInstitucionesSpeiDAO;
    public CodiConsultaOperacionesLogic(CodiOperacionesDAO codiOperacionesDAO,
                                        CatInstitucionesSpeiDAO catInstitucionesSpeiDAO)
    {
        ApplicationContext context = Apps.getInstance().getApplicationContext();
        this.cuentaAspDao = context.getBean("CuentaAspDao", CuentaAspDAO.class);
        this.codiOperacionesDAO = codiOperacionesDAO;
        this.catInstitucionesSpeiDAO = catInstitucionesSpeiDAO;
    }

    /**
     * Metodo empleado para realizar la consulta de operaciones de una cuenta en un periodo dado
     * @param req objeto que tiene los datos para realizar la busqueda de la operacion
     * @return Objeto del tipo Respuesta que regresa el resultado de la operacion
     */
    public Respuesta consultarOperaciones(ConsultarOperacionesCodiReq req)
    {
        Respuesta resultado = new Respuesta();
        try{
            CuentaAspOBJ cuenta = cuentaAspDao.buscaCuentaPorCuentaOCLABE(req.getCuenta());
            if(cuenta == null)
            {
                resultado.setCodigo(2);
                resultado.setMensaje("No se encontró una cuenta relacionado al dato proporcionado.");
                return resultado;
            }
            if(!StringUtils.isBlank(cuenta.getClabeInterbancaria()))
                req.setCuenta(cuenta.getClabeInterbancaria());

            List<MovimientoCodiOBJ> movimientos = codiOperacionesDAO.consultaMovimientosCodi(req);

            if(movimientos != null && !movimientos.isEmpty())
            {
                List<CodiMovimientoOBJ> data = new ArrayList<>();
                data = movimientos.parallelStream().map(x -> {
                            InstitucionSpei banOrigen =  (!StringUtils.isBlank(x.getBancoOrigen()) ? catInstitucionesSpeiDAO.buscarInstitucion(x.getBancoOrigen()):null);
                            InstitucionSpei banDestino = (!StringUtils.isBlank(x.getBancoOrigen()) ? catInstitucionesSpeiDAO.buscarInstitucion(x.getBancoDestino()):null);;
                            CodiMovimientoOBJ obj = new CodiMovimientoOBJ(x.getCuentaComprador(),x.getCuentaVendedor(),
                                    null,x.getMonto(),x.getEstatus(),
                                    (banOrigen != null && !StringUtils.isBlank(banOrigen.getNombre()) ? banOrigen.getNombre().trim() :"N/P"),
                                    (banDestino != null && !StringUtils.isBlank(banDestino.getNombre()) ? banDestino.getNombre().trim() :"N/P"));
                            if(x.getFechaProcesamiento() != null)
                                obj.setFechaOperacion(x.getFechaProcesamiento().toLocalDateTime()
                                        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")));
                            return obj;
                        })
                        .collect(Collectors.toList());
                resultado.setMensaje("Consulta Exitosa");
                resultado.setData(gson.toJson(data));
            }
            else
            {
                resultado.setMensaje(ErroresWS.descError.get(ErroresWS.NO_REGISTROS_CODI));
                resultado.setCodigo(ErroresWS.NO_REGISTROS_CODI);
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al ejecutar la funcion ejecutarReenvioMensaje ",e);
            resultado.setCodigo(ErroresWS.ERROR_INTERNO);
            resultado.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
        }
        return resultado;
    }
}
