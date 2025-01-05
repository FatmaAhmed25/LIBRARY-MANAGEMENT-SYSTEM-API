package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.BorrowingRecordDTO;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.service.BorrowingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getCustomerHistory/{customerId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<BorrowingRecordDTO>> getBorrowedBooksByCustomer(
            @PathVariable Long customerId) {
        List<BorrowingRecordDTO> records = borrowingService.getBorrowedBooks(customerId);
        return ResponseEntity.ok(records);
    }

}
