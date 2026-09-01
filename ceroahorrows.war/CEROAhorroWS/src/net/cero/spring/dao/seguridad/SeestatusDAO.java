package net.cero.spring.dao.seguridad;

import lombok.extern.log4j.Log4j2;
import net.cero.data.seguridad.SeestatusOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Log4j2
public class SeestatusDAO {
    private final NamedParameterJdbcTemplate ceroJdbcTemplate;
    private final String qConsultaEstatusPorNombre;

    public SeestatusDAO(@Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate ceroJdbcTemplate,
                        @Value("${seestatus.dao.get.por-nombre}") String qConsultaEstatusPorNombre) {
        this.ceroJdbcTemplate = ceroJdbcTemplate;
        this.qConsultaEstatusPorNombre = qConsultaEstatusPorNombre;
    }

    public SeestatusOBJ findSeestatusByNombre(String nombre)
    {
        SeestatusOBJ resultado = new SeestatusOBJ();
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("nombre",nombre);
            resultado = ceroJdbcTemplate.queryForObject(this.qConsultaEstatusPorNombre,parametros
                    , new BeanPropertyRowMapper<>(SeestatusOBJ.class));
        }
        catch(Exception e)
        {
            log.error("Error al tratar de consultar el catalogo de estatus",e);
        }
        return resultado;
    }
}
