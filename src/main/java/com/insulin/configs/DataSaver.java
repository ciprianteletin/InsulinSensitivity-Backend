package com.insulin.configs;

import com.insulin.model.User;
import com.insulin.model.UserDetail;
import com.insulin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static com.insulin.enumeration.Role.*;

/**
 * Configured a class that at startup insert a default user account which can be used for testing / debug purposes.
 * The user is admin by default, meaning that he has access to every operation available. Useful until
 * option 'create database' is enabled.
 */
@Configuration
public class DataSaver implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public DataSaver(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        User user = buildUser();
        UserDetail userDetail = buildUserDetails();

        user.setBidirectionalDetails(userDetail);

        userRepository.save(user);
    }

    private User buildUser() {
        return User.builder() //
                .username("Cipri22")
                .role(ADMIN)
                .password(encoder.encode("secretPassword"))
                .build();
    }

    private UserDetail buildUserDetails() {
        return UserDetail.builder() //
                .firstName("Cipri") //
                .lastName("Teletin") //
                .email("ciprian_teletin@yahoo.com") //
                .phoneNr("0712345678") //
                .joinDate(LocalDate.now()) //
                .birthDay("22/07/1999") //
                .gender('M') //
                .build();
    }
}
