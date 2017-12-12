package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.stream.Collectors;

// @Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    class RequestLog {

        public String requestMethod;
        public String requestUri;
        public String requestPayload;
        public String handlerName;
        public String requestParams;

        RequestLog(String requestMethod, String requestUri, String requestPayload, String handlerName, Enumeration<String> requestParams) {
            this.requestMethod = requestMethod;
            this.requestUri = requestUri;
            this.requestPayload = requestPayload;
            this.handlerName = handlerName;

            StringBuilder stringBuilder = new StringBuilder();

            while (requestParams.hasMoreElements()) {
                stringBuilder
                        .append(";")
                        .append(requestParams.nextElement());
            }

            this.requestParams = stringBuilder.toString();
        }
    }

    class ResponseLog {
        public int responseStatus;
        public String responsePayload;

        public ResponseLog(int responseStatus, String responsePayload) {
            this.responseStatus = responseStatus;
            this.responsePayload = responsePayload;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

        String requestPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Enumeration<String> requestParams = request.getParameterNames();
        String requestMethod = request.getMethod();
        String handlerName = handler.toString();

        RequestLog requestLog = new RequestLog(requestMethod, requestUri, requestPayload, handlerName, requestParams);
        String serialized = new ObjectMapper().writeValueAsString(requestLog);

        log.info("Incoming request:" + serialized);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        int responseStatus = response.getStatus();

        boolean is4xx = String.valueOf(responseStatus).startsWith("4");
        boolean is5xx = String.valueOf(responseStatus).startsWith("5");

        // @TODO: Do we want to log all types of response?
        if (is4xx || is5xx || ex != null) {
            String responseBody = getResponseBody(response);
            ResponseLog responseLog = new ResponseLog(responseStatus, responseBody);

            String serialized = new ObjectMapper().writeValueAsString(responseLog);
            log.warn("Response to last request:" + serialized);
        }
    }

    private String getResponseBody(HttpServletResponse response) {
        String responsePayload = "";
        ContentCachingResponseWrapper wrappedRequest = new ContentCachingResponseWrapper(response);

        byte[] responseBuffer = wrappedRequest.getContentAsByteArray();

        if (responseBuffer.length > 0) {
            try {
                responsePayload = new String(responseBuffer, 0, responseBuffer.length, wrappedRequest.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                responsePayload = "[unknown]";
            }
        }

        return responsePayload;
    }
}