package com.insulin.service;

import com.insulin.exception.model.UserNotFoundException;
import com.insulin.model.User;

public interface UserService {

    User findUserByUsername(String username) throws UserNotFoundException;

    User findUserByEmail(String email);
}
