package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.AuthResponseDTO;
import com.librarymanagementsystem.dto.RegistrationResponseDTO;
import com.librarymanagementsystem.dto.UserDTO;
import com.librarymanagementsystem.exception.BusinessLogicException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.librarymanagementsystem.repository.UserRepository;
import com.librarymanagementsystem.security.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BorrowingRecordRepository borrowingRecordRepository;


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<Object> register(User user) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: Username already exists.");
            }

            // Encode the password
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            // Save the user to the database
            userRepository.save(user);

            // Return success message with the user object
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new RegistrationResponseDTO("Registration successful.", user)
            );
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }



    //taking control of login
    public AuthResponseDTO verify(User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());
                return new AuthResponseDTO("Login successful", token);
            } else {
                return new AuthResponseDTO("Authentication failed", null);
            }
        } catch (Exception e) {
            return new AuthResponseDTO("Authentication failed: " + e.getMessage(), null);
        }
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
        return modelMapper.map(user, UserDTO.class);
    }

    public void deleteUserById(Long id) {
        // Fetch the user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));

        // Check if the user is a customer
        if (user.getUserType() == User.UserType.ROLE_CUSTOMER) {
            // Check for active borrowing records
            List<BorrowingRecord> activeBorrowings = borrowingRecordRepository.findByCustomerAndReturnDateIsNull(user);
            if (!activeBorrowings.isEmpty()) {
                throw new BusinessLogicException("Customer cannot be deleted as they have unreturned books.");
            }

            // Delete associated borrowing records (if needed)
            borrowingRecordRepository.deleteAllByCustomer(user);
        }

        // Delete the user
        userRepository.delete(user);
    }


}
