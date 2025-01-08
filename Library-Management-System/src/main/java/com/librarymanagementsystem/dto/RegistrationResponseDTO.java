package com.librarymanagementsystem.dto;

import com.librarymanagementsystem.model.User;

public class RegistrationResponseDTO {
    private String message;
    private User user;

    public RegistrationResponseDTO(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
