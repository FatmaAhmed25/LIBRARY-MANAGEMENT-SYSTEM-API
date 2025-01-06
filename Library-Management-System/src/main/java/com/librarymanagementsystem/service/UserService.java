package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.UserDTO;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.librarymanagementsystem.repository.UserRepository;
import com.librarymanagementsystem.security.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User register(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    //taking control of login
    public String verify(User user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "FAIL";
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    public void deleteUserById(Long id) {
        // Fetch the user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the user is a customer
        if (user.getUserType() == User.UserType.ROLE_CUSTOMER) {
            // Check for active borrowing records
            List<BorrowingRecord> activeBorrowings = borrowingRecordRepository.findByCustomerAndReturnDateIsNull(user);
            if (!activeBorrowings.isEmpty()) {
                throw new IllegalStateException("Customer cannot be deleted as they have unreturned books.");
            }

            // Delete associated borrowing records (if needed)
            borrowingRecordRepository.deleteAllByCustomer(user);
        }

        // Delete the user
        userRepository.delete(user);
    }


}
