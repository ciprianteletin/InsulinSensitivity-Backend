package com.insulin.repository;

import com.insulin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface that manages the communication with the database.
 * It is using Spring Data JPA, which provides a list of methods already implemented.
 * Also, it provides a mechanism to extract information from database by the name of the method
 * It ensures communication between 'users' table and our application.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}
