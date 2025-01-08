package com.librarymanagementsystem.dto;

import java.time.LocalDate;

public class BorrowingRequestDTO {

    private Long requestId; // The ID of the borrowing request
    private String bookTitle; // The title of the requested book
    private String requestStatus; // The status of the request (e.g., PENDING, ACCEPTED)
    private LocalDate requestedOn; // The date when the request was made

    // Getters and setters

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public LocalDate getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(LocalDate requestedOn) {
        this.requestedOn = requestedOn;
    }
}
