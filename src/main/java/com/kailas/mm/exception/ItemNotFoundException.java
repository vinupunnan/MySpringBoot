package com.kailas.mm.exception;

public class ItemNotFoundException extends RuntimeException {


    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ItemNotFoundException(String message){
        this.message = message;
    }


}
