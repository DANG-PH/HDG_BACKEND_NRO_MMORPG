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
    public ResponseEntity<Map<String, Object>> useVangNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User không tồn tại!"));
        }

        if (found.getVangNapTuWeb() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Không đủ vàng nạp!"));
        }

        // chỉ trừ vàng nạp
        found.setVangNapTuWeb(found.getVangNapTuWeb() - amount);
        userService.saveUser(found);

        return ResponseEntity.ok(Map.of("used", amount));
    }

    @PostMapping("/useNgocNapTuWeb")
    public ResponseEntity<Map<String, Object>> useNgocNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User không tồn tại!"));
        }

        if (found.getNgocNapTuWeb() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Không đủ ngọc nạp!"));
        }

        // chỉ trừ ngọc nạp
        found.setNgocNapTuWeb(found.getNgocNapTuWeb() - amount);
        userService.saveUser(found);

        return ResponseEntity.ok(Map.of("used", amount));
    }
}