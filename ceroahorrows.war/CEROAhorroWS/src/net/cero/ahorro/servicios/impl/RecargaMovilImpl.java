package net.cero.ahorro.servicios.impl;

import com.google.gson.Gson;
import net.cero.ahorro.common.DateUtil;
import net.cero.ahorro.common.DbBeans;
import net.cero.ahorro.common.JDBCUtil;
import net.cero.ahorro.common.SqlQueryParams;
import net.cero.ahorro.servicios.RecargaMovil;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.RecargaMovilDTO;
import net.cero.data.RecargaMovilDetalle;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.config.IPAuthenticationProvider;
import net.cero.spring.dao.excepcion.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecargaMovilImpl implements RecargaMovil {
    private DataSource driverSourceCero;
    private DataSource driverSourceProcrea;
    private Apps apps;
    @Autowired
    protected IPAuthenticationProvider authenticationManager;

    protected String CONSULTA_MOVIMIENTOS_RECARGA_AHORRO = "SELECT  a.cuenta, \n" +
                                                                    "atc.monto , \n" +
                                                                    "atc.descripcion ,\n" +
                                                                    "'RECARGA TELEFONICA' descripcion_movimiento,\n" +
                                                                    "atc.num_autorizacion numero_autorizacion,\n" +
                                                                    "atc.fecha fecha_operacion,\n" +
                                                                    "atc.fecha_creacion fecha_aplicacion\n" +
                                                            "FROM ahorro.ahtransacciones_cuentas atc\n" +
                                                            "JOIN ahorro.ahcuentas a ON atc.cuenta_id = a.id \n" +
                                                            "JOIN nucleocentral.ncposicionglobalah ngc  ON a.cuenta  = ngc.cuentaah \n" +
                                                            "WHERE \n ";

    protected String CONSULTA_MOVIMIENTOS_RECARGA_PROCREA ="SELECT mc.cuenta, \n" +
                                                                    "mc.monto, \n" +
                                                                    "mc.obs as descripcion,\n" +
                                                                    "'SIN REFERENCIA' as referencia,\n" +
                                                                    "'RECARGA TELEFÓNICA' as descripcion_movimiento,\n" +
                                                                    "'SIN AUTORIZACION' as numero_autorizacion,\n" +
                                                                    "mc.fecha_deposito as fecha_operacion,\n" +
                                                                    "mc.fecha_creacion as fecha_aplicacion \n" +
                                                            "FROM movimientos_caja mc " +
                                                            "WHERE \n ";

    protected String queryCompaniasTelefonicas = "SELECT ce.servicio as compania\n" +
                                                    "FROM comisionistas.coservicios_empresas ce \n" +
                                                    "WHERE ce.tipo_servicio = 'Recargas' and ce.activo = 'SI'\n" +
                                                    "GROUP BY ce.servicio";
    private static final Logger log = LogManager.getLogger(RecargaMovilImpl.class);
    private Gson gson = new Gson();


    public RecargaMovilImpl() {
        initialized();
    }

    private void initialized() {
        driverSourceCero = DbBeans.DbBeansImpl.getDataSource("ds");
        driverSourceProcrea = DbBeans.DbBeansImpl.getDataSource("dsPr");
    }
    @Override
    public Respuesta getRecargaMovil(RecargaMovilDTO recargaMovilDTO, BindingResult bindingResult) {
        Respuesta respuesta = new Respuesta();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;
        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

        if (!authenticate.isAuthenticated()) {
            respuesta.setCodigo(4);
            respuesta.setData("");
            respuesta.setMensaje("No autorizado");
            return respuesta;
        }

        if(bindingResult.hasErrors()) {
            respuesta.setCodigo(1);
            respuesta.setMensaje(bindingResult.getFieldError().getDefaultMessage());

            return respuesta;
        }

        if(Objects.nonNull(recargaMovilDTO.getNumeroAutorizacion()) && Objects.nonNull(recargaMovilDTO.getNumeroReferencia())){
            respuesta.setCodigo(4);
            respuesta.setMensaje("Se ha proprocionado más de un filtro numeroReferencia - numeroAutorizacion");

            return respuesta;
        }

        try {
            List<RecargaMovilDetalle.RecargaDetalle> recargasAhorro = consultaMovimientosRecargaMovil(recargaMovilDTO, WS_UTIL.PARAM_DESCRIPCION, CONSULTA_MOVIMIENTOS_RECARGA_AHORRO);
            List<RecargaMovilDetalle.RecargaDetalle> recargasProcrea = consultaMovimientosRecargaMovil(recargaMovilDTO, WS_UTIL.PARAM_OBS, CONSULTA_MOVIMIENTOS_RECARGA_PROCREA);

            respuesta.setCodigo(0);
            respuesta.setMensaje("Detalle de recarga movil");
            respuesta.setData(new Gson().toJson(Stream.concat(recargasAhorro.stream(), recargasProcrea.stream()).collect(Collectors.toList())));
        }catch (DaoException | SQLException daoException){
            log.error("Error al obtener la recarga movil", daoException);

            respuesta = new Respuesta(4,"Error");
        }

        return respuesta;
    }

    @Override
    public Respuesta consultaCatalogoCompanias() {
        List<Map<String, Object>> companiasList = new ArrayList<>();
        Respuesta respuesta = new Respuesta();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;

        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

        if (!authenticate.isAuthenticated()) {
            respuesta.setCodigo(4);
            respuesta.setData("");
            respuesta.setMensaje("No autorizado");
        }

        try{
            PreparedStatement ps = JDBCUtil.getPreparedStatementFromDs(queryCompaniasTelefonicas, driverSourceCero);
            ResultSet rs = JDBCUtil.executePreparedStatement(ps);
            while (rs.next()) {
                Map<String, Object> companiaMap = new HashMap<>();

                companiaMap.put("compania",rs.getString("compania"));
                companiasList.add(companiaMap);
            }

            respuesta.setCodigo(0);
            respuesta.setMensaje("Éxito");
        }catch (SQLException e){
            respuesta.setCodigo(0);
            respuesta.setMensaje("Algo ocurrió al consultar las recargas telefónicas");

            log.error("Recarga telefono {}", e);
        }


        respuesta.setData(gson.toJson(companiasList));
        return respuesta;
    }

    private List<RecargaMovilDetalle.RecargaDetalle> consultaMovimientosRecargaMovil(RecargaMovilDTO recargaMovilDTO, String paramFilter, String queryTemplate) throws DaoException, SQLException {
        List<RecargaMovilDetalle.RecargaDetalle> dataModel = new ArrayList<>();
        PreparedStatement ps;

        if(paramFilter.equals(WS_UTIL.PARAM_DESCRIPCION)){
            ps = driverSourceCero.getConnection().prepareStatement(buildQueryByParams(paramFilter, queryTemplate));
        }else{
            ps = driverSourceProcrea.getConnection().prepareStatement(buildQueryByParams(paramFilter, queryTemplate));
        }

        ResultSet rs = JDBCUtil.executePreparedStatementWithParams(ps, buildQueryParams(recargaMovilDTO));

        buildDataModelList(recargaMovilDTO, rs, dataModel);

        return dataModel;
    }

    private void buildDataModelList(RecargaMovilDTO recargaMovilDTO, ResultSet rs, List<RecargaMovilDetalle.RecargaDetalle> dataModel) throws SQLException {
        while (rs.next()){
            String descripcion = String.valueOf(rs.getString("descripcion"));

            String [] descripcionSplit = descripcion.split("\\|");

            if(descripcionSplit[WS_UTIL.INDEX_NUMERO_TELEFONO_RECARGA].equals(recargaMovilDTO.getNumeroCelular()) && descripcionSplit[WS_UTIL.INDEX_COMPANIA_RECARGA].equals(recargaMovilDTO.getCompaniaTel())){
                if(Objects.isNull(recargaMovilDTO.getNumeroReferencia()) && Objects.isNull(recargaMovilDTO.getNumeroAutorizacion())){
                    dataModel.add(construyeRecargaMovilDetalle(recargaMovilDTO, descripcionSplit, rs, descripcionSplit.length));
                }else if(validaFiltroBusqueda(recargaMovilDTO, descripcionSplit, rs, descripcionSplit.length)){
                    dataModel.add(construyeRecargaMovilDetalle(recargaMovilDTO, descripcionSplit, rs, descripcionSplit.length));
                }
            }
        }
    }

    private boolean validaFiltroBusqueda(RecargaMovilDTO recargaMovilDTO, String[] descripcionSplit, ResultSet rs, int sizeDescripcion) {
        String filtro;

        String valorBuscado;
        if(sizeDescripcion == WS_UTIL.RECARGA_NO_HISTORICO){
            filtro = Objects.isNull(recargaMovilDTO.getNumeroAutorizacion()) ?
                    recargaMovilDTO.getNumeroReferencia() : recargaMovilDTO.getNumeroAutorizacion();

            valorBuscado = Objects.isNull(recargaMovilDTO.getNumeroAutorizacion()) ?
                    descripcionSplit[WS_UTIL.INDEX_NUMERO_TELEFONO_RECARGA] : descripcionSplit[WS_UTIL.INDEX_AUTORIZACION_RECARGA];
        }else if(sizeDescripcion == WS_UTIL.RECARGA_HISTORICO){
            filtro = Objects.nonNull(recargaMovilDTO.getNumeroReferencia()) ? recargaMovilDTO.getNumeroReferencia() : "+";

            valorBuscado = Objects.nonNull(recargaMovilDTO.getNumeroReferencia()) ? recargaMovilDTO.getNumeroReferencia() : "-";
        }else {
            filtro = "";
            valorBuscado = "";
        }

        return filtro.equals(valorBuscado);
    }

    private RecargaMovilDetalle.RecargaDetalle construyeRecargaMovilDetalle(RecargaMovilDTO recargaMovilDTO, String[] descripcionSplit, ResultSet rs, int sizeDescription) throws SQLException {
        RecargaMovilDetalle.RecargaDetalle recargaDetalle = new RecargaMovilDetalle.RecargaDetalle();
        recargaDetalle.setCuentaASP(String.valueOf(rs.getString("cuenta")));
        recargaDetalle.setNumeroCelular(descripcionSplit[WS_UTIL.INDEX_NUMERO_TELEFONO_RECARGA]);
        recargaDetalle.setCompaniaTel(descripcionSplit[WS_UTIL.INDEX_COMPANIA_RECARGA]);
        recargaDetalle.setTipoRecarga(descripcionSplit[WS_UTIL.INDEX_TIPO_RECARGA]);
        recargaDetalle.setMonto(String.valueOf(rs.getString("monto")));
        recargaDetalle.setFechaOperacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_operacion")));
        recargaDetalle.setFechaAplicacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_aplicacion")));
        recargaDetalle.setDescripcionMovimiento(rs.getString("descripcion_movimiento"));
        recargaDetalle.setNumeroReferencia(descripcionSplit[WS_UTIL.INDEX_NUMERO_TELEFONO_RECARGA]);


        if(sizeDescription == WS_UTIL.RECARGA_NO_HISTORICO){
            recargaDetalle.setEstatus(descripcionSplit[WS_UTIL.INDEX_ESTATUS_RECARGA]);
            recargaDetalle.setNumeroAutorizacion(descripcionSplit[WS_UTIL.INDEX_AUTORIZACION_RECARGA]);
            recargaDetalle.setDescripcionStatus(descripcionSplit[WS_UTIL.INDEX_MENSAJE_RECARGA]);
        } else if (sizeDescription == WS_UTIL.RECARGA_HISTORICO) {
            recargaDetalle.setEstatus(descripcionSplit[WS_UTIL.INDEX_ESTATUS_RECARGA_HISTORICO]);
            recargaDetalle.setNumeroAutorizacion("");
            recargaDetalle.setDescripcionStatus("");
        }

        return recargaDetalle;
    }

    private List<SqlQueryParams> buildQueryParams(RecargaMovilDTO recargaMovilDTO) {
        List<SqlQueryParams> params = new ArrayList<>();
        params.add(new SqlQueryParams(Types.VARCHAR, recargaMovilDTO.getCompaniaTel()));
        params.add(new SqlQueryParams(Types.VARCHAR, recargaMovilDTO.getNumeroCelular()));

        Timestamp dateWithInitialDay = DateUtil.dateToSqlTimestamp(recargaMovilDTO.getFechaOperacion(), false);
        Timestamp dateWithEndingDay = DateUtil.dateToSqlTimestamp(recargaMovilDTO.getFechaOperacion(), true);

        params.add(new SqlQueryParams(Types.TIMESTAMP, dateWithInitialDay));
        params.add(new SqlQueryParams(Types.TIMESTAMP, dateWithEndingDay));

        return params;
    }

    private String buildQueryByParams(String paramToFilter, String queryTemplate) {
        String sql = queryTemplate
                .concat("(position(? in ".concat(paramToFilter)).concat(")> 0)")
                .concat(" AND ")
                .concat("(position(? in  ".concat(paramToFilter)).concat(")> 0)")
                .concat(" AND ")
                .concat(getFechaFilter(paramToFilter));

        return sql;
    }

    private String getFechaFilter(String paramToFilter) {
        String fechaFilter;
        if(paramToFilter.equals(WS_UTIL.PARAM_DESCRIPCION)){
            fechaFilter = "atc.fecha BETWEEN ? and ? ";
        }else{
            fechaFilter = "mc.fecha BETWEEN ? and ?";
        }

        return  fechaFilter;
    }
}
