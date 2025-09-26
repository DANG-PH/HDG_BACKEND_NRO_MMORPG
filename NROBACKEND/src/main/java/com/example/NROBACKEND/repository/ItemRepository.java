package com.example.NROBACKEND.repository;

import com.example.NROBACKEND.entity.Item;
import com.example.NROBACKEND.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUser(User user);
    List<Item> findByUserId(Long userId);
    void deleteByUser(User user);
}