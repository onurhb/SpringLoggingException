package com.example.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@Component
public class LogFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logRequest(request);
        filterChain.doFilter(requestWrapper, responseWrapper);
        logResponse(responseWrapper);
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) {
            String body = "None";
            byte[] buf = responseWrapper.getContentAsByteArray();

            if (buf.length > 0) {
                int length = Math.min(buf.length, DEFAULT_MAX_PAYLOAD_LENGTH);
                try {
                    body = new String(buf, 0, length, responseWrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        int responseStatus = responseWrapper.getStatusCode();

        boolean is4xx = String.valueOf(responseStatus).startsWith("4");
        boolean is5xx = String.valueOf(responseStatus).startsWith("5");

        if(is4xx) logger.warn("Response: statusCode: {}, body: {}", responseStatus, body);
        else if (is5xx) logger.error("Response: statusCode: {}, body: {}", responseStatus, body);
    }

    private void logRequest(HttpServletRequest request) {
        String body = "None";
        try {
            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Incoming request to URL: {} payload: {}", request.getRequestURI() ,body);
    }
}