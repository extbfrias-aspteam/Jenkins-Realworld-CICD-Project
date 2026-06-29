package mx.net.asp.procesaRendimientosCero.dao;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.ResultadoProcesaRendimientos;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Log4j2
@Repository
public class AhorroDAO {
    private final Properties queries;
    private final JdbcTemplate ceroJdbc;
    private final NamedParameterJdbcTemplate namedCeroJdbc;
    private final JdbcTemplate procreaJdbc;
    private final NamedParameterJdbcTemplate namedProcreaJdbc;
    private final ErrorHandler errorHandler;

    @Autowired
    public AhorroDAO(
            @Qualifier("ceroJdbcTemplate") JdbcTemplate ceroJdbc,
            @Qualifier("namedCeroJdbcTemplate") NamedParameterJdbcTemplate namedCeroJdbc,
            @Qualifier("procreaJdbcTemplate") JdbcTemplate procreaJdbc,
            @Qualifier("namedProcreaJdbcTemplate") NamedParameterJdbcTemplate namedProcreaJdbc,
            ErrorHandler errorHandler
    ) {
        this.queries = QueryLoader.loadYaml("cero", "ahorro.yml");
        this.ceroJdbc = ceroJdbc;
        this.namedCeroJdbc = namedCeroJdbc;
        this.procreaJdbc = procreaJdbc;
        this.namedProcreaJdbc = namedProcreaJdbc;
        this.errorHandler = errorHandler;
    }

    @Transactional(transactionManager = "transactionManagerCero")
    public RespuestaDTO grabarTraspasoCuentas(String tipoTraspaso, String cuentaOrigen, String cuentaDetino, Integer idRetiro, Integer idDeposito, Double monto) {
        RespuestaDTO respuesta = new RespuestaDTO();
        int rows;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Gson gson = new Gson();
        Integer idSecTraspaso;
        try {
            idSecTraspaso = obtenerSecuenciaTraspasoCuentas();

            parameters.addValue("id", idSecTraspaso);
            parameters.addValue("tipo_trasp", tipoTraspaso);
            parameters.addValue("cuenta_ori", cuentaOrigen);
            parameters.addValue("cuenta_dest", cuentaDetino);
            parameters.addValue("id_retiro", idRetiro);
            parameters.addValue("id_deposito", idDeposito);
            parameters.addValue("monto", monto);

            String query = "ahtraspasos_cuentas.grabarTraspasoCuentas";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedCeroJdbc.update(psql, parameters);
            if (rows > 0) {
                respuesta.setCodigo(0);
                respuesta.setMensaje("OK");
                respuesta.setData(null);
            } else {
                log.error("No se logro registrar el traspaso entre cuentas");
                respuesta.setCodigo(-100);
                respuesta.setMensaje("No se logro registrar el traspaso entre cuentas");
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

    @Transactional(transactionManager = "transactionManagerCero")
    public Integer obtenerSecuenciaTraspasoCuentas() {
        try {
            String psql = queries.getProperty("secuencias.sigSecTraspasoCuentas");
            return ceroJdbc.queryForObject(psql, Integer.class);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo obtener la secuencia de traspaso cuentas:: " + e.getMessage()) {
            };
        }
    }

    public void insertarLogResultadoBatch(List<ResultadoProcesaRendimientos> lista) {
        try {
            String queryKey = "ahorro_log_proceso_rendimiento.insertarLogProcesoRendimiento";
            String sql = queries.getProperty(queryKey);

            SqlParameterSource[] batchParams = lista.stream()
                    .map(item -> {
                        MapSqlParameterSource param = new MapSqlParameterSource();
                        param.addValue("cuenta_inversion", item.getCuentaInversion());
                        param.addValue("cuenta_padre", item.getCuentaPadre());
                        param.addValue("proceso", item.getProceso());
                        param.addValue("resultado", item.getResultado());
                        param.addValue("mensaje", item.getMensaje());

                        // Metadatos
                        param.addValue("usuario_creacion", 9L);
                        param.addValue("fecha_creacion", Timestamp.valueOf(LocalDateTime.now()));
                        param.addValue("usuario_modificacion", null);
                        param.addValue("fecha_modificacion", null);
                        return param;
                    })
                    .toArray(SqlParameterSource[]::new);

            namedCeroJdbc.batchUpdate(sql, batchParams);

            log.info("Se insertaron {} registros en log_proceso_rendimiento", lista.size());
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
    }
}
