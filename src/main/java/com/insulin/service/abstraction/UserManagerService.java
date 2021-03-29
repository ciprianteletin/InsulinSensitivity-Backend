package com.insulin.service.abstraction;

import com.insulin.model.User;

public interface UserManagerService {
    User findUserByUsernameOrEmail(String text);
}
