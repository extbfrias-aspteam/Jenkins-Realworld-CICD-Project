package net.cero.spring.dao;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.model.AplicacionOBJ;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Clase de la capa de datos para operaciones relacionadas a la tabla de auditoria de webservices
 */
@Log4j2
@Repository
public class AuditoriaWsDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String qInsertaAuditoriaMensaje;
    private final String qTraspasoHistorico;

    public AuditoriaWsDAO(@Qualifier("namedCeroJdbcTemplate")NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                          @Value("${app.dao.ins.bitacora}")String qInsertaAuditoriaMensaje,
                          @Value("${app.dao.ins.historico}")String qTraspasoHistorico) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.qInsertaAuditoriaMensaje = qInsertaAuditoriaMensaje;
        this.qTraspasoHistorico = qTraspasoHistorico;
    }

    /**
     * Metodo de la capa de datos para registrar la auditoria de consumo de ws en base de datos
     * @param aplicacion Objeto del tipo AplicacionOBJ asociado al endpoint a registrar la auditoria
     * @param usuarioId id del usuario que ejecuta la peticion
     * @param recurso endpoint al que se desea asociar la auditoria
     * @param ipOrigen Ip desde donde se origino la petición
     * @param req String Objeto request serializado para ser guardado como una cadena
     * @param resp Objeto response serializado para ser guardado como una cadena
     * @return Objeto Respuesta con información del resultado de la operacion
     */
    public Respuesta insertaAuditoriaWS(AplicacionOBJ aplicacion,Integer usuarioId,String recurso,String ipOrigen, String req, String resp)
    {
        Respuesta resultado = new Respuesta();
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("ncaplicativoId",aplicacion.getId());
            parametros.put("recurso",recurso);
            parametros.put("ipOrigen",ipOrigen);
            parametros.put("request",req);
            parametros.put("response",resp);
            parametros.put("usuarioCreacion",usuarioId);
            int res = namedParameterJdbcTemplate.update(qInsertaAuditoriaMensaje,parametros);
            if(res == 0)
            {
                resultado.setCodigo(-1);
                resultado.setMensaje("No se realizo el registro de la auditoria");
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al ejecutar la consulta del metodo insertaAuditoriaReenvioMsg",e);
            resultado.setCodigo(-1);
            resultado.setMensaje("Ocurrió un problema al registrar la auditoria del reenvio");
        }
        return resultado;
    }

    /**
     * Metodo usado para ejecutar el traspaso de registros de la tabla principal a la historica para vaciar la info.
     * @param fecha Fecha a la que se realiza el traspaso de registros. Es decir, todo lo que esté antes de esa fecha, se mandará a historico
     * @return Objeto del tipo Respuesta con el resultado de la operacion
     */
    public Respuesta traspasoHistoricoAuditoriaWS(LocalDate fecha)
    {
        Respuesta resultado = new Respuesta();
        try{
            HashMap<String,Object> parametros = new HashMap<>();
            parametros.put("fecha", Timestamp.valueOf(fecha.atTime(0,0,0)));
            String valor =namedParameterJdbcTemplate.queryForObject(qTraspasoHistorico,parametros,String.class);
            if(StringUtils.isBlank(valor) || !valor.equals("OK"))
            {
                resultado.setCodigo(-1);
                resultado.setMensaje("No se realizo el registro de la auditoria");
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrió un error al ejecutar la consulta del metodo insertaAuditoriaReenvioMsg",e);
            resultado.setCodigo(-1);
            resultado.setMensaje("Ocurrió un problema al registrar la auditoria del reenvio");
        }
        return resultado;
    }
}
