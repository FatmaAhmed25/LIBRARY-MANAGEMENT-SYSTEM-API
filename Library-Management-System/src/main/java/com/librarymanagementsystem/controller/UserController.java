package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.AuthResponseDTO;
import com.librarymanagementsystem.dto.UserDTO;
import com.librarymanagementsystem.exception.BusinessLogicException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Login a user",
            description = "Authenticate a user with a username and password. Returns a JWT token if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody User user) {
        AuthResponseDTO response = userService.verify(user);
        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Register a new user by providing necessary user details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully")
    })
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/getUser/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get user by ID",
            description = "Fetch the details of a user by their unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name="BearerAuth")
    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request or validation error"),
            @ApiResponse(responseCode = "409", description = "User cannot be deleted due to business logic constraints")
    })
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BusinessLogicException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpected error occurred.");
        }
    }



}
