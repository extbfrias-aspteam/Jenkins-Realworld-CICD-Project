package net.cero.filters;

import lombok.extern.log4j.Log4j2;
import net.cero.utilidades.caching.HttpServletResponseCopier;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@Log4j2
@WebFilter(filterName = "printRequestContentFilter", urlPatterns = "/*")
public class PrintRequestContentFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ThreadContext.put("myUuid", UUID.randomUUID().toString());
        log.info("IN  PrintRequestContentFilter ");
        InputStream inputStream = httpServletRequest.getInputStream();
        byte[] body = StreamUtils.copyToByteArray(inputStream);
        log.info("Peticion de la ip {} consultado la url {} con request: {} y stringQuery: {}",httpServletRequest.getRemoteAddr()
                ,httpServletRequest.getRequestURI(),new String(body),httpServletRequest.getQueryString());
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        HttpServletResponseCopier responseCopier = (HttpServletResponseCopier)httpServletResponse;
        responseCopier.flushBuffer();
        byte[] copy = responseCopier.getCopy();
        String responseBody=new String(copy, httpServletResponse.getCharacterEncoding());
        log.info("Codigo de Respuesta de la solicitud: {} y respuesta: {}",httpServletResponse.getStatus(),responseBody);
        ThreadContext.clearMap();
    }
}
