package com.insulin.utils;

import com.insulin.model.User;
import com.insulin.model.UserDetails;
import com.insulin.utils.abstractions.AbstractUserFactory;

public class UserFactory implements AbstractUserFactory {
    @Override
    public User createUser(CompleteUser completeUser) {
        return User.builder() //
                .username(completeUser.getUsername()) //
                .password(completeUser.getPassword()) //
                .role(completeUser.getRole()) //
                .build();
    }

    @Override
    public UserDetails createUserDetails(CompleteUser completeUser) {
        return UserDetails.builder() //
                .firstName(completeUser.getFirstName()) //
                .lastName(completeUser.getLastName()) //
                .email(completeUser.getEmail()) //
                .phoneNr(completeUser.getPhoneNr()) //
                .build();
    }
}
