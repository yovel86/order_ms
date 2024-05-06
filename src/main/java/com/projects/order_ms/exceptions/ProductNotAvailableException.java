package com.projects.order_ms.exceptions;

public class ProductNotAvailableException extends Exception {
    public ProductNotAvailableException(String message) {
        super(message);
    }
}
