package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.BorrowingRequest;
import com.librarymanagementsystem.model.BorrowingRequestStatus;
import com.librarymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {
    List<BorrowingRequest> findByStatus(BorrowingRequestStatus borrowingRequestStatus);

    List<BorrowingRequest> findByCustomer(User customer);
}
