package mx.net.asp.asp_pago_api.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.config.JwtEndpointsExcluidosProperties;
import mx.net.asp.asp_pago_api.config.JwtEndpointsExternosProperties;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.RespuestaUtils;
import mx.net.asp.asp_pago_api.utilerias.errores.ErroresGenerales;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoJWT;
import mx.net.asp.asp_pago_api.ws.asp.request.ValidaJWTReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class JwtValidationFilter extends OncePerRequestFilter {

    @Value("${ms.jwt.valida.jwt.service}")
    private String validaJwtService;
    @Value("${path.login.version}")
    private String pathLogInV;
    @Value("${path.pwd.reset}")
    private String pathPwdReset;
    @Value("${path.api.storage}")
    private String pathApiStorage;
    @Value("${valida.jwt.token}")
    private Boolean validaJwtToken;
    @Value("${jwt.default.idcanal:456}")
    private Integer defaultIdCanal;
    @Value("${jwt.idcanal.eiyu}")
    private Integer idCanalEiYu;
    @Value("${aes.key.cambio.pass}")
    private String cambioPassCipherKey;
    @Value("${security.storage.token}")
    private String storageToken;

    private final JwtEndpointsExcluidosProperties endpointsExcluidos;
    private final JwtEndpointsExternosProperties endpointsExternos;
    private final WsAspPagoJWT wsAspPagoJWT;
    private final CifradoUtil cifradoUtil;

    public JwtValidationFilter(JwtEndpointsExcluidosProperties endpointsExcluidos, JwtEndpointsExternosProperties endpointsExternos,
                               WsAspPagoJWT wsAspPagoJWT, CifradoUtil cifradoUtil) {
        this.cifradoUtil = cifradoUtil;
        this.endpointsExcluidos = endpointsExcluidos;
        this.endpointsExternos = endpointsExternos;
        this.wsAspPagoJWT = wsAspPagoJWT;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (Boolean.TRUE.equals(validaJwtToken)) {
            if (!endpointsExcluidos.getExcluidos().stream()
                    .anyMatch(excluido -> request.getRequestURI().contains(excluido))) {

                log.info("Entrando a JWT Filter para {} con X-JWT-Token: {}", request.getRequestURI(), request.getHeader("X-JWT-Token"));

                // Canal
                String canalHeader = request.getHeader("X-Id-Canal");
                Integer idCanal = canalHeader != null ?
                        Integer.valueOf(canalHeader) : defaultIdCanal;

                if (!endpointsExternos.getExternos().stream()
                        .anyMatch(externo -> request.getRequestURI().contains(externo))) {
                    if ((request.getRequestURI().contains(pathLogInV) && idCanal.equals(defaultIdCanal))
                            || request.getRequestURI().contains(pathPwdReset))
                        log.info("X-Telefono cif: {}", request.getHeader("X-Telefono"));
                    else if (idCanal.equals(defaultIdCanal))
                        log.info("X-Id-Persona cif: {}", request.getHeader("X-Id-Persona"));
                }

                //obtener cabecera JWT
                String jwtHeader = "";
                if (request.getHeader("X-JWT-Token") != null && !request.getHeader("X-JWT-Token").isEmpty()) {
                    jwtHeader = request.getHeader("X-JWT-Token");
                }

                String idPersona = "";
                String telefono = "";

                RespuestaDTO respuestaDTO = new RespuestaDTO();
                if (endpointsExternos.getExternos().stream()
                        .anyMatch(externo -> request.getRequestURI().contains(externo))) {
                    if (StringUtils.isBlank(canalHeader))
                        idCanal = idCanalEiYu;
                } else {
                    if (request.getRequestURI().contains(pathLogInV) || request.getRequestURI().contains(pathPwdReset)) {
                        if (request.getHeader("X-Telefono") != null && !request.getHeader("X-Telefono").isEmpty()) {
                            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request.getHeader("X-Telefono"));
                            if (respuestaDTO.getCodigo() == 0) {
                                telefono = respuestaDTO.getData();
                                log.info("X-Telefono desencriptado: {}", telefono);
                            } else {
                                log.error(respuestaDTO.getMensaje() + " de header X-Telefono");
                            }
                        }

                    } else if (!endpointsExcluidos.getExcluidos().stream()
                            .anyMatch(excluido -> request.getRequestURI().contains(excluido)) && idCanal.equals(defaultIdCanal)) {
                        if (request.getHeader("X-Id-Persona") != null && !request.getHeader("X-Id-Persona").isEmpty()) {
                            respuestaDTO = cifradoUtil.decodeAndDecryptRequest(request.getHeader("X-Id-Persona"));
                            if (respuestaDTO.getCodigo() == 0) {
                                idPersona = respuestaDTO.getData();
                                log.info("X-Id-Persona desencriptada: {}", idPersona);
                            } else {
                                log.error(respuestaDTO.getMensaje() + " de header X-Id-Persona");
                            }
                        }
                    }
                }

                log.info("X-Id-Canal: {}", idCanal);

                // Llamar al servicio para validar JWT
                ValidaJWTReq validaJWTReq = new ValidaJWTReq(
                        jwtHeader,
                        request.getRequestURI(),
                        idPersona,
                        telefono,
                        idCanal
                );

                RespuestaDTO respuesta = wsAspPagoJWT.enviarPeticion(validaJwtService, validaJWTReq, HttpMethod.POST, null);
                if (respuesta.getCodigo() != 0) {
                    enviarErrorJson(response, request, respuesta, idCanal);
                    return;
                }

                log.info("JWT Validado correctamente");
            } else if (request.getRequestURI().contains(pathApiStorage)){
                //validacion mediante X-Storage-Key

                String token = request.getHeader("X-Storage-Key");
                log.info("X-Storage-Key:: {}", token);
                if (StringUtils.isBlank(token) || !token.equals(storageToken)) {
                    RespuestaDTO respuesta = new RespuestaDTO();
                    RespuestaUtils.asignarError(respuesta,
                            -401,
                            "No autorizado.");
                    enviarErrorJson(response, request, respuesta, defaultIdCanal);
                    return;
                }
            }
        } else {
            log.info("=== Validacion JWT deshabilitada ===");
        }

        filterChain.doFilter(request, response);
    }

    private void enviarErrorJson(HttpServletResponse response, HttpServletRequest request, RespuestaDTO respuesta, Integer idCanal) throws IOException {
        log.error("Error validacion de seguridad: {}", respuesta.getMensaje());

        response.setCharacterEncoding("UTF-8");

        // Usando Jackson para serializar objeto a JSON
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(respuesta);

        if (!idCanal.equals(defaultIdCanal) || request.getRequestURI().contains("generarAbono")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            if (request.getRequestURI().contains(pathPwdReset))
                json = cifradoUtil.encryptResponse(json, cambioPassCipherKey);
            else
                json = cifradoUtil.encryptResponse(json);
        }

        response.getWriter().write(json);
    }
}
