package net.cero.ahorro.logica.codi;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.data.codi.MovimientoCodiOBJ;
import net.cero.data.spei.InstitucionSpei;
import net.cero.req.codi.ConsultarEstatusCodiReq;
import net.cero.res.codi.CodiMovimientoOBJ;
import net.cero.seguridad.utilidades.ErroresWS;
import net.cero.spring.dao.CatInstitucionesSpeiDAO;
import net.cero.spring.dao.CodiOperacionesDAO;
import net.cero.spring.dao.CuentaAspDAO;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


/**
 * Clase de la capa de negocios que encapsula el comportamiento relacionado a la consulta de estatus de una operacion CoDi
 * @author AASTORGA
 */
@Log4j2
@Service
@AllArgsConstructor
public class CodiConsultaEstatusLogic {
    private final static Gson gson = ToolsR.GBuilder();
    private final CodiOperacionesDAO codiOperacionesDAO;
    private final CatInstitucionesSpeiDAO catInstitucionesSpeiDAO;

    /**
     * Metodo para consulta la información de una operacion CoDi en particular.
     * @param req objeto que tiene los datos para realizar la busqueda de la operacion
     * @return Objeto del tipo Respuesta que regresa el resultado de la operacion
     */
    public Respuesta consultarEstatus(ConsultarEstatusCodiReq req)
    {
        Respuesta resultado = new Respuesta();
        try{
            MovimientoCodiOBJ movimiento = codiOperacionesDAO.consultarEstatusOperacion(req);
            if(movimiento != null)
            {
                CodiMovimientoOBJ data = new CodiMovimientoOBJ();
                InstitucionSpei banOrigen =  (!StringUtils.isBlank(movimiento.getBancoOrigen()) ? catInstitucionesSpeiDAO.buscarInstitucion(movimiento.getBancoOrigen()):null);
                InstitucionSpei banDestino = (!StringUtils.isBlank(movimiento.getBancoOrigen()) ? catInstitucionesSpeiDAO.buscarInstitucion(movimiento.getBancoDestino()):null);;
                data.setBancoOrigen(banOrigen != null && !StringUtils.isBlank(banOrigen.getNombre()) ? banOrigen.getNombre().trim() :"N/P");
                data.setBancoDestino(banDestino != null && !StringUtils.isBlank(banDestino.getNombre()) ? banDestino.getNombre().trim() :"N/P");

                data.setCuentaOrigen(movimiento.getCuentaComprador());
                data.setCuentaDestino(movimiento.getCuentaVendedor());

                data.setEstatus(movimiento.getEstatus());
                if(movimiento.getFechaProcesamiento() != null)
                    data.setFechaOperacion(movimiento.getFechaProcesamiento().toLocalDateTime().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")));
                data.setMonto(movimiento.getMonto());
                resultado.setMensaje("Consulta Exitosa");
                resultado.setData(gson.toJson(data));
            }
            else
            {
                resultado.setMensaje(ErroresWS.descError.get(ErroresWS.NO_DATOS_REFERENCIA));
                resultado.setCodigo(ErroresWS.NO_DATOS_REFERENCIA);
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
