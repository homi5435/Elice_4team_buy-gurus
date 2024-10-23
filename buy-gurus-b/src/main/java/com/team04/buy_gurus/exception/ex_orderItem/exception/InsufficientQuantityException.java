package com.team04.buy_gurus.exception.ex_orderItem.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}