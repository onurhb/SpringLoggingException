package com.example.demo.handlers;

import com.example.demo.models.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomException customException = new CustomException(NOT_FOUND, "Endpoint not found", "Fant ikke endepunkt", ex);

        try {
            String serialized = new ObjectMapper().writeValueAsString(customException);
            log.warn("Response: " + serialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(customException, customException.getStatus());
    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleSpringExceptions(HttpServletRequest request, Exception ex) {
        CustomException customException = new CustomException(INTERNAL_SERVER_ERROR, "Unexpected exception", "En feil oppstod", ex);

        try {
            String serialized = new ObjectMapper().writeValueAsString(customException);
            log.error("Response: " + serialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(customException, customException.getStatus());

    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageEn = ex.getMessage();
        String messageNo = ex.getLocalizedMessage();

        CustomException customException = new CustomException(BAD_REQUEST, messageEn, messageNo, ex);

        try {
            String serialized = new ObjectMapper().writeValueAsString(customException);
            if (status.is4xxClientError()) log.warn("Response: " + serialized);
            if (status.is5xxServerError()) log.error("Response: " + serialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(customException, customException.getStatus());
    }
}