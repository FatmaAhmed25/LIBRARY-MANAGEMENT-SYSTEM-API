package com.librarymanagementsystem.model;

public enum BorrowingRequestStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String status;

    BorrowingRequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
