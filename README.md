# Library Management System (Backend)

## Overview
The Library Management System (LMS) is a backend application built with Java and Spring Boot, designed to manage books, users, and borrowing transactions for a library. This system provides API endpoints to manage book records, user registration, book borrowing and returning, and administrative tasks.

## Features
- **Book Management**: Add, update, and delete books in the library.
- **User Management**: Register users, view user profiles, and manage account details.
- **Borrowing System**: Allows users to borrow and return books, with due date tracking.
- **Admin Management**: Admin users can manage books and users, track borrowing activities.
- **Database Integration**: MySQL database for storing books, users, and transactions.
- **Authentication**: Secure login using JWT (JSON Web Tokens) for users and admins.

## Technologies Used
- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens)
- **ORM**: Hibernate for database interaction
- **Dependency Management**: Maven

## Installation

### Prerequisites
- JDK 11 or later
- MySQL 8.0 or later

### Steps to Set Up Locally
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/library-management-system-backend.git
   cd library-management-system-backend
   ```
