package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.UserDTO;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.register(user);

    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
