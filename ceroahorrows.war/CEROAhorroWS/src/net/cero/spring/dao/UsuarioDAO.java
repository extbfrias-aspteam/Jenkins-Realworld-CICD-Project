package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.model.UsuarioOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class UsuarioDAO {

    private final JdbcTemplate jdbcTemplatePr;
    private final String buscarUsuarioPorId;

    public UsuarioDAO(@Qualifier("procreaJdbcTemplate") JdbcTemplate jdbcTemplatePr
            ,@Value("${usuarios.dao.get.id}") String buscarUsuarioPorId) {
        this.jdbcTemplatePr = jdbcTemplatePr;
        this.buscarUsuarioPorId = buscarUsuarioPorId;
    }

    public UsuarioOBJ consultaUsuarioPorId(Integer usuarioId)
    {
        UsuarioOBJ obj = new UsuarioOBJ();
        try{
            obj = (UsuarioOBJ)jdbcTemplatePr.queryForObject(buscarUsuarioPorId,new BeanPropertyRowMapper(UsuarioOBJ.class)
                    ,usuarioId);
        }
        catch(DataAccessException e )
        {
            log.info("No se encontraron registros.");
        }
        catch(Exception e2)
        {
            log.error("Error al ejecutar consultaUsuarioPorId.",e2);
        }
        return obj;
    }
}
