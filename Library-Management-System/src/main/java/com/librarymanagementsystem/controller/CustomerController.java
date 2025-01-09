package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.exception.NoCustomersFoundException;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Get all customers",
            description = "Retrieve a list of all customers in the system. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No customers found")
    })
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<User> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (NoCustomersFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
