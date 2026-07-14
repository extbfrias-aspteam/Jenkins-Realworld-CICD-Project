package net.cero.ahorro.servicios.impl;

import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.common.ConnectionProvider;
import net.cero.ahorro.common.DateUtil;
import net.cero.ahorro.common.JDBCUtil;
import net.cero.ahorro.common.QueriesUtils;
import net.cero.ahorro.common.SqlQueryParams;
import net.cero.ahorro.servicios.MovimientosCuenta;
import net.cero.ahorro.ws.util.SingletonInstances;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.DetalleCuentaASP;
import net.cero.data.MovimientoCuentaASP;
import net.cero.data.MovimientoCuentaRequest;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.dao.excepcion.DaoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.cero.ahorro.ws.util.WS_UTIL.gson;

@Log4j2
@Service
public class MovimientosCuentaImpl extends ConnectionProvider implements MovimientosCuenta{

    protected IPAuthenticationProvider authenticationManager;
    @Autowired
    public MovimientosCuentaImpl(IPAuthenticationProvider authenticationManager) {
        super.init();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Respuesta consultaMovimientosCuenta(@Valid MovimientoCuentaRequest movimientoCuentaRequest, BindingResult bindingResult) {
        Respuesta respuesta = new Respuesta();

        try {
            validationAndAuthentication(movimientoCuentaRequest, bindingResult);

            DetalleCuentaASP detalleCuentaASP = buildFilterBy(movimientoCuentaRequest);
            respuesta.setCodigo(0);
            respuesta.setMensaje("Solicitud exitosa");

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("header", movimientoCuentaRequest.getHeader());
            bodyMap.put("cuentaASP", detalleCuentaASP.getCuenta());
            bodyMap.put("fechaInicial", movimientoCuentaRequest.getFechaInicio());
            bodyMap.put("fechaFinal", movimientoCuentaRequest.getFechaFin());


            String jsonResponse = WS_UTIL.doPostRequest(gson.toJson(bodyMap), ConstantesUtil.CONCENTRADO_MOVIMIENTOS_WS);
            respuesta = gson.fromJson(jsonResponse, Respuesta.class);
        }catch (DaoException | SQLException daoException){
            log.error("Error al consultar {}", daoException);
            respuesta.setCodigo(4);
            respuesta.setMensaje(daoException.getMessage());
            respuesta.setData("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return respuesta;
    }

    private DetalleCuentaASP buildFilterBy(MovimientoCuentaRequest movimientoCuentaRequest) throws SQLException, DaoException {
        return Objects.nonNull(movimientoCuentaRequest.getCuentaASP()) ?
                                filterBy(movimientoCuentaRequest.getCuentaASP(), QueriesUtils.CUENTA_NCPOSICIONGLOBLA, getDriverSourceCero()) :
                                filterByNumeroTelefono(movimientoCuentaRequest.getNumeroTelefono());
    }

    private DetalleCuentaASP filterByNumeroTelefono(String numeroTelefono) throws SQLException, DaoException {
        DetalleCuentaASP detalleCuentaASP = new DetalleCuentaASP();

        List<SqlQueryParams> params = Arrays.asList(new SqlQueryParams(Types.VARCHAR, numeroTelefono));
        PreparedStatement ps = JDBCUtil.getPreparedStatementFromDs(QueriesUtils.QUERY_CONSULTA_SOLICITANTE_BY_NUMERO, getDriverSourceProcrea());
        ResultSet rs = JDBCUtil.executePreparedStatementWithParams(ps, params);

        if (rs.next()) {
            String personaId = rs.getString("id_solicitante");
            return filterBy(personaId, QueriesUtils.ID_PERSONA_NCPOSICIONGLOBLA, getDriverSourceCero());
        }

        return detalleCuentaASP;
    }

    private DetalleCuentaASP filterBy(String param, String filterBy, DataSource currentConnection) throws SQLException, DaoException {
        DetalleCuentaASP detalleCuentaASP = null;
        List<SqlQueryParams> params = Arrays.asList(new SqlQueryParams(Types.VARCHAR, param));
        PreparedStatement ps = JDBCUtil.getPreparedStatementFromDs(String.format(QueriesUtils.QUERY_CONSULTA_CUENTA_ASP, filterBy), currentConnection);
        ResultSet rs = JDBCUtil.executePreparedStatementWithParams(ps, params);

        int rowCount = 0;

        while (rs.next()) {
            if (rowCount > 1) {
                throw new DaoException("Más de una cuenta está asociada al número de teléfono");
            }
            detalleCuentaASP =
                    DetalleCuentaASP
                            .builder()
                            .cuenta(rs.getString("cuentaah"))
                            .tipoCuenta(rs.getString("tipo_cuenta"))
                            .estatus(rs.getString("estatusah"))
                            .build();
            rowCount++;
        }

        if(rowCount == 0) {
            throw new DaoException("No se encuentró cuenta asociada al número de teléfono");
        }
        return detalleCuentaASP;
    }

    /**
     * Consulta por default los movimientos en la base de datos de procrea.
     * @param cuentaASP cuenta asp.
     * @param fechaInicioStr fecha de inicio en formato strin.
     * @param fechaFinStr fecha fin en formato string.
     * @return
     * @throws SQLException
     */
    private List<MovimientoCuentaASP> consultaMovimientoCuenta(String cuentaASP, String fechaInicioStr, String fechaFinStr) throws SQLException {
        return consultaMovimientoCuenta(cuentaASP, fechaInicioStr, fechaFinStr, Boolean.TRUE);
    }

    /**
     * Consulta los movimientos en la base de datos de cero.
     * @param cuentaASP cuenta asp.
     * @param fechaInicioStr fecha inicio en formato string
     * @param fechaFinStr fecha fin en formato string
     * @param onProcrea booleano que define si consultará en procrea
     * @return
     * @throws SQLException
     */
    private List<MovimientoCuentaASP> consultaMovimientoCuenta(String cuentaASP, String fechaInicioStr,
                                                                      String fechaFinStr, boolean onProcrea) throws SQLException {
        List<MovimientoCuentaASP> movimientoCuentaASPList = new ArrayList<>();
        List<SqlQueryParams> params = Arrays.asList(new SqlQueryParams(Types.VARCHAR, cuentaASP),
                                                    new SqlQueryParams(Types.DATE, DateUtil.dateToSqlTimestamp(fechaInicioStr, Boolean.FALSE)),
                                                    new SqlQueryParams(Types.DATE, DateUtil.dateToSqlTimestamp(fechaFinStr, Boolean.TRUE)));

        PreparedStatement psConsultaMovmientos;
        if (onProcrea){
            psConsultaMovmientos = JDBCUtil.getPreparedStatementFromDs(QueriesUtils.QUERY_CONSULTA_MOVIMIENTOS_CUENTAS, getDriverSourceProcrea());
        }else{
            psConsultaMovmientos = JDBCUtil.getPreparedStatementFromDs(QueriesUtils.QUERY_CONSULTA_MOVIMIENTOS_CUENTAS_CERO, getDriverSourceCero());
        }


        ResultSet rsConsultaMovimientos = JDBCUtil.executePreparedStatementWithParams(psConsultaMovmientos, params);
        while (rsConsultaMovimientos.next()){
            MovimientoCuentaASP movimientoCuentaASP = new MovimientoCuentaASP();

            String descripcion = rsConsultaMovimientos.getString("descripcion");
            String titulo = rsConsultaMovimientos.getString("titulo");

            movimientoCuentaASP.setMonto(rsConsultaMovimientos.getDouble("monto"));
            movimientoCuentaASP.setFechaMovimiento(DateUtil.getStringDateFromSqlDate(DateUtil.getDateFromStringFormat(rsConsultaMovimientos.getString("fecha_movimiento"))));
            movimientoCuentaASP.setFechaAplicacion(DateUtil.getStringDateFromSqlDate(DateUtil.getDateFromStringFormat(rsConsultaMovimientos.getString("fecha_aplicacion"))));

            movimientoCuentaASP.setTitulo(titulo);
            movimientoCuentaASP.setOperacion(rsConsultaMovimientos.getString("operacion"));

            if(StringUtils.isNotBlank(titulo)){
                Pattern pattern = Pattern.compile("CLAVE RASTREO\\s*(\\d{18})");
                Pattern pattern2 = Pattern.compile("CLAVE DE RASTREO:.(\\S+)");
                Pattern pattern3 = Pattern.compile("CLAVE RASTREO.(\\S+)");

                Matcher matcher = pattern.matcher(descripcion);
                Matcher matcher2 = pattern2.matcher(descripcion);
                Matcher matcher3 = pattern3.matcher(descripcion);
                if(matcher.find() || matcher2.find() || matcher3.find()){
                    MovimientoCuentaASP.DetalleMovimiento detalleMovimiento = new MovimientoCuentaASP.DetalleMovimiento();

                    String claveRastreo = getMatcherGroup(matcher, matcher2, matcher3);
                    String queryDetalle = isIncommingOrOutgoing(rsConsultaMovimientos.getString("operacion"));

                    consultaDetalleMovimiento(movimientoCuentaASP, detalleMovimiento, claveRastreo, queryDetalle);
                }
            }
            movimientoCuentaASPList.add(movimientoCuentaASP);
        }

        return movimientoCuentaASPList;
    }

    private String isIncommingOrOutgoing(String operacion) {
        return operacion.equals("+") ?
                QueriesUtils.QUERY_CONSULTA_DETALLE_MOVIMIENTO_CUENTA_INCOMMING : operacion.equals("-") ?
                QueriesUtils.QUERY_CONSULTA_DETALLE_MOVIMIENTO_CUENTA_OUTGOING : "";
    }

    private String getMatcherGroup(Matcher ... matchers) {
        for (Matcher match: matchers) {
            if(match.groupCount() > 0 ){
                try {
                    return match.group(1);
                }catch (IllegalStateException ie) {
                    log.error("No match found {}", ie);
                }
            }
        }

        return "";
    }

    private void consultaDetalleMovimiento(MovimientoCuentaASP movimientoCuentaASP, MovimientoCuentaASP.DetalleMovimiento detalleMovimiento, String claveRastreo, String query) throws SQLException {
        PreparedStatement psConsultaDetalleMov = JDBCUtil.getPreparedStatementFromDs(query, getDriverSourceIzel());
        ResultSet rsConsultaDetalleMov = JDBCUtil.executePreparedStatementWithParams(psConsultaDetalleMov, Arrays.asList(new SqlQueryParams(Types.VARCHAR, claveRastreo)));

        if (rsConsultaDetalleMov.next()){
            detalleMovimiento.setEmisor(rsConsultaDetalleMov.getString("nombre_ordenante"));
            detalleMovimiento.setCuentaOrigen(rsConsultaDetalleMov.getString("cuenta_ordenante"));
            detalleMovimiento.setBeneficiario(rsConsultaDetalleMov.getString("nombre_beneficiario"));
            detalleMovimiento.setCuentaDestino(rsConsultaDetalleMov.getString("cuenta_beneficiario"));
            detalleMovimiento.setCEP(rsConsultaDetalleMov.getString("cep"));
            detalleMovimiento.setConcepto(rsConsultaDetalleMov.getString("concepto_pago"));
            detalleMovimiento.setReferencia(rsConsultaDetalleMov.getString("referencia_cobranza"));
            detalleMovimiento.setClaveRastreo(rsConsultaDetalleMov.getString("cve_rastreo"));

            movimientoCuentaASP.setDetalleMovimiento(detalleMovimiento);
        }
    }

    @Override
    public void validationAndAuthentication(Object body, BindingResult bindingResult) throws DaoException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;
        MovimientoCuentaRequest movimientoCuentaRequest = (MovimientoCuentaRequest) body;

        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

        if (!authenticate.isAuthenticated()) {
            throw new DaoException("No autorizado");
        }
        if (Objects.nonNull(movimientoCuentaRequest.getCuentaASP()) && Objects.nonNull(movimientoCuentaRequest.getNumeroTelefono())) {
            throw new DaoException("Se ha proporcionado más de un filtro");
        }
        if(bindingResult.hasErrors()) {
            throw new DaoException(bindingResult.getFieldError().getDefaultMessage());
        }
    }
}
