package com.kailas.mm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomizedErrorResponse {
    public CustomizedErrorResponse() {
    }

    private String message;

    private Map<String, String> errorMap;



    private HttpStatusCode statusCode;


    public CustomizedErrorResponse(String message, Map<String, String> errorMap, HttpStatusCode statusCode) {
        this.message = message;
        this.errorMap = errorMap;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
