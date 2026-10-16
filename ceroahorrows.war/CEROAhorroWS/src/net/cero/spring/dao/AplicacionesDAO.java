package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.model.AplicacionOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Repository
public class AplicacionesDAO {
    private final NamedParameterJdbcTemplate namedCeroDataSourceTemplate;
    private final String qBuscarDatosAplicacion;

    public AplicacionesDAO(
            @Qualifier("namedCeroJdbcTemplate")NamedParameterJdbcTemplate namedCeroDataSourceTemplate,
            @Value("${app.dao.get.clave}") String qBuscarDatosAplicacion) {
        this.namedCeroDataSourceTemplate = namedCeroDataSourceTemplate;
        this.qBuscarDatosAplicacion = qBuscarDatosAplicacion;
    }

    public AplicacionOBJ consultarDatosAplicacion(String clave)
    {
        AplicacionOBJ resultado = null;
        try{
            Map<String,Object> parametros = new HashMap<>();
            parametros.put("clave",clave);
            resultado = namedCeroDataSourceTemplate.queryForObject(qBuscarDatosAplicacion,parametros,
                    new BeanPropertyRowMapper<>(AplicacionOBJ.class));
        }
        catch(DataAccessException e )
        {
            log.info("No se encontraron registros.");
        }
        catch(Exception e2)
        {
            log.error("Error al ejecutar consultarUltimoMovimiento.",e2);
        }
        return resultado;
    }
}
