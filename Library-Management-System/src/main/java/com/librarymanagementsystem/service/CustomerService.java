package com.librarymanagementsystem.service;

import com.librarymanagementsystem.exception.NoCustomersFoundException;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllCustomers() {
        List<User> customers = userRepository.findByUserType(User.UserType.ROLE_CUSTOMER);
        if (customers.isEmpty()) {
            throw new NoCustomersFoundException("No customers found.");
        }
        return customers;
    }

}
