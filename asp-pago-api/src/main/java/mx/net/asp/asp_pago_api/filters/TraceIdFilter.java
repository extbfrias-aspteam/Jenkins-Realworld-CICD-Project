package mx.net.asp.asp_pago_api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.config.EndpointsDisabledProperties;
import mx.net.asp.asp_pago_api.config.SuffixesCanalesPermitidosProperties;
import mx.net.asp.asp_pago_api.config.TraceSuffixProperties;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Order(0) // Ejecutar primero
@Log4j2
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private final ErrorHandler errorHandler;
    private final CifradoUtil cifradoUtil;

    @Value("${secret.key.uuid}")
    private String SECRET_KEY_UUID;
    @Value("${trace.id.header}")
    private String TRACE_ID_HEADER;
    @Value("${trace.id.header.signature}")
    private String TRACE_SIGNATURE_HEADER;
    @Value("${trace.suffix}")
    private String TRACE_SUFFIX;
    @Value("${path.pwd.reset}")
    private String pathPwdReset;
    @Value("${jwt.default.idcanal:456}")
    private Integer defaultIdCanal;
    @Value("${aes.key.cambio.pass}")
    private String cambioPassCipherKey;

    private final TraceSuffixProperties listSuffixesUUID;
    private final EndpointsDisabledProperties endpointsDisabledProperties;
    private final SuffixesCanalesPermitidosProperties canalesPermitidos;

    private static final List<String> ENDPOINTS_EXCLUIDOS_FIRMA = Arrays.asList(
            "obtenerDatosInicialesV"
    );

    private static final List<String> ENDPOINTS_CAMBIO_PASS = Arrays.asList(
            "peticionCambioPassV2",
            "cambioPasswordV2",
            "validarTokenCambioPass"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ThreadContext.clearAll();

        log.info(">>> Entrando a TraceIdFilter para: {}", request.getRequestURI());

        //validacion de endpoint deshabilitado
        String uri = request.getRequestURI();
        var match = endpointsDisabledProperties.getDeshabilitados().entrySet().stream()
                .filter(e -> uri.contains(e.getKey()))
                .findFirst();

        if (match.isPresent()) {
            log.info("Endpoint {} deshabilitado por configuracion", uri);

            String mensaje = match.get().getValue();

            RespuestaDTO respuesta = new RespuestaDTO();
            respuesta.setCodigo(500);
            respuesta.setMensaje(mensaje);

            enviarErrorJson(response, request, respuesta, defaultIdCanal);

            return;
        }

        String traceIdHeader = request.getHeader(TRACE_ID_HEADER);
        String traceSignatureHeader = request.getHeader(TRACE_SIGNATURE_HEADER);

        String traceId;
        String traceSignature;

        if (traceIdHeader == null || traceIdHeader.isEmpty()) {
            traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "-" + TRACE_SUFFIX;
            traceSignature = firmar(traceId);
            log.warn("TraceId generado por el sistema: {}", traceId);
        } else {
            traceId = traceIdHeader;
            traceSignature = (traceSignatureHeader != null) ? traceSignatureHeader : "";

            if (listSuffixesUUID.getSuffixes().values().stream().anyMatch(traceId::contains)) {
                log.warn("TraceId generado por el sistema: {}", traceId);
            } else if (!canalesPermitidos.getPermitidos().stream()
                    .anyMatch(externo -> traceId.contains(externo))) {
                log.warn("TraceId con sufijo sospechoso: {}", traceId);
            }

            if (traceSignature.isEmpty()) {
                log.warn("Firma vacía para X-Trace-Id: {}", traceId);
            } else if (!estaEnEndpointsExcluidos(request)) {
                if (!traceSignature.equals(firmar(traceId))) {
                    log.warn("Firma inválida para X-Trace-Id: {}", traceId);
                }
            }
        }

        ThreadContext.put("myUuid", traceId);
        ThreadContext.put("myUuidSigned", traceSignature);

        HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if (TRACE_ID_HEADER.equalsIgnoreCase(name)) return traceId;
                if (TRACE_SIGNATURE_HEADER.equalsIgnoreCase(name)) return traceSignature;
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (TRACE_ID_HEADER.equalsIgnoreCase(name))
                    return Collections.enumeration(Collections.singletonList(traceId));
                if (TRACE_SIGNATURE_HEADER.equalsIgnoreCase(name))
                    return Collections.enumeration(Collections.singletonList(traceSignature));
                return super.getHeaders(name);
            }
        };

        filterChain.doFilter(wrappedRequest, response);
    }

    private String firmar(String traceId) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY_UUID.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] firma = hmac.doFinal(traceId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(firma);
        } catch (Exception e) {
            throw new RuntimeException("Error al firmar el TraceId", e);
        }
    }

    private boolean estaEnEndpointsExcluidos(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return ENDPOINTS_EXCLUIDOS_FIRMA.stream().anyMatch(uri::contains);
    }

    private void enviarErrorJson(HttpServletResponse response, HttpServletRequest request, RespuestaDTO respuesta) throws IOException {
        log.error("Error Endpoint deshabilitado: {}", respuesta.getMensaje());

        if (request.getRequestURI().contains("generarAbono"))
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else
            response.setStatus(HttpServletResponse.SC_OK);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        // Usando Jackson para serializar objeto a JSON
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(respuesta);
        json = cifradoUtil.encryptResponse(json);

        response.getWriter().write(json);
    }

    private void enviarErrorJson(HttpServletResponse response, HttpServletRequest request, RespuestaDTO respuesta, Integer idCanal) throws IOException {
        log.error("Error Endpoint deshabilitado: {}", respuesta.getMensaje());

        String uri = request.getRequestURI();

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
            if (request.getRequestURI().contains(pathPwdReset) || ENDPOINTS_CAMBIO_PASS.stream().anyMatch(uri::contains))
                json = cifradoUtil.encryptResponse(json, cambioPassCipherKey);
            else
                json = cifradoUtil.encryptResponse(json);
        }

        response.getWriter().write(json);
    }
}
