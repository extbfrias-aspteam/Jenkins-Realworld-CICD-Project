package net.cero.spring.dao.seguridad;

import lombok.extern.log4j.Log4j2;
import net.cero.data.seguridad.SesionesActivasOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Log4j2
public class SeautenticadoDAO {
    private final NamedParameterJdbcTemplate ceroJdbcTemplate;
    private final String qSeautenticadosPorUsuario;
    private final String qActualizaEstatusAutenticado;

    public SeautenticadoDAO(@Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate ceroJdbcTemplate,
                            @Value("${seautenticado.dao.get.sesiones-activas}") String qSeautenticadosPorUsuario,
                            @Value("${seautenticado.dao.update.sesiones-activas}") String qActualizaEstatusAutenticado) {
        this.ceroJdbcTemplate = ceroJdbcTemplate;
        this.qSeautenticadosPorUsuario = qSeautenticadosPorUsuario;
        this.qActualizaEstatusAutenticado = qActualizaEstatusAutenticado;
    }

    public List<SesionesActivasOBJ> findSeautenticadosByUsuario(String idEstatus,String usuario,int idAplicativo)
    {
        List<SesionesActivasOBJ> resultado = null;
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("usuario",usuario);
            parametros.put("idEstatus",idEstatus);
            parametros.put("idAplicativo",idAplicativo);
            resultado = ceroJdbcTemplate.query(this.qSeautenticadosPorUsuario,parametros,
                    new BeanPropertyRowMapper<>(SesionesActivasOBJ.class));
        }
        catch(Exception e)
        {
            log.error("Error al tratar de consultar las sesiones activas del usuario: {}",usuario,e);
        }
        return resultado;
    }

    public boolean updateSeautenticado(String idEstatus,String usuario,int usuarioModificacion,int idAplicativo)
    {
        boolean resultado = false;
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("usuario",usuario);
            parametros.put("idEstatus",idEstatus);
            parametros.put("usuarioModificacion",usuarioModificacion);
            parametros.put("idAplicativo",idAplicativo);
            ceroJdbcTemplate.update(this.qActualizaEstatusAutenticado,parametros);
            resultado = true;
        }
        catch(Exception e)
        {
            log.error("Error al tratar de consultar las sesiones activas del usuario: {}",usuario,e);
        }
        return resultado;
    }
}
