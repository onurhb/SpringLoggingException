package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    /**
     * This is not a good practice to use sysout. Always integrate any logger
     * with your application. We will discuss about integrating logger with
     * spring boot application in some later article
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("___________________[preHandle]___________________________");
        System.out.println("Incoming request " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) {
        System.out.println("___________________[postHandle]___________________________");
        System.out.println("Handling request to " + request.getMethod() + " " + request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("___________________[afterCompletion]______________________");
        System.out.println("Handled request "  + request.getMethod()  + " " + request.getRequestURI());
        System.out.println("Response to request: " + response.getStatus());
        System.out.println("__________________________________________________________");
    }
}