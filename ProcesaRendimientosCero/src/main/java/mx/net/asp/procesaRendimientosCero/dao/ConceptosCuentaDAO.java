package mx.net.asp.procesaRendimientosCero.dao;

import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.ConceptoCuentaOBJ;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Log4j2
@Repository
public class ConceptosCuentaDAO {
    private final Properties queriesProcrea;
    private final JdbcTemplate procreaJdbc;
    private final NamedParameterJdbcTemplate namedProcreaJdbc;
    private final Environment env;
    private final ErrorHandler errorHandler;

    @Autowired
    public ConceptosCuentaDAO(
            @Qualifier("ceroJdbcTemplate") JdbcTemplate ceroJdbc,
            @Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedCeroJdbc,
            @Qualifier("procreaJdbcTemplate") JdbcTemplate procreaJdbc,
            @Qualifier("namedProcreaJdbcTemplate") NamedParameterJdbcTemplate namedProcreaJdbc,
            Environment env,
            ErrorHandler errorHandler
    ) {
        this.queriesProcrea = QueryLoader.loadYaml("procrea", "conceptos.yml");
        this.procreaJdbc = procreaJdbc;
        this.namedProcreaJdbc = namedProcreaJdbc;
        this.env = env;
        this.errorHandler = errorHandler;
    }

    public List<ConceptoCuentaOBJ> obtenerListadoConceptosInversionProcreaByCuentaAh(String personaId, String cuentaah, List<String> claves) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<Map<String, Object>> rows;
        List<ConceptoCuentaOBJ> conceptosList = new ArrayList<>();
        try {
            parameters.addValue("personaId", personaId);
            parameters.addValue("cuentaah", cuentaah);
            parameters.addValue("claves", claves);

            String query = "ahorro_conceptos.obtenerListadoConceptosInversionProcreaByCuentaAh";
            String psql = queriesProcrea.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    ConceptoCuentaOBJ conceptoCuentaOBJ = new ConceptoCuentaOBJ();
                    conceptoCuentaOBJ.setId(((Integer) row.get("id")));
                    conceptoCuentaOBJ.setClave((String) row.get("clave"));
                    conceptosList.add(conceptoCuentaOBJ);
                }
            } else {
                log.warn("No se encontraron conceptos (procrea) de la cuenta {}", cuentaah);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return Collections.emptyList(); // Retorna lista vacía en caso de error
        }
        return conceptosList.isEmpty() ? Collections.emptyList() : conceptosList;
    }

    public ConceptoCuentaOBJ obtenerConceptoCuentaProcreaByClave(String clave, String cuentaah) {
        ConceptoCuentaOBJ conceptoCuentaOBJOBJ = null;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        List<Map<String, Object>> rows;
        try {
            parameters.addValue("clave", clave);
            parameters.addValue("cuentaah", cuentaah);

            String query = "ahorro_conceptos.obtenerConceptoCuentaByClave";
            String psql = queriesProcrea.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                conceptoCuentaOBJOBJ = new ConceptoCuentaOBJ();

                conceptoCuentaOBJOBJ.setId(((Integer) row.get("id")));
                conceptoCuentaOBJOBJ.setValor((String) row.get("valor"));
            } else {
                log.error("No se encontro concepto cuenta Procrea con clave {} con cuenta {}", clave, cuentaah);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            conceptoCuentaOBJOBJ = null;
        }
        return conceptoCuentaOBJOBJ;
    }


    @Transactional(transactionManager = "transactionManagerProcrea")
    public RespuestaDTO guardarConceptoCuentaProcrea(String clave, String valor, String cuentaah) {
        RespuestaDTO respuesta = new RespuestaDTO();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("clave", clave);
            parameters.addValue("valor", valor);
            parameters.addValue("cuentaah", cuentaah);

            String query = "ahorro_conceptos.guardarConceptoCuenta";
            String psql = queriesProcrea.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(null);
            } else {
                log.error("No se logro guardar concepto cuenta Procrea con clave {}", clave);
                respuesta.setCodigo(-100);
                respuesta.setMensaje("No se logró guardar concepto cuenta Procrea con clave " + clave);
                respuesta.setData(null);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public RespuestaDTO actualizarConceptoCuentaProcreaById(Integer id, String valor) {
        RespuestaDTO respuesta = new RespuestaDTO();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("id", id);
            parameters.addValue("valor", valor);

            String query = "ahorro_conceptos.actualizarValorConceptoCuentaById";
            String psql = queriesProcrea.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(null);
            } else {
                log.error("No se logro actualizar concepto cuenta Procrea con id {}", id);
                respuesta.setCodigo(-100);
                respuesta.setMensaje("No se logró actualizar concepto cuenta Procrea con id " + id);
                respuesta.setData(null);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }
}
