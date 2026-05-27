package com.basscode.employee_leave_management.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final int    MAX_BODY_LENGTH  = 1000;
    private static final String[] SKIP_LOG_PATHS = {
            "/swagger-ui", "/v3/api-docs", "/actuator"
    };

    @Override
    public void doFilter(
            ServletRequest  request,
            ServletResponse response,
            FilterChain     chain
    ) throws IOException, ServletException {

        HttpServletRequest  httpReq = (HttpServletRequest)  request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        if (shouldSkip(httpReq.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);

        long startTime = System.currentTimeMillis();

        CachedBodyRequestWrapper  wrappedReq = new CachedBodyRequestWrapper(httpReq);
        ContentCachingResponseWrapper wrappedRes = new ContentCachingResponseWrapper(httpRes);

        String rawRequestBody = extractBody(wrappedReq.getBodyAsString());

        AppLogger.STREAM.info(
                ">>> REQUEST  [{}] [{}] = {}",
                httpReq.getMethod(),
                httpReq.getRequestURI(),
                extractBody(BodyMaskingUtil.mask(rawRequestBody))
        );

        AppLogger.TRACE.trace(
                ">>> REQUEST  | id={} | method={} | uri={} | ip={} | body={}",
                requestId,
                httpReq.getMethod(),
                httpReq.getRequestURI(),
                httpReq.getRemoteAddr(),
                extractBody(rawRequestBody)
        );

        AppLogger.TRACE.trace(
                ">>> HEADERS  | id={} | Authorization={} | ContentType={}",
                requestId,
                httpReq.getHeader("Authorization") != null ? "[PRESENT]" : "[ABSENT]",
                httpReq.getContentType()
        );

        try {
            chain.doFilter(wrappedReq, wrappedRes);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            String rawResponseBody = extractBody(
                    new String(wrappedRes.getContentAsByteArray())
            );

            AppLogger.STREAM.info(
                    "<<< RESPONSE ({}) [{}] | [{}] = {}",
                    wrappedRes.getStatus(),
                    httpReq.getMethod(),
                    httpReq.getRequestURI(),
                    extractBody(BodyMaskingUtil.mask(rawResponseBody))
            );

            AppLogger.TRACE.trace(
                    "<<< RESPONSE | id={} | status={} | duration={}ms | body={}",
                    requestId,
                    wrappedRes.getStatus(),
                    duration,
                    extractBody(rawResponseBody)
            );


            if (duration > 2000) {
                AppLogger.WARNING.warn(
                        "SLOW REQUEST | id={} | uri={} | duration={}ms",
                        requestId,
                        httpReq.getRequestURI(),
                        duration
                );
            }

            wrappedRes.copyBodyToResponse();
            MDC.clear();
        }
    }

    private String extractBody(String raw) {
        if (raw == null || raw.isBlank()) return " - ";
        return raw.replaceAll("\\s+", " ").trim();
    }

    private boolean shouldSkip(String uri) {
        for (String path : SKIP_LOG_PATHS) {
            if (uri.startsWith(path)) return true;
        }
        return false;
    }
}