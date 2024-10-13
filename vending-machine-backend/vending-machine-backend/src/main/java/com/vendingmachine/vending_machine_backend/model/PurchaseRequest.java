package com.vendingmachine.vending_machine_backend.model;

import java.util.List;

public class PurchaseRequest {

    private List<ItemQuantity> items;
    private int payment;

    // Getters and setters
    public List<ItemQuantity> getItems() {
        return items;
    }

    public void setItems(List<ItemQuantity> items) {
        this.items = items;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}
