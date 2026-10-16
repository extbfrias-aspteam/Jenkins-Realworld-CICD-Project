package net.cero.filters;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@Log4j2
@WebFilter(urlPatterns = "/*")
public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ThreadContext.put("myUuid", UUID.randomUUID().toString());
        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if(StringUtils.isBlank(ip))
            ip = httpServletRequest.getRemoteAddr();
        log.info("Peticion de la ip {} consultado la url {}",ip
                ,httpServletRequest.getRequestURI());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        ThreadContext.clearMap();
    }
}
