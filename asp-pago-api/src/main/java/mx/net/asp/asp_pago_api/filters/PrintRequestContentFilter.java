package mx.net.asp.asp_pago_api.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Order(1)
@Component
@Log4j2
@WebFilter(filterName = "printRequestContentFilter", urlPatterns = "/*")
public class PrintRequestContentFilter extends OncePerRequestFilter {

    String[] endpointsExcluidosPrintReq = {
            "registraImagenesCuentaSimplificada",
            "validaFotoIne",
    };

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        // Definir las rutas que Swagger usa
        if (requestURI.startsWith("/asp-pago-api/doc") ||
                requestURI.startsWith("/asp-pago-api/v3/api-docs") ||
                requestURI.startsWith("/asp-pago-api/swagger-ui") ||
                requestURI.startsWith("/asp-pago-api/swagger-resources") ||
                requestURI.startsWith("/asp-pago-api/webjars/") ||
                requestURI.matches(".*/swagger-ui/.*\\.(css|js|png|ico|json)$")) {

            // Si la petición es de Swagger, saltar el filtro
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String contentType = httpServletRequest.getContentType();

        // Si la petición es multipart/form-data, NO intentes leer el cuerpo
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            log.info("Peticion con archivos detectada de la IP {} consultando la URL {}",
                    httpServletRequest.getRemoteAddr(), httpServletRequest.getRequestURI());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Solo leer el cuerpo si NO es multipart/form-data
        HttpServletRequestCopier requestCopier = new HttpServletRequestCopier(httpServletRequest);
        String requestBody = new String(requestCopier.getBody(), requestCopier.getCharacterEncoding());

        if (httpServletRequest.getMethod().equalsIgnoreCase("GET")) {
            // Obtener parámetros de la URL en formato clave=valor
            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
            String params = parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining("&"));
            // Log de la petición con parámetros incluidos
            if (!params.isEmpty()) {
                log.info("Peticion de la IP {} consultando la URL {}?{}",
                        httpServletRequest.getRemoteAddr(), requestURI, params);
            } else {
                log.info("Peticion de la IP {} consultando la URL {}",
                        httpServletRequest.getRemoteAddr(), requestURI);
            }
        } else {
            // Si la URL contiene "registraImagenesCuentaSimplificada", no loguear el requestBody
            if (requestURI != null && Arrays.stream(endpointsExcluidosPrintReq).anyMatch(requestURI::contains)) {
                log.info("Peticion de la IP {} consultando la URL {}",
                        httpServletRequest.getRemoteAddr(), httpServletRequest.getRequestURI());
            } else {
                log.info("Peticion de la IP {} consultando la URL {} con request: {}",
                        httpServletRequest.getRemoteAddr(), httpServletRequest.getRequestURI(), requestBody);
            }
        }

        filterChain.doFilter(requestCopier, httpServletResponse);

        // Si la respuesta es del tipo esperado, copiarla también
        if (httpServletResponse instanceof HttpServletResponseCopier responseCopier) {
            responseCopier.flushBuffer();
            byte[] copy = responseCopier.getCopy();
            String responseBody = new String(copy, httpServletResponse.getCharacterEncoding());
            // Log del cuerpo de la respuesta (si es necesario)
            log.info("Respuesta: {}", responseBody);
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        // Leer el InputStream de la solicitud y convertirlo a String
        InputStream inputStream = request.getInputStream();
        byte[] bytes = StreamUtils.copyToByteArray(inputStream);
        return new String(bytes, request.getCharacterEncoding());
    }
}
