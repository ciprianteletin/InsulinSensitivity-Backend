package com.insulin.service.abstraction;

import com.insulin.exceptions.model.UserNotFoundException;
import com.insulin.model.User;

import java.util.Optional;

public interface UserManagerService {
    User findUserByUsernameOrEmail(String text);

    User findUserById(Long id) throws UserNotFoundException;

    Optional<User> findUserByUsername(String username);

    User findUserByEmail(String email);
}
