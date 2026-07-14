package net.cero.ahorro.ws.seguridad;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.logica.seguridad.SesionesActivasLogic;
import net.cero.data.Respuesta;
import net.cero.req.seguridad.CerrarSesionesActivasReq;
import net.cero.seguridad.utilidades.AplicacionUtils;
import net.cero.seguridad.utilidades.ErroresWS;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@Log4j2
@AllArgsConstructor
@Validated
public class SesionesController {
    private final SesionesActivasLogic sesionesActivasSvc;
    private final AplicacionUtils aplicacionUtils;
    private final static Gson gson = ToolsR.GBuilder();

    @GetMapping("buscarSesionesActivas")
    public ResponseEntity<Respuesta> buscarSesionesActivas(
            @RequestParam("usuario") String usuario,@RequestParam("idAplicativo") Integer idAplicativo)
    {
        Respuesta resultado = new Respuesta();
        log.info("Consulta de sesiones: {},idAplicativo: {}",usuario,idAplicativo);
        if(StringUtils.isBlank(usuario))
        {
            resultado.setMensaje("El parametro usuario es obligatorio");
            resultado.setCodigo(ErroresWS.CAMPOS_INCORRECTOS);
            return ResponseEntity.badRequest().body(resultado);
        }
        if(idAplicativo==null)
        {
            resultado.setMensaje("El parametro idAplicativo es obligatorio");
            resultado.setCodigo(ErroresWS.CAMPOS_INCORRECTOS);
            return ResponseEntity.badRequest().body(resultado);
        }
        resultado = sesionesActivasSvc.buscarSesionesActivas(usuario,idAplicativo);
        return ResponseEntity.ok(resultado);
    }


    @PostMapping("cerrarSesionesActivas")
    public ResponseEntity<Respuesta> cerrarSesionesActivas(
            @RequestBody CerrarSesionesActivasReq req, BindingResult validacion, HttpServletRequest httpServletRequest)
    {
        Respuesta respuesta= new Respuesta();
        log.info("cerrarSesionesActivas request: {}",req);
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

            }
            respuesta = sesionesActivasSvc.cierraSesiones(req.getUsuario(),
                    (int)req.getHeader().getIdUsuario(),req.getIdAplicativo());
            return ResponseEntity.ok(respuesta);
        }
        catch(Exception e)
        {
            respuesta.setCodigo(ErroresWS.ERROR_INTERNO);
            respuesta.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
            log.error(respuesta,e);
            return ResponseEntity.internalServerError().body(respuesta);
        }
        finally{
            log.info("Bloque finally...");
            Respuesta resTemp = aplicacionUtils.insertaAuditoriaWS((int)req.getHeader().getIdUsuario(),
                    httpServletRequest.getRequestURI(),httpServletRequest.getRemoteAddr(),gson.toJson(req),gson.toJson(respuesta));
            log.info("Registro bitacora resTemp: {}",resTemp);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @Order(value = 1)
    public ResponseEntity<Respuesta> handleValidationExceptions(
            MissingServletRequestParameterException ex) {
        log.warn("Datos incompletos...");
        Respuesta resp = new Respuesta();
        resp.setCodigo(ErroresWS.FALTA_INFORMACION);
        resp.setMensaje(String.format("El parametro %s es obligatorio",ex.getParameterName()));
        log.warn("Fin Datos incompletos... {}",resp);

        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={ Exception.class })
    @Order(value = 2)
    public ResponseEntity<Respuesta> handleUncaughException(
            Exception ex, WebRequest request)
    {
        Respuesta resp = new Respuesta();
        resp.setCodigo(ErroresWS.ERROR_INTERNO);
        resp.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
        log.error("Se detectó un error no controlado en la aplicacion.",ex);

        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
