package com.insulin.service.abstraction;

import com.insulin.exception.model.UserNotFoundException;
import com.insulin.model.User;

/**
 * Service used for specific user operations like updateProfileImage, deleteAccount etc.
 * A new service was created in order to be more specific about auth operation and user operation.
 * "A class must have one and only one role inside an application"
 */
public interface UserService {
    void deleteUser(Long id);

    User getUserById(Long id) throws UserNotFoundException;
}
