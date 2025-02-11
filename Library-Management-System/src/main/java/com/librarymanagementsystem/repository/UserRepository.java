package com.librarymanagementsystem.repository;

import com.librarymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByUserType(User.UserType userType);

    boolean existsByUsername(String username);
}
