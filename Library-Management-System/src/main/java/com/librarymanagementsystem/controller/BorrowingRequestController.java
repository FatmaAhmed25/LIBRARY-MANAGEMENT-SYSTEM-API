package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.BorrowingRequestDTO;
import com.librarymanagementsystem.dto.BorrowingRequestResponseDTO;
import com.librarymanagementsystem.model.BorrowingRequest;
import com.librarymanagementsystem.service.BorrowingRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowingRequests")
public class BorrowingRequestController {

    @Autowired
    private BorrowingRequestService borrowingRequestService;

    @Operation(
            summary = "Submit a borrowing request for a book",
            description = "Allows customers to request borrowing a book."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "201", description = "Borrowing request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Customer or Book not found")
    })
    @PostMapping("/newRequest")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<BorrowingRequest> submitRequest(@RequestParam Long customerId, @RequestParam Long bookId) {
        return borrowingRequestService.submitRequest(customerId, bookId);
    }

    @Operation(
            summary = "Get all pending borrowing requests",
            description = "Fetches all pending borrowing requests for the admin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "List of pending requests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No pending requests found")
    })
    @GetMapping("/getPendingRequests")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<BorrowingRequestResponseDTO>> getPendingRequests() {
        return borrowingRequestService.getPendingRequests();
    }

    @Operation(
            summary = "Accept a borrowing request",
            description = "Admin can accept a borrowing request and create a borrowing record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Request accepted and borrowing record created"),
            @ApiResponse(responseCode = "404", description = "Request not found"),
            @ApiResponse(responseCode = "400", description = "Request already accepted or book is not available")
    })
    @PostMapping("/accept/{requestId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
        return borrowingRequestService.acceptRequest(requestId);
    }

    @Operation(
            summary = "Reject a borrowing request",
            description = "Admin can reject a borrowing request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Request rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found"),
            @ApiResponse(responseCode = "400", description = "Request already rejected")
    })
    @PostMapping("/reject/{requestId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requestId) {
        return borrowingRequestService.rejectRequest(requestId);
    }

    @Operation(
            summary = "Get all borrowing requests for a customer",
            description = "Fetches all borrowing requests for a specific customer, including their status and details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Successfully fetched the customer requests"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/getRequests/{customerId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<BorrowingRequestDTO>> getCustomerRequests(@PathVariable Long customerId) {
        List<BorrowingRequestDTO> customerRequests = borrowingRequestService.getCustomerRequests(customerId);
        return ResponseEntity.ok(customerRequests);
    }
}
