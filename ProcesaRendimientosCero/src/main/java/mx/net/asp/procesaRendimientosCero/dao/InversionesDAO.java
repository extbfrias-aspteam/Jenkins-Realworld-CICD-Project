package mx.net.asp.procesaRendimientosCero.dao;

import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.config.QueryLoader;
import mx.net.asp.procesaRendimientosCero.model.*;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.sql.Date;

@Log4j2
@Repository
public class InversionesDAO {
    private final Properties queries;
    private final JdbcTemplate procreaJdbc;
    private final NamedParameterJdbcTemplate namedProcreaJdbc;
    private final ErrorHandler errorHandler;

    @Value("${reinversiones.ahorro-no-invertir.motivo-id}")
    private Integer motivoIdNoReinversion;

    @Value("${reinversiones.ahorro-no-invertir.observaciones}")
    private String observacionesNoReinversion;

    @Autowired
    public InversionesDAO(
            @Qualifier("procreaJdbcTemplate") JdbcTemplate procreaJdbc,
            @Qualifier("namedProcreaJdbcTemplate") NamedParameterJdbcTemplate namedProcreaJdbc,
            ErrorHandler errorHandler
    ) {
        this.queries = QueryLoader.loadYaml("procrea", "inversiones.yml");
        this.procreaJdbc = procreaJdbc;
        this.namedProcreaJdbc = namedProcreaJdbc;
        this.errorHandler = errorHandler;
    }

