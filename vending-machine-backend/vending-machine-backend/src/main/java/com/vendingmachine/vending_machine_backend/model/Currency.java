package com.vendingmachine.vending_machine_backend.model;

public class Currency {
    private int denomination;
    private int count;

    // Getters and Setters
    public Currency(int denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    
    
}

