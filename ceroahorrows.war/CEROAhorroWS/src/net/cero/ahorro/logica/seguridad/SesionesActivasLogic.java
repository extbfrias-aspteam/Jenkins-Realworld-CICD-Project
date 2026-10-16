package net.cero.ahorro.logica.seguridad;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.data.seguridad.SeestatusOBJ;
import net.cero.data.seguridad.SesionesActivasOBJ;
import net.cero.seguridad.utilidades.ErroresWS;
import net.cero.spring.dao.seguridad.SeautenticadoDAO;
import net.cero.spring.dao.seguridad.SeestatusDAO;
import net.cero.ws.data.ToolsR;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class SesionesActivasLogic {
    private final SeautenticadoDAO seautenticadoRepository;
    private final SeestatusDAO seestatusRepository;
    private final Environment env;
    private final static Gson gson = ToolsR.GBuilder();

    public Respuesta buscarSesionesActivas(String usuario,int idAplicativo)
    {
        Respuesta resultado = new Respuesta();
        resultado.setMensaje("OK");
        try{
            String estatusAlta = env.getProperty("cero.estatus.activo");
            SeestatusOBJ alta = seestatusRepository.findSeestatusByNombre(estatusAlta);
            if(alta == null)
            {
                resultado.setCodigo(ErroresWS.NO_RESULTADOS);
                resultado.setMensaje(ErroresWS.descError.get(ErroresWS.NO_RESULTADOS));
                log.warn(resultado);
                return resultado;
            }
            List<SesionesActivasOBJ> list = seautenticadoRepository.findSeautenticadosByUsuario(alta.getId(),usuario,idAplicativo);
            if(list == null || list.isEmpty())
            {
                resultado.setCodigo(ErroresWS.NO_RESULTADOS);
                resultado.setMensaje("No se encontraron sesiones activas para el usuario proporcionado");
            }
            else
                resultado.setData(gson.toJson(list));
        }
        catch(Exception e) {
            resultado.setCodigo(ErroresWS.ERROR_INTERNO);
            resultado.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
            log.error(resultado,e);
        }
        return resultado;
    }

    public Respuesta cierraSesiones(String usuario,int usuarioModificacion,int idAplicativo)
    {
        Respuesta resultado = new Respuesta();
        try{

            resultado=buscarSesionesActivas(usuario,idAplicativo);
            resultado.setData(null);
            if(resultado.getCodigo()==ErroresWS.NO_RESULTADOS)
                return resultado;

            String estatusBaja = env.getProperty("cero.estatus.baja");
            SeestatusOBJ baja = seestatusRepository.findSeestatusByNombre(estatusBaja);
            if(baja == null)
            {
                resultado.setCodigo(ErroresWS.NO_RESULTADOS);
                resultado.setMensaje(ErroresWS.descError.get(ErroresWS.NO_RESULTADOS));
                log.warn(resultado);
                return resultado;
            }
            seautenticadoRepository.updateSeautenticado(baja.getId(),usuario,usuarioModificacion,idAplicativo);
            resultado.setMensaje("Sesiones cerradas con éxito");
        }
        catch(Exception e) {
            resultado.setCodigo(ErroresWS.ERROR_INTERNO);
            resultado.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
            log.error(resultado,e);
        }
        return resultado;
    }
}
