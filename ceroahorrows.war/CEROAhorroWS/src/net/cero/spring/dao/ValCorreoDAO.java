package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.data.PlvalidacionCorreo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Log4j2
public class ValCorreoDAO {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final String qConsultaRegValidacionCorreo;

    public ValCorreoDAO(@Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedJdbcTemplate,
                        @Value("${valcorreo.dao.get.correo}") String qConsultaRegValidacionCorreo) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.qConsultaRegValidacionCorreo = qConsultaRegValidacionCorreo;
    }

    public PlvalidacionCorreo obtenerUltimoRegistroValCorreo(String correo,String estado)
    {
        PlvalidacionCorreo plvalidacionCorreo=null;
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("correo",correo);
            parametros.put("estado",estado);
            plvalidacionCorreo=namedJdbcTemplate
                    .queryForObject(qConsultaRegValidacionCorreo,parametros, new BeanPropertyRowMapper<>(PlvalidacionCorreo.class));
        }
        catch(DataAccessException ex)
        {
            log.info("No se encontraron registros");
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al ejecutar la consulta del metodo obtenerUltimoRegistroValCorreo",e);
        }
        return plvalidacionCorreo;
    }
}
