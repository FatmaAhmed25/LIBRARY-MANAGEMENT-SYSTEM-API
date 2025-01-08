package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(
            summary = "Add a new book",
            description = "Add a new book to the library database by providing book details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid book details provided")
    })
    @PostMapping("add")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @Operation(
            summary = "Remove a book",
            description = "Delete a book from the library database using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Object> removeBook(@PathVariable Long id) {
        return bookService.removeBook(id);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Retrieve a book from the library database using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/get/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Search books by title",
            description = "Search for books in the library database using a partial or full title."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No books found with the given title")
    })
    @GetMapping("/searchByTitle")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Object> getBookByTitle(@RequestParam String title) {
        return bookService.getBookByTitle(title);
    }

    @Operation(
            summary = "Get all books",
            description = "Retrieve a list of all books available in the library database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No books available")

    })
    @GetMapping("getAllBooks")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Object> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(
            summary = "Update a book's details",
            description = "Update the details of a specific book in the library database using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid book details provided")
    })
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book updatedBookRecord = bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok(updatedBookRecord);
    }
}
