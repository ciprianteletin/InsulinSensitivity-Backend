package com.insulin.service.implementation;

import com.insulin.model.User;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> currentUser = this.userRepository.findById(id);
        return currentUser.orElse(null);
    }
}
