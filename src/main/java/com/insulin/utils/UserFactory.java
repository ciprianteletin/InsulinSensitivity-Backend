package com.insulin.utils;

import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.utils.abstractions.AbstractUserFactory;
import com.insulin.utils.model.CompleteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserFactory implements AbstractUserFactory {
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserFactory(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public User createUser(CompleteUser completeUser) {
        return User.builder() //
                .username(completeUser.getUsername()) //
                .password(encoder.encode(completeUser.getPassword())) //
                .role(completeUser.getRole().toUpperCase()) //
                .build();
    }

    @Override
    public UserDetail createUserDetails(CompleteUser completeUser) {
        return UserDetail.builder() //
                .firstName(completeUser.getFirstName()) //
                .lastName(completeUser.getLastName()) //
                .email(completeUser.getEmail()) //
                .phoneNr(completeUser.getPhoneNr()) //
                .joinDate(new Date(System.currentTimeMillis())) //
                .build();
    }
}
