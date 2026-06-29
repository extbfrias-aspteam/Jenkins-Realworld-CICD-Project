package mx.net.asp.procesaRendimientosCero.dao;

import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.model.SolicitanteOBJ;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Log4j2
@Repository
public class SolicitanteDAO {
    private final Properties queries;
    private final JdbcTemplate procreaJdbc;
    private final NamedParameterJdbcTemplate namedProcreaJdbc;
    private final ErrorHandler errorHandler;

    @Autowired
    public SolicitanteDAO(
            @Qualifier("procreaJdbcTemplate") JdbcTemplate procreaJdbc,
            @Qualifier("namedProcreaJdbcTemplate") NamedParameterJdbcTemplate namedProcreaJdbc,
            ErrorHandler errorHandler
    ) {
        this.queries = QueryLoader.loadYaml("procrea", "solicitante.yml");
        this.procreaJdbc = procreaJdbc;
        this.namedProcreaJdbc = namedProcreaJdbc;
        this.errorHandler = errorHandler;
    }

    public SolicitanteOBJ obtenerInfoBasicaSolicitanteById(String idSolicitante) {
        SolicitanteOBJ solicitanteOBJ = null;
        List<Map<String, Object>> rows;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            //Se definen los parametros para el query
            parameters.addValue("numero", idSolicitante);

            String query = "solicitante.obtenerDatosBasicosById";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                solicitanteOBJ = new SolicitanteOBJ();

                solicitanteOBJ.setNumero((String) row.get("numero"));
                solicitanteOBJ.setNombre((String) row.get("nombre"));
                solicitanteOBJ.setCurp((String) row.get("curp"));
                solicitanteOBJ.setRfc((String) row.get("rfc"));
                solicitanteOBJ.setCorreo((String) row.get("correo"));
                solicitanteOBJ.setTelefonoCoDi((String) row.get("telefono"));
            } else {
                log.error("No se encontro informacion del solicitante {}", idSolicitante);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            solicitanteOBJ = null;
        }
        return solicitanteOBJ;
    }
}
