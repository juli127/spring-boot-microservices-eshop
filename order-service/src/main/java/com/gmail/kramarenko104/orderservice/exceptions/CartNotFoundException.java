package com.gmail.kramarenko104.orderservice.exceptions;

public class CartNotFoundException extends RuntimeException {

    private String message;

    public CartNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
