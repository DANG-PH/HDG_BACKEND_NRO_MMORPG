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
        if (!found.isDaVaoTaiKhoanLanDau()) {
            found.setDaVaoTaiKhoanLanDau(true);
            userService.saveUser(found);
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
        found.setX(user.getX());
        found.setY(user.getY());
        found.setMapHienTai(user.getMapHienTai());

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

    // API mới để cập nhật balance khi nạp tiền
    @PostMapping("/updateBalance")
    public ResponseEntity<Map<String, Object>> updateBalance(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String type = (String) request.get("type");
        long amount = (long) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User không tồn tại!"));
        }

        try {
            if ("vangNapTuWeb".equals(type)) {
                found.setVangNapTuWeb(amount);
            } else if ("ngocNapTuWeb".equals(type)) {
                found.setNgocNapTuWeb(amount);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Loại balance không hợp lệ!"));
            }

            userService.saveUser(found);

            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật balance thành công!",
                    "vangNapTuWeb", found.getVangNapTuWeb(),
                    "ngocNapTuWeb", found.getNgocNapTuWeb()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi cập nhật balance!"));
        }
    }

    // API để thêm vàng nạp từ web (dùng cho nạp tiền)
    @PostMapping(value = "/addVangNapTuWeb", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> addVangNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User không tồn tại!"));
        }

        if (amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Số tiền phải lớn hơn 0!"));
        }

        // Cộng thêm vàng nạp
        found.setVangNapTuWeb(found.getVangNapTuWeb() + amount);
        userService.saveUser(found);

        return ResponseEntity.ok(Map.of(
                "message", "Nạp vàng thành công!",
                "added", amount,
                "totalVangNapTuWeb", found.getVangNapTuWeb()
        ));
    }

    // API để thêm ngọc nạp từ web (dùng cho nạp tiền)
    @PostMapping(value = "/addNgocNapTuWeb", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> addNgocNapTuWeb(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        int amount = (int) request.get("amount");

        User found = userService.findByUsername(username);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User không tồn tại!"));
        }

        if (amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Số tiền phải lớn hơn 0!"));
        }

        // Cộng thêm ngọc nạp
        found.setNgocNapTuWeb(found.getNgocNapTuWeb() + amount);
        userService.saveUser(found);

        return ResponseEntity.ok(Map.of(
                "message", "Nạp ngọc thành công!",
                "added", amount,
                "totalNgocNapTuWeb", found.getNgocNapTuWeb()
        ));
    }

    @GetMapping("/top10")
    public ResponseEntity<List<User>> getTop10Users() {
        List<User> topUsers = userService.getTop10UsersBySucManh();
        return ResponseEntity.ok(topUsers);
    }
}