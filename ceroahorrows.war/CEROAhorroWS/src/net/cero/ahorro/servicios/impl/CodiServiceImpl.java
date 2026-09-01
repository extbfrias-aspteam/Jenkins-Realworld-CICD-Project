package net.cero.ahorro.servicios.impl;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.common.DateUtil;
import net.cero.ahorro.common.DbBeans;
import net.cero.ahorro.common.JDBCUtil;
import net.cero.ahorro.common.SqlQueryParams;
import net.cero.ahorro.servicios.CodiService;
import net.cero.data.ConsultaOperacionesCodiDTO;
import net.cero.data.ConsultaOperacionesCodiReqDTO;
import net.cero.data.Respuesta;
import net.cero.spring.config.IPAuthenticationProvider;
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
import java.util.List;

@Service
@Log4j2
public class CodiServiceImpl implements CodiService {

    @Autowired
    protected IPAuthenticationProvider authenticationManager;

    private DataSource driverSourceCero;
    private Gson gson;

    private String QUERY_CONSULTA_SPEI = "SELECT pg.nombre_comprador nombre_emisor, pg.cuenta_pago_comprador as cuenta_origen,si2.descripcion as  banco_origen,\n" +
                                                "pg.nombre_vendedor nombre_beneficiario, pg.telefono_vendedor , pg.cuenta_cobro_vendedor as cuenta_destino,  si.descripcion as banco_destino,\n" +
                                                "pg.monto,\n" +
                                                "ec.estatus,\n" +
                                                "pg.concepto ,\n" +
                                                "coalesce(pg.clave_rastreo , '') as clave_rastreo,\n" +
                                                "pg.referencia ,\n" +
                                                "pg.fecha_aplicacion ,\n" +
                                                "pg.fecha_creacion as fecha_operacion,\n" +
                                                "case \n" +
                                                        "when si2.id_institucion = 90659  then \n" +
                                                        "case when si.id_institucion = 90659 then 'ABONO'\n" +
                                                        "else 'CARGO' end\n" +
                                                        "else 'ABONO' end  tipo_operacion\n" +
                                                "FROM codi.pagos_generados pg \n" +
                                                "    LEFT JOIN codi.estatus_pagos ec ON pg.estatus_pago_id = ec.id\n" +
                                                "inner join \n" +
                                                "\tspei_instituciones si on si.id_institucion  = pg.clave_institucion_vendedor \n" +
                                                "inner join \n" +
                                                "\tspei_instituciones si2 on si2.id_institucion = pg.clave_institucion_comprador\n" +
                                                "where pg.fecha_creacion between ? and  ?";

    @PostConstruct
    void init(){
        gson = new Gson();
        initialized();
    }

    private void initialized() {
        driverSourceCero = DbBeans.DbBeansImpl.getDataSource("ds");
    }
    @Override
    public Respuesta consultaOperacionesCodi(ConsultaOperacionesCodiReqDTO consultaOperacionesSpeiDTO, BindingResult bindingResult) {
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
        if(DateUtil.compareDaysBetweenDates(consultaOperacionesSpeiDTO.getFechaInicio(), consultaOperacionesSpeiDTO.getFechaFin())){
            respuesta.setCodigo(4);
            respuesta.setMensaje("El periodo de operacion no debe ser mayor a 90 días");
            respuesta.setData("");
            return respuesta;
        }

        try{
            PreparedStatement preparedStatement = JDBCUtil.getPreparedStatementFromDs(QUERY_CONSULTA_SPEI, driverSourceCero);

            List<SqlQueryParams> params = new ArrayList<>();

            Timestamp fechaIinicio = DateUtil.dateToSqlTimestamp(consultaOperacionesSpeiDTO.getFechaInicio(), false);
            Timestamp fechaFin = DateUtil.dateToSqlTimestamp(consultaOperacionesSpeiDTO.getFechaFin(), true);

            params.add(new SqlQueryParams(Types.TIMESTAMP, fechaIinicio));

            params.add(new SqlQueryParams(Types.TIMESTAMP, fechaFin));

            ResultSet rs = JDBCUtil.executePreparedStatementWithParams(preparedStatement, params);
            List<ConsultaOperacionesCodiDTO> operacionesSpeiDTOList = construyeListaOperaciones(rs);

            respuesta.setCodigo(0);
            respuesta.setMensaje("Consulta exitosa");
            respuesta.setData(gson.toJson(operacionesSpeiDTOList));
        }catch (SQLException e){
            log.error("Error al consultar las operaciones CODI", e);
            respuesta.setCodigo(4);
            respuesta.setMensaje("Hubo un error al consultar las operaciones CODI");
            respuesta.setData("");
        }


        return respuesta;
    }

    private List<ConsultaOperacionesCodiDTO> construyeListaOperaciones(ResultSet rs) throws SQLException {
        List<ConsultaOperacionesCodiDTO> operacionesSpeiDTOList = new ArrayList<>();
        while (rs.next()) {
            ConsultaOperacionesCodiDTO consultaOperacionesCodiDTO = new ConsultaOperacionesCodiDTO();

            consultaOperacionesCodiDTO.setNombreEmisor(rs.getString("nombre_emisor"));
            consultaOperacionesCodiDTO.setCuentaOrigen(rs.getString("cuenta_origen").trim());
            consultaOperacionesCodiDTO.setBancoOrigen(rs.getString("banco_origen"));

            consultaOperacionesCodiDTO.setNombreBeneficiario(rs.getString("nombre_beneficiario"));
            consultaOperacionesCodiDTO.setCuentaDestino(rs.getString("cuenta_destino"));
            consultaOperacionesCodiDTO.setBancoDestino(rs.getString("banco_destino").trim());

            consultaOperacionesCodiDTO.setEstatus(rs.getString("estatus"));
            consultaOperacionesCodiDTO.setMonto(rs.getDouble("monto"));
            consultaOperacionesCodiDTO.setDescripcionMovimiento(rs.getString("concepto"));
            consultaOperacionesCodiDTO.setClaveRastreo(rs.getString("clave_rastreo"));
            consultaOperacionesCodiDTO.setReferencia(rs.getString("referencia"));
            consultaOperacionesCodiDTO.setFechaAplicacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_aplicacion")));
            consultaOperacionesCodiDTO.setFechaOperacion(DateUtil.getStringDateFromSqlDate(rs.getDate("fecha_operacion")));
            consultaOperacionesCodiDTO.setTipoOperacion(rs.getString("tipo_operacion"));

            operacionesSpeiDTOList.add(consultaOperacionesCodiDTO);
        }

        return operacionesSpeiDTOList;
    }
}
