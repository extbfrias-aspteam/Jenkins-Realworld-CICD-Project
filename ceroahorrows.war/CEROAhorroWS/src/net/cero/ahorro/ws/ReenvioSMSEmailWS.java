package net.cero.ahorro.ws;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.logica.enviomsgs.AReenviarMensaje;
import net.cero.ahorro.logica.enviomsgs.ReenviarCodigo;
import net.cero.ahorro.logica.enviomsgs.ReenviarCorreoValidacion;
import net.cero.data.Respuesta;
import net.cero.model.TipoReenvioOBJ;
import net.cero.req.reenviarws.ReenviarMensajeReq;
import net.cero.seguridad.utilidades.AplicacionUtils;
import net.cero.spring.dao.ReenviosMensajesDAO;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * Clase empleada para exponer servicios para el reenvio de mensajes SMS o Correo
 */
@AllArgsConstructor
@Log4j2
@RestController
public class ReenvioSMSEmailWS {
    private static Gson gson = ToolsR.GBuilder();
    private final ReenviosMensajesDAO reenviosMensajesDAO;
    private final AplicacionUtils aplicacionUtils;
    private final ApplicationContext applicationContext;

    /**
     * WS para obtener el catalogo de tipos de reenvios existentes
     * @return Objeto del tipo respuesta con el resultado de la operacion
     */
    @GetMapping("/consultarCatalogoReenvios")
    public ResponseEntity<Respuesta> consultarCatalogoReenvios() {
        Respuesta respuesta = new Respuesta();
        try{
            List<TipoReenvioOBJ> listado = reenviosMensajesDAO.consultarMovimientosReenvio();
            if(listado != null && !listado.isEmpty())
            {
                respuesta.setMensaje("OK");
                respuesta.setData(gson.toJson(listado));
            }
            else
            {
                respuesta.setCodigo(2);
                respuesta.setMensaje("No se encontraron registros disponibles");
            }
            return ResponseEntity.ok(respuesta);
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al momento de consultar el catalogo de movimientos para reenvio",e);
            respuesta.setCodigo(1);
            respuesta.setMensaje("Ocurrio un error al momento de consultar el catalogo de movimientos para reenvio");
            return ResponseEntity.internalServerError().body(respuesta);
        }
    }

    /**
     * Servicio empleado para realizar el reenvio de algun tipo de mensaje soportado por la operacion como el token del mensaje o
     * de la validacion del correo
     * @param request Objeto con los campos necesarios para realizar la operacion
     * @param validacion Objeto interno usado para ejecutar las reglas o validaciones designadas a cada atributo del objeto del request
     * @param httpServletRequest Objeto trae información adicional relacionada al objeto del request que se recibe
     * @return Regresa un objeto del tipo Respuesta con el resultado de la operación.
     */
    @PostMapping("/reenviarMensaje")
    public ResponseEntity<Respuesta> reenviarMensaje(@Valid @RequestBody ReenviarMensajeReq request,
                                                     BindingResult validacion, HttpServletRequest httpServletRequest) {
        Respuesta respuesta = new Respuesta();
        try{
            if(validacion.hasErrors()) {
                // Inicializacion de objeto de respuesta
                respuesta = new Respuesta();
                // Seteo de datos
                respuesta.setCodigo(2);
                if(validacion.getFieldError() != null && !StringUtils.isBlank(validacion.getFieldError().getDefaultMessage()))
                    if(validacion.getFieldError().getDefaultMessage().contains("%s"))
                        respuesta.setMensaje(String.format(validacion.getFieldError().getDefaultMessage(),validacion.getFieldError().getField()));
                    else
                        respuesta.setMensaje(validacion.getFieldError().getDefaultMessage());
                else
                    respuesta.setMensaje("Campos Incorrectos");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }else {
                TipoReenvioOBJ tipoReenvio = reenviosMensajesDAO.consultarMovimientosReenvioPorClave(request.getTipoReenvio());
                if(tipoReenvio == null)
                {
                    respuesta.setCodigo(3);
                    respuesta.setMensaje("El tipo de reenvio no es valido. Favor de verifcarlo.");
                    return ResponseEntity.badRequest().body(respuesta);
                }
                if(!tipoReenvio.getActivo())
                {
                    respuesta.setCodigo(4);
                    respuesta.setMensaje(String.format("El tipo de reenvio %S no se encuentra activo.",tipoReenvio.getClave()));
                    return ResponseEntity.badRequest().body(respuesta);
                }
                AReenviarMensaje reenvioMensaje=null;
                switch(tipoReenvio.getClave())
                {
                    case "VALID_CORREO":
                        reenvioMensaje = applicationContext.getBean("reenviarCorreoValidacion", ReenviarCorreoValidacion.class);
                        break;
                    case "SMS_DISP_DIFF":
                    case "SMS_COD_SEG":
                        reenvioMensaje = applicationContext.getBean("reenviarCodigo", ReenviarCodigo.class);
                        break;
                }
                if(reenvioMensaje != null)
                    respuesta = reenvioMensaje.ejecutarReenvioMensaje(request);
                return ResponseEntity.ok(respuesta);
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al tratar de realizar el reenvio del mensaje",e);
            respuesta.setCodigo(1);
            respuesta.setMensaje("Ocurrio un error al tratar de realizar el reenvio del mensaje");
            return ResponseEntity.internalServerError().body(respuesta);
        }
        finally{
            log.info("Bloque finally...");
            Respuesta resTemp = aplicacionUtils.insertaAuditoriaWS((int)request.getHeader().getIdUsuario(),
                    httpServletRequest.getRequestURI(),httpServletRequest.getRemoteAddr(),gson.toJson(request),gson.toJson(respuesta));
            log.info("Registro bitacora resTemp: {}",resTemp);
        }
    }
}