    public List<RendimientoPendOBJ> obtenerListadoRendimientos(String estatus) {
        List<Map<String, Object>> rows;
        List<RendimientoPendOBJ> rendimientoPendList = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("estatus", estatus);

            String query = "ahorro_rendimientos_cero.obtenerColaRendimientos";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    RendimientoPendOBJ obj = new RendimientoPendOBJ();
                    obj.setId((Long) row.get("id"));
                    obj.setCuentaInversion((String) row.get("cuenta_inv"));
                    obj.setCuentaPadre((String) row.get("cuenta_des"));
                    obj.setMonto((BigDecimal) row.get("monto"));
                    obj.setIdMov((Integer) row.get("idmov"));
                    obj.setEstatus((String) row.get("estatus"));
                    //obj.setFechaRend((LocalDate) row.get("fecha_rend"));
                    Object fechaObj = row.get("fecha_rend");
                    if (fechaObj != null) {
                        java.sql.Date sqlDate = (java.sql.Date) fechaObj;
                        obj.setFechaRend(sqlDate.toLocalDate());
                    } else {
                        obj.setFechaRend(null);
                    }
                    rendimientoPendList.add(obj);
                }
            } else {
                log.error("No se encontraron rendimientos pendientes de procesar");
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return Collections.emptyList(); // Retorna lista vacía en caso de error
        }
        return rendimientoPendList.isEmpty() ? Collections.emptyList() : rendimientoPendList;
    }

    public List<ReinversionPendOBJ> obtenerListadoReinversiones(String estatus) {
        List<Map<String, Object>> rows;
        List<ReinversionPendOBJ> reinversionPendList = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("estatus", estatus);

            String query = "ahorro_reinversiones_cero.obtenerColaReinversiones";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                for (Map<String, Object> row : rows) {
                    ReinversionPendOBJ obj = new ReinversionPendOBJ();
                    obj.setId((Long) row.get("id"));
                    obj.setCuentaInversion((String) row.get("cuenta_inv"));
                    obj.setCuentaPadre((String) row.get("cuenta_padre"));
                    obj.setEstatus((String) row.get("estatus"));
                    //obj.setFechaCalc((LocalDate) row.get("pp_feccalc"));
                    Object fechaObj = row.get("pp_feccalc");
                    if (fechaObj != null) {
                        java.sql.Date sqlDate = (java.sql.Date) fechaObj;
                        obj.setFechaCalc(sqlDate.toLocalDate());
                    } else {
                        log.info("Fecha calc null");
                        obj.setFechaCalc(null);
                    }
                    //obj.setFechaPlazo((LocalDate) row.get("fecha_plazo"));
                    fechaObj = row.get("fecha_plazo");
                    if (fechaObj != null) {
                        java.sql.Date sqlDate = (java.sql.Date) fechaObj;
                        obj.setFechaPlazo(sqlDate.toLocalDate());
                    } else {
                        obj.setFechaPlazo(null);
                    }
                    reinversionPendList.add(obj);
                }
            } else {
                log.error("No se encontraron reinversiones pendientes de procesar");
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return Collections.emptyList(); // Retorna lista vacía en caso de error
        }
        return reinversionPendList.isEmpty() ? Collections.emptyList() : reinversionPendList;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void actualizaEstatusRendimientoPendById(Long id, String estatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("id", id);
            parameters.addValue("estatus", estatus);

            String query = "ahorro_rendimientos_cero.actualizaEstatusRendimientoPendById";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
        }
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void actualizaEstatusReinversionPendById(Long id, String estatus) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("id", id);
            parameters.addValue("estatus", estatus);

            String query = "ahorro_reinversiones_cero.actualizaEstatusReinversionPendById";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
        }
    }

    public ModalidadOBJ obtenerModalidadByTipoId(Integer modalidadId) {
        List<Map<String, Object>> rows;
        ModalidadOBJ modalidadOBJ = null;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("tipo_id", modalidadId);

            String query = "modalidades.obtenerModalidadByTipoId";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                modalidadOBJ = new ModalidadOBJ();
                modalidadOBJ.setTipoModalidadId((Integer) row.get("tipo_id"));
                modalidadOBJ.setDescripcion((String) row.get("descripcion"));
                modalidadOBJ.setMontoMin((Double) row.get("monto_min"));
                modalidadOBJ.setMontoMax((Double) row.get("monto_max"));
            } else {
                log.error("No se encontro modalidad de tipo {}", modalidadId);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            modalidadOBJ = null;
        }
        return modalidadOBJ;
    }

    public DetalleInversionOBJ obtenerDetalleInversionByCuenta(String cuentaInversion) {
        List<Map<String, Object>> rows;
        DetalleInversionOBJ detalleInversionOBJ = null;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);

            String query = "inversiones.obtenerDetalleInversionByCuenta";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                detalleInversionOBJ = new DetalleInversionOBJ();
                detalleInversionOBJ.setRendimientoVigenteId((Integer) row.get("rendimiento_vigente_id"));
                detalleInversionOBJ.setTitulo((String) row.get("titulo"));
                detalleInversionOBJ.setCuentaInversion((String) row.get("cuenta_inversion"));
                detalleInversionOBJ.setCuentaPadre((String) row.get("cuenta_padre"));
                detalleInversionOBJ.setTipoModalidadId((Integer) row.get("tipo_modalidad_id"));
                detalleInversionOBJ.setDescModalidad((String) row.get("desc_modalidad"));
                detalleInversionOBJ.setCapital((Double) row.get("capital"));
                detalleInversionOBJ.setPlazo((Integer) row.get("plazo"));
                detalleInversionOBJ.setTasa((Double) row.get("tasa_int"));
                detalleInversionOBJ.setReinversion((Boolean) row.get("reinvertir"));
                detalleInversionOBJ.setFechaInicioD((Date) row.get("fecha_inicio"));
                detalleInversionOBJ.setFechaFinD((Date) row.get("fecha_final"));
            } else {
                log.error("No se encontro el detalle de la cuenta de inversion {}", cuentaInversion);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            detalleInversionOBJ = null;
        }
        return detalleInversionOBJ;
    }

    public List<DetalleInversionOBJ> obtenerInversionesActivasFechaFin() {
        List<Map<String, Object>> rows;
        List<DetalleInversionOBJ> inversionesActivasList = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {

            String query = "inversiones.obtenerInversionesActivas";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}", query);

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) for (Map<String, Object> row : rows) {
                DetalleInversionOBJ detalleInversionOBJ = new DetalleInversionOBJ();
                detalleInversionOBJ.setRendimientoVigenteId((Integer) row.get("rendimiento_vigente_id"));
                detalleInversionOBJ.setTitulo((String) row.get("titulo"));
                detalleInversionOBJ.setCuentaInversion((String) row.get("cuenta_inversion"));
                detalleInversionOBJ.setCuentaPadre((String) row.get("cuenta_padre"));
                detalleInversionOBJ.setTipoModalidadId((Integer) row.get("tipo_modalidad_id"));
                detalleInversionOBJ.setDescModalidad((String) row.get("desc_modalidad"));
                detalleInversionOBJ.setCapital((Double) row.get("capital"));
                detalleInversionOBJ.setPlazo((Integer) row.get("plazo"));
                detalleInversionOBJ.setTasa((Double) row.get("tasa_int"));
                detalleInversionOBJ.setReinversion((Boolean) row.get("reinvertir"));
                detalleInversionOBJ.setFechaInicioD((Date) row.get("fecha_inicio"));
                detalleInversionOBJ.setFechaFinD((Date) row.get("fecha_final"));
                inversionesActivasList.add(detalleInversionOBJ);
            }
            else {
                log.warn("No se encontraron inversiones activas con fecha fin de hoy");
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return Collections.emptyList();
        }
        return inversionesActivasList.isEmpty() ? Collections.emptyList() : inversionesActivasList;
    }

    public DatosReinversionOBJ obtenerDatosReinversionByCuenta(String cuentaInversion) {
        List<Map<String, Object>> rows;
        DatosReinversionOBJ datosReinversionOBJ = null;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);

            String query = "inversiones.obtenerDatosReinversionByCuenta";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                datosReinversionOBJ = new DatosReinversionOBJ();
                datosReinversionOBJ.setTituloReinversion((String) row.get("titulo_reinversion"));
                datosReinversionOBJ.setCuentaInversion((String) row.get("cuenta_inversion"));
                datosReinversionOBJ.setCuentaPadre((String) row.get("cuenta_padre"));
                datosReinversionOBJ.setTipoReinversionId((Integer) row.get("id_tipo_reinvertir"));
                datosReinversionOBJ.setCapitalReinvertir((BigDecimal) row.get("capital_reinvertir"));
            } else {
                log.error("No se encontron los datos de la reinversion {}", cuentaInversion);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            datosReinversionOBJ = null;
        }
        return datosReinversionOBJ;
    }

    public Double obtenerSaldoByCuenta(String cuentaInversion) {
        List<Map<String, Object>> rows;
        Double saldo;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);

            // Query en tu archivo queries.properties
            String query = "ahorro_saldos.obtenerSaldoRealByCuenta";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.queryForList(psql, parameters);

            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.get(0); // Tomamos el primer resultado
                Object saldoObj = row.get("saldo_real");
                if (saldoObj != null) {
                    saldo = ((Number) saldoObj).doubleValue(); // Convertimos float8 → Double
                } else {
                    saldo = 0.0;
                }
            } else {
                log.warn("No se encontró saldo para la cuenta {}", cuentaInversion);
                saldo = 0.0;
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            saldo = null;
        }

        return saldo;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public Long registraBitacoraInversiones(BitacoraInversionesOBJ bitacoraOBJ) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        Long resultado = 0L;
        try {
            parameters.addValue("valor_referencia", bitacoraOBJ.getValorReferencia());
            parameters.addValue("clave_cat_eventos_bit", bitacoraOBJ.getClaveEvento());
            parameters.addValue("tipo_referencia", bitacoraOBJ.getTipoReferencia());
            parameters.addValue("observaciones", bitacoraOBJ.getObservaciones());
            parameters.addValue("codigo", bitacoraOBJ.getCodigo());
            parameters.addValue("idProcesoBit", bitacoraOBJ.getIdProcesoBitacora());

            String query = "bitacora.registraBitacoraInversiones";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            Map<String, Object> row = namedProcreaJdbc.queryForMap(psql, parameters);
            if (row != null) {
                resultado = (Long) row.get("id");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
        }
        return resultado;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void actualizaIdBitacoraInversiones(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("id", id);
            parameters.addValue("idProcesoBit", id);

            String query = "bitacora.actualizaIdBitacoraInversiones";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
        }
    }

    public Boolean existeNoReinvertirByCuenta(String cuentaInversion) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Boolean resultado = false;
        try {

            parameters.addValue("cuentaInversion", cuentaInversion);

            String query = "inversiones.existeNoReinvertirByCuenta";
            String sql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            resultado = namedProcreaJdbc.queryForObject(sql, parameters, Boolean.class);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo obtener la existencia de la cuenta en la tabla ahorro_no_reinvertir: " + e.getMessage()) {
            };
        }

        return resultado;
    }

    public Integer obtenerInversionesVencidasByCuenaPadre(String cuentaInversion) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Integer resultado = 0;
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);

            String query = "inversiones.obtenerInversionesVencidasByCuentaInv";
            String sql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            resultado = namedProcreaJdbc.queryForObject(sql, parameters, Integer.class);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo obtener cantidad de inversiones vencidas: " + e.getMessage()) {
            };
        }
        return resultado;
    }

    public PlazoPorcentajeOBJ obtenerRendimientoIdByPlazo(Integer plazo, Integer tipoModalidadId) {
        List<Map<String, Object>> rows;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        PlazoPorcentajeOBJ obj = null;
        try {
            parameters.addValue("plazo", plazo);

            String query = "plazos.obtenerPorcentajesByPlazo";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}", query);

            rows = namedProcreaJdbc.queryForList(psql, parameters);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst();
                obj = new PlazoPorcentajeOBJ();
                switch (tipoModalidadId) {
                    case 2 -> {
                        obj.setPorcentaje(((BigDecimal) row.get("porcentaje")).doubleValue());
                    }
                    case 4 -> {
                        obj.setPorcentaje(((BigDecimal) row.get("porcentaje_mensual")).doubleValue());
                    }
                }
                obj.setPlazo(plazo);
                obj.setTasaId((Integer) row.get("tasa_id"));
                obj.setRendimientoId((Integer) row.get("rendimiento_id"));
            } else {
                log.error("No se encontro la tasa para el pazo proporcionado");
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return null;
        }
        return obj;
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void insertaAhorroNoReinvertirByCuenta(String cuentaInversion) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("cuenta", cuentaInversion);
            parameters.addValue("observaciones", observacionesNoReinversion);
            parameters.addValue("motivoId", motivoIdNoReinversion);
            parameters.addValue("fechaCreacion", Timestamp.valueOf(LocalDateTime.now()));
            parameters.addValue("usuarioCreacion", 9);

            String query = "inversiones.insertaAhorroNoReinvertirByCuenta";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {

            } else {
                log.error("No se logro guardar el registro en ahorro_no_reinvertir.");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            throw new DataAccessException("No se logro guardar el registro en ahorro_no_reinvertir: " + e.getMessage()) {
            };
        }

    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void actualizarAhorroDatosReinversionByCuenta(String cuentaInversion) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("cuentaInversion", cuentaInversion);
            parameters.addValue("fechaModificacion", Timestamp.valueOf(LocalDateTime.now()));
            parameters.addValue("usuarioModificacion", 9);

            String query = "inversiones.actualizarAhorroDatosReinversionByCuenta";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {

            } else {
                log.error("No se logro actualizar el registro en ahorro_datos_reinversion.");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            throw new DataAccessException("No se logro actualizar el registro en ahorro_datos_reinversion: " + e.getMessage()) {
            };
        }
    }

    public LocalDate obtenerDiaHabil(LocalDate fechaBase, String tipo) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fechaBase", Date.valueOf(fechaBase)); // Convertir a java.sql.Date
        params.addValue("tipo", tipo); // Ejemplo: "D"

        try {
            String queryKey = "funciones.obtenerDiaHabil";
            String sql = queries.getProperty(queryKey);
            log.info("Ejecutando consulta: {} parametros: {}", queryKey, params.getValues());

            Date resultado = namedProcreaJdbc.queryForObject(sql, params, Date.class);
            return resultado.toLocalDate();
        } catch (Exception e) {
            throw new DataAccessException("Error al obtener día hábil: " + e.getMessage()) {
            };
        }
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void insertaAhorroRendimientoVigente(String cuentaInversion, Integer plazo, Double tasa, Integer idRend, Integer vdisposicion,
                                                Double monto, LocalDate fechaCalc, LocalDate fechaFin, Integer idAhorroRend, Integer tasaId) {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        int rows;
        try {
            parameters.addValue("pp_cuenta", cuentaInversion);
            parameters.addValue("vplazo_nuevo", plazo);
            parameters.addValue("vtasa_nuevo", tasa);
            parameters.addValue("pp_usuario", 9);
            parameters.addValue("vrendimiento", idRend);
            parameters.addValue("vdisposicion", vdisposicion);
            parameters.addValue("pp_tipo", 1);
            parameters.addValue("v_mtoreinversion", monto);
            parameters.addValue("pp_feccalc", fechaCalc);
            parameters.addValue("v_fecfinal", fechaFin);
            parameters.addValue("vid_ren", idAhorroRend);
            parameters.addValue("vid_ren", idAhorroRend);
            parameters.addValue("vvtasa_id", tasaId);

            String query = "ahorro_rendimiento_vigente.registrarAhorroRendimientoVigente";
            String psql = queries.getProperty(query);
            log.info("Ejecutando consulta: {}, parametros: {}", query, parameters.getValues());

            rows = namedProcreaJdbc.update(psql, parameters);
            if (rows > 0) {

            } else {
                log.error("No se logro guardar el registro en ahorro_rendimiento vigente.");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            errorHandler.handleException(e);
            throw new DataAccessException("No se logro guardar el registro en ahorro_no_reinvertir: " + e.getMessage()) {
            };
        }
    }

    @Transactional(transactionManager = "transactionManagerProcrea")
    public void insertarEdoCuenta(String cuenta, LocalDate fechaCalc, LocalDate fechaPlazo, BigDecimal monto) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        long dias = ChronoUnit.DAYS.between(fechaPlazo, fechaCalc);
        int mes = fechaCalc.getMonthValue();
        int anio = fechaCalc.getYear();

        params.addValue("cuenta", cuenta);
        params.addValue("saldoReal", monto);
        params.addValue("saldoPromedio", monto);
        params.addValue("saldoAcumulado", monto);
        params.addValue("dias", dias);
        params.addValue("mes", mes);
        params.addValue("anio", anio);
        params.addValue("saldoDisponible", monto);
        params.addValue("fechaCorte", java.sql.Date.valueOf(fechaCalc));

        try {
            String queryKey = "ahorro_edocuenta.insertarEdoCuenta";
            String sql = queries.getProperty(queryKey);
            log.info("Insertando estado de cuenta: {} con parámetros: {}", queryKey, params.getValues());

            namedProcreaJdbc.update(sql, params);
        } catch (Exception e) {
            throw new DataAccessException("Error al insertar en ahorro_edocuenta: " + e.getMessage()) {
            };
        }
    }
}
