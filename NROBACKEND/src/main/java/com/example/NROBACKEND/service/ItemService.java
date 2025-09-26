package com.example.NROBACKEND.service;

import com.example.NROBACKEND.entity.Item;
import com.example.NROBACKEND.entity.User;
import com.example.NROBACKEND.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItemsByUser(User user) {
        return itemRepository.findByUser(user);
    }

    public List<Item> getItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    public Optional<Item> getItem(Long id) {
        return itemRepository.findById(id);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> saveAll(List<Item> items) {
        return itemRepository.saveAll(items);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public void deleteByUser(User user) {
        itemRepository.deleteByUser(user);
    }
    
    
}
