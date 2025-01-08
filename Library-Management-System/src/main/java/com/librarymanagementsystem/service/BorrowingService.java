package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.BorrowingRecordDTO;
import com.librarymanagementsystem.exception.*;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.librarymanagementsystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public BorrowingRecordDTO borrowBook(Long bookId, Long customerId) {
        // Fetch customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NoCustomersFoundException("Customer with ID " + customerId + " not found"));

        // Validate that the user is a customer
        if (customer.getUserType() != User.UserType.ROLE_CUSTOMER) {
            throw new UnauthorizedActionException("Only customers can borrow books");
        }

        // Fetch book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));

        // Validate book availability
        if (book.getQuantity() <= 0) {
            throw new BookUnavailableException("Book with ID " + bookId + " is not available");
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

        // Map saved record to DTO
        BorrowingRecordDTO borrowingRecordDTO = modelMapper.map(savedRecord, BorrowingRecordDTO.class);
        borrowingRecordDTO.setCustomerId(savedRecord.getCustomer().getId());
        borrowingRecordDTO.setCustomerUsername(savedRecord.getCustomer().getUsername());
        borrowingRecordDTO.setBookId(savedRecord.getBook().getId());
        borrowingRecordDTO.setBookTitle(savedRecord.getBook().getTitle());

        return borrowingRecordDTO;
    }

    public List<BorrowingRecordDTO> getBorrowedBooks(Long customerId) {
        // Validate customer existence
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NoCustomersFoundException("Customer with ID " + customerId + " not found"));

        // Ensure the user is a customer
        if (customer.getUserType() != User.UserType.ROLE_CUSTOMER) {
            throw new UnauthorizedActionException("Only customers have borrowing records");
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

    public BorrowingRecord returnBook(Long borrowRecordId) {
        // Fetch the borrowing record
        BorrowingRecord record = borrowingRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new BorrowingRecordNotFoundException("Borrowing record with ID " + borrowRecordId + " not found"));

        // Check if the book has already been returned
        if (record.getReturnDate() != null) {
            throw new IllegalStateException("Book with ID " + borrowRecordId + " has already been returned");
        }

        // Mark the return date
        record.setReturnDate(LocalDate.now());

        // Fetch the associated book and increase its quantity
        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);

        // Save the updated borrowing record and book
        borrowingRecordRepository.save(record);
        bookRepository.save(book);

        return record;
    }

    public List<BorrowingRecordDTO> fetchSortedBorrowingHistory(Long customerId, String sortBy, String sortDirection) {
        // Validate customer existence
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NoCustomersFoundException("Customer with ID " + customerId + " not found"));

        // Ensure the user is a customer
        if (customer.getUserType() != User.UserType.ROLE_CUSTOMER) {
            throw new UnauthorizedActionException("Only customers have borrowing records");
        }

        // Define sorting direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Fetch borrowing records with sorting
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findByCustomer(customer, Sort.by(direction, sortBy));

        // Map borrowing records to DTOs
        return borrowingRecords.stream()
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


