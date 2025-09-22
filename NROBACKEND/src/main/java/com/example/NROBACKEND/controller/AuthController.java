package com.example.NROBACKEND.controller;

import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

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
        found.setSucManh(user.getSucManh());

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

    @GetMapping("/balance/{username}")
    public ResponseEntity<?> getBalance(@PathVariable String username) {
        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User không tồn tại!");
        }

        // chỉ trả về vàng/ngọc nạp từ web
        Map<String, Object> balance = new HashMap<>();
        balance.put("vangNapTuWeb", found.getVangNapTuWeb());
        balance.put("ngocNapTuWeb", found.getNgocNapTuWeb());

        return ResponseEntity.ok(balance);
    }

    @PostMapping("/useVangNapTuWeb")
    public ResponseEntity<String> useVangNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User không tồn tại!");
        }

        if (found.getVangNapTuWeb() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không đủ vàng nạp!");
        }

        // trừ vàng nạp và cộng vào vàng thường
        found.setVangNapTuWeb(found.getVangNapTuWeb() - amount);
        found.setVang(found.getVang() + amount);

        userService.saveUser(found);

        return ResponseEntity.ok("Đổi thành công " + amount + " vàng nạp!");
    }

    @PostMapping("/useNgocNapTuWeb")
    public ResponseEntity<String> useNgocNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User không tồn tại!");
        }

        if (found.getNgocNapTuWeb() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không đủ ngọc nạp!");
        }

        // trừ ngọc nạp và cộng vào ngọc thường
        found.setNgocNapTuWeb(found.getNgocNapTuWeb() - amount);
        found.setNgoc(found.getNgoc() + amount);

        userService.saveUser(found);

        return ResponseEntity.ok("Đổi thành công " + amount + " ngọc nạp!");
    }
}