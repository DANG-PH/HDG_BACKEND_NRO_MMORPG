package com.example.NROBACKEND.service;

import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // thêm method này
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}