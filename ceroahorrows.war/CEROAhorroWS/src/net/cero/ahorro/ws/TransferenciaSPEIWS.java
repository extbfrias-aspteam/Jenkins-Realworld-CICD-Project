package net.cero.ahorro.ws;



import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.NuevoSpeiOutService;
import net.cero.data.Respuesta;
import net.cero.req.transferenciaspei.TransferenciaSpeiReq;
import net.cero.seguridad.utilidades.AplicacionUtils;
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
 * Clase usada para exponer un endpoint para realizar transferencias SPEI OUT
 */
@RestController
@Log4j2
@AllArgsConstructor
public class TransferenciaSPEIWS {
    private static Gson gson = ToolsR.GBuilder();
    private final NuevoSpeiOutService nuevoSpeiOutService;
    private final AplicacionUtils aplicacionUtils;

    /**
     * Endpoint para exponer la funcionalidad del registro de un SPEI OUT para una cuenta por medio de su clave ASP O clabe interbancaria
     * @param request Objeto con los datos del request necesarios para el WS
     * @param validacion Objeto interno usado para la validación de los datos que vengan en el request
     * @return Objeto que contiene el resultado de la ejecución del endpoint
     */
    @PostMapping("/generarNuevoSpeiOut")
    public ResponseEntity<Respuesta> realizarTransferenciaSpeiOut(@Valid @RequestBody TransferenciaSpeiReq request,
                                                                  BindingResult validacion, HttpServletRequest httpServletRequest)
    {
        Respuesta respuesta = new Respuesta();
        try{
            if(validacion.hasErrors()) {
                // Inicializacion de objeto de respuesta
                respuesta = new Respuesta();
                // Seteo de datos
                respuesta.setCodigo(1);
                if(validacion.getFieldError() != null && !StringUtils.isBlank(validacion.getFieldError().getDefaultMessage()))
                    if(validacion.getFieldError().getDefaultMessage().contains("%s"))
                        respuesta.setMensaje(String.format(validacion.getFieldError().getDefaultMessage(),validacion.getFieldError().getField()));
                    else
                        respuesta.setMensaje(validacion.getFieldError().getDefaultMessage());
                else
                    respuesta.setMensaje("Campos Incorrectos");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }else {
                respuesta = nuevoSpeiOutService.procesarEnvioSpei(request);
                if(respuesta.getCodigo() == -1)
                    return ResponseEntity.internalServerError().body(respuesta);
                else
                    return ResponseEntity.ok(respuesta);
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al tratar de procesar la transferencia SPEI",e);
            respuesta.setCodigo(1);
            respuesta.setMensaje("Ocurrio un error al tratar de procesar la transferencia SPEI");
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
