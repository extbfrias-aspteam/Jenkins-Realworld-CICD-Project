package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.model.TipoReenvioOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase de la capa de datos con funciones relacionadas a la consylta de catalogos de tipos de operaciones que se pueden renviar
 * @author AASTORGA
 */
@Log4j2
@Repository
public class ReenviosMensajesDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String qConsultarTiposReenvios;
    private final String qbuscarTipoEnviosPorClave;

    public ReenviosMensajesDAO(@Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   @Value("${msgRee.dao.get.all}") String qConsultarTiposReenvios,
                               @Value("${msgRee.dao.get.clave}") String qbuscarTipoEnviosPorClave){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.qConsultarTiposReenvios = qConsultarTiposReenvios;
        this.qbuscarTipoEnviosPorClave = qbuscarTipoEnviosPorClave;
    }

    public List<TipoReenvioOBJ> consultarMovimientosReenvio()
    {
        List<TipoReenvioOBJ> listado = new ArrayList<>();
        try{
            listado = namedParameterJdbcTemplate.
                    query(qConsultarTiposReenvios,new BeanPropertyRowMapper<>(TipoReenvioOBJ.class));
        }
        catch(DataAccessException ex)
        {
            log.info("No se encontraron registros");
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al ejecutar la consulta del metodo consultarMovimientosReenvio",e);
        }
        return listado;
    }


    public TipoReenvioOBJ consultarMovimientosReenvioPorClave(String clave)
    {
        TipoReenvioOBJ resultado = null;
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("clave",clave);
            resultado = namedParameterJdbcTemplate.
                    queryForObject(qbuscarTipoEnviosPorClave,parametros,new BeanPropertyRowMapper<>(TipoReenvioOBJ.class));
        }
        catch(DataAccessException ex)
        {
            log.info("No se encontraron registros");
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al ejecutar la consulta del metodo consultarMovimientosReenvio",e);
        }
        return resultado;
    }
}
