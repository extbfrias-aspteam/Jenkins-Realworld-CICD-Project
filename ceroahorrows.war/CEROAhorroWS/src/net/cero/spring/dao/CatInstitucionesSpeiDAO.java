package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.data.spei.InstitucionSpei;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Clase para exponer funciones relacionados al catalogo de instituciones SPEI
 */
@Repository
@Log4j2
public class CatInstitucionesSpeiDAO {
    private final NamedParameterJdbcTemplate namedIzelStiDataSourceTemplate;
    private final String qBuscarInstitucionSpei;

    public CatInstitucionesSpeiDAO(@Qualifier("namedIzelStiDataSourceTemplate")NamedParameterJdbcTemplate namedIzelStiDataSourceTemplate,
                                   @Value("${spei.dao.catalogo.institucion}") String qBuscarInstitucionSpei) {
        this.namedIzelStiDataSourceTemplate = namedIzelStiDataSourceTemplate;
        this.qBuscarInstitucionSpei = qBuscarInstitucionSpei;
    }

    /**
     * Metodo para buscar una institución SPEI dentro del catalogo de instituciones que se encuentra en la base de izelSTI
     * @param idInstitucion id de la institucion SPEI
     * @return Objeto InstitucionSpei con la info de la institución
     */
    public InstitucionSpei buscarInstitucion(String idInstitucion)
    {
        InstitucionSpei resultado = new InstitucionSpei();
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("idInstitucion",idInstitucion);
            resultado = this.namedIzelStiDataSourceTemplate.queryForObject(qBuscarInstitucionSpei
                    ,parametros,new BeanPropertyRowMapper<>(InstitucionSpei.class));
        }
        catch(DataAccessException ex)
        {
            log.info("No se encontraron resultados idInstitucion: {}",idInstitucion);
            resultado=null;
        }
        catch(Exception e)
        {
            resultado = null;
            log.error("Ocurrió un error al tratar de ejecutar la busqueda dentro del metodo buscarInstitucion.", e);
        }
        return resultado;
    }
}
