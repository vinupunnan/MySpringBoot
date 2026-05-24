package com.kailas.mm.exception.handler;

import com.kailas.mm.exception.CustomizedErrorResponse;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.exception.MemberExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class RestExceptionHandler {
//    @ExceptionHandler(ItemNotFoundException.class)
//     public ResponseEntity handleItemNotfoundException(ItemNotFoundException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", "Item Not Found");
//        response.put("invalidItemIds", ex.getItemIds());
//        response.put("message", ex.getMessage());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ProblemDetail handleItemNotfoundException(ItemNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Item Not Found");
        response.put("invalidItemIds", ex.getItemIds());
        response.put("message", ex.getMessage());

        return  ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    }

    @ExceptionHandler(MemberExistsException.class)
    public CustomizedErrorResponse handleMemberExistsException(MemberExistsException ex) {
        CustomizedErrorResponse response = new CustomizedErrorResponse();
        response.setMessage(ex.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomizedErrorResponse inputRequestException(MethodArgumentNotValidException ex) {
        CustomizedErrorResponse response = new CustomizedErrorResponse();
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();
        fieldErrors.stream().forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));
        response.setErrorMap(errorMap);
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return response;
    }


}
