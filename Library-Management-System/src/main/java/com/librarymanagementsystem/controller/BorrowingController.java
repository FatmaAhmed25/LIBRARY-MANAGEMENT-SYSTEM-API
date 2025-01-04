package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.service.BorrowingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/{bookId}/{customerId}")
    @SecurityRequirement(name="BearerAuth")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long customerId,
            @PathVariable Long bookId
    ) {
        BorrowingRecord record = borrowingService.borrowBook(bookId, customerId);
        return ResponseEntity.ok(record);
    }
}
