package com.vendingmachine.vending_machine_backend.model;

import javax.persistence.Embeddable;

@Embeddable
public class ItemQuantity {

    private String name;
    private int quantity;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
