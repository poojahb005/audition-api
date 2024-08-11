package com.audition.interceptor;


import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInjector implements HandlerInterceptor {

    @Autowired
    private Tracer tracer;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) {
        if (null != tracer.currentSpan() && null != tracer.currentSpan().context()) {
            response.addHeader("x-Trace-Id", tracer.currentSpan().context().traceId());
            response.addHeader("x-Span-Id", tracer.currentSpan().context().spanId());
        }
        return true;
    }
}