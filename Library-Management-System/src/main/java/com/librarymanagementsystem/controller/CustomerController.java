package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<User>> getAllCustomers() {
        List<User> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
