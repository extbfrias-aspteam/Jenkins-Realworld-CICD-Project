package mx.net.asp.procesaRendimientosCero.dao;

import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

@Log4j2
@Repository
public class MovimientosCajaDAO {
    private final Properties queries;
    private final JdbcTemplate procreaJdbc;
    private final NamedParameterJdbcTemplate namedProcreaJdbc;
    private final ErrorHandler errorHandler;

    @Autowired
    public MovimientosCajaDAO(
            @Qualifier("procreaJdbcTemplate") JdbcTemplate procreaJdbc,
            @Qualifier("namedProcreaJdbcTemplate") NamedParameterJdbcTemplate namedProcreaJdbc,
            ErrorHandler errorHandler
    ) {
        this.queries = QueryLoader.loadYaml("procrea", "movimientoscaja.yml");
        this.procreaJdbc = procreaJdbc;
        this.namedProcreaJdbc = namedProcreaJdbc;
        this.errorHandler = errorHandler;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void actualizarAhorroSaldos(String cuentaInversion, LocalDate ppfecha, LocalDate pp_feccalc) {
        RespuestaDTO respuesta = new RespuestaDTO();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);
            parameters.addValue("pp_fecha", ppfecha);
            parameters.addValue("pp_feccalc", pp_feccalc);

            String query = "saldo.actualizarAhorroSaldos";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parámetros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {

            } else {
                log.error("No se logro actualizar el saldo en ahorro saldos");
                RespuestaUtils.asignarError(respuesta, -100, "No se logró actualizar el saldo en ahorro saldos");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
    }

    public int actualizarAvisosDeRetiro(String cuenta, LocalDate fechaLimite) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("pp_cuenta", cuenta);
            parameters.addValue("pp_fecha", Date.valueOf(fechaLimite)); // java.sql.Date

            String queryKey = "ahorro_avisos_retiro.actualizaAvisoRetiro";
            String sql = queries.getProperty(queryKey);
            log.info("Ejecutando consulta: {}, parámetros: {}", queryKey, parameters.getValues());

            return namedProcreaJdbc.update(sql, parameters);
        } catch (Exception e) {
            errorHandler.handleException(e);
            return 0;
        }
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public Integer ejecutarDepositoAhorro(Integer caja, LocalDate fecha, Integer usuario, String cuenta, Double monto, String observaciones,
                                         Integer movtoId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("pcaja", caja);
            parameters.addValue("pfecha", fecha);
            parameters.addValue("puser", usuario);
            parameters.addValue("pcuenta", cuenta);
            parameters.addValue("pmonto", monto);
            parameters.addValue("pformapago", 4);
            parameters.addValue("pbanco", 0);
            parameters.addValue("pobs", observaciones);
            parameters.addValue("pcheque", "");
            parameters.addValue("pmovto", movtoId);
            parameters.addValue("ptransaccion_id", null);
            parameters.addValue("ptarjeta_operativa_id", null);
            parameters.addValue("papp", null);
            parameters.addValue("ptransaccion_version_id", null);
            parameters.addValue("p_para_conciliar", 0);
            parameters.addValue("p_fecha_deposito", null);

            String query = "funciones.cajaDepositoAhorro";
            String sql = queries.getProperty(query);
            log.info("Ejecutando función: {}, parámetros: {}", query, parameters.getValues());

            return namedProcreaJdbc.queryForObject(sql, parameters, Integer.class);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            return null;
        }
    }
}
