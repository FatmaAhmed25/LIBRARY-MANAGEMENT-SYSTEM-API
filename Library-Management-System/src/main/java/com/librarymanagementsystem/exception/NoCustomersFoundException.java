package com.librarymanagementsystem.exception;

public class NoCustomersFoundException extends RuntimeException {
    public NoCustomersFoundException(String message) {
        super(message);
    }
}
