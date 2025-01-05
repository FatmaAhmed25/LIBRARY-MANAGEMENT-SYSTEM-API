package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.BorrowingRecordDTO;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.librarymanagementsystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingService {

    @Autowired
    BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    public BorrowingRecord borrowBook(Long bookId, Long customerId) {
        // Fetch customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        //Validate that the user is a customer
        if (customer.getUserType() != User.UserType.ROLE_CUSTOMER) {
            throw new IllegalStateException("Only customers can borrow books");
        }
        // Fetch book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        // Validate book availability
        if (book.getQuantity() <= 0) {
            throw new IllegalStateException("Book is not available");
        }

        // Create borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setCustomer(customer);
        borrowingRecord.setBorrowDate(LocalDate.now());

        // Save borrowing record
        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);

        // Update book quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);
        return savedRecord;
    }

    public List<BorrowingRecordDTO> getBorrowedBooks(Long customerId) {
        // Validate customer existence
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Ensure the user is a customer
        if (customer.getUserType() != User.UserType.ROLE_CUSTOMER) {
            throw new IllegalStateException("Only customers have borrowing records");
        }

        // Fetch borrowing records and map them to DTOs
        return borrowingRecordRepository.findByCustomer(customer).stream()
                .map(record -> {
                    BorrowingRecordDTO dto = modelMapper.map(record, BorrowingRecordDTO.class);
                    dto.setCustomerId(record.getCustomer().getId());
                    dto.setCustomerUsername(record.getCustomer().getUsername());
                    dto.setBookId(record.getBook().getId());
                    dto.setBookTitle(record.getBook().getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

