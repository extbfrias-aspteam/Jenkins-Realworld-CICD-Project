package net.cero.ahorro.servicios.impl;

import com.google.gson.Gson;
import net.cero.ahorro.common.DateUtil;
import net.cero.ahorro.common.DbBeans;
import net.cero.ahorro.common.JDBCUtil;
import net.cero.ahorro.common.SqlQueryParams;
import net.cero.ahorro.servicios.PagoServicios;
import net.cero.ahorro.ws.util.WS_UTIL;
import net.cero.data.PagoServicioDTO;
import net.cero.data.PagoServiciosConsultaDTO;
import net.cero.data.ProveedorDTO;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PagoServiciosImpl implements PagoServicios {


    @Autowired
    protected IPAuthenticationProvider authenticationManager;
    private DataSource driverSourceCero;
    private DataSource driverSourceProcrea;

    private String QUERY_PROVEEDORES = "SELECT servicio AS proveedor_servicio\n" +
                                        "FROM comisionistas.coservicios_empresas ce\n" +
                                        "WHERE ce.activo = 'SI' and tipo_servicio like '%Terceros%'";

    private String QUERY_CONSULTA_PAGO_SERVICIO = "SELECT ac.monto ,\n" +
                                                        "a.cuenta , \n" +
                                                        "ac.descripcion , \n" +
                                                        "ac.tipo_transaccion_id , \n" +
                                                        "ac.fecha fecha_aplicacion, \n" +
                                                        "ac.fecha_creacion fecha_operacion, \n" +
                                                        "a.persona_id id_persona\n" +
                                                    "FROM ahorro.ahtransacciones_cuentas ac\n" +
                                                    "INNER JOIN ahorro.ahcuentas a on a.id = ac.cuenta_id \n" +
                                                    "WHERE tipo_transaccion_id = 110 \n" +
                                                    "AND descripcion != ''\n" +
                                                    "AND position(? in descripcion) > 0\n" +
                                                    "AND fecha between ? and  ?";

    private String QUERY_CONSULTA_PAGO_SERVICIOS_PROCREA = "SELECT  mc.cuenta, \n" +
                                                                "mc.monto, \n" +
                                                                "mc.obs as descripcion,\n" +
                                                                "'PAGO DE SERVICIOS' as descripcion_movimiento,\n" +
                                                                "mc.fecha as fecha_aplicacion,\n" +
                                                                "mc.fecha_creacion as fecha_operacion,\n" +
                                                                "s.nombre as emisor\n" +
                                                            "FROM movimientos_caja mc\n" +
                                                            "INNER JOIN ahorro_contrato ac on mc.cuenta = ac.cuenta \n" +
                                                            "INNER JOIN solicitante s on s.numero = ac.solicitante_id\n" +
                                                            "WHERE position(? in mc.obs ) > 0\n" +
                                                            "AND mc.fecha_creacion between ? and ?";
    private String QUERY_CONSULTA_EMISOR = "SELECT s.nombre as emisor FROM solicitante s WHERE s.numero = ?";
    private Gson gson;
    private static final Logger log = LogManager.getLogger(PagoServiciosImpl.class);
    ;

    @PostConstruct
    void init(){
        gson = new Gson();
        initialized();
    }

    public PagoServiciosImpl(IPAuthenticationProvider authenticationManager) {
        this.authenticationManager = authenticationManager;
        gson = new Gson();
        initialized();
    }

    private void initialized() {
        driverSourceCero = DbBeans.DbBeansImpl.getDataSource("ds");
        driverSourceProcrea = DbBeans.DbBeansImpl.getDataSource("dsPr");
    }

    @Override
    public Respuesta getProveedores() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authenticate;
        Respuesta respuesta = new Respuesta();
        authenticate = authenticationManager.authenticate(securityContext.getAuthentication());

        if (!authenticate.isAuthenticated()) {
            respuesta.setCodigo(4);
            respuesta.setData("");
            respuesta.setMensaje("No autorizado");

            return respuesta;
        }

        try {
            PreparedStatement ps = driverSourceCero.getConnection().prepareStatement(QUERY_PROVEEDORES);
            ResultSet rs = JDBCUtil.executePreparedStatement(ps);

            Set<ProveedorDTO> proveedores = getProveedorFromResultSet(rs);

            respuesta.setCodigo(0);
            respuesta.setMensaje("Solicitud exitosa");
            respuesta.setData(gson.toJson(proveedores));
        }catch (SQLException e){
            log.error("Error al obtener los proveedores {}", e);
            respuesta.setCodigo(4);
            respuesta.setMensaje("Hubo un error al consultar los proveedores");
            respuesta.setData("");
        }

        return respuesta;
    }

    @Override
    public Respuesta consultaPagoServicios(PagoServiciosConsultaDTO pagoServiciosConsultaDTO, BindingResult bindingResult) {
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
            respuesta.setCodigo(4);
            respuesta.setMensaje(bindingResult.getFieldError().getDefaultMessage());

            return respuesta;
        }

        if(Objects.nonNull(pagoServiciosConsultaDTO.getNumeroReferencia()) && Objects.nonNull(pagoServiciosConsultaDTO.getNumeroAutorizacion())){
            respuesta.setCodigo(4);
            respuesta.setMensaje("Se ha proprocionado más de un filtro numeroReferencia - numeroAutorizacion");

            return respuesta;
        }
        try{
            ResultSet rsCero = consultaPagoServiciosFromDatabase(pagoServiciosConsultaDTO);
            ResultSet rsProcrea = consultaPagoServiciosFromDatabase(pagoServiciosConsultaDTO, Boolean.FALSE);

            List<PagoServicioDTO> pagoServicioDTOListCero = construyeListaPagoServicios(rsCero, pagoServiciosConsultaDTO);
            List<PagoServicioDTO> pagoServicioDTOListProcrea = construyeListaPagoServicios(rsProcrea, pagoServiciosConsultaDTO, Boolean.FALSE);

            respuesta.setCodigo(0);

            if(pagoServicioDTOListCero.isEmpty() && pagoServicioDTOListProcrea.isEmpty()){
                respuesta.setMensaje("No existe un pago de servicio en la fecha informada");
                respuesta.setData("");
            }else{
                respuesta.setMensaje("Solicitud exitosa");
                respuesta.setData(gson.toJson(Stream.
                                    concat(pagoServicioDTOListCero.stream(), pagoServicioDTOListProcrea.stream())
                                    .collect(Collectors.toList())));
            }

        }catch (SQLException e){
            log.error("Error al obtener los pagos de servicio {}", e);
            respuesta.setCodigo(4);
            respuesta.setMensaje("Hubo un error al consultar los pagos de servicios");
            respuesta.setData("");
        }

        return respuesta;
    }

    private ResultSet consultaPagoServiciosFromDatabase(PagoServiciosConsultaDTO pagoServiciosConsultaDTO) throws SQLException {
        return consultaPagoServiciosFromDatabase(pagoServiciosConsultaDTO, Boolean.TRUE);
    }
    private ResultSet consultaPagoServiciosFromDatabase(PagoServiciosConsultaDTO pagoServiciosConsultaDTO, boolean ceroDatabase) throws SQLException {
        PreparedStatement ps;
        if(ceroDatabase) {
            ps = JDBCUtil.getPreparedStatementFromDs(QUERY_CONSULTA_PAGO_SERVICIO, driverSourceCero);
        }else {
            ps = JDBCUtil.getPreparedStatementFromDs(QUERY_CONSULTA_PAGO_SERVICIOS_PROCREA, driverSourceProcrea);
        }

        List<SqlQueryParams> params = new ArrayList<>();
        params.add(new SqlQueryParams(Types.VARCHAR, pagoServiciosConsultaDTO.getProveedor()));

        Timestamp timestampStart = DateUtil.dateToSqlTimestamp(pagoServiciosConsultaDTO.getFechaOperacion(), false);
        Timestamp timestampEnding = DateUtil.dateToSqlTimestamp(pagoServiciosConsultaDTO.getFechaOperacion(), true);

        params.add(new SqlQueryParams(Types.TIMESTAMP, timestampStart));
        params.add(new SqlQueryParams(Types.TIMESTAMP, timestampEnding));

        return JDBCUtil.executePreparedStatementWithParams(ps, params);
    }

    private List<PagoServicioDTO> construyeListaPagoServicios(ResultSet rs, PagoServiciosConsultaDTO pagoServiciosConsultaDTO) throws SQLException {
        return construyeListaPagoServicios(rs, pagoServiciosConsultaDTO, Boolean.TRUE);
    }
    private List<PagoServicioDTO> construyeListaPagoServicios(ResultSet rs, PagoServiciosConsultaDTO pagoServiciosConsultaDTO, boolean ceroDatabase) throws SQLException {
        List<PagoServicioDTO> pagoServicioDTOList = new ArrayList<>();

        if(ceroDatabase){
            while (rs.next()) {
                PagoServicioDTO pagoServicioDTO = new PagoServicioDTO();
                String[] descripcion = rs.getString("descripcion").split("\\|");


                if (validaFiltroBusqueda(descripcion, pagoServiciosConsultaDTO)) {
                    construyePagoServicioDTOObject(descripcion, pagoServicioDTO, rs, descripcion.length);

                    String idPersona = rs.getString("id_persona");

                    PreparedStatement ps = JDBCUtil.getPreparedStatementFromDs(QUERY_CONSULTA_EMISOR, driverSourceProcrea);
                    ResultSet rsEmisor = JDBCUtil.executePreparedStatementWithParams(ps, Arrays.asList(new SqlQueryParams(Types.VARCHAR, idPersona)));

                    while (rsEmisor.next()) {
                        pagoServicioDTO.setEmisor(rsEmisor.getString("emisor"));
                    }

                    pagoServicioDTOList.add(pagoServicioDTO);
                }
            }
        }else{
            while (rs.next()) {
                PagoServicioDTO pagoServicioDTO = new PagoServicioDTO();
                String [] descripcion = rs.getString("descripcion").split("\\|");

                if(validaFiltroBusqueda(descripcion, pagoServiciosConsultaDTO)) {
                    pagoServicioDTO.setEmisor(rs.getString("emisor"));
                    construyePagoServicioDTOObject(descripcion, pagoServicioDTO, rs, descripcion.length);

                    pagoServicioDTOList.add(pagoServicioDTO);
                }
            }
        }


        return pagoServicioDTOList;
    }

    private boolean validaFiltroBusqueda(String[] descripcion, PagoServiciosConsultaDTO pagoServiciosConsultaDTO) {
        if(Objects.isNull(pagoServiciosConsultaDTO.getNumeroAutorizacion()) && Objects.isNull(pagoServiciosConsultaDTO.getNumeroReferencia())){
            return true;
        }

        String filtro = Objects.isNull(pagoServiciosConsultaDTO.getNumeroReferencia()) ?
                                                pagoServiciosConsultaDTO.getNumeroAutorizacion() : pagoServiciosConsultaDTO.getNumeroReferencia();

        String valorBuscado = Objects.isNull(pagoServiciosConsultaDTO.getNumeroReferencia()) ?
                                                descripcion[WS_UTIL.INDEX_AUTORIZACION] : descripcion[WS_UTIL.INDEX_REFERENCIA];

        return filtro.equals(valorBuscado);
    }

    private void construyePagoServicioDTOObject(String[] descripcion, PagoServicioDTO pagoServicioDTO, ResultSet rs, int datoHistorico) throws SQLException {
        pagoServicioDTO.setCuentaASP(rs.getString("cuenta"));

        if(WS_UTIL.PAGO_NO_HISOTRICO == datoHistorico){
            pagoServicioDTO.setMensajeEstatus(descripcion[WS_UTIL.INDEX_MENSAJE_ESTATUS]);
        }else if(WS_UTIL.PAGO_HISOTRICO == datoHistorico){
            pagoServicioDTO.setMensajeEstatus("");
        }

        pagoServicioDTO.setNumeroAutorizacion(descripcion[WS_UTIL.INDEX_AUTORIZACION]);
        pagoServicioDTO.setProveedor(descripcion[WS_UTIL.INDEX_PROVEEDOR]);
        pagoServicioDTO.setReferencia(descripcion[WS_UTIL.INDEX_REFERENCIA]);
        pagoServicioDTO.setEstatus(descripcion[WS_UTIL.INDEX_ESTATUS]);

        if(descripcion[WS_UTIL.INDEX_ESTATUS].equals("ERROR")){
            pagoServicioDTO.setMovimiento("");
        }else{
            pagoServicioDTO.setMovimiento("CARGO");
        }
        pagoServicioDTO.setFechaAplicacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_aplicacion")));
        pagoServicioDTO.setFechaOperacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_operacion")));
        pagoServicioDTO.setMonto(rs.getDouble("monto"));

        pagoServicioDTO.setDescripcionMovimiento("PAGO DE SERVICIOS");
    }

    private Set<ProveedorDTO> getProveedorFromResultSet(ResultSet rs) throws SQLException {
        Set<ProveedorDTO> proveedores = new HashSet<>();
        while (rs.next()) {
            ProveedorDTO proveedorDTO = new ProveedorDTO();
            proveedorDTO.setProveedor(rs.getString("proveedor_servicio"));
            proveedores.add(proveedorDTO);
        }

        return proveedores;
    }
}
