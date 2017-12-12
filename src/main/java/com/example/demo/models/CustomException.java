package com.example.demo.models;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomException{

    private String timestamp;
    private HttpStatus status;
    private String exceptionMessage;
    private String exceptionType;

    private CustomException() {
        timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }

    public CustomException(HttpStatus status) {
        this();
        this.status = status;
    }

    public CustomException(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.exceptionMessage = ex.getMessage();
        this.exceptionType = ex.getClass().getTypeName();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}

