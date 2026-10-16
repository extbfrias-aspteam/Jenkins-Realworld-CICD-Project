package net.cero.quartz;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.data.Respuesta;
import net.cero.spring.dao.AuditoriaWsDAO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;


/**
 * Clase empleada para ejecutarse por medio de un quartz para traspasar los registros
 * de historico de invocaciones de WS a historico del día anterior
 * @author AASTORGA
 */
@Component
@Log4j2
@AllArgsConstructor
public class TraspasoJob {
    private final AuditoriaWsDAO auditoriaWsDAO;

    /**
     * Cron empleado para ejecutar el metodo en donde es empleado, solo una vez cada dia a la media noche.
     */
    @Scheduled(cron = "0 0 0 * * *")
    private void ejecutaTraspaso()
    {
        UUID uuid = UUID.randomUUID();
        log.info("{} =====================Inicia proceso de traspaso de registros de auditoria a historico=====================",uuid);
        try{
            Respuesta resp = auditoriaWsDAO.traspasoHistoricoAuditoriaWS(LocalDate.now());
            log.info("{} Resultado de traspaso a historico: {}",uuid,resp);
        }
        catch(Exception e)
        {
            log.error("{} Ocurrio un detalle al intentar traspaso a historico los registros",uuid,e);
        }
        finally
        {
            log.info("{} =====================Fin proceso de traspaso de registros de auditoria a historico=====================",uuid);
        }

    }
}
