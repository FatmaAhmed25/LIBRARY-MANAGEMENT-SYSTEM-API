package com.librarymanagementsystem.exception;

public class BorrowingRecordNotFoundException extends RuntimeException {
    public BorrowingRecordNotFoundException(String message) {
        super(message);
    }
}
