package com.vendingmachine.vending_machine_backend.repository;

import com.vendingmachine.vending_machine_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
}

