package com.librarymanagementsystem.service;

import com.librarymanagementsystem.dto.BookResponseDTO;
import com.librarymanagementsystem.exception.BusinessLogicException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public ResponseEntity<Object> removeBook(Long id) {
        try {
            // Check if the book exists
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found."));

            // Check for active borrowing records
            boolean hasActiveRecords = borrowingRecordRepository.existsByBookAndReturnDateIsNull(book);
            if (hasActiveRecords) {
                throw new BusinessLogicException("Cannot delete book with active borrowing records.");
            }

            // Delete the book
            bookRepository.delete(book);

            // Return success response
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BookResponseDTO("Book deleted successfully.", null));
        } catch (ResourceNotFoundException | BusinessLogicException e) {
            // Handle business and resource-related exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BookResponseDTO(e.getMessage(), null));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BookResponseDTO("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getBookById(Long id) {
        try {
            // Find the book by ID
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found."));

            // Return success response
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BookResponseDTO("Book found.", book));
        } catch (ResourceNotFoundException e) {
            // Handle resource not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BookResponseDTO(e.getMessage(), null));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BookResponseDTO("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getBookByTitle(String title) {
        try {
            // Find books by title
            List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);

            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BookResponseDTO("No books found with the title: " + title, null));
            }

            // Return success response
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BookResponseDTO("Books retrieved successfully.", books));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BookResponseDTO("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getAllBooks() {
        try {
            // Fetch all books
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BookResponseDTO("No books available.", null));
            }

            // Return success response
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BookResponseDTO("Books retrieved successfully.", books));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BookResponseDTO("An unexpected error occurred: " + e.getMessage(), null));
        }
    }


    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setQuantity(updatedBook.getQuantity());

        return bookRepository.save(existingBook);
    }









}
