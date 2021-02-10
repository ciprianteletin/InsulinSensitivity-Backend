package com.insulin.utils.abstractions;

import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.utils.model.CompleteUser;

/**
 * Using Abstract Factory Method for creating User & UserDetails objects from CompleteUser class.
 */
public interface AbstractUserFactory {
    User createUser(CompleteUser completeUser);

    UserDetail createUserDetails(CompleteUser completeUser);
}
