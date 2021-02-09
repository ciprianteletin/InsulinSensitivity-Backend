package com.insulin.service;

import com.insulin.exception.model.EmailAlreadyExistentException;
import com.insulin.exception.model.UserNotFoundException;
import com.insulin.exception.model.UsernameAlreadyExistentException;
import com.insulin.model.User;
import com.insulin.utils.CompleteUser;

public interface UserService {

    void register(CompleteUser completeUser) throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
