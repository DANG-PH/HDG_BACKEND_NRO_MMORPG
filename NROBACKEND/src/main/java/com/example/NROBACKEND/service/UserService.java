package com.example.NROBACKEND.service;

import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection: Spring inject repo vào service.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        // Gọi repository.save -> JPA decide: INSERT (nếu id null) hoặc UPDATE (nếu id tồn tại).
        // Sau save, DB đã nhận dữ liệu (thực tế commit tùy config transaction), object User sẽ có id nếu là insert.
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); // SELECT * FROM users
    }

    // kiểm tra tồn tại username
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username); // SELECT COUNT...
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // SELECT * WHERE username = ?
    }

    public void updateVangNgoc(String username, Long vang, Long ngoc) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // thay đổi object trong heap
            user.setVang(vang);
            user.setNgoc(ngoc);
            // Lưu lại -> UPDATE vào DB
            userRepository.save(user);
        }
    }

    public List<User> getTop10UsersBySucManh() {
        return userRepository.findTop10ByOrderBySucManhDesc();
    }

    public List<User> getTop10UsersByVang() {
        return userRepository.findTop10ByOrderByVangDesc();
    }
}