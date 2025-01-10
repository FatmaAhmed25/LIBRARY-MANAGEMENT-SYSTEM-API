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
   git clone https://github.com/FatmaAhmed25/LIBRARY-MANAGEMENT-SYSTEM-API.git
   cd Library-Management-System
   ```
2. **Set up MySQL Database**:
  Create a new database in MySQL:
   ```bash
   CREATE DATABASE library;
   ```
   Update the src/main/resources/application.properties file with your MySQL database credentials:
   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/library_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
3. **Build the Project: If you're using Maven to manage dependencies:**:
   ```bash
   mvn clean install
   ```
4. **Run the Application: To start the backend server**:
   ```bash
   mvn spring-boot:run
   ```
5. **The backend will now be running on http://localhost:8080**.



   
   
   
