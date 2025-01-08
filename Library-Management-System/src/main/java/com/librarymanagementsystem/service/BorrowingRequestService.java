package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.BorrowingRequestDTO;
import com.librarymanagementsystem.dto.BorrowingRequestResponseDTO;
import com.librarymanagementsystem.exception.BookNotFoundException;
import com.librarymanagementsystem.exception.NoCustomersFoundException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.BorrowingRequest;
import com.librarymanagementsystem.model.BorrowingRequestStatus;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.librarymanagementsystem.repository.BorrowingRequestRepository;
import com.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingRequestService {

    @Autowired
    BorrowingRequestRepository borrowingRequestRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BorrowingRecordRepository borrowingRecordRepository;

    public ResponseEntity<BorrowingRequest> submitRequest(Long customerId, Long bookId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new NoCustomersFoundException("Customer not found with ID: " + customerId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        BorrowingRequest borrowingRequest = new BorrowingRequest();
        borrowingRequest.setCustomer(customer);
        borrowingRequest.setBook(book);
        borrowingRequest.setRequestDate(LocalDate.now());
        borrowingRequest.setStatus(BorrowingRequestStatus.PENDING);

        BorrowingRequest savedRequest = borrowingRequestRepository.save(borrowingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }

    public ResponseEntity<List<BorrowingRequestResponseDTO>> getPendingRequests() {
        List<BorrowingRequest> pendingRequests = borrowingRequestRepository.findByStatus(BorrowingRequestStatus.PENDING);
        if (pendingRequests.isEmpty()) {
            throw new ResourceNotFoundException("No pending requests found.");
        }
        List<BorrowingRequestResponseDTO> response = pendingRequests.stream()
                .map(request -> {
                    BorrowingRequestResponseDTO dto = new BorrowingRequestResponseDTO();
                    dto.setRequestId(request.getId());
                    dto.setRequestStatus(request.getStatus().name());
                    dto.setRequestedOn(request.getRequestDate());
                    dto.setCustomerId(request.getCustomer().getId());
                    dto.setCustomerUsername(request.getCustomer().getUsername());
                    dto.setBookId(request.getBook().getId());
                    dto.setBookTitle(request.getBook().getTitle());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> acceptRequest(Long requestId) {
        BorrowingRequest request = borrowingRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Borrowing Request not found with ID: " + requestId));

        if (request.getStatus().equals(BorrowingRequestStatus.ACCEPTED)) {
            return ResponseEntity.status(400).body("Request already accepted");
        }

        Book book = request.getBook();
        if (book.getQuantity() <= 0) {
            return ResponseEntity.status(400).body("Book not available for borrowing");
        }

        // Update request status to Accepted
        request.setStatus(BorrowingRequestStatus.ACCEPTED);
        borrowingRequestRepository.save(request);

        // Create borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setCustomer(request.getCustomer());
        borrowingRecord.setBorrowDate(LocalDate.now());

        borrowingRecordRepository.save(borrowingRecord);

        // Update book quantity
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        return ResponseEntity.ok("Request accepted and borrowing record created");
    }

    public ResponseEntity<String> rejectRequest(Long requestId) {
        BorrowingRequest request = borrowingRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing Request not found with ID: " + requestId));

        if (request.getStatus().equals(BorrowingRequestStatus.REJECTED)) {
            return ResponseEntity.status(400).body("Request already rejected");
        }

        // Update request status to Rejected
        request.setStatus(BorrowingRequestStatus.REJECTED);
        borrowingRequestRepository.save(request);

        return ResponseEntity.ok("Request rejected");
    }

    public List<BorrowingRequestDTO> getCustomerRequests(Long customerId) {
        // Fetch the customer from the repository
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NoCustomersFoundException("Customer not found"));

        // Fetch all borrowing requests associated with the customer
        List<BorrowingRequest> requests = borrowingRequestRepository.findByCustomer(customer);

        return requests.stream()
                .map(request -> {
                    BorrowingRequestDTO dto = new BorrowingRequestDTO();
                    dto.setRequestId(request.getId());
                    dto.setBookTitle(request.getBook().getTitle());
                    dto.setRequestStatus(request.getStatus().name());
                    dto.setRequestedOn(request.getRequestDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
