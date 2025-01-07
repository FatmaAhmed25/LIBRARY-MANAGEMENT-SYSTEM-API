package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByCustomer(User customer);

    boolean existsByBookAndReturnDateIsNull(Book book);

    void deleteAllByCustomer(User customer);

    List<BorrowingRecord> findByCustomer(User customer, Sort sort);
}
