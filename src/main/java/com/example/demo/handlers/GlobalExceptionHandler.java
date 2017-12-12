package com.example.demo.handlers;

import com.example.demo.models.CustomException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomException customException = new CustomException(NOT_FOUND, ex.getMessage(), ex.getLocalizedMessage(), ex);
        ex.printStackTrace();
        return new ResponseEntity<>(customException, customException.getStatus());
    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleSpringExceptions(HttpServletRequest request, Exception ex) {
        CustomException customException = new CustomException(INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getLocalizedMessage(), ex);
        ex.printStackTrace();
        return new ResponseEntity<>(customException, customException.getStatus());

    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomException customException = new CustomException(INTERNAL_SERVER_ERROR, ex.getMessage(),ex.getLocalizedMessage(), ex);
        ex.printStackTrace();
        return new ResponseEntity<>(customException, customException.getStatus());
    }
}