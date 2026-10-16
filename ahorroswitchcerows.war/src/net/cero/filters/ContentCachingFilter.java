package net.cero.filters;

import lombok.extern.log4j.Log4j2;
import net.cero.utilidades.caching.CachedBodyHttpServletRequest;
import net.cero.utilidades.caching.HttpServletResponseCopier;
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

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@Log4j2
@WebFilter(urlPatterns = "/*")
public class ContentCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("IN  ContentCachingFilter ");
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(httpServletResponse);
        filterChain.doFilter(cachedBodyHttpServletRequest, responseCopier);
    }
}
