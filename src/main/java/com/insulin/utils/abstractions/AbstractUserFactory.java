package com.insulin.utils.abstractions;

import com.insulin.model.User;
import com.insulin.model.UserDetails;
import com.insulin.utils.CompleteUser;

/**
 * Using Abstract Factory Method for creating User & UserDetails objects from CompleteUser class.
 */
public interface AbstractUserFactory {
    User createUser(CompleteUser completeUser);

    UserDetails createUserDetails(CompleteUser completeUser);
}
