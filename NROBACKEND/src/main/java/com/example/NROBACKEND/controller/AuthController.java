package com.example.NROBACKEND.controller;

import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public boolean register(@RequestBody User user) {
        // kiểm tra username trùng
        if (userService.existsByUsername(user.getUsername())) {
            return false;
        }
        userService.saveUser(user);
        return true;
    }
}