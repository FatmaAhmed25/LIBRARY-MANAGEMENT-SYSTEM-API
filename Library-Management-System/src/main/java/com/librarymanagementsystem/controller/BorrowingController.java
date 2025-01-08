package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.BorrowingRecordDTO;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowing")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/{customerId}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Borrow a book",
            description = "Allows a customer to borrow a book if it is available and the user is a customer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, customer or book not found or other validation error")
    })
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable Long customerId) {
        try {
            BorrowingRecordDTO borrowingRecordDTO = borrowingService.borrowBook(bookId, customerId);
            return ResponseEntity.status(201).body(borrowingRecordDTO);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/borrowed/{customerId}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get all borrowed books for a customer",
            description = "Fetches all the borrowed books for a specified customer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Successfully fetched borrowed books"),
            @ApiResponse(responseCode = "400", description = "Bad request, customer not found or other validation error")
    })
    public ResponseEntity<?> getBorrowedBooks(@PathVariable Long customerId) {
        try {
            List<BorrowingRecordDTO> borrowedBooks = borrowingService.getBorrowedBooks(customerId);
            return ResponseEntity.status(200).body(borrowedBooks);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/return/{borrowRecordId}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Return a borrowed book",
            description = "Allows a customer to return a borrowed book and updates the book quantity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, borrowing record not found or book already returned")
    })
    public ResponseEntity<?> returnBook(@PathVariable Long borrowRecordId) {
        try {
            BorrowingRecord borrowingRecord = borrowingService.returnBook(borrowRecordId);
            return ResponseEntity.status(200).body(borrowingRecord);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/history/{customerId}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Fetch sorted borrowing history for a customer",
            description = "Fetches a customer's borrowing history, sorted by a specified field and direction."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Successfully fetched sorted borrowing history"),
            @ApiResponse(responseCode = "400", description = "Bad request, customer not found or other validation error")
    })
    public ResponseEntity<?> fetchSortedBorrowingHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            List<BorrowingRecordDTO> borrowingHistory = borrowingService.fetchSortedBorrowingHistory(customerId, sortBy, sortDirection);
            return ResponseEntity.status(200).body(borrowingHistory);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}