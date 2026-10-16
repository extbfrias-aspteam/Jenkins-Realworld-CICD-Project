package net.cero.ahorro.ws.codi;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.logica.codi.CodiConsultaEstatusLogic;
import net.cero.ahorro.logica.codi.CodiConsultaOperacionesLogic;
import net.cero.data.Respuesta;
import net.cero.req.codi.ConsultarEstatusCodiReq;
import net.cero.req.codi.ConsultarOperacionesCodiReq;
import net.cero.seguridad.utilidades.AplicacionUtils;
import net.cero.seguridad.utilidades.ErroresWS;
import net.cero.ws.data.ToolsR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Clase empleada para exponer servicios para consyltar info sobre operaciones CoDi de cuentas que soporten dichas operaciones
 * @author AASTORGA
 */
@RestController
@Log4j2
@AllArgsConstructor
public class ConsultaOperacionesCodiWS {
    private static Gson gson = ToolsR.GBuilder();
    private final AplicacionUtils aplicacionUtils;
    private final CodiConsultaOperacionesLogic codiConsultaOperacionesLogic;
    private final CodiConsultaEstatusLogic codiConsultaEstatus;

    /**
     * Se consylta la info en particular de una operacion codi por medio de una referencia o folio y una fecha de operacion
     * @param request objeto que tiene los datos para realizar la busqueda de la operacion
     * @param validacion Objeto interno usado para ejecutar las reglas o validaciones designadas a cada atributo del objeto del request
     * @param httpServletRequest Objeto trae información adicional relacionada al objeto del request que se recibe
     * @return Regresa un objeto del tipo Respuesta con el resultado de la operación.
     */
    @PostMapping("/consultaEstatusCodi")
    public ResponseEntity<Respuesta> consultarEstatusCodi(@Valid @RequestBody ConsultarEstatusCodiReq request,
                                                          BindingResult validacion, HttpServletRequest httpServletRequest)
    {
        Respuesta respuesta = new Respuesta();
        try{
            if(validacion.hasErrors()) {
                // Inicializacion de objeto de respuesta
                respuesta = new Respuesta();
                // Seteo de datos
                respuesta.setCodigo(ErroresWS.CAMPOS_INCORRECTOS);
                if(validacion.getFieldError() != null && !StringUtils.isBlank(validacion.getFieldError().getDefaultMessage()))
                    if(validacion.getFieldError().getDefaultMessage().contains("%s"))
                        respuesta.setMensaje(String.format(validacion.getFieldError().getDefaultMessage(),validacion.getFieldError().getField()));
                    else
                        respuesta.setMensaje(validacion.getFieldError().getDefaultMessage());
                else
                    respuesta.setMensaje(ErroresWS.descError.get(ErroresWS.CAMPOS_INCORRECTOS));
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
            else
            {
                respuesta = codiConsultaEstatus.consultarEstatus(request);
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al tratar de procesar la petición dentro del metodo consultaEstatusCodi",e);
            respuesta.setCodigo(ErroresWS.ERROR_INTERNO);
            respuesta.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
            return ResponseEntity.internalServerError().body(respuesta);
        }
        finally{
            log.info("Codigo finally:");
            Respuesta resTemp = aplicacionUtils.insertaAuditoriaWS((int)request.getHeader().getIdUsuario(),
                    httpServletRequest.getRequestURI(),httpServletRequest.getRemoteAddr(),gson.toJson(request),gson.toJson(respuesta));
            log.info("Registro bitacora resTemp: {}",resTemp);
        }
    }

    /**
     * Permite consultar operaciones codi de una cuenta ya sea por la cuenta ASP o la CLABE que se hayan realizado en un periodo dado
     * @param request objeto que tiene los datos para realizar la busqueda de la operacion
     * @param validacion Objeto interno usado para ejecutar las reglas o validaciones designadas a cada atributo del objeto del request
     * @param httpServletRequest Objeto trae información adicional relacionada al objeto del request que se recibe
     * @return Regresa un objeto del tipo Respuesta con el resultado de la operación.
     */
    @PostMapping("/consultaOperacionesCodi")
    public ResponseEntity<Respuesta> consultarOperacionesCodi(@Valid @RequestBody ConsultarOperacionesCodiReq request,
                                                              BindingResult validacion,HttpServletRequest httpServletRequest)
    {
        Respuesta respuesta = new Respuesta();
        try{
            if(validacion.hasErrors()) {
                respuesta = new Respuesta();
                respuesta.setCodigo(ErroresWS.CAMPOS_INCORRECTOS);
                if(validacion.getFieldError() != null && !StringUtils.isBlank(validacion.getFieldError().getDefaultMessage()))
                    if(validacion.getFieldError().getDefaultMessage().contains("%s"))
                        respuesta.setMensaje(String.format(validacion.getFieldError().getDefaultMessage(),validacion.getFieldError().getField()));
                    else
                        respuesta.setMensaje(validacion.getFieldError().getDefaultMessage());
                else
                    respuesta.setMensaje(ErroresWS.descError.get(ErroresWS.CAMPOS_INCORRECTOS));
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
            else
            {
                respuesta = codiConsultaOperacionesLogic.consultarOperaciones(request);
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al tratar de procesar la petición dentro del metodo consultaOperacionesCodi",e);
            respuesta.setCodigo(ErroresWS.ERROR_INTERNO);
            respuesta.setMensaje(ErroresWS.descError.get(ErroresWS.ERROR_INTERNO));
            return ResponseEntity.internalServerError().body(respuesta);
        }
        finally{
            log.info("Codigo finally:");
            Respuesta resTemp = aplicacionUtils.insertaAuditoriaWS((int)request.getHeader().getIdUsuario(),
                    httpServletRequest.getRequestURI(),httpServletRequest.getRemoteAddr(),gson.toJson(request),gson.toJson(respuesta));
            log.info("Registro bitacora resTemp: {}",resTemp);
        }
    }
}
