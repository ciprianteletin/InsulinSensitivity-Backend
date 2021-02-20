package com.insulin.service.implementation;

import com.insulin.exception.model.UserNotFoundException;
import com.insulin.model.User;
import com.insulin.repository.UserRepository;
import com.insulin.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> currentUser = this.userRepository.findById(id);
        return currentUser.orElseThrow(() -> new UserNotFoundException("User not found for the provided id"));
    }
}
