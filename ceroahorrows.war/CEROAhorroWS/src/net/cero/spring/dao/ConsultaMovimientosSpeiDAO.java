package net.cero.spring.dao;

import lombok.extern.log4j.Log4j2;
import net.cero.model.MovimientoSpeiOBJ;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Repository
public class ConsultaMovimientosSpeiDAO {
    private final NamedParameterJdbcTemplate namedIzelStiDataSourceTemplate;
    private final String buscarUltimoMovimiento;

    public ConsultaMovimientosSpeiDAO(@Qualifier("namedIzelStiDataSourceTemplate") NamedParameterJdbcTemplate namedIzelStiDataSourceTemplate,
                                      @Value("${consSpei.dao.get.mov}") String buscarUltimoMovimiento) {
        this.namedIzelStiDataSourceTemplate = namedIzelStiDataSourceTemplate;
        this.buscarUltimoMovimiento = buscarUltimoMovimiento;
    }

    public MovimientoSpeiOBJ consultarUltimoMovimiento(String cuentaOrdenante, String cuentaBeneficiario, Double monto)
    {
        MovimientoSpeiOBJ resultado = null;
        try{
            Map<String,Object> parametros = new HashMap<>();
            parametros.put("cuentaOrd",cuentaOrdenante);
            parametros.put("cuentaBen",cuentaBeneficiario);
            parametros.put("monto",monto);

            resultado = (MovimientoSpeiOBJ)namedIzelStiDataSourceTemplate.queryForObject(buscarUltimoMovimiento,parametros
                    ,new BeanPropertyRowMapper(MovimientoSpeiOBJ.class));
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
