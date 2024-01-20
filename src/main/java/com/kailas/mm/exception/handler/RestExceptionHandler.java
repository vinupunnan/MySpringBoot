package com.kailas.mm.exception.handler;

import com.kailas.mm.exception.CustomizedErrorResponse;
import com.kailas.mm.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomizedErrorResponse handleItemNotfoundException(ItemNotFoundException ex) {
        CustomizedErrorResponse response = new CustomizedErrorResponse();
        response.setMessage(ex.getMessage());
        return response;
    }
}
