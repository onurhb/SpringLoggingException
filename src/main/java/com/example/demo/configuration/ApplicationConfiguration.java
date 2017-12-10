package com.example.demo.configuration;

import com.example.demo.interceptor.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// TODO @Configuration
class ApplicationConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

    private final RequestInterceptor requestInterceptor;

    public ApplicationConfiguration(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }

    // TODO @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            /**
             *  Called first
             */
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                return super.resolveException(request, response, handler, ex);
            }

            /**
             *  Called after, we can modify response here
             *  Caution: This method is not called if you use ControllerAdvice to modify exception message
             */
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorModel = super.getErrorAttributes(requestAttributes, includeStackTrace);

                String logString = String.format(
                        "Detected error/warning: %s, exception: %s, message: %s, request-path: %s",
                        errorModel.get("error"),
                        errorModel.get("exception"),
                        errorModel.get("message"),
                        errorModel.get("path"));

                String statusCode = errorModel.get("status").toString();

                if (statusCode.startsWith("5"))  log.error(logString);
                else if (statusCode.startsWith("4")) log.warn(logString);

                return errorModel;
            }
        };
    }

}
