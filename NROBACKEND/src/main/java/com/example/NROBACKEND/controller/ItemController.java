package com.example.NROBACKEND.controller;

import com.example.NROBACKEND.entity.Item;
import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.service.ItemService;
import com.example.NROBACKEND.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    // Lấy toàn bộ item của 1 user
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getItemsByUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }
        List<Item> items = itemService.getItemsByUser(user);
        return ResponseEntity.ok(items);
    }

    // Thêm item cho user
    @PostMapping("/add/{username}")
    public ResponseEntity<?> addItem(@PathVariable String username, @RequestBody Item item) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }
        item.setUser(user);
        Item saved = itemService.saveItem(item);
        return ResponseEntity.ok(saved);
    }

    // Cập nhật item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item itemUpdate) {
        return itemService.getItem(id)
                .map(item -> {
                    item.setTen(itemUpdate.getTen());
                    item.setLoai(itemUpdate.getLoai());
                    item.setMoTa(itemUpdate.getMoTa());
                    item.setSoLuong(itemUpdate.getSoLuong());
                    item.setViTri(itemUpdate.getViTri());
                    item.setChiso(itemUpdate.getChiso());
                    return ResponseEntity.ok(itemService.saveItem(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Xóa item
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Xóa item thành công!");
    }

    @PostMapping("/add-multiple/{username}")
    @Transactional
    public ResponseEntity<?> addItems(@PathVariable String username, @RequestBody List<Item> items) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.badRequest().body("User không tồn tại!");
            }

            // Xóa item cũ
            itemService.deleteByUser(user);

            // Gán user cho từng item
            for (Item item : items) {
                item.setUser(user);
            }

            List<Item> savedItems = itemService.saveAll(items);
            return ResponseEntity.ok(savedItems);

        } catch (Exception e) {
            e.printStackTrace(); // In stacktrace trong console để debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lưu item: " + e.getMessage());
        }
    }
}