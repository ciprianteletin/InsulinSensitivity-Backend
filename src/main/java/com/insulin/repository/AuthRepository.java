package com.insulin.repository;

import com.insulin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interface that manages the communication with the database.
 * It is using Spring Data JPA, which provides a list of methods already implemented.
 * Also, it provides a mechanism to extract information from database by the name of the method
 * It ensures communication between 'users' table and our application.
 */
public interface AuthRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    @Query(value = "SELECT us.id, us.username, us.password, us.role FROM users as us " +
            "INNER JOIN user_details as ud ON us.id = ud.user_id WHERE ud.email = ?1" , nativeQuery = true)
    User findUserByEmail(String email);
}
