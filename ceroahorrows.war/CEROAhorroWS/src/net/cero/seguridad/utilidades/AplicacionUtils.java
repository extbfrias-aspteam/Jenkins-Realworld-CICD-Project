package net.cero.seguridad.utilidades;

import lombok.Getter;
import net.cero.data.Respuesta;
import net.cero.model.AplicacionOBJ;
import net.cero.spring.dao.AplicacionesDAO;
import net.cero.spring.dao.AuditoriaWsDAO;
import org.springframework.stereotype.Component;

/**
 * Clase de apoyo para registrar auditoria de la información relacionada a los request y response de los endpoints que
 * implemente esta clase
 */
@Component
@Getter
public class AplicacionUtils {

    private final AplicacionOBJ aplicacion;
    private final AuditoriaWsDAO auditoriaWsDAO;
    public AplicacionUtils(AplicacionesDAO aplicacionesDAO,AuditoriaWsDAO auditoriaWsDAO) {
        this.auditoriaWsDAO = auditoriaWsDAO;
        this.aplicacion= aplicacionesDAO.consultarDatosAplicacion(ConstantesUtil.CERO_AHORRO_WS_CLAVE);
    }

    /**
     * Metodo para registrar una auditoria de una petición con sus respectivos datos para ser almacenados en base de daots
     * @param usuarioId id del usuario que ejecuta la peticion
     * @param recurso endpoint al que se desea asociar la auditoria
     * @param ipOrigen Ip desde donde se origino la petición
     * @param req String Objeto request serializado para ser guardado como una cadena
     * @param resp Objeto response serializado para ser guardado como una cadena
     * @return Objeto Respuesta con información del resultado de la operacion
     */
    public Respuesta insertaAuditoriaWS(Integer usuarioId, String recurso, String ipOrigen, String req, String resp)
    {
        return auditoriaWsDAO.insertaAuditoriaWS(this.aplicacion,usuarioId,
                recurso,ipOrigen,req,resp);
    }
}
