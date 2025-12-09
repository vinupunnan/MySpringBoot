package com.kailas.mm.filter;


import com.kailas.mm.service.impl.DcRateLimiterService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DcHeaderRateLimitFilter implements Filter {

    private static final String DC_HEADER = "X-DC-ID";

    private final DcRateLimiterService rateLimiterService;

    public DcHeaderRateLimitFilter(DcRateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        String path = httpReq.getRequestURI();
        String method = httpReq.getMethod();

        // Apply rate-limiter only for POST /alerts
        if (!"POST".equalsIgnoreCase(method) || !path.startsWith("/alerts")) {
            chain.doFilter(request, response);
            return;
        }

        String dcId = httpReq.getHeader(DC_HEADER);

        if (dcId == null || dcId.isBlank()) {
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResp.setContentType("text/plain");
            httpResp.getWriter().write("Missing required header: " + DC_HEADER);
            return;
        }

        Bucket bucket = rateLimiterService.getBucketFor(dcId);

        if (bucket.tryConsume(1)) {
            // allowed
            chain.doFilter(request, response);
        } else {
            // too many requests
            httpResp.setStatus(429);
            httpResp.setContentType("text/plain");
            httpResp.getWriter().write("Rate limit exceeded for DC: " + dcId);
        }
    }
}