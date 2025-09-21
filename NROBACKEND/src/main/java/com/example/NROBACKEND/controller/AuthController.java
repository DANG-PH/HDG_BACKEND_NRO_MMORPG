package com.example.NROBACKEND.controller;

import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User found = userService.findByUsername(user.getUsername());
        if (found == null || !found.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(found); // login ok
    }

    @PostMapping("/saveGame")
    public ResponseEntity<String> saveGame(@RequestBody User user) {
        User found = userService.findByUsername(user.getUsername());
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User không tồn tại!");
        }

        // cập nhật dữ liệu từ client gửi lên
        found.setVang(user.getVang());
        found.setNgoc(user.getNgoc());

        userService.saveUser(found);

        return ResponseEntity.ok("Lưu dữ liệu game thành công!");
    }

    @GetMapping("/loadGame/{username}")
    public ResponseEntity<User> loadGame(@PathVariable String username) {
        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(found);
    }
}