package com.example.demo.handlers;

import com.example.demo.models.CustomException;
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

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the CustomException object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, 
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messageEn = ex.getParameterName() + " parameter is missing";
        String messageNo = ex.getParameterName() + " parameter is missing";

        CustomException customException = new CustomException(BAD_REQUEST, messageEn, messageNo, ex);
        
        return new ResponseEntity<>(customException, customException.getStatus());
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the CustomException object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        StringBuilder builderEn = new StringBuilder();
        StringBuilder builderNo = new StringBuilder();

        builderEn.append(ex.getContentType());
        builderNo.append(ex.getContentType());

        builderEn.append(" media type is not supported. Supported media types are ");
        builderNo.append(" media er ikke støttet. Støttet media typer er ");

        ex.getSupportedMediaTypes().forEach(t -> builderEn.append(t).append(", "));

        String messageEn = builderEn.substring(0, builderEn.length() - 2);
        String messageNo = builderNo.substring(0, builderEn.length() - 2);
        
        CustomException customException = new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, messageEn, messageNo, ex);

        return new ResponseEntity<>(customException, customException.getStatus());
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the CustomException object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        CustomException customException = new CustomException(HttpStatus.BAD_REQUEST, "Malformed JSON request", "Misformet JSON forespørsel", ex);

        // log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());

        return new ResponseEntity<>(customException, customException.getStatus());
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the CustomException object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomException customException = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing JSON output", "Kunne ikke skrive ut JSON", ex);
        return new ResponseEntity<>(customException, customException.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        CustomException CustomException = new CustomException(NOT_FOUND);
        CustomException.setMessage("Resource was not found", "Fant ikke ønsket ressurs");
        CustomException.setExceptionMessage(ex.getMessage());

        log.warn("{} to {} - No controlller was found", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());

        return new ResponseEntity<>(CustomException, CustomException.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(HttpServletRequest request, Exception ex) {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        CustomException CustomException = new CustomException(INTERNAL_SERVER_ERROR);
        CustomException.setMessage("Unexpected exception", "En feil oppstod");
        CustomException.setExceptionMessage(ex.getMessage());

        log.error("{} to {} - {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), ex.getStackTrace());

        return new ResponseEntity<>(CustomException, CustomException.getStatus());

    }

}