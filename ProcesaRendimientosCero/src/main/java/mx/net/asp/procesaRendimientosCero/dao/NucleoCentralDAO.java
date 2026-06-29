package mx.net.asp.procesaRendimientosCero.dao;

import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.PosGlobalCuentaOBJ;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Log4j2
@Repository
public class NucleoCentralDAO {
    private final Properties queries;
    private final JdbcTemplate ceroJdbc;
    private final NamedParameterJdbcTemplate namedCeroJdbc;
    private final ErrorHandler errorHandler;

    @Autowired
    public NucleoCentralDAO(
            @Qualifier("ceroJdbcTemplate") JdbcTemplate ceroJdbc,
            @Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedCeroJdbc,
            ErrorHandler errorHandler
    ) {
        this.queries = QueryLoader.loadYaml("cero", "nucleocentral.yml");
        this.ceroJdbc = ceroJdbc;
        this.namedCeroJdbc = namedCeroJdbc;
        this.errorHandler = errorHandler;
    }

    public PosGlobalCuentaOBJ obtenerCuentaPosicionGlobalByCuentaAh(String cuentaah) {
        PosGlobalCuentaOBJ posGlobCuentaObj = null;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<Map<String, Object>> rows;
        try {
            parameters.addValue("cuentaah", cuentaah);

            String query = "ncposicionglobalah.obtenerCuentaPosicionGlobalByCuentaAh";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedCeroJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                posGlobCuentaObj = new PosGlobalCuentaOBJ();

                posGlobCuentaObj.setTipoCuenta((String) row.get("tipocuentaah"));
                posGlobCuentaObj.setCuentaah((String) row.get("cuentaah"));
                posGlobCuentaObj.setClabe((String) row.get("clabe_interbancaria"));
                posGlobCuentaObj.setEstatus((String) row.get("estatusah"));
                posGlobCuentaObj.setCuenta_cobro((String) row.get("cuenta_cobro"));
                posGlobCuentaObj.setCuenta_pago((String) row.get("cuenta_pago"));
                posGlobCuentaObj.setPersonaId((String) row.get("idpersona"));
                posGlobCuentaObj.setTienePlastico(false);

                if (!(StringUtils.isBlank((String) row.get("tarjeta_principal")))) {
                    posGlobCuentaObj.setTarjetaVirtual((String) row.get("tarjeta_principal"));
                    posGlobCuentaObj.setTienePlastico(true);
                }
                if (!(StringUtils.isBlank((String) row.get("tarjeta_adicional")))) {
                    posGlobCuentaObj.setTarjetaFisica((String) row.get("tarjeta_adicional"));
                    posGlobCuentaObj.setTienePlastico(true);
                }
            } else {
                log.error("No se encontro informacion para la cuenta {}", cuentaah);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            posGlobCuentaObj = null;
        }
        return posGlobCuentaObj;
    }
}
