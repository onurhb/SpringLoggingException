package com.example.demo.models;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomException {

    private String timestamp;
    private HttpStatus status;
    private String exceptionMessage;
    private String exceptionType;
    private String messageEn;
    private String messageNo;

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
        this.messageEn = "Unexpected exception";
        this.messageNo = "Noe gikk galt";
    }

    public CustomException(HttpStatus status, String messageEn, String messageNo, Throwable ex) {
        this();
        this.status = status;
        this.exceptionMessage = ex.getMessage();
        this.exceptionType = ex.getClass().getTypeName();
        this.messageEn = messageEn;
        this.messageNo = messageNo;
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

    public void setMessage(String messageEn, String messageNo) {
        this.messageEn = messageEn;
        this.messageNo = messageNo;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public void setMessageEn(String messageEn) {
        this.messageEn = messageEn;
    }

    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }
}

