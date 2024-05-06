package com.projects.order_ms.exceptions;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
