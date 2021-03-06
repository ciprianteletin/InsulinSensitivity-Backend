package com.insulin.service.abstraction;

import com.insulin.exceptions.model.*;
import com.insulin.model.User;
import com.insulin.utils.model.BasicUserInfo;
import com.insulin.utils.model.UserPasswordInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * Service used for specific user operations like updateProfileImage, deleteAccount etc.
 * A new service was created in order to be more specific about auth operation and user operation.
 * "A class must have one and only one role inside an application"
 */
public interface UserService {
    void deleteUser(User user, String principal, HttpServletRequest request);

    User getUserById(Long id) throws UserNotFoundException;

    User getUserByUsername(String username) throws DataFormatException, IOException;

    String getUserIpAddress(String username) throws UserNotFoundException;

    User updateUser(Long id, BasicUserInfo basicUserInfo)
            throws UserNotFoundException, EmailAlreadyExistentException, UsernameAlreadyExistentException, PhoneNumberUniqueException;

    User updateProfileImage(Long id, MultipartFile file) throws UserNotFoundException, IOException;

    void updatePassword(User user, String principal, UserPasswordInfo userPasswordInfo) throws OldPasswordException;
}
