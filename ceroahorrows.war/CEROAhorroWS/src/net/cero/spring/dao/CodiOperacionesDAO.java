package net.cero.spring.dao;


import lombok.extern.log4j.Log4j2;
import net.cero.data.codi.MovimientoCodiOBJ;
import net.cero.req.codi.ConsultarEstatusCodiReq;
import net.cero.req.codi.ConsultarOperacionesCodiReq;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase de la capa de datos para modelar las operaciones relacionadas a las consultas de operaciones CoDi
 * @author AASTORGA
 */
@Repository
@Log4j2
public class CodiOperacionesDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String qConsultaOperacionesCobro;
    private final String qConsultaOperacionesPago;

    private final String qConsultaOperacionesCobroPorReferencia;
    private final String qConsultaOperacionesPagoPorReferencia;
    public CodiOperacionesDAO(@Qualifier("namedCeroJdbcTemplate")NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              @Value("${codi.dao.operaciones.cobro}") String qConsultaOperacionesCobro,
                              @Value("${codi,dao.operaciones.pago}")String qConsultaOperacionesPago,
                              @Value("${codi.dao.operacion.cobro.referencia}")String qConsultaOperacionesCobroPorReferencia,
                              @Value("${codi.dao.operacion.pago.referencia}")String qConsultaOperacionesPagoPorReferencia) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.qConsultaOperacionesCobro = qConsultaOperacionesCobro;
        this.qConsultaOperacionesPago = qConsultaOperacionesPago;
        this.qConsultaOperacionesCobroPorReferencia = qConsultaOperacionesCobroPorReferencia;
        this.qConsultaOperacionesPagoPorReferencia = qConsultaOperacionesPagoPorReferencia;
    }

    /**
     * Permite realizar la busqueda de operaciones CoDi de una cuenta en un periodo dado de fechas
     * @param req objeto que tiene los datos para realizar la busqueda de la operacion
     * @return Regresa un listado de MovimientoCodiOBJ con el resultado de la consulta en caso de regresar dato alguno
     */
    public List<MovimientoCodiOBJ> consultaMovimientosCodi(ConsultarOperacionesCodiReq req)
    {
        List<MovimientoCodiOBJ> resultado = new ArrayList<>();
        try{
            final String query = (req.getTipoOperacion().equals("P") ? this.qConsultaOperacionesPago : this.qConsultaOperacionesCobro);
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("cuenta",req.getCuenta());
            parametros.put("fechaInicio",req.getFechaInicio());
            parametros.put("fechaFin",req.getFechaFin());
            resultado = this.namedParameterJdbcTemplate.query(query,parametros,new BeanPropertyRowMapper<>(MovimientoCodiOBJ.class));
        }
        catch(EmptyResultDataAccessException ex)
        {
            log.info("No se encontraron resultados");
        }
        catch(Exception e)
        {
            resultado = null;
            log.error("Ocurrió un error al tratar de ejecutar la busqueda dentro del metodo consultaMovimientosCodi.", e);
        }
        return resultado;
    }

    /**
     * Permite traer la info de una operacion codi en particular por medio de la busqueda de una referencia o folio y de una fecha de operacion
     * @param req objeto que tiene los datos para realizar la busqueda de la operacion
     * @return Regreasa un objeto del tipo MovimientoCodiOBJ con la info de la operacion
     */
    public MovimientoCodiOBJ consultarEstatusOperacion(ConsultarEstatusCodiReq req)
    {
        MovimientoCodiOBJ resultado = new MovimientoCodiOBJ();
        try{
            String query = (req.getTipoOperacion().equals("P") ? this.qConsultaOperacionesPagoPorReferencia : this.qConsultaOperacionesCobroPorReferencia);
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("fechaInicio",req.getFechaOperacion());
            parametros.put("fechaFin",req.getFechaOperacion());
            parametros.put("referencia",req.getReferencia());
            List<MovimientoCodiOBJ> resultadoQuery = this.namedParameterJdbcTemplate.query(query,parametros,new BeanPropertyRowMapper<>(MovimientoCodiOBJ.class));
            if(!resultadoQuery.isEmpty())
                resultado = resultadoQuery.stream().findFirst().orElse(null);
            else
                resultado = null;
        }
        catch(EmptyResultDataAccessException ex)
        {
            resultado = null;
            log.info("No se encontraron resultados");
        }
        catch(Exception e)
        {
            resultado = null;
            log.error("Ocurrió un error al tratar de ejecutar la busqueda dentro del metodo consultaMovimientosCodi.", e);
        }
        return resultado;
    }

}
